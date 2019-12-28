package com.google.api.client.repackaged.org.apache.commons.codec.binary;

import java.math.BigInteger;

public class Base64 extends BaseNCodec
{
    private static final int BITS_PER_ENCODED_BYTE = 6;
    private static final int BYTES_PER_UNENCODED_BLOCK = 3;
    private static final int BYTES_PER_ENCODED_BLOCK = 4;
    static final byte[] CHUNK_SEPARATOR;
    private static final byte[] STANDARD_ENCODE_TABLE;
    private static final byte[] URL_SAFE_ENCODE_TABLE;
    private static final byte[] DECODE_TABLE;
    private static final int MASK_6BITS = 63;
    private final byte[] encodeTable;
    private final byte[] decodeTable;
    private final byte[] lineSeparator;
    private final int decodeSize;
    private final int encodeSize;
    private int bitWorkArea;
    
    public Base64() {
        this(0);
    }
    
    public Base64(final boolean a1) {
        this(76, Base64.CHUNK_SEPARATOR, a1);
    }
    
    public Base64(final int a1) {
        this(a1, Base64.CHUNK_SEPARATOR);
    }
    
    public Base64(final int a1, final byte[] a2) {
        this(a1, a2, false);
    }
    
    public Base64(final int a3, final byte[] v1, final boolean v2) {
        super(3, 4, a3, (v1 == null) ? 0 : v1.length);
        this.decodeTable = Base64.DECODE_TABLE;
        if (v1 != null) {
            if (this.containsAlphabetOrPad(v1)) {
                final String a4 = StringUtils.newStringUtf8(v1);
                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + a4 + "]");
            }
            if (a3 > 0) {
                this.encodeSize = 4 + v1.length;
                System.arraycopy(v1, 0, this.lineSeparator = new byte[v1.length], 0, v1.length);
            }
            else {
                this.encodeSize = 4;
                this.lineSeparator = null;
            }
        }
        else {
            this.encodeSize = 4;
            this.lineSeparator = null;
        }
        this.decodeSize = this.encodeSize - 1;
        this.encodeTable = (v2 ? Base64.URL_SAFE_ENCODE_TABLE : Base64.STANDARD_ENCODE_TABLE);
    }
    
    public boolean isUrlSafe() {
        /*SL:305*/return this.encodeTable == Base64.URL_SAFE_ENCODE_TABLE;
    }
    
    void encode(final byte[] v2, int v3, final int v4) {
        /*SL:328*/if (this.eof) {
            /*SL:329*/return;
        }
        /*SL:333*/if (v4 < 0) {
            /*SL:334*/this.eof = true;
            /*SL:335*/if (0 == this.modulus && this.lineLength == 0) {
                /*SL:336*/return;
            }
            /*SL:338*/this.ensureBufferSize(this.encodeSize);
            final int a1 = /*EL:339*/this.pos;
            /*SL:340*/switch (this.modulus) {
                case 1: {
                    /*SL:342*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea >> 2 & 0x3F];
                    /*SL:343*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea << 4 & 0x3F];
                    /*SL:345*/if (this.encodeTable == Base64.STANDARD_ENCODE_TABLE) {
                        /*SL:346*/this.buffer[this.pos++] = 61;
                        /*SL:347*/this.buffer[this.pos++] = 61;
                        break;
                    }
                    break;
                }
                case 2: {
                    /*SL:352*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea >> 10 & 0x3F];
                    /*SL:353*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea >> 4 & 0x3F];
                    /*SL:354*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea << 2 & 0x3F];
                    /*SL:356*/if (this.encodeTable == Base64.STANDARD_ENCODE_TABLE) {
                        /*SL:357*/this.buffer[this.pos++] = 61;
                        break;
                    }
                    break;
                }
            }
            /*SL:361*/this.currentLinePos += this.pos - a1;
            /*SL:363*/if (this.lineLength > 0 && this.currentLinePos > 0) {
                /*SL:364*/System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
                /*SL:365*/this.pos += this.lineSeparator.length;
            }
        }
        else {
            /*SL:368*/for (int a2 = 0; a2 < v4; ++a2) {
                /*SL:369*/this.ensureBufferSize(this.encodeSize);
                /*SL:370*/this.modulus = (this.modulus + 1) % 3;
                int a3 = /*EL:371*/v2[v3++];
                /*SL:372*/if (a3 < 0) {
                    /*SL:373*/a3 += 256;
                }
                /*SL:375*/this.bitWorkArea = (this.bitWorkArea << 8) + a3;
                /*SL:376*/if (0 == this.modulus) {
                    /*SL:377*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea >> 18 & 0x3F];
                    /*SL:378*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea >> 12 & 0x3F];
                    /*SL:379*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea >> 6 & 0x3F];
                    /*SL:380*/this.buffer[this.pos++] = this.encodeTable[this.bitWorkArea & 0x3F];
                    /*SL:381*/this.currentLinePos += 4;
                    /*SL:382*/if (this.lineLength > 0 && this.lineLength <= this.currentLinePos) {
                        /*SL:383*/System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
                        /*SL:384*/this.pos += this.lineSeparator.length;
                        /*SL:385*/this.currentLinePos = 0;
                    }
                }
            }
        }
    }
    
    void decode(final byte[] v2, int v3, final int v4) {
        /*SL:417*/if (this.eof) {
            /*SL:418*/return;
        }
        /*SL:420*/if (v4 < 0) {
            /*SL:421*/this.eof = true;
        }
        /*SL:423*/for (int a3 = 0; a3 < v4; ++a3) {
            /*SL:424*/this.ensureBufferSize(this.decodeSize);
            final byte a2 = /*EL:425*/v2[v3++];
            /*SL:426*/if (a2 == 61) {
                /*SL:428*/this.eof = true;
                /*SL:429*/break;
            }
            /*SL:431*/if (a2 >= 0 && a2 < Base64.DECODE_TABLE.length) {
                /*SL:432*/a3 = Base64.DECODE_TABLE[a2];
                /*SL:433*/if (a3 >= 0) {
                    /*SL:434*/this.modulus = (this.modulus + 1) % 4;
                    /*SL:435*/this.bitWorkArea = (this.bitWorkArea << 6) + a3;
                    /*SL:436*/if (this.modulus == 0) {
                        /*SL:437*/this.buffer[this.pos++] = (byte)(this.bitWorkArea >> 16 & 0xFF);
                        /*SL:438*/this.buffer[this.pos++] = (byte)(this.bitWorkArea >> 8 & 0xFF);
                        /*SL:439*/this.buffer[this.pos++] = (byte)(this.bitWorkArea & 0xFF);
                    }
                }
            }
        }
        /*SL:449*/if (this.eof && this.modulus != 0) {
            /*SL:450*/this.ensureBufferSize(this.decodeSize);
            /*SL:454*/switch (this.modulus) {
                case 2: {
                    /*SL:458*/this.bitWorkArea >>= 4;
                    /*SL:459*/this.buffer[this.pos++] = (byte)(this.bitWorkArea & 0xFF);
                    /*SL:460*/break;
                }
                case 3: {
                    /*SL:462*/this.bitWorkArea >>= 2;
                    /*SL:463*/this.buffer[this.pos++] = (byte)(this.bitWorkArea >> 8 & 0xFF);
                    /*SL:464*/this.buffer[this.pos++] = (byte)(this.bitWorkArea & 0xFF);
                    break;
                }
            }
        }
    }
    
    public static boolean isArrayByteBase64(final byte[] a1) {
        /*SL:481*/return isBase64(a1);
    }
    
    public static boolean isBase64(final byte a1) {
        /*SL:493*/return a1 == 61 || (a1 >= 0 && a1 < Base64.DECODE_TABLE.length && Base64.DECODE_TABLE[a1] != -1);
    }
    
    public static boolean isBase64(final String a1) {
        /*SL:507*/return isBase64(StringUtils.getBytesUtf8(a1));
    }
    
    public static boolean isBase64(final byte[] v1) {
        /*SL:521*/for (int a1 = 0; a1 < v1.length; ++a1) {
            /*SL:522*/if (!isBase64(v1[a1]) && !BaseNCodec.isWhiteSpace(v1[a1])) {
                /*SL:523*/return false;
            }
        }
        /*SL:526*/return true;
    }
    
    public static byte[] encodeBase64(final byte[] a1) {
        /*SL:537*/return encodeBase64(a1, false);
    }
    
    public static String encodeBase64String(final byte[] a1) {
        /*SL:552*/return StringUtils.newStringUtf8(encodeBase64(a1, false));
    }
    
    public static byte[] encodeBase64URLSafe(final byte[] a1) {
        /*SL:565*/return encodeBase64(a1, false, true);
    }
    
    public static String encodeBase64URLSafeString(final byte[] a1) {
        /*SL:578*/return StringUtils.newStringUtf8(encodeBase64(a1, false, true));
    }
    
    public static byte[] encodeBase64Chunked(final byte[] a1) {
        /*SL:589*/return encodeBase64(a1, true);
    }
    
    public static byte[] encodeBase64(final byte[] a1, final boolean a2) {
        /*SL:604*/return encodeBase64(a1, a2, false);
    }
    
    public static byte[] encodeBase64(final byte[] a1, final boolean a2, final boolean a3) {
        /*SL:622*/return encodeBase64(a1, a2, a3, Integer.MAX_VALUE);
    }
    
    public static byte[] encodeBase64(final byte[] a1, final boolean a2, final boolean a3, final int a4) {
        /*SL:642*/if (a1 == null || a1.length == 0) {
            /*SL:643*/return a1;
        }
        final Base64 v1 = /*EL:648*/a2 ? new Base64(a3) : new Base64(0, Base64.CHUNK_SEPARATOR, a3);
        final long v2 = /*EL:649*/v1.getEncodedLength(a1);
        /*SL:650*/if (v2 > a4) {
            /*SL:651*/throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + v2 + ") than the specified maximum size of " + a4);
        }
        /*SL:657*/return v1.encode(a1);
    }
    
    public static byte[] decodeBase64(final String a1) {
        /*SL:669*/return new Base64().decode(a1);
    }
    
    public static byte[] decodeBase64(final byte[] a1) {
        /*SL:680*/return new Base64().decode(a1);
    }
    
    public static BigInteger decodeInteger(final byte[] a1) {
        /*SL:695*/return new BigInteger(1, decodeBase64(a1));
    }
    
    public static byte[] encodeInteger(final BigInteger a1) {
        /*SL:709*/if (a1 == null) {
            /*SL:710*/throw new NullPointerException("encodeInteger called with null parameter");
        }
        /*SL:712*/return encodeBase64(toIntegerBytes(a1), false);
    }
    
    static byte[] toIntegerBytes(final BigInteger a1) {
        int v1 = /*EL:723*/a1.bitLength();
        /*SL:725*/v1 = v1 + 7 >> 3 << 3;
        final byte[] v2 = /*EL:726*/a1.toByteArray();
        /*SL:728*/if (a1.bitLength() % 8 != 0 && a1.bitLength() / 8 + 1 == v1 / 8) {
            /*SL:729*/return v2;
        }
        int v3 = /*EL:732*/0;
        int v4 = /*EL:733*/v2.length;
        /*SL:736*/if (a1.bitLength() % 8 == 0) {
            /*SL:737*/v3 = 1;
            /*SL:738*/--v4;
        }
        final int v5 = /*EL:740*/v1 / 8 - v4;
        final byte[] v6 = /*EL:741*/new byte[v1 / 8];
        /*SL:742*/System.arraycopy(v2, v3, v6, v5, v4);
        /*SL:743*/return v6;
    }
    
    protected boolean isInAlphabet(final byte a1) {
        /*SL:755*/return a1 >= 0 && a1 < this.decodeTable.length && this.decodeTable[a1] != -1;
    }
    
    static {
        CHUNK_SEPARATOR = new byte[] { 13, 10 };
        STANDARD_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        URL_SAFE_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
        DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
    }
}
