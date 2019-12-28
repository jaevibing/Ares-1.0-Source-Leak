package com.google.api.client.googleapis.testing.services.json;

import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;

@Beta
public class MockGoogleJsonClient extends AbstractGoogleJsonClient
{
    protected MockGoogleJsonClient(final Builder a1) {
        super(a1);
    }
    
    public MockGoogleJsonClient(final HttpTransport a1, final JsonFactory a2, final String a3, final String a4, final HttpRequestInitializer a5, final boolean a6) {
        this(new Builder(a1, a2, a3, a4, a5, a6));
    }
    
    @Beta
    public static class Builder extends AbstractGoogleJsonClient.Builder
    {
        public Builder(final HttpTransport a1, final JsonFactory a2, final String a3, final String a4, final HttpRequestInitializer a5, final boolean a6) {
            super(a1, a2, a3, a4, a5, a6);
        }
        
        public MockGoogleJsonClient build() {
            /*SL:84*/return new MockGoogleJsonClient(this);
        }
        
        public Builder setRootUrl(final String a1) {
            /*SL:89*/return (Builder)super.setRootUrl(a1);
        }
        
        public Builder setServicePath(final String a1) {
            /*SL:94*/return (Builder)super.setServicePath(a1);
        }
        
        public Builder setGoogleClientRequestInitializer(final GoogleClientRequestInitializer a1) {
            /*SL:100*/return (Builder)super.setGoogleClientRequestInitializer(a1);
        }
        
        public Builder setHttpRequestInitializer(final HttpRequestInitializer a1) {
            /*SL:105*/return (Builder)super.setHttpRequestInitializer(a1);
        }
        
        public Builder setApplicationName(final String a1) {
            /*SL:110*/return (Builder)super.setApplicationName(a1);
        }
        
        public Builder setSuppressPatternChecks(final boolean a1) {
            /*SL:115*/return (Builder)super.setSuppressPatternChecks(a1);
        }
        
        public Builder setSuppressRequiredParameterChecks(final boolean a1) {
            /*SL:120*/return (Builder)super.setSuppressRequiredParameterChecks(a1);
        }
        
        public Builder setSuppressAllChecks(final boolean a1) {
            /*SL:125*/return (Builder)super.setSuppressAllChecks(a1);
        }
    }
}
