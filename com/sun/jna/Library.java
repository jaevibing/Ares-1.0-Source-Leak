package com.sun.jna;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

public interface Library
{
    public static final String OPTION_TYPE_MAPPER = "type-mapper";
    public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
    public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
    public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
    public static final String OPTION_STRING_ENCODING = "string-encoding";
    public static final String OPTION_ALLOW_OBJECTS = "allow-objects";
    public static final String OPTION_CALLING_CONVENTION = "calling-convention";
    public static final String OPTION_OPEN_FLAGS = "open-flags";
    public static final String OPTION_CLASSLOADER = "classloader";
    
    public static class Handler implements InvocationHandler
    {
        static final Method OBJECT_TOSTRING;
        static final Method OBJECT_HASHCODE;
        static final Method OBJECT_EQUALS;
        private final NativeLibrary nativeLibrary;
        private final Class<?> interfaceClass;
        private final Map<String, Object> options;
        private final InvocationMapper invocationMapper;
        private final Map<Method, FunctionInfo> functions;
        
        public Handler(String a1, Class<?> a2, Map<String, ?> a3) {
            int v1;
            this.functions = new WeakHashMap<Method, FunctionInfo>();
            if (a1 != null && "".equals(a1.trim())) {
                throw new IllegalArgumentException("Invalid library name \"" + a1 + "\"");
            }
            if (!a2.isInterface()) {
                throw new IllegalArgumentException(a1 + " does not implement an interface: " + a2.getName());
            }
            this.interfaceClass = a2;
            this.options = new HashMap<String, Object>(a3);
            v1 = (AltCallingConvention.class.isAssignableFrom(a2) ? 63 : 0);
            if (this.options.get("calling-convention") == null) {
                this.options.put("calling-convention", v1);
            }
            if (this.options.get("classloader") == null) {
                this.options.put("classloader", a2.getClassLoader());
            }
            this.nativeLibrary = NativeLibrary.getInstance(a1, this.options);
            this.invocationMapper = this.options.get("invocation-mapper");
        }
        
        public NativeLibrary getNativeLibrary() {
            /*SL:184*/return this.nativeLibrary;
        }
        
        public String getLibraryName() {
            /*SL:188*/return this.nativeLibrary.getName();
        }
        
        public Class<?> getInterfaceClass() {
            /*SL:192*/return this.interfaceClass;
        }
        
        @Override
        public Object invoke(Object v-6, Method v-5, Object[] v-4) throws Throwable {
            Object a1;
            FunctionInfo functionInfo;
            boolean a2;
            InvocationHandler a3;
            Function v1;
            Class<?>[] v2;
            Map<String, Object> v3;
            /*SL:200*/if (Handler.OBJECT_TOSTRING.equals(v-5)) {
                /*SL:201*/return "Proxy interface to " + this.nativeLibrary;
            }
            /*SL:202*/if (Handler.OBJECT_HASHCODE.equals(v-5)) {
                /*SL:203*/return this.hashCode();
            }
            /*SL:204*/if (Handler.OBJECT_EQUALS.equals(v-5)) {
                /*SL:205*/a1 = v-4[0];
                /*SL:206*/if (a1 != null && Proxy.isProxyClass(a1.getClass())) {
                    /*SL:207*/return Function.valueOf(Proxy.getInvocationHandler(a1) == this);
                }
                /*SL:209*/return Boolean.FALSE;
            }
            else {
                /*SL:213*/functionInfo = this.functions.get(v-5);
                /*SL:214*/if (functionInfo == null) {
                    /*SL:215*/synchronized (this.functions) {
                        /*SL:216*/functionInfo = this.functions.get(v-5);
                        /*SL:217*/if (functionInfo == null) {
                            /*SL:218*/a2 = Function.isVarArgs(v-5);
                            /*SL:219*/a3 = null;
                            /*SL:220*/if (this.invocationMapper != null) {
                                /*SL:221*/a3 = this.invocationMapper.getInvocationHandler(this.nativeLibrary, v-5);
                            }
                            /*SL:223*/v1 = null;
                            /*SL:224*/v2 = null;
                            /*SL:225*/v3 = null;
                            /*SL:226*/if (a3 == null) {
                                /*SL:228*/v1 = this.nativeLibrary.getFunction(v-5.getName(), v-5);
                                /*SL:229*/v2 = v-5.getParameterTypes();
                                /*SL:230*/v3 = new HashMap<String, Object>(this.options);
                                /*SL:231*/v3.put("invoking-method", v-5);
                            }
                            /*SL:233*/functionInfo = new FunctionInfo(a3, v1, v2, a2, v3);
                            /*SL:234*/this.functions.put(v-5, functionInfo);
                        }
                    }
                }
                /*SL:238*/if (functionInfo.isVarArgs) {
                    /*SL:239*/v-4 = Function.concatenateVarArgs(v-4);
                }
                /*SL:241*/if (functionInfo.handler != null) {
                    /*SL:242*/return functionInfo.handler.invoke(v-6, v-5, v-4);
                }
                /*SL:244*/return functionInfo.function.invoke(v-5, functionInfo.parameterTypes, v-5.getReturnType(), v-4, functionInfo.options);
            }
        }
        
        static {
            try {
                OBJECT_TOSTRING = Object.class.getMethod("toString", (Class<?>[])new Class[0]);
                OBJECT_HASHCODE = Object.class.getMethod("hashCode", (Class<?>[])new Class[0]);
                OBJECT_EQUALS = Object.class.getMethod("equals", Object.class);
            }
            catch (Exception v1) {
                throw new Error("Error retrieving Object.toString() method");
            }
        }
        
        private static final class FunctionInfo
        {
            final InvocationHandler handler;
            final Function function;
            final boolean isVarArgs;
            final Map<String, ?> options;
            final Class<?>[] parameterTypes;
            
            FunctionInfo(InvocationHandler a1, Function a2, Class<?>[] a3, boolean a4, Map<String, ?> a5) {
                this.handler = a1;
                this.function = a2;
                this.isVarArgs = a4;
                this.options = a5;
                this.parameterTypes = a3;
            }
        }
    }
}
