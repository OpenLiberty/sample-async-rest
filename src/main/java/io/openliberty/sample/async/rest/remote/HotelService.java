package io.openliberty.sample.async.rest.remote;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.openliberty.sample.async.rest.App;
import io.openliberty.sample.async.rest.model.Reservation;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/hotel")
public class HotelService {

    private final static AtomicBoolean INJECT_ERRORS = new AtomicBoolean(false);

    @Path("/cost")
    @GET
    public Reservation getReservation(@QueryParam("location") String location, 
                                      @QueryParam("start") LocalDate start,
                                      @QueryParam("end") LocalDate end) {

        if (INJECT_ERRORS.get()) {
            throw new InternalServerErrorException("Hotel service failure!");
        }

        Reservation r = new Reservation();
        r.setDestination(location);
        r.setStartDate(start);
        r.setReturnDate(end);
        r.setCost(DBHelper.getCostOfHotelReservation(location, start, end));
        
        // 10% loyalty discount
        if (App.LOYALTY_ID_THREADLOCAL.get() != null) {
        	r.setCost(r.getCost() * 0.9);
        }
        
        return r;
    }
    
    @Path("/injectErrors")
    @Produces(MediaType.TEXT_PLAIN)
    @PUT
    public String injectErrors() {
        INJECT_ERRORS.set(true);
        return "Hotel service is now experiencing technical difficulties.";
    }

    @Path("/fixErrors")
    @Produces(MediaType.TEXT_PLAIN)
    @PUT
    public String fixErrors() {
        INJECT_ERRORS.set(false);
        return "Hotel service is now back to normal operation.";
    }
}
