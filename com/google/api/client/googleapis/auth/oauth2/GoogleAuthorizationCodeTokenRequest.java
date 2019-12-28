package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenRequest;
import java.io.IOException;
import com.google.api.client.util.Preconditions;
import java.util.Collection;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;

public class GoogleAuthorizationCodeTokenRequest extends AuthorizationCodeTokenRequest
{
    public GoogleAuthorizationCodeTokenRequest(final HttpTransport a1, final JsonFactory a2, final String a3, final String a4, final String a5, final String a6) {
        this(a1, a2, "https://accounts.google.com/o/oauth2/token", a3, a4, a5, a6);
    }
    
    public GoogleAuthorizationCodeTokenRequest(final HttpTransport a1, final JsonFactory a2, final String a3, final String a4, final String a5, final String a6, final String a7) {
        super(a1, a2, new GenericUrl(a3), a6);
        this.setClientAuthentication(new ClientParametersAuthentication(a4, a5));
        this.setRedirectUri(a7);
    }
    
    public GoogleAuthorizationCodeTokenRequest setRequestInitializer(final HttpRequestInitializer a1) {
        /*SL:119*/return (GoogleAuthorizationCodeTokenRequest)super.setRequestInitializer(a1);
    }
    
    public GoogleAuthorizationCodeTokenRequest setTokenServerUrl(final GenericUrl a1) {
        /*SL:124*/return (GoogleAuthorizationCodeTokenRequest)super.setTokenServerUrl(a1);
    }
    
    public GoogleAuthorizationCodeTokenRequest setScopes(final Collection<String> a1) {
        /*SL:129*/return (GoogleAuthorizationCodeTokenRequest)super.setScopes(a1);
    }
    
    public GoogleAuthorizationCodeTokenRequest setGrantType(final String a1) {
        /*SL:134*/return (GoogleAuthorizationCodeTokenRequest)super.setGrantType(a1);
    }
    
    public GoogleAuthorizationCodeTokenRequest setClientAuthentication(final HttpExecuteInterceptor a1) {
        /*SL:140*/Preconditions.<HttpExecuteInterceptor>checkNotNull(a1);
        /*SL:141*/return (GoogleAuthorizationCodeTokenRequest)super.setClientAuthentication(a1);
    }
    
    public GoogleAuthorizationCodeTokenRequest setCode(final String a1) {
        /*SL:147*/return (GoogleAuthorizationCodeTokenRequest)super.setCode(a1);
    }
    
    public GoogleAuthorizationCodeTokenRequest setRedirectUri(final String a1) {
        /*SL:152*/Preconditions.<String>checkNotNull(a1);
        /*SL:153*/return (GoogleAuthorizationCodeTokenRequest)super.setRedirectUri(a1);
    }
    
    public GoogleTokenResponse execute() throws IOException {
        /*SL:158*/return this.executeUnparsed().<GoogleTokenResponse>parseAs(GoogleTokenResponse.class);
    }
    
    public GoogleAuthorizationCodeTokenRequest set(final String a1, final Object a2) {
        /*SL:163*/return (GoogleAuthorizationCodeTokenRequest)super.set(a1, a2);
    }
}
