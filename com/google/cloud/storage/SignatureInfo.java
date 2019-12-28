package com.google.cloud.storage;

import java.util.HashMap;
import com.google.common.base.Preconditions;
import com.google.common.net.UrlEscapers;
import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.common.collect.ImmutableMap;
import java.net.URI;
import java.util.Map;

public class SignatureInfo
{
    public static final char COMPONENT_SEPARATOR = '\n';
    public static final String GOOG4_RSA_SHA256 = "GOOG4-RSA-SHA256";
    public static final String SCOPE = "/auto/storage/goog4_request";
    private final HttpMethod httpVerb;
    private final String contentMd5;
    private final String contentType;
    private final long expiration;
    private final Map<String, String> canonicalizedExtensionHeaders;
    private final URI canonicalizedResource;
    private final Storage.SignUrlOption.SignatureVersion signatureVersion;
    private final String accountEmail;
    private final long timestamp;
    private final String yearMonthDay;
    private final String exactDate;
    
    private SignatureInfo(final Builder a1) {
        this.httpVerb = a1.httpVerb;
        this.contentMd5 = a1.contentMd5;
        this.contentType = a1.contentType;
        this.expiration = a1.expiration;
        this.canonicalizedResource = a1.canonicalizedResource;
        this.signatureVersion = a1.signatureVersion;
        this.accountEmail = a1.accountEmail;
        this.timestamp = a1.timestamp;
        if (Storage.SignUrlOption.SignatureVersion.V4.equals(this.signatureVersion) && !a1.canonicalizedExtensionHeaders.containsKey("host")) {
            this.canonicalizedExtensionHeaders = new ImmutableMap.Builder<String, String>().putAll(a1.canonicalizedExtensionHeaders).put("host", "storage.googleapis.com").build();
        }
        else {
            this.canonicalizedExtensionHeaders = a1.canonicalizedExtensionHeaders;
        }
        final Date v1 = new Date(this.timestamp);
        final SimpleDateFormat v2 = new SimpleDateFormat("yyyyMMdd");
        final SimpleDateFormat v3 = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        v2.setTimeZone(TimeZone.getTimeZone("UTC"));
        v3.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.yearMonthDay = v2.format(v1);
        this.exactDate = v3.format(v1);
    }
    
    public String constructUnsignedPayload() {
        /*SL:99*/if (Storage.SignUrlOption.SignatureVersion.V4.equals(this.signatureVersion)) {
            /*SL:100*/return this.constructV4UnsignedPayload();
        }
        /*SL:102*/return this.constructV2UnsignedPayload();
    }
    
    private String constructV2UnsignedPayload() {
        final StringBuilder v1 = /*EL:106*/new StringBuilder();
        /*SL:108*/v1.append(this.httpVerb.name()).append('\n');
        /*SL:109*/if (this.contentMd5 != null) {
            /*SL:110*/v1.append(this.contentMd5);
        }
        /*SL:112*/v1.append('\n');
        /*SL:114*/if (this.contentType != null) {
            /*SL:115*/v1.append(this.contentType);
        }
        /*SL:117*/v1.append('\n');
        /*SL:118*/v1.append(this.expiration).append('\n');
        /*SL:120*/if (this.canonicalizedExtensionHeaders != null) {
            /*SL:121*/v1.append((CharSequence)new CanonicalExtensionHeadersSerializer(Storage.SignUrlOption.SignatureVersion.V2).serialize(this.canonicalizedExtensionHeaders));
        }
        /*SL:126*/v1.append(this.canonicalizedResource);
        /*SL:128*/return v1.toString();
    }
    
    private String constructV4UnsignedPayload() {
        final StringBuilder v1 = /*EL:132*/new StringBuilder();
        /*SL:134*/v1.append("GOOG4-RSA-SHA256").append('\n');
        /*SL:135*/v1.append(this.exactDate).append('\n');
        /*SL:136*/v1.append(this.yearMonthDay).append("/auto/storage/goog4_request").append('\n');
        /*SL:137*/v1.append(this.constructV4CanonicalRequestHash());
        /*SL:139*/return v1.toString();
    }
    
    private String constructV4CanonicalRequestHash() {
        final StringBuilder v1 = /*EL:143*/new StringBuilder();
        final CanonicalExtensionHeadersSerializer v2 = /*EL:145*/new CanonicalExtensionHeadersSerializer(Storage.SignUrlOption.SignatureVersion.V4);
        /*SL:148*/v1.append(this.httpVerb.name()).append('\n');
        /*SL:149*/v1.append(this.canonicalizedResource).append('\n');
        /*SL:150*/v1.append(this.constructV4QueryString()).append('\n');
        /*SL:151*/v1.append((CharSequence)v2.serialize(this.canonicalizedExtensionHeaders)).append(/*EL:152*/'\n');
        /*SL:154*/v1.append((CharSequence)v2.serializeHeaderNames(this.canonicalizedExtensionHeaders)).append(/*EL:155*/'\n');
        /*SL:157*/v1.append("UNSIGNED-PAYLOAD");
        /*SL:159*/return Hashing.sha256().hashString(v1.toString(), StandardCharsets.UTF_8).toString();
    }
    
    public String constructV4QueryString() {
        final StringBuilder v1 = /*EL:165*/new CanonicalExtensionHeadersSerializer(Storage.SignUrlOption.SignatureVersion.V4).serializeHeaderNames(this.canonicalizedExtensionHeaders);
        final StringBuilder v2 = /*EL:169*/new StringBuilder();
        /*SL:170*/v2.append("X-Goog-Algorithm=").append("GOOG4-RSA-SHA256").append("&");
        /*SL:171*/v2.append("X-Goog-Credential=" + /*EL:173*/UrlEscapers.urlFormParameterEscaper().escape(this.accountEmail + "/" + this.yearMonthDay + "/auto/storage/goog4_request") + /*EL:174*/"&");
        /*SL:176*/v2.append("X-Goog-Date=" + this.exactDate + "&");
        /*SL:177*/v2.append("X-Goog-Expires=" + this.expiration + "&");
        /*SL:178*/v2.append("X-Goog-SignedHeaders=" + /*EL:180*/UrlEscapers.urlFormParameterEscaper().escape(v1.toString()));
        /*SL:181*/return v2.toString();
    }
    
    public HttpMethod getHttpVerb() {
        /*SL:185*/return this.httpVerb;
    }
    
    public String getContentMd5() {
        /*SL:189*/return this.contentMd5;
    }
    
    public String getContentType() {
        /*SL:193*/return this.contentType;
    }
    
    public long getExpiration() {
        /*SL:197*/return this.expiration;
    }
    
    public Map<String, String> getCanonicalizedExtensionHeaders() {
        /*SL:201*/return this.canonicalizedExtensionHeaders;
    }
    
    public URI getCanonicalizedResource() {
        /*SL:205*/return this.canonicalizedResource;
    }
    
    public Storage.SignUrlOption.SignatureVersion getSignatureVersion() {
        /*SL:209*/return this.signatureVersion;
    }
    
    public long getTimestamp() {
        /*SL:213*/return this.timestamp;
    }
    
    public String getAccountEmail() {
        /*SL:217*/return this.accountEmail;
    }
    
    public static final class Builder
    {
        private final HttpMethod httpVerb;
        private String contentMd5;
        private String contentType;
        private final long expiration;
        private Map<String, String> canonicalizedExtensionHeaders;
        private final URI canonicalizedResource;
        private Storage.SignUrlOption.SignatureVersion signatureVersion;
        private String accountEmail;
        private long timestamp;
        
        public Builder(final HttpMethod a1, final long a2, final URI a3) {
            this.httpVerb = a1;
            this.expiration = a2;
            this.canonicalizedResource = a3;
        }
        
        public Builder(final SignatureInfo a1) {
            this.httpVerb = a1.httpVerb;
            this.contentMd5 = a1.contentMd5;
            this.contentType = a1.contentType;
            this.expiration = a1.expiration;
            this.canonicalizedExtensionHeaders = a1.canonicalizedExtensionHeaders;
            this.canonicalizedResource = a1.canonicalizedResource;
            this.signatureVersion = a1.signatureVersion;
            this.accountEmail = a1.accountEmail;
            this.timestamp = a1.timestamp;
        }
        
        public Builder setContentMd5(final String a1) {
            /*SL:259*/this.contentMd5 = a1;
            /*SL:261*/return this;
        }
        
        public Builder setContentType(final String a1) {
            /*SL:265*/this.contentType = a1;
            /*SL:267*/return this;
        }
        
        public Builder setCanonicalizedExtensionHeaders(final Map<String, String> a1) {
            /*SL:272*/this.canonicalizedExtensionHeaders = a1;
            /*SL:274*/return this;
        }
        
        public Builder setSignatureVersion(final Storage.SignUrlOption.SignatureVersion a1) {
            /*SL:278*/this.signatureVersion = a1;
            /*SL:280*/return this;
        }
        
        public Builder setAccountEmail(final String a1) {
            /*SL:284*/this.accountEmail = a1;
            /*SL:286*/return this;
        }
        
        public Builder setTimestamp(final long a1) {
            /*SL:290*/this.timestamp = a1;
            /*SL:292*/return this;
        }
        
        public SignatureInfo build() {
            /*SL:297*/Preconditions.checkArgument(this.httpVerb != null, (Object)"Required HTTP method");
            /*SL:298*/Preconditions.checkArgument(this.canonicalizedResource != null, (Object)"Required canonicalized resource");
            /*SL:299*/Preconditions.checkArgument(this.expiration >= 0L, (Object)"Expiration must be greater than or equal to zero");
            /*SL:301*/if (Storage.SignUrlOption.SignatureVersion.V4.equals(this.signatureVersion)) {
                /*SL:302*/Preconditions.checkArgument(this.accountEmail != null, (Object)"Account email required to use V4 signing");
                /*SL:303*/Preconditions.checkArgument(this.timestamp > 0L, (Object)"Timestamp required to use V4 signing");
                /*SL:304*/Preconditions.checkArgument(this.expiration <= 604800L, (Object)"Expiration can't be longer than 7 days to use V4 signing");
            }
            /*SL:308*/if (this.canonicalizedExtensionHeaders == null) {
                /*SL:309*/this.canonicalizedExtensionHeaders = new HashMap<String, String>();
            }
            /*SL:312*/return new SignatureInfo(this, null);
        }
    }
}
