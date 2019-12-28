package com.google.api.client.googleapis.auth.oauth2;

import java.util.regex.Matcher;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.JsonParser;
import com.google.api.client.http.HttpResponse;
import java.security.cert.CertificateFactory;
import java.util.Collections;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import com.google.api.client.util.StringUtils;
import java.security.cert.X509Certificate;
import com.google.api.client.util.Preconditions;
import com.google.api.client.json.JsonToken;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.SecurityUtils;
import java.util.ArrayList;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.locks.ReentrantLock;
import com.google.api.client.util.Clock;
import java.util.concurrent.locks.Lock;
import com.google.api.client.http.HttpTransport;
import java.security.PublicKey;
import java.util.List;
import com.google.api.client.json.JsonFactory;
import java.util.regex.Pattern;
import com.google.api.client.util.Beta;

@Beta
public class GooglePublicKeysManager
{
    private static final long REFRESH_SKEW_MILLIS = 300000L;
    private static final Pattern MAX_AGE_PATTERN;
    private final JsonFactory jsonFactory;
    private List<PublicKey> publicKeys;
    private long expirationTimeMilliseconds;
    private final HttpTransport transport;
    private final Lock lock;
    private final Clock clock;
    private final String publicCertsEncodedUrl;
    
    public GooglePublicKeysManager(final HttpTransport a1, final JsonFactory a2) {
        this(new Builder(a1, a2));
    }
    
    protected GooglePublicKeysManager(final Builder a1) {
        this.lock = new ReentrantLock();
        this.transport = a1.transport;
        this.jsonFactory = a1.jsonFactory;
        this.clock = a1.clock;
        this.publicCertsEncodedUrl = a1.publicCertsEncodedUrl;
    }
    
    public final HttpTransport getTransport() {
        /*SL:109*/return this.transport;
    }
    
    public final JsonFactory getJsonFactory() {
        /*SL:114*/return this.jsonFactory;
    }
    
    public final String getPublicCertsEncodedUrl() {
        /*SL:119*/return this.publicCertsEncodedUrl;
    }
    
    public final Clock getClock() {
        /*SL:124*/return this.clock;
    }
    
    public final List<PublicKey> getPublicKeys() throws GeneralSecurityException, IOException {
        /*SL:137*/this.lock.lock();
        try {
            /*SL:139*/if (this.publicKeys == null || this.clock.currentTimeMillis() + 300000L > this.expirationTimeMilliseconds) {
                /*SL:141*/this.refresh();
            }
            /*SL:145*/return this.publicKeys;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public final long getExpirationTimeMilliseconds() {
        /*SL:154*/return this.expirationTimeMilliseconds;
    }
    
    public GooglePublicKeysManager refresh() throws GeneralSecurityException, IOException {
        /*SL:167*/this.lock.lock();
        try {
            /*SL:169*/this.publicKeys = new ArrayList<PublicKey>();
            final CertificateFactory x509CertificateFactory = /*EL:171*/SecurityUtils.getX509CertificateFactory();
            final HttpResponse execute = /*EL:172*/this.transport.createRequestFactory().buildGetRequest(new GenericUrl(this.publicCertsEncodedUrl)).execute();
            /*SL:174*/this.expirationTimeMilliseconds = this.clock.currentTimeMillis() + this.getCacheTimeInSec(execute.getHeaders()) * 1000L;
            final JsonParser jsonParser = /*EL:177*/this.jsonFactory.createJsonParser(execute.getContent());
            JsonToken v0 = /*EL:178*/jsonParser.getCurrentToken();
            /*SL:180*/if (v0 == null) {
                /*SL:181*/v0 = jsonParser.nextToken();
            }
            /*SL:183*/Preconditions.checkArgument(v0 == JsonToken.START_OBJECT);
            try {
                /*SL:185*/while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    /*SL:186*/jsonParser.nextToken();
                    final String v = /*EL:187*/jsonParser.getText();
                    final X509Certificate v2 = /*EL:188*/(X509Certificate)x509CertificateFactory.generateCertificate(new ByteArrayInputStream(StringUtils.getBytesUtf8(v)));
                    /*SL:190*/this.publicKeys.add(v2.getPublicKey());
                }
                /*SL:192*/this.publicKeys = Collections.<PublicKey>unmodifiableList((List<? extends PublicKey>)this.publicKeys);
            }
            finally {
                /*SL:194*/jsonParser.close();
            }
            /*SL:198*/return this;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    long getCacheTimeInSec(final HttpHeaders v-5) {
        long longValue = /*EL:210*/0L;
        /*SL:211*/if (v-5.getCacheControl() != null) {
            /*SL:212*/for (final String v : v-5.getCacheControl().split(",")) {
                final Matcher a1 = GooglePublicKeysManager.MAX_AGE_PATTERN.matcher(/*EL:213*/v);
                /*SL:214*/if (a1.matches()) {
                    /*SL:215*/longValue = Long.valueOf(a1.group(1));
                    /*SL:216*/break;
                }
            }
        }
        /*SL:220*/if (v-5.getAge() != null) {
            /*SL:221*/longValue -= v-5.getAge();
        }
        /*SL:223*/return Math.max(0L, longValue);
    }
    
    static {
        MAX_AGE_PATTERN = Pattern.compile("\\s*max-age\\s*=\\s*(\\d+)\\s*");
    }
    
    @Beta
    public static class Builder
    {
        Clock clock;
        final HttpTransport transport;
        final JsonFactory jsonFactory;
        String publicCertsEncodedUrl;
        
        public Builder(final HttpTransport a1, final JsonFactory a2) {
            this.clock = Clock.SYSTEM;
            this.publicCertsEncodedUrl = "https://www.googleapis.com/oauth2/v1/certs";
            this.transport = Preconditions.<HttpTransport>checkNotNull(a1);
            this.jsonFactory = Preconditions.<JsonFactory>checkNotNull(a2);
        }
        
        public GooglePublicKeysManager build() {
            /*SL:264*/return new GooglePublicKeysManager(this);
        }
        
        public final HttpTransport getTransport() {
            /*SL:269*/return this.transport;
        }
        
        public final JsonFactory getJsonFactory() {
            /*SL:274*/return this.jsonFactory;
        }
        
        public final String getPublicCertsEncodedUrl() {
            /*SL:279*/return this.publicCertsEncodedUrl;
        }
        
        public Builder setPublicCertsEncodedUrl(final String a1) {
            /*SL:295*/this.publicCertsEncodedUrl = Preconditions.<String>checkNotNull(a1);
            /*SL:296*/return this;
        }
        
        public final Clock getClock() {
            /*SL:301*/return this.clock;
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:313*/this.clock = Preconditions.<Clock>checkNotNull(a1);
            /*SL:314*/return this;
        }
    }
}
