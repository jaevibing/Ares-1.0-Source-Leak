package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;

public interface InputAccessor
{
    boolean hasMoreBytes() throws IOException;
    
    byte nextByte() throws IOException;
    
    void reset();
    
    public static class Std implements InputAccessor
    {
        protected final InputStream _in;
        protected final byte[] _buffer;
        protected final int _bufferedStart;
        protected int _bufferedEnd;
        protected int _ptr;
        
        public Std(InputStream a1, byte[] a2) {
            this._in = a1;
            this._buffer = a2;
            this._bufferedStart = 0;
            this._ptr = 0;
            this._bufferedEnd = 0;
        }
        
        public Std(byte[] a1) {
            this._in = null;
            this._buffer = a1;
            this._bufferedStart = 0;
            this._bufferedEnd = a1.length;
        }
        
        public Std(byte[] a1, int a2, int a3) {
            this._in = null;
            this._buffer = a1;
            this._ptr = a2;
            this._bufferedStart = a2;
            this._bufferedEnd = a2 + a3;
        }
        
        @Override
        public boolean hasMoreBytes() throws IOException {
            int v1;
            int v2;
            /*SL:104*/if (this._ptr < this._bufferedEnd) {
                /*SL:105*/return true;
            }
            /*SL:107*/if (this._in == null) {
                /*SL:108*/return false;
            }
            /*SL:110*/v1 = this._buffer.length - this._ptr;
            /*SL:111*/if (v1 < 1) {
                /*SL:112*/return false;
            }
            /*SL:114*/v2 = this._in.read(this._buffer, this._ptr, v1);
            /*SL:115*/if (v2 <= 0) {
                /*SL:116*/return false;
            }
            /*SL:118*/this._bufferedEnd += v2;
            /*SL:119*/return true;
        }
        
        @Override
        public byte nextByte() throws IOException {
            /*SL:126*/if (this._ptr >= this._bufferedEnd && /*EL:127*/!this.hasMoreBytes()) {
                /*SL:128*/throw new EOFException("Failed auto-detect: could not read more than " + this._ptr + " bytes (max buffer size: " + this._buffer.length + ")");
            }
            /*SL:131*/return this._buffer[this._ptr++];
        }
        
        @Override
        public void reset() {
            /*SL:136*/this._ptr = this._bufferedStart;
        }
        
        public DataFormatMatcher createMatcher(JsonFactory a1, MatchStrength a2) {
            /*SL:147*/return new DataFormatMatcher(this._in, this._buffer, this._bufferedStart, this._bufferedEnd - this._bufferedStart, a1, a2);
        }
    }
}
