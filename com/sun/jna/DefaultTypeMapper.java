package com.sun.jna;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class DefaultTypeMapper implements TypeMapper
{
    private List<Entry> toNativeConverters;
    private List<Entry> fromNativeConverters;
    
    public DefaultTypeMapper() {
        this.toNativeConverters = new ArrayList<Entry>();
        this.fromNativeConverters = new ArrayList<Entry>();
    }
    
    private Class<?> getAltClass(final Class<?> a1) {
        /*SL:59*/if (a1 == Boolean.class) {
            /*SL:60*/return Boolean.TYPE;
        }
        /*SL:61*/if (a1 == Boolean.TYPE) {
            /*SL:62*/return Boolean.class;
        }
        /*SL:63*/if (a1 == Byte.class) {
            /*SL:64*/return Byte.TYPE;
        }
        /*SL:65*/if (a1 == Byte.TYPE) {
            /*SL:66*/return Byte.class;
        }
        /*SL:67*/if (a1 == Character.class) {
            /*SL:68*/return Character.TYPE;
        }
        /*SL:69*/if (a1 == Character.TYPE) {
            /*SL:70*/return Character.class;
        }
        /*SL:71*/if (a1 == Short.class) {
            /*SL:72*/return Short.TYPE;
        }
        /*SL:73*/if (a1 == Short.TYPE) {
            /*SL:74*/return Short.class;
        }
        /*SL:75*/if (a1 == Integer.class) {
            /*SL:76*/return Integer.TYPE;
        }
        /*SL:77*/if (a1 == Integer.TYPE) {
            /*SL:78*/return Integer.class;
        }
        /*SL:79*/if (a1 == Long.class) {
            /*SL:80*/return Long.TYPE;
        }
        /*SL:81*/if (a1 == Long.TYPE) {
            /*SL:82*/return Long.class;
        }
        /*SL:83*/if (a1 == Float.class) {
            /*SL:84*/return Float.TYPE;
        }
        /*SL:85*/if (a1 == Float.TYPE) {
            /*SL:86*/return Float.class;
        }
        /*SL:87*/if (a1 == Double.class) {
            /*SL:88*/return Double.TYPE;
        }
        /*SL:89*/if (a1 == Double.TYPE) {
            /*SL:90*/return Double.class;
        }
        /*SL:92*/return null;
    }
    
    public void addToNativeConverter(final Class<?> a1, final ToNativeConverter a2) {
        /*SL:102*/this.toNativeConverters.add(new Entry(a1, a2));
        final Class<?> v1 = /*EL:103*/this.getAltClass(a1);
        /*SL:104*/if (v1 != null) {
            /*SL:105*/this.toNativeConverters.add(new Entry(v1, a2));
        }
    }
    
    public void addFromNativeConverter(final Class<?> a1, final FromNativeConverter a2) {
        /*SL:117*/this.fromNativeConverters.add(new Entry(a1, a2));
        final Class<?> v1 = /*EL:118*/this.getAltClass(a1);
        /*SL:119*/if (v1 != null) {
            /*SL:120*/this.fromNativeConverters.add(new Entry(v1, a2));
        }
    }
    
    public void addTypeConverter(final Class<?> a1, final TypeConverter a2) {
        /*SL:133*/this.addFromNativeConverter(a1, a2);
        /*SL:134*/this.addToNativeConverter(a1, a2);
    }
    
    private Object lookupConverter(final Class<?> v1, final Collection<? extends Entry> v2) {
        /*SL:138*/for (final Entry a1 : v2) {
            /*SL:139*/if (a1.type.isAssignableFrom(v1)) {
                /*SL:140*/return a1.converter;
            }
        }
        /*SL:143*/return null;
    }
    
    @Override
    public FromNativeConverter getFromNativeConverter(final Class<?> a1) {
        /*SL:148*/return (FromNativeConverter)this.lookupConverter(a1, this.fromNativeConverters);
    }
    
    @Override
    public ToNativeConverter getToNativeConverter(final Class<?> a1) {
        /*SL:153*/return (ToNativeConverter)this.lookupConverter(a1, this.toNativeConverters);
    }
    
    private static class Entry
    {
        public Class<?> type;
        public Object converter;
        
        public Entry(final Class<?> a1, final Object a2) {
            this.type = a1;
            this.converter = a2;
        }
    }
}
