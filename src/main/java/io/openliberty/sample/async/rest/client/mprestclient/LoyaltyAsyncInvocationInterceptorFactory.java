package io.openliberty.sample.async.rest.client.mprestclient;

import org.eclipse.microprofile.rest.client.ext.AsyncInvocationInterceptor;
import org.eclipse.microprofile.rest.client.ext.AsyncInvocationInterceptorFactory;

import io.openliberty.sample.async.rest.App;

public class LoyaltyAsyncInvocationInterceptorFactory implements AsyncInvocationInterceptorFactory {

    @Override
    public AsyncInvocationInterceptor newInterceptor() {
        return new AsyncInvocationInterceptor() {

            String loyaltyId;
            
            @Override
            public void prepareContext() {
                loyaltyId = App.LOYALTY_ID_THREADLOCAL.get();
            }

            @Override
            public void applyContext() {
                App.LOYALTY_ID_THREADLOCAL.set(loyaltyId);
            }};
    }

}
