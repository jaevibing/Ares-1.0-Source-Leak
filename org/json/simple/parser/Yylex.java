package org.json.simple.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.Reader;

class Yylex
{
    public static final int YYEOF = -1;
    private static final int ZZ_BUFFERSIZE = 16384;
    public static final int YYINITIAL = 0;
    public static final int STRING_BEGIN = 2;
    private static final int[] ZZ_LEXSTATE;
    private static final String ZZ_CMAP_PACKED = "\t\u0000\u0001\u0007\u0001\u0007\u0002\u0000\u0001\u0007\u0012\u0000\u0001\u0007\u0001\u0000\u0001\t\b\u0000\u0001\u0006\u0001\u0019\u0001\u0002\u0001\u0004\u0001\n\n\u0003\u0001\u001a\u0006\u0000\u0004\u0001\u0001\u0005\u0001\u0001\u0014\u0000\u0001\u0017\u0001\b\u0001\u0018\u0003\u0000\u0001\u0012\u0001\u000b\u0002\u0001\u0001\u0011\u0001\f\u0005\u0000\u0001\u0013\u0001\u0000\u0001\r\u0003\u0000\u0001\u000e\u0001\u0014\u0001\u000f\u0001\u0010\u0005\u0000\u0001\u0015\u0001\u0000\u0001\u0016\uff82\u0000";
    private static final char[] ZZ_CMAP;
    private static final int[] ZZ_ACTION;
    private static final String ZZ_ACTION_PACKED_0 = "\u0002\u0000\u0002\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0003\u0001\u0001\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\r\u0005\u0000\u0001\f\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0000\u0001\u0015\u0001\u0000\u0001\u0015\u0004\u0000\u0001\u0016\u0001\u0017\u0002\u0000\u0001\u0018";
    private static final int[] ZZ_ROWMAP;
    private static final String ZZ_ROWMAP_PACKED_0 = "\u0000\u0000\u0000\u001b\u00006\u0000Q\u0000l\u0000\u0087\u00006\u0000¢\u0000½\u0000\u00d8\u00006\u00006\u00006\u00006\u00006\u00006\u0000\u00f3\u0000\u010e\u00006\u0000\u0129\u0000\u0144\u0000\u015f\u0000\u017a\u0000\u0195\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u0000\u01b0\u0000\u01cb\u0000\u01e6\u0000\u01e6\u0000\u0201\u0000\u021c\u0000\u0237\u0000\u0252\u00006\u00006\u0000\u026d\u0000\u0288\u00006";
    private static final int[] ZZ_TRANS;
    private static final int ZZ_UNKNOWN_ERROR = 0;
    private static final int ZZ_NO_MATCH = 1;
    private static final int ZZ_PUSHBACK_2BIG = 2;
    private static final String[] ZZ_ERROR_MSG;
    private static final int[] ZZ_ATTRIBUTE;
    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\u0002\u0000\u0001\t\u0003\u0001\u0001\t\u0003\u0001\u0006\t\u0002\u0001\u0001\t\u0005\u0000\b\t\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0004\u0000\u0002\t\u0002\u0000\u0001\t";
    private Reader zzReader;
    private int zzState;
    private int zzLexicalState;
    private char[] zzBuffer;
    private int zzMarkedPos;
    private int zzCurrentPos;
    private int zzStartRead;
    private int zzEndRead;
    private int yyline;
    private int yychar;
    private int yycolumn;
    private boolean zzAtBOL;
    private boolean zzAtEOF;
    private StringBuffer sb;
    
    private static int[] zzUnpackAction() {
        final int[] v1 = /*EL:55*/new int[45];
        int v2 = /*EL:56*/0;
        /*SL:57*/v2 = zzUnpackAction("\u0002\u0000\u0002\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0003\u0001\u0001\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\r\u0005\u0000\u0001\f\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0000\u0001\u0015\u0001\u0000\u0001\u0015\u0004\u0000\u0001\u0016\u0001\u0017\u0002\u0000\u0001\u0018", v2, v1);
        /*SL:58*/return v1;
    }
    
    private static int zzUnpackAction(final String a3, final int v1, final int[] v2) {
        int v3 = /*EL:62*/0;
        int v4 = /*EL:63*/v1;
        final int v5 = /*EL:64*/a3.length();
        /*SL:65*/while (v3 < v5) {
            int a4 = /*EL:66*/a3.charAt(v3++);
            final int a5 = /*EL:67*/a3.charAt(v3++);
            /*SL:68*/do {
                v2[v4++] = a5;
            } while (--a4 > 0);
        }
        /*SL:70*/return v4;
    }
    
    private static int[] zzUnpackRowMap() {
        final int[] v1 = /*EL:88*/new int[45];
        int v2 = /*EL:89*/0;
        /*SL:90*/v2 = zzUnpackRowMap("\u0000\u0000\u0000\u001b\u00006\u0000Q\u0000l\u0000\u0087\u00006\u0000¢\u0000½\u0000\u00d8\u00006\u00006\u00006\u00006\u00006\u00006\u0000\u00f3\u0000\u010e\u00006\u0000\u0129\u0000\u0144\u0000\u015f\u0000\u017a\u0000\u0195\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u0000\u01b0\u0000\u01cb\u0000\u01e6\u0000\u01e6\u0000\u0201\u0000\u021c\u0000\u0237\u0000\u0252\u00006\u00006\u0000\u026d\u0000\u0288\u00006", v2, v1);
        /*SL:91*/return v1;
    }
    
    private static int zzUnpackRowMap(final String a2, final int a3, final int[] v1) {
        int v2 = /*EL:95*/0;
        int v3 = /*EL:96*/a3;
        int a4;
        /*SL:98*/for (int v4 = a2.length(); v2 < v4; /*SL:99*/a4 = a2.charAt(v2++) << 16, /*SL:100*/v1[v3++] = (a4 | a2.charAt(v2++))) {}
        /*SL:102*/return v3;
    }
    
    private static int[] zzUnpackAttribute() {
        final int[] v1 = /*EL:202*/new int[45];
        int v2 = /*EL:203*/0;
        /*SL:204*/v2 = zzUnpackAttribute("\u0002\u0000\u0001\t\u0003\u0001\u0001\t\u0003\u0001\u0006\t\u0002\u0001\u0001\t\u0005\u0000\b\t\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0004\u0000\u0002\t\u0002\u0000\u0001\t", v2, v1);
        /*SL:205*/return v1;
    }
    
    private static int zzUnpackAttribute(final String a3, final int v1, final int[] v2) {
        int v3 = /*EL:209*/0;
        int v4 = /*EL:210*/v1;
        final int v5 = /*EL:211*/a3.length();
        /*SL:212*/while (v3 < v5) {
            int a4 = /*EL:213*/a3.charAt(v3++);
            final int a5 = /*EL:214*/a3.charAt(v3++);
            /*SL:215*/do {
                v2[v4++] = a5;
            } while (--a4 > 0);
        }
        /*SL:217*/return v4;
    }
    
    int getPosition() {
        /*SL:270*/return this.yychar;
    }
    
    Yylex(final Reader a1) {
        this.zzLexicalState = 0;
        this.zzBuffer = new char[16384];
        this.zzAtBOL = true;
        this.sb = new StringBuffer();
        this.zzReader = a1;
    }
    
    Yylex(final InputStream a1) {
        this(new InputStreamReader(a1));
    }
    
    private static char[] zzUnpackCMap(final String v-4) {
        final char[] array = /*EL:302*/new char[65536];
        int i = /*EL:303*/0;
        int n = /*EL:304*/0;
        /*SL:305*/while (i < 90) {
            int a1 = /*EL:306*/v-4.charAt(i++);
            final char v1 = /*EL:307*/v-4.charAt(i++);
            /*SL:308*/do {
                array[n++] = v1;
            } while (--a1 > 0);
        }
        /*SL:310*/return array;
    }
    
    private boolean zzRefill() throws IOException {
        /*SL:324*/if (this.zzStartRead > 0) {
            /*SL:325*/System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
            /*SL:330*/this.zzEndRead -= this.zzStartRead;
            /*SL:331*/this.zzCurrentPos -= this.zzStartRead;
            /*SL:332*/this.zzMarkedPos -= this.zzStartRead;
            /*SL:333*/this.zzStartRead = 0;
        }
        /*SL:337*/if (this.zzCurrentPos >= this.zzBuffer.length) {
            final char[] v1 = /*EL:339*/new char[this.zzCurrentPos * 2];
            /*SL:340*/System.arraycopy(this.zzBuffer, 0, v1, 0, this.zzBuffer.length);
            /*SL:341*/this.zzBuffer = v1;
        }
        final int v2 = /*EL:345*/this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
        /*SL:348*/if (v2 > 0) {
            /*SL:349*/this.zzEndRead += v2;
            /*SL:350*/return false;
        }
        /*SL:353*/if (v2 != 0) {
            /*SL:364*/return true;
        }
        final int v3 = this.zzReader.read();
        if (v3 == -1) {
            return true;
        }
        this.zzBuffer[this.zzEndRead++] = (char)v3;
        return false;
    }
    
    public final void yyclose() throws IOException {
        /*SL:372*/this.zzAtEOF = true;
        /*SL:373*/this.zzEndRead = this.zzStartRead;
        /*SL:375*/if (this.zzReader != null) {
            /*SL:376*/this.zzReader.close();
        }
    }
    
    public final void yyreset(final Reader a1) {
        /*SL:391*/this.zzReader = a1;
        /*SL:392*/this.zzAtBOL = true;
        /*SL:393*/this.zzAtEOF = false;
        final boolean b = /*EL:394*/false;
        this.zzStartRead = (b ? 1 : 0);
        this.zzEndRead = (b ? 1 : 0);
        final boolean b2 = /*EL:395*/false;
        this.zzMarkedPos = (b2 ? 1 : 0);
        this.zzCurrentPos = (b2 ? 1 : 0);
        final boolean yyline = /*EL:396*/false;
        this.yycolumn = (yyline ? 1 : 0);
        this.yychar = (yyline ? 1 : 0);
        this.yyline = (yyline ? 1 : 0);
        /*SL:397*/this.zzLexicalState = 0;
    }
    
    public final int yystate() {
        /*SL:405*/return this.zzLexicalState;
    }
    
    public final void yybegin(final int a1) {
        /*SL:415*/this.zzLexicalState = a1;
    }
    
    public final String yytext() {
        /*SL:423*/return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
    }
    
    public final char yycharat(final int a1) {
        /*SL:439*/return this.zzBuffer[this.zzStartRead + a1];
    }
    
    public final int yylength() {
        /*SL:447*/return this.zzMarkedPos - this.zzStartRead;
    }
    
    private void zzScanError(final int v2) {
        String v3;
        try {
            /*SL:468*/v3 = Yylex.ZZ_ERROR_MSG[v2];
        }
        catch (ArrayIndexOutOfBoundsException a1) {
            /*SL:471*/v3 = Yylex.ZZ_ERROR_MSG[0];
        }
        /*SL:474*/throw new Error(v3);
    }
    
    public void yypushback(final int a1) {
        /*SL:487*/if (a1 > this.yylength()) {
            /*SL:488*/this.zzScanError(2);
        }
        /*SL:490*/this.zzMarkedPos -= a1;
    }
    
    public Yytoken yylex() throws IOException, ParseException {
        int v5 = /*EL:508*/this.zzEndRead;
        char[] v2 = /*EL:509*/this.zzBuffer;
        final char[] v3 = Yylex.ZZ_CMAP;
        final int[] v4 = Yylex.ZZ_TRANS;
        /*SL:513*/v5 = Yylex.ZZ_ROWMAP;
        final int[] v6 = Yylex.ZZ_ATTRIBUTE;
        while (true) {
            int v7 = /*EL:517*/this.zzMarkedPos;
            /*SL:519*/this.yychar += v7 - this.zzStartRead;
            int v8 = /*EL:521*/-1;
            final int n = /*EL:523*/v7;
            this.zzStartRead = n;
            this.zzCurrentPos = n;
            int v9 = n;
            /*SL:525*/this.zzState = Yylex.ZZ_LEXSTATE[this.zzLexicalState];
            int v10;
            while (true) {
                /*SL:531*/if (v9 < v5) {
                    /*SL:532*/v10 = v2[v9++];
                }
                else {
                    /*SL:533*/if (this.zzAtEOF) {
                        /*SL:534*/v10 = -1;
                        /*SL:535*/break;
                    }
                    /*SL:539*/this.zzCurrentPos = v9;
                    /*SL:540*/this.zzMarkedPos = v7;
                    final boolean v11 = /*EL:541*/this.zzRefill();
                    /*SL:543*/v9 = this.zzCurrentPos;
                    /*SL:544*/v7 = this.zzMarkedPos;
                    /*SL:545*/v2 = this.zzBuffer;
                    /*SL:546*/v5 = this.zzEndRead;
                    /*SL:547*/if (v11) {
                        /*SL:548*/v10 = -1;
                        /*SL:549*/break;
                    }
                    /*SL:552*/v10 = v2[v9++];
                }
                final int v12 = /*EL:555*/v4[v5[this.zzState] + v3[v10]];
                /*SL:556*/if (v12 == -1) {
                    break;
                }
                /*SL:557*/this.zzState = v12;
                final int v13 = /*EL:559*/v6[this.zzState];
                /*SL:560*/if ((v13 & 0x1) != 0x1) {
                    continue;
                }
                /*SL:561*/v8 = this.zzState;
                /*SL:562*/v7 = v9;
                /*SL:563*/if ((v13 & 0x8) == 0x8) {
                    break;
                }
            }
            /*SL:570*/this.zzMarkedPos = v7;
            /*SL:572*/switch ((v8 < 0) ? v8 : Yylex.ZZ_ACTION[v8]) {
                case 11: {
                    /*SL:574*/this.sb.append(this.yytext());
                }
                case 25: {
                    /*SL:576*/continue;
                }
                case 4: {
                    /*SL:578*/this.sb.delete(0, this.sb.length());
                    this.yybegin(2);
                }
                case 26: {
                    /*SL:580*/continue;
                }
                case 16: {
                    /*SL:582*/this.sb.append('\b');
                }
                case 27: {
                    /*SL:584*/continue;
                }
                case 6: {
                    /*SL:586*/return new Yytoken(2, null);
                }
                case 28: {
                    /*SL:588*/continue;
                }
                case 23: {
                    final Boolean v14 = /*EL:590*/Boolean.valueOf(this.yytext());
                    return new Yytoken(0, v14);
                }
                case 29: {
                    /*SL:592*/continue;
                }
                case 22: {
                    /*SL:594*/return new Yytoken(0, null);
                }
                case 30: {
                    /*SL:596*/continue;
                }
                case 13: {
                    /*SL:598*/this.yybegin(0);
                    return new Yytoken(0, this.sb.toString());
                }
                case 31: {
                    /*SL:600*/continue;
                }
                case 12: {
                    /*SL:602*/this.sb.append('\\');
                }
                case 32: {
                    /*SL:604*/continue;
                }
                case 21: {
                    final Double v15 = /*EL:606*/Double.valueOf(this.yytext());
                    return new Yytoken(0, v15);
                }
                case 33: {
                    /*SL:608*/continue;
                }
                case 1: {
                    /*SL:610*/throw new ParseException(this.yychar, 0, new Character(this.yycharat(0)));
                }
                case 34: {
                    /*SL:612*/continue;
                }
                case 8: {
                    /*SL:614*/return new Yytoken(4, null);
                }
                case 35: {
                    /*SL:616*/continue;
                }
                case 19: {
                    /*SL:618*/this.sb.append('\r');
                }
                case 36: {
                    /*SL:620*/continue;
                }
                case 15: {
                    /*SL:622*/this.sb.append('/');
                }
                case 37: {
                    /*SL:624*/continue;
                }
                case 10: {
                    /*SL:626*/return new Yytoken(6, null);
                }
                case 38: {
                    /*SL:628*/continue;
                }
                case 14: {
                    /*SL:630*/this.sb.append('\"');
                }
                case 39: {
                    /*SL:632*/continue;
                }
                case 5: {
                    /*SL:634*/return new Yytoken(1, null);
                }
                case 40: {
                    /*SL:636*/continue;
                }
                case 17: {
                    /*SL:638*/this.sb.append('\f');
                }
                case 41: {
                    /*SL:640*/continue;
                }
                case 24: {
                    try {
                        final int v12 = /*EL:643*/Integer.parseInt(this.yytext().substring(2), 16);
                        /*SL:644*/this.sb.append((char)v12);
                    }
                    catch (Exception v16) {
                        /*SL:647*/throw new ParseException(this.yychar, 2, v16);
                    }
                }
                case 42: {
                    /*SL:650*/continue;
                }
                case 20: {
                    /*SL:652*/this.sb.append('\t');
                }
                case 43: {
                    /*SL:654*/continue;
                }
                case 7: {
                    /*SL:656*/return new Yytoken(3, null);
                }
                case 44: {
                    /*SL:658*/continue;
                }
                case 2: {
                    final Long v17 = /*EL:660*/Long.valueOf(this.yytext());
                    return new Yytoken(0, v17);
                }
                case 45: {
                    /*SL:662*/continue;
                }
                case 18: {
                    /*SL:664*/this.sb.append('\n');
                }
                case 46: {
                    /*SL:666*/continue;
                }
                case 9: {
                    /*SL:668*/return new Yytoken(5, null);
                }
                case 47: {
                    /*SL:670*/continue;
                }
                case 3:
                case 48: {
                    /*SL:674*/continue;
                }
                default: {
                    /*SL:676*/if (v10 == -1 && this.zzStartRead == this.zzCurrentPos) {
                        /*SL:677*/this.zzAtEOF = true;
                        /*SL:678*/return null;
                    }
                    /*SL:681*/this.zzScanError(1);
                    continue;
                }
            }
        }
    }
    
    static {
        ZZ_LEXSTATE = new int[] { 0, 0, 1, 1 };
        ZZ_CMAP = zzUnpackCMap("\t\u0000\u0001\u0007\u0001\u0007\u0002\u0000\u0001\u0007\u0012\u0000\u0001\u0007\u0001\u0000\u0001\t\b\u0000\u0001\u0006\u0001\u0019\u0001\u0002\u0001\u0004\u0001\n\n\u0003\u0001\u001a\u0006\u0000\u0004\u0001\u0001\u0005\u0001\u0001\u0014\u0000\u0001\u0017\u0001\b\u0001\u0018\u0003\u0000\u0001\u0012\u0001\u000b\u0002\u0001\u0001\u0011\u0001\f\u0005\u0000\u0001\u0013\u0001\u0000\u0001\r\u0003\u0000\u0001\u000e\u0001\u0014\u0001\u000f\u0001\u0010\u0005\u0000\u0001\u0015\u0001\u0000\u0001\u0016\uff82\u0000");
        ZZ_ACTION = zzUnpackAction();
        ZZ_ROWMAP = zzUnpackRowMap();
        ZZ_TRANS = new int[] { 2, 2, 3, 4, 2, 2, 2, 5, 2, 6, 2, 2, 7, 8, 2, 9, 2, 2, 2, 2, 2, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, 19, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, 32, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 34, 35, -1, -1, 34, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, 39, -1, 39, -1, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 40, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 41, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 42, -1, 42, -1, 42, -1, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, 43, -1, 43, -1, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, -1, 44, -1, 44, -1, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, -1, -1, -1, -1 };
        ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
        ZZ_ATTRIBUTE = zzUnpackAttribute();
    }
}
