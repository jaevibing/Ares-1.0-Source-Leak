package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpExecuteInterceptor;
import java.util.Collection;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Key;

public class RefreshTokenRequest extends TokenRequest
{
    @Key("refresh_token")
    private String refreshToken;
    
    public RefreshTokenRequest(final HttpTransport a1, final JsonFactory a2, final GenericUrl a3, final String a4) {
        super(a1, a2, a3, "refresh_token");
        this.setRefreshToken(a4);
    }
    
    @Override
    public RefreshTokenRequest setRequestInitializer(final HttpRequestInitializer a1) {
        /*SL:100*/return (RefreshTokenRequest)super.setRequestInitializer(a1);
    }
    
    @Override
    public RefreshTokenRequest setTokenServerUrl(final GenericUrl a1) {
        /*SL:105*/return (RefreshTokenRequest)super.setTokenServerUrl(a1);
    }
    
    @Override
    public RefreshTokenRequest setScopes(final Collection<String> a1) {
        /*SL:110*/return (RefreshTokenRequest)super.setScopes(a1);
    }
    
    @Override
    public RefreshTokenRequest setGrantType(final String a1) {
        /*SL:115*/return (RefreshTokenRequest)super.setGrantType(a1);
    }
    
    @Override
    public RefreshTokenRequest setClientAuthentication(final HttpExecuteInterceptor a1) {
        /*SL:120*/return (RefreshTokenRequest)super.setClientAuthentication(a1);
    }
    
    public final String getRefreshToken() {
        /*SL:125*/return this.refreshToken;
    }
    
    public RefreshTokenRequest setRefreshToken(final String a1) {
        /*SL:137*/this.refreshToken = Preconditions.<String>checkNotNull(a1);
        /*SL:138*/return this;
    }
    
    @Override
    public RefreshTokenRequest set(final String a1, final Object a2) {
        /*SL:143*/return (RefreshTokenRequest)super.set(a1, a2);
    }
}
