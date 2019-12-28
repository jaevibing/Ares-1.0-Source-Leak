package com.sun.jna;

public interface NativeMapped
{
    Object fromNative(Object p0, FromNativeContext p1);
    
    Object toNative();
    
    Class<?> nativeType();
}
