package com.google.api.client.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public final class ByteStreams
{
    private static final int BUF_SIZE = 4096;
    
    public static long copy(final InputStream a2, final OutputStream v1) throws IOException {
        /*SL:46*/Preconditions.<InputStream>checkNotNull(a2);
        /*SL:47*/Preconditions.<OutputStream>checkNotNull(v1);
        final byte[] v2 = /*EL:48*/new byte[4096];
        long v3 = /*EL:49*/0L;
        while (true) {
            final int a3 = /*EL:51*/a2.read(v2);
            /*SL:52*/if (a3 == -1) {
                break;
            }
            /*SL:55*/v1.write(v2, 0, a3);
            /*SL:56*/v3 += a3;
        }
        /*SL:58*/return v3;
    }
    
    public static InputStream limit(final InputStream a1, final long a2) {
        /*SL:69*/return new LimitedInputStream(a1, a2);
    }
    
    public static int read(final InputStream a2, final byte[] a3, final int a4, final int v1) throws IOException {
        /*SL:174*/Preconditions.<InputStream>checkNotNull(a2);
        /*SL:175*/Preconditions.<byte[]>checkNotNull(a3);
        /*SL:176*/if (v1 < 0) {
            /*SL:177*/throw new IndexOutOfBoundsException("len is negative");
        }
        int v2;
        int a5;
        /*SL:180*/for (v2 = 0; v2 < v1; /*SL:185*/v2 += a5) {
            a5 = a2.read(a3, a4 + v2, v1 - v2);
            if (a5 == -1) {
                break;
            }
        }
        /*SL:187*/return v2;
    }
    
    private static final class LimitedInputStream extends FilterInputStream
    {
        private long left;
        private long mark;
        
        LimitedInputStream(final InputStream a1, final long a2) {
            super(a1);
            this.mark = -1L;
            Preconditions.<InputStream>checkNotNull(a1);
            Preconditions.checkArgument(a2 >= 0L, (Object)"limit must be non-negative");
            this.left = a2;
        }
        
        @Override
        public int available() throws IOException {
            /*SL:86*/return (int)Math.min(this.in.available(), this.left);
        }
        
        @Override
        public synchronized void mark(final int a1) {
            /*SL:92*/this.in.mark(a1);
            /*SL:93*/this.mark = this.left;
        }
        
        @Override
        public int read() throws IOException {
            /*SL:98*/if (this.left == 0L) {
                /*SL:99*/return -1;
            }
            final int v1 = /*EL:102*/this.in.read();
            /*SL:103*/if (v1 != -1) {
                /*SL:104*/--this.left;
            }
            /*SL:106*/return v1;
        }
        
        @Override
        public int read(final byte[] a1, final int a2, int a3) throws IOException {
            /*SL:111*/if (this.left == 0L) {
                /*SL:112*/return -1;
            }
            /*SL:115*/a3 = (int)Math.min(a3, this.left);
            final int v1 = /*EL:116*/this.in.read(a1, a2, a3);
            /*SL:117*/if (v1 != -1) {
                /*SL:118*/this.left -= v1;
            }
            /*SL:120*/return v1;
        }
        
        @Override
        public synchronized void reset() throws IOException {
            /*SL:125*/if (!this.in.markSupported()) {
                /*SL:126*/throw new IOException("Mark not supported");
            }
            /*SL:128*/if (this.mark == -1L) {
                /*SL:129*/throw new IOException("Mark not set");
            }
            /*SL:132*/this.in.reset();
            /*SL:133*/this.left = this.mark;
        }
        
        @Override
        public long skip(long a1) throws IOException {
            /*SL:138*/a1 = Math.min(a1, this.left);
            final long v1 = /*EL:139*/this.in.skip(a1);
            /*SL:140*/this.left -= v1;
            /*SL:141*/return v1;
        }
    }
}
