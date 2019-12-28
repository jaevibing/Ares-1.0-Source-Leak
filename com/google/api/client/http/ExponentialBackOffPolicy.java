package com.google.api.client.http;

import com.google.api.client.util.NanoClock;
import java.io.IOException;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.client.util.Beta;

@Deprecated
@Beta
public class ExponentialBackOffPolicy implements BackOffPolicy
{
    public static final int DEFAULT_INITIAL_INTERVAL_MILLIS = 500;
    public static final double DEFAULT_RANDOMIZATION_FACTOR = 0.5;
    public static final double DEFAULT_MULTIPLIER = 1.5;
    public static final int DEFAULT_MAX_INTERVAL_MILLIS = 60000;
    public static final int DEFAULT_MAX_ELAPSED_TIME_MILLIS = 900000;
    private final ExponentialBackOff exponentialBackOff;
    
    public ExponentialBackOffPolicy() {
        this(new Builder());
    }
    
    protected ExponentialBackOffPolicy(final Builder a1) {
        this.exponentialBackOff = a1.exponentialBackOffBuilder.build();
    }
    
    @Override
    public boolean isBackOffRequired(final int a1) {
        /*SL:156*/switch (a1) {
            case 500:
            case 503: {
                /*SL:159*/return true;
            }
            default: {
                /*SL:161*/return false;
            }
        }
    }
    
    @Override
    public final void reset() {
        /*SL:169*/this.exponentialBackOff.reset();
    }
    
    @Override
    public long getNextBackOffMillis() throws IOException {
        /*SL:189*/return this.exponentialBackOff.nextBackOffMillis();
    }
    
    public final int getInitialIntervalMillis() {
        /*SL:196*/return this.exponentialBackOff.getInitialIntervalMillis();
    }
    
    public final double getRandomizationFactor() {
        /*SL:208*/return this.exponentialBackOff.getRandomizationFactor();
    }
    
    public final int getCurrentIntervalMillis() {
        /*SL:215*/return this.exponentialBackOff.getCurrentIntervalMillis();
    }
    
    public final double getMultiplier() {
        /*SL:222*/return this.exponentialBackOff.getMultiplier();
    }
    
    public final int getMaxIntervalMillis() {
        /*SL:230*/return this.exponentialBackOff.getMaxIntervalMillis();
    }
    
    public final int getMaxElapsedTimeMillis() {
        /*SL:243*/return this.exponentialBackOff.getMaxElapsedTimeMillis();
    }
    
    public final long getElapsedTimeMillis() {
        /*SL:255*/return this.exponentialBackOff.getElapsedTimeMillis();
    }
    
    public static Builder builder() {
        /*SL:262*/return new Builder();
    }
    
    @Deprecated
    @Beta
    public static class Builder
    {
        final ExponentialBackOff.Builder exponentialBackOffBuilder;
        
        protected Builder() {
            this.exponentialBackOffBuilder = new ExponentialBackOff.Builder();
        }
        
        public ExponentialBackOffPolicy build() {
            /*SL:287*/return new ExponentialBackOffPolicy(this);
        }
        
        public final int getInitialIntervalMillis() {
            /*SL:295*/return this.exponentialBackOffBuilder.getInitialIntervalMillis();
        }
        
        public Builder setInitialIntervalMillis(final int a1) {
            /*SL:308*/this.exponentialBackOffBuilder.setInitialIntervalMillis(a1);
            /*SL:309*/return this;
        }
        
        public final double getRandomizationFactor() {
            /*SL:327*/return this.exponentialBackOffBuilder.getRandomizationFactor();
        }
        
        public Builder setRandomizationFactor(final double a1) {
            /*SL:346*/this.exponentialBackOffBuilder.setRandomizationFactor(a1);
            /*SL:347*/return this;
        }
        
        public final double getMultiplier() {
            /*SL:355*/return this.exponentialBackOffBuilder.getMultiplier();
        }
        
        public Builder setMultiplier(final double a1) {
            /*SL:368*/this.exponentialBackOffBuilder.setMultiplier(a1);
            /*SL:369*/return this;
        }
        
        public final int getMaxIntervalMillis() {
            /*SL:378*/return this.exponentialBackOffBuilder.getMaxIntervalMillis();
        }
        
        public Builder setMaxIntervalMillis(final int a1) {
            /*SL:392*/this.exponentialBackOffBuilder.setMaxIntervalMillis(a1);
            /*SL:393*/return this;
        }
        
        public final int getMaxElapsedTimeMillis() {
            /*SL:407*/return this.exponentialBackOffBuilder.getMaxElapsedTimeMillis();
        }
        
        public Builder setMaxElapsedTimeMillis(final int a1) {
            /*SL:426*/this.exponentialBackOffBuilder.setMaxElapsedTimeMillis(a1);
            /*SL:427*/return this;
        }
        
        public final NanoClock getNanoClock() {
            /*SL:436*/return this.exponentialBackOffBuilder.getNanoClock();
        }
        
        public Builder setNanoClock(final NanoClock a1) {
            /*SL:450*/this.exponentialBackOffBuilder.setNanoClock(a1);
            /*SL:451*/return this;
        }
    }
}
