package org.spongepowered.asm.mixin.injection.invoke.arg;

public abstract class Args
{
    protected final Object[] values;
    
    protected Args(final Object[] a1) {
        this.values = a1;
    }
    
    public int size() {
        /*SL:56*/return this.values.length;
    }
    
    public <T> T get(final int a1) {
        /*SL:70*/return (T)this.values[a1];
    }
    
    public abstract <T> void set(final int p0, final T p1);
    
    public abstract void setAll(final Object... p0);
}
