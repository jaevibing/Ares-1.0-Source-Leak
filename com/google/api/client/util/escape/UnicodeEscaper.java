package com.google.api.client.util.escape;

public abstract class UnicodeEscaper extends Escaper
{
    private static final int DEST_PAD = 32;
    
    protected abstract char[] escape(final int p0);
    
    protected abstract int nextEscapeIndex(final CharSequence p0, final int p1, final int p2);
    
    @Override
    public abstract String escape(final String p0);
    
    protected final String escapeSlow(final String v-9, int v-8) {
        final int length = /*EL:132*/v-9.length();
        char[] array = /*EL:135*/Platform.charBufferFromThreadLocal();
        int n = /*EL:136*/0;
        int n2 = /*EL:137*/0;
        /*SL:139*/while (v-8 < length) {
            final int codePoint = codePointAt(/*EL:140*/v-9, v-8, length);
            /*SL:141*/if (codePoint < 0) {
                /*SL:142*/throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            final char[] escape = /*EL:147*/this.escape(codePoint);
            final int n3 = /*EL:148*/v-8 + (Character.isSupplementaryCodePoint(codePoint) ? 2 : 1);
            /*SL:149*/if (escape != null) {
                int a2 = /*EL:150*/v-8 - n2;
                final int v1 = /*EL:154*/n + a2 + escape.length;
                /*SL:155*/if (array.length < v1) {
                    /*SL:156*/a2 = v1 + length - v-8 + 32;
                    /*SL:157*/array = growBuffer(array, n, a2);
                }
                /*SL:160*/if (a2 > 0) {
                    /*SL:161*/v-9.getChars(n2, v-8, array, n);
                    /*SL:162*/n += a2;
                }
                /*SL:164*/if (escape.length > 0) {
                    /*SL:165*/System.arraycopy(escape, 0, array, n, escape.length);
                    /*SL:166*/n += escape.length;
                }
                /*SL:169*/n2 = n3;
            }
            /*SL:171*/v-8 = this.nextEscapeIndex(v-9, n3, length);
        }
        final int codePoint = /*EL:176*/length - n2;
        /*SL:177*/if (codePoint > 0) {
            final int a3 = /*EL:178*/n + codePoint;
            /*SL:179*/if (array.length < a3) {
                /*SL:180*/array = growBuffer(array, n, a3);
            }
            /*SL:182*/v-9.getChars(n2, length, array, n);
            /*SL:183*/n = a3;
        }
        /*SL:185*/return new String(array, 0, n);
    }
    
    protected static int codePointAt(final CharSequence a3, int v1, final int v2) {
        /*SL:221*/if (v1 >= v2) {
            /*SL:245*/throw new IndexOutOfBoundsException("Index exceeds specified range");
        }
        final char a4 = a3.charAt(v1++);
        if (a4 < '\ud800' || a4 > '\udfff') {
            return a4;
        }
        if (a4 > '\udbff') {
            throw new IllegalArgumentException("Unexpected low surrogate character '" + a4 + "' with value " + (int)a4 + " at index " + (v1 - 1));
        }
        if (v1 == v2) {
            return -a4;
        }
        final char a5 = a3.charAt(v1);
        if (Character.isLowSurrogate(a5)) {
            return Character.toCodePoint(a4, a5);
        }
        throw new IllegalArgumentException("Expected low surrogate but got char '" + a5 + "' with value " + (int)a5 + " at index " + v1);
    }
    
    private static char[] growBuffer(final char[] a1, final int a2, final int a3) {
        final char[] v1 = /*EL:253*/new char[a3];
        /*SL:254*/if (a2 > 0) {
            /*SL:255*/System.arraycopy(a1, 0, v1, 0, a2);
        }
        /*SL:257*/return v1;
    }
}
