package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.BufferRecyclers;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.TextBuffer;

public final class JsonStringEncoder
{
    private static final char[] HC;
    private static final byte[] HB;
    private static final int SURR1_FIRST = 55296;
    private static final int SURR1_LAST = 56319;
    private static final int SURR2_FIRST = 56320;
    private static final int SURR2_LAST = 57343;
    protected TextBuffer _text;
    protected ByteArrayBuilder _bytes;
    protected final char[] _qbuf;
    
    public JsonStringEncoder() {
        (this._qbuf = new char[6])[0] = '\\';
        this._qbuf[2] = '0';
        this._qbuf[3] = '0';
    }
    
    @Deprecated
    public static JsonStringEncoder getInstance() {
        /*SL:71*/return BufferRecyclers.getJsonStringEncoder();
    }
    
    public char[] quoteAsString(final String v-10) {
        TextBuffer text = /*EL:86*/this._text;
        /*SL:87*/if (text == null) {
            /*SL:89*/text = (this._text = new TextBuffer(null));
        }
        char[] array = /*EL:91*/text.emptyAndGetCurrentSegment();
        final int[] get7BitOutputEscapes = /*EL:92*/CharTypes.get7BitOutputEscapes();
        final int length = /*EL:93*/get7BitOutputEscapes.length;
        int i = /*EL:94*/0;
        final int length2 = /*EL:95*/v-10.length();
        int currentLength = /*EL:96*/0;
        /*SL:99*/Label_0261:
        while (i < length2) {
            while (true) {
                final char a1 = /*EL:102*/v-10.charAt(i);
                /*SL:103*/if (a1 < length && get7BitOutputEscapes[a1] != 0) {
                    final char char1 = /*EL:116*/v-10.charAt(i++);
                    final int a2 = /*EL:117*/get7BitOutputEscapes[char1];
                    final int v0 = /*EL:118*/(a2 < 0) ? this._appendNumeric(char1, this._qbuf) : this._appendNamed(a2, this._qbuf);
                    /*SL:122*/if (currentLength + v0 > array.length) {
                        final int v = /*EL:123*/array.length - currentLength;
                        /*SL:124*/if (v > 0) {
                            /*SL:125*/System.arraycopy(this._qbuf, 0, array, currentLength, v);
                        }
                        /*SL:127*/array = text.finishCurrentSegment();
                        final int v2 = /*EL:128*/v0 - v;
                        /*SL:129*/System.arraycopy(this._qbuf, v, array, 0, v2);
                        /*SL:130*/currentLength = v2;
                    }
                    else {
                        /*SL:132*/System.arraycopy(this._qbuf, 0, array, currentLength, v0);
                        /*SL:133*/currentLength += v0;
                    }
                    /*SL:135*/break;
                }
                if (currentLength >= array.length) {
                    array = text.finishCurrentSegment();
                    currentLength = 0;
                }
                array[currentLength++] = a1;
                if (++i >= length2) {
                    break Label_0261;
                }
            }
        }
        /*SL:136*/text.setCurrentLength(currentLength);
        /*SL:137*/return text.contentsAsArray();
    }
    
    public void quoteAsString(final CharSequence v-6, final StringBuilder v-5) {
        final int[] get7BitOutputEscapes = /*EL:149*/CharTypes.get7BitOutputEscapes();
        final int length = /*EL:150*/get7BitOutputEscapes.length;
        int i = /*EL:151*/0;
        final int length2 = /*EL:152*/v-6.length();
        /*SL:155*/Label_0140:
        while (i < length2) {
            while (true) {
                final char a1 = /*EL:158*/v-6.charAt(i);
                /*SL:159*/if (a1 < length && get7BitOutputEscapes[a1] != 0) {
                    final char a2 = /*EL:168*/v-6.charAt(i++);
                    final int v1 = /*EL:169*/get7BitOutputEscapes[a2];
                    final int v2 = /*EL:170*/(v1 < 0) ? this._appendNumeric(a2, this._qbuf) : this._appendNamed(v1, this._qbuf);
                    /*SL:174*/v-5.append(this._qbuf, 0, v2);
                    /*SL:175*/break;
                }
                v-5.append(a1);
                if (++i >= length2) {
                    break Label_0140;
                }
            }
        }
    }
    
    public byte[] quoteAsUTF8(final String v-7) {
        ByteArrayBuilder bytes = /*EL:185*/this._bytes;
        /*SL:186*/if (bytes == null) {
            /*SL:188*/bytes = (this._bytes = new ByteArrayBuilder(null));
        }
        int i = /*EL:190*/0;
        final int length = /*EL:191*/v-7.length();
        int appendByte = /*EL:192*/0;
        byte[] array = /*EL:193*/bytes.resetAndGetFirstSegment();
        /*SL:196*/Label_0492:
        while (i < length) {
            final int[] get7BitOutputEscapes = /*EL:197*/CharTypes.get7BitOutputEscapes();
            while (true) {
                final int a1 = /*EL:201*/v-7.charAt(i);
                if (/*EL:202*/a1 <= 127 && get7BitOutputEscapes[a1] == 0) {
                    /*SL:205*/if (appendByte >= array.length) {
                        /*SL:206*/array = bytes.finishCurrentSegment();
                        /*SL:207*/appendByte = 0;
                    }
                    /*SL:209*/array[appendByte++] = (byte)a1;
                    /*SL:210*/if (++i >= length) {
                        break Label_0492;
                    }
                    /*SL:213*/continue;
                }
                else {
                    /*SL:214*/if (appendByte >= array.length) {
                        /*SL:215*/array = bytes.finishCurrentSegment();
                        /*SL:216*/appendByte = 0;
                    }
                    int v0 = /*EL:219*/v-7.charAt(i++);
                    /*SL:220*/if (v0 <= 127) {
                        final int v = /*EL:221*/get7BitOutputEscapes[v0];
                        /*SL:223*/appendByte = this._appendByte(v0, v, bytes, appendByte);
                        /*SL:224*/array = bytes.getCurrentSegment();
                        /*SL:225*/break;
                    }
                    /*SL:227*/if (v0 <= 2047) {
                        /*SL:228*/array[appendByte++] = (byte)(0xC0 | v0 >> 6);
                        /*SL:229*/v0 = (0x80 | (v0 & 0x3F));
                    }
                    else/*SL:232*/ if (v0 < 55296 || v0 > 57343) {
                        /*SL:233*/array[appendByte++] = (byte)(0xE0 | v0 >> 12);
                        /*SL:234*/if (appendByte >= array.length) {
                            /*SL:235*/array = bytes.finishCurrentSegment();
                            /*SL:236*/appendByte = 0;
                        }
                        /*SL:238*/array[appendByte++] = (byte)(0x80 | (v0 >> 6 & 0x3F));
                        /*SL:239*/v0 = (0x80 | (v0 & 0x3F));
                    }
                    else {
                        /*SL:241*/if (v0 > 56319) {
                            _illegal(/*EL:242*/v0);
                        }
                        /*SL:245*/if (i >= length) {
                            _illegal(/*EL:246*/v0);
                        }
                        /*SL:248*/v0 = _convert(v0, v-7.charAt(i++));
                        /*SL:249*/if (v0 > 1114111) {
                            _illegal(/*EL:250*/v0);
                        }
                        /*SL:252*/array[appendByte++] = (byte)(0xF0 | v0 >> 18);
                        /*SL:253*/if (appendByte >= array.length) {
                            /*SL:254*/array = bytes.finishCurrentSegment();
                            /*SL:255*/appendByte = 0;
                        }
                        /*SL:257*/array[appendByte++] = (byte)(0x80 | (v0 >> 12 & 0x3F));
                        /*SL:258*/if (appendByte >= array.length) {
                            /*SL:259*/array = bytes.finishCurrentSegment();
                            /*SL:260*/appendByte = 0;
                        }
                        /*SL:262*/array[appendByte++] = (byte)(0x80 | (v0 >> 6 & 0x3F));
                        /*SL:263*/v0 = (0x80 | (v0 & 0x3F));
                    }
                    /*SL:266*/if (appendByte >= array.length) {
                        /*SL:267*/array = bytes.finishCurrentSegment();
                        /*SL:268*/appendByte = 0;
                    }
                    /*SL:270*/array[appendByte++] = (byte)v0;
                    /*SL:271*/break;
                }
            }
        }
        /*SL:272*/return this._bytes.completeAndCoalesce(appendByte);
    }
    
    public byte[] encodeAsUTF8(final String v2) {
        ByteArrayBuilder v3 = /*EL:282*/this._bytes;
        /*SL:283*/if (v3 == null) {
            /*SL:285*/v3 = (this._bytes = new ByteArrayBuilder(null));
        }
        int v4 = /*EL:287*/0;
        final int v5 = /*EL:288*/v2.length();
        int v6 = /*EL:289*/0;
        byte[] v7 = /*EL:290*/v3.resetAndGetFirstSegment();
        int v8 = /*EL:291*/v7.length;
        /*SL:294*/Label_0443:
        while (v4 < v5) {
            int a1;
            /*SL:298*/for (a1 = v2.charAt(v4++); a1 <= 127; /*SL:308*/a1 = v2.charAt(v4++)) {
                if (v6 >= v8) {
                    v7 = v3.finishCurrentSegment();
                    v8 = v7.length;
                    v6 = 0;
                }
                v7[v6++] = (byte)a1;
                if (v4 >= v5) {
                    break Label_0443;
                }
            }
            /*SL:312*/if (v6 >= v8) {
                /*SL:313*/v7 = v3.finishCurrentSegment();
                /*SL:314*/v8 = v7.length;
                /*SL:315*/v6 = 0;
            }
            /*SL:317*/if (a1 < 2048) {
                /*SL:318*/v7[v6++] = (byte)(0xC0 | a1 >> 6);
            }
            else/*SL:321*/ if (a1 < 55296 || a1 > 57343) {
                /*SL:322*/v7[v6++] = (byte)(0xE0 | a1 >> 12);
                /*SL:323*/if (v6 >= v8) {
                    /*SL:324*/v7 = v3.finishCurrentSegment();
                    /*SL:325*/v8 = v7.length;
                    /*SL:326*/v6 = 0;
                }
                /*SL:328*/v7[v6++] = (byte)(0x80 | (a1 >> 6 & 0x3F));
            }
            else {
                /*SL:330*/if (a1 > 56319) {
                    _illegal(/*EL:331*/a1);
                }
                /*SL:334*/if (v4 >= v5) {
                    _illegal(/*EL:335*/a1);
                }
                /*SL:337*/a1 = _convert(a1, v2.charAt(v4++));
                /*SL:338*/if (a1 > 1114111) {
                    _illegal(/*EL:339*/a1);
                }
                /*SL:341*/v7[v6++] = (byte)(0xF0 | a1 >> 18);
                /*SL:342*/if (v6 >= v8) {
                    /*SL:343*/v7 = v3.finishCurrentSegment();
                    /*SL:344*/v8 = v7.length;
                    /*SL:345*/v6 = 0;
                }
                /*SL:347*/v7[v6++] = (byte)(0x80 | (a1 >> 12 & 0x3F));
                /*SL:348*/if (v6 >= v8) {
                    /*SL:349*/v7 = v3.finishCurrentSegment();
                    /*SL:350*/v8 = v7.length;
                    /*SL:351*/v6 = 0;
                }
                /*SL:353*/v7[v6++] = (byte)(0x80 | (a1 >> 6 & 0x3F));
            }
            /*SL:356*/if (v6 >= v8) {
                /*SL:357*/v7 = v3.finishCurrentSegment();
                /*SL:358*/v8 = v7.length;
                /*SL:359*/v6 = 0;
            }
            /*SL:361*/v7[v6++] = (byte)(0x80 | (a1 & 0x3F));
        }
        /*SL:363*/return this._bytes.completeAndCoalesce(v6);
    }
    
    private int _appendNumeric(final int a1, final char[] a2) {
        /*SL:373*/a2[1] = 'u';
        /*SL:375*/a2[4] = JsonStringEncoder.HC[a1 >> 4];
        /*SL:376*/a2[5] = JsonStringEncoder.HC[a1 & 0xF];
        /*SL:377*/return 6;
    }
    
    private int _appendNamed(final int a1, final char[] a2) {
        /*SL:381*/a2[1] = (char)a1;
        /*SL:382*/return 2;
    }
    
    private int _appendByte(int a3, final int a4, final ByteArrayBuilder v1, final int v2) {
        /*SL:387*/v1.setCurrentSegmentLength(v2);
        /*SL:388*/v1.append(92);
        /*SL:389*/if (a4 < 0) {
            /*SL:390*/v1.append(117);
            /*SL:391*/if (a3 > 255) {
                final int a5 = /*EL:392*/a3 >> 8;
                /*SL:393*/v1.append(JsonStringEncoder.HB[a5 >> 4]);
                /*SL:394*/v1.append(JsonStringEncoder.HB[a5 & 0xF]);
                /*SL:395*/a3 &= 0xFF;
            }
            else {
                /*SL:397*/v1.append(48);
                /*SL:398*/v1.append(48);
            }
            /*SL:400*/v1.append(JsonStringEncoder.HB[a3 >> 4]);
            /*SL:401*/v1.append(JsonStringEncoder.HB[a3 & 0xF]);
        }
        else {
            /*SL:403*/v1.append((byte)a4);
        }
        /*SL:405*/return v1.getCurrentSegmentLength();
    }
    
    private static int _convert(final int a1, final int a2) {
        /*SL:410*/if (a2 < 56320 || a2 > 57343) {
            /*SL:411*/throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(a1) + ", second 0x" + Integer.toHexString(a2) + "; illegal combination");
        }
        /*SL:413*/return 65536 + (a1 - 55296 << 10) + (a2 - 56320);
    }
    
    private static void _illegal(final int a1) {
        /*SL:417*/throw new IllegalArgumentException(UTF8Writer.illegalSurrogateDesc(a1));
    }
    
    static {
        HC = CharTypes.copyHexChars();
        HB = CharTypes.copyHexBytes();
    }
}
