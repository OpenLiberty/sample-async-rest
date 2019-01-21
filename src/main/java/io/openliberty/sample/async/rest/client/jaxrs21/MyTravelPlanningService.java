package io.openliberty.sample.async.rest.client.jaxrs21;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.function.BiConsumer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import io.openliberty.sample.async.rest.App;
import io.openliberty.sample.async.rest.model.Reservation;

@Path("/jaxrs21/travel")
public class MyTravelPlanningService {

    private static WebTarget airlineWebTarget = ClientBuilder.newClient()
                                                             .target(App.AIRLINE_URL);

    private static WebTarget hotelWebTarget = ClientBuilder.newClient()
                                                           .target(App.HOTEL_URL);

    private static WebTarget carRentalWebTarget = ClientBuilder.newClient()
                                                               .target(App.RENTAL_URL);


    @Path("/cost")
    @GET
    public double getCostOfTravel(@QueryParam("from") String from,
                                  @QueryParam("to") String to,
                                  @QueryParam("startDate") LocalDate start,
                                  @QueryParam("returnDate") LocalDate end) {
    	System.out.println("JAXRS 2.1");
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

        airlineWebTarget.queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .request()
                        .rx()
                        .get(Reservation.class)
                        .whenCompleteAsync(consumer);
        
        hotelWebTarget.queryParam("location", to)
                      .queryParam("start", start)
                      .queryParam("end", end)
                      .request()
                      .rx()
                      .get(Reservation.class)
                      .whenCompleteAsync(consumer);

        carRentalWebTarget.queryParam("location", to)
                          .queryParam("start", start)
                          .queryParam("end", end)
                          .request()
                          .rx()
                          .get(Reservation.class)
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
