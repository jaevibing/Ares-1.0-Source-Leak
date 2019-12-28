package com.google.api.client.googleapis.services.json;

import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.googleapis.services.AbstractGoogleClient;

public abstract class AbstractGoogleJsonClient extends AbstractGoogleClient
{
    protected AbstractGoogleJsonClient(final Builder a1) {
        super(a1);
    }
    
    public JsonObjectParser getObjectParser() {
        /*SL:46*/return (JsonObjectParser)super.getObjectParser();
    }
    
    public final JsonFactory getJsonFactory() {
        /*SL:51*/return this.getObjectParser().getJsonFactory();
    }
    
    public abstract static class Builder extends AbstractGoogleClient.Builder
    {
        protected Builder(final HttpTransport a1, final JsonFactory a2, final String a3, final String a4, final HttpRequestInitializer a5, final boolean a6) {
            super(a1, a3, a4, new JsonObjectParser.Builder(a2).setWrapperKeys((Collection<String>)(a6 ? Arrays.<String>asList("data", "error") : Collections.<Object>emptySet())).build(), a5);
        }
        
        public final JsonObjectParser getObjectParser() {
            /*SL:82*/return (JsonObjectParser)super.getObjectParser();
        }
        
        public final JsonFactory getJsonFactory() {
            /*SL:87*/return this.getObjectParser().getJsonFactory();
        }
        
        public abstract AbstractGoogleJsonClient build();
        
        public Builder setRootUrl(final String a1) {
            /*SL:95*/return (Builder)super.setRootUrl(a1);
        }
        
        public Builder setServicePath(final String a1) {
            /*SL:100*/return (Builder)super.setServicePath(a1);
        }
        
        public Builder setGoogleClientRequestInitializer(final GoogleClientRequestInitializer a1) {
            /*SL:106*/return (Builder)super.setGoogleClientRequestInitializer(a1);
        }
        
        public Builder setHttpRequestInitializer(final HttpRequestInitializer a1) {
            /*SL:111*/return (Builder)super.setHttpRequestInitializer(a1);
        }
        
        public Builder setApplicationName(final String a1) {
            /*SL:116*/return (Builder)super.setApplicationName(a1);
        }
        
        public Builder setSuppressPatternChecks(final boolean a1) {
            /*SL:121*/return (Builder)super.setSuppressPatternChecks(a1);
        }
        
        public Builder setSuppressRequiredParameterChecks(final boolean a1) {
            /*SL:126*/return (Builder)super.setSuppressRequiredParameterChecks(a1);
        }
        
        public Builder setSuppressAllChecks(final boolean a1) {
            /*SL:131*/return (Builder)super.setSuppressAllChecks(a1);
        }
    }
}
