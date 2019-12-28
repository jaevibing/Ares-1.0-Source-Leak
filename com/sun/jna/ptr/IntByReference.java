package com.sun.jna.ptr;

public class IntByReference extends ByReference
{
    public IntByReference() {
        this(0);
    }
    
    public IntByReference(final int a1) {
        super(4);
        this.setValue(a1);
    }
    
    public void setValue(final int a1) {
        /*SL:38*/this.getPointer().setInt(0L, a1);
    }
    
    public int getValue() {
        /*SL:42*/return this.getPointer().getInt(0L);
    }
}
