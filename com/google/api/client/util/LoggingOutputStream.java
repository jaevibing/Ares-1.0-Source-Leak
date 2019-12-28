package com.google.api.client.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.OutputStream;
import java.io.FilterOutputStream;

public class LoggingOutputStream extends FilterOutputStream
{
    private final LoggingByteArrayOutputStream logStream;
    
    public LoggingOutputStream(final OutputStream a1, final Logger a2, final Level a3, final int a4) {
        super(a1);
        this.logStream = new LoggingByteArrayOutputStream(a2, a3, a4);
    }
    
    @Override
    public void write(final int a1) throws IOException {
        /*SL:51*/this.out.write(a1);
        /*SL:52*/this.logStream.write(a1);
    }
    
    @Override
    public void write(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:57*/this.out.write(a1, a2, a3);
        /*SL:58*/this.logStream.write(a1, a2, a3);
    }
    
    @Override
    public void close() throws IOException {
        /*SL:63*/this.logStream.close();
        /*SL:64*/super.close();
    }
    
    public final LoggingByteArrayOutputStream getLogStream() {
        /*SL:69*/return this.logStream;
    }
}
