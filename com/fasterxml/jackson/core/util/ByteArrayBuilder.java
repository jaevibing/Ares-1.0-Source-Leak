package com.fasterxml.jackson.core.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.io.OutputStream;

public final class ByteArrayBuilder extends OutputStream
{
    public static final byte[] NO_BYTES;
    private static final int INITIAL_BLOCK_SIZE = 500;
    private static final int MAX_BLOCK_SIZE = 262144;
    static final int DEFAULT_BLOCK_ARRAY_SIZE = 40;
    private final BufferRecycler _bufferRecycler;
    private final LinkedList<byte[]> _pastBlocks;
    private int _pastLen;
    private byte[] _currBlock;
    private int _currBlockPtr;
    
    public ByteArrayBuilder() {
        this(null);
    }
    
    public ByteArrayBuilder(final BufferRecycler a1) {
        this(a1, 500);
    }
    
    public ByteArrayBuilder(final int a1) {
        this(null, a1);
    }
    
    public ByteArrayBuilder(final BufferRecycler a1, final int a2) {
        this._pastBlocks = new LinkedList<byte[]>();
        this._bufferRecycler = a1;
        this._currBlock = ((a1 == null) ? new byte[a2] : a1.allocByteBuffer(2));
    }
    
    public void reset() {
        /*SL:61*/this._pastLen = 0;
        /*SL:62*/this._currBlockPtr = 0;
        /*SL:64*/if (!this._pastBlocks.isEmpty()) {
            /*SL:65*/this._pastBlocks.clear();
        }
    }
    
    public int size() {
        /*SL:73*/return this._pastLen + this._currBlockPtr;
    }
    
    public void release() {
        /*SL:82*/this.reset();
        /*SL:83*/if (this._bufferRecycler != null && this._currBlock != null) {
            /*SL:84*/this._bufferRecycler.releaseByteBuffer(2, this._currBlock);
            /*SL:85*/this._currBlock = null;
        }
    }
    
    public void append(final int a1) {
        /*SL:90*/if (this._currBlockPtr >= this._currBlock.length) {
            /*SL:91*/this._allocMore();
        }
        /*SL:93*/this._currBlock[this._currBlockPtr++] = (byte)a1;
    }
    
    public void appendTwoBytes(final int a1) {
        /*SL:97*/if (this._currBlockPtr + 1 < this._currBlock.length) {
            /*SL:98*/this._currBlock[this._currBlockPtr++] = (byte)(a1 >> 8);
            /*SL:99*/this._currBlock[this._currBlockPtr++] = (byte)a1;
        }
        else {
            /*SL:101*/this.append(a1 >> 8);
            /*SL:102*/this.append(a1);
        }
    }
    
    public void appendThreeBytes(final int a1) {
        /*SL:107*/if (this._currBlockPtr + 2 < this._currBlock.length) {
            /*SL:108*/this._currBlock[this._currBlockPtr++] = (byte)(a1 >> 16);
            /*SL:109*/this._currBlock[this._currBlockPtr++] = (byte)(a1 >> 8);
            /*SL:110*/this._currBlock[this._currBlockPtr++] = (byte)a1;
        }
        else {
            /*SL:112*/this.append(a1 >> 16);
            /*SL:113*/this.append(a1 >> 8);
            /*SL:114*/this.append(a1);
        }
    }
    
    public void appendFourBytes(final int a1) {
        /*SL:122*/if (this._currBlockPtr + 3 < this._currBlock.length) {
            /*SL:123*/this._currBlock[this._currBlockPtr++] = (byte)(a1 >> 24);
            /*SL:124*/this._currBlock[this._currBlockPtr++] = (byte)(a1 >> 16);
            /*SL:125*/this._currBlock[this._currBlockPtr++] = (byte)(a1 >> 8);
            /*SL:126*/this._currBlock[this._currBlockPtr++] = (byte)a1;
        }
        else {
            /*SL:128*/this.append(a1 >> 24);
            /*SL:129*/this.append(a1 >> 16);
            /*SL:130*/this.append(a1 >> 8);
            /*SL:131*/this.append(a1);
        }
    }
    
    public byte[] toByteArray() {
        final int n = /*EL:141*/this._pastLen + this._currBlockPtr;
        /*SL:143*/if (n == 0) {
            /*SL:144*/return ByteArrayBuilder.NO_BYTES;
        }
        final byte[] array = /*EL:146*/new byte[n];
        int n2 = /*EL:147*/0;
        /*SL:149*/for (final byte[] v0 : this._pastBlocks) {
            final int v = /*EL:150*/v0.length;
            /*SL:151*/System.arraycopy(v0, 0, array, n2, v);
            /*SL:152*/n2 += v;
        }
        /*SL:154*/System.arraycopy(this._currBlock, 0, array, n2, this._currBlockPtr);
        /*SL:155*/n2 += this._currBlockPtr;
        /*SL:156*/if (n2 != n) {
            /*SL:157*/throw new RuntimeException("Internal error: total len assumed to be " + n + ", copied " + n2 + " bytes");
        }
        /*SL:160*/if (!this._pastBlocks.isEmpty()) {
            /*SL:161*/this.reset();
        }
        /*SL:163*/return array;
    }
    
    public byte[] resetAndGetFirstSegment() {
        /*SL:177*/this.reset();
        /*SL:178*/return this._currBlock;
    }
    
    public byte[] finishCurrentSegment() {
        /*SL:187*/this._allocMore();
        /*SL:188*/return this._currBlock;
    }
    
    public byte[] completeAndCoalesce(final int a1) {
        /*SL:201*/this._currBlockPtr = a1;
        /*SL:202*/return this.toByteArray();
    }
    
    public byte[] getCurrentSegment() {
        /*SL:205*/return this._currBlock;
    }
    
    public void setCurrentSegmentLength(final int a1) {
        /*SL:206*/this._currBlockPtr = a1;
    }
    
    public int getCurrentSegmentLength() {
        /*SL:207*/return this._currBlockPtr;
    }
    
    @Override
    public void write(final byte[] a1) {
        /*SL:217*/this.write(a1, 0, a1.length);
    }
    
    @Override
    public void write(final byte[] v1, int v2, int v3) {
        while (true) {
            final int a1 = /*EL:224*/this._currBlock.length - this._currBlockPtr;
            final int a2 = /*EL:225*/Math.min(a1, v3);
            /*SL:226*/if (a2 > 0) {
                /*SL:227*/System.arraycopy(v1, v2, this._currBlock, this._currBlockPtr, a2);
                /*SL:228*/v2 += a2;
                /*SL:229*/this._currBlockPtr += a2;
                /*SL:230*/v3 -= a2;
            }
            /*SL:232*/if (v3 <= 0) {
                break;
            }
            /*SL:233*/this._allocMore();
        }
    }
    
    @Override
    public void write(final int a1) {
        /*SL:239*/this.append(a1);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void flush() {
    }
    
    private void _allocMore() {
        final int v1 = /*EL:253*/this._pastLen + this._currBlock.length;
        /*SL:257*/if (v1 < 0) {
            /*SL:258*/throw new IllegalStateException("Maximum Java array size (2GB) exceeded by `ByteArrayBuilder`");
        }
        /*SL:261*/this._pastLen = v1;
        int v2 = /*EL:269*/Math.max(this._pastLen >> 1, 1000);
        /*SL:271*/if (v2 > 262144) {
            /*SL:272*/v2 = 262144;
        }
        /*SL:274*/this._pastBlocks.add(this._currBlock);
        /*SL:275*/this._currBlock = new byte[v2];
        /*SL:276*/this._currBlockPtr = 0;
    }
    
    static {
        NO_BYTES = new byte[0];
    }
}
