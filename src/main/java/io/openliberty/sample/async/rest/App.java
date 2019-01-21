package io.openliberty.sample.async.rest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class App extends Application {
	public final static String PORT = System.getProperty("default.http.port");
	public final static String CONTEXT_ROOT = "/" + System.getProperty("app.context.root");
	public final static String AIRLINE_URL = "http://localhost:" + PORT + CONTEXT_ROOT + "/airline/cost";
	public final static String HOTEL_URL = "http://localhost:" + PORT + CONTEXT_ROOT + "/hotel/cost";
	public final static String RENTAL_URL = "http://localhost:" + PORT + CONTEXT_ROOT + "/carRental/cost";
	
	public final static ThreadLocal<String> LOYALTY_ID_THREADLOCAL = new ThreadLocal<>();
	
	private static ExecutorService executor = Executors.newFixedThreadPool(10);
	
	public static ExecutorService executorService() {
		return executor;
	}
}
