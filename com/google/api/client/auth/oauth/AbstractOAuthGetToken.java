package com.google.api.client.auth.oauth;

import java.io.IOException;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.UrlEncodedParser;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;
import com.google.api.client.http.GenericUrl;

@Beta
public abstract class AbstractOAuthGetToken extends GenericUrl
{
    public HttpTransport transport;
    public String consumerKey;
    public OAuthSigner signer;
    protected boolean usePost;
    
    protected AbstractOAuthGetToken(final String a1) {
        super(a1);
    }
    
    public final OAuthCredentialsResponse execute() throws IOException {
        final HttpRequestFactory v1 = /*EL:69*/this.transport.createRequestFactory();
        final HttpRequest v2 = /*EL:70*/v1.buildRequest(this.usePost ? "POST" : "GET", this, null);
        /*SL:72*/this.createParameters().intercept(v2);
        final HttpResponse v3 = /*EL:73*/v2.execute();
        /*SL:74*/v3.setContentLoggingLimit(0);
        final OAuthCredentialsResponse v4 = /*EL:75*/new OAuthCredentialsResponse();
        /*SL:76*/UrlEncodedParser.parse(v3.parseAsString(), v4);
        /*SL:77*/return v4;
    }
    
    public OAuthParameters createParameters() {
        final OAuthParameters v1 = /*EL:85*/new OAuthParameters();
        /*SL:86*/v1.consumerKey = this.consumerKey;
        /*SL:87*/v1.signer = this.signer;
        /*SL:88*/return v1;
    }
}
