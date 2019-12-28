package com.google.api.client.util;

import java.io.IOException;
import java.io.OutputStream;

final class ByteCountingOutputStream extends OutputStream
{
    long count;
    
    @Override
    public void write(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:33*/this.count += a3;
    }
    
    @Override
    public void write(final int a1) throws IOException {
        /*SL:38*/++this.count;
    }
}
