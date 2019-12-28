package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public class TokenErrorResponse extends GenericJson
{
    @Key
    private String error;
    @Key("error_description")
    private String errorDescription;
    @Key("error_uri")
    private String errorUri;
    
    public final String getError() {
        /*SL:64*/return this.error;
    }
    
    public TokenErrorResponse setError(final String a1) {
        /*SL:79*/this.error = Preconditions.<String>checkNotNull(a1);
        /*SL:80*/return this;
    }
    
    public final String getErrorDescription() {
        /*SL:88*/return this.errorDescription;
    }
    
    public TokenErrorResponse setErrorDescription(final String a1) {
        /*SL:101*/this.errorDescription = a1;
        /*SL:102*/return this;
    }
    
    public final String getErrorUri() {
        /*SL:111*/return this.errorUri;
    }
    
    public TokenErrorResponse setErrorUri(final String a1) {
        /*SL:125*/this.errorUri = a1;
        /*SL:126*/return this;
    }
    
    @Override
    public TokenErrorResponse set(final String a1, final Object a2) {
        /*SL:131*/return (TokenErrorResponse)super.set(a1, a2);
    }
    
    @Override
    public TokenErrorResponse clone() {
        /*SL:136*/return (TokenErrorResponse)super.clone();
    }
}
