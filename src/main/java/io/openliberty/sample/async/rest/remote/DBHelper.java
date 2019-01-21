package io.openliberty.sample.async.rest.remote;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

public class DBHelper {
	
	private static Map<String, Double> airlineCosts = new HashMap<>();
	private static Map<String, Double> hotelCosts = new HashMap<>();
	private static Map<String, Double> carRentalCosts = new HashMap<>();

	static {
		airlineCosts.put("New York/Kansas City", 300.00);
		hotelCosts.put("Kansas City", 135.50);
		carRentalCosts.put("Kansas City", 34.99);
	}

	public static double getCostOfAirlineReservation(String from, 
			                                         String to, 
			                                         LocalDate startDate, 
			                                         LocalDate endDate) {

		sleep(500); // simulate DB call
		return airlineCosts.getOrDefault(from + "/" + to, 500.00);
	}

	public static double getCostOfHotelReservation(String location, 
			                                       LocalDate startDate, 
			                                       LocalDate endDate) {

		sleep(600); // simulate DB call
		long days = Period.between(startDate, endDate).getDays();
		return days * hotelCosts.getOrDefault(location, 200.00);
	}
	
	public static double getCostOfCarRentalReservation(String location, 
			LocalDate startDate, 
			LocalDate endDate) {

		sleep(700); // simulate DB call
		long days = Period.between(startDate, endDate).getDays();
		return days * carRentalCosts.getOrDefault(location, 50.00);
	}
	
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
