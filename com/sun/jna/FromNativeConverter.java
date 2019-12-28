package com.sun.jna;

public interface FromNativeConverter
{
    Object fromNative(Object p0, FromNativeContext p1);
    
    Class<?> nativeType();
}
