package io.openliberty.sample.async.rest.remote;

import java.time.LocalDate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.openliberty.sample.async.rest.model.Reservation;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/carRental")
public class CarRentalService {

	@Path("/cost")
	@GET
	public Reservation getReservation(@QueryParam("location") String location, 
			                          @QueryParam("start") LocalDate start,
			                          @QueryParam("end") LocalDate end) {

		Reservation r = new Reservation();
		r.setDestination(location);
		r.setStartDate(start);
		r.setReturnDate(end);
		r.setCost(DBHelper.getCostOfCarRentalReservation(location, start, end));
		
		return r;
	}
}
