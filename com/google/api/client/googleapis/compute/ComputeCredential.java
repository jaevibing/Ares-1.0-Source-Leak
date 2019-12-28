package com.google.api.client.googleapis.compute;

import java.util.Collection;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Preconditions;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.googleapis.auth.oauth2.OAuth2Utils;
import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;
import com.google.api.client.auth.oauth2.Credential;

@Beta
public class ComputeCredential extends Credential
{
    public static final String TOKEN_SERVER_ENCODED_URL;
    
    public ComputeCredential(final HttpTransport a1, final JsonFactory a2) {
        this(new Builder(a1, a2));
    }
    
    protected ComputeCredential(final Builder a1) {
        super(a1);
    }
    
    protected TokenResponse executeRefreshToken() throws IOException {
        final GenericUrl v1 = /*EL:84*/new GenericUrl(this.getTokenServerEncodedUrl());
        final HttpRequest v2 = /*EL:85*/this.getTransport().createRequestFactory().buildGetRequest(v1);
        /*SL:86*/v2.setParser(new JsonObjectParser(this.getJsonFactory()));
        /*SL:87*/v2.getHeaders().set("Metadata-Flavor", "Google");
        /*SL:88*/return v2.execute().<TokenResponse>parseAs(TokenResponse.class);
    }
    
    static {
        TOKEN_SERVER_ENCODED_URL = String.valueOf(OAuth2Utils.getMetadataServerUrl()).concat("/computeMetadata/v1/instance/service-accounts/default/token");
    }
    
    @Beta
    public static class Builder extends Credential.Builder
    {
        public Builder(final HttpTransport a1, final JsonFactory a2) {
            super(BearerToken.authorizationHeaderAccessMethod());
            this.setTransport(a1);
            this.setJsonFactory(a2);
            this.setTokenServerEncodedUrl(ComputeCredential.TOKEN_SERVER_ENCODED_URL);
        }
        
        public ComputeCredential build() {
            /*SL:115*/return new ComputeCredential(this);
        }
        
        public Builder setTransport(final HttpTransport a1) {
            /*SL:120*/return (Builder)super.setTransport(Preconditions.<HttpTransport>checkNotNull(a1));
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:125*/return (Builder)super.setClock(a1);
        }
        
        public Builder setJsonFactory(final JsonFactory a1) {
            /*SL:130*/return (Builder)super.setJsonFactory(Preconditions.<JsonFactory>checkNotNull(a1));
        }
        
        public Builder setTokenServerUrl(final GenericUrl a1) {
            /*SL:135*/return (Builder)super.setTokenServerUrl(Preconditions.<GenericUrl>checkNotNull(a1));
        }
        
        public Builder setTokenServerEncodedUrl(final String a1) {
            /*SL:140*/return (Builder)super.setTokenServerEncodedUrl(Preconditions.<String>checkNotNull(a1));
        }
        
        public Builder setClientAuthentication(final HttpExecuteInterceptor a1) {
            /*SL:146*/Preconditions.checkArgument(a1 == null);
            /*SL:147*/return this;
        }
        
        public Builder setRequestInitializer(final HttpRequestInitializer a1) {
            /*SL:152*/return (Builder)super.setRequestInitializer(a1);
        }
        
        public Builder addRefreshListener(final CredentialRefreshListener a1) {
            /*SL:157*/return (Builder)super.addRefreshListener(a1);
        }
        
        public Builder setRefreshListeners(final Collection<CredentialRefreshListener> a1) {
            /*SL:162*/return (Builder)super.setRefreshListeners(a1);
        }
    }
}
