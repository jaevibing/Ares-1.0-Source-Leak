package com.fasterxml.jackson.core;

public enum JsonEncoding
{
    UTF8("UTF-8", false, 8), 
    UTF16_BE("UTF-16BE", true, 16), 
    UTF16_LE("UTF-16LE", false, 16), 
    UTF32_BE("UTF-32BE", true, 32), 
    UTF32_LE("UTF-32LE", false, 32);
    
    private final String _javaName;
    private final boolean _bigEndian;
    private final int _bits;
    
    private JsonEncoding(final String a1, final boolean a2, final int a3) {
        this._javaName = a1;
        this._bigEndian = a2;
        this._bits = a3;
    }
    
    public String getJavaName() {
        /*SL:44*/return this._javaName;
    }
    
    public boolean isBigEndian() {
        /*SL:54*/return this._bigEndian;
    }
    
    public int bits() {
        /*SL:56*/return this._bits;
    }
}
