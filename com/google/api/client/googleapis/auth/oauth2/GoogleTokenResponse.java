package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.json.GenericJson;
import java.io.IOException;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Key;
import com.google.api.client.auth.oauth2.TokenResponse;

public class GoogleTokenResponse extends TokenResponse
{
    @Key("id_token")
    private String idToken;
    
    public GoogleTokenResponse setAccessToken(final String a1) {
        /*SL:52*/return (GoogleTokenResponse)super.setAccessToken(a1);
    }
    
    public GoogleTokenResponse setTokenType(final String a1) {
        /*SL:57*/return (GoogleTokenResponse)super.setTokenType(a1);
    }
    
    public GoogleTokenResponse setExpiresInSeconds(final Long a1) {
        /*SL:62*/return (GoogleTokenResponse)super.setExpiresInSeconds(a1);
    }
    
    public GoogleTokenResponse setRefreshToken(final String a1) {
        /*SL:67*/return (GoogleTokenResponse)super.setRefreshToken(a1);
    }
    
    public GoogleTokenResponse setScope(final String a1) {
        /*SL:72*/return (GoogleTokenResponse)super.setScope(a1);
    }
    
    @Beta
    public final String getIdToken() {
        /*SL:81*/return this.idToken;
    }
    
    @Beta
    public GoogleTokenResponse setIdToken(final String a1) {
        /*SL:95*/this.idToken = Preconditions.<String>checkNotNull(a1);
        /*SL:96*/return this;
    }
    
    @Beta
    public GoogleIdToken parseIdToken() throws IOException {
        /*SL:106*/return GoogleIdToken.parse(this.getFactory(), this.getIdToken());
    }
    
    public GoogleTokenResponse set(final String a1, final Object a2) {
        /*SL:111*/return (GoogleTokenResponse)super.set(a1, a2);
    }
    
    public GoogleTokenResponse clone() {
        /*SL:116*/return (GoogleTokenResponse)super.clone();
    }
}
