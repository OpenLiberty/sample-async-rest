package io.openliberty.sample.async.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class LocalDateParamConverterProvider implements ParamConverterProvider {
	private LocalDateParamConverter converterInstance = new LocalDateParamConverter();

	@SuppressWarnings("unchecked")
	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if (LocalDate.class.equals(rawType)) {
			return (ParamConverter<T>) converterInstance;
		}
		return null;
	}

}
