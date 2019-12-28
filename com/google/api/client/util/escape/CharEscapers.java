package com.google.api.client.util.escape;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class CharEscapers
{
    private static final Escaper URI_ESCAPER;
    private static final Escaper URI_PATH_ESCAPER;
    private static final Escaper URI_RESERVED_ESCAPER;
    private static final Escaper URI_USERINFO_ESCAPER;
    private static final Escaper URI_QUERY_STRING_ESCAPER;
    
    public static String escapeUri(final String a1) {
        /*SL:75*/return CharEscapers.URI_ESCAPER.escape(a1);
    }
    
    public static String decodeUri(final String v1) {
        try {
            /*SL:92*/return URLDecoder.decode(v1, "UTF-8");
        }
        catch (UnsupportedEncodingException a1) {
            /*SL:95*/throw new RuntimeException(a1);
        }
    }
    
    public static String escapeUriPath(final String a1) {
        /*SL:127*/return CharEscapers.URI_PATH_ESCAPER.escape(a1);
    }
    
    public static String escapeUriPathWithoutReserved(final String a1) {
        /*SL:136*/return CharEscapers.URI_RESERVED_ESCAPER.escape(a1);
    }
    
    public static String escapeUriUserInfo(final String a1) {
        /*SL:169*/return CharEscapers.URI_USERINFO_ESCAPER.escape(a1);
    }
    
    public static String escapeUriQuery(final String a1) {
        /*SL:213*/return CharEscapers.URI_QUERY_STRING_ESCAPER.escape(a1);
    }
    
    static {
        URI_ESCAPER = new PercentEscaper("-_.*", true);
        URI_PATH_ESCAPER = new PercentEscaper("-_.!~*'()@:$&,;=", false);
        URI_RESERVED_ESCAPER = new PercentEscaper("-_.!~*'()@:$&,;=+/?", false);
        URI_USERINFO_ESCAPER = new PercentEscaper("-_.!~*'():$&,;=", false);
        URI_QUERY_STRING_ESCAPER = new PercentEscaper("-_.!~*'()@:$,;/?:", false);
    }
}
