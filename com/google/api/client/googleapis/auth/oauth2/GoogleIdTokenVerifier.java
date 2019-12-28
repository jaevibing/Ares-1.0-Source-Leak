package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.util.Clock;
import java.util.Collection;
import java.util.Arrays;
import com.google.api.client.util.Preconditions;
import java.util.Iterator;
import com.google.api.client.auth.openidconnect.IdToken;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.List;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;

@Beta
public class GoogleIdTokenVerifier extends IdTokenVerifier
{
    private final GooglePublicKeysManager publicKeys;
    
    public GoogleIdTokenVerifier(final HttpTransport a1, final JsonFactory a2) {
        this(new Builder(a1, a2));
    }
    
    public GoogleIdTokenVerifier(final GooglePublicKeysManager a1) {
        this(new Builder(a1));
    }
    
    protected GoogleIdTokenVerifier(final Builder a1) {
        super(a1);
        this.publicKeys = a1.publicKeys;
    }
    
    public final GooglePublicKeysManager getPublicKeysManager() {
        /*SL:94*/return this.publicKeys;
    }
    
    public final HttpTransport getTransport() {
        /*SL:103*/return this.publicKeys.getTransport();
    }
    
    public final JsonFactory getJsonFactory() {
        /*SL:108*/return this.publicKeys.getJsonFactory();
    }
    
    @Deprecated
    public final String getPublicCertsEncodedUrl() {
        /*SL:120*/return this.publicKeys.getPublicCertsEncodedUrl();
    }
    
    @Deprecated
    public final List<PublicKey> getPublicKeys() throws GeneralSecurityException, IOException {
        /*SL:137*/return this.publicKeys.getPublicKeys();
    }
    
    @Deprecated
    public final long getExpirationTimeMilliseconds() {
        /*SL:149*/return this.publicKeys.getExpirationTimeMilliseconds();
    }
    
    public boolean verify(final GoogleIdToken v0) throws GeneralSecurityException, IOException {
        /*SL:170*/if (!super.verify(v0)) {
            /*SL:171*/return false;
        }
        /*SL:174*/for (final PublicKey a1 : this.publicKeys.getPublicKeys()) {
            /*SL:175*/if (v0.verifySignature(a1)) {
                /*SL:176*/return true;
            }
        }
        /*SL:179*/return false;
    }
    
    public GoogleIdToken verify(final String a1) throws GeneralSecurityException, IOException {
        final GoogleIdToken v1 = /*EL:191*/GoogleIdToken.parse(this.getJsonFactory(), a1);
        /*SL:192*/return this.verify(v1) ? v1 : null;
    }
    
    @Deprecated
    public GoogleIdTokenVerifier loadPublicCerts() throws GeneralSecurityException, IOException {
        /*SL:210*/this.publicKeys.refresh();
        /*SL:211*/return this;
    }
    
    @Beta
    public static class Builder extends IdTokenVerifier.Builder
    {
        GooglePublicKeysManager publicKeys;
        
        public Builder(final HttpTransport a1, final JsonFactory a2) {
            this(new GooglePublicKeysManager(a1, a2));
        }
        
        public Builder(final GooglePublicKeysManager a1) {
            this.publicKeys = Preconditions.<GooglePublicKeysManager>checkNotNull(a1);
            this.setIssuers(Arrays.<String>asList("accounts.google.com", "https://accounts.google.com"));
        }
        
        public GoogleIdTokenVerifier build() {
            /*SL:251*/return new GoogleIdTokenVerifier(this);
        }
        
        public final GooglePublicKeysManager getPublicCerts() {
            /*SL:260*/return this.publicKeys;
        }
        
        public final HttpTransport getTransport() {
            /*SL:265*/return this.publicKeys.getTransport();
        }
        
        public final JsonFactory getJsonFactory() {
            /*SL:270*/return this.publicKeys.getJsonFactory();
        }
        
        @Deprecated
        public final String getPublicCertsEncodedUrl() {
            /*SL:282*/return this.publicKeys.getPublicCertsEncodedUrl();
        }
        
        @Deprecated
        public Builder setPublicCertsEncodedUrl(final String a1) {
            /*SL:304*/this.publicKeys = new GooglePublicKeysManager.Builder(this.getTransport(), this.getJsonFactory()).setPublicCertsEncodedUrl(a1).setClock(this.publicKeys.getClock()).build();
            /*SL:307*/return this;
        }
        
        public Builder setIssuer(final String a1) {
            /*SL:312*/return (Builder)super.setIssuer(a1);
        }
        
        public Builder setIssuers(final Collection<String> a1) {
            /*SL:320*/return (Builder)super.setIssuers(a1);
        }
        
        public Builder setAudience(final Collection<String> a1) {
            /*SL:325*/return (Builder)super.setAudience(a1);
        }
        
        public Builder setAcceptableTimeSkewSeconds(final long a1) {
            /*SL:330*/return (Builder)super.setAcceptableTimeSkewSeconds(a1);
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:335*/return (Builder)super.setClock(a1);
        }
    }
}
