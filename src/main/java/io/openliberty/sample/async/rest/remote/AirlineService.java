package io.openliberty.sample.async.rest.remote;

import java.time.LocalDate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import io.openliberty.sample.async.rest.App;
import io.openliberty.sample.async.rest.model.Reservation;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/airline")
public class AirlineService {

	@Path("/cost")
	@GET
	public void getReservation(@QueryParam("from") String from, 
			                   @QueryParam("to") String to,
			                   @QueryParam("start") LocalDate start,
			                   @QueryParam("end") LocalDate end,
			                   @Suspended AsyncResponse ar) {

		App.executorService().execute( () -> {
			Reservation r = new Reservation();
			r.setDestination(to);
			r.setInitialLocation(from);
			r.setStartDate(start);
			r.setReturnDate(end);
			r.setCost(DBHelper.getCostOfAirlineReservation(from, to, start, end));
			
			ar.resume(r);
		});
		
	}
}
