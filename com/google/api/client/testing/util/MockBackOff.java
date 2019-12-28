package com.google.api.client.testing.util;

import com.google.api.client.util.Preconditions;
import java.io.IOException;
import com.google.api.client.util.Beta;
import com.google.api.client.util.BackOff;

@Beta
public class MockBackOff implements BackOff
{
    private long backOffMillis;
    private int maxTries;
    private int numTries;
    
    public MockBackOff() {
        this.maxTries = 10;
    }
    
    @Override
    public void reset() throws IOException {
        /*SL:47*/this.numTries = 0;
    }
    
    @Override
    public long nextBackOffMillis() throws IOException {
        /*SL:51*/if (this.numTries >= this.maxTries || this.backOffMillis == -1L) {
            /*SL:52*/return -1L;
        }
        /*SL:54*/++this.numTries;
        /*SL:55*/return this.backOffMillis;
    }
    
    public MockBackOff setBackOffMillis(final long a1) {
        /*SL:67*/Preconditions.checkArgument(a1 == -1L || a1 >= 0L);
        /*SL:68*/this.backOffMillis = a1;
        /*SL:69*/return this;
    }
    
    public MockBackOff setMaxTries(final int a1) {
        /*SL:81*/Preconditions.checkArgument(a1 >= 0);
        /*SL:82*/this.maxTries = a1;
        /*SL:83*/return this;
    }
    
    public final int getMaxTries() {
        /*SL:88*/return this.numTries;
    }
    
    public final int getNumberOfTries() {
        /*SL:93*/return this.numTries;
    }
}
