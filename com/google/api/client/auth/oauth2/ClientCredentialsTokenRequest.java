package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.http.HttpExecuteInterceptor;
import java.util.Collection;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;

public class ClientCredentialsTokenRequest extends TokenRequest
{
    public ClientCredentialsTokenRequest(final HttpTransport a1, final JsonFactory a2, final GenericUrl a3) {
        super(a1, a2, a3, "client_credentials");
    }
    
    @Override
    public ClientCredentialsTokenRequest setRequestInitializer(final HttpRequestInitializer a1) {
        /*SL:94*/return (ClientCredentialsTokenRequest)super.setRequestInitializer(a1);
    }
    
    @Override
    public ClientCredentialsTokenRequest setTokenServerUrl(final GenericUrl a1) {
        /*SL:99*/return (ClientCredentialsTokenRequest)super.setTokenServerUrl(a1);
    }
    
    @Override
    public ClientCredentialsTokenRequest setScopes(final Collection<String> a1) {
        /*SL:104*/return (ClientCredentialsTokenRequest)super.setScopes(a1);
    }
    
    @Override
    public ClientCredentialsTokenRequest setGrantType(final String a1) {
        /*SL:109*/return (ClientCredentialsTokenRequest)super.setGrantType(a1);
    }
    
    @Override
    public ClientCredentialsTokenRequest setClientAuthentication(final HttpExecuteInterceptor a1) {
        /*SL:115*/return (ClientCredentialsTokenRequest)super.setClientAuthentication(a1);
    }
    
    @Override
    public ClientCredentialsTokenRequest set(final String a1, final Object a2) {
        /*SL:120*/return (ClientCredentialsTokenRequest)super.set(a1, a2);
    }
}
