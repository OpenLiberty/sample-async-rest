package io.openliberty.sample.async.rest.client.jaxrs20;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.DoubleAdder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;

import io.openliberty.sample.async.rest.App;
import io.openliberty.sample.async.rest.model.Reservation;

@Path("/jaxrs20/travel")
public class MyTravelPlanningService {

    private static WebTarget airlineWebTarget = ClientBuilder.newClient()
                                                             .target(App.AIRLINE_URL);

    private static WebTarget hotelWebTarget = ClientBuilder.newClient()
                                                           .target(App.HOTEL_URL);

    private static WebTarget carRentalWebTarget = ClientBuilder.newClient()
                                                               .target(App.RENTAL_URL);

    private class ReservationInvocationCallback implements InvocationCallback<Reservation> {
        private final CountDownLatch latch;
        private final DoubleAdder costAdder;
        Throwable throwable;

        ReservationInvocationCallback(CountDownLatch latch, DoubleAdder adder) {
            this.latch = latch;
            this.costAdder = adder;
        }

        @Override
        public void completed(Reservation r) {
            costAdder.add(r.getCost());
            latch.countDown();
        }

        @Override
        public void failed(Throwable t) {
            t.printStackTrace();
            throwable = t;
            latch.countDown();
        }
    }

    @Path("/cost")
    @GET
    public double getCostOfTravel(@QueryParam("from") String from,
                                  @QueryParam("to") String to,
                                  @QueryParam("startDate") LocalDate start,
                                  @QueryParam("returnDate") LocalDate end) {

    	System.out.println("JAXRS 2.0");
        final CountDownLatch latch = new CountDownLatch(3);
        final DoubleAdder cost = new DoubleAdder();
        ReservationInvocationCallback callback = new ReservationInvocationCallback(latch, cost);
        airlineWebTarget.queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .request()
                        .async()
                        .get(callback);
        hotelWebTarget.queryParam("location", to)
                      .queryParam("start", start)
                      .queryParam("end", end)
                      .request()
                      .async()
                      .get(callback);
        carRentalWebTarget.queryParam("location", to)
                          .queryParam("start", start)
                          .queryParam("end", end)
                          .request()
                          .async()
                          .get(callback);

        try {
        	latch.await();
        } catch (InterruptedException ex) {
        	throw new WebApplicationException(ex, 500);
        }
        if (callback.throwable != null) {
        	callback.throwable.printStackTrace();
            throw new WebApplicationException("Failure in downstream service",
            		callback.throwable, 500);
        }
        return cost.doubleValue();
    }
}
