package com.google.api.client.auth.oauth2;

import java.io.IOException;

public interface CredentialRefreshListener
{
    void onTokenResponse(Credential p0, TokenResponse p1) throws IOException;
    
    void onTokenErrorResponse(Credential p0, TokenErrorResponse p1) throws IOException;
}
