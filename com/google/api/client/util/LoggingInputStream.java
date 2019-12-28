package com.google.api.client.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import java.io.FilterInputStream;

public class LoggingInputStream extends FilterInputStream
{
    private final LoggingByteArrayOutputStream logStream;
    
    public LoggingInputStream(final InputStream a1, final Logger a2, final Level a3, final int a4) {
        super(a1);
        this.logStream = new LoggingByteArrayOutputStream(a2, a3, a4);
    }
    
    @Override
    public int read() throws IOException {
        final int v1 = /*EL:50*/super.read();
        /*SL:51*/this.logStream.write(v1);
        /*SL:52*/return v1;
    }
    
    @Override
    public int read(final byte[] a1, final int a2, final int a3) throws IOException {
        final int v1 = /*EL:57*/super.read(a1, a2, a3);
        /*SL:58*/if (v1 > 0) {
            /*SL:59*/this.logStream.write(a1, a2, v1);
        }
        /*SL:61*/return v1;
    }
    
    @Override
    public void close() throws IOException {
        /*SL:66*/this.logStream.close();
        /*SL:67*/super.close();
    }
    
    public final LoggingByteArrayOutputStream getLogStream() {
        /*SL:72*/return this.logStream;
    }
}
