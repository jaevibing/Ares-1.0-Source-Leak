package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;
import com.google.api.client.http.GenericUrl;

public class AuthorizationCodeResponseUrl extends GenericUrl
{
    @Key
    private String code;
    @Key
    private String state;
    @Key
    private String error;
    @Key("error_description")
    private String errorDescription;
    @Key("error_uri")
    private String errorUri;
    
    public AuthorizationCodeResponseUrl(final String a1) {
        super(a1);
        Preconditions.checkArgument(this.code == null != (this.error == null));
    }
    
    public final String getCode() {
        /*SL:108*/return this.code;
    }
    
    public AuthorizationCodeResponseUrl setCode(final String a1) {
        /*SL:120*/this.code = a1;
        /*SL:121*/return this;
    }
    
    public final String getState() {
        /*SL:129*/return this.state;
    }
    
    public AuthorizationCodeResponseUrl setState(final String a1) {
        /*SL:142*/this.state = a1;
        /*SL:143*/return this;
    }
    
    public final String getError() {
        /*SL:154*/return this.error;
    }
    
    public AuthorizationCodeResponseUrl setError(final String a1) {
        /*SL:170*/this.error = a1;
        /*SL:171*/return this;
    }
    
    public final String getErrorDescription() {
        /*SL:179*/return this.errorDescription;
    }
    
    public AuthorizationCodeResponseUrl setErrorDescription(final String a1) {
        /*SL:192*/this.errorDescription = a1;
        /*SL:193*/return this;
    }
    
    public final String getErrorUri() {
        /*SL:202*/return this.errorUri;
    }
    
    public AuthorizationCodeResponseUrl setErrorUri(final String a1) {
        /*SL:216*/this.errorUri = a1;
        /*SL:217*/return this;
    }
    
    @Override
    public AuthorizationCodeResponseUrl set(final String a1, final Object a2) {
        /*SL:222*/return (AuthorizationCodeResponseUrl)super.set(a1, a2);
    }
    
    @Override
    public AuthorizationCodeResponseUrl clone() {
        /*SL:227*/return (AuthorizationCodeResponseUrl)super.clone();
    }
}
