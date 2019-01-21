package io.openliberty.sample.async.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.ext.ParamConverter;

public class LocalDateParamConverter implements ParamConverter<LocalDate> {

	DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
	@Override
	public LocalDate fromString(String value) {
		return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	@Override
	public String toString(LocalDate value) {
		return value.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

}
