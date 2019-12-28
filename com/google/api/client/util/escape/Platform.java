package com.google.api.client.util.escape;

final class Platform
{
    private static final ThreadLocal<char[]> DEST_TL;
    
    static char[] charBufferFromThreadLocal() {
        /*SL:28*/return Platform.DEST_TL.get();
    }
    
    static {
        DEST_TL = new ThreadLocal<char[]>() {
            @Override
            protected char[] initialValue() {
                /*SL:39*/return new char[1024];
            }
        };
    }
}
