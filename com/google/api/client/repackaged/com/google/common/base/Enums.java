package com.google.api.client.repackaged.com.google.common.base;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.WeakHashMap;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.HashMap;
import java.lang.reflect.Field;
import com.google.api.client.repackaged.com.google.common.annotations.GwtIncompatible;
import java.lang.ref.WeakReference;
import java.util.Map;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Enums
{
    @GwtIncompatible
    private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache;
    
    @GwtIncompatible
    public static Field getField(final Enum<?> v1) {
        final Class<?> v2 = /*EL:51*/v1.getDeclaringClass();
        try {
            /*SL:53*/return v2.getDeclaredField(v1.name());
        }
        catch (NoSuchFieldException a1) {
            /*SL:55*/throw new AssertionError((Object)a1);
        }
    }
    
    public static <T extends Enum<T>> Optional<T> getIfPresent(final Class<T> a1, final String a2) {
        /*SL:68*/Preconditions.<Class<T>>checkNotNull(a1);
        /*SL:69*/Preconditions.<String>checkNotNull(a2);
        /*SL:70*/return Platform.<T>getEnumIfPresent(a1, a2);
    }
    
    @GwtIncompatible
    private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(final Class<T> v-1) {
        final Map<String, WeakReference<? extends Enum<?>>> v0 = /*EL:82*/new HashMap<String, WeakReference<? extends Enum<?>>>();
        /*SL:84*/for (final T a1 : EnumSet.<T>allOf(v-1)) {
            /*SL:85*/v0.put(a1.name(), new WeakReference<Enum<?>>(a1));
        }
        Enums.enumConstantCache.put(/*EL:87*/v-1, v0);
        /*SL:88*/return v0;
    }
    
    @GwtIncompatible
    static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(final Class<T> v1) {
        /*SL:94*/synchronized (Enums.enumConstantCache) {
            Map<String, WeakReference<? extends Enum<?>>> a1 = Enums.enumConstantCache.get(/*EL:95*/v1);
            /*SL:96*/if (a1 == null) {
                /*SL:97*/a1 = Enums.<Enum>populateCache((Class<Enum>)v1);
            }
            /*SL:99*/return a1;
        }
    }
    
    public static <T extends Enum<T>> Converter<String, T> stringConverter(final Class<T> a1) {
        /*SL:112*/return (Converter<String, T>)new StringConverter((Class<Enum>)a1);
    }
    
    static {
        enumConstantCache = new WeakHashMap<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>>();
    }
    
    private static final class StringConverter<T extends Enum<T>> extends Converter<String, T> implements Serializable
    {
        private final Class<T> enumClass;
        private static final long serialVersionUID = 0L;
        
        StringConverter(final Class<T> a1) {
            this.enumClass = Preconditions.<Class<T>>checkNotNull(a1);
        }
        
        @Override
        protected T doForward(final String a1) {
            /*SL:126*/return Enum.<T>valueOf(this.enumClass, a1);
        }
        
        @Override
        protected String doBackward(final T a1) {
            /*SL:131*/return a1.name();
        }
        
        @Override
        public boolean equals(@Nullable final Object v2) {
            /*SL:136*/if (v2 instanceof StringConverter) {
                final StringConverter<?> a1 = /*EL:137*/(StringConverter<?>)v2;
                /*SL:138*/return this.enumClass.equals(a1.enumClass);
            }
            /*SL:140*/return false;
        }
        
        @Override
        public int hashCode() {
            /*SL:145*/return this.enumClass.hashCode();
        }
        
        @Override
        public String toString() {
            /*SL:150*/return "Enums.stringConverter(" + this.enumClass.getName() + ".class)";
        }
    }
}
