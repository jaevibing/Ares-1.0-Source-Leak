package com.google.api.client.http;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public abstract class HttpTransport
{
    static final Logger LOGGER;
    private static final String[] SUPPORTED_METHODS;
    
    public final HttpRequestFactory createRequestFactory() {
        /*SL:102*/return this.createRequestFactory(null);
    }
    
    public final HttpRequestFactory createRequestFactory(final HttpRequestInitializer a1) {
        /*SL:114*/return new HttpRequestFactory(this, a1);
    }
    
    HttpRequest buildRequest() {
        /*SL:123*/return new HttpRequest(this, null);
    }
    
    public boolean supportsMethod(final String a1) throws IOException {
        /*SL:139*/return Arrays.binarySearch(HttpTransport.SUPPORTED_METHODS, a1) >= 0;
    }
    
    protected abstract LowLevelHttpRequest buildRequest(final String p0, final String p1) throws IOException;
    
    public void shutdown() throws IOException {
    }
    
    static {
        LOGGER = Logger.getLogger(HttpTransport.class.getName());
        Arrays.sort(SUPPORTED_METHODS = new String[] { "DELETE", "GET", "POST", "PUT" });
    }
}
