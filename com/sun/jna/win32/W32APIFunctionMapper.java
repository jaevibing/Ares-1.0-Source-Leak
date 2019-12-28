package com.sun.jna.win32;

import java.lang.reflect.Method;
import com.sun.jna.NativeLibrary;
import com.sun.jna.FunctionMapper;

public class W32APIFunctionMapper implements FunctionMapper
{
    public static final FunctionMapper UNICODE;
    public static final FunctionMapper ASCII;
    private final String suffix;
    
    protected W32APIFunctionMapper(final boolean a1) {
        this.suffix = (a1 ? "W" : "A");
    }
    
    @Override
    public String getFunctionName(final NativeLibrary a1, final Method a2) {
        String v1 = /*EL:42*/a2.getName();
        /*SL:43*/if (!v1.endsWith("W") && !v1.endsWith("A")) {
            try {
                /*SL:45*/v1 = a1.getFunction(v1 + this.suffix, 63).getName();
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
        }
        /*SL:51*/return v1;
    }
    
    static {
        UNICODE = new W32APIFunctionMapper(true);
        ASCII = new W32APIFunctionMapper(false);
    }
}
