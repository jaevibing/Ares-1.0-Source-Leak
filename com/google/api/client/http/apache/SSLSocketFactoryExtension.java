package com.google.api.client.http.apache;

import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.SSLSocketFactory;

final class SSLSocketFactoryExtension extends SSLSocketFactory
{
    private final javax.net.ssl.SSLSocketFactory socketFactory;
    
    SSLSocketFactoryExtension(final SSLContext a1) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super((KeyStore)null);
        this.socketFactory = a1.getSocketFactory();
    }
    
    public Socket createSocket() throws IOException {
        /*SL:51*/return this.socketFactory.createSocket();
    }
    
    public Socket createSocket(final Socket a1, final String a2, final int a3, final boolean a4) throws IOException, UnknownHostException {
        final SSLSocket v1 = /*EL:57*/(SSLSocket)this.socketFactory.createSocket(a1, a2, a3, a4);
        /*SL:58*/this.getHostnameVerifier().verify(a2, v1);
        /*SL:59*/return v1;
    }
}
