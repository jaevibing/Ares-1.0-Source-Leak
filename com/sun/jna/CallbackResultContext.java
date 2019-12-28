package com.sun.jna;

import java.lang.reflect.Method;

public class CallbackResultContext extends ToNativeContext
{
    private Method method;
    
    CallbackResultContext(final Method a1) {
        this.method = a1;
    }
    
    public Method getMethod() {
        /*SL:32*/return this.method;
    }
}
