package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import java.util.Arrays;
import java.io.Serializable;

public final class Base64Variant implements Serializable
{
    private static final int INT_SPACE = 32;
    private static final long serialVersionUID = 1L;
    static final char PADDING_CHAR_NONE = '\0';
    public static final int BASE64_VALUE_INVALID = -1;
    public static final int BASE64_VALUE_PADDING = -2;
    private final transient int[] _asciiToBase64;
    private final transient char[] _base64ToAsciiC;
    private final transient byte[] _base64ToAsciiB;
    final String _name;
    private final transient boolean _usesPadding;
    private final transient char _paddingChar;
    private final transient int _maxLineLength;
    
    public Base64Variant(final String a4, final String a5, final boolean v1, final char v2, final int v3) {
        this._asciiToBase64 = new int[128];
        this._base64ToAsciiC = new char[64];
        this._base64ToAsciiB = new byte[64];
        this._name = a4;
        this._usesPadding = v1;
        this._paddingChar = v2;
        this._maxLineLength = v3;
        final int v4 = a5.length();
        if (v4 != 64) {
            throw new IllegalArgumentException("Base64Alphabet length must be exactly 64 (was " + v4 + ")");
        }
        a5.getChars(0, v4, this._base64ToAsciiC, 0);
        Arrays.fill(this._asciiToBase64, -1);
        for (int a6 = 0; a6 < v4; ++a6) {
            final char a7 = this._base64ToAsciiC[a6];
            this._base64ToAsciiB[a6] = (byte)a7;
            this._asciiToBase64[a7] = a6;
        }
        if (v1) {
            this._asciiToBase64[v2] = -2;
        }
    }
    
    public Base64Variant(final Base64Variant a1, final String a2, final int a3) {
        this(a1, a2, a1._usesPadding, a1._paddingChar, a3);
    }
    
    public Base64Variant(final Base64Variant a1, final String a2, final boolean a3, final char a4, final int a5) {
        this._asciiToBase64 = new int[128];
        this._base64ToAsciiC = new char[64];
        this._base64ToAsciiB = new byte[64];
        this._name = a2;
        final byte[] v1 = a1._base64ToAsciiB;
        System.arraycopy(v1, 0, this._base64ToAsciiB, 0, v1.length);
        final char[] v2 = a1._base64ToAsciiC;
        System.arraycopy(v2, 0, this._base64ToAsciiC, 0, v2.length);
        final int[] v3 = a1._asciiToBase64;
        System.arraycopy(v3, 0, this._asciiToBase64, 0, v3.length);
        this._usesPadding = a3;
        this._paddingChar = a4;
        this._maxLineLength = a5;
    }
    
    protected Object readResolve() {
        /*SL:182*/return Base64Variants.valueOf(this._name);
    }
    
    public String getName() {
        /*SL:191*/return this._name;
    }
    
    public boolean usesPadding() {
        /*SL:193*/return this._usesPadding;
    }
    
    public boolean usesPaddingChar(final char a1) {
        /*SL:194*/return a1 == this._paddingChar;
    }
    
    public boolean usesPaddingChar(final int a1) {
        /*SL:195*/return a1 == this._paddingChar;
    }
    
    public char getPaddingChar() {
        /*SL:196*/return this._paddingChar;
    }
    
    public byte getPaddingByte() {
        /*SL:197*/return (byte)this._paddingChar;
    }
    
    public int getMaxLineLength() {
        /*SL:199*/return this._maxLineLength;
    }
    
    public int decodeBase64Char(final char a1) {
        /*SL:213*/return (a1 <= '\u007f') ? this._asciiToBase64[a1] : -1;
    }
    
    public int decodeBase64Char(final int a1) {
        /*SL:218*/return (a1 <= 127) ? this._asciiToBase64[a1] : -1;
    }
    
    public int decodeBase64Byte(final byte a1) {
        /*SL:225*/if (a1 < 0) {
            /*SL:226*/return -1;
        }
        /*SL:228*/return this._asciiToBase64[a1];
    }
    
    public char encodeBase64BitsAsChar(final int a1) {
        /*SL:242*/return this._base64ToAsciiC[a1];
    }
    
    public int encodeBase64Chunk(final int a1, final char[] a2, int a3) {
        /*SL:251*/a2[a3++] = this._base64ToAsciiC[a1 >> 18 & 0x3F];
        /*SL:252*/a2[a3++] = this._base64ToAsciiC[a1 >> 12 & 0x3F];
        /*SL:253*/a2[a3++] = this._base64ToAsciiC[a1 >> 6 & 0x3F];
        /*SL:254*/a2[a3++] = this._base64ToAsciiC[a1 & 0x3F];
        /*SL:255*/return a3;
    }
    
    public void encodeBase64Chunk(final StringBuilder a1, final int a2) {
        /*SL:260*/a1.append(this._base64ToAsciiC[a2 >> 18 & 0x3F]);
        /*SL:261*/a1.append(this._base64ToAsciiC[a2 >> 12 & 0x3F]);
        /*SL:262*/a1.append(this._base64ToAsciiC[a2 >> 6 & 0x3F]);
        /*SL:263*/a1.append(this._base64ToAsciiC[a2 & 0x3F]);
    }
    
    public int encodeBase64Partial(final int a1, final int a2, final char[] a3, int a4) {
        /*SL:276*/a3[a4++] = this._base64ToAsciiC[a1 >> 18 & 0x3F];
        /*SL:277*/a3[a4++] = this._base64ToAsciiC[a1 >> 12 & 0x3F];
        /*SL:278*/if (this._usesPadding) {
            /*SL:279*/a3[a4++] = ((a2 == 2) ? this._base64ToAsciiC[a1 >> 6 & 0x3F] : this._paddingChar);
            /*SL:281*/a3[a4++] = this._paddingChar;
        }
        else/*SL:283*/ if (a2 == 2) {
            /*SL:284*/a3[a4++] = this._base64ToAsciiC[a1 >> 6 & 0x3F];
        }
        /*SL:287*/return a4;
    }
    
    public void encodeBase64Partial(final StringBuilder a1, final int a2, final int a3) {
        /*SL:292*/a1.append(this._base64ToAsciiC[a2 >> 18 & 0x3F]);
        /*SL:293*/a1.append(this._base64ToAsciiC[a2 >> 12 & 0x3F]);
        /*SL:294*/if (this._usesPadding) {
            /*SL:295*/a1.append((a3 == 2) ? this._base64ToAsciiC[a2 >> 6 & 0x3F] : this._paddingChar);
            /*SL:297*/a1.append(this._paddingChar);
        }
        else/*SL:299*/ if (a3 == 2) {
            /*SL:300*/a1.append(this._base64ToAsciiC[a2 >> 6 & 0x3F]);
        }
    }
    
    public byte encodeBase64BitsAsByte(final int a1) {
        /*SL:308*/return this._base64ToAsciiB[a1];
    }
    
    public int encodeBase64Chunk(final int a1, final byte[] a2, int a3) {
        /*SL:317*/a2[a3++] = this._base64ToAsciiB[a1 >> 18 & 0x3F];
        /*SL:318*/a2[a3++] = this._base64ToAsciiB[a1 >> 12 & 0x3F];
        /*SL:319*/a2[a3++] = this._base64ToAsciiB[a1 >> 6 & 0x3F];
        /*SL:320*/a2[a3++] = this._base64ToAsciiB[a1 & 0x3F];
        /*SL:321*/return a3;
    }
    
    public int encodeBase64Partial(final int a3, final int a4, final byte[] v1, int v2) {
        /*SL:334*/v1[v2++] = this._base64ToAsciiB[a3 >> 18 & 0x3F];
        /*SL:335*/v1[v2++] = this._base64ToAsciiB[a3 >> 12 & 0x3F];
        /*SL:336*/if (this._usesPadding) {
            final byte a5 = /*EL:337*/(byte)this._paddingChar;
            /*SL:338*/v1[v2++] = ((a4 == 2) ? this._base64ToAsciiB[a3 >> 6 & 0x3F] : a5);
            /*SL:340*/v1[v2++] = a5;
        }
        else/*SL:342*/ if (a4 == 2) {
            /*SL:343*/v1[v2++] = this._base64ToAsciiB[a3 >> 6 & 0x3F];
        }
        /*SL:346*/return v2;
    }
    
    public String encode(final byte[] a1) {
        /*SL:365*/return this.encode(a1, false);
    }
    
    public String encode(final byte[] v-7, final boolean v-6) {
        final int length = /*EL:378*/v-7.length;
        final int a1 = /*EL:382*/length + (length >> 2) + (length >> 3);
        final StringBuilder sb = /*EL:383*/new StringBuilder(a1);
        /*SL:385*/if (v-6) {
            /*SL:386*/sb.append('\"');
        }
        int n = /*EL:389*/this.getMaxLineLength() >> 2;
        int i = /*EL:392*/0;
        final int n2 = /*EL:393*/length - 3;
        /*SL:395*/while (i <= n2) {
            int a2 = /*EL:397*/v-7[i++] << 8;
            /*SL:398*/a2 |= (v-7[i++] & 0xFF);
            /*SL:399*/a2 = (a2 << 8 | (v-7[i++] & 0xFF));
            /*SL:400*/this.encodeBase64Chunk(sb, a2);
            /*SL:401*/if (--n <= 0) {
                /*SL:403*/sb.append('\\');
                /*SL:404*/sb.append('n');
                /*SL:405*/n = this.getMaxLineLength() >> 2;
            }
        }
        final int v0 = /*EL:410*/length - i;
        /*SL:411*/if (v0 > 0) {
            int v = /*EL:412*/v-7[i++] << 16;
            /*SL:413*/if (v0 == 2) {
                /*SL:414*/v |= (v-7[i++] & 0xFF) << 8;
            }
            /*SL:416*/this.encodeBase64Partial(sb, v, v0);
        }
        /*SL:419*/if (v-6) {
            /*SL:420*/sb.append('\"');
        }
        /*SL:422*/return sb.toString();
    }
    
    public byte[] decode(final String a1) throws IllegalArgumentException {
        final ByteArrayBuilder v1 = /*EL:438*/new ByteArrayBuilder();
        /*SL:439*/this.decode(a1, v1);
        /*SL:440*/return v1.toByteArray();
    }
    
    public void decode(final String v-5, final ByteArrayBuilder v-4) throws IllegalArgumentException {
        int i = /*EL:458*/0;
        final int length = /*EL:459*/v-5.length();
        /*SL:466*/while (i < length) {
            char a1 = /*EL:469*/v-5.charAt(i++);
            /*SL:470*/if (a1 > ' ') {
                int a2 = /*EL:471*/this.decodeBase64Char(a1);
                /*SL:472*/if (a2 < 0) {
                    /*SL:473*/this._reportInvalidBase64(a1, 0, null);
                }
                int v1 = /*EL:475*/a2;
                /*SL:477*/if (i >= length) {
                    /*SL:478*/this._reportBase64EOF();
                }
                /*SL:480*/a1 = v-5.charAt(i++);
                /*SL:481*/a2 = this.decodeBase64Char(a1);
                /*SL:482*/if (a2 < 0) {
                    /*SL:483*/this._reportInvalidBase64(a1, 1, null);
                }
                /*SL:485*/v1 = (v1 << 6 | a2);
                /*SL:487*/if (i >= length) {
                    /*SL:489*/if (!this.usesPadding()) {
                        /*SL:490*/v1 >>= 4;
                        /*SL:491*/v-4.append(v1);
                        /*SL:492*/break;
                    }
                    /*SL:494*/this._reportBase64EOF();
                }
                /*SL:496*/a1 = v-5.charAt(i++);
                /*SL:497*/a2 = this.decodeBase64Char(a1);
                /*SL:500*/if (a2 < 0) {
                    /*SL:501*/if (a2 != -2) {
                        /*SL:502*/this._reportInvalidBase64(a1, 2, null);
                    }
                    /*SL:505*/if (i >= length) {
                        /*SL:506*/this._reportBase64EOF();
                    }
                    /*SL:508*/a1 = v-5.charAt(i++);
                    /*SL:509*/if (!this.usesPaddingChar(a1)) {
                        /*SL:510*/this._reportInvalidBase64(a1, 3, "expected padding character '" + this.getPaddingChar() + "'");
                    }
                    /*SL:513*/v1 >>= 4;
                    /*SL:514*/v-4.append(v1);
                    /*SL:515*/continue;
                }
                /*SL:518*/v1 = (v1 << 6 | a2);
                /*SL:520*/if (i >= length) {
                    /*SL:522*/if (!this.usesPadding()) {
                        /*SL:523*/v1 >>= 2;
                        /*SL:524*/v-4.appendTwoBytes(v1);
                        /*SL:525*/break;
                    }
                    /*SL:527*/this._reportBase64EOF();
                }
                /*SL:529*/a1 = v-5.charAt(i++);
                /*SL:530*/a2 = this.decodeBase64Char(a1);
                /*SL:531*/if (a2 < 0) {
                    /*SL:532*/if (a2 != -2) {
                        /*SL:533*/this._reportInvalidBase64(a1, 3, null);
                    }
                    /*SL:535*/v1 >>= 2;
                    /*SL:536*/v-4.appendTwoBytes(v1);
                }
                else {
                    /*SL:539*/v1 = (v1 << 6 | a2);
                    /*SL:540*/v-4.appendThreeBytes(v1);
                }
                /*SL:542*/continue;
            }
        }
    }
    
    @Override
    public String toString() {
        /*SL:552*/return this._name;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:557*/return a1 == this;
    }
    
    @Override
    public int hashCode() {
        /*SL:562*/return this._name.hashCode();
    }
    
    protected void _reportInvalidBase64(final char v2, final int v3, final String v4) throws IllegalArgumentException {
        String v5 = null;
        /*SL:579*/if (v2 <= ' ') {
            final String a1 = /*EL:580*/"Illegal white space character (code 0x" + Integer.toHexString(v2) + ") as character #" + (v3 + 1) + " of 4-char base64 unit: can only used between units";
        }
        else/*SL:581*/ if (this.usesPaddingChar(v2)) {
            final String a2 = /*EL:582*/"Unexpected padding character ('" + this.getPaddingChar() + "') as character #" + (v3 + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        }
        else/*SL:583*/ if (!Character.isDefined(v2) || Character.isISOControl(v2)) {
            final String a3 = /*EL:585*/"Illegal character (code 0x" + Integer.toHexString(v2) + ") in base64 content";
        }
        else {
            /*SL:587*/v5 = "Illegal character '" + v2 + "' (code 0x" + Integer.toHexString(v2) + ") in base64 content";
        }
        /*SL:589*/if (v4 != null) {
            /*SL:590*/v5 = v5 + ": " + v4;
        }
        /*SL:592*/throw new IllegalArgumentException(v5);
    }
    
    protected void _reportBase64EOF() throws IllegalArgumentException {
        /*SL:596*/throw new IllegalArgumentException("Unexpected end-of-String in base64 content");
    }
}
