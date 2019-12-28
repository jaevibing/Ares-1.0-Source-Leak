package com.sun.jna.ptr;

import com.sun.jna.NativeLong;

public class NativeLongByReference extends ByReference
{
    public NativeLongByReference() {
        this(new NativeLong(0L));
    }
    
    public NativeLongByReference(final NativeLong a1) {
        super(NativeLong.SIZE);
        this.setValue(a1);
    }
    
    public void setValue(final NativeLong a1) {
        /*SL:39*/this.getPointer().setNativeLong(0L, a1);
    }
    
    public NativeLong getValue() {
        /*SL:43*/return this.getPointer().getNativeLong(0L);
    }
}
