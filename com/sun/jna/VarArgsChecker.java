package com.sun.jna;

import java.lang.reflect.Method;

abstract class VarArgsChecker
{
    static VarArgsChecker create() {
        try {
            final Method v1 = /*EL:79*/Method.class.getMethod("isVarArgs", (Class<?>[])new Class[0]);
            /*SL:80*/if (v1 != null) {
                /*SL:82*/return new RealVarArgsChecker();
            }
            /*SL:84*/return new NoVarArgsChecker();
        }
        catch (NoSuchMethodException v2) {
            /*SL:87*/return new NoVarArgsChecker();
        }
        catch (SecurityException v3) {
            /*SL:89*/return new NoVarArgsChecker();
        }
    }
    
    abstract boolean isVarArgs(final Method p0);
    
    abstract int fixedArgs(final Method p0);
    
    private static final class RealVarArgsChecker extends VarArgsChecker
    {
        private RealVarArgsChecker() {
            super(null);
        }
        
        @Override
        boolean isVarArgs(final Method a1) {
            /*SL:47*/return a1.isVarArgs();
        }
        
        @Override
        int fixedArgs(final Method a1) {
            /*SL:52*/return a1.isVarArgs() ? (a1.getParameterTypes().length - 1) : 0;
        }
    }
    
    private static final class NoVarArgsChecker extends VarArgsChecker
    {
        private NoVarArgsChecker() {
            super(null);
        }
        
        @Override
        boolean isVarArgs(final Method a1) {
            /*SL:63*/return false;
        }
        
        @Override
        int fixedArgs(final Method a1) {
            /*SL:67*/return 0;
        }
    }
}
