package com.sun.jna;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import com.sun.jna.win32.DLLCallback;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.ref.Reference;
import java.util.Map;
import java.lang.ref.WeakReference;

public class CallbackReference extends WeakReference<Callback>
{
    static final Map<Callback, CallbackReference> callbackMap;
    static final Map<Callback, CallbackReference> directCallbackMap;
    static final Map<Pointer, Reference<Callback>> pointerCallbackMap;
    static final Map<Object, Object> allocations;
    private static final Map<CallbackReference, Reference<CallbackReference>> allocatedMemory;
    private static final Method PROXY_CALLBACK_METHOD;
    private static final Map<Callback, CallbackThreadInitializer> initializers;
    Pointer cbstruct;
    Pointer trampoline;
    CallbackProxy proxy;
    Method method;
    int callingConvention;
    
    static CallbackThreadInitializer setCallbackThreadInitializer(final Callback a1, final CallbackThreadInitializer a2) {
        /*SL:78*/synchronized (CallbackReference.initializers) {
            /*SL:79*/if (a2 != null) {
                /*SL:80*/return CallbackReference.initializers.put(a1, a2);
            }
            /*SL:82*/return CallbackReference.initializers.remove(a1);
        }
    }
    
    private static ThreadGroup initializeThread(Callback a1, final AttachOptions a2) {
        CallbackThreadInitializer v1 = /*EL:105*/null;
        /*SL:106*/if (a1 instanceof DefaultCallbackProxy) {
            /*SL:107*/a1 = ((DefaultCallbackProxy)a1).getCallback();
        }
        /*SL:109*/synchronized (CallbackReference.initializers) {
            /*SL:110*/v1 = CallbackReference.initializers.get(a1);
        }
        ThreadGroup v2 = /*EL:112*/null;
        /*SL:113*/if (v1 != null) {
            /*SL:114*/v2 = v1.getThreadGroup(a1);
            /*SL:115*/a2.name = v1.getName(a1);
            /*SL:116*/a2.daemon = v1.isDaemon(a1);
            /*SL:117*/a2.detach = v1.detach(a1);
            /*SL:118*/a2.write();
        }
        /*SL:120*/return v2;
    }
    
    public static Callback getCallback(final Class<?> a1, final Pointer a2) {
        /*SL:131*/return getCallback(a1, a2, false);
    }
    
    private static Callback getCallback(final Class<?> v-7, final Pointer v-6, final boolean v-5) {
        /*SL:135*/if (v-6 == null) {
            /*SL:136*/return null;
        }
        /*SL:139*/if (!v-7.isInterface()) {
            /*SL:140*/throw new IllegalArgumentException("Callback type must be an interface");
        }
        final Map<Callback, CallbackReference> map = /*EL:141*/v-5 ? CallbackReference.directCallbackMap : CallbackReference.callbackMap;
        /*SL:142*/synchronized (CallbackReference.pointerCallbackMap) {
            Callback a1 = /*EL:143*/null;
            final Reference<Callback> a2 = CallbackReference.pointerCallbackMap.get(/*EL:144*/v-6);
            /*SL:145*/if (a2 == null) {
                final int a3 = /*EL:155*/AltCallingConvention.class.isAssignableFrom(v-7) ? 63 : 0;
                final Map<String, Object> v1 = /*EL:157*/new HashMap<String, Object>(Native.getLibraryOptions(v-7));
                /*SL:158*/v1.put("invoking-method", getCallbackMethod(v-7));
                final NativeFunctionHandler v2 = /*EL:159*/new NativeFunctionHandler(v-6, a3, v1);
                /*SL:160*/a1 = (Callback)Proxy.newProxyInstance(v-7.getClassLoader(), new Class[] { v-7 }, v2);
                /*SL:162*/map.remove(a1);
                CallbackReference.pointerCallbackMap.put(/*EL:163*/v-6, new WeakReference<Callback>(a1));
                /*SL:164*/return a1;
            }
            a1 = a2.get();
            if (a1 != null && !v-7.isAssignableFrom(a1.getClass())) {
                throw new IllegalStateException("Pointer " + v-6 + " already mapped to " + a1 + ".\nNative code may be re-using a default function pointer, in which case you may need to use a common Callback class wherever the function pointer is reused.");
            }
            return a1;
        }
    }
    
    private CallbackReference(final Callback v-9, final int v-8, boolean v-7) {
        super(v-9);
        final TypeMapper typeMapper = Native.getTypeMapper(v-9.getClass());
        this.callingConvention = v-8;
        final boolean ppc = Platform.isPPC();
        if (v-7) {
            Method a2 = getCallbackMethod(v-9);
            a2 = a2.getParameterTypes();
            for (int a3 = 0; a3 < a2.length; ++a3) {
                if (ppc && (a2[a3] == Float.TYPE || a2[a3] == Double.TYPE)) {
                    v-7 = false;
                    break;
                }
                if (typeMapper != null && typeMapper.getFromNativeConverter(a2[a3]) != null) {
                    v-7 = false;
                    break;
                }
            }
            if (typeMapper != null && typeMapper.getToNativeConverter(a2.getReturnType()) != null) {
                v-7 = false;
            }
        }
        final String stringEncoding = Native.getStringEncoding(v-9.getClass());
        long a4 = 0L;
        if (v-7) {
            this.method = getCallbackMethod(v-9);
            final Class<?>[] array = this.method.getParameterTypes();
            final Class<?> clazz = this.method.getReturnType();
            int v1 = 1;
            if (v-9 instanceof DLLCallback) {
                v1 |= 0x2;
            }
            a4 = Native.createNativeCallback(v-9, this.method, array, clazz, v-8, v1, stringEncoding);
        }
        else {
            if (v-9 instanceof CallbackProxy) {
                this.proxy = (CallbackProxy)v-9;
            }
            else {
                this.proxy = new DefaultCallbackProxy(getCallbackMethod(v-9), typeMapper, stringEncoding);
            }
            final Class<?>[] array = /*EL:165*/this.proxy.getParameterTypes();
            Class<?> clazz = this.proxy.getReturnType();
            if (typeMapper != null) {
                for (int v1 = 0; v1 < array.length; ++v1) {
                    final FromNativeConverter v2 = typeMapper.getFromNativeConverter(array[v1]);
                    if (v2 != null) {
                        array[v1] = v2.nativeType();
                    }
                }
                final ToNativeConverter v3 = typeMapper.getToNativeConverter(clazz);
                if (v3 != null) {
                    clazz = v3.nativeType();
                }
            }
            for (int v1 = 0; v1 < array.length; ++v1) {
                array[v1] = this.getNativeType(array[v1]);
                if (!isAllowableNativeType(array[v1])) {
                    final String v4 = "Callback argument " + array[v1] + " requires custom type conversion";
                    throw new IllegalArgumentException(v4);
                }
            }
            clazz = this.getNativeType(clazz);
            if (!isAllowableNativeType(clazz)) {
                final String v5 = "Callback return type " + clazz + " requires custom type conversion";
                throw new IllegalArgumentException(v5);
            }
            int v1 = (v-9 instanceof DLLCallback) ? 2 : 0;
            a4 = Native.createNativeCallback(this.proxy, CallbackReference.PROXY_CALLBACK_METHOD, array, clazz, v-8, v1, stringEncoding);
        }
        this.cbstruct = ((a4 != 0L) ? new Pointer(a4) : null);
        CallbackReference.allocatedMemory.put(this, new WeakReference<CallbackReference>(this));
    }
    
    private Class<?> getNativeType(final Class<?> a1) {
        /*SL:273*/if (Structure.class.isAssignableFrom(a1)) {
            /*SL:275*/Structure.validate(a1);
            /*SL:276*/if (!Structure.ByValue.class.isAssignableFrom(a1)) {
                /*SL:277*/return Pointer.class;
            }
        }
        else {
            /*SL:278*/if (NativeMapped.class.isAssignableFrom(a1)) {
                /*SL:279*/return NativeMappedConverter.getInstance(a1).nativeType();
            }
            /*SL:280*/if (a1 == String.class || a1 == WString.class || a1 == String[].class || a1 == WString[].class || Callback.class.isAssignableFrom(a1)) {
                /*SL:285*/return Pointer.class;
            }
        }
        /*SL:287*/return a1;
    }
    
    private static Method checkMethod(final Method v1) {
        /*SL:291*/if (v1.getParameterTypes().length > 256) {
            final String a1 = /*EL:292*/"Method signature exceeds the maximum parameter count: " + v1;
            /*SL:294*/throw new UnsupportedOperationException(a1);
        }
        /*SL:296*/return v1;
    }
    
    static Class<?> findCallbackClass(final Class<?> v-1) {
        /*SL:305*/if (!Callback.class.isAssignableFrom(v-1)) {
            /*SL:306*/throw new IllegalArgumentException(v-1.getName() + " is not derived from com.sun.jna.Callback");
        }
        /*SL:308*/if (v-1.isInterface()) {
            /*SL:309*/return v-1;
        }
        final Class<?>[] v0 = /*EL:311*/v-1.getInterfaces();
        /*SL:312*/for (int v = 0; v < v0.length; ++v) {
            /*SL:313*/if (Callback.class.isAssignableFrom(v0[v])) {
                try {
                    getCallbackMethod(/*EL:316*/v0[v]);
                    /*SL:317*/return v0[v];
                }
                catch (IllegalArgumentException a1) {
                    /*SL:320*/break;
                }
            }
        }
        /*SL:324*/if (Callback.class.isAssignableFrom(v-1.getSuperclass())) {
            /*SL:325*/return findCallbackClass(v-1.getSuperclass());
        }
        /*SL:327*/return v-1;
    }
    
    private static Method getCallbackMethod(final Callback a1) {
        /*SL:331*/return getCallbackMethod(findCallbackClass(a1.getClass()));
    }
    
    private static Method getCallbackMethod(final Class<?> v-3) {
        final Method[] declaredMethods = /*EL:336*/v-3.getDeclaredMethods();
        final Method[] methods = /*EL:337*/v-3.getMethods();
        final Set<Method> v0 = /*EL:338*/new HashSet<Method>(Arrays.<Method>asList(declaredMethods));
        /*SL:339*/v0.retainAll(Arrays.<Method>asList(methods));
        final Iterator<Method> v = /*EL:342*/v0.iterator();
        while (v.hasNext()) {
            final Method a1 = /*EL:343*/v.next();
            /*SL:344*/if (Callback.FORBIDDEN_NAMES.contains(a1.getName())) {
                /*SL:345*/v.remove();
            }
        }
        final Method[] v2 = /*EL:349*/v0.<Method>toArray(new Method[v0.size()]);
        /*SL:350*/if (v2.length == 1) {
            /*SL:351*/return checkMethod(v2[0]);
        }
        /*SL:353*/for (int v3 = 0; v3 < v2.length; ++v3) {
            final Method v4 = /*EL:354*/v2[v3];
            /*SL:355*/if ("callback".equals(v4.getName())) {
                /*SL:356*/return checkMethod(v4);
            }
        }
        final String v5 = /*EL:359*/"Callback must implement a single public method, or one public method named 'callback'";
        /*SL:361*/throw new IllegalArgumentException(v5);
    }
    
    private void setCallbackOptions(final int a1) {
        /*SL:366*/this.cbstruct.setInt(Pointer.SIZE, a1);
    }
    
    public Pointer getTrampoline() {
        /*SL:371*/if (this.trampoline == null) {
            /*SL:372*/this.trampoline = this.cbstruct.getPointer(0L);
        }
        /*SL:374*/return this.trampoline;
    }
    
    @Override
    protected void finalize() {
        /*SL:380*/this.dispose();
    }
    
    protected synchronized void dispose() {
        /*SL:385*/if (this.cbstruct != null) {
            try {
                /*SL:387*/Native.freeNativeCallback(this.cbstruct.peer);
            }
            finally {
                /*SL:389*/this.cbstruct.peer = 0L;
                /*SL:390*/this.cbstruct = null;
                CallbackReference.allocatedMemory.remove(/*EL:391*/this);
            }
        }
    }
    
    static void disposeAll() {
        final Collection<CallbackReference> collection = /*EL:399*/new LinkedList<CallbackReference>(CallbackReference.allocatedMemory.keySet());
        /*SL:400*/for (final CallbackReference v1 : collection) {
            /*SL:401*/v1.dispose();
        }
    }
    
    private Callback getCallback() {
        /*SL:406*/return this.get();
    }
    
    private static Pointer getNativeFunctionPointer(final Callback v1) {
        /*SL:413*/if (Proxy.isProxyClass(v1.getClass())) {
            final Object a1 = /*EL:414*/Proxy.getInvocationHandler(v1);
            /*SL:415*/if (a1 instanceof NativeFunctionHandler) {
                /*SL:416*/return ((NativeFunctionHandler)a1).getPointer();
            }
        }
        /*SL:419*/return null;
    }
    
    public static Pointer getFunctionPointer(final Callback a1) {
        /*SL:426*/return getFunctionPointer(a1, false);
    }
    
    private static Pointer getFunctionPointer(final Callback a2, final boolean v1) {
        Pointer v2 = /*EL:431*/null;
        /*SL:432*/if (a2 == null) {
            /*SL:433*/return null;
        }
        /*SL:435*/if ((v2 = getNativeFunctionPointer(a2)) != null) {
            /*SL:436*/return v2;
        }
        final Map<String, ?> v3 = /*EL:438*/Native.getLibraryOptions(a2.getClass());
        final int v4 = /*EL:439*/(int)((a2 instanceof AltCallingConvention) ? 63 : ((v3 != null && v3.containsKey("calling-convention")) ? /*EL:441*/v3.get("calling-convention") : /*EL:442*/0));
        final Map<Callback, CallbackReference> v5 = /*EL:445*/v1 ? CallbackReference.directCallbackMap : CallbackReference.callbackMap;
        /*SL:446*/synchronized (CallbackReference.pointerCallbackMap) {
            CallbackReference a3 = /*EL:447*/v5.get(a2);
            /*SL:448*/if (a3 == null) {
                /*SL:449*/a3 = new CallbackReference(a2, v4, v1);
                /*SL:450*/v5.put(a2, a3);
                CallbackReference.pointerCallbackMap.put(/*EL:451*/a3.getTrampoline(), new WeakReference<Callback>(a2));
                /*SL:452*/if (CallbackReference.initializers.containsKey(a2)) {
                    /*SL:453*/a3.setCallbackOptions(1);
                }
            }
            /*SL:456*/return a3.getTrampoline();
        }
    }
    
    private static boolean isAllowableNativeType(final Class<?> a1) {
        /*SL:691*/return a1 == Void.TYPE || a1 == Void.class || a1 == Boolean.TYPE || a1 == Boolean.class || a1 == Byte.TYPE || a1 == Byte.class || a1 == Short.TYPE || a1 == Short.class || a1 == Character.TYPE || a1 == Character.class || a1 == Integer.TYPE || a1 == Integer.class || a1 == Long.TYPE || a1 == Long.class || a1 == Float.TYPE || a1 == Float.class || a1 == Double.TYPE || a1 == Double.class || (Structure.ByValue.class.isAssignableFrom(a1) && /*EL:700*/Structure.class.isAssignableFrom(a1)) || /*EL:701*/Pointer.class.isAssignableFrom(a1);
    }
    
    private static Pointer getNativeString(final Object a2, final boolean v1) {
        /*SL:706*/if (a2 != null) {
            final NativeString a3 = /*EL:707*/new NativeString(a2.toString(), v1);
            CallbackReference.allocations.put(/*EL:709*/a2, a3);
            /*SL:710*/return a3.getPointer();
        }
        /*SL:712*/return null;
    }
    
    static {
        callbackMap = new WeakHashMap<Callback, CallbackReference>();
        directCallbackMap = new WeakHashMap<Callback, CallbackReference>();
        pointerCallbackMap = new WeakHashMap<Pointer, Reference<Callback>>();
        allocations = new WeakHashMap<Object, Object>();
        allocatedMemory = Collections.<CallbackReference, Reference<CallbackReference>>synchronizedMap(new WeakHashMap<CallbackReference, Reference<CallbackReference>>());
        try {
            PROXY_CALLBACK_METHOD = CallbackProxy.class.getMethod("callback", Object[].class);
        }
        catch (Exception v1) {
            throw new Error("Error looking up CallbackProxy.callback() method");
        }
        initializers = new WeakHashMap<Callback, CallbackThreadInitializer>();
    }
    
    static class AttachOptions extends Structure
    {
        public static final List<String> FIELDS;
        public boolean daemon;
        public boolean detach;
        public String name;
        
        AttachOptions() {
            this.setStringEncoding("utf8");
        }
        
        @Override
        protected List<String> getFieldOrder() {
            /*SL:99*/return AttachOptions.FIELDS;
        }
        
        static {
            FIELDS = Structure.createFieldsOrder("daemon", "detach", "name");
        }
    }
    
    private class DefaultCallbackProxy implements CallbackProxy
    {
        private final Method callbackMethod;
        private ToNativeConverter toNative;
        private final FromNativeConverter[] fromNative;
        private final String encoding;
        
        public DefaultCallbackProxy(final Method a4, final TypeMapper v1, final String v2) {
            this.callbackMethod = a4;
            this.encoding = v2;
            final Class<?>[] v3 = a4.getParameterTypes();
            final Class<?> v4 = a4.getReturnType();
            this.fromNative = new FromNativeConverter[v3.length];
            if (NativeMapped.class.isAssignableFrom(v4)) {
                this.toNative = NativeMappedConverter.getInstance(v4);
            }
            else if (v1 != null) {
                this.toNative = v1.getToNativeConverter(v4);
            }
            for (int a5 = 0; a5 < this.fromNative.length; ++a5) {
                if (NativeMapped.class.isAssignableFrom(v3[a5])) {
                    this.fromNative[a5] = new NativeMappedConverter(v3[a5]);
                }
                else if (v1 != null) {
                    this.fromNative[a5] = v1.getFromNativeConverter(v3[a5]);
                }
            }
            if (!a4.isAccessible()) {
                try {
                    a4.setAccessible(true);
                }
                catch (SecurityException a6) {
                    throw new IllegalArgumentException("Callback method is inaccessible, make sure the interface is public: " + a4);
                }
            }
        }
        
        public Callback getCallback() {
            /*SL:496*/return CallbackReference.this.getCallback();
        }
        
        private Object invokeCallback(final Object[] v-3) {
            final Class<?>[] parameterTypes = /*EL:500*/this.callbackMethod.getParameterTypes();
            final Object[] array = /*EL:501*/new Object[v-3.length];
            /*SL:504*/for (int v0 = 0; v0 < v-3.length; ++v0) {
                final Class<?> v = /*EL:505*/parameterTypes[v0];
                final Object v2 = /*EL:506*/v-3[v0];
                /*SL:507*/if (this.fromNative[v0] != null) {
                    final FromNativeContext a1 = /*EL:508*/new CallbackParameterContext(v, this.callbackMethod, v-3, v0);
                    /*SL:510*/array[v0] = this.fromNative[v0].fromNative(v2, a1);
                }
                else {
                    /*SL:512*/array[v0] = this.convertArgument(v2, v);
                }
            }
            Object v3 = /*EL:516*/null;
            final Callback v4 = /*EL:517*/this.getCallback();
            /*SL:518*/if (v4 != null) {
                try {
                    /*SL:520*/v3 = this.convertResult(this.callbackMethod.invoke(v4, array));
                }
                catch (IllegalArgumentException v5) {
                    /*SL:523*/Native.getCallbackExceptionHandler().uncaughtException(v4, v5);
                }
                catch (IllegalAccessException v6) {
                    /*SL:526*/Native.getCallbackExceptionHandler().uncaughtException(v4, v6);
                }
                catch (InvocationTargetException v7) {
                    /*SL:529*/Native.getCallbackExceptionHandler().uncaughtException(v4, v7.getTargetException());
                }
            }
            /*SL:533*/for (int v8 = 0; v8 < array.length; ++v8) {
                /*SL:534*/if (array[v8] instanceof Structure && !(array[v8] instanceof Structure.ByValue)) {
                    /*SL:536*/((Structure)array[v8]).autoWrite();
                }
            }
            /*SL:540*/return v3;
        }
        
        @Override
        public Object callback(final Object[] v2) {
            try {
                /*SL:551*/return this.invokeCallback(v2);
            }
            catch (Throwable a1) {
                /*SL:554*/Native.getCallbackExceptionHandler().uncaughtException(this.getCallback(), a1);
                /*SL:555*/return null;
            }
        }
        
        private Object convertArgument(Object v-1, final Class<?> v0) {
            /*SL:563*/if (v-1 instanceof Pointer) {
                /*SL:564*/if (v0 == String.class) {
                    /*SL:565*/v-1 = ((Pointer)v-1).getString(0L, this.encoding);
                }
                else/*SL:567*/ if (v0 == WString.class) {
                    /*SL:568*/v-1 = new WString(((Pointer)v-1).getWideString(0L));
                }
                else/*SL:570*/ if (v0 == String[].class) {
                    /*SL:571*/v-1 = ((Pointer)v-1).getStringArray(0L, this.encoding);
                }
                else/*SL:573*/ if (v0 == WString[].class) {
                    /*SL:574*/v-1 = ((Pointer)v-1).getWideStringArray(0L);
                }
                else/*SL:576*/ if (Callback.class.isAssignableFrom(v0)) {
                    /*SL:577*/v-1 = CallbackReference.getCallback(v0, (Pointer)v-1);
                }
                else/*SL:579*/ if (Structure.class.isAssignableFrom(v0)) {
                    /*SL:582*/if (Structure.ByValue.class.isAssignableFrom(v0)) {
                        final Structure a1 = /*EL:583*/Structure.newInstance(v0);
                        final byte[] a2 = /*EL:584*/new byte[a1.size()];
                        /*SL:585*/((Pointer)v-1).read(0L, a2, 0, a2.length);
                        /*SL:586*/a1.getPointer().write(0L, a2, 0, a2.length);
                        /*SL:587*/a1.read();
                        /*SL:588*/v-1 = a1;
                    }
                    else {
                        final Structure v = /*EL:590*/Structure.newInstance(v0, (Pointer)v-1);
                        /*SL:591*/v.conditionalAutoRead();
                        /*SL:592*/v-1 = v;
                    }
                }
            }
            else/*SL:596*/ if ((Boolean.TYPE == v0 || Boolean.class == v0) && v-1 instanceof Number) {
                /*SL:598*/v-1 = Function.valueOf(((Number)v-1).intValue() != 0);
            }
            /*SL:600*/return v-1;
        }
        
        private Object convertResult(Object v2) {
            /*SL:604*/if (this.toNative != null) {
                /*SL:605*/v2 = this.toNative.toNative(v2, new CallbackResultContext(this.callbackMethod));
            }
            /*SL:607*/if (v2 == null) {
                /*SL:608*/return null;
            }
            final Class<?> v3 = /*EL:611*/v2.getClass();
            /*SL:612*/if (Structure.class.isAssignableFrom(v3)) {
                /*SL:613*/if (Structure.ByValue.class.isAssignableFrom(v3)) {
                    /*SL:614*/return v2;
                }
                /*SL:616*/return ((Structure)v2).getPointer();
            }
            else {
                /*SL:617*/if (v3 == Boolean.TYPE || v3 == Boolean.class) {
                    /*SL:618*/return Boolean.TRUE.equals(v2) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE;
                }
                /*SL:620*/if (v3 == String.class || v3 == WString.class) {
                    /*SL:621*/return getNativeString(v2, v3 == WString.class);
                }
                /*SL:622*/if (v3 == String[].class || v3 == WString.class) {
                    final StringArray a1 = /*EL:623*/(v3 == String[].class) ? new StringArray((String[])v2, this.encoding) : new StringArray((WString[])v2);
                    CallbackReference.allocations.put(/*EL:627*/v2, a1);
                    /*SL:628*/return a1;
                }
                /*SL:629*/if (Callback.class.isAssignableFrom(v3)) {
                    /*SL:630*/return CallbackReference.getFunctionPointer((Callback)v2);
                }
                /*SL:632*/return v2;
            }
        }
        
        @Override
        public Class<?>[] getParameterTypes() {
            /*SL:636*/return this.callbackMethod.getParameterTypes();
        }
        
        @Override
        public Class<?> getReturnType() {
            /*SL:640*/return this.callbackMethod.getReturnType();
        }
    }
    
    private static class NativeFunctionHandler implements InvocationHandler
    {
        private final Function function;
        private final Map<String, ?> options;
        
        public NativeFunctionHandler(final Pointer a1, final int a2, final Map<String, ?> a3) {
            this.options = a3;
            this.function = new Function(a1, a2, (String)a3.get("string-encoding"));
        }
        
        @Override
        public Object invoke(final Object v-2, final Method v-1, Object[] v0) throws Throwable {
            /*SL:660*/if (Library.Handler.OBJECT_TOSTRING.equals(v-1)) {
                String a1 = /*EL:661*/"Proxy interface to " + this.function;
                final Method a2 = /*EL:662*/(Method)this.options.get("invoking-method");
                final Class<?> a3 = /*EL:663*/CallbackReference.findCallbackClass(a2.getDeclaringClass());
                /*SL:664*/a1 = a1 + " (" + a3.getName() + ")";
                /*SL:666*/return a1;
            }
            /*SL:667*/if (Library.Handler.OBJECT_HASHCODE.equals(v-1)) {
                /*SL:668*/return this.hashCode();
            }
            /*SL:669*/if (!Library.Handler.OBJECT_EQUALS.equals(v-1)) {
                /*SL:676*/if (Function.isVarArgs(v-1)) {
                    /*SL:677*/v0 = Function.concatenateVarArgs(v0);
                }
                /*SL:679*/return this.function.invoke(v-1.getReturnType(), v0, this.options);
            }
            final Object v = v0[0];
            if (v != null && Proxy.isProxyClass(v.getClass())) {
                return Function.valueOf(Proxy.getInvocationHandler(v) == this);
            }
            return Boolean.FALSE;
        }
        
        public Pointer getPointer() {
            /*SL:683*/return this.function;
        }
    }
}
