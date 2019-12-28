package com.sun.jna;

import java.lang.reflect.Method;

public class MethodParameterContext extends FunctionParameterContext
{
    private Method method;
    
    MethodParameterContext(final Function a1, final Object[] a2, final int a3, final Method a4) {
        super(a1, a2, a3);
        this.method = a4;
    }
    
    public Method getMethod() {
        /*SL:37*/return this.method;
    }
}
