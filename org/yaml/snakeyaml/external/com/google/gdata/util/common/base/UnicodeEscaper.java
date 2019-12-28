package org.yaml.snakeyaml.external.com.google.gdata.util.common.base;

import java.io.IOException;

public abstract class UnicodeEscaper implements Escaper
{
    private static final int DEST_PAD = 32;
    private static final ThreadLocal<char[]> DEST_TL;
    
    protected abstract char[] escape(final int p0);
    
    protected int nextEscapeIndex(final CharSequence a3, final int v1, final int v2) {
        int v3;
        int a4;
        /*SL:123*/for (v3 = v1; v3 < v2; /*SL:128*/v3 += (Character.isSupplementaryCodePoint(a4) ? 2 : 1)) {
            a4 = codePointAt(a3, v3, v2);
            if (a4 < 0) {
                break;
            }
            if (this.escape(a4) != null) {
                break;
            }
        }
        /*SL:130*/return v3;
    }
    
    @Override
    public String escape(final String a1) {
        final int v1 = /*EL:161*/a1.length();
        final int v2 = /*EL:162*/this.nextEscapeIndex(a1, 0, v1);
        /*SL:163*/return (v2 == v1) ? a1 : this.escapeSlow(a1, v2);
    }
    
    protected final String escapeSlow(final String v-8, int v-7) {
        final int length = /*EL:188*/v-8.length();
        char[] array = UnicodeEscaper.DEST_TL.get();
        int n = /*EL:192*/0;
        int v2;
        /*SL:195*/for (v2 = 0; v-7 < length; /*SL:223*/v-7 = this.nextEscapeIndex(v-8, v2, length)) {
            final int codePoint = codePointAt(v-8, v-7, length);
            if (codePoint < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            final char[] escape = this.escape(codePoint);
            if (escape != null) {
                int a2 = v-7 - v2;
                final int v1 = n + a2 + escape.length;
                if (array.length < v1) {
                    a2 = v1 + (length - v-7) + 32;
                    array = growBuffer(array, n, a2);
                }
                if (a2 > 0) {
                    v-8.getChars(v2, v-7, array, n);
                    n += a2;
                }
                if (escape.length > 0) {
                    System.arraycopy(escape, 0, array, n, escape.length);
                    n += escape.length;
                }
            }
            v2 = v-7 + (Character.isSupplementaryCodePoint(codePoint) ? 2 : 1);
        }
        final int codePoint = /*EL:229*/length - v2;
        /*SL:230*/if (codePoint > 0) {
            final int a3 = /*EL:231*/n + codePoint;
            /*SL:232*/if (array.length < a3) {
                /*SL:233*/array = growBuffer(array, n, a3);
            }
            /*SL:235*/v-8.getChars(v2, length, array, n);
            /*SL:236*/n = a3;
        }
        /*SL:238*/return new String(array, 0, n);
    }
    
    @Override
    public Appendable escape(final Appendable a1) {
        /*SL:286*/assert a1 != null;
        /*SL:288*/return new Appendable() {
            int pendingHighSurrogate = -1;
            char[] decodedChars = new char[2];
            
            @Override
            public Appendable append(final CharSequence a1) throws IOException {
                /*SL:293*/return this.append(a1, 0, a1.length());
            }
            
            @Override
            public Appendable append(final CharSequence v-4, final int v-3, final int v-2) throws IOException {
                int nextEscapeIndex = /*EL:297*/v-3;
                /*SL:298*/if (nextEscapeIndex < v-2) {
                    int v0 = /*EL:305*/nextEscapeIndex;
                    /*SL:306*/if (this.pendingHighSurrogate != -1) {
                        final char a1 = /*EL:310*/v-4.charAt(nextEscapeIndex++);
                        /*SL:311*/if (!Character.isLowSurrogate(a1)) {
                            /*SL:312*/throw new IllegalArgumentException("Expected low surrogate character but got " + a1);
                        }
                        final char[] a2 = /*EL:315*/UnicodeEscaper.this.escape(Character.toCodePoint((char)this.pendingHighSurrogate, a1));
                        /*SL:317*/if (a2 != null) {
                            /*SL:321*/this.outputChars(a2, a2.length);
                            /*SL:322*/++v0;
                        }
                        else {
                            /*SL:328*/a1.append((char)this.pendingHighSurrogate);
                        }
                        /*SL:330*/this.pendingHighSurrogate = -1;
                    }
                    while (true) {
                        /*SL:335*/nextEscapeIndex = UnicodeEscaper.this.nextEscapeIndex(v-4, nextEscapeIndex, v-2);
                        /*SL:336*/if (nextEscapeIndex > v0) {
                            /*SL:337*/a1.append(v-4, v0, nextEscapeIndex);
                        }
                        /*SL:339*/if (nextEscapeIndex == v-2) {
                            /*SL:340*/break;
                        }
                        final int v = /*EL:344*/UnicodeEscaper.codePointAt(v-4, nextEscapeIndex, v-2);
                        /*SL:345*/if (v < 0) {
                            /*SL:349*/this.pendingHighSurrogate = -v;
                            /*SL:350*/break;
                        }
                        final char[] v2 = /*EL:353*/UnicodeEscaper.this.escape(v);
                        /*SL:354*/if (v2 != null) {
                            /*SL:355*/this.outputChars(v2, v2.length);
                        }
                        else {
                            final int a3 = /*EL:360*/Character.toChars(v, this.decodedChars, 0);
                            /*SL:361*/this.outputChars(this.decodedChars, a3);
                        }
                        /*SL:365*/nextEscapeIndex = /*EL:366*/(v0 = nextEscapeIndex + (Character.isSupplementaryCodePoint(v) ? 2 : 1));
                    }
                }
                /*SL:369*/return this;
            }
            
            @Override
            public Appendable append(final char v0) throws IOException {
                /*SL:373*/if (this.pendingHighSurrogate != -1) {
                    /*SL:377*/if (!Character.isLowSurrogate(v0)) {
                        /*SL:378*/throw new IllegalArgumentException("Expected low surrogate character but got '" + v0 + "' with value " + (int)v0);
                    }
                    final char[] a1 = /*EL:382*/UnicodeEscaper.this.escape(Character.toCodePoint((char)this.pendingHighSurrogate, v0));
                    /*SL:383*/if (a1 != null) {
                        /*SL:384*/this.outputChars(a1, a1.length);
                    }
                    else {
                        /*SL:386*/a1.append((char)this.pendingHighSurrogate);
                        /*SL:387*/a1.append(v0);
                    }
                    /*SL:389*/this.pendingHighSurrogate = -1;
                }
                else/*SL:390*/ if (Character.isHighSurrogate(v0)) {
                    /*SL:392*/this.pendingHighSurrogate = v0;
                }
                else {
                    /*SL:394*/if (Character.isLowSurrogate(v0)) {
                        /*SL:395*/throw new IllegalArgumentException("Unexpected low surrogate character '" + v0 + "' with value " + (int)v0);
                    }
                    final char[] v = /*EL:399*/UnicodeEscaper.this.escape(v0);
                    /*SL:400*/if (v != null) {
                        /*SL:401*/this.outputChars(v, v.length);
                    }
                    else {
                        /*SL:403*/a1.append(v0);
                    }
                }
                /*SL:406*/return this;
            }
            
            private void outputChars(final char[] v1, final int v2) throws IOException {
                /*SL:410*/for (int a1 = 0; a1 < v2; ++a1) {
                    /*SL:411*/a1.append(v1[a1]);
                }
            }
        };
    }
    
    protected static final int codePointAt(final CharSequence a3, int v1, final int v2) {
        /*SL:456*/if (v1 >= v2) {
            /*SL:479*/throw new IndexOutOfBoundsException("Index exceeds specified range");
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
    
    private static final char[] growBuffer(final char[] a1, final int a2, final int a3) {
        final char[] v1 = /*EL:488*/new char[a3];
        /*SL:489*/if (a2 > 0) {
            /*SL:490*/System.arraycopy(a1, 0, v1, 0, a2);
        }
        /*SL:492*/return v1;
    }
    
    static {
        DEST_TL = new ThreadLocal<char[]>() {
            @Override
            protected char[] initialValue() {
                /*SL:503*/return new char[1024];
            }
        };
    }
}
