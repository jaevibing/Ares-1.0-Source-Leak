package com.sun.jna.win32;

import com.sun.jna.ToNativeConverter;
import com.sun.jna.FromNativeContext;
import com.sun.jna.WString;
import com.sun.jna.StringArray;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.DefaultTypeMapper;

public class W32APITypeMapper extends DefaultTypeMapper
{
    public static final TypeMapper UNICODE;
    public static final TypeMapper ASCII;
    public static final TypeMapper DEFAULT;
    
    protected W32APITypeMapper(final boolean v2) {
        if (v2) {
            final TypeConverter a1 = new TypeConverter() {
                @Override
                public Object toNative(final Object a1, final ToNativeContext a2) {
                    /*SL:55*/if (a1 == null) {
                        /*SL:56*/return null;
                    }
                    /*SL:57*/if (a1 instanceof String[]) {
                        /*SL:58*/return new StringArray((String[])a1, true);
                    }
                    /*SL:60*/return new WString(a1.toString());
                }
                
                @Override
                public Object fromNative(final Object a1, final FromNativeContext a2) {
                    /*SL:64*/if (a1 == null) {
                        /*SL:65*/return null;
                    }
                    /*SL:66*/return a1.toString();
                }
                
                @Override
                public Class<?> nativeType() {
                    /*SL:70*/return WString.class;
                }
            };
            this.addTypeConverter(String.class, a1);
            this.addToNativeConverter(String[].class, a1);
        }
        final TypeConverter v3 = new TypeConverter() {
            @Override
            public Object toNative(final Object a1, final ToNativeContext a2) {
                /*SL:79*/return Boolean.TRUE.equals(a1) ? 1 : 0;
            }
            
            @Override
            public Object fromNative(final Object a1, final FromNativeContext a2) {
                /*SL:83*/return ((int)a1 != 0) ? Boolean.TRUE : Boolean.FALSE;
            }
            
            @Override
            public Class<?> nativeType() {
                /*SL:88*/return Integer.class;
            }
        };
        this.addTypeConverter(Boolean.class, v3);
    }
    
    static {
        UNICODE = new W32APITypeMapper(true);
        ASCII = new W32APITypeMapper(false);
        DEFAULT = (Boolean.getBoolean("w32.ascii") ? W32APITypeMapper.ASCII : W32APITypeMapper.UNICODE);
    }
}
