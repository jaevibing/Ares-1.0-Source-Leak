package com.google.api.client.testing.http;

import java.util.Collections;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.LowLevelHttpRequest;
import java.io.IOException;
import java.util.Set;
import com.google.api.client.util.Beta;
import com.google.api.client.http.HttpTransport;

@Beta
public class MockHttpTransport extends HttpTransport
{
    private Set<String> supportedMethods;
    private MockLowLevelHttpRequest lowLevelHttpRequest;
    private MockLowLevelHttpResponse lowLevelHttpResponse;
    
    public MockHttpTransport() {
    }
    
    protected MockHttpTransport(final Builder a1) {
        this.supportedMethods = a1.supportedMethods;
        this.lowLevelHttpRequest = a1.lowLevelHttpRequest;
        this.lowLevelHttpResponse = a1.lowLevelHttpResponse;
    }
    
    @Override
    public boolean supportsMethod(final String a1) throws IOException {
        /*SL:75*/return this.supportedMethods == null || this.supportedMethods.contains(a1);
    }
    
    public LowLevelHttpRequest buildRequest(final String a1, final String a2) throws IOException {
        /*SL:80*/Preconditions.checkArgument(this.supportsMethod(a1), "HTTP method %s not supported", a1);
        /*SL:81*/if (this.lowLevelHttpRequest != null) {
            /*SL:82*/return this.lowLevelHttpRequest;
        }
        /*SL:84*/this.lowLevelHttpRequest = new MockLowLevelHttpRequest(a2);
        /*SL:85*/if (this.lowLevelHttpResponse != null) {
            /*SL:86*/this.lowLevelHttpRequest.setResponse(this.lowLevelHttpResponse);
        }
        /*SL:88*/return this.lowLevelHttpRequest;
    }
    
    public final Set<String> getSupportedMethods() {
        /*SL:96*/return (this.supportedMethods == null) ? null : Collections.<String>unmodifiableSet((Set<? extends String>)this.supportedMethods);
    }
    
    public final MockLowLevelHttpRequest getLowLevelHttpRequest() {
        /*SL:106*/return this.lowLevelHttpRequest;
    }
    
    @Deprecated
    public static Builder builder() {
        /*SL:120*/return new Builder();
    }
    
    @Beta
    public static class Builder
    {
        Set<String> supportedMethods;
        MockLowLevelHttpRequest lowLevelHttpRequest;
        MockLowLevelHttpResponse lowLevelHttpResponse;
        
        public MockHttpTransport build() {
            /*SL:164*/return new MockHttpTransport(this);
        }
        
        public final Set<String> getSupportedMethods() {
            /*SL:171*/return this.supportedMethods;
        }
        
        public final Builder setSupportedMethods(final Set<String> a1) {
            /*SL:178*/this.supportedMethods = a1;
            /*SL:179*/return this;
        }
        
        public final Builder setLowLevelHttpRequest(final MockLowLevelHttpRequest a1) {
            /*SL:193*/Preconditions.checkState(this.lowLevelHttpResponse == null, (Object)"Cannnot set a low level HTTP request when a low level HTTP response has been set.");
            /*SL:195*/this.lowLevelHttpRequest = a1;
            /*SL:196*/return this;
        }
        
        public final MockLowLevelHttpRequest getLowLevelHttpRequest() {
            /*SL:206*/return this.lowLevelHttpRequest;
        }
        
        public final Builder setLowLevelHttpResponse(final MockLowLevelHttpResponse a1) {
            /*SL:222*/Preconditions.checkState(this.lowLevelHttpRequest == null, (Object)"Cannot set a low level HTTP response when a low level HTTP request has been set.");
            /*SL:224*/this.lowLevelHttpResponse = a1;
            /*SL:225*/return this;
        }
        
        MockLowLevelHttpResponse getLowLevelHttpResponse() {
            /*SL:236*/return this.lowLevelHttpResponse;
        }
    }
}
