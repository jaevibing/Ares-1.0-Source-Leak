package com.sun.jna;

import java.nio.CharBuffer;

class NativeString implements CharSequence, Comparable
{
    static final String WIDE_STRING = "--WIDE-STRING--";
    private Pointer pointer;
    private String encoding;
    
    public NativeString(final String a1) {
        this(a1, Native.getDefaultStringEncoding());
    }
    
    public NativeString(final String a1, final boolean a2) {
        this(a1, a2 ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
    }
    
    public NativeString(final WString a1) {
        this(a1.toString(), "--WIDE-STRING--");
    }
    
    public NativeString(final String v2, final String v3) {
        if (v2 == null) {
            throw new NullPointerException("String must not be null");
        }
        this.encoding = v3;
        if ("--WIDE-STRING--".equals(this.encoding)) {
            final int a1 = (v2.length() + 1) * Native.WCHAR_SIZE;
            (this.pointer = new StringMemory(a1)).setWideString(0L, v2);
        }
        else {
            final byte[] a2 = Native.getBytes(v2, v3);
            (this.pointer = new StringMemory(a2.length + 1)).write(0L, a2, 0, a2.length);
            this.pointer.setByte(a2.length, (byte)0);
        }
    }
    
    @Override
    public int hashCode() {
        /*SL:100*/return this.toString().hashCode();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:105*/return a1 instanceof CharSequence && /*EL:106*/this.compareTo(a1) == 0;
    }
    
    @Override
    public String toString() {
        final boolean v1 = /*EL:113*/"--WIDE-STRING--".equals(this.encoding);
        String v2 = /*EL:114*/v1 ? "const wchar_t*" : "const char*";
        /*SL:115*/v2 = v2 + "(" + (v1 ? this.pointer.getWideString(0L) : this.pointer.getString(0L, this.encoding)) + ")";
        /*SL:116*/return v2;
    }
    
    public Pointer getPointer() {
        /*SL:120*/return this.pointer;
    }
    
    @Override
    public char charAt(final int a1) {
        /*SL:125*/return this.toString().charAt(a1);
    }
    
    @Override
    public int length() {
        /*SL:130*/return this.toString().length();
    }
    
    @Override
    public CharSequence subSequence(final int a1, final int a2) {
        /*SL:135*/return CharBuffer.wrap(this.toString()).subSequence(a1, a2);
    }
    
    @Override
    public int compareTo(final Object a1) {
        /*SL:140*/if (a1 == null) {
            /*SL:141*/return 1;
        }
        /*SL:143*/return this.toString().compareTo(a1.toString());
    }
    
    private class StringMemory extends Memory
    {
        public StringMemory(final long a1) {
            super(a1);
        }
        
        @Override
        public String toString() {
            /*SL:45*/return NativeString.this.toString();
        }
    }
}
