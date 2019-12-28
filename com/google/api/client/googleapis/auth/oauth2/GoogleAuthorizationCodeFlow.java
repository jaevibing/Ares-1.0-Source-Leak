package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.util.Clock;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Beta;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.DataStore;
import java.io.IOException;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import java.util.Collection;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;

public class GoogleAuthorizationCodeFlow extends AuthorizationCodeFlow
{
    private final String approvalPrompt;
    private final String accessType;
    
    public GoogleAuthorizationCodeFlow(final HttpTransport a1, final JsonFactory a2, final String a3, final String a4, final Collection<String> a5) {
        this(new Builder(a1, a2, a3, a4, a5));
    }
    
    protected GoogleAuthorizationCodeFlow(final Builder a1) {
        super(a1);
        this.accessType = a1.accessType;
        this.approvalPrompt = a1.approvalPrompt;
    }
    
    public GoogleAuthorizationCodeTokenRequest newTokenRequest(final String a1) {
        /*SL:114*/return new GoogleAuthorizationCodeTokenRequest(this.getTransport(), this.getJsonFactory(), this.getTokenServerEncodedUrl(), "", "", a1, "").setClientAuthentication(this.getClientAuthentication()).setRequestInitializer(this.getRequestInitializer()).setScopes(this.getScopes());
    }
    
    public GoogleAuthorizationCodeRequestUrl newAuthorizationUrl() {
        /*SL:123*/return new GoogleAuthorizationCodeRequestUrl(this.getAuthorizationServerEncodedUrl(), this.getClientId(), "", this.getScopes()).setAccessType(this.accessType).setApprovalPrompt(this.approvalPrompt);
    }
    
    public final String getApprovalPrompt() {
        /*SL:134*/return this.approvalPrompt;
    }
    
    public final String getAccessType() {
        /*SL:142*/return this.accessType;
    }
    
    public static class Builder extends AuthorizationCodeFlow.Builder
    {
        String approvalPrompt;
        String accessType;
        
        public Builder(final HttpTransport a1, final JsonFactory a2, final String a3, final String a4, final Collection<String> a5) {
            super(BearerToken.authorizationHeaderAccessMethod(), a1, a2, new GenericUrl("https://accounts.google.com/o/oauth2/token"), new ClientParametersAuthentication(a3, a4), a3, "https://accounts.google.com/o/oauth2/auth");
            this.setScopes(a5);
        }
        
        public Builder(final HttpTransport a1, final JsonFactory a2, final GoogleClientSecrets a3, final Collection<String> a4) {
            super(BearerToken.authorizationHeaderAccessMethod(), a1, a2, new GenericUrl("https://accounts.google.com/o/oauth2/token"), new ClientParametersAuthentication(a3.getDetails().getClientId(), a3.getDetails().getClientSecret()), a3.getDetails().getClientId(), "https://accounts.google.com/o/oauth2/auth");
            this.setScopes(a4);
        }
        
        public GoogleAuthorizationCodeFlow build() {
            /*SL:204*/return new GoogleAuthorizationCodeFlow(this);
        }
        
        public Builder setDataStoreFactory(final DataStoreFactory a1) throws IOException {
            /*SL:209*/return (Builder)super.setDataStoreFactory(a1);
        }
        
        public Builder setCredentialDataStore(final DataStore<StoredCredential> a1) {
            /*SL:214*/return (Builder)super.setCredentialDataStore(a1);
        }
        
        public Builder setCredentialCreatedListener(final CredentialCreatedListener a1) {
            /*SL:220*/return (Builder)super.setCredentialCreatedListener(a1);
        }
        
        @Deprecated
        @Beta
        public Builder setCredentialStore(final CredentialStore a1) {
            /*SL:227*/return (Builder)super.setCredentialStore(a1);
        }
        
        public Builder setRequestInitializer(final HttpRequestInitializer a1) {
            /*SL:232*/return (Builder)super.setRequestInitializer(a1);
        }
        
        public Builder setScopes(final Collection<String> a1) {
            /*SL:237*/Preconditions.checkState(!a1.isEmpty());
            /*SL:238*/return (Builder)super.setScopes(a1);
        }
        
        public Builder setMethod(final Credential.AccessMethod a1) {
            /*SL:246*/return (Builder)super.setMethod(a1);
        }
        
        public Builder setTransport(final HttpTransport a1) {
            /*SL:254*/return (Builder)super.setTransport(a1);
        }
        
        public Builder setJsonFactory(final JsonFactory a1) {
            /*SL:262*/return (Builder)super.setJsonFactory(a1);
        }
        
        public Builder setTokenServerUrl(final GenericUrl a1) {
            /*SL:270*/return (Builder)super.setTokenServerUrl(a1);
        }
        
        public Builder setClientAuthentication(final HttpExecuteInterceptor a1) {
            /*SL:278*/return (Builder)super.setClientAuthentication(a1);
        }
        
        public Builder setClientId(final String a1) {
            /*SL:286*/return (Builder)super.setClientId(a1);
        }
        
        public Builder setAuthorizationServerEncodedUrl(final String a1) {
            /*SL:294*/return (Builder)super.setAuthorizationServerEncodedUrl(a1);
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:302*/return (Builder)super.setClock(a1);
        }
        
        public Builder addRefreshListener(final CredentialRefreshListener a1) {
            /*SL:307*/return (Builder)super.addRefreshListener(a1);
        }
        
        public Builder setRefreshListeners(final Collection<CredentialRefreshListener> a1) {
            /*SL:312*/return (Builder)super.setRefreshListeners(a1);
        }
        
        public Builder setApprovalPrompt(final String a1) {
            /*SL:330*/this.approvalPrompt = a1;
            /*SL:331*/return this;
        }
        
        public final String getApprovalPrompt() {
            /*SL:340*/return this.approvalPrompt;
        }
        
        public Builder setAccessType(final String a1) {
            /*SL:358*/this.accessType = a1;
            /*SL:359*/return this;
        }
        
        public final String getAccessType() {
            /*SL:367*/return this.accessType;
        }
    }
}
