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

public class AuthorizationCodeTokenRequest extends TokenRequest
{
    @Key
    private String code;
    @Key("redirect_uri")
    private String redirectUri;
    
    public AuthorizationCodeTokenRequest(final HttpTransport a1, final JsonFactory a2, final GenericUrl a3, final String a4) {
        super(a1, a2, a3, "authorization_code");
        this.setCode(a4);
    }
    
    @Override
    public AuthorizationCodeTokenRequest setRequestInitializer(final HttpRequestInitializer a1) {
        /*SL:109*/return (AuthorizationCodeTokenRequest)super.setRequestInitializer(a1);
    }
    
    @Override
    public AuthorizationCodeTokenRequest setTokenServerUrl(final GenericUrl a1) {
        /*SL:114*/return (AuthorizationCodeTokenRequest)super.setTokenServerUrl(a1);
    }
    
    @Override
    public AuthorizationCodeTokenRequest setScopes(final Collection<String> a1) {
        /*SL:119*/return (AuthorizationCodeTokenRequest)super.setScopes(a1);
    }
    
    @Override
    public AuthorizationCodeTokenRequest setGrantType(final String a1) {
        /*SL:124*/return (AuthorizationCodeTokenRequest)super.setGrantType(a1);
    }
    
    @Override
    public AuthorizationCodeTokenRequest setClientAuthentication(final HttpExecuteInterceptor a1) {
        /*SL:130*/return (AuthorizationCodeTokenRequest)super.setClientAuthentication(a1);
    }
    
    public final String getCode() {
        /*SL:135*/return this.code;
    }
    
    public AuthorizationCodeTokenRequest setCode(final String a1) {
        /*SL:147*/this.code = Preconditions.<String>checkNotNull(a1);
        /*SL:148*/return this;
    }
    
    public final String getRedirectUri() {
        /*SL:156*/return this.redirectUri;
    }
    
    public AuthorizationCodeTokenRequest setRedirectUri(final String a1) {
        /*SL:169*/this.redirectUri = a1;
        /*SL:170*/return this;
    }
    
    @Override
    public AuthorizationCodeTokenRequest set(final String a1, final Object a2) {
        /*SL:175*/return (AuthorizationCodeTokenRequest)super.set(a1, a2);
    }
}
