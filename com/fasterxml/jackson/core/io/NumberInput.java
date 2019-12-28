package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

public final class NumberInput
{
    public static final String NASTY_SMALL_DOUBLE = "2.2250738585072012e-308";
    static final long L_BILLION = 1000000000L;
    static final String MIN_LONG_STR_NO_SIGN;
    static final String MAX_LONG_STR;
    
    public static int parseInt(final char[] a1, int a2, int a3) {
        int v1 = /*EL:30*/a1[a2] - '0';
        /*SL:32*/if (a3 > 4) {
            /*SL:33*/v1 = v1 * 10 + (a1[++a2] - '0');
            /*SL:34*/v1 = v1 * 10 + (a1[++a2] - '0');
            /*SL:35*/v1 = v1 * 10 + (a1[++a2] - '0');
            /*SL:36*/v1 = v1 * 10 + (a1[++a2] - '0');
            /*SL:37*/a3 -= 4;
            /*SL:38*/if (a3 > 4) {
                /*SL:39*/v1 = v1 * 10 + (a1[++a2] - '0');
                /*SL:40*/v1 = v1 * 10 + (a1[++a2] - '0');
                /*SL:41*/v1 = v1 * 10 + (a1[++a2] - '0');
                /*SL:42*/v1 = v1 * 10 + (a1[++a2] - '0');
                /*SL:43*/return v1;
            }
        }
        /*SL:46*/if (a3 > 1) {
            /*SL:47*/v1 = v1 * 10 + (a1[++a2] - '0');
            /*SL:48*/if (a3 > 2) {
                /*SL:49*/v1 = v1 * 10 + (a1[++a2] - '0');
                /*SL:50*/if (a3 > 3) {
                    /*SL:51*/v1 = v1 * 10 + (a1[++a2] - '0');
                }
            }
        }
        /*SL:55*/return v1;
    }
    
    public static int parseInt(final String a1) {
        char v1 = /*EL:68*/a1.charAt(0);
        final int v2 = /*EL:69*/a1.length();
        final boolean v3 = /*EL:70*/v1 == '-';
        int v4 = /*EL:71*/1;
        /*SL:74*/if (v3) {
            /*SL:75*/if (v2 == 1 || v2 > 10) {
                /*SL:76*/return Integer.parseInt(a1);
            }
            /*SL:78*/v1 = a1.charAt(v4++);
        }
        else/*SL:80*/ if (v2 > 9) {
            /*SL:81*/return Integer.parseInt(a1);
        }
        /*SL:84*/if (v1 > '9' || v1 < '0') {
            /*SL:85*/return Integer.parseInt(a1);
        }
        int v5 = /*EL:87*/v1 - '0';
        /*SL:88*/if (v4 < v2) {
            /*SL:89*/v1 = a1.charAt(v4++);
            /*SL:90*/if (v1 > '9' || v1 < '0') {
                /*SL:91*/return Integer.parseInt(a1);
            }
            /*SL:93*/v5 = v5 * 10 + (v1 - '0');
            /*SL:94*/if (v4 < v2) {
                /*SL:95*/v1 = a1.charAt(v4++);
                /*SL:96*/if (v1 > '9' || v1 < '0') {
                    /*SL:97*/return Integer.parseInt(a1);
                }
                /*SL:99*/v5 = v5 * 10 + (v1 - '0');
                /*SL:101*/if (v4 < v2) {
                    /*SL:108*/do {
                        v1 = a1.charAt(v4++);
                        if (v1 > '9' || v1 < '0') {
                            return Integer.parseInt(a1);
                        }
                        v5 = v5 * 10 + (v1 - '0');
                    } while (v4 < v2);
                }
            }
        }
        /*SL:112*/return v3 ? (-v5) : v5;
    }
    
    public static long parseLong(final char[] a1, final int a2, final int a3) {
        final int v1 = /*EL:118*/a3 - 9;
        final long v2 = parseInt(/*EL:119*/a1, a2, v1) * 1000000000L;
        /*SL:120*/return v2 + parseInt(a1, a2 + v1, 9);
    }
    
    public static long parseLong(final String a1) {
        final int v1 = /*EL:128*/a1.length();
        /*SL:129*/if (v1 <= 9) {
            /*SL:130*/return parseInt(a1);
        }
        /*SL:133*/return Long.parseLong(a1);
    }
    
    public static boolean inLongRange(final char[] a3, final int a4, final int v1, final boolean v2) {
        final String v3 = /*EL:148*/v2 ? NumberInput.MIN_LONG_STR_NO_SIGN : NumberInput.MAX_LONG_STR;
        final int v4 = /*EL:149*/v3.length();
        /*SL:150*/if (v1 < v4) {
            return true;
        }
        /*SL:151*/if (v1 > v4) {
            return false;
        }
        /*SL:153*/for (int a5 = 0; a5 < v4; ++a5) {
            final int a6 = /*EL:154*/a3[a4 + a5] - v3.charAt(a5);
            /*SL:155*/if (a6 != 0) {
                /*SL:156*/return a6 < 0;
            }
        }
        /*SL:159*/return true;
    }
    
    public static boolean inLongRange(final String v1, final boolean v2) {
        final String v3 = /*EL:171*/v2 ? NumberInput.MIN_LONG_STR_NO_SIGN : NumberInput.MAX_LONG_STR;
        final int v4 = /*EL:172*/v3.length();
        final int v5 = /*EL:173*/v1.length();
        /*SL:174*/if (v5 < v4) {
            return true;
        }
        /*SL:175*/if (v5 > v4) {
            return false;
        }
        /*SL:178*/for (int a2 = 0; a2 < v4; ++a2) {
            /*SL:179*/a2 = v1.charAt(a2) - v3.charAt(a2);
            /*SL:180*/if (a2 != 0) {
                /*SL:181*/return a2 < 0;
            }
        }
        /*SL:184*/return true;
    }
    
    public static int parseAsInt(String v-3, final int v-2) {
        /*SL:189*/if (v-3 == null) {
            /*SL:190*/return v-2;
        }
        /*SL:192*/v-3 = v-3.trim();
        int n = /*EL:193*/v-3.length();
        /*SL:194*/if (n == 0) {
            /*SL:195*/return v-2;
        }
        int v0 = /*EL:198*/0;
        /*SL:199*/if (v0 < n) {
            final char a1 = /*EL:200*/v-3.charAt(0);
            /*SL:201*/if (a1 == '+') {
                /*SL:202*/v-3 = v-3.substring(1);
                /*SL:203*/n = v-3.length();
            }
            else/*SL:204*/ if (a1 == '-') {
                /*SL:205*/++v0;
            }
        }
        /*SL:208*/while (v0 < n) {
            final char v = /*EL:209*/v-3.charAt(v0);
            Label_0103: {
                /*SL:211*/if (v <= '9') {
                    if (v >= '0') {
                        break Label_0103;
                    }
                }
                try {
                    /*SL:213*/return (int)parseDouble(v-3);
                }
                catch (NumberFormatException a2) {
                    /*SL:215*/return v-2;
                }
            }
            ++v0;
        }
        try {
            /*SL:220*/return Integer.parseInt(v-3);
        }
        catch (NumberFormatException v2) {
            /*SL:222*/return v-2;
        }
    }
    
    public static long parseAsLong(String v-4, final long v-3) {
        /*SL:227*/if (v-4 == null) {
            /*SL:228*/return v-3;
        }
        /*SL:230*/v-4 = v-4.trim();
        int n = /*EL:231*/v-4.length();
        /*SL:232*/if (n == 0) {
            /*SL:233*/return v-3;
        }
        int v0 = /*EL:236*/0;
        /*SL:237*/if (v0 < n) {
            final char a1 = /*EL:238*/v-4.charAt(0);
            /*SL:239*/if (a1 == '+') {
                /*SL:240*/v-4 = v-4.substring(1);
                /*SL:241*/n = v-4.length();
            }
            else/*SL:242*/ if (a1 == '-') {
                /*SL:243*/++v0;
            }
        }
        /*SL:246*/while (v0 < n) {
            final char v = /*EL:247*/v-4.charAt(v0);
            Label_0107: {
                /*SL:249*/if (v <= '9') {
                    if (v >= '0') {
                        break Label_0107;
                    }
                }
                try {
                    /*SL:251*/return (long)parseDouble(v-4);
                }
                catch (NumberFormatException a2) {
                    /*SL:253*/return v-3;
                }
            }
            ++v0;
        }
        try {
            /*SL:258*/return Long.parseLong(v-4);
        }
        catch (NumberFormatException v2) {
            /*SL:260*/return v-3;
        }
    }
    
    public static double parseAsDouble(String a2, final double v1) {
        /*SL:265*/if (a2 == null) {
            return v1;
        }
        /*SL:266*/a2 = a2.trim();
        final int v2 = /*EL:267*/a2.length();
        /*SL:268*/if (v2 == 0) {
            /*SL:269*/return v1;
        }
        try {
            /*SL:272*/return parseDouble(a2);
        }
        catch (NumberFormatException a3) {
            /*SL:274*/return v1;
        }
    }
    
    public static double parseDouble(final String a1) throws NumberFormatException {
        /*SL:282*/if ("2.2250738585072012e-308".equals(a1)) {
            /*SL:283*/return Double.MIN_VALUE;
        }
        /*SL:285*/return Double.parseDouble(a1);
    }
    
    public static BigDecimal parseBigDecimal(final String v1) throws NumberFormatException {
        try {
            /*SL:289*/return new BigDecimal(v1);
        }
        catch (NumberFormatException a1) {
            throw _badBD(/*EL:290*/v1);
        }
    }
    
    public static BigDecimal parseBigDecimal(final char[] a1) throws NumberFormatException {
        /*SL:295*/return parseBigDecimal(a1, 0, a1.length);
    }
    
    public static BigDecimal parseBigDecimal(final char[] a2, final int a3, final int v1) throws NumberFormatException {
        try {
            /*SL:299*/return new BigDecimal(a2, a3, v1);
        }
        catch (NumberFormatException a4) {
            throw _badBD(/*EL:300*/new String(a2, a3, v1));
        }
    }
    
    private static NumberFormatException _badBD(final String a1) {
        /*SL:305*/return new NumberFormatException("Value \"" + a1 + "\" can not be represented as BigDecimal");
    }
    
    static {
        MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
        MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);
    }
}
