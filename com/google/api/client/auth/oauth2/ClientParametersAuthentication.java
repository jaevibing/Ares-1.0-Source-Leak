package com.google.api.client.auth.oauth2;

import java.util.Map;
import com.google.api.client.util.Data;
import com.google.api.client.http.UrlEncodedContent;
import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;

public class ClientParametersAuthentication implements HttpRequestInitializer, HttpExecuteInterceptor
{
    private final String clientId;
    private final String clientSecret;
    
    public ClientParametersAuthentication(final String a1, final String a2) {
        this.clientId = Preconditions.<String>checkNotNull(a1);
        this.clientSecret = a2;
    }
    
    @Override
    public void initialize(final HttpRequest a1) throws IOException {
        /*SL:94*/a1.setInterceptor(this);
    }
    
    @Override
    public void intercept(final HttpRequest a1) throws IOException {
        final Map<String, Object> v1 = /*EL:98*/Data.mapOf(UrlEncodedContent.getContent(a1).getData());
        /*SL:99*/v1.put("client_id", this.clientId);
        /*SL:100*/if (this.clientSecret != null) {
            /*SL:101*/v1.put("client_secret", this.clientSecret);
        }
    }
    
    public final String getClientId() {
        /*SL:107*/return this.clientId;
    }
    
    public final String getClientSecret() {
        /*SL:112*/return this.clientSecret;
    }
}
