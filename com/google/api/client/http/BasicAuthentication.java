package com.google.api.client.http;

import java.io.IOException;
import com.google.api.client.util.Preconditions;

public final class BasicAuthentication implements HttpRequestInitializer, HttpExecuteInterceptor
{
    private final String username;
    private final String password;
    
    public BasicAuthentication(final String a1, final String a2) {
        this.username = Preconditions.<String>checkNotNull(a1);
        this.password = Preconditions.<String>checkNotNull(a2);
    }
    
    @Override
    public void initialize(final HttpRequest a1) throws IOException {
        /*SL:47*/a1.setInterceptor(this);
    }
    
    @Override
    public void intercept(final HttpRequest a1) throws IOException {
        /*SL:51*/a1.getHeaders().setBasicAuthentication(this.username, this.password);
    }
    
    public String getUsername() {
        /*SL:56*/return this.username;
    }
    
    public String getPassword() {
        /*SL:61*/return this.password;
    }
}
