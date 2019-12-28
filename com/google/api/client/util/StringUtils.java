package com.google.api.client.util;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;

public class StringUtils
{
    public static final String LINE_SEPARATOR;
    
    public static byte[] getBytesUtf8(final String a1) {
        /*SL:57*/return com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils.getBytesUtf8(a1);
    }
    
    public static String newStringUtf8(final byte[] a1) {
        /*SL:73*/return com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils.newStringUtf8(a1);
    }
    
    static {
        LINE_SEPARATOR = System.getProperty("line.separator");
    }
}
