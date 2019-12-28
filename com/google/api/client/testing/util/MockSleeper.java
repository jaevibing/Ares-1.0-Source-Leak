package com.google.api.client.testing.util;

import com.google.api.client.util.Beta;
import com.google.api.client.util.Sleeper;

@Beta
public class MockSleeper implements Sleeper
{
    private int count;
    private long lastMillis;
    
    @Override
    public void sleep(final long a1) throws InterruptedException {
        /*SL:44*/++this.count;
        /*SL:45*/this.lastMillis = a1;
    }
    
    public final int getCount() {
        /*SL:50*/return this.count;
    }
    
    public final long getLastMillis() {
        /*SL:58*/return this.lastMillis;
    }
}
