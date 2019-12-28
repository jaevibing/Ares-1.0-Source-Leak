package com.fasterxml.jackson.core.io;

import java.util.Arrays;

public final class CharTypes
{
    private static final char[] HC;
    private static final byte[] HB;
    private static final int[] sInputCodes;
    private static final int[] sInputCodesUTF8;
    private static final int[] sInputCodesJsNames;
    private static final int[] sInputCodesUtf8JsNames;
    private static final int[] sInputCodesComment;
    private static final int[] sInputCodesWS;
    private static final int[] sOutputEscapes128;
    private static final int[] sHexValues;
    
    public static int[] getInputCodeLatin1() {
        /*SL:195*/return CharTypes.sInputCodes;
    }
    
    public static int[] getInputCodeUtf8() {
        /*SL:196*/return CharTypes.sInputCodesUTF8;
    }
    
    public static int[] getInputCodeLatin1JsNames() {
        /*SL:198*/return CharTypes.sInputCodesJsNames;
    }
    
    public static int[] getInputCodeUtf8JsNames() {
        /*SL:199*/return CharTypes.sInputCodesUtf8JsNames;
    }
    
    public static int[] getInputCodeComment() {
        /*SL:201*/return CharTypes.sInputCodesComment;
    }
    
    public static int[] getInputCodeWS() {
        /*SL:202*/return CharTypes.sInputCodesWS;
    }
    
    public static int[] get7BitOutputEscapes() {
        /*SL:211*/return CharTypes.sOutputEscapes128;
    }
    
    public static int charToHex(final int a1) {
        /*SL:215*/return (a1 > 127) ? -1 : CharTypes.sHexValues[a1];
    }
    
    public static void appendQuoted(final StringBuilder v-6, final String v-5) {
        final int[] sOutputEscapes128 = CharTypes.sOutputEscapes128;
        final int length = /*EL:221*/sOutputEscapes128.length;
        /*SL:222*/for (int i = 0, length2 = v-5.length(); i < length2; ++i) {
            char a2 = /*EL:223*/v-5.charAt(i);
            /*SL:224*/if (a2 >= length || sOutputEscapes128[a2] == 0) {
                /*SL:225*/v-6.append(a2);
            }
            else {
                /*SL:228*/v-6.append('\\');
                final int v1 = /*EL:229*/sOutputEscapes128[a2];
                /*SL:230*/if (v1 < 0) {
                    /*SL:239*/v-6.append('u');
                    /*SL:240*/v-6.append('0');
                    /*SL:241*/v-6.append('0');
                    /*SL:242*/a2 = a2;
                    /*SL:243*/v-6.append(CharTypes.HC[a2 >> 4]);
                    /*SL:244*/v-6.append(CharTypes.HC[a2 & 0xF]);
                }
                else {
                    /*SL:246*/v-6.append((char)v1);
                }
            }
        }
    }
    
    public static char[] copyHexChars() {
        /*SL:252*/return CharTypes.HC.clone();
    }
    
    public static byte[] copyHexBytes() {
        /*SL:256*/return CharTypes.HB.clone();
    }
    
    static {
        HC = "0123456789ABCDEF".toCharArray();
        int v0 = CharTypes.HC.length;
        HB = new byte[v0];
        for (int v = 0; v < v0; ++v) {
            CharTypes.HB[v] = (byte)CharTypes.HC[v];
        }
        int[] v2 = new int[256];
        for (int v = 0; v < 32; ++v) {
            v2[v] = -1;
        }
        v2[92] = (v2[34] = 1);
        sInputCodes = v2;
        v2 = new int[CharTypes.sInputCodes.length];
        System.arraycopy(CharTypes.sInputCodes, 0, v2, 0, v2.length);
        for (int v = 128; v < 256; ++v) {
            int v3;
            if ((v & 0xE0) == 0xC0) {
                v3 = 2;
            }
            else if ((v & 0xF0) == 0xE0) {
                v3 = 3;
            }
            else if ((v & 0xF8) == 0xF0) {
                v3 = 4;
            }
            else {
                v3 = -1;
            }
            v2[v] = v3;
        }
        sInputCodesUTF8 = v2;
        v2 = new int[256];
        Arrays.fill(v2, -1);
        for (int v = 33; v < 256; ++v) {
            if (Character.isJavaIdentifierPart((char)v)) {
                v2[v] = 0;
            }
        }
        v2[64] = 0;
        v2[42] = (v2[35] = 0);
        v2[43] = (v2[45] = 0);
        sInputCodesJsNames = v2;
        v2 = new int[256];
        System.arraycopy(CharTypes.sInputCodesJsNames, 0, v2, 0, v2.length);
        Arrays.fill(v2, 128, 128, 0);
        sInputCodesUtf8JsNames = v2;
        v2 = new int[256];
        System.arraycopy(CharTypes.sInputCodesUTF8, 128, v2, 128, 128);
        Arrays.fill(v2, 0, 32, -1);
        v2[9] = 0;
        v2[10] = 10;
        v2[13] = 13;
        v2[42] = 42;
        sInputCodesComment = v2;
        v2 = new int[256];
        System.arraycopy(CharTypes.sInputCodesUTF8, 128, v2, 128, 128);
        Arrays.fill(v2, 0, 32, -1);
        v2[9] = (v2[32] = 1);
        v2[10] = 10;
        v2[13] = 13;
        v2[47] = 47;
        v2[35] = 35;
        sInputCodesWS = v2;
        v2 = new int[128];
        for (int v = 0; v < 32; ++v) {
            v2[v] = -1;
        }
        v2[34] = 34;
        v2[92] = 92;
        v2[8] = 98;
        v2[9] = 116;
        v2[12] = 102;
        v2[10] = 110;
        v2[13] = 114;
        sOutputEscapes128 = v2;
        Arrays.fill(sHexValues = new int[128], -1);
        for (v0 = 0; v0 < 10; ++v0) {
            CharTypes.sHexValues[48 + v0] = v0;
        }
        for (v0 = 0; v0 < 6; ++v0) {
            CharTypes.sHexValues[97 + v0] = 10 + v0;
            CharTypes.sHexValues[65 + v0] = 10 + v0;
        }
    }
}
