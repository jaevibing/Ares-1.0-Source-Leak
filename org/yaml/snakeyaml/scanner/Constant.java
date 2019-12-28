package org.yaml.snakeyaml.scanner;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public final class Constant
{
    private static final String ALPHA_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    private static final String LINEBR_S = "\n\u0085\u2028\u2029";
    private static final String FULL_LINEBR_S = "\r\n\u0085\u2028\u2029";
    private static final String NULL_OR_LINEBR_S = "\u0000\r\n\u0085\u2028\u2029";
    private static final String NULL_BL_LINEBR_S = " \u0000\r\n\u0085\u2028\u2029";
    private static final String NULL_BL_T_LINEBR_S = "\t \u0000\r\n\u0085\u2028\u2029";
    private static final String NULL_BL_T_S = "\u0000 \t";
    private static final String URI_CHARS_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%";
    public static final Constant LINEBR;
    public static final Constant FULL_LINEBR;
    public static final Constant NULL_OR_LINEBR;
    public static final Constant NULL_BL_LINEBR;
    public static final Constant NULL_BL_T_LINEBR;
    public static final Constant NULL_BL_T;
    public static final Constant URI_CHARS;
    public static final Constant ALPHA;
    private String content;
    boolean[] contains;
    boolean noASCII;
    
    private Constant(final String v-1) {
        this.contains = new boolean[128];
        this.noASCII = false;
        Arrays.fill(this.contains, false);
        final StringBuilder v0 = new StringBuilder();
        for (int v = 0; v < v-1.length(); ++v) {
            final int a1 = v-1.codePointAt(v);
            if (a1 < 128) {
                this.contains[a1] = true;
            }
            else {
                v0.appendCodePoint(a1);
            }
        }
        if (v0.length() > 0) {
            this.noASCII = true;
            this.content = v0.toString();
        }
    }
    
    public boolean has(final int a1) {
        /*SL:62*/return (a1 < 128) ? this.contains[a1] : (this.noASCII && this.content.indexOf(a1, 0) != -1);
    }
    
    public boolean hasNo(final int a1) {
        /*SL:66*/return !this.has(a1);
    }
    
    public boolean has(final int a1, final String a2) {
        /*SL:70*/return this.has(a1) || a2.indexOf(a1, 0) != -1;
    }
    
    public boolean hasNo(final int a1, final String a2) {
        /*SL:74*/return !this.has(a1, a2);
    }
    
    static {
        LINEBR = new Constant("\n\u0085\u2028\u2029");
        FULL_LINEBR = new Constant("\r\n\u0085\u2028\u2029");
        NULL_OR_LINEBR = new Constant("\u0000\r\n\u0085\u2028\u2029");
        NULL_BL_LINEBR = new Constant(" \u0000\r\n\u0085\u2028\u2029");
        NULL_BL_T_LINEBR = new Constant("\t \u0000\r\n\u0085\u2028\u2029");
        NULL_BL_T = new Constant("\u0000 \t");
        URI_CHARS = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%");
        ALPHA = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_");
    }
}
