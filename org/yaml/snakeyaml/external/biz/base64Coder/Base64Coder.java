package org.yaml.snakeyaml.external.biz.base64Coder;

public class Base64Coder
{
    private static final String systemLineSeparator;
    private static char[] map1;
    private static byte[] map2;
    
    public static String encodeString(final String a1) {
        /*SL:69*/return new String(encode(a1.getBytes()));
    }
    
    public static String encodeLines(final byte[] a1) {
        /*SL:82*/return encodeLines(a1, 0, a1.length, 76, Base64Coder.systemLineSeparator);
    }
    
    public static String encodeLines(final byte[] a2, final int a3, final int a4, final int a5, final String v1) {
        final int v2 = /*EL:104*/a5 * 3 / 4;
        /*SL:105*/if (v2 <= 0) {
            /*SL:106*/throw new IllegalArgumentException();
        }
        final int v3 = /*EL:107*/(a4 + v2 - 1) / v2;
        final int v4 = /*EL:108*/(a4 + 2) / 3 * 4 + v3 * v1.length();
        final StringBuilder v5 = /*EL:109*/new StringBuilder(v4);
        int a6;
        /*SL:111*/for (int v6 = 0; v6 < a4; /*SL:115*/v6 += a6) {
            a6 = Math.min(a4 - v6, v2);
            v5.append(encode(a2, a3 + v6, a6));
            v5.append(v1);
        }
        /*SL:117*/return v5.toString();
    }
    
    public static char[] encode(final byte[] a1) {
        /*SL:129*/return encode(a1, 0, a1.length);
    }
    
    public static char[] encode(final byte[] a1, final int a2) {
        /*SL:143*/return encode(a1, 0, a2);
    }
    
    public static char[] encode(final byte[] v-11, final int v-10, final int v-9) {
        final int n = /*EL:160*/(v-9 * 4 + 2) / 3;
        final int n2 = /*EL:161*/(v-9 + 2) / 3 * 4;
        final char[] array = /*EL:162*/new char[n2];
        int a1;
        int a2;
        int a3;
        int v1;
        int v2;
        int v3;
        int v4;
        /*SL:166*/for (int i = v-10, n3 = v-10 + v-9, n4 = 0; i < n3; /*SL:167*/a1 = (v-11[i++] & 0xFF), /*SL:168*/a2 = ((i < n3) ? (v-11[i++] & 0xFF) : 0), /*SL:169*/a3 = ((i < n3) ? (v-11[i++] & 0xFF) : 0), /*SL:170*/v1 = a1 >>> 2, /*SL:171*/v2 = ((a1 & 0x3) << 4 | a2 >>> 4), /*SL:172*/v3 = ((a2 & 0xF) << 2 | a3 >>> 6), /*SL:173*/v4 = (a3 & 0x3F), /*SL:174*/array[n4++] = Base64Coder.map1[v1], /*SL:175*/array[n4++] = Base64Coder.map1[v2], /*SL:176*/array[n4] = ((n4 < n) ? Base64Coder.map1[v3] : '='), /*SL:177*/++n4, /*SL:178*/array[n4] = ((n4 < n) ? Base64Coder.map1[v4] : '='), /*SL:179*/++n4) {}
        /*SL:181*/return array;
    }
    
    public static String decodeString(final String a1) {
        /*SL:195*/return new String(decode(a1));
    }
    
    public static byte[] decodeLines(final String v-2) {
        final char[] v-3 = /*EL:211*/new char[v-2.length()];
        int v0 = /*EL:212*/0;
        /*SL:213*/for (int v = 0; v < v-2.length(); ++v) {
            final char a1 = /*EL:214*/v-2.charAt(v);
            /*SL:215*/if (a1 != ' ' && a1 != '\r' && a1 != '\n' && a1 != '\t') {
                /*SL:216*/v-3[v0++] = a1;
            }
        }
        /*SL:218*/return decode(v-3, 0, v0);
    }
    
    public static byte[] decode(final String a1) {
        /*SL:232*/return decode(a1.toCharArray());
    }
    
    public static byte[] decode(final char[] a1) {
        /*SL:246*/return decode(a1, 0, a1.length);
    }
    
    public static byte[] decode(final char[] v-10, final int v-9, int v-8) {
        /*SL:266*/if (v-8 % 4 != 0) {
            /*SL:267*/throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        }
        /*SL:269*/while (v-8 > 0 && v-10[v-9 + v-8 - 1] == '=') {
            /*SL:270*/--v-8;
        }
        final int n = /*EL:271*/v-8 * 3 / 4;
        final byte[] array = /*EL:272*/new byte[n];
        int i = /*EL:273*/v-9;
        final int n2 = /*EL:274*/v-9 + v-8;
        int n3 = /*EL:275*/0;
        /*SL:276*/while (i < n2) {
            final int a1 = /*EL:277*/v-10[i++];
            final int a2 = /*EL:278*/v-10[i++];
            final int a3 = /*EL:279*/(i < n2) ? v-10[i++] : 'A';
            final int v1 = /*EL:280*/(i < n2) ? v-10[i++] : 'A';
            /*SL:281*/if (a1 > 127 || a2 > 127 || a3 > 127 || v1 > 127) {
                /*SL:282*/throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }
            final int v2 = /*EL:283*/Base64Coder.map2[a1];
            final int v3 = /*EL:284*/Base64Coder.map2[a2];
            final int v4 = /*EL:285*/Base64Coder.map2[a3];
            final int v5 = /*EL:286*/Base64Coder.map2[v1];
            /*SL:287*/if (v2 < 0 || v3 < 0 || v4 < 0 || v5 < 0) {
                /*SL:288*/throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            }
            final int v6 = /*EL:289*/v2 << 2 | v3 >>> 4;
            final int v7 = /*EL:290*/(v3 & 0xF) << 4 | v4 >>> 2;
            final int v8 = /*EL:291*/(v4 & 0x3) << 6 | v5;
            /*SL:292*/array[n3++] = (byte)v6;
            /*SL:293*/if (n3 < n) {
                /*SL:294*/array[n3++] = (byte)v7;
            }
            /*SL:295*/if (n3 >= n) {
                continue;
            }
            /*SL:296*/array[n3++] = (byte)v8;
        }
        /*SL:298*/return array;
    }
    
    static {
        systemLineSeparator = System.getProperty("line.separator");
        Base64Coder.map1 = new char[64];
        int v0 = 0;
        for (char v = 'A'; v <= 'Z'; ++v) {
            Base64Coder.map1[v0++] = v;
        }
        for (char v = 'a'; v <= 'z'; ++v) {
            Base64Coder.map1[v0++] = v;
        }
        for (char v = '0'; v <= '9'; ++v) {
            Base64Coder.map1[v0++] = v;
        }
        Base64Coder.map1[v0++] = '+';
        Base64Coder.map1[v0++] = '/';
        Base64Coder.map2 = new byte[128];
        for (v0 = 0; v0 < Base64Coder.map2.length; ++v0) {
            Base64Coder.map2[v0] = -1;
        }
        for (v0 = 0; v0 < 64; ++v0) {
            Base64Coder.map2[Base64Coder.map1[v0]] = (byte)v0;
        }
    }
}
