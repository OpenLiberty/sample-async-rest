package io.openliberty.sample.async.rest;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class LoyaltyIDFilter implements ContainerRequestFilter, ClientRequestFilter {

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		String loyaltyId = App.LOYALTY_ID_THREADLOCAL.get();
		if (loyaltyId != null) {
			requestContext.getHeaders().putSingle("Loyalty-ID", loyaltyId);
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		System.out.println("ContainerRequestFilter.filter");
		String loyaltyId = requestContext.getHeaderString("Loyalty-ID");
		if (loyaltyId != null) {
			System.out.println("Setting threadLocal loyalty ID to: " + loyaltyId);
			App.LOYALTY_ID_THREADLOCAL.set(loyaltyId);
		}
	}

}
