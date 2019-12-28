package com.google.api.client.auth.openidconnect;

import com.google.api.client.util.GenericData;
import com.google.api.client.json.GenericJson;
import com.google.api.client.auth.oauth2.TokenRequest;
import java.io.IOException;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;
import com.google.api.client.util.Beta;
import com.google.api.client.auth.oauth2.TokenResponse;

@Beta
public class IdTokenResponse extends TokenResponse
{
    @Key("id_token")
    private String idToken;
    
    public final String getIdToken() {
        /*SL:57*/return this.idToken;
    }
    
    public IdTokenResponse setIdToken(final String a1) {
        /*SL:69*/this.idToken = Preconditions.<String>checkNotNull(a1);
        /*SL:70*/return this;
    }
    
    @Override
    public IdTokenResponse setAccessToken(final String a1) {
        /*SL:75*/super.setAccessToken(a1);
        /*SL:76*/return this;
    }
    
    @Override
    public IdTokenResponse setTokenType(final String a1) {
        /*SL:81*/super.setTokenType(a1);
        /*SL:82*/return this;
    }
    
    @Override
    public IdTokenResponse setExpiresInSeconds(final Long a1) {
        /*SL:87*/super.setExpiresInSeconds(a1);
        /*SL:88*/return this;
    }
    
    @Override
    public IdTokenResponse setRefreshToken(final String a1) {
        /*SL:93*/super.setRefreshToken(a1);
        /*SL:94*/return this;
    }
    
    @Override
    public IdTokenResponse setScope(final String a1) {
        /*SL:99*/super.setScope(a1);
        /*SL:100*/return this;
    }
    
    public IdToken parseIdToken() throws IOException {
        /*SL:109*/return IdToken.parse(this.getFactory(), this.idToken);
    }
    
    public static IdTokenResponse execute(final TokenRequest a1) throws IOException {
        /*SL:120*/return a1.executeUnparsed().<IdTokenResponse>parseAs(IdTokenResponse.class);
    }
    
    @Override
    public IdTokenResponse set(final String a1, final Object a2) {
        /*SL:125*/return (IdTokenResponse)super.set(a1, a2);
    }
    
    @Override
    public IdTokenResponse clone() {
        /*SL:130*/return (IdTokenResponse)super.clone();
    }
}
