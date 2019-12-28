package com.google.api.client.googleapis.testing.services;

import com.google.api.client.util.GenericData;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpContent;
import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.util.Beta;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;

@Beta
public class MockGoogleClientRequest<T> extends AbstractGoogleClientRequest<T>
{
    public MockGoogleClientRequest(final AbstractGoogleClient a1, final String a2, final String a3, final HttpContent a4, final Class<T> a5) {
        super(a1, a2, a3, a4, a5);
    }
    
    public MockGoogleClientRequest<T> setDisableGZipContent(final boolean a1) {
        /*SL:50*/return (MockGoogleClientRequest)super.setDisableGZipContent(a1);
    }
    
    public MockGoogleClientRequest<T> setRequestHeaders(final HttpHeaders a1) {
        /*SL:55*/return (MockGoogleClientRequest)super.setRequestHeaders(a1);
    }
    
    public MockGoogleClientRequest<T> set(final String a1, final Object a2) {
        /*SL:60*/return (MockGoogleClientRequest)super.set(a1, a2);
    }
}
