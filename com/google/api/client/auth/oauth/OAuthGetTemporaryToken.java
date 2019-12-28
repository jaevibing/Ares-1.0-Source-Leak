package com.google.api.client.auth.oauth;

import com.google.api.client.util.Beta;

@Beta
public class OAuthGetTemporaryToken extends AbstractOAuthGetToken
{
    public String callback;
    
    public OAuthGetTemporaryToken(final String a1) {
        super(a1);
    }
    
    @Override
    public OAuthParameters createParameters() {
        final OAuthParameters v1 = /*EL:50*/super.createParameters();
        /*SL:51*/v1.callback = this.callback;
        /*SL:52*/return v1;
    }
}
