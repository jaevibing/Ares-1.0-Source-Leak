package com.fasterxml.jackson.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public final class UTF8Writer extends Writer
{
    static final int SURR1_FIRST = 55296;
    static final int SURR1_LAST = 56319;
    static final int SURR2_FIRST = 56320;
    static final int SURR2_LAST = 57343;
    private final IOContext _context;
    private OutputStream _out;
    private byte[] _outBuffer;
    private final int _outBufferEnd;
    private int _outPtr;
    private int _surrogate;
    
    public UTF8Writer(final IOContext a1, final OutputStream a2) {
        this._context = a1;
        this._out = a2;
        this._outBuffer = a1.allocWriteEncodingBuffer();
        this._outBufferEnd = this._outBuffer.length - 4;
        this._outPtr = 0;
    }
    
    @Override
    public Writer append(final char a1) throws IOException {
        /*SL:47*/this.write(a1);
        /*SL:48*/return this;
    }
    
    @Override
    public void close() throws IOException {
        /*SL:55*/if (this._out != null) {
            /*SL:56*/if (this._outPtr > 0) {
                /*SL:57*/this._out.write(this._outBuffer, 0, this._outPtr);
                /*SL:58*/this._outPtr = 0;
            }
            final OutputStream v1 = /*EL:60*/this._out;
            /*SL:61*/this._out = null;
            final byte[] v2 = /*EL:63*/this._outBuffer;
            /*SL:64*/if (v2 != null) {
                /*SL:65*/this._outBuffer = null;
                /*SL:66*/this._context.releaseWriteEncodingBuffer(v2);
            }
            /*SL:69*/v1.close();
            final int v3 = /*EL:74*/this._surrogate;
            /*SL:75*/this._surrogate = 0;
            /*SL:76*/if (v3 > 0) {
                illegalSurrogate(/*EL:77*/v3);
            }
        }
    }
    
    @Override
    public void flush() throws IOException {
        /*SL:86*/if (this._out != null) {
            /*SL:87*/if (this._outPtr > 0) {
                /*SL:88*/this._out.write(this._outBuffer, 0, this._outPtr);
                /*SL:89*/this._outPtr = 0;
            }
            /*SL:91*/this._out.flush();
        }
    }
    
    @Override
    public void write(final char[] a1) throws IOException {
        /*SL:99*/this.write(a1, 0, a1.length);
    }
    
    @Override
    public void write(final char[] v-5, int v-4, int v-3) throws IOException {
        /*SL:106*/if (v-3 < 2) {
            /*SL:107*/if (v-3 == 1) {
                /*SL:108*/this.write(v-5[v-4]);
            }
            /*SL:110*/return;
        }
        /*SL:114*/if (this._surrogate > 0) {
            final char a1 = /*EL:115*/v-5[v-4++];
            /*SL:116*/--v-3;
            /*SL:117*/this.write(this.convertSurrogate(a1));
        }
        int outPtr = /*EL:121*/this._outPtr;
        final byte[] outBuffer = /*EL:122*/this._outBuffer;
        final int v0 = /*EL:123*/this._outBufferEnd;
        /*SL:126*/v-3 += v-4;
        /*SL:129*/while (v-4 < v-3) {
            /*SL:133*/if (outPtr >= v0) {
                /*SL:134*/this._out.write(outBuffer, 0, outPtr);
                /*SL:135*/outPtr = 0;
            }
            int v = /*EL:138*/v-5[v-4++];
            Label_0193: {
                /*SL:140*/if (v < 128) {
                    /*SL:141*/outBuffer[outPtr++] = (byte)v;
                    int a2 = /*EL:143*/v-3 - v-4;
                    final int a3 = /*EL:144*/v0 - outPtr;
                    /*SL:146*/if (a2 > a3) {
                        /*SL:147*/a2 = a3;
                    }
                    /*SL:149*/a2 += v-4;
                    /*SL:152*/while (v-4 < a2) {
                        /*SL:155*/v = v-5[v-4++];
                        /*SL:156*/if (v >= 128) {
                            break Label_0193;
                        }
                        /*SL:159*/outBuffer[outPtr++] = (byte)v;
                    }
                    continue;
                }
            }
            /*SL:164*/if (v < 2048) {
                /*SL:165*/outBuffer[outPtr++] = (byte)(0xC0 | v >> 6);
                /*SL:166*/outBuffer[outPtr++] = (byte)(0x80 | (v & 0x3F));
            }
            else/*SL:169*/ if (v < 55296 || v > 57343) {
                /*SL:170*/outBuffer[outPtr++] = (byte)(0xE0 | v >> 12);
                /*SL:171*/outBuffer[outPtr++] = (byte)(0x80 | (v >> 6 & 0x3F));
                /*SL:172*/outBuffer[outPtr++] = (byte)(0x80 | (v & 0x3F));
            }
            else {
                /*SL:176*/if (v > 56319) {
                    /*SL:177*/this._outPtr = outPtr;
                    illegalSurrogate(/*EL:178*/v);
                }
                /*SL:180*/this._surrogate = v;
                /*SL:182*/if (v-4 >= v-3) {
                    /*SL:183*/break;
                }
                /*SL:185*/v = this.convertSurrogate(v-5[v-4++]);
                /*SL:186*/if (v > 1114111) {
                    /*SL:187*/this._outPtr = outPtr;
                    illegalSurrogate(/*EL:188*/v);
                }
                /*SL:190*/outBuffer[outPtr++] = (byte)(0xF0 | v >> 18);
                /*SL:191*/outBuffer[outPtr++] = (byte)(0x80 | (v >> 12 & 0x3F));
                /*SL:192*/outBuffer[outPtr++] = (byte)(0x80 | (v >> 6 & 0x3F));
                /*SL:193*/outBuffer[outPtr++] = (byte)(0x80 | (v & 0x3F));
            }
        }
        /*SL:196*/this._outPtr = outPtr;
    }
    
    @Override
    public void write(int v2) throws IOException {
        /*SL:203*/if (this._surrogate > 0) {
            /*SL:204*/v2 = this.convertSurrogate(v2);
        }
        else/*SL:206*/ if (v2 >= 55296 && v2 <= 57343) {
            /*SL:208*/if (v2 > 56319) {
                illegalSurrogate(/*EL:209*/v2);
            }
            /*SL:212*/this._surrogate = v2;
            /*SL:213*/return;
        }
        /*SL:216*/if (this._outPtr >= this._outBufferEnd) {
            /*SL:217*/this._out.write(this._outBuffer, 0, this._outPtr);
            /*SL:218*/this._outPtr = 0;
        }
        /*SL:221*/if (v2 < 128) {
            /*SL:222*/this._outBuffer[this._outPtr++] = (byte)v2;
        }
        else {
            int a1 = /*EL:224*/this._outPtr;
            /*SL:225*/if (v2 < 2048) {
                /*SL:226*/this._outBuffer[a1++] = (byte)(0xC0 | v2 >> 6);
                /*SL:227*/this._outBuffer[a1++] = (byte)(0x80 | (v2 & 0x3F));
            }
            else/*SL:228*/ if (v2 <= 65535) {
                /*SL:229*/this._outBuffer[a1++] = (byte)(0xE0 | v2 >> 12);
                /*SL:230*/this._outBuffer[a1++] = (byte)(0x80 | (v2 >> 6 & 0x3F));
                /*SL:231*/this._outBuffer[a1++] = (byte)(0x80 | (v2 & 0x3F));
            }
            else {
                /*SL:233*/if (v2 > 1114111) {
                    illegalSurrogate(/*EL:234*/v2);
                }
                /*SL:236*/this._outBuffer[a1++] = (byte)(0xF0 | v2 >> 18);
                /*SL:237*/this._outBuffer[a1++] = (byte)(0x80 | (v2 >> 12 & 0x3F));
                /*SL:238*/this._outBuffer[a1++] = (byte)(0x80 | (v2 >> 6 & 0x3F));
                /*SL:239*/this._outBuffer[a1++] = (byte)(0x80 | (v2 & 0x3F));
            }
            /*SL:241*/this._outPtr = a1;
        }
    }
    
    @Override
    public void write(final String a1) throws IOException {
        /*SL:248*/this.write(a1, 0, a1.length());
    }
    
    @Override
    public void write(final String v-5, int v-4, int v-3) throws IOException {
        /*SL:254*/if (v-3 < 2) {
            /*SL:255*/if (v-3 == 1) {
                /*SL:256*/this.write(v-5.charAt(v-4));
            }
            /*SL:258*/return;
        }
        /*SL:262*/if (this._surrogate > 0) {
            final char a1 = /*EL:263*/v-5.charAt(v-4++);
            /*SL:264*/--v-3;
            /*SL:265*/this.write(this.convertSurrogate(a1));
        }
        int outPtr = /*EL:269*/this._outPtr;
        final byte[] outBuffer = /*EL:270*/this._outBuffer;
        final int v0 = /*EL:271*/this._outBufferEnd;
        /*SL:274*/v-3 += v-4;
        /*SL:277*/while (v-4 < v-3) {
            /*SL:281*/if (outPtr >= v0) {
                /*SL:282*/this._out.write(outBuffer, 0, outPtr);
                /*SL:283*/outPtr = 0;
            }
            int v = /*EL:286*/v-5.charAt(v-4++);
            Label_0201: {
                /*SL:288*/if (v < 128) {
                    /*SL:289*/outBuffer[outPtr++] = (byte)v;
                    int a2 = /*EL:291*/v-3 - v-4;
                    final int a3 = /*EL:292*/v0 - outPtr;
                    /*SL:294*/if (a2 > a3) {
                        /*SL:295*/a2 = a3;
                    }
                    /*SL:297*/a2 += v-4;
                    /*SL:300*/while (v-4 < a2) {
                        /*SL:303*/v = v-5.charAt(v-4++);
                        /*SL:304*/if (v >= 128) {
                            break Label_0201;
                        }
                        /*SL:307*/outBuffer[outPtr++] = (byte)v;
                    }
                    continue;
                }
            }
            /*SL:312*/if (v < 2048) {
                /*SL:313*/outBuffer[outPtr++] = (byte)(0xC0 | v >> 6);
                /*SL:314*/outBuffer[outPtr++] = (byte)(0x80 | (v & 0x3F));
            }
            else/*SL:317*/ if (v < 55296 || v > 57343) {
                /*SL:318*/outBuffer[outPtr++] = (byte)(0xE0 | v >> 12);
                /*SL:319*/outBuffer[outPtr++] = (byte)(0x80 | (v >> 6 & 0x3F));
                /*SL:320*/outBuffer[outPtr++] = (byte)(0x80 | (v & 0x3F));
            }
            else {
                /*SL:324*/if (v > 56319) {
                    /*SL:325*/this._outPtr = outPtr;
                    illegalSurrogate(/*EL:326*/v);
                }
                /*SL:328*/this._surrogate = v;
                /*SL:330*/if (v-4 >= v-3) {
                    /*SL:331*/break;
                }
                /*SL:333*/v = this.convertSurrogate(v-5.charAt(v-4++));
                /*SL:334*/if (v > 1114111) {
                    /*SL:335*/this._outPtr = outPtr;
                    illegalSurrogate(/*EL:336*/v);
                }
                /*SL:338*/outBuffer[outPtr++] = (byte)(0xF0 | v >> 18);
                /*SL:339*/outBuffer[outPtr++] = (byte)(0x80 | (v >> 12 & 0x3F));
                /*SL:340*/outBuffer[outPtr++] = (byte)(0x80 | (v >> 6 & 0x3F));
                /*SL:341*/outBuffer[outPtr++] = (byte)(0x80 | (v & 0x3F));
            }
        }
        /*SL:344*/this._outPtr = outPtr;
    }
    
    protected int convertSurrogate(final int a1) throws IOException {
        final int v1 = /*EL:359*/this._surrogate;
        /*SL:360*/this._surrogate = 0;
        /*SL:363*/if (a1 < 56320 || a1 > 57343) {
            /*SL:364*/throw new IOException("Broken surrogate pair: first char 0x" + Integer.toHexString(v1) + ", second 0x" + Integer.toHexString(a1) + "; illegal combination");
        }
        /*SL:366*/return 65536 + (v1 - 55296 << 10) + (a1 - 56320);
    }
    
    protected static void illegalSurrogate(final int a1) throws IOException {
        /*SL:370*/throw new IOException(illegalSurrogateDesc(a1));
    }
    
    protected static String illegalSurrogateDesc(final int a1) {
        /*SL:375*/if (a1 > 1114111) {
            /*SL:376*/return "Illegal character point (0x" + Integer.toHexString(a1) + ") to output; max is 0x10FFFF as per RFC 4627";
        }
        /*SL:378*/if (a1 < 55296) {
            /*SL:385*/return "Illegal character point (0x" + Integer.toHexString(a1) + ") to output";
        }
        if (a1 <= 56319) {
            return "Unmatched first part of surrogate pair (0x" + Integer.toHexString(a1) + ")";
        }
        return "Unmatched second part of surrogate pair (0x" + Integer.toHexString(a1) + ")";
    }
}
