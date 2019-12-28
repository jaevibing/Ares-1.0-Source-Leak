package com.fasterxml.jackson.core.io;

public final class NumberOutput
{
    private static int MILLION;
    private static int BILLION;
    private static long BILLION_L;
    private static long MIN_INT_AS_LONG;
    private static long MAX_INT_AS_LONG;
    static final String SMALLEST_INT;
    static final String SMALLEST_LONG;
    private static final int[] TRIPLET_TO_CHARS;
    private static final String[] sSmallIntStrs;
    private static final String[] sSmallIntStrs2;
    
    public static int outputInt(int a2, final char[] a3, int v1) {
        /*SL:58*/if (a2 < 0) {
            /*SL:59*/if (a2 == Integer.MIN_VALUE) {
                /*SL:62*/return _outputSmallestI(a3, v1);
            }
            /*SL:64*/a3[v1++] = '-';
            /*SL:65*/a2 = -a2;
        }
        /*SL:68*/if (a2 < NumberOutput.MILLION) {
            /*SL:69*/if (a2 >= 1000) {
                final int a4 = /*EL:76*/a2 / 1000;
                /*SL:77*/a2 -= a4 * 1000;
                /*SL:78*/v1 = _leading3(a4, a3, v1);
                /*SL:79*/v1 = _full3(a2, a3, v1);
                /*SL:80*/return v1;
            }
            if (a2 < 10) {
                a3[v1] = (char)(48 + a2);
                return v1 + 1;
            }
            return _leading3(a2, a3, v1);
        }
        else {
            /*SL:88*/if (a2 >= NumberOutput.BILLION) {
                /*SL:89*/a2 -= NumberOutput.BILLION;
                /*SL:90*/if (a2 >= NumberOutput.BILLION) {
                    /*SL:91*/a2 -= NumberOutput.BILLION;
                    /*SL:92*/a3[v1++] = '2';
                }
                else {
                    /*SL:94*/a3[v1++] = '1';
                }
                /*SL:96*/return _outputFullBillion(a2, a3, v1);
            }
            int v2 = /*EL:98*/a2 / 1000;
            final int v3 = /*EL:99*/a2 - v2 * 1000;
            /*SL:100*/a2 = v2;
            /*SL:101*/v2 /= 1000;
            final int v4 = /*EL:102*/a2 - v2 * 1000;
            /*SL:104*/v1 = _leading3(v2, a3, v1);
            /*SL:105*/v1 = _full3(v4, a3, v1);
            /*SL:106*/return _full3(v3, a3, v1);
        }
    }
    
    public static int outputInt(int a2, final byte[] a3, int v1) {
        /*SL:111*/if (a2 < 0) {
            /*SL:112*/if (a2 == Integer.MIN_VALUE) {
                /*SL:113*/return _outputSmallestI(a3, v1);
            }
            /*SL:115*/a3[v1++] = 45;
            /*SL:116*/a2 = -a2;
        }
        /*SL:119*/if (a2 < NumberOutput.MILLION) {
            /*SL:120*/if (a2 < 1000) {
                /*SL:121*/if (a2 < 10) {
                    /*SL:122*/a3[v1++] = (byte)(48 + a2);
                }
                else {
                    /*SL:124*/v1 = _leading3(a2, a3, v1);
                }
            }
            else {
                final int a4 = /*EL:127*/a2 / 1000;
                /*SL:128*/a2 -= a4 * 1000;
                /*SL:129*/v1 = _leading3(a4, a3, v1);
                /*SL:130*/v1 = _full3(a2, a3, v1);
            }
            /*SL:132*/return v1;
        }
        /*SL:134*/if (a2 >= NumberOutput.BILLION) {
            /*SL:135*/a2 -= NumberOutput.BILLION;
            /*SL:136*/if (a2 >= NumberOutput.BILLION) {
                /*SL:137*/a2 -= NumberOutput.BILLION;
                /*SL:138*/a3[v1++] = 50;
            }
            else {
                /*SL:140*/a3[v1++] = 49;
            }
            /*SL:142*/return _outputFullBillion(a2, a3, v1);
        }
        int v2 = /*EL:144*/a2 / 1000;
        final int v3 = /*EL:145*/a2 - v2 * 1000;
        /*SL:146*/a2 = v2;
        /*SL:147*/v2 /= 1000;
        final int v4 = /*EL:148*/a2 - v2 * 1000;
        /*SL:149*/v1 = _leading3(v2, a3, v1);
        /*SL:150*/v1 = _full3(v4, a3, v1);
        /*SL:151*/return _full3(v3, a3, v1);
    }
    
    public static int outputLong(long a2, final char[] a3, int v1) {
        /*SL:160*/if (a2 < 0L) {
            /*SL:161*/if (a2 > NumberOutput.MIN_INT_AS_LONG) {
                /*SL:162*/return outputInt((int)a2, a3, v1);
            }
            /*SL:164*/if (a2 == Long.MIN_VALUE) {
                /*SL:165*/return _outputSmallestL(a3, v1);
            }
            /*SL:167*/a3[v1++] = '-';
            /*SL:168*/a2 = -a2;
        }
        else/*SL:170*/ if (a2 <= NumberOutput.MAX_INT_AS_LONG) {
            /*SL:171*/return outputInt((int)a2, a3, v1);
        }
        long v2 = /*EL:176*/a2 / NumberOutput.BILLION_L;
        /*SL:177*/a2 -= v2 * NumberOutput.BILLION_L;
        /*SL:180*/if (v2 < NumberOutput.BILLION_L) {
            /*SL:181*/v1 = _outputUptoBillion((int)v2, a3, v1);
        }
        else {
            final long a4 = /*EL:184*/v2 / NumberOutput.BILLION_L;
            /*SL:185*/v2 -= a4 * NumberOutput.BILLION_L;
            /*SL:186*/v1 = _leading3((int)a4, a3, v1);
            /*SL:187*/v1 = _outputFullBillion((int)v2, a3, v1);
        }
        /*SL:189*/return _outputFullBillion((int)a2, a3, v1);
    }
    
    public static int outputLong(long a2, final byte[] a3, int v1) {
        /*SL:194*/if (a2 < 0L) {
            /*SL:195*/if (a2 > NumberOutput.MIN_INT_AS_LONG) {
                /*SL:196*/return outputInt((int)a2, a3, v1);
            }
            /*SL:198*/if (a2 == Long.MIN_VALUE) {
                /*SL:199*/return _outputSmallestL(a3, v1);
            }
            /*SL:201*/a3[v1++] = 45;
            /*SL:202*/a2 = -a2;
        }
        else/*SL:204*/ if (a2 <= NumberOutput.MAX_INT_AS_LONG) {
            /*SL:205*/return outputInt((int)a2, a3, v1);
        }
        long v2 = /*EL:210*/a2 / NumberOutput.BILLION_L;
        /*SL:211*/a2 -= v2 * NumberOutput.BILLION_L;
        /*SL:214*/if (v2 < NumberOutput.BILLION_L) {
            /*SL:215*/v1 = _outputUptoBillion((int)v2, a3, v1);
        }
        else {
            final long a4 = /*EL:218*/v2 / NumberOutput.BILLION_L;
            /*SL:219*/v2 -= a4 * NumberOutput.BILLION_L;
            /*SL:220*/v1 = _leading3((int)a4, a3, v1);
            /*SL:221*/v1 = _outputFullBillion((int)v2, a3, v1);
        }
        /*SL:223*/return _outputFullBillion((int)a2, a3, v1);
    }
    
    public static String toString(final int v1) {
        /*SL:238*/if (v1 < NumberOutput.sSmallIntStrs.length) {
            /*SL:239*/if (v1 >= 0) {
                /*SL:240*/return NumberOutput.sSmallIntStrs[v1];
            }
            final int a1 = /*EL:242*/-v1 - 1;
            /*SL:243*/if (a1 < NumberOutput.sSmallIntStrs2.length) {
                /*SL:244*/return NumberOutput.sSmallIntStrs2[a1];
            }
        }
        /*SL:247*/return Integer.toString(v1);
    }
    
    public static String toString(final long a1) {
        /*SL:251*/if (a1 <= 2147483647L && a1 >= -2147483648L) {
            /*SL:252*/return toString((int)a1);
        }
        /*SL:254*/return Long.toString(a1);
    }
    
    public static String toString(final double a1) {
        /*SL:258*/return Double.toString(a1);
    }
    
    public static String toString(final float a1) {
        /*SL:265*/return Float.toString(a1);
    }
    
    private static int _outputUptoBillion(final int a3, final char[] v1, int v2) {
        /*SL:276*/if (a3 >= NumberOutput.MILLION) {
            int v3 = /*EL:284*/a3 / 1000;
            final int v4 = /*EL:285*/a3 - v3 * 1000;
            final int v5 = /*EL:286*/v3 / 1000;
            /*SL:287*/v3 -= v5 * 1000;
            /*SL:289*/v2 = _leading3(v5, v1, v2);
            int v6 = /*EL:291*/NumberOutput.TRIPLET_TO_CHARS[v3];
            /*SL:292*/v1[v2++] = (char)(v6 >> 16);
            /*SL:293*/v1[v2++] = (char)(v6 >> 8 & 0x7F);
            /*SL:294*/v1[v2++] = (char)(v6 & 0x7F);
            /*SL:296*/v6 = NumberOutput.TRIPLET_TO_CHARS[v4];
            /*SL:297*/v1[v2++] = (char)(v6 >> 16);
            /*SL:298*/v1[v2++] = (char)(v6 >> 8 & 0x7F);
            /*SL:299*/v1[v2++] = (char)(v6 & 0x7F);
            /*SL:301*/return v2;
        }
        if (a3 < 1000) {
            return _leading3(a3, v1, v2);
        }
        final int a4 = a3 / 1000;
        final int a5 = a3 - a4 * 1000;
        return _outputUptoMillion(v1, v2, a4, a5);
    }
    
    private static int _outputFullBillion(final int a1, final char[] a2, int a3) {
        int v1 = /*EL:306*/a1 / 1000;
        final int v2 = /*EL:307*/a1 - v1 * 1000;
        final int v3 = /*EL:308*/v1 / 1000;
        int v4 = /*EL:310*/NumberOutput.TRIPLET_TO_CHARS[v3];
        /*SL:311*/a2[a3++] = (char)(v4 >> 16);
        /*SL:312*/a2[a3++] = (char)(v4 >> 8 & 0x7F);
        /*SL:313*/a2[a3++] = (char)(v4 & 0x7F);
        /*SL:315*/v1 -= v3 * 1000;
        /*SL:316*/v4 = NumberOutput.TRIPLET_TO_CHARS[v1];
        /*SL:317*/a2[a3++] = (char)(v4 >> 16);
        /*SL:318*/a2[a3++] = (char)(v4 >> 8 & 0x7F);
        /*SL:319*/a2[a3++] = (char)(v4 & 0x7F);
        /*SL:321*/v4 = NumberOutput.TRIPLET_TO_CHARS[v2];
        /*SL:322*/a2[a3++] = (char)(v4 >> 16);
        /*SL:323*/a2[a3++] = (char)(v4 >> 8 & 0x7F);
        /*SL:324*/a2[a3++] = (char)(v4 & 0x7F);
        /*SL:326*/return a3;
    }
    
    private static int _outputUptoBillion(final int a3, final byte[] v1, int v2) {
        /*SL:331*/if (a3 >= NumberOutput.MILLION) {
            int v3 = /*EL:339*/a3 / 1000;
            final int v4 = /*EL:340*/a3 - v3 * 1000;
            final int v5 = /*EL:341*/v3 / 1000;
            /*SL:342*/v3 -= v5 * 1000;
            /*SL:344*/v2 = _leading3(v5, v1, v2);
            int v6 = /*EL:346*/NumberOutput.TRIPLET_TO_CHARS[v3];
            /*SL:347*/v1[v2++] = (byte)(v6 >> 16);
            /*SL:348*/v1[v2++] = (byte)(v6 >> 8);
            /*SL:349*/v1[v2++] = (byte)v6;
            /*SL:351*/v6 = NumberOutput.TRIPLET_TO_CHARS[v4];
            /*SL:352*/v1[v2++] = (byte)(v6 >> 16);
            /*SL:353*/v1[v2++] = (byte)(v6 >> 8);
            /*SL:354*/v1[v2++] = (byte)v6;
            /*SL:356*/return v2;
        }
        if (a3 < 1000) {
            return _leading3(a3, v1, v2);
        }
        final int a4 = a3 / 1000;
        final int a5 = a3 - a4 * 1000;
        return _outputUptoMillion(v1, v2, a4, a5);
    }
    
    private static int _outputFullBillion(final int a1, final byte[] a2, int a3) {
        int v1 = /*EL:361*/a1 / 1000;
        final int v2 = /*EL:362*/a1 - v1 * 1000;
        final int v3 = /*EL:363*/v1 / 1000;
        /*SL:364*/v1 -= v3 * 1000;
        int v4 = /*EL:366*/NumberOutput.TRIPLET_TO_CHARS[v3];
        /*SL:367*/a2[a3++] = (byte)(v4 >> 16);
        /*SL:368*/a2[a3++] = (byte)(v4 >> 8);
        /*SL:369*/a2[a3++] = (byte)v4;
        /*SL:371*/v4 = NumberOutput.TRIPLET_TO_CHARS[v1];
        /*SL:372*/a2[a3++] = (byte)(v4 >> 16);
        /*SL:373*/a2[a3++] = (byte)(v4 >> 8);
        /*SL:374*/a2[a3++] = (byte)v4;
        /*SL:376*/v4 = NumberOutput.TRIPLET_TO_CHARS[v2];
        /*SL:377*/a2[a3++] = (byte)(v4 >> 16);
        /*SL:378*/a2[a3++] = (byte)(v4 >> 8);
        /*SL:379*/a2[a3++] = (byte)v4;
        /*SL:381*/return a3;
    }
    
    private static int _outputUptoMillion(final char[] a1, int a2, final int a3, final int a4) {
        int v1 = /*EL:386*/NumberOutput.TRIPLET_TO_CHARS[a3];
        /*SL:387*/if (a3 > 9) {
            /*SL:388*/if (a3 > 99) {
                /*SL:389*/a1[a2++] = (char)(v1 >> 16);
            }
            /*SL:391*/a1[a2++] = (char)(v1 >> 8 & 0x7F);
        }
        /*SL:393*/a1[a2++] = (char)(v1 & 0x7F);
        /*SL:395*/v1 = NumberOutput.TRIPLET_TO_CHARS[a4];
        /*SL:396*/a1[a2++] = (char)(v1 >> 16);
        /*SL:397*/a1[a2++] = (char)(v1 >> 8 & 0x7F);
        /*SL:398*/a1[a2++] = (char)(v1 & 0x7F);
        /*SL:399*/return a2;
    }
    
    private static int _outputUptoMillion(final byte[] a1, int a2, final int a3, final int a4) {
        int v1 = /*EL:404*/NumberOutput.TRIPLET_TO_CHARS[a3];
        /*SL:405*/if (a3 > 9) {
            /*SL:406*/if (a3 > 99) {
                /*SL:407*/a1[a2++] = (byte)(v1 >> 16);
            }
            /*SL:409*/a1[a2++] = (byte)(v1 >> 8);
        }
        /*SL:411*/a1[a2++] = (byte)v1;
        /*SL:413*/v1 = NumberOutput.TRIPLET_TO_CHARS[a4];
        /*SL:414*/a1[a2++] = (byte)(v1 >> 16);
        /*SL:415*/a1[a2++] = (byte)(v1 >> 8);
        /*SL:416*/a1[a2++] = (byte)v1;
        /*SL:417*/return a2;
    }
    
    private static int _leading3(final int a1, final char[] a2, int a3) {
        final int v1 = /*EL:422*/NumberOutput.TRIPLET_TO_CHARS[a1];
        /*SL:423*/if (a1 > 9) {
            /*SL:424*/if (a1 > 99) {
                /*SL:425*/a2[a3++] = (char)(v1 >> 16);
            }
            /*SL:427*/a2[a3++] = (char)(v1 >> 8 & 0x7F);
        }
        /*SL:429*/a2[a3++] = (char)(v1 & 0x7F);
        /*SL:430*/return a3;
    }
    
    private static int _leading3(final int a1, final byte[] a2, int a3) {
        final int v1 = /*EL:435*/NumberOutput.TRIPLET_TO_CHARS[a1];
        /*SL:436*/if (a1 > 9) {
            /*SL:437*/if (a1 > 99) {
                /*SL:438*/a2[a3++] = (byte)(v1 >> 16);
            }
            /*SL:440*/a2[a3++] = (byte)(v1 >> 8);
        }
        /*SL:442*/a2[a3++] = (byte)v1;
        /*SL:443*/return a3;
    }
    
    private static int _full3(final int a1, final char[] a2, int a3) {
        final int v1 = /*EL:448*/NumberOutput.TRIPLET_TO_CHARS[a1];
        /*SL:449*/a2[a3++] = (char)(v1 >> 16);
        /*SL:450*/a2[a3++] = (char)(v1 >> 8 & 0x7F);
        /*SL:451*/a2[a3++] = (char)(v1 & 0x7F);
        /*SL:452*/return a3;
    }
    
    private static int _full3(final int a1, final byte[] a2, int a3) {
        final int v1 = /*EL:457*/NumberOutput.TRIPLET_TO_CHARS[a1];
        /*SL:458*/a2[a3++] = (byte)(v1 >> 16);
        /*SL:459*/a2[a3++] = (byte)(v1 >> 8);
        /*SL:460*/a2[a3++] = (byte)v1;
        /*SL:461*/return a3;
    }
    
    private static int _outputSmallestL(final char[] a1, final int a2) {
        final int v1 = NumberOutput.SMALLEST_LONG.length();
        NumberOutput.SMALLEST_LONG.getChars(/*EL:469*/0, v1, a1, a2);
        /*SL:470*/return a2 + v1;
    }
    
    private static int _outputSmallestL(final byte[] a2, int v1) {
        /*SL:476*/for (int v2 = NumberOutput.SMALLEST_LONG.length(), a3 = 0; a3 < v2; ++a3) {
            /*SL:477*/a2[v1++] = (byte)NumberOutput.SMALLEST_LONG.charAt(a3);
        }
        /*SL:479*/return v1;
    }
    
    private static int _outputSmallestI(final char[] a1, final int a2) {
        final int v1 = NumberOutput.SMALLEST_INT.length();
        NumberOutput.SMALLEST_INT.getChars(/*EL:485*/0, v1, a1, a2);
        /*SL:486*/return a2 + v1;
    }
    
    private static int _outputSmallestI(final byte[] a2, int v1) {
        /*SL:492*/for (int v2 = NumberOutput.SMALLEST_INT.length(), a3 = 0; a3 < v2; ++a3) {
            /*SL:493*/a2[v1++] = (byte)NumberOutput.SMALLEST_INT.charAt(a3);
        }
        /*SL:495*/return v1;
    }
    
    static {
        NumberOutput.MILLION = 1000000;
        NumberOutput.BILLION = 1000000000;
        NumberOutput.BILLION_L = 1000000000L;
        NumberOutput.MIN_INT_AS_LONG = -2147483648L;
        NumberOutput.MAX_INT_AS_LONG = 2147483647L;
        SMALLEST_INT = String.valueOf(Integer.MIN_VALUE);
        SMALLEST_LONG = String.valueOf(Long.MIN_VALUE);
        TRIPLET_TO_CHARS = new int[1000];
        int n = 0;
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                for (int v0 = 0; v0 < 10; ++v0) {
                    final int v = i + 48 << 16 | j + 48 << 8 | v0 + 48;
                    NumberOutput.TRIPLET_TO_CHARS[n++] = v;
                }
            }
        }
        sSmallIntStrs = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        sSmallIntStrs2 = new String[] { "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10" };
    }
}
