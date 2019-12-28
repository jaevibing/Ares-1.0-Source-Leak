package org.spongepowered.asm.util;

public final class Counter
{
    public int value;
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:39*/return a1 != null && a1.getClass() == Counter.class && ((Counter)a1).value == this.value;
    }
    
    @Override
    public int hashCode() {
        /*SL:44*/return this.value;
    }
}
