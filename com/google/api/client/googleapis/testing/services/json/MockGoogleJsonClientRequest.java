package com.google.api.client.googleapis.testing.services.json;

import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.util.Beta;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;

@Beta
public class MockGoogleJsonClientRequest<T> extends AbstractGoogleJsonClientRequest<T>
{
    public MockGoogleJsonClientRequest(final AbstractGoogleJsonClient a1, final String a2, final String a3, final Object a4, final Class<T> a5) {
        super(a1, a2, a3, a4, a5);
    }
    
    public MockGoogleJsonClient getAbstractGoogleClient() {
        /*SL:48*/return (MockGoogleJsonClient)super.getAbstractGoogleClient();
    }
    
    public MockGoogleJsonClientRequest<T> setDisableGZipContent(final boolean a1) {
        /*SL:53*/return (MockGoogleJsonClientRequest)super.setDisableGZipContent(a1);
    }
    
    public MockGoogleJsonClientRequest<T> setRequestHeaders(final HttpHeaders a1) {
        /*SL:58*/return (MockGoogleJsonClientRequest)super.setRequestHeaders(a1);
    }
}
