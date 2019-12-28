package com.google.api.client.util;

public interface NanoClock
{
    public static final NanoClock SYSTEM = new NanoClock() {
        @Override
        public long nanoTime() {
            /*SL:41*/return System.nanoTime();
        }
    };
    
    long nanoTime();
}
