package com.google.api.client.auth.oauth2;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.UrlEncodedContent;
import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.Joiner;
import java.util.Collection;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.GenericData;

public class TokenRequest extends GenericData
{
    HttpRequestInitializer requestInitializer;
    HttpExecuteInterceptor clientAuthentication;
    private final HttpTransport transport;
    private final JsonFactory jsonFactory;
    private GenericUrl tokenServerUrl;
    @Key("scope")
    private String scopes;
    @Key("grant_type")
    private String grantType;
    
    public TokenRequest(final HttpTransport a1, final JsonFactory a2, final GenericUrl a3, final String a4) {
        this.transport = Preconditions.<HttpTransport>checkNotNull(a1);
        this.jsonFactory = Preconditions.<JsonFactory>checkNotNull(a2);
        this.setTokenServerUrl(a3);
        this.setGrantType(a4);
    }
    
    public final HttpTransport getTransport() {
        /*SL:102*/return this.transport;
    }
    
    public final JsonFactory getJsonFactory() {
        /*SL:107*/return this.jsonFactory;
    }
    
    public final HttpRequestInitializer getRequestInitializer() {
        /*SL:112*/return this.requestInitializer;
    }
    
    public TokenRequest setRequestInitializer(final HttpRequestInitializer a1) {
        /*SL:124*/this.requestInitializer = a1;
        /*SL:125*/return this;
    }
    
    public final HttpExecuteInterceptor getClientAuthentication() {
        /*SL:130*/return this.clientAuthentication;
    }
    
    public TokenRequest setClientAuthentication(final HttpExecuteInterceptor a1) {
        /*SL:155*/this.clientAuthentication = a1;
        /*SL:156*/return this;
    }
    
    public final GenericUrl getTokenServerUrl() {
        /*SL:161*/return this.tokenServerUrl;
    }
    
    public TokenRequest setTokenServerUrl(final GenericUrl a1) {
        /*SL:173*/this.tokenServerUrl = a1;
        /*SL:174*/Preconditions.checkArgument(a1.getFragment() == null);
        /*SL:175*/return this;
    }
    
    public final String getScopes() {
        /*SL:184*/return this.scopes;
    }
    
    public TokenRequest setScopes(final Collection<String> a1) {
        /*SL:202*/this.scopes = ((a1 == null) ? null : Joiner.on(' ').join(a1));
        /*SL:203*/return this;
    }
    
    public final String getGrantType() {
        /*SL:212*/return this.grantType;
    }
    
    public TokenRequest setGrantType(final String a1) {
        /*SL:226*/this.grantType = Preconditions.<String>checkNotNull(a1);
        /*SL:227*/return this;
    }
    
    public final HttpResponse executeUnparsed() throws IOException {
        final HttpRequestFactory v1 = /*EL:258*/this.transport.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(final HttpRequest a1) throws IOException {
                /*SL:262*/if (TokenRequest.this.requestInitializer != null) {
                    /*SL:263*/TokenRequest.this.requestInitializer.initialize(a1);
                }
                final HttpExecuteInterceptor v1 = /*EL:265*/a1.getInterceptor();
                /*SL:266*/a1.setInterceptor(new HttpExecuteInterceptor() {
                    @Override
                    public void intercept(final HttpRequest a1) throws IOException {
                        /*SL:268*/if (/*EL:272*/v1 != null) {
                            v1.intercept(a1);
                        }
                        if (TokenRequest.this.clientAuthentication != null) {
                            TokenRequest.this.clientAuthentication.intercept(a1);
                        }
                    }
                });
            }
        });
        final HttpRequest v2 = /*EL:279*/v1.buildPostRequest(this.tokenServerUrl, new UrlEncodedContent(this));
        /*SL:281*/v2.setParser(new JsonObjectParser(this.jsonFactory));
        /*SL:282*/v2.setThrowExceptionOnExecuteError(false);
        final HttpResponse v3 = /*EL:283*/v2.execute();
        /*SL:284*/if (v3.isSuccessStatusCode()) {
            /*SL:285*/return v3;
        }
        /*SL:287*/throw TokenResponseException.from(this.jsonFactory, v3);
    }
    
    public TokenResponse execute() throws IOException {
        /*SL:307*/return this.executeUnparsed().<TokenResponse>parseAs(TokenResponse.class);
    }
    
    @Override
    public TokenRequest set(final String a1, final Object a2) {
        /*SL:312*/return (TokenRequest)super.set(a1, a2);
    }
}
