package com.google.api.client.http;

import java.io.IOException;

public final class HttpRequestFactory
{
    private final HttpTransport transport;
    private final HttpRequestInitializer initializer;
    
    HttpRequestFactory(final HttpTransport a1, final HttpRequestInitializer a2) {
        this.transport = a1;
        this.initializer = a2;
    }
    
    public HttpTransport getTransport() {
        /*SL:64*/return this.transport;
    }
    
    public HttpRequestInitializer getInitializer() {
        /*SL:77*/return this.initializer;
    }
    
    public HttpRequest buildRequest(final String a1, final GenericUrl a2, final HttpContent a3) throws IOException {
        final HttpRequest v1 = /*EL:91*/this.transport.buildRequest();
        /*SL:92*/if (this.initializer != null) {
            /*SL:93*/this.initializer.initialize(v1);
        }
        /*SL:95*/v1.setRequestMethod(a1);
        /*SL:96*/if (a2 != null) {
            /*SL:97*/v1.setUrl(a2);
        }
        /*SL:99*/if (a3 != null) {
            /*SL:100*/v1.setContent(a3);
        }
        /*SL:102*/return v1;
    }
    
    public HttpRequest buildDeleteRequest(final GenericUrl a1) throws IOException {
        /*SL:112*/return this.buildRequest("DELETE", a1, null);
    }
    
    public HttpRequest buildGetRequest(final GenericUrl a1) throws IOException {
        /*SL:122*/return this.buildRequest("GET", a1, null);
    }
    
    public HttpRequest buildPostRequest(final GenericUrl a1, final HttpContent a2) throws IOException {
        /*SL:133*/return this.buildRequest("POST", a1, a2);
    }
    
    public HttpRequest buildPutRequest(final GenericUrl a1, final HttpContent a2) throws IOException {
        /*SL:144*/return this.buildRequest("PUT", a1, a2);
    }
    
    public HttpRequest buildPatchRequest(final GenericUrl a1, final HttpContent a2) throws IOException {
        /*SL:155*/return this.buildRequest("PATCH", a1, a2);
    }
    
    public HttpRequest buildHeadRequest(final GenericUrl a1) throws IOException {
        /*SL:165*/return this.buildRequest("HEAD", a1, null);
    }
}
