package com.fasterxml.jackson.core.io;

import java.io.IOException;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import java.io.Writer;

public final class SegmentedStringWriter extends Writer
{
    private final TextBuffer _buffer;
    
    public SegmentedStringWriter(final BufferRecycler a1) {
        this._buffer = new TextBuffer(a1);
    }
    
    @Override
    public Writer append(final char a1) {
        /*SL:33*/this.write(a1);
        /*SL:34*/return this;
    }
    
    @Override
    public Writer append(final CharSequence a1) {
        final String v1 = /*EL:39*/a1.toString();
        /*SL:40*/this._buffer.append(v1, 0, v1.length());
        /*SL:41*/return this;
    }
    
    @Override
    public Writer append(final CharSequence a1, final int a2, final int a3) {
        final String v1 = /*EL:46*/a1.subSequence(a2, a3).toString();
        /*SL:47*/this._buffer.append(v1, 0, v1.length());
        /*SL:48*/return this;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public void write(final char[] a1) {
        /*SL:55*/this._buffer.append(a1, 0, a1.length);
    }
    
    @Override
    public void write(final char[] a1, final int a2, final int a3) {
        /*SL:58*/this._buffer.append(a1, a2, a3);
    }
    
    @Override
    public void write(final int a1) {
        /*SL:61*/this._buffer.append((char)a1);
    }
    
    @Override
    public void write(final String a1) {
        /*SL:64*/this._buffer.append(a1, 0, a1.length());
    }
    
    @Override
    public void write(final String a1, final int a2, final int a3) {
        /*SL:67*/this._buffer.append(a1, a2, a3);
    }
    
    public String getAndClear() {
        final String v1 = /*EL:83*/this._buffer.contentsAsString();
        /*SL:84*/this._buffer.releaseBuffers();
        /*SL:85*/return v1;
    }
}
