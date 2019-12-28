package com.sun.jna.ptr;

import com.sun.jna.Pointer;

public class PointerByReference extends ByReference
{
    public PointerByReference() {
        this(null);
    }
    
    public PointerByReference(final Pointer a1) {
        super(Pointer.SIZE);
        this.setValue(a1);
    }
    
    public void setValue(final Pointer a1) {
        /*SL:44*/this.getPointer().setPointer(0L, a1);
    }
    
    public Pointer getValue() {
        /*SL:48*/return this.getPointer().getPointer(0L);
    }
}
