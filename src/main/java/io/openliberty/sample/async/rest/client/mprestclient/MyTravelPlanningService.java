package io.openliberty.sample.async.rest.client.mprestclient;

import java.net.URI;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.function.BiConsumer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import io.openliberty.sample.async.rest.App;
import io.openliberty.sample.async.rest.model.Reservation;

@Path("/mpRest/travel")
public class MyTravelPlanningService {
    private final static String BASE_URI = "http://localhost:" + App.PORT + App.CONTEXT_ROOT;

    private final static AirlineServiceClient AIRLINE_CLIENT = RestClientBuilder.newBuilder()
                                                                                .baseUri(URI.create(BASE_URI))
                                                                                .build(AirlineServiceClient.class);
    private final static HotelServiceClient HOTEL_CLIENT = RestClientBuilder.newBuilder()
                                                                            .baseUri(URI.create(BASE_URI))
                                                                            .build(HotelServiceClient.class);
    private final static CarRentalServiceClient CAR_RENTAL_CLIENT = RestClientBuilder.newBuilder()
                                                                                     .baseUri(URI.create(BASE_URI))
                                                                                     .build(CarRentalServiceClient.class);

    @Path("/cost")
    @GET
    public double getCostOfTravel(@QueryParam("from") String from,
                                  @QueryParam("to") String to,
                                  @QueryParam("startDate") LocalDate start,
                                  @QueryParam("returnDate") LocalDate end) {
        System.out.println("MP Rest Client");
        final CountDownLatch latch = new CountDownLatch(3);
        final DoubleAdder cost = new DoubleAdder();
        final AtomicReference<Throwable> throwable = new AtomicReference<>();
        
        BiConsumer<Reservation, Throwable> consumer = (r, t) -> {
            if (t != null) {
                throwable.set(t);
            } else {
                cost.add(r.getCost());
            }
            latch.countDown();
        };

        AIRLINE_CLIENT.getReservation(from, to, start, end)
                      .whenCompleteAsync(consumer);

        HOTEL_CLIENT.getReservation(to, start, end)
                    .whenCompleteAsync(consumer);

        CAR_RENTAL_CLIENT.getReservation(to, start, end)
                         .whenCompleteAsync(consumer);

        try {
            latch.await();
        } catch (InterruptedException ex) {
            throw new WebApplicationException(ex, 500);
        }
        
        Throwable t = throwable.get();
        if (t != null) {
            throw new WebApplicationException("Failure in downstream service",
                                              t, 500);
        }
        return cost.doubleValue();
    }
}
