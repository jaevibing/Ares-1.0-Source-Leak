package com.google.api.client.googleapis.auth.oauth2;

import java.util.List;
import com.google.api.client.util.GenericData;
import java.io.IOException;
import java.io.Reader;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class GoogleClientSecrets extends GenericJson
{
    @Key
    private Details installed;
    @Key
    private Details web;
    
    public Details getInstalled() {
        /*SL:59*/return this.installed;
    }
    
    public GoogleClientSecrets setInstalled(final Details a1) {
        /*SL:64*/this.installed = a1;
        /*SL:65*/return this;
    }
    
    public Details getWeb() {
        /*SL:70*/return this.web;
    }
    
    public GoogleClientSecrets setWeb(final Details a1) {
        /*SL:75*/this.web = a1;
        /*SL:76*/return this;
    }
    
    public Details getDetails() {
        /*SL:82*/Preconditions.checkArgument(this.web == null != (this.installed == null));
        /*SL:83*/return (this.web == null) ? this.installed : this.web;
    }
    
    public GoogleClientSecrets set(final String a1, final Object a2) {
        /*SL:177*/return (GoogleClientSecrets)super.set(a1, a2);
    }
    
    public GoogleClientSecrets clone() {
        /*SL:182*/return (GoogleClientSecrets)super.clone();
    }
    
    public static GoogleClientSecrets load(final JsonFactory a1, final Reader a2) throws IOException {
        /*SL:192*/return a1.<GoogleClientSecrets>fromReader(a2, GoogleClientSecrets.class);
    }
    
    public static final class Details extends GenericJson
    {
        @Key("client_id")
        private String clientId;
        @Key("client_secret")
        private String clientSecret;
        @Key("redirect_uris")
        private List<String> redirectUris;
        @Key("auth_uri")
        private String authUri;
        @Key("token_uri")
        private String tokenUri;
        
        public String getClientId() {
            /*SL:111*/return this.clientId;
        }
        
        public Details setClientId(final String a1) {
            /*SL:116*/this.clientId = a1;
            /*SL:117*/return this;
        }
        
        public String getClientSecret() {
            /*SL:122*/return this.clientSecret;
        }
        
        public Details setClientSecret(final String a1) {
            /*SL:127*/this.clientSecret = a1;
            /*SL:128*/return this;
        }
        
        public List<String> getRedirectUris() {
            /*SL:133*/return this.redirectUris;
        }
        
        public Details setRedirectUris(final List<String> a1) {
            /*SL:138*/this.redirectUris = a1;
            /*SL:139*/return this;
        }
        
        public String getAuthUri() {
            /*SL:144*/return this.authUri;
        }
        
        public Details setAuthUri(final String a1) {
            /*SL:149*/this.authUri = a1;
            /*SL:150*/return this;
        }
        
        public String getTokenUri() {
            /*SL:155*/return this.tokenUri;
        }
        
        public Details setTokenUri(final String a1) {
            /*SL:160*/this.tokenUri = a1;
            /*SL:161*/return this;
        }
        
        public Details set(final String a1, final Object a2) {
            /*SL:166*/return (Details)super.set(a1, a2);
        }
        
        public Details clone() {
            /*SL:171*/return (Details)super.clone();
        }
    }
}
