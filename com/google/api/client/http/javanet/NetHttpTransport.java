package com.google.api.client.http.javanet;

import com.google.api.client.util.Beta;
import javax.net.ssl.SSLContext;
import com.google.api.client.util.SslUtils;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import com.google.api.client.util.SecurityUtils;
import java.io.InputStream;
import com.google.api.client.http.LowLevelHttpRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import com.google.api.client.util.Preconditions;
import java.util.Arrays;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import com.google.api.client.http.HttpTransport;

public final class NetHttpTransport extends HttpTransport
{
    private static final String[] SUPPORTED_METHODS;
    private static final String SHOULD_USE_PROXY_FLAG = "com.google.api.client.should_use_proxy";
    private final ConnectionFactory connectionFactory;
    private final SSLSocketFactory sslSocketFactory;
    private final HostnameVerifier hostnameVerifier;
    
    private static Proxy defaultProxy() {
        /*SL:63*/return new Proxy(Proxy.Type.HTTP, /*EL:66*/new InetSocketAddress(System.getProperty("https.proxyHost"), Integer.parseInt(System.getProperty("https.proxyPort"))));
    }
    
    public NetHttpTransport() {
        this((ConnectionFactory)null, null, null);
    }
    
    NetHttpTransport(final Proxy a1, final SSLSocketFactory a2, final HostnameVerifier a3) {
        this(new DefaultConnectionFactory(a1), a2, a3);
    }
    
    NetHttpTransport(final ConnectionFactory a1, final SSLSocketFactory a2, final HostnameVerifier a3) {
        this.connectionFactory = this.getConnectionFactory(a1);
        this.sslSocketFactory = a2;
        this.hostnameVerifier = a3;
    }
    
    private ConnectionFactory getConnectionFactory(final ConnectionFactory a1) {
        /*SL:134*/if (a1 != null) {
            /*SL:140*/return a1;
        }
        if (System.getProperty("com.google.api.client.should_use_proxy") != null) {
            return new DefaultConnectionFactory(defaultProxy());
        }
        return new DefaultConnectionFactory();
    }
    
    @Override
    public boolean supportsMethod(final String a1) {
        /*SL:145*/return Arrays.binarySearch(NetHttpTransport.SUPPORTED_METHODS, a1) >= 0;
    }
    
    @Override
    protected NetHttpRequest buildRequest(final String v1, final String v2) throws IOException {
        /*SL:150*/Preconditions.checkArgument(this.supportsMethod(v1), "HTTP method %s not supported", v1);
        final URL v3 = /*EL:152*/new URL(v2);
        final HttpURLConnection v4 = /*EL:153*/this.connectionFactory.openConnection(v3);
        /*SL:154*/v4.setRequestMethod(v1);
        /*SL:156*/if (v4 instanceof HttpsURLConnection) {
            final HttpsURLConnection a1 = /*EL:157*/(HttpsURLConnection)v4;
            /*SL:158*/if (this.hostnameVerifier != null) {
                /*SL:159*/a1.setHostnameVerifier(this.hostnameVerifier);
            }
            /*SL:161*/if (this.sslSocketFactory != null) {
                /*SL:162*/a1.setSSLSocketFactory(this.sslSocketFactory);
            }
        }
        /*SL:165*/return new NetHttpRequest(v4);
    }
    
    static {
        Arrays.sort(SUPPORTED_METHODS = new String[] { "DELETE", "GET", "HEAD", "OPTIONS", "POST", "PUT", "TRACE" });
    }
    
    public static final class Builder
    {
        private SSLSocketFactory sslSocketFactory;
        private HostnameVerifier hostnameVerifier;
        private Proxy proxy;
        private ConnectionFactory connectionFactory;
        
        public Builder setProxy(final Proxy a1) {
            /*SL:212*/this.proxy = a1;
            /*SL:213*/return this;
        }
        
        public Builder setConnectionFactory(final ConnectionFactory a1) {
            /*SL:228*/this.connectionFactory = a1;
            /*SL:229*/return this;
        }
        
        public Builder trustCertificatesFromJavaKeyStore(final InputStream a1, final String a2) throws GeneralSecurityException, IOException {
            final KeyStore v1 = /*EL:250*/SecurityUtils.getJavaKeyStore();
            /*SL:251*/SecurityUtils.loadKeyStore(v1, a1, a2);
            /*SL:252*/return this.trustCertificates(v1);
        }
        
        public Builder trustCertificatesFromStream(final InputStream a1) throws GeneralSecurityException, IOException {
            final KeyStore v1 = /*EL:272*/SecurityUtils.getJavaKeyStore();
            /*SL:273*/v1.load(null, null);
            /*SL:274*/SecurityUtils.loadKeyStoreFromCertificates(v1, /*EL:275*/SecurityUtils.getX509CertificateFactory(), a1);
            /*SL:276*/return this.trustCertificates(v1);
        }
        
        public Builder trustCertificates(final KeyStore a1) throws GeneralSecurityException {
            final SSLContext v1 = /*EL:287*/SslUtils.getTlsSslContext();
            /*SL:288*/SslUtils.initSslContext(v1, a1, SslUtils.getPkixTrustManagerFactory());
            /*SL:289*/return this.setSslSocketFactory(v1.getSocketFactory());
        }
        
        @Beta
        public Builder doNotValidateCertificate() throws GeneralSecurityException {
            /*SL:305*/this.hostnameVerifier = SslUtils.trustAllHostnameVerifier();
            /*SL:306*/this.sslSocketFactory = SslUtils.trustAllSSLContext().getSocketFactory();
            /*SL:307*/return this;
        }
        
        public SSLSocketFactory getSslSocketFactory() {
            /*SL:312*/return this.sslSocketFactory;
        }
        
        public Builder setSslSocketFactory(final SSLSocketFactory a1) {
            /*SL:317*/this.sslSocketFactory = a1;
            /*SL:318*/return this;
        }
        
        public HostnameVerifier getHostnameVerifier() {
            /*SL:323*/return this.hostnameVerifier;
        }
        
        public Builder setHostnameVerifier(final HostnameVerifier a1) {
            /*SL:328*/this.hostnameVerifier = a1;
            /*SL:329*/return this;
        }
        
        public NetHttpTransport build() {
            /*SL:334*/if (System.getProperty("com.google.api.client.should_use_proxy") != null) {
                /*SL:335*/this.setProxy(defaultProxy());
            }
            /*SL:337*/return (this.proxy == null) ? new NetHttpTransport(this.connectionFactory, this.sslSocketFactory, this.hostnameVerifier) : new NetHttpTransport(this.proxy, this.sslSocketFactory, this.hostnameVerifier);
        }
    }
}
