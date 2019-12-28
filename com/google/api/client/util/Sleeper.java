package com.google.api.client.util;

public interface Sleeper
{
    public static final Sleeper DEFAULT = new Sleeper() {
        @Override
        public void sleep(long a1) throws InterruptedException {
            /*SL:43*/Thread.sleep(a1);
        }
    };
    
    void sleep(long p0) throws InterruptedException;
}
