package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.http.GenericUrl;
import java.util.Collection;
import java.util.Collections;

public class AuthorizationCodeRequestUrl extends AuthorizationRequestUrl
{
    public AuthorizationCodeRequestUrl(final String a1, final String a2) {
        super(a1, a2, Collections.<String>singleton("code"));
    }
    
    @Override
    public AuthorizationCodeRequestUrl setResponseTypes(final Collection<String> a1) {
        /*SL:65*/return (AuthorizationCodeRequestUrl)super.setResponseTypes(a1);
    }
    
    @Override
    public AuthorizationCodeRequestUrl setRedirectUri(final String a1) {
        /*SL:70*/return (AuthorizationCodeRequestUrl)super.setRedirectUri(a1);
    }
    
    @Override
    public AuthorizationCodeRequestUrl setScopes(final Collection<String> a1) {
        /*SL:75*/return (AuthorizationCodeRequestUrl)super.setScopes(a1);
    }
    
    @Override
    public AuthorizationCodeRequestUrl setClientId(final String a1) {
        /*SL:80*/return (AuthorizationCodeRequestUrl)super.setClientId(a1);
    }
    
    @Override
    public AuthorizationCodeRequestUrl setState(final String a1) {
        /*SL:85*/return (AuthorizationCodeRequestUrl)super.setState(a1);
    }
    
    @Override
    public AuthorizationCodeRequestUrl set(final String a1, final Object a2) {
        /*SL:90*/return (AuthorizationCodeRequestUrl)super.set(a1, a2);
    }
    
    @Override
    public AuthorizationCodeRequestUrl clone() {
        /*SL:95*/return (AuthorizationCodeRequestUrl)super.clone();
    }
}
