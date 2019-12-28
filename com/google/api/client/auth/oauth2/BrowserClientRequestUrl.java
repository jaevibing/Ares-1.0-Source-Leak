package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.http.GenericUrl;
import java.util.Collection;
import java.util.Collections;

public class BrowserClientRequestUrl extends AuthorizationRequestUrl
{
    public BrowserClientRequestUrl(final String a1, final String a2) {
        super(a1, a2, Collections.<String>singleton("token"));
    }
    
    @Override
    public BrowserClientRequestUrl setResponseTypes(final Collection<String> a1) {
        /*SL:62*/return (BrowserClientRequestUrl)super.setResponseTypes(a1);
    }
    
    @Override
    public BrowserClientRequestUrl setRedirectUri(final String a1) {
        /*SL:67*/return (BrowserClientRequestUrl)super.setRedirectUri(a1);
    }
    
    @Override
    public BrowserClientRequestUrl setScopes(final Collection<String> a1) {
        /*SL:72*/return (BrowserClientRequestUrl)super.setScopes(a1);
    }
    
    @Override
    public BrowserClientRequestUrl setClientId(final String a1) {
        /*SL:77*/return (BrowserClientRequestUrl)super.setClientId(a1);
    }
    
    @Override
    public BrowserClientRequestUrl setState(final String a1) {
        /*SL:82*/return (BrowserClientRequestUrl)super.setState(a1);
    }
    
    @Override
    public BrowserClientRequestUrl set(final String a1, final Object a2) {
        /*SL:87*/return (BrowserClientRequestUrl)super.set(a1, a2);
    }
    
    @Override
    public BrowserClientRequestUrl clone() {
        /*SL:92*/return (BrowserClientRequestUrl)super.clone();
    }
}
