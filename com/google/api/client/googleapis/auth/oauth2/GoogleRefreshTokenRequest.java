package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenRequest;
import java.io.IOException;
import java.util.Collection;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;

public class GoogleRefreshTokenRequest extends RefreshTokenRequest
{
    public GoogleRefreshTokenRequest(final HttpTransport a1, final JsonFactory a2, final String a3, final String a4, final String a5) {
        super(a1, a2, new GenericUrl("https://accounts.google.com/o/oauth2/token"), a3);
        this.setClientAuthentication(new ClientParametersAuthentication(a4, a5));
    }
    
    public GoogleRefreshTokenRequest setRequestInitializer(final HttpRequestInitializer a1) {
        /*SL:94*/return (GoogleRefreshTokenRequest)super.setRequestInitializer(a1);
    }
    
    public GoogleRefreshTokenRequest setTokenServerUrl(final GenericUrl a1) {
        /*SL:99*/return (GoogleRefreshTokenRequest)super.setTokenServerUrl(a1);
    }
    
    public GoogleRefreshTokenRequest setScopes(final Collection<String> a1) {
        /*SL:104*/return (GoogleRefreshTokenRequest)super.setScopes(a1);
    }
    
    public GoogleRefreshTokenRequest setGrantType(final String a1) {
        /*SL:109*/return (GoogleRefreshTokenRequest)super.setGrantType(a1);
    }
    
    public GoogleRefreshTokenRequest setClientAuthentication(final HttpExecuteInterceptor a1) {
        /*SL:115*/return (GoogleRefreshTokenRequest)super.setClientAuthentication(a1);
    }
    
    public GoogleRefreshTokenRequest setRefreshToken(final String a1) {
        /*SL:120*/return (GoogleRefreshTokenRequest)super.setRefreshToken(a1);
    }
    
    public GoogleTokenResponse execute() throws IOException {
        /*SL:125*/return this.executeUnparsed().<GoogleTokenResponse>parseAs(GoogleTokenResponse.class);
    }
    
    public GoogleRefreshTokenRequest set(final String a1, final Object a2) {
        /*SL:130*/return (GoogleRefreshTokenRequest)super.set(a1, a2);
    }
}
