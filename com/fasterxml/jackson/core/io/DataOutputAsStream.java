package com.fasterxml.jackson.core.io;

import java.io.IOException;
import java.io.DataOutput;
import java.io.OutputStream;

public class DataOutputAsStream extends OutputStream
{
    protected final DataOutput _output;
    
    public DataOutputAsStream(final DataOutput a1) {
        this._output = a1;
    }
    
    @Override
    public void write(final int a1) throws IOException {
        /*SL:22*/this._output.write(a1);
    }
    
    @Override
    public void write(final byte[] a1) throws IOException {
        /*SL:27*/this._output.write(a1, 0, a1.length);
    }
    
    @Override
    public void write(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:32*/this._output.write(a1, a2, a3);
    }
}
