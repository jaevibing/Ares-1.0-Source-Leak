package com.google.api.client.repackaged.org.apache.commons.codec.binary;

import java.io.UnsupportedEncodingException;

public class StringUtils
{
    public static byte[] getBytesIso8859_1(final String a1) {
        /*SL:49*/return getBytesUnchecked(a1, "ISO-8859-1");
    }
    
    public static byte[] getBytesUsAscii(final String a1) {
        /*SL:65*/return getBytesUnchecked(a1, "US-ASCII");
    }
    
    public static byte[] getBytesUtf16(final String a1) {
        /*SL:81*/return getBytesUnchecked(a1, "UTF-16");
    }
    
    public static byte[] getBytesUtf16Be(final String a1) {
        /*SL:97*/return getBytesUnchecked(a1, "UTF-16BE");
    }
    
    public static byte[] getBytesUtf16Le(final String a1) {
        /*SL:113*/return getBytesUnchecked(a1, "UTF-16LE");
    }
    
    public static byte[] getBytesUtf8(final String a1) {
        /*SL:129*/return getBytesUnchecked(a1, "UTF-8");
    }
    
    public static byte[] getBytesUnchecked(final String a2, final String v1) {
        /*SL:152*/if (a2 == null) {
            /*SL:153*/return null;
        }
        try {
            /*SL:156*/return a2.getBytes(v1);
        }
        catch (UnsupportedEncodingException a3) {
            throw newIllegalStateException(/*EL:158*/v1, a3);
        }
    }
    
    private static IllegalStateException newIllegalStateException(final String a1, final UnsupportedEncodingException a2) {
        /*SL:163*/return new IllegalStateException(a1 + ": " + a2);
    }
    
    public static String newString(final byte[] a2, final String v1) {
        /*SL:186*/if (a2 == null) {
            /*SL:187*/return null;
        }
        try {
            /*SL:190*/return new String(a2, v1);
        }
        catch (UnsupportedEncodingException a3) {
            throw newIllegalStateException(/*EL:192*/v1, a3);
        }
    }
    
    public static String newStringIso8859_1(final byte[] a1) {
        /*SL:208*/return newString(a1, "ISO-8859-1");
    }
    
    public static String newStringUsAscii(final byte[] a1) {
        /*SL:223*/return newString(a1, "US-ASCII");
    }
    
    public static String newStringUtf16(final byte[] a1) {
        /*SL:238*/return newString(a1, "UTF-16");
    }
    
    public static String newStringUtf16Be(final byte[] a1) {
        /*SL:253*/return newString(a1, "UTF-16BE");
    }
    
    public static String newStringUtf16Le(final byte[] a1) {
        /*SL:268*/return newString(a1, "UTF-16LE");
    }
    
    public static String newStringUtf8(final byte[] a1) {
        /*SL:283*/return newString(a1, "UTF-8");
    }
}
