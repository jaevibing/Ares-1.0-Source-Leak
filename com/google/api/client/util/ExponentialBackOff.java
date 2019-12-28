package com.google.api.client.util;

import java.io.IOException;

public class ExponentialBackOff implements BackOff
{
    public static final int DEFAULT_INITIAL_INTERVAL_MILLIS = 500;
    public static final double DEFAULT_RANDOMIZATION_FACTOR = 0.5;
    public static final double DEFAULT_MULTIPLIER = 1.5;
    public static final int DEFAULT_MAX_INTERVAL_MILLIS = 60000;
    public static final int DEFAULT_MAX_ELAPSED_TIME_MILLIS = 900000;
    private int currentIntervalMillis;
    private final int initialIntervalMillis;
    private final double randomizationFactor;
    private final double multiplier;
    private final int maxIntervalMillis;
    long startTimeNanos;
    private final int maxElapsedTimeMillis;
    private final NanoClock nanoClock;
    
    public ExponentialBackOff() {
        this(new Builder());
    }
    
    protected ExponentialBackOff(final Builder a1) {
        this.initialIntervalMillis = a1.initialIntervalMillis;
        this.randomizationFactor = a1.randomizationFactor;
        this.multiplier = a1.multiplier;
        this.maxIntervalMillis = a1.maxIntervalMillis;
        this.maxElapsedTimeMillis = a1.maxElapsedTimeMillis;
        this.nanoClock = a1.nanoClock;
        Preconditions.checkArgument(this.initialIntervalMillis > 0);
        Preconditions.checkArgument(0.0 <= this.randomizationFactor && this.randomizationFactor < 1.0);
        Preconditions.checkArgument(this.multiplier >= 1.0);
        Preconditions.checkArgument(this.maxIntervalMillis >= this.initialIntervalMillis);
        Preconditions.checkArgument(this.maxElapsedTimeMillis > 0);
        this.reset();
    }
    
    @Override
    public final void reset() {
        /*SL:176*/this.currentIntervalMillis = this.initialIntervalMillis;
        /*SL:177*/this.startTimeNanos = this.nanoClock.nanoTime();
    }
    
    @Override
    public long nextBackOffMillis() throws IOException {
        /*SL:194*/if (this.getElapsedTimeMillis() > this.maxElapsedTimeMillis) {
            /*SL:195*/return -1L;
        }
        final int v1 = getRandomValueFromInterval(/*EL:197*/this.randomizationFactor, /*EL:198*/Math.random(), this.currentIntervalMillis);
        /*SL:199*/this.incrementCurrentInterval();
        /*SL:200*/return v1;
    }
    
    static int getRandomValueFromInterval(final double a1, final double a2, final int a3) {
        final double v1 = /*EL:209*/a1 * a3;
        final double v2 = /*EL:210*/a3 - v1;
        final double v3 = /*EL:211*/a3 + v1;
        final int v4 = /*EL:215*/(int)(v2 + a2 * (v3 - v2 + 1.0));
        /*SL:216*/return v4;
    }
    
    public final int getInitialIntervalMillis() {
        /*SL:221*/return this.initialIntervalMillis;
    }
    
    public final double getRandomizationFactor() {
        /*SL:233*/return this.randomizationFactor;
    }
    
    public final int getCurrentIntervalMillis() {
        /*SL:240*/return this.currentIntervalMillis;
    }
    
    public final double getMultiplier() {
        /*SL:247*/return this.multiplier;
    }
    
    public final int getMaxIntervalMillis() {
        /*SL:255*/return this.maxIntervalMillis;
    }
    
    public final int getMaxElapsedTimeMillis() {
        /*SL:268*/return this.maxElapsedTimeMillis;
    }
    
    public final long getElapsedTimeMillis() {
        /*SL:280*/return (this.nanoClock.nanoTime() - this.startTimeNanos) / 1000000L;
    }
    
    private void incrementCurrentInterval() {
        /*SL:288*/if (this.currentIntervalMillis >= this.maxIntervalMillis / this.multiplier) {
            /*SL:289*/this.currentIntervalMillis = this.maxIntervalMillis;
        }
        else {
            /*SL:291*/this.currentIntervalMillis *= (int)this.multiplier;
        }
    }
    
    public static class Builder
    {
        int initialIntervalMillis;
        double randomizationFactor;
        double multiplier;
        int maxIntervalMillis;
        int maxElapsedTimeMillis;
        NanoClock nanoClock;
        
        public Builder() {
            this.initialIntervalMillis = 500;
            this.randomizationFactor = 0.5;
            this.multiplier = 1.5;
            this.maxIntervalMillis = 60000;
            this.maxElapsedTimeMillis = 900000;
            this.nanoClock = NanoClock.SYSTEM;
        }
        
        public ExponentialBackOff build() {
            /*SL:341*/return new ExponentialBackOff(this);
        }
        
        public final int getInitialIntervalMillis() {
            /*SL:349*/return this.initialIntervalMillis;
        }
        
        public Builder setInitialIntervalMillis(final int a1) {
            /*SL:362*/this.initialIntervalMillis = a1;
            /*SL:363*/return this;
        }
        
        public final double getRandomizationFactor() {
            /*SL:381*/return this.randomizationFactor;
        }
        
        public Builder setRandomizationFactor(final double a1) {
            /*SL:400*/this.randomizationFactor = a1;
            /*SL:401*/return this;
        }
        
        public final double getMultiplier() {
            /*SL:409*/return this.multiplier;
        }
        
        public Builder setMultiplier(final double a1) {
            /*SL:422*/this.multiplier = a1;
            /*SL:423*/return this;
        }
        
        public final int getMaxIntervalMillis() {
            /*SL:432*/return this.maxIntervalMillis;
        }
        
        public Builder setMaxIntervalMillis(final int a1) {
            /*SL:446*/this.maxIntervalMillis = a1;
            /*SL:447*/return this;
        }
        
        public final int getMaxElapsedTimeMillis() {
            /*SL:461*/return this.maxElapsedTimeMillis;
        }
        
        public Builder setMaxElapsedTimeMillis(final int a1) {
            /*SL:480*/this.maxElapsedTimeMillis = a1;
            /*SL:481*/return this;
        }
        
        public final NanoClock getNanoClock() {
            /*SL:486*/return this.nanoClock;
        }
        
        public Builder setNanoClock(final NanoClock a1) {
            /*SL:498*/this.nanoClock = Preconditions.<NanoClock>checkNotNull(a1);
            /*SL:499*/return this;
        }
    }
}
