package com.sun.jna;

public interface ToNativeConverter
{
    Object toNative(Object p0, ToNativeContext p1);
    
    Class<?> nativeType();
}
