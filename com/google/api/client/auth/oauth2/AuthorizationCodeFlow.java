package com.google.api.client.auth.oauth2;

import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Strings;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.GenericUrl;
import java.util.Collection;
import com.google.api.client.util.Clock;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.Beta;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;

public class AuthorizationCodeFlow
{
    private final Credential.AccessMethod method;
    private final HttpTransport transport;
    private final JsonFactory jsonFactory;
    private final String tokenServerEncodedUrl;
    private final HttpExecuteInterceptor clientAuthentication;
    private final String clientId;
    private final String authorizationServerEncodedUrl;
    @Deprecated
    @Beta
    private final CredentialStore credentialStore;
    @Beta
    private final DataStore<StoredCredential> credentialDataStore;
    private final HttpRequestInitializer requestInitializer;
    private final Clock clock;
    private final Collection<String> scopes;
    private final CredentialCreatedListener credentialCreatedListener;
    private final Collection<CredentialRefreshListener> refreshListeners;
    
    public AuthorizationCodeFlow(final Credential.AccessMethod a1, final HttpTransport a2, final JsonFactory a3, final GenericUrl a4, final HttpExecuteInterceptor a5, final String a6, final String a7) {
        this(new Builder(a1, a2, a3, a4, a5, a6, a7));
    }
    
    protected AuthorizationCodeFlow(final Builder a1) {
        this.method = Preconditions.<Credential.AccessMethod>checkNotNull(a1.method);
        this.transport = Preconditions.<HttpTransport>checkNotNull(a1.transport);
        this.jsonFactory = Preconditions.<JsonFactory>checkNotNull(a1.jsonFactory);
        this.tokenServerEncodedUrl = Preconditions.<GenericUrl>checkNotNull(a1.tokenServerUrl).build();
        this.clientAuthentication = a1.clientAuthentication;
        this.clientId = Preconditions.<String>checkNotNull(a1.clientId);
        this.authorizationServerEncodedUrl = Preconditions.<String>checkNotNull(a1.authorizationServerEncodedUrl);
        this.requestInitializer = a1.requestInitializer;
        this.credentialStore = a1.credentialStore;
        this.credentialDataStore = a1.credentialDataStore;
        this.scopes = Collections.<String>unmodifiableCollection((Collection<? extends String>)a1.scopes);
        this.clock = Preconditions.<Clock>checkNotNull(a1.clock);
        this.credentialCreatedListener = a1.credentialCreatedListener;
        this.refreshListeners = Collections.<CredentialRefreshListener>unmodifiableCollection((Collection<? extends CredentialRefreshListener>)a1.refreshListeners);
    }
    
    public AuthorizationCodeRequestUrl newAuthorizationUrl() {
        /*SL:185*/return new AuthorizationCodeRequestUrl(this.authorizationServerEncodedUrl, this.clientId).setScopes(this.scopes);
    }
    
    public AuthorizationCodeTokenRequest newTokenRequest(final String a1) {
        /*SL:209*/return new AuthorizationCodeTokenRequest(this.transport, this.jsonFactory, new GenericUrl(this.tokenServerEncodedUrl), a1).setClientAuthentication(this.clientAuthentication).setRequestInitializer(/*EL:210*/this.requestInitializer).setScopes(/*EL:211*/this.scopes);
    }
    
    public Credential createAndStoreCredential(final TokenResponse a1, final String a2) throws IOException {
        final Credential v1 = /*EL:225*/this.newCredential(a2).setFromTokenResponse(a1);
        /*SL:226*/if (this.credentialStore != null) {
            /*SL:227*/this.credentialStore.store(a2, v1);
        }
        /*SL:229*/if (this.credentialDataStore != null) {
            /*SL:230*/this.credentialDataStore.set(a2, (Serializable)new StoredCredential(v1));
        }
        /*SL:232*/if (this.credentialCreatedListener != null) {
            /*SL:233*/this.credentialCreatedListener.onCredentialCreated(v1, a1);
        }
        /*SL:235*/return v1;
    }
    
    public Credential loadCredential(final String v2) throws IOException {
        /*SL:249*/if (Strings.isNullOrEmpty(v2)) {
            /*SL:250*/return null;
        }
        /*SL:253*/if (this.credentialDataStore == null && this.credentialStore == null) {
            /*SL:254*/return null;
        }
        final Credential v3 = /*EL:256*/this.newCredential(v2);
        /*SL:257*/if (this.credentialDataStore != null) {
            final StoredCredential a1 = /*EL:258*/(StoredCredential)this.credentialDataStore.get(v2);
            /*SL:259*/if (a1 == null) {
                /*SL:260*/return null;
            }
            /*SL:262*/v3.setAccessToken(a1.getAccessToken());
            /*SL:263*/v3.setRefreshToken(a1.getRefreshToken());
            /*SL:264*/v3.setExpirationTimeMilliseconds(a1.getExpirationTimeMilliseconds());
        }
        else/*SL:265*/ if (!this.credentialStore.load(v2, v3)) {
            /*SL:266*/return null;
        }
        /*SL:268*/return v3;
    }
    
    private Credential newCredential(final String a1) {
        final Credential.Builder v1 = /*EL:278*/new Credential.Builder(this.method).setTransport(this.transport).setJsonFactory(this.jsonFactory).setTokenServerEncodedUrl(/*EL:279*/this.tokenServerEncodedUrl).setClientAuthentication(/*EL:280*/this.clientAuthentication).setRequestInitializer(/*EL:281*/this.requestInitializer).setClock(/*EL:282*/this.clock);
        /*SL:284*/if (this.credentialDataStore != null) {
            /*SL:285*/v1.addRefreshListener(new DataStoreCredentialRefreshListener(a1, this.credentialDataStore));
        }
        else/*SL:287*/ if (this.credentialStore != null) {
            /*SL:288*/v1.addRefreshListener(new CredentialStoreRefreshListener(a1, this.credentialStore));
        }
        /*SL:290*/v1.getRefreshListeners().addAll(this.refreshListeners);
        /*SL:291*/return v1.build();
    }
    
    public final Credential.AccessMethod getMethod() {
        /*SL:299*/return this.method;
    }
    
    public final HttpTransport getTransport() {
        /*SL:304*/return this.transport;
    }
    
    public final JsonFactory getJsonFactory() {
        /*SL:309*/return this.jsonFactory;
    }
    
    public final String getTokenServerEncodedUrl() {
        /*SL:314*/return this.tokenServerEncodedUrl;
    }
    
    public final HttpExecuteInterceptor getClientAuthentication() {
        /*SL:322*/return this.clientAuthentication;
    }
    
    public final String getClientId() {
        /*SL:327*/return this.clientId;
    }
    
    public final String getAuthorizationServerEncodedUrl() {
        /*SL:332*/return this.authorizationServerEncodedUrl;
    }
    
    @Deprecated
    @Beta
    public final CredentialStore getCredentialStore() {
        /*SL:343*/return this.credentialStore;
    }
    
    @Beta
    public final DataStore<StoredCredential> getCredentialDataStore() {
        /*SL:354*/return this.credentialDataStore;
    }
    
    public final HttpRequestInitializer getRequestInitializer() {
        /*SL:359*/return this.requestInitializer;
    }
    
    public final String getScopesAsString() {
        /*SL:368*/return Joiner.on(' ').join(this.scopes);
    }
    
    public final Collection<String> getScopes() {
        /*SL:373*/return this.scopes;
    }
    
    public final Clock getClock() {
        /*SL:381*/return this.clock;
    }
    
    public final Collection<CredentialRefreshListener> getRefreshListeners() {
        /*SL:390*/return this.refreshListeners;
    }
    
    public static class Builder
    {
        Credential.AccessMethod method;
        HttpTransport transport;
        JsonFactory jsonFactory;
        GenericUrl tokenServerUrl;
        HttpExecuteInterceptor clientAuthentication;
        String clientId;
        String authorizationServerEncodedUrl;
        @Deprecated
        @Beta
        CredentialStore credentialStore;
        @Beta
        DataStore<StoredCredential> credentialDataStore;
        HttpRequestInitializer requestInitializer;
        Collection<String> scopes;
        Clock clock;
        CredentialCreatedListener credentialCreatedListener;
        Collection<CredentialRefreshListener> refreshListeners;
        
        public Builder(final Credential.AccessMethod a1, final HttpTransport a2, final JsonFactory a3, final GenericUrl a4, final HttpExecuteInterceptor a5, final String a6, final String a7) {
            this.scopes = (Collection<String>)Lists.<Object>newArrayList();
            this.clock = Clock.SYSTEM;
            this.refreshListeners = (Collection<CredentialRefreshListener>)Lists.<Object>newArrayList();
            this.setMethod(a1);
            this.setTransport(a2);
            this.setJsonFactory(a3);
            this.setTokenServerUrl(a4);
            this.setClientAuthentication(a5);
            this.setClientId(a6);
            this.setAuthorizationServerEncodedUrl(a7);
        }
        
        public AuthorizationCodeFlow build() {
            /*SL:504*/return new AuthorizationCodeFlow(this);
        }
        
        public final Credential.AccessMethod getMethod() {
            /*SL:512*/return this.method;
        }
        
        public Builder setMethod(final Credential.AccessMethod a1) {
            /*SL:526*/this.method = Preconditions.<Credential.AccessMethod>checkNotNull(a1);
            /*SL:527*/return this;
        }
        
        public final HttpTransport getTransport() {
            /*SL:532*/return this.transport;
        }
        
        public Builder setTransport(final HttpTransport a1) {
            /*SL:545*/this.transport = Preconditions.<HttpTransport>checkNotNull(a1);
            /*SL:546*/return this;
        }
        
        public final JsonFactory getJsonFactory() {
            /*SL:551*/return this.jsonFactory;
        }
        
        public Builder setJsonFactory(final JsonFactory a1) {
            /*SL:564*/this.jsonFactory = Preconditions.<JsonFactory>checkNotNull(a1);
            /*SL:565*/return this;
        }
        
        public final GenericUrl getTokenServerUrl() {
            /*SL:570*/return this.tokenServerUrl;
        }
        
        public Builder setTokenServerUrl(final GenericUrl a1) {
            /*SL:583*/this.tokenServerUrl = Preconditions.<GenericUrl>checkNotNull(a1);
            /*SL:584*/return this;
        }
        
        public final HttpExecuteInterceptor getClientAuthentication() {
            /*SL:592*/return this.clientAuthentication;
        }
        
        public Builder setClientAuthentication(final HttpExecuteInterceptor a1) {
            /*SL:606*/this.clientAuthentication = a1;
            /*SL:607*/return this;
        }
        
        public final String getClientId() {
            /*SL:612*/return this.clientId;
        }
        
        public Builder setClientId(final String a1) {
            /*SL:625*/this.clientId = Preconditions.<String>checkNotNull(a1);
            /*SL:626*/return this;
        }
        
        public final String getAuthorizationServerEncodedUrl() {
            /*SL:631*/return this.authorizationServerEncodedUrl;
        }
        
        public Builder setAuthorizationServerEncodedUrl(final String a1) {
            /*SL:644*/this.authorizationServerEncodedUrl = /*EL:645*/Preconditions.<String>checkNotNull(a1);
            /*SL:646*/return this;
        }
        
        @Deprecated
        @Beta
        public final CredentialStore getCredentialStore() {
            /*SL:657*/return this.credentialStore;
        }
        
        @Beta
        public final DataStore<StoredCredential> getCredentialDataStore() {
            /*SL:668*/return this.credentialDataStore;
        }
        
        public final Clock getClock() {
            /*SL:677*/return this.clock;
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:694*/this.clock = Preconditions.<Clock>checkNotNull(a1);
            /*SL:695*/return this;
        }
        
        @Deprecated
        @Beta
        public Builder setCredentialStore(final CredentialStore a1) {
            /*SL:721*/Preconditions.checkArgument(this.credentialDataStore == null);
            /*SL:722*/this.credentialStore = a1;
            /*SL:723*/return this;
        }
        
        @Beta
        public Builder setDataStoreFactory(final DataStoreFactory a1) throws IOException {
            /*SL:744*/return this.setCredentialDataStore(StoredCredential.getDefaultDataStore(a1));
        }
        
        @Beta
        public Builder setCredentialDataStore(final DataStore<StoredCredential> a1) {
            /*SL:765*/Preconditions.checkArgument(this.credentialStore == null);
            /*SL:766*/this.credentialDataStore = a1;
            /*SL:767*/return this;
        }
        
        public final HttpRequestInitializer getRequestInitializer() {
            /*SL:772*/return this.requestInitializer;
        }
        
        public Builder setRequestInitializer(final HttpRequestInitializer a1) {
            /*SL:784*/this.requestInitializer = a1;
            /*SL:785*/return this;
        }
        
        public Builder setScopes(final Collection<String> a1) {
            /*SL:800*/this.scopes = Preconditions.<Collection<String>>checkNotNull(a1);
            /*SL:801*/return this;
        }
        
        public final Collection<String> getScopes() {
            /*SL:806*/return this.scopes;
        }
        
        public Builder setCredentialCreatedListener(final CredentialCreatedListener a1) {
            /*SL:821*/this.credentialCreatedListener = a1;
            /*SL:822*/return this;
        }
        
        public Builder addRefreshListener(final CredentialRefreshListener a1) {
            /*SL:837*/this.refreshListeners.add(Preconditions.<CredentialRefreshListener>checkNotNull(a1));
            /*SL:838*/return this;
        }
        
        public final Collection<CredentialRefreshListener> getRefreshListeners() {
            /*SL:847*/return this.refreshListeners;
        }
        
        public Builder setRefreshListeners(final Collection<CredentialRefreshListener> a1) {
            /*SL:861*/this.refreshListeners = Preconditions.<Collection<CredentialRefreshListener>>checkNotNull(a1);
            /*SL:862*/return this;
        }
        
        public final CredentialCreatedListener getCredentialCreatedListener() {
            /*SL:871*/return this.credentialCreatedListener;
        }
    }
    
    public interface CredentialCreatedListener
    {
        void onCredentialCreated(Credential p0, TokenResponse p1) throws IOException;
    }
}
