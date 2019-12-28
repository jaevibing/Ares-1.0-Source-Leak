package com.google.api.client.auth.openidconnect;

import com.google.api.client.util.Preconditions;
import java.util.Collections;
import java.util.Collection;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Beta;

@Beta
public class IdTokenVerifier
{
    public static final long DEFAULT_TIME_SKEW_SECONDS = 300L;
    private final Clock clock;
    private final long acceptableTimeSkewSeconds;
    private final Collection<String> issuers;
    private final Collection<String> audience;
    
    public IdTokenVerifier() {
        this(new Builder());
    }
    
    protected IdTokenVerifier(final Builder a1) {
        this.clock = a1.clock;
        this.acceptableTimeSkewSeconds = a1.acceptableTimeSkewSeconds;
        this.issuers = ((a1.issuers == null) ? null : Collections.<String>unmodifiableCollection((Collection<? extends String>)a1.issuers));
        this.audience = ((a1.audience == null) ? null : Collections.<String>unmodifiableCollection((Collection<? extends String>)a1.audience));
    }
    
    public final Clock getClock() {
        /*SL:94*/return this.clock;
    }
    
    public final long getAcceptableTimeSkewSeconds() {
        /*SL:99*/return this.acceptableTimeSkewSeconds;
    }
    
    public final String getIssuer() {
        /*SL:106*/if (this.issuers == null) {
            /*SL:107*/return null;
        }
        /*SL:109*/return this.issuers.iterator().next();
    }
    
    public final Collection<String> getIssuers() {
        /*SL:119*/return this.issuers;
    }
    
    public final Collection<String> getAudience() {
        /*SL:127*/return this.audience;
    }
    
    public boolean verify(final IdToken a1) {
        /*SL:153*/return (this.issuers == null || a1.verifyIssuer(this.issuers)) && (this.audience == null || a1.verifyAudience(this.audience)) && /*EL:154*/a1.verifyTime(this.clock.currentTimeMillis(), /*EL:155*/this.acceptableTimeSkewSeconds);
    }
    
    @Beta
    public static class Builder
    {
        Clock clock;
        long acceptableTimeSkewSeconds;
        Collection<String> issuers;
        Collection<String> audience;
        
        public Builder() {
            this.clock = Clock.SYSTEM;
            this.acceptableTimeSkewSeconds = 300L;
        }
        
        public IdTokenVerifier build() {
            /*SL:185*/return new IdTokenVerifier(this);
        }
        
        public final Clock getClock() {
            /*SL:190*/return this.clock;
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:202*/this.clock = Preconditions.<Clock>checkNotNull(a1);
            /*SL:203*/return this;
        }
        
        public final String getIssuer() {
            /*SL:210*/if (this.issuers == null) {
                /*SL:211*/return null;
            }
            /*SL:213*/return this.issuers.iterator().next();
        }
        
        public Builder setIssuer(final String a1) {
            /*SL:226*/if (a1 == null) {
                /*SL:227*/return this.setIssuers(null);
            }
            /*SL:229*/return this.setIssuers(Collections.<String>singleton(a1));
        }
        
        public final Collection<String> getIssuers() {
            /*SL:239*/return this.issuers;
        }
        
        public Builder setIssuers(final Collection<String> a1) {
            /*SL:255*/Preconditions.checkArgument(a1 == null || !a1.isEmpty(), (Object)"Issuers must not be empty");
            /*SL:257*/this.issuers = a1;
            /*SL:258*/return this;
        }
        
        public final Collection<String> getAudience() {
            /*SL:266*/return this.audience;
        }
        
        public Builder setAudience(final Collection<String> a1) {
            /*SL:278*/this.audience = a1;
            /*SL:279*/return this;
        }
        
        public final long getAcceptableTimeSkewSeconds() {
            /*SL:284*/return this.acceptableTimeSkewSeconds;
        }
        
        public Builder setAcceptableTimeSkewSeconds(final long a1) {
            /*SL:301*/Preconditions.checkArgument(a1 >= 0L);
            /*SL:302*/this.acceptableTimeSkewSeconds = a1;
            /*SL:303*/return this;
        }
    }
}
