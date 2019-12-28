package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.util.GenericData;
import com.google.api.client.json.GenericJson;
import java.util.List;
import com.google.api.client.util.Key;
import java.security.GeneralSecurityException;
import java.io.IOException;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;
import com.google.api.client.auth.openidconnect.IdToken;

@Beta
public class GoogleIdToken extends IdToken
{
    public static GoogleIdToken parse(final JsonFactory a1, final String a2) throws IOException {
        final JsonWebSignature v1 = /*EL:57*/JsonWebSignature.parser(a1).setPayloadClass(Payload.class).parse(a2);
        /*SL:59*/return new GoogleIdToken(v1.getHeader(), (Payload)v1.getPayload(), v1.getSignatureBytes(), v1.getSignedContentBytes());
    }
    
    public GoogleIdToken(final Header a1, final Payload a2, final byte[] a3, final byte[] a4) {
        super(a1, a2, a3, a4);
    }
    
    public boolean verify(final GoogleIdTokenVerifier a1) throws GeneralSecurityException, IOException {
        /*SL:79*/return a1.verify(this);
    }
    
    public Payload getPayload() {
        /*SL:84*/return (Payload)super.getPayload();
    }
    
    @Beta
    public static class Payload extends IdToken.Payload
    {
        @Key("hd")
        private String hostedDomain;
        @Key("email")
        private String email;
        @Key("email_verified")
        private Object emailVerified;
        
        @Deprecated
        public String getUserId() {
            /*SL:120*/return this.getSubject();
        }
        
        @Deprecated
        public Payload setUserId(final String a1) {
            /*SL:130*/return this.setSubject(a1);
        }
        
        @Deprecated
        public String getIssuee() {
            /*SL:140*/return this.getAuthorizedParty();
        }
        
        @Deprecated
        public Payload setIssuee(final String a1) {
            /*SL:151*/return this.setAuthorizedParty(a1);
        }
        
        public String getHostedDomain() {
            /*SL:159*/return this.hostedDomain;
        }
        
        public Payload setHostedDomain(final String a1) {
            /*SL:167*/this.hostedDomain = a1;
            /*SL:168*/return this;
        }
        
        public String getEmail() {
            /*SL:181*/return this.email;
        }
        
        public Payload setEmail(final String a1) {
            /*SL:194*/this.email = a1;
            /*SL:195*/return this;
        }
        
        public Boolean getEmailVerified() {
            /*SL:215*/if (this.emailVerified == null) {
                /*SL:216*/return null;
            }
            /*SL:218*/if (this.emailVerified instanceof Boolean) {
                /*SL:219*/return (Boolean)this.emailVerified;
            }
            /*SL:222*/return Boolean.valueOf((String)this.emailVerified);
        }
        
        public Payload setEmailVerified(final Boolean a1) {
            /*SL:241*/this.emailVerified = a1;
            /*SL:242*/return this;
        }
        
        public Payload setAuthorizationTimeSeconds(final Long a1) {
            /*SL:247*/return (Payload)super.setAuthorizationTimeSeconds(a1);
        }
        
        public Payload setAuthorizedParty(final String a1) {
            /*SL:252*/return (Payload)super.setAuthorizedParty(a1);
        }
        
        public Payload setNonce(final String a1) {
            /*SL:257*/return (Payload)super.setNonce(a1);
        }
        
        public Payload setAccessTokenHash(final String a1) {
            /*SL:262*/return (Payload)super.setAccessTokenHash(a1);
        }
        
        public Payload setClassReference(final String a1) {
            /*SL:267*/return (Payload)super.setClassReference(a1);
        }
        
        public Payload setMethodsReferences(final List<String> a1) {
            /*SL:272*/return (Payload)super.setMethodsReferences(a1);
        }
        
        public Payload setExpirationTimeSeconds(final Long a1) {
            /*SL:277*/return (Payload)super.setExpirationTimeSeconds(a1);
        }
        
        public Payload setNotBeforeTimeSeconds(final Long a1) {
            /*SL:282*/return (Payload)super.setNotBeforeTimeSeconds(a1);
        }
        
        public Payload setIssuedAtTimeSeconds(final Long a1) {
            /*SL:287*/return (Payload)super.setIssuedAtTimeSeconds(a1);
        }
        
        public Payload setIssuer(final String a1) {
            /*SL:292*/return (Payload)super.setIssuer(a1);
        }
        
        public Payload setAudience(final Object a1) {
            /*SL:297*/return (Payload)super.setAudience(a1);
        }
        
        public Payload setJwtId(final String a1) {
            /*SL:302*/return (Payload)super.setJwtId(a1);
        }
        
        public Payload setType(final String a1) {
            /*SL:307*/return (Payload)super.setType(a1);
        }
        
        public Payload setSubject(final String a1) {
            /*SL:312*/return (Payload)super.setSubject(a1);
        }
        
        public Payload set(final String a1, final Object a2) {
            /*SL:317*/return (Payload)super.set(a1, a2);
        }
        
        public Payload clone() {
            /*SL:322*/return (Payload)super.clone();
        }
    }
}
