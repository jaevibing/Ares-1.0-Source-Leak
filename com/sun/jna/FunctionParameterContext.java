package com.sun.jna;

public class FunctionParameterContext extends ToNativeContext
{
    private Function function;
    private Object[] args;
    private int index;
    
    FunctionParameterContext(final Function a1, final Object[] a2, final int a3) {
        this.function = a1;
        this.args = a2;
        this.index = a3;
    }
    
    public Function getFunction() {
        /*SL:39*/return this.function;
    }
    
    public Object[] getParameters() {
        /*SL:41*/return this.args;
    }
    
    public int getParameterIndex() {
        /*SL:42*/return this.index;
    }
}
