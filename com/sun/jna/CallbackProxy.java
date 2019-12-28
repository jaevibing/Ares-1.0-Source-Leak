package com.sun.jna;

public interface CallbackProxy extends Callback
{
    Object callback(Object[] p0);
    
    Class<?>[] getParameterTypes();
    
    Class<?> getReturnType();
}
