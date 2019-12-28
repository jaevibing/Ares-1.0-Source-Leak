package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.JsonEncoding;

public class IOContext
{
    protected final Object _sourceRef;
    protected JsonEncoding _encoding;
    protected final boolean _managedResource;
    protected final BufferRecycler _bufferRecycler;
    protected byte[] _readIOBuffer;
    protected byte[] _writeEncodingBuffer;
    protected byte[] _base64Buffer;
    protected char[] _tokenCBuffer;
    protected char[] _concatCBuffer;
    protected char[] _nameCopyBuffer;
    
    public IOContext(final BufferRecycler a1, final Object a2, final boolean a3) {
        this._bufferRecycler = a1;
        this._sourceRef = a2;
        this._managedResource = a3;
    }
    
    public void setEncoding(final JsonEncoding a1) {
        /*SL:109*/this._encoding = a1;
    }
    
    public IOContext withEncoding(final JsonEncoding a1) {
        /*SL:116*/this._encoding = a1;
        /*SL:117*/return this;
    }
    
    public Object getSourceReference() {
        /*SL:126*/return this._sourceRef;
    }
    
    public JsonEncoding getEncoding() {
        /*SL:127*/return this._encoding;
    }
    
    public boolean isResourceManaged() {
        /*SL:128*/return this._managedResource;
    }
    
    public TextBuffer constructTextBuffer() {
        /*SL:137*/return new TextBuffer(this._bufferRecycler);
    }
    
    public byte[] allocReadIOBuffer() {
        /*SL:146*/this._verifyAlloc(this._readIOBuffer);
        /*SL:147*/return this._readIOBuffer = this._bufferRecycler.allocByteBuffer(0);
    }
    
    public byte[] allocReadIOBuffer(final int a1) {
        /*SL:154*/this._verifyAlloc(this._readIOBuffer);
        /*SL:155*/return this._readIOBuffer = this._bufferRecycler.allocByteBuffer(0, a1);
    }
    
    public byte[] allocWriteEncodingBuffer() {
        /*SL:159*/this._verifyAlloc(this._writeEncodingBuffer);
        /*SL:160*/return this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(1);
    }
    
    public byte[] allocWriteEncodingBuffer(final int a1) {
        /*SL:167*/this._verifyAlloc(this._writeEncodingBuffer);
        /*SL:168*/return this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(1, a1);
    }
    
    public byte[] allocBase64Buffer() {
        /*SL:175*/this._verifyAlloc(this._base64Buffer);
        /*SL:176*/return this._base64Buffer = this._bufferRecycler.allocByteBuffer(3);
    }
    
    public byte[] allocBase64Buffer(final int a1) {
        /*SL:183*/this._verifyAlloc(this._base64Buffer);
        /*SL:184*/return this._base64Buffer = this._bufferRecycler.allocByteBuffer(3, a1);
    }
    
    public char[] allocTokenBuffer() {
        /*SL:188*/this._verifyAlloc(this._tokenCBuffer);
        /*SL:189*/return this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(0);
    }
    
    public char[] allocTokenBuffer(final int a1) {
        /*SL:196*/this._verifyAlloc(this._tokenCBuffer);
        /*SL:197*/return this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(0, a1);
    }
    
    public char[] allocConcatBuffer() {
        /*SL:201*/this._verifyAlloc(this._concatCBuffer);
        /*SL:202*/return this._concatCBuffer = this._bufferRecycler.allocCharBuffer(1);
    }
    
    public char[] allocNameCopyBuffer(final int a1) {
        /*SL:206*/this._verifyAlloc(this._nameCopyBuffer);
        /*SL:207*/return this._nameCopyBuffer = this._bufferRecycler.allocCharBuffer(3, a1);
    }
    
    public void releaseReadIOBuffer(final byte[] a1) {
        /*SL:215*/if (a1 != null) {
            /*SL:219*/this._verifyRelease(a1, this._readIOBuffer);
            /*SL:220*/this._readIOBuffer = null;
            /*SL:221*/this._bufferRecycler.releaseByteBuffer(0, a1);
        }
    }
    
    public void releaseWriteEncodingBuffer(final byte[] a1) {
        /*SL:226*/if (a1 != null) {
            /*SL:230*/this._verifyRelease(a1, this._writeEncodingBuffer);
            /*SL:231*/this._writeEncodingBuffer = null;
            /*SL:232*/this._bufferRecycler.releaseByteBuffer(1, a1);
        }
    }
    
    public void releaseBase64Buffer(final byte[] a1) {
        /*SL:237*/if (a1 != null) {
            /*SL:238*/this._verifyRelease(a1, this._base64Buffer);
            /*SL:239*/this._base64Buffer = null;
            /*SL:240*/this._bufferRecycler.releaseByteBuffer(3, a1);
        }
    }
    
    public void releaseTokenBuffer(final char[] a1) {
        /*SL:245*/if (a1 != null) {
            /*SL:246*/this._verifyRelease(a1, this._tokenCBuffer);
            /*SL:247*/this._tokenCBuffer = null;
            /*SL:248*/this._bufferRecycler.releaseCharBuffer(0, a1);
        }
    }
    
    public void releaseConcatBuffer(final char[] a1) {
        /*SL:253*/if (a1 != null) {
            /*SL:255*/this._verifyRelease(a1, this._concatCBuffer);
            /*SL:256*/this._concatCBuffer = null;
            /*SL:257*/this._bufferRecycler.releaseCharBuffer(1, a1);
        }
    }
    
    public void releaseNameCopyBuffer(final char[] a1) {
        /*SL:262*/if (a1 != null) {
            /*SL:264*/this._verifyRelease(a1, this._nameCopyBuffer);
            /*SL:265*/this._nameCopyBuffer = null;
            /*SL:266*/this._bufferRecycler.releaseCharBuffer(3, a1);
        }
    }
    
    protected final void _verifyAlloc(final Object a1) {
        /*SL:277*/if (a1 != null) {
            throw new IllegalStateException("Trying to call same allocXxx() method second time");
        }
    }
    
    protected final void _verifyRelease(final byte[] a1, final byte[] a2) {
        /*SL:282*/if (a1 != a2 && a1.length < a2.length) {
            throw this.wrongBuf();
        }
    }
    
    protected final void _verifyRelease(final char[] a1, final char[] a2) {
        /*SL:287*/if (a1 != a2 && a1.length < a2.length) {
            throw this.wrongBuf();
        }
    }
    
    private IllegalArgumentException wrongBuf() {
        /*SL:292*/return new IllegalArgumentException("Trying to release buffer smaller than original");
    }
}
