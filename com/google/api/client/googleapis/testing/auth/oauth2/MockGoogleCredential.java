package com.google.api.client.googleapis.testing.auth.oauth2;

import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Clock;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.util.Beta;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

@Beta
public class MockGoogleCredential extends GoogleCredential
{
    public static final String ACCESS_TOKEN = "access_xyz";
    public static final String REFRESH_TOKEN = "refresh123";
    private static final String EXPIRES_IN_SECONDS = "3600";
    private static final String TOKEN_TYPE = "Bearer";
    private static final String TOKEN_RESPONSE = "{\"access_token\": \"%s\", \"expires_in\":  %s, \"refresh_token\": \"%s\", \"token_type\": \"%s\"}";
    private static final String DEFAULT_TOKEN_RESPONSE_JSON;
    
    public MockGoogleCredential(final Builder a1) {
        super(a1);
    }
    
    public static MockHttpTransport newMockHttpTransportWithSampleTokenResponse() {
        final MockLowLevelHttpResponse v1 = /*EL:105*/new MockLowLevelHttpResponse().setContentType("application/json; charset=UTF-8").setContent(MockGoogleCredential.DEFAULT_TOKEN_RESPONSE_JSON);
        final MockLowLevelHttpRequest v2 = /*EL:108*/new MockLowLevelHttpRequest().setResponse(v1);
        /*SL:110*/return new MockHttpTransport.Builder().setLowLevelHttpRequest(v2).build();
    }
    
    static {
        DEFAULT_TOKEN_RESPONSE_JSON = String.format("{\"access_token\": \"%s\", \"expires_in\":  %s, \"refresh_token\": \"%s\", \"token_type\": \"%s\"}", "access_xyz", "3600", "refresh123", "Bearer");
    }
    
    @Beta
    public static class Builder extends GoogleCredential.Builder
    {
        public Builder setTransport(final HttpTransport a1) {
            /*SL:61*/return (Builder)super.setTransport(a1);
        }
        
        public Builder setClientAuthentication(final HttpExecuteInterceptor a1) {
            /*SL:66*/return (Builder)super.setClientAuthentication(a1);
        }
        
        public Builder setJsonFactory(final JsonFactory a1) {
            /*SL:71*/return (Builder)super.setJsonFactory(a1);
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:76*/return (Builder)super.setClock(a1);
        }
        
        public MockGoogleCredential build() {
            /*SL:81*/if (this.getTransport() == null) {
                /*SL:82*/this.setTransport(new MockHttpTransport.Builder().build());
            }
            /*SL:84*/if (this.getClientAuthentication() == null) {
                /*SL:85*/this.setClientAuthentication(new MockClientAuthentication());
            }
            /*SL:87*/if (this.getJsonFactory() == null) {
                /*SL:88*/this.setJsonFactory(new JacksonFactory());
            }
            /*SL:90*/return new MockGoogleCredential(this);
        }
    }
    
    @Beta
    private static class MockClientAuthentication implements HttpExecuteInterceptor
    {
        public void intercept(final HttpRequest a1) throws IOException {
        }
    }
}
