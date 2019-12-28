package javassist.compiler;

public class Lex implements TokenId
{
    private int lastChar;
    private StringBuffer textBuffer;
    private Token currentToken;
    private Token lookAheadTokens;
    private String input;
    private int position;
    private int maxlen;
    private int lineNumber;
    private static final int[] equalOps;
    private static final KeywordTable ktable;
    
    public Lex(final String a1) {
        this.lastChar = -1;
        this.textBuffer = new StringBuffer();
        this.currentToken = new Token();
        this.lookAheadTokens = null;
        this.input = a1;
        this.position = 0;
        this.maxlen = a1.length();
        this.lineNumber = 0;
    }
    
    public int get() {
        /*SL:53*/if (this.lookAheadTokens == null) {
            /*SL:54*/return this.get(this.currentToken);
        }
        final Token v1 = /*EL:57*/this.currentToken = this.lookAheadTokens;
        /*SL:58*/this.lookAheadTokens = this.lookAheadTokens.next;
        /*SL:59*/return v1.tokenId;
    }
    
    public int lookAhead() {
        /*SL:67*/return this.lookAhead(0);
    }
    
    public int lookAhead(int v2) {
        Token v3 = /*EL:71*/this.lookAheadTokens;
        /*SL:72*/if (v3 == null) {
            /*SL:73*/v3 = (this.lookAheadTokens = this.currentToken);
            /*SL:74*/v3.next = null;
            /*SL:75*/this.get(v3);
        }
        /*SL:78*/while (v2-- > 0) {
            /*SL:79*/if (v3.next == null) {
                final Token a1 = /*EL:81*/v3.next = new Token();
                /*SL:82*/this.get(a1);
            }
            v3 = v3.next;
        }
        /*SL:85*/this.currentToken = v3;
        /*SL:86*/return v3.tokenId;
    }
    
    public String getString() {
        /*SL:90*/return this.currentToken.textValue;
    }
    
    public long getLong() {
        /*SL:94*/return this.currentToken.longValue;
    }
    
    public double getDouble() {
        /*SL:98*/return this.currentToken.doubleValue;
    }
    
    private int get(final Token a1) {
        int v1;
        /*SL:105*/do {
            v1 = this.readLine(a1);
        } while (v1 == 10);
        /*SL:107*/return a1.tokenId = v1;
    }
    
    private int readLine(final Token v2) {
        int v3 = /*EL:111*/this.getNextNonWhiteChar();
        /*SL:112*/if (v3 < 0) {
            /*SL:113*/return v3;
        }
        /*SL:114*/if (v3 == 10) {
            /*SL:115*/++this.lineNumber;
            /*SL:116*/return 10;
        }
        /*SL:118*/if (v3 == 39) {
            /*SL:119*/return this.readCharConst(v2);
        }
        /*SL:120*/if (v3 == 34) {
            /*SL:121*/return this.readStringL(v2);
        }
        /*SL:122*/if (48 <= v3 && v3 <= 57) {
            /*SL:123*/return this.readNumber(v3, v2);
        }
        /*SL:124*/if (v3 == 46) {
            /*SL:125*/v3 = this.getc();
            /*SL:126*/if (48 <= v3 && v3 <= 57) {
                final StringBuffer a1 = /*EL:127*/this.textBuffer;
                /*SL:128*/a1.setLength(0);
                /*SL:129*/a1.append('.');
                /*SL:130*/return this.readDouble(a1, v3, v2);
            }
            /*SL:133*/this.ungetc(v3);
            /*SL:134*/return this.readSeparator(46);
        }
        else {
            /*SL:137*/if (Character.isJavaIdentifierStart((char)v3)) {
                /*SL:138*/return this.readIdentifier(v3, v2);
            }
            /*SL:140*/return this.readSeparator(v3);
        }
    }
    
    private int getNextNonWhiteChar() {
        int v1;
        /*SL:171*/do {
            v1 = this.getc();
            if (v1 == 47) {
                v1 = this.getc();
                if (v1 == 47) {
                    do {
                        v1 = this.getc();
                        if (v1 != 10 && v1 != 13) {
                            continue;
                        }
                        break;
                    } while (v1 != -1);
                }
                else if (v1 == 42) {
                    while (true) {
                        v1 = this.getc();
                        if (v1 == -1) {
                            break;
                        }
                        if (v1 != 42) {
                            continue;
                        }
                        if ((v1 = this.getc()) == 47) {
                            v1 = 32;
                            break;
                        }
                        this.ungetc(v1);
                    }
                }
                else {
                    this.ungetc(v1);
                    v1 = 47;
                }
            }
        } while (isBlank(v1));
        /*SL:172*/return v1;
    }
    
    private int readCharConst(final Token a1) {
        int v2 = /*EL:177*/0;
        /*SL:178*/while ((v2 = this.getc()) != 39) {
            /*SL:179*/if (v2 == 92) {
                /*SL:180*/v2 = this.readEscapeChar();
            }
            else {
                /*SL:181*/if (v2 < 32) {
                    /*SL:182*/if (v2 == 10) {
                        /*SL:183*/++this.lineNumber;
                    }
                    /*SL:185*/return 500;
                }
                /*SL:188*/v2 = v2;
            }
        }
        /*SL:190*/a1.longValue = v2;
        /*SL:191*/return 401;
    }
    
    private int readEscapeChar() {
        int v1 = /*EL:195*/this.getc();
        /*SL:196*/if (v1 == 110) {
            /*SL:197*/v1 = 10;
        }
        else/*SL:198*/ if (v1 == 116) {
            /*SL:199*/v1 = 9;
        }
        else/*SL:200*/ if (v1 == 114) {
            /*SL:201*/v1 = 13;
        }
        else/*SL:202*/ if (v1 == 102) {
            /*SL:203*/v1 = 12;
        }
        else/*SL:204*/ if (v1 == 10) {
            /*SL:205*/++this.lineNumber;
        }
        /*SL:207*/return v1;
    }
    
    private int readStringL(final Token a1) {
        StringBuffer v2 = /*EL:212*/this.textBuffer;
        /*SL:213*/v2.setLength(0);
        while (true) {
            /*SL:215*/if ((v2 = this.getc()) != 34) {
                /*SL:216*/if (v2 == 92) {
                    /*SL:217*/v2 = this.readEscapeChar();
                }
                else/*SL:218*/ if (v2 == 10 || v2 < 0) {
                    /*SL:219*/++this.lineNumber;
                    /*SL:220*/return 500;
                }
                /*SL:223*/v2.append((char)v2);
            }
            else {
                while (true) {
                    /*SL:227*/v2 = this.getc();
                    /*SL:228*/if (v2 == 10) {
                        /*SL:229*/++this.lineNumber;
                    }
                    else {
                        /*SL:230*/if (!isBlank(v2)) {
                            break;
                        }
                        continue;
                    }
                }
                /*SL:234*/if (v2 != 34) {
                    /*SL:235*/this.ungetc(v2);
                    /*SL:240*/a1.textValue = v2.toString();
                    /*SL:241*/return 406;
                }
                continue;
            }
        }
    }
    
    private int readNumber(int v1, final Token v2) {
        long v3 = /*EL:245*/0L;
        int v4 = /*EL:246*/this.getc();
        /*SL:247*/if (v1 == 48) {
            /*SL:248*/if (v4 == 88 || v4 == 120) {
                while (true) {
                    /*SL:250*/v1 = this.getc();
                    /*SL:251*/if (48 <= v1 && v1 <= 57) {
                        /*SL:252*/v3 = v3 * 16L + (v1 - 48);
                    }
                    else/*SL:253*/ if (65 <= v1 && v1 <= 70) {
                        /*SL:254*/v3 = v3 * 16L + (v1 - 65 + 10);
                    }
                    else {
                        /*SL:255*/if (97 > v1 || v1 > 102) {
                            break;
                        }
                        /*SL:256*/v3 = v3 * 16L + (v1 - 97 + 10);
                    }
                }
                /*SL:258*/v2.longValue = v3;
                /*SL:259*/if (v1 == 76 || v1 == 108) {
                    /*SL:260*/return 403;
                }
                /*SL:262*/this.ungetc(v1);
                /*SL:263*/return 402;
            }
            else/*SL:267*/ if (48 <= v4 && v4 <= 55) {
                /*SL:268*/v3 = v4 - 48;
                while (true) {
                    /*SL:270*/v1 = this.getc();
                    /*SL:271*/if (48 > v1 || v1 > 55) {
                        break;
                    }
                    /*SL:272*/v3 = v3 * 8L + (v1 - 48);
                }
                /*SL:274*/v2.longValue = v3;
                /*SL:275*/if (v1 == 76 || v1 == 108) {
                    /*SL:276*/return 403;
                }
                /*SL:278*/this.ungetc(v1);
                /*SL:279*/return 402;
            }
        }
        /*SL:285*/v3 = v1 - 48;
        /*SL:286*/while (48 <= v4 && v4 <= 57) {
            /*SL:287*/v3 = v3 * 10L + v4 - 48L;
            /*SL:288*/v4 = this.getc();
        }
        /*SL:291*/v2.longValue = v3;
        /*SL:292*/if (v4 == 70 || v4 == 102) {
            /*SL:293*/v2.doubleValue = v3;
            /*SL:294*/return 404;
        }
        /*SL:296*/if (v4 == 69 || v4 == 101 || v4 == 68 || v4 == 100 || v4 == 46) {
            final StringBuffer a1 = /*EL:298*/this.textBuffer;
            /*SL:299*/a1.setLength(0);
            /*SL:300*/a1.append(v3);
            /*SL:301*/return this.readDouble(a1, v4, v2);
        }
        /*SL:303*/if (v4 == 76 || v4 == 108) {
            /*SL:304*/return 403;
        }
        /*SL:306*/this.ungetc(v4);
        /*SL:307*/return 402;
    }
    
    private int readDouble(final StringBuffer a3, int v1, final Token v2) {
        /*SL:312*/if (v1 != 69 && v1 != 101 && v1 != 68 && v1 != 100) {
            /*SL:313*/a3.append((char)v1);
            while (true) {
                /*SL:315*/v1 = this.getc();
                /*SL:316*/if (48 > v1 || v1 > 57) {
                    break;
                }
                /*SL:317*/a3.append((char)v1);
            }
        }
        /*SL:323*/if (v1 == 69 || v1 == 101) {
            /*SL:324*/a3.append((char)v1);
            /*SL:325*/v1 = this.getc();
            /*SL:326*/if (v1 == 43 || v1 == 45) {
                /*SL:327*/a3.append((char)v1);
                /*SL:328*/v1 = this.getc();
            }
            /*SL:331*/while (48 <= v1 && v1 <= 57) {
                /*SL:332*/a3.append((char)v1);
                /*SL:333*/v1 = this.getc();
            }
        }
        try {
            /*SL:338*/v2.doubleValue = Double.parseDouble(a3.toString());
        }
        catch (NumberFormatException a4) {
            /*SL:341*/return 500;
        }
        /*SL:344*/if (v1 == 70 || v1 == 102) {
            /*SL:345*/return 404;
        }
        /*SL:347*/if (v1 != 68 && v1 != 100) {
            /*SL:348*/this.ungetc(v1);
        }
        /*SL:350*/return 405;
    }
    
    private int readSeparator(final int v-1) {
        int v2;
        int v2;
        /*SL:363*/if (33 <= v-1 && v-1 <= 63) {
            /*SL:364*/v2 = Lex.equalOps[v-1 - 33];
            /*SL:365*/if (v2 == 0) {
                /*SL:366*/return v-1;
            }
            /*SL:368*/v2 = this.getc();
            /*SL:369*/if (v-1 == v2) {
                /*SL:370*/switch (v-1) {
                    case 61: {
                        /*SL:372*/return 358;
                    }
                    case 43: {
                        /*SL:374*/return 362;
                    }
                    case 45: {
                        /*SL:376*/return 363;
                    }
                    case 38: {
                        /*SL:378*/return 369;
                    }
                    case 60: {
                        final int a1 = /*EL:380*/this.getc();
                        /*SL:381*/if (a1 == 61) {
                            /*SL:382*/return 365;
                        }
                        /*SL:384*/this.ungetc(a1);
                        /*SL:385*/return 364;
                    }
                    case 62: {
                        int v3 = /*EL:388*/this.getc();
                        /*SL:389*/if (v3 == 61) {
                            /*SL:390*/return 367;
                        }
                        /*SL:391*/if (v3 != 62) {
                            /*SL:401*/this.ungetc(v3);
                            /*SL:402*/return 366;
                        }
                        v3 = this.getc();
                        if (v3 == 61) {
                            return 371;
                        }
                        this.ungetc(v3);
                        return 370;
                    }
                }
            }
            else/*SL:407*/ if (v2 == 61) {
                /*SL:408*/return v2;
            }
        }
        else/*SL:411*/ if (v-1 == 94) {
            /*SL:412*/v2 = this.getc();
            /*SL:413*/if (v2 == 61) {
                /*SL:414*/return 360;
            }
        }
        else {
            /*SL:416*/if (v-1 != 124) {
                /*SL:424*/return v-1;
            }
            v2 = this.getc();
            if (v2 == 61) {
                return 361;
            }
            if (v2 == 124) {
                return 368;
            }
        }
        /*SL:426*/this.ungetc(v2);
        /*SL:427*/return v-1;
    }
    
    private int readIdentifier(int a1, final Token a2) {
        final StringBuffer v1 = /*EL:431*/this.textBuffer;
        /*SL:432*/v1.setLength(0);
        /*SL:437*/do {
            v1.append((char)a1);
            a1 = this.getc();
        } while (Character.isJavaIdentifierPart((char)a1));
        /*SL:439*/this.ungetc(a1);
        final String v2 = /*EL:441*/v1.toString();
        final int v3 = Lex.ktable.lookup(/*EL:442*/v2);
        /*SL:443*/if (v3 >= 0) {
            /*SL:444*/return v3;
        }
        /*SL:453*/a2.textValue = v2;
        /*SL:454*/return 400;
    }
    
    private static boolean isBlank(final int a1) {
        /*SL:515*/return a1 == 32 || a1 == 9 || a1 == 12 || a1 == 13 || a1 == 10;
    }
    
    private static boolean isDigit(final int a1) {
        /*SL:520*/return 48 <= a1 && a1 <= 57;
    }
    
    private void ungetc(final int a1) {
        /*SL:524*/this.lastChar = a1;
    }
    
    public String getTextAround() {
        int v1 = /*EL:528*/this.position - 10;
        /*SL:529*/if (v1 < 0) {
            /*SL:530*/v1 = 0;
        }
        int v2 = /*EL:532*/this.position + 10;
        /*SL:533*/if (v2 > this.maxlen) {
            /*SL:534*/v2 = this.maxlen;
        }
        /*SL:536*/return this.input.substring(v1, v2);
    }
    
    private int getc() {
        /*SL:540*/if (this.lastChar >= 0) {
            final int v1 = /*EL:546*/this.lastChar;
            /*SL:547*/this.lastChar = -1;
            /*SL:548*/return v1;
        }
        if (this.position < this.maxlen) {
            return this.input.charAt(this.position++);
        }
        return -1;
    }
    
    static {
        equalOps = new int[] { 350, 0, 0, 0, 351, 352, 0, 0, 0, 353, 354, 0, 355, 0, 356, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 357, 358, 359, 0 };
        (ktable = new KeywordTable()).append("abstract", 300);
        Lex.ktable.append("boolean", 301);
        Lex.ktable.append("break", 302);
        Lex.ktable.append("byte", 303);
        Lex.ktable.append("case", 304);
        Lex.ktable.append("catch", 305);
        Lex.ktable.append("char", 306);
        Lex.ktable.append("class", 307);
        Lex.ktable.append("const", 308);
        Lex.ktable.append("continue", 309);
        Lex.ktable.append("default", 310);
        Lex.ktable.append("do", 311);
        Lex.ktable.append("double", 312);
        Lex.ktable.append("else", 313);
        Lex.ktable.append("extends", 314);
        Lex.ktable.append("false", 411);
        Lex.ktable.append("final", 315);
        Lex.ktable.append("finally", 316);
        Lex.ktable.append("float", 317);
        Lex.ktable.append("for", 318);
        Lex.ktable.append("goto", 319);
        Lex.ktable.append("if", 320);
        Lex.ktable.append("implements", 321);
        Lex.ktable.append("import", 322);
        Lex.ktable.append("instanceof", 323);
        Lex.ktable.append("int", 324);
        Lex.ktable.append("interface", 325);
        Lex.ktable.append("long", 326);
        Lex.ktable.append("native", 327);
        Lex.ktable.append("new", 328);
        Lex.ktable.append("null", 412);
        Lex.ktable.append("package", 329);
        Lex.ktable.append("private", 330);
        Lex.ktable.append("protected", 331);
        Lex.ktable.append("public", 332);
        Lex.ktable.append("return", 333);
        Lex.ktable.append("short", 334);
        Lex.ktable.append("static", 335);
        Lex.ktable.append("strictfp", 347);
        Lex.ktable.append("super", 336);
        Lex.ktable.append("switch", 337);
        Lex.ktable.append("synchronized", 338);
        Lex.ktable.append("this", 339);
        Lex.ktable.append("throw", 340);
        Lex.ktable.append("throws", 341);
        Lex.ktable.append("transient", 342);
        Lex.ktable.append("true", 410);
        Lex.ktable.append("try", 343);
        Lex.ktable.append("void", 344);
        Lex.ktable.append("volatile", 345);
        Lex.ktable.append("while", 346);
    }
}
