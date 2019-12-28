package com.fasterxml.jackson.core.json;

import java.io.CharConversionException;
import com.fasterxml.jackson.core.format.MatchStrength;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.UTF32Reader;
import java.io.InputStreamReader;
import com.fasterxml.jackson.core.io.MergedStream;
import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.DataInput;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonEncoding;
import java.io.InputStream;
import com.fasterxml.jackson.core.io.IOContext;

public final class ByteSourceJsonBootstrapper
{
    public static final byte UTF8_BOM_1 = -17;
    public static final byte UTF8_BOM_2 = -69;
    public static final byte UTF8_BOM_3 = -65;
    private final IOContext _context;
    private final InputStream _in;
    private final byte[] _inputBuffer;
    private int _inputPtr;
    private int _inputEnd;
    private final boolean _bufferRecyclable;
    private boolean _bigEndian;
    private int _bytesPerChar;
    
    public ByteSourceJsonBootstrapper(final IOContext a1, final InputStream a2) {
        this._bigEndian = true;
        this._context = a1;
        this._in = a2;
        this._inputBuffer = a1.allocReadIOBuffer();
        final boolean b = false;
        this._inputPtr = (b ? 1 : 0);
        this._inputEnd = (b ? 1 : 0);
        this._bufferRecyclable = true;
    }
    
    public ByteSourceJsonBootstrapper(final IOContext a1, final byte[] a2, final int a3, final int a4) {
        this._bigEndian = true;
        this._context = a1;
        this._in = null;
        this._inputBuffer = a2;
        this._inputPtr = a3;
        this._inputEnd = a3 + a4;
        this._bufferRecyclable = false;
    }
    
    public JsonEncoding detectEncoding() throws IOException {
        boolean v0 = /*EL:120*/false;
        /*SL:129*/if (this.ensureLoaded(4)) {
            final int v = /*EL:130*/this._inputBuffer[this._inputPtr] << 24 | (this._inputBuffer[this._inputPtr + 1] & 0xFF) << 16 | (this._inputBuffer[this._inputPtr + 2] & 0xFF) << 8 | (this._inputBuffer[this._inputPtr + 3] & 0xFF);
            /*SL:135*/if (this.handleBOM(v)) {
                /*SL:136*/v0 = true;
            }
            else/*SL:144*/ if (this.checkUTF32(v)) {
                /*SL:145*/v0 = true;
            }
            else/*SL:146*/ if (this.checkUTF16(v >>> 16)) {
                /*SL:147*/v0 = true;
            }
        }
        else/*SL:150*/ if (this.ensureLoaded(2)) {
            final int v = /*EL:151*/(this._inputBuffer[this._inputPtr] & 0xFF) << 8 | (this._inputBuffer[this._inputPtr + 1] & 0xFF);
            /*SL:153*/if (this.checkUTF16(v)) {
                /*SL:154*/v0 = true;
            }
        }
        JsonEncoding v2 = null;
        /*SL:161*/if (!v0) {
            /*SL:162*/v2 = JsonEncoding.UTF8;
        }
        else {
            /*SL:164*/switch (this._bytesPerChar) {
                case 1: {
                    /*SL:165*/v2 = JsonEncoding.UTF8;
                    /*SL:166*/break;
                }
                case 2: {
                    /*SL:167*/v2 = (this._bigEndian ? JsonEncoding.UTF16_BE : JsonEncoding.UTF16_LE);
                    /*SL:168*/break;
                }
                case 4: {
                    /*SL:169*/v2 = (this._bigEndian ? JsonEncoding.UTF32_BE : JsonEncoding.UTF32_LE);
                    /*SL:170*/break;
                }
                default: {
                    /*SL:171*/throw new RuntimeException("Internal error");
                }
            }
        }
        /*SL:174*/this._context.setEncoding(v2);
        /*SL:175*/return v2;
    }
    
    public static int skipUTF8BOM(final DataInput a1) throws IOException {
        int v1 = /*EL:187*/a1.readUnsignedByte();
        /*SL:188*/if (v1 != 239) {
            /*SL:189*/return v1;
        }
        /*SL:193*/v1 = a1.readUnsignedByte();
        /*SL:194*/if (v1 != 187) {
            /*SL:195*/throw new IOException("Unexpected byte 0x" + Integer.toHexString(v1) + " following 0xEF; should get 0xBB as part of UTF-8 BOM");
        }
        /*SL:198*/v1 = a1.readUnsignedByte();
        /*SL:199*/if (v1 != 191) {
            /*SL:200*/throw new IOException("Unexpected byte 0x" + Integer.toHexString(v1) + " following 0xEF 0xBB; should get 0xBF as part of UTF-8 BOM");
        }
        /*SL:203*/return a1.readUnsignedByte();
    }
    
    public Reader constructReader() throws IOException {
        final JsonEncoding v0 = /*EL:215*/this._context.getEncoding();
        /*SL:216*/switch (v0.bits()) {
            case 8:
            case 16: {
                InputStream v = /*EL:221*/this._in;
                /*SL:223*/if (v == null) {
                    /*SL:224*/v = new ByteArrayInputStream(this._inputBuffer, this._inputPtr, this._inputEnd);
                }
                else/*SL:229*/ if (this._inputPtr < this._inputEnd) {
                    /*SL:230*/v = new MergedStream(this._context, v, this._inputBuffer, this._inputPtr, this._inputEnd);
                }
                /*SL:233*/return new InputStreamReader(v, v0.getJavaName());
            }
            case 32: {
                /*SL:236*/return new UTF32Reader(this._context, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._context.getEncoding().isBigEndian());
            }
            default: {
                /*SL:239*/throw new RuntimeException("Internal error");
            }
        }
    }
    
    public JsonParser constructParser(final int a3, final ObjectCodec a4, final ByteQuadsCanonicalizer a5, final CharsToNameCanonicalizer v1, final int v2) throws IOException {
        final JsonEncoding v3 = /*EL:246*/this.detectEncoding();
        /*SL:248*/if (v3 == JsonEncoding.UTF8 && JsonFactory.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(/*EL:252*/v2)) {
            final ByteQuadsCanonicalizer a6 = /*EL:253*/a5.makeChild(v2);
            /*SL:254*/return new UTF8StreamJsonParser(this._context, a3, this._in, a4, a6, this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
        }
        /*SL:258*/return new ReaderBasedJsonParser(this._context, a3, this.constructReader(), a4, v1.makeChild(v2));
    }
    
    public static MatchStrength hasJSONFormat(final InputAccessor a1) throws IOException {
        /*SL:279*/if (!a1.hasMoreBytes()) {
            /*SL:280*/return MatchStrength.INCONCLUSIVE;
        }
        byte v1 = /*EL:282*/a1.nextByte();
        /*SL:284*/if (v1 == -17) {
            /*SL:285*/if (!a1.hasMoreBytes()) {
                /*SL:286*/return MatchStrength.INCONCLUSIVE;
            }
            /*SL:288*/if (a1.nextByte() != -69) {
                /*SL:289*/return MatchStrength.NO_MATCH;
            }
            /*SL:291*/if (!a1.hasMoreBytes()) {
                /*SL:292*/return MatchStrength.INCONCLUSIVE;
            }
            /*SL:294*/if (a1.nextByte() != -65) {
                /*SL:295*/return MatchStrength.NO_MATCH;
            }
            /*SL:297*/if (!a1.hasMoreBytes()) {
                /*SL:298*/return MatchStrength.INCONCLUSIVE;
            }
            /*SL:300*/v1 = a1.nextByte();
        }
        int v2 = skipSpace(/*EL:303*/a1, v1);
        /*SL:304*/if (v2 < 0) {
            /*SL:305*/return MatchStrength.INCONCLUSIVE;
        }
        /*SL:308*/if (v2 == 123) {
            /*SL:310*/v2 = skipSpace(a1);
            /*SL:311*/if (v2 < 0) {
                /*SL:312*/return MatchStrength.INCONCLUSIVE;
            }
            /*SL:314*/if (v2 == 34 || v2 == 125) {
                /*SL:315*/return MatchStrength.SOLID_MATCH;
            }
            /*SL:318*/return MatchStrength.NO_MATCH;
        }
        else/*SL:322*/ if (v2 == 91) {
            /*SL:323*/v2 = skipSpace(a1);
            /*SL:324*/if (v2 < 0) {
                /*SL:325*/return MatchStrength.INCONCLUSIVE;
            }
            /*SL:328*/if (v2 == 93 || v2 == 91) {
                /*SL:329*/return MatchStrength.SOLID_MATCH;
            }
            /*SL:331*/return MatchStrength.SOLID_MATCH;
        }
        else {
            final MatchStrength v3 = MatchStrength.WEAK_MATCH;
            /*SL:337*/if (v2 == 34) {
                /*SL:338*/return v3;
            }
            /*SL:340*/if (v2 <= 57 && v2 >= 48) {
                /*SL:341*/return v3;
            }
            /*SL:343*/if (v2 == 45) {
                /*SL:344*/v2 = skipSpace(a1);
                /*SL:345*/if (v2 < 0) {
                    /*SL:346*/return MatchStrength.INCONCLUSIVE;
                }
                /*SL:348*/return (v2 <= 57 && v2 >= 48) ? v3 : MatchStrength.NO_MATCH;
            }
            else {
                /*SL:351*/if (v2 == 110) {
                    /*SL:352*/return tryMatch(a1, "ull", v3);
                }
                /*SL:354*/if (v2 == 116) {
                    /*SL:355*/return tryMatch(a1, "rue", v3);
                }
                /*SL:357*/if (v2 == 102) {
                    /*SL:358*/return tryMatch(a1, "alse", v3);
                }
                /*SL:360*/return MatchStrength.NO_MATCH;
            }
        }
    }
    
    private static MatchStrength tryMatch(final InputAccessor a3, final String v1, final MatchStrength v2) throws IOException {
        /*SL:366*/for (int a4 = 0, a5 = v1.length(); a4 < a5; ++a4) {
            /*SL:367*/if (!a3.hasMoreBytes()) {
                /*SL:368*/return MatchStrength.INCONCLUSIVE;
            }
            /*SL:370*/if (a3.nextByte() != v1.charAt(a4)) {
                /*SL:371*/return MatchStrength.NO_MATCH;
            }
        }
        /*SL:374*/return v2;
    }
    
    private static int skipSpace(final InputAccessor a1) throws IOException {
        /*SL:379*/if (!a1.hasMoreBytes()) {
            /*SL:380*/return -1;
        }
        /*SL:382*/return skipSpace(a1, a1.nextByte());
    }
    
    private static int skipSpace(final InputAccessor a2, byte v1) throws IOException {
        while (true) {
            final int a3 = /*EL:388*/v1 & 0xFF;
            /*SL:389*/if (a3 != 32 && a3 != 13 && a3 != 10 && a3 != 9) {
                /*SL:390*/return a3;
            }
            /*SL:392*/if (!a2.hasMoreBytes()) {
                /*SL:393*/return -1;
            }
            /*SL:395*/v1 = a2.nextByte();
        }
    }
    
    private boolean handleBOM(final int a1) throws IOException {
        /*SL:414*/switch (a1) {
            case 65279: {
                /*SL:416*/this._bigEndian = true;
                /*SL:417*/this._inputPtr += 4;
                /*SL:418*/this._bytesPerChar = 4;
                /*SL:419*/return true;
            }
            case -131072: {
                /*SL:421*/this._inputPtr += 4;
                /*SL:422*/this._bytesPerChar = 4;
                /*SL:423*/this._bigEndian = false;
                /*SL:424*/return true;
            }
            case 65534: {
                /*SL:426*/this.reportWeirdUCS4("2143");
                /*SL:427*/break;
            }
            case -16842752: {
                /*SL:429*/this.reportWeirdUCS4("3412");
                break;
            }
        }
        final int v1 = /*EL:434*/a1 >>> 16;
        /*SL:435*/if (v1 == 65279) {
            /*SL:436*/this._inputPtr += 2;
            /*SL:437*/this._bytesPerChar = 2;
            /*SL:439*/return this._bigEndian = true;
        }
        /*SL:441*/if (v1 == 65534) {
            /*SL:442*/this._inputPtr += 2;
            /*SL:443*/this._bytesPerChar = 2;
            /*SL:444*/this._bigEndian = false;
            /*SL:445*/return true;
        }
        /*SL:448*/if (a1 >>> 8 == 15711167) {
            /*SL:449*/this._inputPtr += 3;
            /*SL:450*/this._bytesPerChar = 1;
            /*SL:452*/return this._bigEndian = true;
        }
        /*SL:454*/return false;
    }
    
    private boolean checkUTF32(final int a1) throws IOException {
        /*SL:462*/if (a1 >> 8 == 0) {
            /*SL:463*/this._bigEndian = true;
        }
        else/*SL:464*/ if ((a1 & 0xFFFFFF) == 0x0) {
            /*SL:465*/this._bigEndian = false;
        }
        else/*SL:466*/ if ((a1 & 0xFF00FFFF) == 0x0) {
            /*SL:467*/this.reportWeirdUCS4("3412");
        }
        else {
            /*SL:468*/if ((a1 & 0xFFFF00FF) != 0x0) {
                /*SL:472*/return false;
            }
            this.reportWeirdUCS4("2143");
        }
        /*SL:476*/this._bytesPerChar = 4;
        /*SL:477*/return true;
    }
    
    private boolean checkUTF16(final int a1) {
        /*SL:482*/if ((a1 & 0xFF00) == 0x0) {
            /*SL:483*/this._bigEndian = true;
        }
        else {
            /*SL:484*/if ((a1 & 0xFF) != 0x0) {
                /*SL:487*/return false;
            }
            this._bigEndian = false;
        }
        /*SL:491*/this._bytesPerChar = 2;
        /*SL:492*/return true;
    }
    
    private void reportWeirdUCS4(final String a1) throws IOException {
        /*SL:502*/throw new CharConversionException("Unsupported UCS-4 endianness (" + a1 + ") detected");
    }
    
    protected boolean ensureLoaded(final int v-1) throws IOException {
        int v = 0;
        /*SL:516*/for (int v0 = this._inputEnd - this._inputPtr; v0 < v-1; /*SL:528*/v0 += v) {
            if (this._in == null) {
                final int a1 = -1;
            }
            else {
                v = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
            }
            if (v < 1) {
                return false;
            }
            this._inputEnd += v;
        }
        /*SL:530*/return true;
    }
}
