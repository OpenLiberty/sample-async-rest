package io.openliberty.sample.async.rest.client.mprestclient;

import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import io.openliberty.sample.async.rest.LocalDateParamConverter;
import io.openliberty.sample.async.rest.model.Reservation;

@Path("/carRental")
@RegisterProvider(LocalDateParamConverter.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CarRentalServiceClient {

    @Path("/cost")
    @GET
    CompletionStage<Reservation> getReservation(@QueryParam("location") String location,
                                                @QueryParam("start") LocalDate startDate,
                                                @QueryParam("end") LocalDate endDate);
}
