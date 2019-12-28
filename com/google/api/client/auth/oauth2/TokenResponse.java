package com.google.api.client.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public class TokenResponse extends GenericJson
{
    @Key("access_token")
    private String accessToken;
    @Key("token_type")
    private String tokenType;
    @Key("expires_in")
    private Long expiresInSeconds;
    @Key("refresh_token")
    private String refreshToken;
    @Key
    private String scope;
    
    public final String getAccessToken() {
        /*SL:69*/return this.accessToken;
    }
    
    public TokenResponse setAccessToken(final String a1) {
        /*SL:81*/this.accessToken = Preconditions.<String>checkNotNull(a1);
        /*SL:82*/return this;
    }
    
    public final String getTokenType() {
        /*SL:90*/return this.tokenType;
    }
    
    public TokenResponse setTokenType(final String a1) {
        /*SL:103*/this.tokenType = Preconditions.<String>checkNotNull(a1);
        /*SL:104*/return this;
    }
    
    public final Long getExpiresInSeconds() {
        /*SL:112*/return this.expiresInSeconds;
    }
    
    public TokenResponse setExpiresInSeconds(final Long a1) {
        /*SL:125*/this.expiresInSeconds = a1;
        /*SL:126*/return this;
    }
    
    public final String getRefreshToken() {
        /*SL:134*/return this.refreshToken;
    }
    
    public TokenResponse setRefreshToken(final String a1) {
        /*SL:147*/this.refreshToken = a1;
        /*SL:148*/return this;
    }
    
    public final String getScope() {
        /*SL:155*/return this.scope;
    }
    
    public TokenResponse setScope(final String a1) {
        /*SL:167*/this.scope = a1;
        /*SL:168*/return this;
    }
    
    @Override
    public TokenResponse set(final String a1, final Object a2) {
        /*SL:173*/return (TokenResponse)super.set(a1, a2);
    }
    
    @Override
    public TokenResponse clone() {
        /*SL:178*/return (TokenResponse)super.clone();
    }
}
