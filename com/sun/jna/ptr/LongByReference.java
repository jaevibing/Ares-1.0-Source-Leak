package com.sun.jna.ptr;

public class LongByReference extends ByReference
{
    public LongByReference() {
        this(0L);
    }
    
    public LongByReference(final long a1) {
        super(8);
        this.setValue(a1);
    }
    
    public void setValue(final long a1) {
        /*SL:37*/this.getPointer().setLong(0L, a1);
    }
    
    public long getValue() {
        /*SL:41*/return this.getPointer().getLong(0L);
    }
}
