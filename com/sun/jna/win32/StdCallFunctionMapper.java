package com.sun.jna.win32;

import com.sun.jna.Function;
import java.lang.reflect.Method;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeMapped;
import com.sun.jna.FunctionMapper;

public class StdCallFunctionMapper implements FunctionMapper
{
    protected int getArgumentNativeStackSize(Class<?> v2) {
        /*SL:48*/if (NativeMapped.class.isAssignableFrom(v2)) {
            /*SL:49*/v2 = NativeMappedConverter.getInstance(v2).nativeType();
        }
        /*SL:51*/if (v2.isArray()) {
            /*SL:52*/return Pointer.SIZE;
        }
        try {
            /*SL:55*/return Native.getNativeSize(v2);
        }
        catch (IllegalArgumentException a1) {
            /*SL:57*/throw new IllegalArgumentException("Unknown native stack allocation size for " + v2);
        }
    }
    
    @Override
    public String getFunctionName(final NativeLibrary v-7, final Method v-6) {
        String s = /*EL:70*/v-6.getName();
        int n = /*EL:71*/0;
        final Class<?>[] parameterTypes;
        final Class<?>[] array = /*EL:73*/parameterTypes = v-6.getParameterTypes();
        for (final Class<?> a1 : parameterTypes) {
            /*SL:74*/n += this.getArgumentNativeStackSize(a1);
        }
        final String string = /*EL:77*/s + "@" + n;
        final int n2 = /*EL:78*/63;
        try {
            final Function a2 = /*EL:80*/v-7.getFunction(string, n2);
            /*SL:81*/s = a2.getName();
        }
        catch (UnsatisfiedLinkError v2) {
            try {
                final Function v1 = /*EL:85*/v-7.getFunction("_" + string, n2);
                /*SL:86*/s = v1.getName();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        /*SL:92*/return s;
    }
}
