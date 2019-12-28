package com.fasterxml.jackson.core;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class Base64Variants
{
    static final String STD_BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    public static final Base64Variant MIME;
    public static final Base64Variant MIME_NO_LINEFEEDS;
    public static final Base64Variant PEM;
    public static final Base64Variant MODIFIED_FOR_URL;
    
    public static Base64Variant getDefaultVariant() {
        /*SL:84*/return Base64Variants.MIME_NO_LINEFEEDS;
    }
    
    public static Base64Variant valueOf(String a1) throws IllegalArgumentException {
        /*SL:92*/if (Base64Variants.MIME._name.equals(a1)) {
            /*SL:93*/return Base64Variants.MIME;
        }
        /*SL:95*/if (Base64Variants.MIME_NO_LINEFEEDS._name.equals(a1)) {
            /*SL:96*/return Base64Variants.MIME_NO_LINEFEEDS;
        }
        /*SL:98*/if (Base64Variants.PEM._name.equals(a1)) {
            /*SL:99*/return Base64Variants.PEM;
        }
        /*SL:101*/if (Base64Variants.MODIFIED_FOR_URL._name.equals(a1)) {
            /*SL:102*/return Base64Variants.MODIFIED_FOR_URL;
        }
        /*SL:104*/if (a1 == null) {
            /*SL:105*/a1 = "<null>";
        }
        else {
            /*SL:107*/a1 = "'" + a1 + "'";
        }
        /*SL:109*/throw new IllegalArgumentException("No Base64Variant with name " + a1);
    }
    
    static {
        MIME = new Base64Variant("MIME", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", true, '=', 76);
        MIME_NO_LINEFEEDS = new Base64Variant(Base64Variants.MIME, "MIME-NO-LINEFEEDS", Integer.MAX_VALUE);
        PEM = new Base64Variant(Base64Variants.MIME, "PEM", true, '=', 64);
        final StringBuilder v1 = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
        v1.setCharAt(v1.indexOf("+"), '-');
        v1.setCharAt(v1.indexOf("/"), '_');
        MODIFIED_FOR_URL = new Base64Variant("MODIFIED-FOR-URL", v1.toString(), false, '\0', Integer.MAX_VALUE);
    }
}
