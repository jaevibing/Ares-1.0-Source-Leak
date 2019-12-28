package com.sun.jna;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class StringArray extends Memory implements Function.PostCallRead
{
    private String encoding;
    private List<NativeString> natives;
    private Object[] original;
    
    public StringArray(final String[] a1) {
        this(a1, false);
    }
    
    public StringArray(final String[] a1, final boolean a2) {
        this((Object[])a1, a2 ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
    }
    
    public StringArray(final String[] a1, final String a2) {
        this((Object[])a1, a2);
    }
    
    public StringArray(final WString[] a1) {
        this(a1, "--WIDE-STRING--");
    }
    
    private StringArray(final Object[] v-1, final String v0) {
        super((v-1.length + 1) * Pointer.SIZE);
        this.natives = new ArrayList<NativeString>();
        this.original = v-1;
        this.encoding = v0;
        for (int v = 0; v < v-1.length; ++v) {
            Pointer a2 = null;
            if (v-1[v] != null) {
                a2 = new NativeString(v-1[v].toString(), v0);
                this.natives.add(a2);
                a2 = a2.getPointer();
            }
            this.setPointer(Pointer.SIZE * v, a2);
        }
        this.setPointer(Pointer.SIZE * v-1.length, null);
    }
    
    @Override
    public void read() {
        final boolean b = /*EL:73*/this.original instanceof WString[];
        final boolean equals = /*EL:74*/"--WIDE-STRING--".equals(this.encoding);
        /*SL:75*/for (int v0 = 0; v0 < this.original.length; ++v0) {
            final Pointer v = /*EL:76*/this.getPointer(v0 * Pointer.SIZE);
            Object v2 = /*EL:77*/null;
            /*SL:78*/if (v != null) {
                /*SL:79*/v2 = (equals ? v.getWideString(0L) : v.getString(0L, this.encoding));
                /*SL:80*/if (b) {
                    v2 = new WString((String)v2);
                }
            }
            /*SL:82*/this.original[v0] = v2;
        }
    }
    
    @Override
    public String toString() {
        final boolean v1 = /*EL:88*/"--WIDE-STRING--".equals(this.encoding);
        String v2 = /*EL:89*/v1 ? "const wchar_t*[]" : "const char*[]";
        /*SL:90*/v2 += Arrays.<Object>asList(this.original);
        /*SL:91*/return v2;
    }
}
