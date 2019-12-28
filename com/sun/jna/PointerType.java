package com.sun.jna;

public abstract class PointerType implements NativeMapped
{
    private Pointer pointer;
    
    protected PointerType() {
        this.pointer = Pointer.NULL;
    }
    
    protected PointerType(final Pointer a1) {
        this.pointer = a1;
    }
    
    @Override
    public Class<?> nativeType() {
        /*SL:49*/return Pointer.class;
    }
    
    @Override
    public Object toNative() {
        /*SL:55*/return this.getPointer();
    }
    
    public Pointer getPointer() {
        /*SL:62*/return this.pointer;
    }
    
    public void setPointer(final Pointer a1) {
        /*SL:66*/this.pointer = a1;
    }
    
    @Override
    public Object fromNative(final Object v-1, final FromNativeContext v0) {
        /*SL:78*/if (v-1 == null) {
            /*SL:79*/return null;
        }
        try {
            final PointerType a1 = /*EL:82*/(PointerType)this.getClass().newInstance();
            /*SL:83*/a1.pointer = (Pointer)v-1;
            /*SL:84*/return a1;
        }
        catch (InstantiationException a2) {
            /*SL:87*/throw new IllegalArgumentException("Can't instantiate " + this.getClass());
        }
        catch (IllegalAccessException v) {
            /*SL:90*/throw new IllegalArgumentException("Not allowed to instantiate " + this.getClass());
        }
    }
    
    @Override
    public int hashCode() {
        /*SL:99*/return (this.pointer != null) ? this.pointer.hashCode() : 0;
    }
    
    @Override
    public boolean equals(final Object v2) {
        /*SL:107*/if (v2 == this) {
            /*SL:108*/return true;
        }
        /*SL:110*/if (!(v2 instanceof PointerType)) {
            /*SL:117*/return false;
        }
        final Pointer a1 = ((PointerType)v2).getPointer();
        if (this.pointer == null) {
            return a1 == null;
        }
        return this.pointer.equals(a1);
    }
    
    @Override
    public String toString() {
        /*SL:122*/return (this.pointer == null) ? "NULL" : (this.pointer.toString() + " (" + super.toString() + ")");
    }
}
