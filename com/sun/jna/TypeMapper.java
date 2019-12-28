package com.sun.jna;

public interface TypeMapper
{
    FromNativeConverter getFromNativeConverter(Class<?> p0);
    
    ToNativeConverter getToNativeConverter(Class<?> p0);
}
