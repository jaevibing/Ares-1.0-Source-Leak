package com.google.api.client.testing.http;

import java.io.IOException;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.Beta;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;

@Beta
public class MockHttpUnsuccessfulResponseHandler implements HttpUnsuccessfulResponseHandler
{
    private boolean isCalled;
    private boolean successfullyHandleResponse;
    
    public MockHttpUnsuccessfulResponseHandler(final boolean a1) {
        this.successfullyHandleResponse = a1;
    }
    
    public boolean isCalled() {
        /*SL:54*/return this.isCalled;
    }
    
    @Override
    public boolean handleResponse(final HttpRequest a1, final HttpResponse a2, final boolean a3) throws IOException {
        /*SL:59*/this.isCalled = true;
        /*SL:60*/return this.successfullyHandleResponse;
    }
}
