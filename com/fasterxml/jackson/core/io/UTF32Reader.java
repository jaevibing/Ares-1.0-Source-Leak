package com.fasterxml.jackson.core.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class UTF32Reader extends Reader
{
    protected static final int LAST_VALID_UNICODE_CHAR = 1114111;
    protected static final char NC = '\0';
    protected final IOContext _context;
    protected InputStream _in;
    protected byte[] _buffer;
    protected int _ptr;
    protected int _length;
    protected final boolean _bigEndian;
    protected char _surrogate;
    protected int _charCount;
    protected int _byteCount;
    protected final boolean _managedBuffers;
    protected char[] _tmpBuf;
    
    public UTF32Reader(final IOContext a1, final InputStream a2, final byte[] a3, final int a4, final int a5, final boolean a6) {
        this._surrogate = '\0';
        this._context = a1;
        this._in = a2;
        this._buffer = a3;
        this._ptr = a4;
        this._length = a5;
        this._bigEndian = a6;
        this._managedBuffers = (a2 != null);
    }
    
    @Override
    public void close() throws IOException {
        final InputStream v1 = /*EL:75*/this._in;
        /*SL:77*/if (v1 != null) {
            /*SL:78*/this._in = null;
            /*SL:79*/this.freeBuffers();
            /*SL:80*/v1.close();
        }
    }
    
    @Override
    public int read() throws IOException {
        /*SL:93*/if (this._tmpBuf == null) {
            /*SL:94*/this._tmpBuf = new char[1];
        }
        /*SL:96*/if (this.read(this._tmpBuf, 0, 1) < 1) {
            /*SL:97*/return -1;
        }
        /*SL:99*/return this._tmpBuf[0];
    }
    
    @Override
    public int read(final char[] v-8, final int v-7, final int v-6) throws IOException {
        /*SL:105*/if (this._buffer == null) {
            return -1;
        }
        /*SL:106*/if (v-6 < 1) {
            return v-6;
        }
        /*SL:108*/if (v-7 < 0 || v-7 + v-6 > v-8.length) {
            /*SL:109*/this.reportBounds(v-8, v-7, v-6);
        }
        int i = /*EL:112*/v-7;
        final int n = /*EL:113*/v-6 + v-7;
        /*SL:116*/if (this._surrogate != '\0') {
            /*SL:117*/v-8[i++] = this._surrogate;
            /*SL:118*/this._surrogate = '\0';
        }
        else {
            final int a1 = /*EL:123*/this._length - this._ptr;
            /*SL:124*/if (a1 < 4 && /*EL:125*/!this.loadMore(a1)) {
                /*SL:127*/if (a1 == 0) {
                    /*SL:128*/return -1;
                }
                /*SL:130*/this.reportUnexpectedEOF(this._length - this._ptr, 4);
            }
        }
        final int n2 = /*EL:136*/this._length - 4;
        /*SL:139*/while (i < n) {
            final int ptr = /*EL:140*/this._ptr;
            int v0 = 0;
            int n3 = 0;
            /*SL:143*/if (this._bigEndian) {
                final int a2 = /*EL:144*/this._buffer[ptr] << 8 | (this._buffer[ptr + 1] & 0xFF);
                final int a3 = /*EL:145*/(this._buffer[ptr + 2] & 0xFF) << 8 | (this._buffer[ptr + 3] & 0xFF);
            }
            else {
                /*SL:147*/v0 = ((this._buffer[ptr] & 0xFF) | (this._buffer[ptr + 1] & 0xFF) << 8);
                /*SL:148*/n3 = ((this._buffer[ptr + 2] & 0xFF) | this._buffer[ptr + 3] << 8);
            }
            /*SL:150*/this._ptr += 4;
            /*SL:154*/if (n3 != 0) {
                /*SL:155*/n3 &= 0xFFFF;
                final int v = /*EL:156*/n3 - 1 << 16 | v0;
                /*SL:157*/if (n3 > 16) {
                    /*SL:158*/this.reportInvalid(v, i - v-7, String.format(" (above 0x%08x)", 1114111));
                }
                /*SL:161*/v-8[i++] = (char)(55296 + (v >> 10));
                /*SL:163*/v0 = (0xDC00 | (v & 0x3FF));
                /*SL:165*/if (i >= n) {
                    /*SL:166*/this._surrogate = (char)v;
                    /*SL:167*/break;
                }
            }
            /*SL:170*/v-8[i++] = (char)v0;
            /*SL:171*/if (this._ptr > n2) {
                /*SL:172*/break;
            }
        }
        final int ptr = /*EL:175*/i - v-7;
        /*SL:176*/this._charCount += ptr;
        /*SL:177*/return ptr;
    }
    
    private void reportUnexpectedEOF(final int a1, final int a2) throws IOException {
        final int v1 = /*EL:187*/this._byteCount + a1;
        final int v2 = this._charCount;
        /*SL:189*/throw new CharConversionException("Unexpected EOF in the middle of a 4-byte UTF-32 char: got " + a1 + ", needed " + a2 + ", at char #" + v2 + ", byte #" + v1 + ")");
    }
    
    private void reportInvalid(final int a1, final int a2, final String a3) throws IOException {
        final int v1 = /*EL:193*/this._byteCount + this._ptr - 1;
        final int v2 = this._charCount + a2;
        /*SL:195*/throw new CharConversionException("Invalid UTF-32 character 0x" + Integer.toHexString(a1) + a3 + " at char #" + v2 + ", byte #" + v1 + ")");
    }
    
    private boolean loadMore(final int v0) throws IOException {
        /*SL:205*/this._byteCount += this._length - v0;
        /*SL:208*/if (v0 > 0) {
            /*SL:209*/if (this._ptr > 0) {
                /*SL:210*/System.arraycopy(this._buffer, this._ptr, this._buffer, 0, v0);
                /*SL:211*/this._ptr = 0;
            }
            /*SL:213*/this._length = v0;
        }
        else {
            /*SL:218*/this._ptr = 0;
            final int a1 = /*EL:219*/(this._in == null) ? -1 : this._in.read(this._buffer);
            /*SL:220*/if (a1 < 1) {
                /*SL:221*/this._length = 0;
                /*SL:222*/if (a1 < 0) {
                    /*SL:223*/if (this._managedBuffers) {
                        /*SL:224*/this.freeBuffers();
                    }
                    /*SL:226*/return false;
                }
                /*SL:229*/this.reportStrangeStream();
            }
            /*SL:231*/this._length = a1;
        }
        /*SL:237*/while (this._length < 4) {
            final int v = /*EL:238*/(this._in == null) ? -1 : this._in.read(this._buffer, this._length, this._buffer.length - this._length);
            /*SL:239*/if (v < 1) {
                /*SL:240*/if (v < 0) {
                    /*SL:241*/if (this._managedBuffers) {
                        /*SL:242*/this.freeBuffers();
                    }
                    /*SL:244*/this.reportUnexpectedEOF(this._length, 4);
                }
                /*SL:247*/this.reportStrangeStream();
            }
            /*SL:249*/this._length += v;
        }
        /*SL:251*/return true;
    }
    
    private void freeBuffers() {
        final byte[] v1 = /*EL:260*/this._buffer;
        /*SL:261*/if (v1 != null) {
            /*SL:262*/this._buffer = null;
            /*SL:263*/this._context.releaseReadIOBuffer(v1);
        }
    }
    
    private void reportBounds(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:268*/throw new ArrayIndexOutOfBoundsException("read(buf," + a2 + "," + a3 + "), cbuf[" + a1.length + "]");
    }
    
    private void reportStrangeStream() throws IOException {
        /*SL:272*/throw new IOException("Strange I/O stream, returned 0 bytes on read");
    }
}
