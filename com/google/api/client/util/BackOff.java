package com.google.api.client.util;

import java.io.IOException;

public interface BackOff
{
    public static final long STOP = -1L;
    public static final BackOff ZERO_BACKOFF = new BackOff() {
        @Override
        public void reset() throws IOException {
        }
        
        @Override
        public long nextBackOffMillis() throws IOException {
            /*SL:62*/return 0L;
        }
    };
    public static final BackOff STOP_BACKOFF = new BackOff() {
        @Override
        public void reset() throws IOException {
        }
        
        @Override
        public long nextBackOffMillis() throws IOException {
            /*SL:76*/return -1L;
        }
    };
    
    void reset() throws IOException;
    
    long nextBackOffMillis() throws IOException;
}
