package com.sun.jna;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public class Function extends Pointer
{
    public static final int MAX_NARGS = 256;
    public static final int C_CONVENTION = 0;
    public static final int ALT_CONVENTION = 63;
    private static final int MASK_CC = 63;
    public static final int THROW_LAST_ERROR = 64;
    public static final int USE_VARARGS = 384;
    static final Integer INTEGER_TRUE;
    static final Integer INTEGER_FALSE;
    private NativeLibrary library;
    private final String functionName;
    final String encoding;
    final int callFlags;
    final Map<String, ?> options;
    static final String OPTION_INVOKING_METHOD = "invoking-method";
    private static final VarArgsChecker IS_VARARGS;
    
    public static Function getFunction(final String a1, final String a2) {
        /*SL:95*/return NativeLibrary.getInstance(a1).getFunction(a2);
    }
    
    public static Function getFunction(final String a1, final String a2, final int a3) {
        /*SL:116*/return NativeLibrary.getInstance(a1).getFunction(a2, a3, null);
    }
    
    public static Function getFunction(final String a1, final String a2, final int a3, final String a4) {
        /*SL:140*/return NativeLibrary.getInstance(a1).getFunction(a2, a3, a4);
    }
    
    public static Function getFunction(final Pointer a1) {
        /*SL:155*/return getFunction(a1, 0, null);
    }
    
    public static Function getFunction(final Pointer a1, final int a2) {
        /*SL:173*/return getFunction(a1, a2, null);
    }
    
    public static Function getFunction(final Pointer a1, final int a2, final String a3) {
        /*SL:194*/return new Function(a1, a2, a3);
    }
    
    Function(final NativeLibrary a3, final String a4, final int v1, final String v2) {
        this.checkCallingConvention(v1 & 0x3F);
        if (a4 == null) {
            throw new NullPointerException("Function name must not be null");
        }
        this.library = a3;
        this.functionName = a4;
        this.callFlags = v1;
        this.options = a3.options;
        this.encoding = ((v2 != null) ? v2 : Native.getDefaultStringEncoding());
        try {
            this.peer = a3.getSymbolAddress(a4);
        }
        catch (UnsatisfiedLinkError a5) {
            throw new UnsatisfiedLinkError("Error looking up function '" + a4 + "': " + a5.getMessage());
        }
    }
    
    Function(final Pointer a1, final int a2, final String a3) {
        this.checkCallingConvention(a2 & 0x3F);
        if (a1 == null || a1.peer == 0L) {
            throw new NullPointerException("Function address may not be null");
        }
        this.functionName = a1.toString();
        this.callFlags = a2;
        this.peer = a1.peer;
        this.options = (Map<String, ?>)Collections.EMPTY_MAP;
        this.encoding = ((a3 != null) ? a3 : Native.getDefaultStringEncoding());
    }
    
    private void checkCallingConvention(final int a1) throws IllegalArgumentException {
        /*SL:281*/if ((a1 & 0x3F) != a1) {
            /*SL:282*/throw new IllegalArgumentException("Unrecognized calling convention: " + a1);
        }
    }
    
    public String getName() {
        /*SL:288*/return this.functionName;
    }
    
    public int getCallingConvention() {
        /*SL:292*/return this.callFlags & 0x3F;
    }
    
    public Object invoke(final Class<?> a1, final Object[] a2) {
        /*SL:299*/return this.invoke(a1, a2, this.options);
    }
    
    public Object invoke(final Class<?> a1, final Object[] a2, final Map<String, ?> a3) {
        final Method v1 = /*EL:306*/(Method)a3.get("invoking-method");
        final Class<?>[] v2 = /*EL:307*/(Class<?>[])((v1 != null) ? v1.getParameterTypes() : null);
        /*SL:308*/return this.invoke(v1, v2, a1, a2, a3);
    }
    
    Object invoke(final Method v-18, final Class<?>[] v-17, final Class<?> v-16, final Object[] v-15, final Map<String, ?> v-14) {
        Object[] array = /*EL:319*/new Object[0];
        /*SL:320*/if (v-15 != null) {
            /*SL:321*/if (v-15.length > 256) {
                /*SL:322*/throw new UnsupportedOperationException("Maximum argument count is 256");
            }
            /*SL:324*/array = new Object[v-15.length];
            /*SL:325*/System.arraycopy(v-15, 0, array, 0, array.length);
        }
        final TypeMapper v-19 = /*EL:328*/(TypeMapper)v-14.get("type-mapper");
        final boolean equals = Boolean.TRUE.equals(/*EL:329*/v-14.get("allow-objects"));
        final boolean b = /*EL:330*/array.length > 0 && v-18 != null && isVarArgs(v-18);
        final int v-20 = /*EL:331*/(array.length > 0 && v-18 != null) ? fixedArgs(v-18) : 0;
        /*SL:332*/for (Class<?> a2 = (Class<?>)0; a2 < array.length; ++a2) {
            /*SL:335*/a2 = ((v-18 != null) ? ((b && a2 >= v-17.length - 1) ? v-17[v-17.length - 1].getComponentType() : v-17[a2]) : null);
            /*SL:338*/array[a2] = this.convertArgument(array, a2, v-18, v-19, equals, a2);
        }
        Class<?> a3 = /*EL:341*/v-16;
        FromNativeConverter fromNativeConverter = /*EL:342*/null;
        /*SL:343*/if (NativeMapped.class.isAssignableFrom(v-16)) {
            final NativeMappedConverter a4 = /*EL:345*/(NativeMappedConverter)(fromNativeConverter = NativeMappedConverter.getInstance(v-16));
            /*SL:346*/a3 = a4.nativeType();
        }
        else/*SL:347*/ if (v-19 != null) {
            /*SL:348*/fromNativeConverter = v-19.getFromNativeConverter(v-16);
            /*SL:349*/if (fromNativeConverter != null) {
                /*SL:350*/a3 = fromNativeConverter.nativeType();
            }
        }
        Object o = /*EL:354*/this.invoke(array, a3, equals, v-20);
        /*SL:356*/if (fromNativeConverter != null) {
            final FromNativeContext a6;
            /*SL:358*/if (v-18 != null) {
                final FromNativeContext a5 = /*EL:359*/new MethodResultContext(v-16, this, v-15, v-18);
            }
            else {
                /*SL:361*/a6 = new FunctionResultContext(v-16, this, v-15);
            }
            /*SL:363*/o = fromNativeConverter.fromNative(o, a6);
        }
        /*SL:367*/if (v-15 != null) {
            /*SL:368*/for (int i = 0; i < v-15.length; ++i) {
                final Object o2 = /*EL:369*/v-15[i];
                /*SL:370*/if (o2 != null) {
                    /*SL:372*/if (o2 instanceof Structure) {
                        /*SL:373*/if (!(o2 instanceof Structure.ByValue)) {
                            /*SL:374*/((Structure)o2).autoRead();
                        }
                    }
                    else/*SL:376*/ if (array[i] instanceof PostCallRead) {
                        /*SL:377*/((PostCallRead)array[i]).read();
                        /*SL:378*/if (array[i] instanceof PointerArray) {
                            final PointerArray pointerArray = /*EL:379*/(PointerArray)array[i];
                            /*SL:380*/if (Structure.ByReference[].class.isAssignableFrom(o2.getClass())) {
                                final Class<?> componentType = /*EL:381*/o2.getClass().getComponentType();
                                final Structure[] array2 = /*EL:382*/(Structure[])o2;
                                /*SL:383*/for (int v0 = 0; v0 < array2.length; ++v0) {
                                    final Pointer v = /*EL:384*/pointerArray.getPointer(Pointer.SIZE * v0);
                                    /*SL:385*/array2[v0] = Structure.updateStructureByReference(componentType, array2[v0], v);
                                }
                            }
                        }
                    }
                    else/*SL:389*/ if (Structure[].class.isAssignableFrom(o2.getClass())) {
                        /*SL:390*/Structure.autoRead((Structure[])o2);
                    }
                }
            }
        }
        /*SL:395*/return o;
    }
    
    Object invoke(final Object[] a1, final Class<?> a2, final boolean a3) {
        /*SL:400*/return this.invoke(a1, a2, a3, 0);
    }
    
    Object invoke(final Object[] v-8, final Class<?> v-7, final boolean v-6, final int v-5) {
        Object o = /*EL:405*/null;
        final int a5 = /*EL:406*/this.callFlags | (v-5 & 0x3) << 7;
        /*SL:407*/if (v-7 == null || v-7 == Void.TYPE || v-7 == Void.class) {
            /*SL:408*/Native.invokeVoid(this, this.peer, a5, v-8);
            /*SL:409*/o = null;
        }
        else/*SL:410*/ if (v-7 == Boolean.TYPE || v-7 == Boolean.class) {
            /*SL:411*/o = valueOf(Native.invokeInt(this, this.peer, a5, v-8) != 0);
        }
        else/*SL:412*/ if (v-7 == Byte.TYPE || v-7 == Byte.class) {
            /*SL:413*/o = (byte)Native.invokeInt(this, this.peer, a5, v-8);
        }
        else/*SL:414*/ if (v-7 == Short.TYPE || v-7 == Short.class) {
            /*SL:415*/o = (short)Native.invokeInt(this, this.peer, a5, v-8);
        }
        else/*SL:416*/ if (v-7 == Character.TYPE || v-7 == Character.class) {
            /*SL:417*/o = (char)Native.invokeInt(this, this.peer, a5, v-8);
        }
        else/*SL:418*/ if (v-7 == Integer.TYPE || v-7 == Integer.class) {
            /*SL:419*/o = Native.invokeInt(this, this.peer, a5, v-8);
        }
        else/*SL:420*/ if (v-7 == Long.TYPE || v-7 == Long.class) {
            /*SL:421*/o = Native.invokeLong(this, this.peer, a5, v-8);
        }
        else/*SL:422*/ if (v-7 == Float.TYPE || v-7 == Float.class) {
            /*SL:423*/o = Native.invokeFloat(this, this.peer, a5, v-8);
        }
        else/*SL:424*/ if (v-7 == Double.TYPE || v-7 == Double.class) {
            /*SL:425*/o = Native.invokeDouble(this, this.peer, a5, v-8);
        }
        else/*SL:426*/ if (v-7 == String.class) {
            /*SL:427*/o = this.invokeString(a5, v-8, false);
        }
        else/*SL:428*/ if (v-7 == WString.class) {
            final String a1 = /*EL:429*/this.invokeString(a5, v-8, true);
            /*SL:430*/if (a1 != null) {
                /*SL:431*/o = new WString(a1);
            }
        }
        else {
            /*SL:433*/if (Pointer.class.isAssignableFrom(v-7)) {
                /*SL:434*/return this.invokePointer(a5, v-8);
            }
            /*SL:435*/if (Structure.class.isAssignableFrom(v-7)) {
                /*SL:436*/if (Structure.ByValue.class.isAssignableFrom(v-7)) {
                    final Structure a2 = /*EL:438*/Native.invokeStructure(this, this.peer, a5, v-8, /*EL:439*/Structure.newInstance(v-7));
                    /*SL:440*/a2.autoRead();
                    /*SL:441*/o = a2;
                }
                else {
                    /*SL:443*/o = this.invokePointer(a5, v-8);
                    /*SL:444*/if (o != null) {
                        final Structure a3 = /*EL:445*/Structure.newInstance(v-7, (Pointer)o);
                        /*SL:446*/a3.conditionalAutoRead();
                        /*SL:447*/o = a3;
                    }
                }
            }
            else/*SL:450*/ if (Callback.class.isAssignableFrom(v-7)) {
                /*SL:451*/o = this.invokePointer(a5, v-8);
                /*SL:452*/if (o != null) {
                    /*SL:453*/o = CallbackReference.getCallback(v-7, (Pointer)o);
                }
            }
            else/*SL:455*/ if (v-7 == String[].class) {
                final Pointer a4 = /*EL:456*/this.invokePointer(a5, v-8);
                /*SL:457*/if (a4 != null) {
                    /*SL:458*/o = a4.getStringArray(0L, this.encoding);
                }
            }
            else/*SL:460*/ if (v-7 == WString[].class) {
                final Pointer pointer = /*EL:461*/this.invokePointer(a5, v-8);
                /*SL:462*/if (pointer != null) {
                    final String[] wideStringArray = /*EL:463*/pointer.getWideStringArray(0L);
                    final WString[] v0 = /*EL:464*/new WString[wideStringArray.length];
                    /*SL:465*/for (int v = 0; v < wideStringArray.length; ++v) {
                        /*SL:466*/v0[v] = new WString(wideStringArray[v]);
                    }
                    /*SL:468*/o = v0;
                }
            }
            else/*SL:470*/ if (v-7 == Pointer[].class) {
                final Pointer pointer = /*EL:471*/this.invokePointer(a5, v-8);
                /*SL:472*/if (pointer != null) {
                    /*SL:473*/o = pointer.getPointerArray(0L);
                }
            }
            else {
                /*SL:475*/if (!v-6) {
                    /*SL:484*/throw new IllegalArgumentException("Unsupported return type " + v-7 + " in function " + this.getName());
                }
                o = Native.invokeObject(this, this.peer, a5, v-8);
                if (o != null && !v-7.isAssignableFrom(o.getClass())) {
                    throw new ClassCastException("Return type " + v-7 + " does not match result " + o.getClass());
                }
            }
        }
        /*SL:486*/return o;
    }
    
    private Pointer invokePointer(final int a1, final Object[] a2) {
        final long v1 = /*EL:490*/Native.invokePointer(this, this.peer, a1, a2);
        /*SL:491*/return (v1 == 0L) ? null : new Pointer(v1);
    }
    
    private Object convertArgument(final Object[] v-8, final int v-7, final Method v-6, final TypeMapper v-5, final boolean v-4, final Class<?> v-3) {
        Object native1 = /*EL:497*/v-8[v-7];
        /*SL:498*/if (native1 != null) {
            Class<?> a3 = /*EL:499*/native1.getClass();
            ToNativeConverter a2 = /*EL:500*/null;
            /*SL:501*/if (NativeMapped.class.isAssignableFrom(a3)) {
                /*SL:502*/a2 = NativeMappedConverter.getInstance(a3);
            }
            else/*SL:503*/ if (v-5 != null) {
                /*SL:504*/a2 = v-5.getToNativeConverter(a3);
            }
            /*SL:506*/if (a2 != null) {
                final ToNativeContext a4;
                /*SL:508*/if (v-6 != null) {
                    /*SL:509*/a3 = new MethodParameterContext(this, v-8, v-7, v-6);
                }
                else {
                    /*SL:512*/a4 = new FunctionParameterContext(this, v-8, v-7);
                }
                /*SL:514*/native1 = a2.toNative(native1, a4);
            }
        }
        /*SL:517*/if (native1 == null || this.isPrimitiveArray(native1.getClass())) {
            /*SL:518*/return native1;
        }
        Class<?> a3 = /*EL:521*/native1.getClass();
        /*SL:523*/if (native1 instanceof Structure) {
            final Structure v0 = /*EL:524*/(Structure)native1;
            /*SL:525*/v0.autoWrite();
            /*SL:526*/if (v0 instanceof Structure.ByValue) {
                Class<?> a5 = /*EL:528*/v0.getClass();
                /*SL:529*/if (v-6 != null) {
                    final Class<?>[] a6 = /*EL:530*/v-6.getParameterTypes();
                    /*SL:531*/if (Function.IS_VARARGS.isVarArgs(v-6)) {
                        /*SL:532*/if (v-7 < a6.length - 1) {
                            /*SL:533*/a5 = a6[v-7];
                        }
                        else {
                            final Class<?> a7 = /*EL:535*/a6[a6.length - 1].getComponentType();
                            /*SL:536*/if (a7 != Object.class) {
                                /*SL:537*/a5 = a7;
                            }
                        }
                    }
                    else {
                        /*SL:541*/a5 = a6[v-7];
                    }
                }
                /*SL:544*/if (Structure.ByValue.class.isAssignableFrom(a5)) {
                    /*SL:545*/return v0;
                }
            }
            /*SL:548*/return v0.getPointer();
        }
        /*SL:549*/if (native1 instanceof Callback) {
            /*SL:551*/return CallbackReference.getFunctionPointer((Callback)native1);
        }
        /*SL:552*/if (native1 instanceof String) {
            /*SL:557*/return new NativeString((String)native1, false).getPointer();
        }
        /*SL:558*/if (native1 instanceof WString) {
            /*SL:560*/return new NativeString(native1.toString(), true).getPointer();
        }
        /*SL:561*/if (native1 instanceof Boolean) {
            /*SL:564*/return Boolean.TRUE.equals(native1) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE;
        }
        /*SL:565*/if (String[].class == a3) {
            /*SL:566*/return new StringArray((String[])native1, this.encoding);
        }
        /*SL:567*/if (WString[].class == a3) {
            /*SL:568*/return new StringArray((WString[])native1);
        }
        /*SL:569*/if (Pointer[].class == a3) {
            /*SL:570*/return new PointerArray((Pointer[])native1);
        }
        /*SL:571*/if (NativeMapped[].class.isAssignableFrom(a3)) {
            /*SL:572*/return new NativeMappedArray((NativeMapped[])native1);
        }
        /*SL:573*/if (Structure[].class.isAssignableFrom(a3)) {
            final Structure[] v = /*EL:576*/(Structure[])native1;
            final Class<?> a5 = /*EL:577*/a3.getComponentType();
            final boolean v2 = /*EL:578*/Structure.ByReference.class.isAssignableFrom(a5);
            /*SL:579*/if (v-3 != null && /*EL:580*/!Structure.ByReference[].class.isAssignableFrom(v-3)) {
                /*SL:581*/if (v2) {
                    /*SL:582*/throw new IllegalArgumentException("Function " + this.getName() + " declared Structure[] at parameter " + v-7 + " but array of " + a5 + " was passed");
                }
                /*SL:587*/for (int v3 = 0; v3 < v.length; ++v3) {
                    /*SL:588*/if (v[v3] instanceof Structure.ByReference) {
                        /*SL:589*/throw new IllegalArgumentException("Function " + this.getName() + " declared Structure[] at parameter " + v-7 + " but element " + v3 + " is of Structure.ByReference type");
                    }
                }
            }
            /*SL:597*/if (v2) {
                /*SL:598*/Structure.autoWrite(v);
                final Pointer[] v4 = /*EL:599*/new Pointer[v.length + 1];
                /*SL:600*/for (int v5 = 0; v5 < v.length; ++v5) {
                    /*SL:601*/v4[v5] = ((v[v5] != null) ? v[v5].getPointer() : null);
                }
                /*SL:603*/return new PointerArray(v4);
            }
            /*SL:604*/if (v.length == 0) {
                /*SL:605*/throw new IllegalArgumentException("Structure array must have non-zero length");
            }
            /*SL:606*/if (v[0] == null) {
                /*SL:607*/Structure.newInstance(a5).toArray(v);
                /*SL:608*/return v[0].getPointer();
            }
            /*SL:610*/Structure.autoWrite(v);
            /*SL:611*/return v[0].getPointer();
        }
        else {
            /*SL:613*/if (a3.isArray()) {
                /*SL:614*/throw new IllegalArgumentException("Unsupported array argument type: " + a3.getComponentType());
            }
            /*SL:616*/if (v-4) {
                /*SL:617*/return native1;
            }
            /*SL:618*/if (!Native.isSupportedNativeType(native1.getClass())) {
                /*SL:619*/throw new IllegalArgumentException("Unsupported argument type " + native1.getClass().getName() + /*EL:620*/" at parameter " + v-7 + " of function " + this.getName());
            }
            /*SL:624*/return native1;
        }
    }
    
    private boolean isPrimitiveArray(final Class<?> a1) {
        /*SL:628*/return a1.isArray() && a1.getComponentType().isPrimitive();
    }
    
    public void invoke(final Object[] a1) {
        /*SL:639*/this.invoke(Void.class, a1);
    }
    
    private String invokeString(final int a1, final Object[] a2, final boolean a3) {
        final Pointer v1 = /*EL:654*/this.invokePointer(a1, a2);
        String v2 = /*EL:655*/null;
        /*SL:656*/if (v1 != null) {
            /*SL:657*/if (a3) {
                /*SL:658*/v2 = v1.getWideString(0L);
            }
            else {
                /*SL:661*/v2 = v1.getString(0L, this.encoding);
            }
        }
        /*SL:664*/return v2;
    }
    
    @Override
    public String toString() {
        /*SL:670*/if (this.library != null) {
            /*SL:671*/return "native function " + this.functionName + "(" + this.library.getName() + ")@0x" + /*EL:672*/Long.toHexString(this.peer);
        }
        /*SL:674*/return "native function@0x" + Long.toHexString(this.peer);
    }
    
    public Object invokeObject(final Object[] a1) {
        /*SL:681*/return this.invoke(Object.class, a1);
    }
    
    public Pointer invokePointer(final Object[] a1) {
        /*SL:688*/return (Pointer)this.invoke(Pointer.class, a1);
    }
    
    public String invokeString(final Object[] a1, final boolean a2) {
        final Object v1 = /*EL:699*/this.invoke((Class<?>)(a2 ? WString.class : String.class), a1);
        /*SL:700*/return (v1 != null) ? v1.toString() : null;
    }
    
    public int invokeInt(final Object[] a1) {
        /*SL:707*/return (int)this.invoke(Integer.class, a1);
    }
    
    public long invokeLong(final Object[] a1) {
        /*SL:713*/return (long)this.invoke(Long.class, a1);
    }
    
    public float invokeFloat(final Object[] a1) {
        /*SL:719*/return (float)this.invoke(Float.class, a1);
    }
    
    public double invokeDouble(final Object[] a1) {
        /*SL:725*/return (double)this.invoke(Double.class, a1);
    }
    
    public void invokeVoid(final Object[] a1) {
        /*SL:731*/this.invoke(Void.class, a1);
    }
    
    @Override
    public boolean equals(final Object v2) {
        /*SL:739*/if (v2 == this) {
            return true;
        }
        /*SL:740*/if (v2 == null) {
            return false;
        }
        /*SL:741*/if (v2.getClass() == this.getClass()) {
            final Function a1 = /*EL:742*/(Function)v2;
            /*SL:743*/return a1.callFlags == this.callFlags && a1.options.equals(this.options) && /*EL:744*/a1.peer == this.peer;
        }
        /*SL:747*/return false;
    }
    
    @Override
    public int hashCode() {
        /*SL:755*/return this.callFlags + this.options.hashCode() + super.hashCode();
    }
    
    static Object[] concatenateVarArgs(Object[] v-2) {
        /*SL:765*/if (v-2 != null && v-2.length > 0) {
            final Object o = /*EL:766*/v-2[v-2.length - 1];
            final Class<?> v0 = /*EL:767*/(o != null) ? o.getClass() : null;
            /*SL:768*/if (v0 != null && v0.isArray()) {
                final Object[] v = /*EL:769*/(Object[])o;
                /*SL:771*/for (int a1 = 0; a1 < v.length; ++a1) {
                    /*SL:772*/if (v[a1] instanceof Float) {
                        /*SL:773*/v[a1] = v[a1];
                    }
                }
                final Object[] v2 = /*EL:776*/new Object[v-2.length + v.length];
                /*SL:777*/System.arraycopy(v-2, 0, v2, 0, v-2.length - 1);
                /*SL:778*/System.arraycopy(v, 0, v2, v-2.length - 1, v.length);
                /*SL:784*/v2[v2.length - 1] = null;
                /*SL:785*/v-2 = v2;
            }
        }
        /*SL:788*/return v-2;
    }
    
    static boolean isVarArgs(final Method a1) {
        /*SL:793*/return Function.IS_VARARGS.isVarArgs(a1);
    }
    
    static int fixedArgs(final Method a1) {
        /*SL:798*/return Function.IS_VARARGS.fixedArgs(a1);
    }
    
    static Boolean valueOf(final boolean a1) {
        /*SL:832*/return a1 ? Boolean.TRUE : Boolean.FALSE;
    }
    
    static {
        INTEGER_TRUE = -1;
        INTEGER_FALSE = 0;
        IS_VARARGS = VarArgsChecker.create();
    }
    
    private static class NativeMappedArray extends Memory implements PostCallRead
    {
        private final NativeMapped[] original;
        
        public NativeMappedArray(final NativeMapped[] a1) {
            super(Native.getNativeSize(a1.getClass(), a1));
            this.setValue(0L, this.original = a1, this.original.getClass());
        }
        
        @Override
        public void read() {
            /*SL:810*/this.getValue(0L, this.original.getClass(), this.original);
        }
    }
    
    private static class PointerArray extends Memory implements PostCallRead
    {
        private final Pointer[] original;
        
        public PointerArray(final Pointer[] v2) {
            super(Pointer.SIZE * (v2.length + 1));
            this.original = v2;
            /*SL:811*/for (int a1 = 0; a1 < v2.length; ++a1) {
                this.setPointer(a1 * Pointer.SIZE, v2[a1]);
            }
            this.setPointer(Pointer.SIZE * v2.length, null);
        }
        
        @Override
        public void read() {
            /*SL:826*/this.read(0L, this.original, 0, this.original.length);
        }
    }
    
    public interface PostCallRead
    {
        void read();
    }
}
