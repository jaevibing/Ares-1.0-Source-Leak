package com.fasterxml.jackson.core.io;

import java.io.IOException;
import java.io.InputStream;

public final class MergedStream extends InputStream
{
    private final IOContext _ctxt;
    private final InputStream _in;
    private byte[] _b;
    private int _ptr;
    private final int _end;
    
    public MergedStream(final IOContext a1, final InputStream a2, final byte[] a3, final int a4, final int a5) {
        this._ctxt = a1;
        this._in = a2;
        this._b = a3;
        this._ptr = a4;
        this._end = a5;
    }
    
    @Override
    public int available() throws IOException {
        /*SL:35*/if (this._b != null) {
            /*SL:36*/return this._end - this._ptr;
        }
        /*SL:38*/return this._in.available();
    }
    
    @Override
    public void close() throws IOException {
        /*SL:42*/this._free();
        /*SL:43*/this._in.close();
    }
    
    @Override
    public void mark(final int a1) {
        /*SL:47*/if (this._b == null) {
            this._in.mark(a1);
        }
    }
    
    @Override
    public boolean markSupported() {
        /*SL:52*/return this._b == null && this._in.markSupported();
    }
    
    @Override
    public int read() throws IOException {
        /*SL:56*/if (this._b != null) {
            final int v1 = /*EL:57*/this._b[this._ptr++] & 0xFF;
            /*SL:58*/if (this._ptr >= this._end) {
                /*SL:59*/this._free();
            }
            /*SL:61*/return v1;
        }
        /*SL:63*/return this._in.read();
    }
    
    @Override
    public int read(final byte[] a1) throws IOException {
        /*SL:67*/return this.read(a1, 0, a1.length);
    }
    
    @Override
    public int read(final byte[] a3, final int v1, int v2) throws IOException {
        /*SL:72*/if (this._b != null) {
            final int a4 = /*EL:73*/this._end - this._ptr;
            /*SL:74*/if (v2 > a4) {
                /*SL:75*/v2 = a4;
            }
            /*SL:77*/System.arraycopy(this._b, this._ptr, a3, v1, v2);
            /*SL:78*/this._ptr += v2;
            /*SL:79*/if (this._ptr >= this._end) {
                /*SL:80*/this._free();
            }
            /*SL:82*/return v2;
        }
        /*SL:84*/return this._in.read(a3, v1, v2);
    }
    
    @Override
    public void reset() throws IOException {
        /*SL:89*/if (this._b == null) {
            this._in.reset();
        }
    }
    
    @Override
    public long skip(long v2) throws IOException {
        long v3 = /*EL:94*/0L;
        /*SL:96*/if (this._b != null) {
            final int a1 = /*EL:97*/this._end - this._ptr;
            /*SL:99*/if (a1 > v2) {
                /*SL:100*/this._ptr += (int)v2;
                /*SL:101*/return v2;
            }
            /*SL:103*/this._free();
            /*SL:104*/v3 += a1;
            /*SL:105*/v2 -= a1;
        }
        /*SL:108*/if (v2 > 0L) {
            v3 += this._in.skip(v2);
        }
        /*SL:109*/return v3;
    }
    
    private void _free() {
        final byte[] v1 = /*EL:113*/this._b;
        /*SL:114*/if (v1 != null) {
            /*SL:115*/this._b = null;
            /*SL:116*/if (this._ctxt != null) {
                /*SL:117*/this._ctxt.releaseReadIOBuffer(v1);
            }
        }
    }
}
