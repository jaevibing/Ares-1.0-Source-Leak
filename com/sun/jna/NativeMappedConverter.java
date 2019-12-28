package com.sun.jna;

import java.util.WeakHashMap;
import java.lang.ref.SoftReference;
import java.lang.ref.Reference;
import java.util.Map;

public class NativeMappedConverter implements TypeConverter
{
    private static final Map<Class<?>, Reference<NativeMappedConverter>> converters;
    private final Class<?> type;
    private final Class<?> nativeType;
    private final NativeMapped instance;
    
    public static NativeMappedConverter getInstance(final Class<?> v-2) {
        /*SL:40*/synchronized (NativeMappedConverter.converters) {
            final Reference<NativeMappedConverter> a1 = NativeMappedConverter.converters.get(/*EL:41*/v-2);
            NativeMappedConverter v1 = /*EL:42*/(a1 != null) ? a1.get() : null;
            /*SL:43*/if (v1 == null) {
                /*SL:44*/v1 = new NativeMappedConverter(v-2);
                NativeMappedConverter.converters.put(/*EL:45*/v-2, new SoftReference<NativeMappedConverter>(v1));
            }
            /*SL:47*/return v1;
        }
    }
    
    public NativeMappedConverter(final Class<?> a1) {
        if (!NativeMapped.class.isAssignableFrom(a1)) {
            throw new IllegalArgumentException("Type must derive from " + NativeMapped.class);
        }
        this.type = a1;
        this.instance = this.defaultValue();
        this.nativeType = this.instance.nativeType();
    }
    
    public NativeMapped defaultValue() {
        try {
            /*SL:61*/return (NativeMapped)this.type.newInstance();
        }
        catch (InstantiationException v2) {
            final String v1 = /*EL:63*/"Can't create an instance of " + this.type + ", requires a no-arg constructor: " + v2;
            /*SL:65*/throw new IllegalArgumentException(v1);
        }
        catch (IllegalAccessException v3) {
            final String v1 = /*EL:67*/"Not allowed to create an instance of " + this.type + ", requires a public, no-arg constructor: " + v3;
            /*SL:69*/throw new IllegalArgumentException(v1);
        }
    }
    
    @Override
    public Object fromNative(final Object a1, final FromNativeContext a2) {
        /*SL:74*/return this.instance.fromNative(a1, a2);
    }
    
    @Override
    public Class<?> nativeType() {
        /*SL:79*/return this.nativeType;
    }
    
    @Override
    public Object toNative(Object a1, final ToNativeContext a2) {
        /*SL:84*/if (a1 == null) {
            /*SL:85*/if (Pointer.class.isAssignableFrom(this.nativeType)) {
                /*SL:86*/return null;
            }
            /*SL:88*/a1 = this.defaultValue();
        }
        /*SL:90*/return ((NativeMapped)a1).toNative();
    }
    
    static {
        converters = new WeakHashMap<Class<?>, Reference<NativeMappedConverter>>();
    }
}
