package com.google.api.client.json.webtoken;

import java.util.Collections;
import java.util.List;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Objects;
import com.google.api.client.util.Preconditions;

public class JsonWebToken
{
    private final Header header;
    private final Payload payload;
    
    public JsonWebToken(final Header a1, final Payload a2) {
        this.header = Preconditions.<Header>checkNotNull(a1);
        this.payload = Preconditions.<Payload>checkNotNull(a2);
    }
    
    @Override
    public String toString() {
        /*SL:386*/return Objects.toStringHelper(this).add("header", this.header).add("payload", this.payload).toString();
    }
    
    public Header getHeader() {
        /*SL:398*/return this.header;
    }
    
    public Payload getPayload() {
        /*SL:410*/return this.payload;
    }
    
    public static class Header extends GenericJson
    {
        @Key("typ")
        private String type;
        @Key("cty")
        private String contentType;
        
        public final String getType() {
            /*SL:74*/return this.type;
        }
        
        public Header setType(final String a1) {
            /*SL:87*/this.type = a1;
            /*SL:88*/return this;
        }
        
        public final String getContentType() {
            /*SL:96*/return this.contentType;
        }
        
        public Header setContentType(final String a1) {
            /*SL:109*/this.contentType = a1;
            /*SL:110*/return this;
        }
        
        @Override
        public Header set(final String a1, final Object a2) {
            /*SL:115*/return (Header)super.set(a1, a2);
        }
        
        @Override
        public Header clone() {
            /*SL:120*/return (Header)super.clone();
        }
    }
    
    public static class Payload extends GenericJson
    {
        @Key("exp")
        private Long expirationTimeSeconds;
        @Key("nbf")
        private Long notBeforeTimeSeconds;
        @Key("iat")
        private Long issuedAtTimeSeconds;
        @Key("iss")
        private String issuer;
        @Key("aud")
        private Object audience;
        @Key("jti")
        private String jwtId;
        @Key("typ")
        private String type;
        @Key("sub")
        private String subject;
        
        public final Long getExpirationTimeSeconds() {
            /*SL:188*/return this.expirationTimeSeconds;
        }
        
        public Payload setExpirationTimeSeconds(final Long a1) {
            /*SL:201*/this.expirationTimeSeconds = a1;
            /*SL:202*/return this;
        }
        
        public final Long getNotBeforeTimeSeconds() {
            /*SL:210*/return this.notBeforeTimeSeconds;
        }
        
        public Payload setNotBeforeTimeSeconds(final Long a1) {
            /*SL:223*/this.notBeforeTimeSeconds = a1;
            /*SL:224*/return this;
        }
        
        public final Long getIssuedAtTimeSeconds() {
            /*SL:232*/return this.issuedAtTimeSeconds;
        }
        
        public Payload setIssuedAtTimeSeconds(final Long a1) {
            /*SL:245*/this.issuedAtTimeSeconds = a1;
            /*SL:246*/return this;
        }
        
        public final String getIssuer() {
            /*SL:254*/return this.issuer;
        }
        
        public Payload setIssuer(final String a1) {
            /*SL:267*/this.issuer = a1;
            /*SL:268*/return this;
        }
        
        public final Object getAudience() {
            /*SL:276*/return this.audience;
        }
        
        public final List<String> getAudienceAsList() {
            /*SL:285*/if (this.audience == null) {
                /*SL:286*/return Collections.<String>emptyList();
            }
            /*SL:288*/if (this.audience instanceof String) {
                /*SL:289*/return Collections.<String>singletonList(this.audience);
            }
            /*SL:291*/return (List<String>)this.audience;
        }
        
        public Payload setAudience(final Object a1) {
            /*SL:304*/this.audience = a1;
            /*SL:305*/return this;
        }
        
        public final String getJwtId() {
            /*SL:313*/return this.jwtId;
        }
        
        public Payload setJwtId(final String a1) {
            /*SL:325*/this.jwtId = a1;
            /*SL:326*/return this;
        }
        
        public final String getType() {
            /*SL:334*/return this.type;
        }
        
        public Payload setType(final String a1) {
            /*SL:347*/this.type = a1;
            /*SL:348*/return this;
        }
        
        public final String getSubject() {
            /*SL:356*/return this.subject;
        }
        
        public Payload setSubject(final String a1) {
            /*SL:369*/this.subject = a1;
            /*SL:370*/return this;
        }
        
        @Override
        public Payload set(final String a1, final Object a2) {
            /*SL:375*/return (Payload)super.set(a1, a2);
        }
        
        @Override
        public Payload clone() {
            /*SL:380*/return (Payload)super.clone();
        }
    }
}
