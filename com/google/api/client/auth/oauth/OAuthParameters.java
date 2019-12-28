package com.google.api.client.auth.oauth;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.escape.PercentEscaper;
import java.security.SecureRandom;
import com.google.api.client.util.Beta;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpExecuteInterceptor;

@Beta
public final class OAuthParameters implements HttpExecuteInterceptor, HttpRequestInitializer
{
    private static final SecureRandom RANDOM;
    public OAuthSigner signer;
    public String callback;
    public String consumerKey;
    public String nonce;
    public String realm;
    public String signature;
    public String signatureMethod;
    public String timestamp;
    public String token;
    public String verifier;
    public String version;
    private static final PercentEscaper ESCAPER;
    
    public void computeNonce() {
        /*SL:120*/this.nonce = Long.toHexString(Math.abs(OAuthParameters.RANDOM.nextLong()));
    }
    
    public void computeTimestamp() {
        /*SL:128*/this.timestamp = Long.toString(System.currentTimeMillis() / 1000L);
    }
    
    public void computeSignature(final String v-6, final GenericUrl v-5) throws GeneralSecurityException {
        final OAuthSigner signer = /*EL:139*/this.signer;
        final String signatureMethod = /*EL:140*/signer.getSignatureMethod();
        this.signatureMethod = signatureMethod;
        final String a4 = signatureMethod;
        final TreeMap<String, String> treeMap = /*EL:142*/new TreeMap<String, String>();
        /*SL:143*/this.putParameterIfValueNotNull(treeMap, "oauth_callback", this.callback);
        /*SL:144*/this.putParameterIfValueNotNull(treeMap, "oauth_consumer_key", this.consumerKey);
        /*SL:145*/this.putParameterIfValueNotNull(treeMap, "oauth_nonce", this.nonce);
        /*SL:146*/this.putParameterIfValueNotNull(treeMap, "oauth_signature_method", a4);
        /*SL:147*/this.putParameterIfValueNotNull(treeMap, "oauth_timestamp", this.timestamp);
        /*SL:148*/this.putParameterIfValueNotNull(treeMap, "oauth_token", this.token);
        /*SL:149*/this.putParameterIfValueNotNull(treeMap, "oauth_verifier", this.verifier);
        /*SL:150*/this.putParameterIfValueNotNull(treeMap, "oauth_version", this.version);
        /*SL:152*/for (final Map.Entry<String, Object> v0 : v-5.entrySet()) {
            final Object v = /*EL:153*/v0.getValue();
            /*SL:154*/if (v != null) {
                String a2 = /*EL:155*/v0.getKey();
                /*SL:156*/if (v instanceof Collection) {
                    final Iterator iterator2 = /*EL:157*/((Collection)v).iterator();
                    while (iterator2.hasNext()) {
                        a2 = iterator2.next();
                        /*SL:158*/this.putParameter(treeMap, a2, a2);
                    }
                }
                else {
                    /*SL:161*/this.putParameter(treeMap, a2, v);
                }
            }
        }
        final StringBuilder sb = /*EL:166*/new StringBuilder();
        boolean v2 = /*EL:167*/true;
        /*SL:168*/for (final Map.Entry<String, String> a3 : treeMap.entrySet()) {
            /*SL:169*/if (v2) {
                /*SL:170*/v2 = false;
            }
            else {
                /*SL:172*/sb.append('&');
            }
            /*SL:174*/sb.append(a3.getKey());
            final String v3 = /*EL:175*/a3.getValue();
            /*SL:176*/if (v3 != null) {
                /*SL:177*/sb.append('=').append(v3);
            }
        }
        final String v4 = /*EL:180*/sb.toString();
        final GenericUrl v5 = /*EL:182*/new GenericUrl();
        final String v3 = /*EL:183*/v-5.getScheme();
        /*SL:184*/v5.setScheme(v3);
        /*SL:185*/v5.setHost(v-5.getHost());
        /*SL:186*/v5.setPathParts(v-5.getPathParts());
        int v6 = /*EL:187*/v-5.getPort();
        /*SL:188*/if (("http".equals(v3) && v6 == 80) || ("https".equals(v3) && v6 == 443)) {
            /*SL:189*/v6 = -1;
        }
        /*SL:191*/v5.setPort(v6);
        final String v7 = /*EL:192*/v5.build();
        final StringBuilder v8 = /*EL:194*/new StringBuilder();
        /*SL:195*/v8.append(escape(v-6)).append('&');
        /*SL:196*/v8.append(escape(v7)).append('&');
        /*SL:197*/v8.append(escape(v4));
        final String v9 = /*EL:198*/v8.toString();
        /*SL:199*/this.signature = signer.computeSignature(v9);
    }
    
    public String getAuthorizationHeader() {
        final StringBuilder v1 = /*EL:207*/new StringBuilder("OAuth");
        /*SL:208*/this.appendParameter(v1, "realm", this.realm);
        /*SL:209*/this.appendParameter(v1, "oauth_callback", this.callback);
        /*SL:210*/this.appendParameter(v1, "oauth_consumer_key", this.consumerKey);
        /*SL:211*/this.appendParameter(v1, "oauth_nonce", this.nonce);
        /*SL:212*/this.appendParameter(v1, "oauth_signature", this.signature);
        /*SL:213*/this.appendParameter(v1, "oauth_signature_method", this.signatureMethod);
        /*SL:214*/this.appendParameter(v1, "oauth_timestamp", this.timestamp);
        /*SL:215*/this.appendParameter(v1, "oauth_token", this.token);
        /*SL:216*/this.appendParameter(v1, "oauth_verifier", this.verifier);
        /*SL:217*/this.appendParameter(v1, "oauth_version", this.version);
        /*SL:219*/return v1.substring(0, v1.length() - 1);
    }
    
    private void appendParameter(final StringBuilder a1, final String a2, final String a3) {
        /*SL:223*/if (a3 != null) {
            /*SL:224*/a1.append(' ').append(escape(a2)).append("=\"").append(escape(a3)).append("\",");
        }
    }
    
    private void putParameterIfValueNotNull(final TreeMap<String, String> a1, final String a2, final String a3) {
        /*SL:230*/if (a3 != null) {
            /*SL:231*/this.putParameter(a1, a2, a3);
        }
    }
    
    private void putParameter(final TreeMap<String, String> a1, final String a2, final Object a3) {
        /*SL:236*/a1.put(escape(a2), (a3 == null) ? null : escape(a3.toString()));
    }
    
    public static String escape(final String a1) {
        /*SL:241*/return OAuthParameters.ESCAPER.escape(a1);
    }
    
    @Override
    public void initialize(final HttpRequest a1) throws IOException {
        /*SL:245*/a1.setInterceptor(this);
    }
    
    @Override
    public void intercept(final HttpRequest v0) throws IOException {
        /*SL:249*/this.computeNonce();
        /*SL:250*/this.computeTimestamp();
        try {
            /*SL:252*/this.computeSignature(v0.getRequestMethod(), v0.getUrl());
        }
        catch (GeneralSecurityException v) {
            final IOException a1 = /*EL:254*/new IOException();
            /*SL:255*/a1.initCause(v);
            /*SL:256*/throw a1;
        }
        /*SL:258*/v0.getHeaders().setAuthorization(this.getAuthorizationHeader());
    }
    
    static {
        RANDOM = new SecureRandom();
        ESCAPER = new PercentEscaper("-_.~", false);
    }
}
