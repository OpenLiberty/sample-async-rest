package io.openliberty.sample.async.rest.model;

import java.time.LocalDate;

public class Reservation {

	private String initialLocation;
	private String destination;
	private LocalDate startDate;
	private LocalDate returnDate;
	private double cost;
	
	public String getInitialLocation() {
		return initialLocation;
	}
	public void setInitialLocation(String initialLocation) {
		this.initialLocation = initialLocation;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
}
