package com.fasterxml.jackson.core.util;

public class BufferRecycler
{
    public static final int BYTE_READ_IO_BUFFER = 0;
    public static final int BYTE_WRITE_ENCODING_BUFFER = 1;
    public static final int BYTE_WRITE_CONCAT_BUFFER = 2;
    public static final int BYTE_BASE64_CODEC_BUFFER = 3;
    public static final int CHAR_TOKEN_BUFFER = 0;
    public static final int CHAR_CONCAT_BUFFER = 1;
    public static final int CHAR_TEXT_BUFFER = 2;
    public static final int CHAR_NAME_COPY_BUFFER = 3;
    private static final int[] BYTE_BUFFER_LENGTHS;
    private static final int[] CHAR_BUFFER_LENGTHS;
    protected final byte[][] _byteBuffers;
    protected final char[][] _charBuffers;
    
    public BufferRecycler() {
        this(4, 4);
    }
    
    protected BufferRecycler(final int a1, final int a2) {
        this._byteBuffers = new byte[a1][];
        this._charBuffers = new char[a2][];
    }
    
    public final byte[] allocByteBuffer(final int a1) {
        /*SL:86*/return this.allocByteBuffer(a1, 0);
    }
    
    public byte[] allocByteBuffer(final int a1, int a2) {
        final int v1 = /*EL:90*/this.byteBufferLength(a1);
        /*SL:91*/if (a2 < v1) {
            /*SL:92*/a2 = v1;
        }
        byte[] v2 = /*EL:94*/this._byteBuffers[a1];
        /*SL:95*/if (v2 == null || v2.length < a2) {
            /*SL:96*/v2 = this.balloc(a2);
        }
        else {
            /*SL:98*/this._byteBuffers[a1] = null;
        }
        /*SL:100*/return v2;
    }
    
    public void releaseByteBuffer(final int a1, final byte[] a2) {
        /*SL:104*/this._byteBuffers[a1] = a2;
    }
    
    public final char[] allocCharBuffer(final int a1) {
        /*SL:114*/return this.allocCharBuffer(a1, 0);
    }
    
    public char[] allocCharBuffer(final int a1, int a2) {
        final int v1 = /*EL:118*/this.charBufferLength(a1);
        /*SL:119*/if (a2 < v1) {
            /*SL:120*/a2 = v1;
        }
        char[] v2 = /*EL:122*/this._charBuffers[a1];
        /*SL:123*/if (v2 == null || v2.length < a2) {
            /*SL:124*/v2 = this.calloc(a2);
        }
        else {
            /*SL:126*/this._charBuffers[a1] = null;
        }
        /*SL:128*/return v2;
    }
    
    public void releaseCharBuffer(final int a1, final char[] a2) {
        /*SL:132*/this._charBuffers[a1] = a2;
    }
    
    protected int byteBufferLength(final int a1) {
        /*SL:142*/return BufferRecycler.BYTE_BUFFER_LENGTHS[a1];
    }
    
    protected int charBufferLength(final int a1) {
        /*SL:146*/return BufferRecycler.CHAR_BUFFER_LENGTHS[a1];
    }
    
    protected byte[] balloc(final int a1) {
        /*SL:155*/return new byte[a1];
    }
    
    protected char[] calloc(final int a1) {
        /*SL:156*/return new char[a1];
    }
    
    static {
        BYTE_BUFFER_LENGTHS = new int[] { 8000, 8000, 2000, 2000 };
        CHAR_BUFFER_LENGTHS = new int[] { 4000, 4000, 200, 200 };
    }
}
