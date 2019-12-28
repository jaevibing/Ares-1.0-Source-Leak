package com.google.api.client.testing.util;

import java.io.IOException;
import com.google.api.client.util.Beta;
import java.io.ByteArrayInputStream;

@Beta
public class TestableByteArrayInputStream extends ByteArrayInputStream
{
    private boolean closed;
    
    public TestableByteArrayInputStream(final byte[] a1) {
        super(a1);
    }
    
    public TestableByteArrayInputStream(final byte[] a1, final int a2, final int a3) {
        super(a1);
    }
    
    @Override
    public void close() throws IOException {
        /*SL:58*/this.closed = true;
    }
    
    public final byte[] getBuffer() {
        /*SL:63*/return this.buf;
    }
    
    public final boolean isClosed() {
        /*SL:68*/return this.closed;
    }
}
