package com.sun.jna;

public class FromNativeContext
{
    private Class<?> type;
    
    FromNativeContext(final Class<?> a1) {
        this.type = a1;
    }
    
    public Class<?> getTargetType() {
        /*SL:34*/return this.type;
    }
}
