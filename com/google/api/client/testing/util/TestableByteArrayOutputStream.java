package com.google.api.client.testing.util;

import java.io.IOException;
import com.google.api.client.util.Beta;
import java.io.ByteArrayOutputStream;

@Beta
public class TestableByteArrayOutputStream extends ByteArrayOutputStream
{
    private boolean closed;
    
    @Override
    public void close() throws IOException {
        /*SL:44*/this.closed = true;
    }
    
    public final byte[] getBuffer() {
        /*SL:49*/return this.buf;
    }
    
    public final boolean isClosed() {
        /*SL:54*/return this.closed;
    }
}
