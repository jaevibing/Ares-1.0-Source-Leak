package com.google.api.client.util;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

public class Base64
{
    public static byte[] encodeBase64(final byte[] a1) {
        /*SL:42*/return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64(a1);
    }
    
    public static String encodeBase64String(final byte[] a1) {
        /*SL:53*/return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64String(a1);
    }
    
    public static byte[] encodeBase64URLSafe(final byte[] a1) {
        /*SL:67*/return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64URLSafe(a1);
    }
    
    public static String encodeBase64URLSafeString(final byte[] a1) {
        /*SL:79*/return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(a1);
    }
    
    public static byte[] decodeBase64(final byte[] a1) {
        /*SL:90*/return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.decodeBase64(a1);
    }
    
    public static byte[] decodeBase64(final String a1) {
        /*SL:101*/return com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.decodeBase64(a1);
    }
}
