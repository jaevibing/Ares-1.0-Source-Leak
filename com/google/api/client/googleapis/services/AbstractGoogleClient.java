package com.google.api.client.googleapis.services;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.googleapis.batch.BatchRequest;
import java.io.IOException;
import com.google.api.client.util.Strings;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.http.HttpRequestFactory;
import java.util.logging.Logger;

public abstract class AbstractGoogleClient
{
    static final Logger logger;
    private final HttpRequestFactory requestFactory;
    private final GoogleClientRequestInitializer googleClientRequestInitializer;
    private final String rootUrl;
    private final String servicePath;
    private final String batchPath;
    private final String applicationName;
    private final ObjectParser objectParser;
    private final boolean suppressPatternChecks;
    private final boolean suppressRequiredParameterChecks;
    
    protected AbstractGoogleClient(final Builder a1) {
        this.googleClientRequestInitializer = a1.googleClientRequestInitializer;
        this.rootUrl = normalizeRootUrl(a1.rootUrl);
        this.servicePath = normalizeServicePath(a1.servicePath);
        this.batchPath = a1.batchPath;
        if (Strings.isNullOrEmpty(a1.applicationName)) {
            AbstractGoogleClient.logger.warning("Application name is not set. Call Builder#setApplicationName.");
        }
        this.applicationName = a1.applicationName;
        this.requestFactory = ((a1.httpRequestInitializer == null) ? a1.transport.createRequestFactory() : a1.transport.createRequestFactory(a1.httpRequestInitializer));
        this.objectParser = a1.objectParser;
        this.suppressPatternChecks = a1.suppressPatternChecks;
        this.suppressRequiredParameterChecks = a1.suppressRequiredParameterChecks;
    }
    
    public final String getRootUrl() {
        /*SL:106*/return this.rootUrl;
    }
    
    public final String getServicePath() {
        /*SL:118*/return this.servicePath;
    }
    
    public final String getBaseUrl() {
        final String value = /*EL:130*/String.valueOf(this.rootUrl);
        final String value2 = String.valueOf(this.servicePath);
        return (value2.length() != 0) ? value.concat(value2) : new String(value);
    }
    
    public final String getApplicationName() {
        /*SL:138*/return this.applicationName;
    }
    
    public final HttpRequestFactory getRequestFactory() {
        /*SL:143*/return this.requestFactory;
    }
    
    public final GoogleClientRequestInitializer getGoogleClientRequestInitializer() {
        /*SL:148*/return this.googleClientRequestInitializer;
    }
    
    public ObjectParser getObjectParser() {
        /*SL:160*/return this.objectParser;
    }
    
    protected void initialize(final AbstractGoogleClientRequest<?> a1) throws IOException {
        /*SL:191*/if (this.getGoogleClientRequestInitializer() != null) {
            /*SL:192*/this.getGoogleClientRequestInitializer().initialize(a1);
        }
    }
    
    public final BatchRequest batch() {
        /*SL:213*/return this.batch(null);
    }
    
    public final BatchRequest batch(final HttpRequestInitializer a1) {
        final BatchRequest batchRequest;
        final BatchRequest v1 = /*EL:237*/batchRequest = new BatchRequest(this.getRequestFactory().getTransport(), a1);
        final String value = String.valueOf(this.getRootUrl());
        final String value2 = String.valueOf(this.batchPath);
        batchRequest.setBatchUrl(new GenericUrl((value2.length() != 0) ? value.concat(value2) : new String(value)));
        /*SL:238*/return v1;
    }
    
    public final boolean getSuppressPatternChecks() {
        /*SL:243*/return this.suppressPatternChecks;
    }
    
    public final boolean getSuppressRequiredParameterChecks() {
        /*SL:252*/return this.suppressRequiredParameterChecks;
    }
    
    static String normalizeRootUrl(String a1) {
        /*SL:257*/Preconditions.<String>checkNotNull(a1, (Object)"root URL cannot be null.");
        /*SL:258*/if (!a1.endsWith("/")) {
            /*SL:259*/a1 = String.valueOf(a1).concat("/");
        }
        /*SL:261*/return a1;
    }
    
    static String normalizeServicePath(String a1) {
        /*SL:269*/Preconditions.<String>checkNotNull(a1, (Object)"service path cannot be null");
        /*SL:270*/if (a1.length() == 1) {
            /*SL:271*/Preconditions.checkArgument("/".equals(a1), (Object)"service path must equal \"/\" if it is of length 1.");
            /*SL:273*/a1 = "";
        }
        else/*SL:274*/ if (a1.length() > 0) {
            /*SL:275*/if (!a1.endsWith("/")) {
                /*SL:276*/a1 = String.valueOf(a1).concat("/");
            }
            /*SL:278*/if (a1.startsWith("/")) {
                /*SL:279*/a1 = a1.substring(1);
            }
        }
        /*SL:282*/return a1;
    }
    
    static {
        logger = Logger.getLogger(AbstractGoogleClient.class.getName());
    }
    
    public abstract static class Builder
    {
        final HttpTransport transport;
        GoogleClientRequestInitializer googleClientRequestInitializer;
        HttpRequestInitializer httpRequestInitializer;
        final ObjectParser objectParser;
        String rootUrl;
        String servicePath;
        String batchPath;
        String applicationName;
        boolean suppressPatternChecks;
        boolean suppressRequiredParameterChecks;
        
        protected Builder(final HttpTransport a1, final String a2, final String a3, final ObjectParser a4, final HttpRequestInitializer a5) {
            this.transport = Preconditions.<HttpTransport>checkNotNull(a1);
            this.objectParser = a4;
            this.setRootUrl(a2);
            this.setServicePath(a3);
            this.httpRequestInitializer = a5;
        }
        
        public abstract AbstractGoogleClient build();
        
        public final HttpTransport getTransport() {
            /*SL:353*/return this.transport;
        }
        
        public ObjectParser getObjectParser() {
            /*SL:365*/return this.objectParser;
        }
        
        public final String getRootUrl() {
            /*SL:377*/return this.rootUrl;
        }
        
        public Builder setRootUrl(final String a1) {
            /*SL:393*/this.rootUrl = AbstractGoogleClient.normalizeRootUrl(a1);
            /*SL:394*/return this;
        }
        
        public final String getServicePath() {
            /*SL:406*/return this.servicePath;
        }
        
        public Builder setServicePath(final String a1) {
            /*SL:429*/this.servicePath = AbstractGoogleClient.normalizeServicePath(a1);
            /*SL:430*/return this;
        }
        
        public Builder setBatchPath(final String a1) {
            /*SL:437*/this.batchPath = a1;
            /*SL:438*/return this;
        }
        
        public final GoogleClientRequestInitializer getGoogleClientRequestInitializer() {
            /*SL:443*/return this.googleClientRequestInitializer;
        }
        
        public Builder setGoogleClientRequestInitializer(final GoogleClientRequestInitializer a1) {
            /*SL:456*/this.googleClientRequestInitializer = a1;
            /*SL:457*/return this;
        }
        
        public final HttpRequestInitializer getHttpRequestInitializer() {
            /*SL:462*/return this.httpRequestInitializer;
        }
        
        public Builder setHttpRequestInitializer(final HttpRequestInitializer a1) {
            /*SL:474*/this.httpRequestInitializer = a1;
            /*SL:475*/return this;
        }
        
        public final String getApplicationName() {
            /*SL:483*/return this.applicationName;
        }
        
        public Builder setApplicationName(final String a1) {
            /*SL:496*/this.applicationName = a1;
            /*SL:497*/return this;
        }
        
        public final boolean getSuppressPatternChecks() {
            /*SL:502*/return this.suppressPatternChecks;
        }
        
        public Builder setSuppressPatternChecks(final boolean a1) {
            /*SL:518*/this.suppressPatternChecks = a1;
            /*SL:519*/return this;
        }
        
        public final boolean getSuppressRequiredParameterChecks() {
            /*SL:528*/return this.suppressRequiredParameterChecks;
        }
        
        public Builder setSuppressRequiredParameterChecks(final boolean a1) {
            /*SL:546*/this.suppressRequiredParameterChecks = a1;
            /*SL:547*/return this;
        }
        
        public Builder setSuppressAllChecks(final boolean a1) {
            /*SL:561*/return this.setSuppressPatternChecks(true).setSuppressRequiredParameterChecks(true);
        }
    }
}
