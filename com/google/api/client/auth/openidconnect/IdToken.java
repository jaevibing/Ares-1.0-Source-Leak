package com.google.api.client.auth.openidconnect;

import com.google.api.client.util.GenericData;
import com.google.api.client.json.GenericJson;
import java.util.List;
import com.google.api.client.util.Key;
import java.io.IOException;
import com.google.api.client.json.JsonFactory;
import java.util.Collection;
import java.util.Collections;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.api.client.util.Beta;
import com.google.api.client.json.webtoken.JsonWebSignature;

@Beta
public class IdToken extends JsonWebSignature
{
    public IdToken(final Header a1, final Payload a2, final byte[] a3, final byte[] a4) {
        super(a1, a2, a3, a4);
    }
    
    @Override
    public Payload getPayload() {
        /*SL:60*/return (Payload)super.getPayload();
    }
    
    public final boolean verifyIssuer(final String a1) {
        /*SL:72*/return this.verifyIssuer(Collections.<String>singleton(a1));
    }
    
    public final boolean verifyIssuer(final Collection<String> a1) {
        /*SL:85*/return a1.contains(this.getPayload().getIssuer());
    }
    
    public final boolean verifyAudience(final Collection<String> a1) {
        final Collection<String> v1 = /*EL:97*/this.getPayload().getAudienceAsList();
        /*SL:98*/return !v1.isEmpty() && /*EL:101*/a1.containsAll(v1);
    }
    
    public final boolean verifyTime(final long a1, final long a2) {
        /*SL:116*/return this.verifyExpirationTime(a1, a2) && this.verifyIssuedAtTime(a1, a2);
    }
    
    public final boolean verifyExpirationTime(final long a1, final long a2) {
        /*SL:132*/return a1 <= (this.getPayload().getExpirationTimeSeconds() + /*EL:133*/a2) * 1000L;
    }
    
    public final boolean verifyIssuedAtTime(final long a1, final long a2) {
        /*SL:147*/return a1 >= (this.getPayload().getIssuedAtTimeSeconds() - /*EL:148*/a2) * 1000L;
    }
    
    public static IdToken parse(final JsonFactory a1, final String a2) throws IOException {
        final JsonWebSignature v1 = /*EL:160*/JsonWebSignature.parser(a1).setPayloadClass(Payload.class).parse(a2);
        /*SL:161*/return /*EL:162*/new IdToken(v1.getHeader(), (Payload)v1.getPayload(), v1.getSignatureBytes(), v1.getSignedContentBytes());
    }
    
    @Beta
    public static class Payload extends JsonWebToken.Payload
    {
        @Key("auth_time")
        private Long authorizationTimeSeconds;
        @Key("azp")
        private String authorizedParty;
        @Key
        private String nonce;
        @Key("at_hash")
        private String accessTokenHash;
        @Key("acr")
        private String classReference;
        @Key("amr")
        private List<String> methodsReferences;
        
        public final Long getAuthorizationTimeSeconds() {
            /*SL:198*/return this.authorizationTimeSeconds;
        }
        
        public Payload setAuthorizationTimeSeconds(final Long a1) {
            /*SL:210*/this.authorizationTimeSeconds = a1;
            /*SL:211*/return this;
        }
        
        public final String getAuthorizedParty() {
            /*SL:223*/return this.authorizedParty;
        }
        
        public Payload setAuthorizedParty(final String a1) {
            /*SL:240*/this.authorizedParty = a1;
            /*SL:241*/return this;
        }
        
        public final String getNonce() {
            /*SL:251*/return this.nonce;
        }
        
        public Payload setNonce(final String a1) {
            /*SL:265*/this.nonce = a1;
            /*SL:266*/return this;
        }
        
        public final String getAccessTokenHash() {
            /*SL:275*/return this.accessTokenHash;
        }
        
        public Payload setAccessTokenHash(final String a1) {
            /*SL:289*/this.accessTokenHash = a1;
            /*SL:290*/return this;
        }
        
        public final String getClassReference() {
            /*SL:299*/return this.classReference;
        }
        
        public Payload setClassReference(final String a1) {
            /*SL:313*/this.classReference = a1;
            /*SL:314*/return this;
        }
        
        public final List<String> getMethodsReferences() {
            /*SL:323*/return this.methodsReferences;
        }
        
        public Payload setMethodsReferences(final List<String> a1) {
            /*SL:337*/this.methodsReferences = a1;
            /*SL:338*/return this;
        }
        
        @Override
        public Payload setExpirationTimeSeconds(final Long a1) {
            /*SL:343*/return (Payload)super.setExpirationTimeSeconds(a1);
        }
        
        @Override
        public Payload setNotBeforeTimeSeconds(final Long a1) {
            /*SL:348*/return (Payload)super.setNotBeforeTimeSeconds(a1);
        }
        
        @Override
        public Payload setIssuedAtTimeSeconds(final Long a1) {
            /*SL:353*/return (Payload)super.setIssuedAtTimeSeconds(a1);
        }
        
        @Override
        public Payload setIssuer(final String a1) {
            /*SL:358*/return (Payload)super.setIssuer(a1);
        }
        
        @Override
        public Payload setAudience(final Object a1) {
            /*SL:363*/return (Payload)super.setAudience(a1);
        }
        
        @Override
        public Payload setJwtId(final String a1) {
            /*SL:368*/return (Payload)super.setJwtId(a1);
        }
        
        @Override
        public Payload setType(final String a1) {
            /*SL:373*/return (Payload)super.setType(a1);
        }
        
        @Override
        public Payload setSubject(final String a1) {
            /*SL:378*/return (Payload)super.setSubject(a1);
        }
        
        @Override
        public Payload set(final String a1, final Object a2) {
            /*SL:383*/return (Payload)super.set(a1, a2);
        }
        
        @Override
        public Payload clone() {
            /*SL:388*/return (Payload)super.clone();
        }
    }
}
