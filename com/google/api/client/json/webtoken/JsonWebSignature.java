package com.google.api.client.json.webtoken;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import com.google.api.client.util.GenericData;
import com.google.api.client.json.GenericJson;
import java.util.ArrayList;
import com.google.api.client.util.Key;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Base64;
import java.security.PrivateKey;
import java.io.IOException;
import com.google.api.client.json.JsonFactory;
import javax.net.ssl.TrustManager;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStore;
import javax.net.ssl.TrustManagerFactory;
import com.google.api.client.util.Beta;
import java.util.List;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.Signature;
import com.google.api.client.util.SecurityUtils;
import java.security.PublicKey;
import com.google.api.client.util.Preconditions;

public class JsonWebSignature extends JsonWebToken
{
    private final byte[] signatureBytes;
    private final byte[] signedContentBytes;
    
    public JsonWebSignature(final Header a1, final Payload a2, final byte[] a3, final byte[] a4) {
        super(a1, a2);
        this.signatureBytes = Preconditions.<byte[]>checkNotNull(a3);
        this.signedContentBytes = Preconditions.<byte[]>checkNotNull(a4);
    }
    
    @Override
    public Header getHeader() {
        /*SL:400*/return (Header)super.getHeader();
    }
    
    public final boolean verifySignature(final PublicKey a1) throws GeneralSecurityException {
        Signature v1 = /*EL:416*/null;
        final String v2 = /*EL:417*/this.getHeader().getAlgorithm();
        /*SL:418*/if ("RS256".equals(v2)) {
            /*SL:419*/v1 = SecurityUtils.getSha256WithRsaSignatureAlgorithm();
            /*SL:423*/return SecurityUtils.verify(v1, a1, this.signatureBytes, this.signedContentBytes);
        }
        return false;
    }
    
    @Beta
    public final X509Certificate verifySignature(final X509TrustManager a1) throws GeneralSecurityException {
        final List<String> v1 = /*EL:448*/this.getHeader().getX509Certificates();
        /*SL:449*/if (v1 == null || v1.isEmpty()) {
            /*SL:450*/return null;
        }
        final String v2 = /*EL:452*/this.getHeader().getAlgorithm();
        Signature v3 = /*EL:453*/null;
        /*SL:454*/if ("RS256".equals(v2)) {
            /*SL:455*/v3 = SecurityUtils.getSha256WithRsaSignatureAlgorithm();
            /*SL:459*/return SecurityUtils.verify(v3, a1, v1, this.signatureBytes, this.signedContentBytes);
        }
        return null;
    }
    
    @Beta
    public final X509Certificate verifySignature() throws GeneralSecurityException {
        final X509TrustManager v1 = getDefaultX509TrustManager();
        /*SL:487*/if (v1 == null) {
            /*SL:488*/return null;
        }
        /*SL:490*/return this.verifySignature(v1);
    }
    
    private static X509TrustManager getDefaultX509TrustManager() {
        try {
            final TrustManagerFactory instance = /*EL:496*/TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            /*SL:497*/instance.init((KeyStore)null);
            /*SL:498*/for (final TrustManager v1 : instance.getTrustManagers()) {
                /*SL:499*/if (v1 instanceof X509TrustManager) {
                    /*SL:500*/return (X509TrustManager)v1;
                }
            }
            /*SL:503*/return null;
        }
        catch (NoSuchAlgorithmException ex) {
            /*SL:505*/return null;
        }
        catch (KeyStoreException ex2) {
            /*SL:507*/return null;
        }
    }
    
    public final byte[] getSignatureBytes() {
        /*SL:513*/return this.signatureBytes;
    }
    
    public final byte[] getSignedContentBytes() {
        /*SL:518*/return this.signedContentBytes;
    }
    
    public static JsonWebSignature parse(final JsonFactory a1, final String a2) throws IOException {
        /*SL:530*/return parser(a1).parse(a2);
    }
    
    public static Parser parser(final JsonFactory a1) {
        /*SL:535*/return new Parser(a1);
    }
    
    public static String signUsingRsaSha256(final PrivateKey a1, final JsonFactory a2, final Header a3, final Payload a4) throws GeneralSecurityException, IOException {
        final String v1 = /*EL:634*/Base64.encodeBase64URLSafeString(a2.toByteArray(a3)) + "." + /*EL:635*/Base64.encodeBase64URLSafeString(a2.toByteArray(a4));
        final byte[] v2 = /*EL:636*/StringUtils.getBytesUtf8(v1);
        final byte[] v3 = /*EL:637*/SecurityUtils.sign(/*EL:638*/SecurityUtils.getSha256WithRsaSignatureAlgorithm(), a1, v2);
        /*SL:639*/return v1 + "." + Base64.encodeBase64URLSafeString(v3);
    }
    
    public static class Header extends JsonWebToken.Header
    {
        @Key("alg")
        private String algorithm;
        @Key("jku")
        private String jwkUrl;
        @Key("jwk")
        private String jwk;
        @Key("kid")
        private String keyId;
        @Key("x5u")
        private String x509Url;
        @Key("x5t")
        private String x509Thumbprint;
        @Key("x5c")
        private List<String> x509Certificates;
        @Key("crit")
        private List<String> critical;
        
        @Override
        public Header setType(final String a1) {
            /*SL:156*/super.setType(a1);
            /*SL:157*/return this;
        }
        
        public final String getAlgorithm() {
            /*SL:165*/return this.algorithm;
        }
        
        public Header setAlgorithm(final String a1) {
            /*SL:178*/this.algorithm = a1;
            /*SL:179*/return this;
        }
        
        public final String getJwkUrl() {
            /*SL:188*/return this.jwkUrl;
        }
        
        public Header setJwkUrl(final String a1) {
            /*SL:202*/this.jwkUrl = a1;
            /*SL:203*/return this;
        }
        
        public final String getJwk() {
            /*SL:211*/return this.jwk;
        }
        
        public Header setJwk(final String a1) {
            /*SL:224*/this.jwk = a1;
            /*SL:225*/return this;
        }
        
        public final String getKeyId() {
            /*SL:233*/return this.keyId;
        }
        
        public Header setKeyId(final String a1) {
            /*SL:246*/this.keyId = a1;
            /*SL:247*/return this;
        }
        
        public final String getX509Url() {
            /*SL:256*/return this.x509Url;
        }
        
        public Header setX509Url(final String a1) {
            /*SL:270*/this.x509Url = a1;
            /*SL:271*/return this;
        }
        
        public final String getX509Thumbprint() {
            /*SL:280*/return this.x509Thumbprint;
        }
        
        public Header setX509Thumbprint(final String a1) {
            /*SL:294*/this.x509Thumbprint = a1;
            /*SL:295*/return this;
        }
        
        @Deprecated
        public final String getX509Certificate() {
            /*SL:307*/if (this.x509Certificates == null || this.x509Certificates.isEmpty()) {
                /*SL:308*/return null;
            }
            /*SL:310*/return this.x509Certificates.get(0);
        }
        
        public final List<String> getX509Certificates() {
            /*SL:321*/return this.x509Certificates;
        }
        
        @Deprecated
        public Header setX509Certificate(final String a1) {
            final ArrayList<String> v1 = /*EL:338*/new ArrayList<String>();
            /*SL:339*/v1.add(a1);
            /*SL:340*/this.x509Certificates = v1;
            /*SL:341*/return this;
        }
        
        public Header setX509Certificates(final List<String> a1) {
            /*SL:357*/this.x509Certificates = a1;
            /*SL:358*/return this;
        }
        
        public final List<String> getCritical() {
            /*SL:368*/return this.critical;
        }
        
        public Header setCritical(final List<String> a1) {
            /*SL:383*/this.critical = a1;
            /*SL:384*/return this;
        }
        
        @Override
        public Header set(final String a1, final Object a2) {
            /*SL:389*/return (Header)super.set(a1, a2);
        }
        
        @Override
        public Header clone() {
            /*SL:394*/return (Header)super.clone();
        }
    }
    
    public static final class Parser
    {
        private final JsonFactory jsonFactory;
        private Class<? extends Header> headerClass;
        private Class<? extends Payload> payloadClass;
        
        public Parser(final JsonFactory a1) {
            this.headerClass = Header.class;
            this.payloadClass = Payload.class;
            this.jsonFactory = Preconditions.<JsonFactory>checkNotNull(a1);
        }
        
        public Class<? extends Header> getHeaderClass() {
            /*SL:565*/return this.headerClass;
        }
        
        public Parser setHeaderClass(final Class<? extends Header> a1) {
            /*SL:570*/this.headerClass = a1;
            /*SL:571*/return this;
        }
        
        public Class<? extends Payload> getPayloadClass() {
            /*SL:576*/return this.payloadClass;
        }
        
        public Parser setPayloadClass(final Class<? extends Payload> a1) {
            /*SL:581*/this.payloadClass = a1;
            /*SL:582*/return this;
        }
        
        public JsonFactory getJsonFactory() {
            /*SL:587*/return this.jsonFactory;
        }
        
        public JsonWebSignature parse(final String a1) throws IOException {
            final int v1 = /*EL:598*/a1.indexOf(46);
            /*SL:599*/Preconditions.checkArgument(v1 != -1);
            final byte[] v2 = /*EL:600*/Base64.decodeBase64(a1.substring(0, v1));
            final int v3 = /*EL:601*/a1.indexOf(46, v1 + 1);
            /*SL:602*/Preconditions.checkArgument(v3 != -1);
            /*SL:603*/Preconditions.checkArgument(a1.indexOf(46, v3 + 1) == -1);
            final byte[] v4 = /*EL:605*/Base64.decodeBase64(a1.substring(v1 + 1, v3));
            final byte[] v5 = /*EL:606*/Base64.decodeBase64(a1.substring(v3 + 1));
            final byte[] v6 = /*EL:607*/StringUtils.getBytesUtf8(a1.substring(0, v3));
            final Header v7 = /*EL:609*/this.jsonFactory.<Header>fromInputStream(new ByteArrayInputStream(v2), this.headerClass);
            /*SL:611*/Preconditions.checkArgument(v7.getAlgorithm() != null);
            final Payload v8 = /*EL:612*/this.jsonFactory.<Payload>fromInputStream(new ByteArrayInputStream(v4), this.payloadClass);
            /*SL:614*/return new JsonWebSignature(v7, v8, v5, v6);
        }
    }
}
