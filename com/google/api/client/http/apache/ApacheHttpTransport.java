package com.google.api.client.http.apache;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Beta;
import javax.net.ssl.SSLContext;
import com.google.api.client.util.SslUtils;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import com.google.api.client.util.SecurityUtils;
import java.io.InputStream;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.HttpHost;
import java.io.IOException;
import com.google.api.client.http.LowLevelHttpRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.BasicHttpParams;
import java.net.ProxySelector;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.ProtocolVersion;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import com.google.api.client.http.HttpTransport;

public final class ApacheHttpTransport extends HttpTransport
{
    private final HttpClient httpClient;
    
    public ApacheHttpTransport() {
        this((HttpClient)newDefaultHttpClient());
    }
    
    public ApacheHttpTransport(final HttpClient a1) {
        this.httpClient = a1;
        HttpParams v1 = a1.getParams();
        if (v1 == null) {
            v1 = newDefaultHttpClient().getParams();
        }
        HttpProtocolParams.setVersion(v1, (ProtocolVersion)HttpVersion.HTTP_1_1);
        v1.setBooleanParameter("http.protocol.handle-redirects", false);
    }
    
    public static DefaultHttpClient newDefaultHttpClient() {
        /*SL:157*/return newDefaultHttpClient(/*EL:158*/SSLSocketFactory.getSocketFactory(), newDefaultHttpParams(), ProxySelector.getDefault());
    }
    
    static HttpParams newDefaultHttpParams() {
        final HttpParams v1 = /*EL:163*/(HttpParams)new BasicHttpParams();
        /*SL:166*/HttpConnectionParams.setStaleCheckingEnabled(v1, false);
        /*SL:167*/HttpConnectionParams.setSocketBufferSize(v1, 8192);
        /*SL:168*/ConnManagerParams.setMaxTotalConnections(v1, 200);
        /*SL:169*/ConnManagerParams.setMaxConnectionsPerRoute(v1, (ConnPerRoute)new ConnPerRouteBean(20));
        /*SL:170*/return v1;
    }
    
    static DefaultHttpClient newDefaultHttpClient(final SSLSocketFactory a1, final HttpParams a2, final ProxySelector a3) {
        final SchemeRegistry v1 = /*EL:186*/new SchemeRegistry();
        /*SL:187*/v1.register(new Scheme("http", (SocketFactory)PlainSocketFactory.getSocketFactory(), 80));
        /*SL:188*/v1.register(new Scheme("https", (SocketFactory)a1, 443));
        final ClientConnectionManager v2 = /*EL:189*/(ClientConnectionManager)new ThreadSafeClientConnManager(a2, v1);
        final DefaultHttpClient v3 = /*EL:190*/new DefaultHttpClient(v2, a2);
        /*SL:191*/v3.setHttpRequestRetryHandler((HttpRequestRetryHandler)new DefaultHttpRequestRetryHandler(0, false));
        /*SL:192*/if (a3 != null) {
            /*SL:193*/v3.setRoutePlanner((HttpRoutePlanner)new ProxySelectorRoutePlanner(v1, a3));
        }
        /*SL:195*/return v3;
    }
    
    @Override
    public boolean supportsMethod(final String a1) {
        /*SL:200*/return true;
    }
    
    @Override
    protected ApacheHttpRequest buildRequest(final String v-1, final String v0) {
        HttpRequestBase v = null;
        /*SL:206*/if (v-1.equals("DELETE")) {
            final HttpRequestBase a1 = /*EL:207*/(HttpRequestBase)new HttpDelete(v0);
        }
        else/*SL:208*/ if (v-1.equals("GET")) {
            final HttpRequestBase a2 = /*EL:209*/(HttpRequestBase)new HttpGet(v0);
        }
        else/*SL:210*/ if (v-1.equals("HEAD")) {
            /*SL:211*/v = (HttpRequestBase)new HttpHead(v0);
        }
        else/*SL:212*/ if (v-1.equals("POST")) {
            /*SL:213*/v = (HttpRequestBase)new HttpPost(v0);
        }
        else/*SL:214*/ if (v-1.equals("PUT")) {
            /*SL:215*/v = (HttpRequestBase)new HttpPut(v0);
        }
        else/*SL:216*/ if (v-1.equals("TRACE")) {
            /*SL:217*/v = (HttpRequestBase)new HttpTrace(v0);
        }
        else/*SL:218*/ if (v-1.equals("OPTIONS")) {
            /*SL:219*/v = (HttpRequestBase)new HttpOptions(v0);
        }
        else {
            /*SL:221*/v = (HttpRequestBase)new HttpExtensionMethod(v-1, v0);
        }
        /*SL:223*/return new ApacheHttpRequest(this.httpClient, v);
    }
    
    @Override
    public void shutdown() {
        /*SL:234*/this.httpClient.getConnectionManager().shutdown();
    }
    
    public HttpClient getHttpClient() {
        /*SL:243*/return this.httpClient;
    }
    
    public static final class Builder
    {
        private SSLSocketFactory socketFactory;
        private HttpParams params;
        private ProxySelector proxySelector;
        
        public Builder() {
            this.socketFactory = SSLSocketFactory.getSocketFactory();
            this.params = ApacheHttpTransport.newDefaultHttpParams();
            this.proxySelector = ProxySelector.getDefault();
        }
        
        public Builder setProxy(final HttpHost a1) {
            /*SL:288*/ConnRouteParams.setDefaultProxy(this.params, a1);
            /*SL:289*/if (a1 != null) {
                /*SL:290*/this.proxySelector = null;
            }
            /*SL:292*/return this;
        }
        
        public Builder setProxySelector(final ProxySelector a1) {
            /*SL:306*/this.proxySelector = a1;
            /*SL:307*/if (a1 != null) {
                /*SL:308*/ConnRouteParams.setDefaultProxy(this.params, (HttpHost)null);
            }
            /*SL:310*/return this;
        }
        
        public Builder trustCertificatesFromJavaKeyStore(final InputStream a1, final String a2) throws GeneralSecurityException, IOException {
            final KeyStore v1 = /*EL:330*/SecurityUtils.getJavaKeyStore();
            /*SL:331*/SecurityUtils.loadKeyStore(v1, a1, a2);
            /*SL:332*/return this.trustCertificates(v1);
        }
        
        public Builder trustCertificatesFromStream(final InputStream a1) throws GeneralSecurityException, IOException {
            final KeyStore v1 = /*EL:352*/SecurityUtils.getJavaKeyStore();
            /*SL:353*/v1.load(null, null);
            /*SL:354*/SecurityUtils.loadKeyStoreFromCertificates(v1, /*EL:355*/SecurityUtils.getX509CertificateFactory(), a1);
            /*SL:356*/return this.trustCertificates(v1);
        }
        
        public Builder trustCertificates(final KeyStore a1) throws GeneralSecurityException {
            final SSLContext v1 = /*EL:368*/SslUtils.getTlsSslContext();
            /*SL:369*/SslUtils.initSslContext(v1, a1, SslUtils.getPkixTrustManagerFactory());
            /*SL:370*/return this.setSocketFactory(new SSLSocketFactoryExtension(v1));
        }
        
        @Beta
        public Builder doNotValidateCertificate() throws GeneralSecurityException {
            /*SL:386*/(this.socketFactory = new SSLSocketFactoryExtension(SslUtils.trustAllSSLContext())).setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            /*SL:388*/return this;
        }
        
        public Builder setSocketFactory(final SSLSocketFactory a1) {
            /*SL:393*/this.socketFactory = Preconditions.<SSLSocketFactory>checkNotNull(a1);
            /*SL:394*/return this;
        }
        
        public SSLSocketFactory getSSLSocketFactory() {
            /*SL:399*/return this.socketFactory;
        }
        
        public HttpParams getHttpParams() {
            /*SL:404*/return this.params;
        }
        
        public ApacheHttpTransport build() {
            /*SL:409*/return new ApacheHttpTransport((HttpClient)ApacheHttpTransport.newDefaultHttpClient(this.socketFactory, this.params, this.proxySelector));
        }
    }
}
