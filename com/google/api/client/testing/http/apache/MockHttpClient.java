package com.google.api.client.testing.http.apache;

import com.google.api.client.util.Preconditions;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.ProtocolVersion;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;
import org.apache.http.client.RequestDirector;
import org.apache.http.params.HttpParams;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.protocol.HttpRequestExecutor;
import com.google.api.client.util.Beta;
import org.apache.http.impl.client.DefaultHttpClient;

@Beta
public class MockHttpClient extends DefaultHttpClient
{
    int responseCode;
    
    protected RequestDirector createClientRequestDirector(final HttpRequestExecutor a1, final ClientConnectionManager a2, final ConnectionReuseStrategy a3, final ConnectionKeepAliveStrategy a4, final HttpRoutePlanner a5, final HttpProcessor a6, final HttpRequestRetryHandler a7, final RedirectHandler a8, final AuthenticationHandler a9, final AuthenticationHandler a10, final UserTokenHandler a11, final HttpParams a12) {
        /*SL:66*/return (RequestDirector)new RequestDirector() {
            @Beta
            public HttpResponse execute(final HttpHost a1, final HttpRequest a2, final HttpContext a3) throws HttpException, IOException {
                /*SL:70*/return (HttpResponse)new BasicHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, MockHttpClient.this.responseCode, (String)null);
            }
        };
    }
    
    public final int getResponseCode() {
        /*SL:77*/return this.responseCode;
    }
    
    public MockHttpClient setResponseCode(final int a1) {
        /*SL:82*/Preconditions.checkArgument(a1 >= 0);
        /*SL:83*/this.responseCode = a1;
        /*SL:84*/return this;
    }
}
