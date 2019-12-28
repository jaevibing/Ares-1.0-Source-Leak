package com.google.api.client.auth.oauth;

import com.google.api.client.util.Beta;

@Beta
public class OAuthGetAccessToken extends AbstractOAuthGetToken
{
    public String temporaryToken;
    public String verifier;
    
    public OAuthGetAccessToken(final String a1) {
        super(a1);
    }
    
    @Override
    public OAuthParameters createParameters() {
        final OAuthParameters v1 = /*EL:55*/super.createParameters();
        /*SL:56*/v1.token = this.temporaryToken;
        /*SL:57*/v1.verifier = this.verifier;
        /*SL:58*/return v1;
    }
}
