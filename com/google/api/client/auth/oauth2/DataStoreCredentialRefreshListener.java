package com.google.api.client.auth.oauth2;

import java.io.Serializable;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.Beta;

@Beta
public final class DataStoreCredentialRefreshListener implements CredentialRefreshListener
{
    private final DataStore<StoredCredential> credentialDataStore;
    private final String userId;
    
    public DataStoreCredentialRefreshListener(final String a1, final DataStoreFactory a2) throws IOException {
        this(a1, StoredCredential.getDefaultDataStore(a2));
    }
    
    public DataStoreCredentialRefreshListener(final String a1, final DataStore<StoredCredential> a2) {
        this.userId = Preconditions.<String>checkNotNull(a1);
        this.credentialDataStore = Preconditions.<DataStore<StoredCredential>>checkNotNull(a2);
    }
    
    @Override
    public void onTokenResponse(final Credential a1, final TokenResponse a2) throws IOException {
        /*SL:79*/this.makePersistent(a1);
    }
    
    @Override
    public void onTokenErrorResponse(final Credential a1, final TokenErrorResponse a2) throws IOException {
        /*SL:84*/this.makePersistent(a1);
    }
    
    public DataStore<StoredCredential> getCredentialDataStore() {
        /*SL:89*/return this.credentialDataStore;
    }
    
    public void makePersistent(final Credential a1) throws IOException {
        /*SL:94*/this.credentialDataStore.set(this.userId, (Serializable)new StoredCredential(a1));
    }
}
