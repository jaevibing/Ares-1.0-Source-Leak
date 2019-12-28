package com.google.api.client.http.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.Proxy;

public class DefaultConnectionFactory implements ConnectionFactory
{
    private final Proxy proxy;
    
    public DefaultConnectionFactory() {
        this(null);
    }
    
    public DefaultConnectionFactory(final Proxy a1) {
        this.proxy = a1;
    }
    
    @Override
    public HttpURLConnection openConnection(final URL a1) throws IOException {
        /*SL:31*/return (HttpURLConnection)((this.proxy == null) ? a1.openConnection() : a1.openConnection(this.proxy));
    }
}
