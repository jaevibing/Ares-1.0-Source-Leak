package com.google.api.client.auth.oauth2;

import java.io.IOException;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Beta;

@Deprecated
@Beta
public final class CredentialStoreRefreshListener implements CredentialRefreshListener
{
    private final CredentialStore credentialStore;
    private final String userId;
    
    public CredentialStoreRefreshListener(final String a1, final CredentialStore a2) {
        this.userId = Preconditions.<String>checkNotNull(a1);
        this.credentialStore = Preconditions.<CredentialStore>checkNotNull(a2);
    }
    
    @Override
    public void onTokenResponse(final Credential a1, final TokenResponse a2) throws IOException {
        /*SL:57*/this.makePersistent(a1);
    }
    
    @Override
    public void onTokenErrorResponse(final Credential a1, final TokenErrorResponse a2) throws IOException {
        /*SL:62*/this.makePersistent(a1);
    }
    
    public CredentialStore getCredentialStore() {
        /*SL:67*/return this.credentialStore;
    }
    
    public void makePersistent(final Credential a1) throws IOException {
        /*SL:72*/this.credentialStore.store(this.userId, a1);
    }
}
