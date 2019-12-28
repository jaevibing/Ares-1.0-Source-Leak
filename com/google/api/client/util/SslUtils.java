package com.google.api.client.util;

import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;

public final class SslUtils
{
    public static SSLContext getSslContext() throws NoSuchAlgorithmException {
        /*SL:45*/return SSLContext.getInstance("SSL");
    }
    
    public static SSLContext getTlsSslContext() throws NoSuchAlgorithmException {
        /*SL:54*/return SSLContext.getInstance("TLS");
    }
    
    public static TrustManagerFactory getDefaultTrustManagerFactory() throws NoSuchAlgorithmException {
        /*SL:64*/return TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    }
    
    public static TrustManagerFactory getPkixTrustManagerFactory() throws NoSuchAlgorithmException {
        /*SL:73*/return TrustManagerFactory.getInstance("PKIX");
    }
    
    public static KeyManagerFactory getDefaultKeyManagerFactory() throws NoSuchAlgorithmException {
        /*SL:82*/return KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    }
    
    public static KeyManagerFactory getPkixKeyManagerFactory() throws NoSuchAlgorithmException {
        /*SL:91*/return KeyManagerFactory.getInstance("PKIX");
    }
    
    public static SSLContext initSslContext(final SSLContext a1, final KeyStore a2, final TrustManagerFactory a3) throws GeneralSecurityException {
        /*SL:109*/a3.init(a2);
        /*SL:110*/a1.init(null, a3.getTrustManagers(), null);
        /*SL:111*/return a1;
    }
    
    @Beta
    public static SSLContext trustAllSSLContext() throws GeneralSecurityException {
        final TrustManager[] v1 = /*EL:125*/{ new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] a1, final String a2) throws CertificateException {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] a1, final String a2) throws CertificateException {
                }
                
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    /*SL:136*/return null;
                }
            } };
        final SSLContext v2 = getTlsSslContext();
        /*SL:140*/v2.init(null, v1, null);
        /*SL:141*/return v2;
    }
    
    @Beta
    public static HostnameVerifier trustAllHostnameVerifier() {
        /*SL:155*/return new HostnameVerifier() {
            @Override
            public boolean verify(final String a1, final SSLSession a2) {
                /*SL:158*/return true;
            }
        };
    }
}
