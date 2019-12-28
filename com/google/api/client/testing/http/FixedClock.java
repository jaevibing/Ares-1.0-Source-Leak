package com.google.api.client.testing.http;

import java.util.concurrent.atomic.AtomicLong;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Clock;

@Beta
public class FixedClock implements Clock
{
    private AtomicLong currentTime;
    
    public FixedClock() {
        this(0L);
    }
    
    public FixedClock(final long a1) {
        this.currentTime = new AtomicLong(a1);
    }
    
    public FixedClock setTime(final long a1) {
        /*SL:57*/this.currentTime.set(a1);
        /*SL:58*/return this;
    }
    
    @Override
    public long currentTimeMillis() {
        /*SL:62*/return this.currentTime.get();
    }
}
