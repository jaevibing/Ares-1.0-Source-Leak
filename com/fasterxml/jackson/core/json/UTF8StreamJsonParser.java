package com.fasterxml.jackson.core.json;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.Base64Variant;
import java.io.Writer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.io.OutputStream;
import com.fasterxml.jackson.core.io.IOContext;
import java.io.InputStream;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;

public class UTF8StreamJsonParser extends ParserBase
{
    static final byte BYTE_LF = 10;
    private static final int[] _icUTF8;
    protected static final int[] _icLatin1;
    protected static final int FEAT_MASK_TRAILING_COMMA;
    protected ObjectCodec _objectCodec;
    protected final ByteQuadsCanonicalizer _symbols;
    protected int[] _quadBuffer;
    protected boolean _tokenIncomplete;
    private int _quad1;
    protected int _nameStartOffset;
    protected int _nameStartRow;
    protected int _nameStartCol;
    protected InputStream _inputStream;
    protected byte[] _inputBuffer;
    protected boolean _bufferRecyclable;
    
    public UTF8StreamJsonParser(final IOContext a1, final int a2, final InputStream a3, final ObjectCodec a4, final ByteQuadsCanonicalizer a5, final byte[] a6, final int a7, final int a8, final boolean a9) {
        super(a1, a2);
        this._quadBuffer = new int[16];
        this._inputStream = a3;
        this._objectCodec = a4;
        this._symbols = a5;
        this._inputBuffer = a6;
        this._inputPtr = a7;
        this._inputEnd = a8;
        this._currInputRowStart = a7;
        this._currInputProcessed = -a7;
        this._bufferRecyclable = a9;
    }
    
    @Override
    public ObjectCodec getCodec() {
        /*SL:149*/return this._objectCodec;
    }
    
    @Override
    public void setCodec(final ObjectCodec a1) {
        /*SL:154*/this._objectCodec = a1;
    }
    
    @Override
    public int releaseBuffered(final OutputStream a1) throws IOException {
        final int v1 = /*EL:166*/this._inputEnd - this._inputPtr;
        /*SL:167*/if (v1 < 1) {
            /*SL:168*/return 0;
        }
        final int v2 = /*EL:171*/this._inputPtr;
        /*SL:172*/a1.write(this._inputBuffer, v2, v1);
        /*SL:173*/return v1;
    }
    
    @Override
    public Object getInputSource() {
        /*SL:178*/return this._inputStream;
    }
    
    protected final boolean _loadMore() throws IOException {
        final int v0 = /*EL:189*/this._inputEnd;
        /*SL:191*/this._currInputProcessed += this._inputEnd;
        /*SL:192*/this._currInputRowStart -= this._inputEnd;
        /*SL:197*/this._nameStartOffset -= v0;
        /*SL:199*/if (this._inputStream != null) {
            final int v = /*EL:200*/this._inputBuffer.length;
            /*SL:201*/if (v == 0) {
                /*SL:202*/return false;
            }
            final int v2 = /*EL:205*/this._inputStream.read(this._inputBuffer, 0, v);
            /*SL:206*/if (v2 > 0) {
                /*SL:207*/this._inputPtr = 0;
                /*SL:208*/this._inputEnd = v2;
                /*SL:209*/return true;
            }
            /*SL:212*/this._closeInput();
            /*SL:214*/if (v2 == 0) {
                /*SL:215*/throw new IOException("InputStream.read() returned 0 characters when trying to read " + this._inputBuffer.length + " bytes");
            }
        }
        /*SL:218*/return false;
    }
    
    @Override
    protected void _closeInput() throws IOException {
        /*SL:226*/if (this._inputStream != null) {
            /*SL:227*/if (this._ioContext.isResourceManaged() || this.isEnabled(Feature.AUTO_CLOSE_SOURCE)) {
                /*SL:228*/this._inputStream.close();
            }
            /*SL:230*/this._inputStream = null;
        }
    }
    
    @Override
    protected void _releaseBuffers() throws IOException {
        /*SL:243*/super._releaseBuffers();
        /*SL:245*/this._symbols.release();
        /*SL:246*/if (this._bufferRecyclable) {
            final byte[] v1 = /*EL:247*/this._inputBuffer;
            /*SL:248*/if (v1 != null) {
                /*SL:251*/this._inputBuffer = UTF8StreamJsonParser.NO_BYTES;
                /*SL:252*/this._ioContext.releaseReadIOBuffer(v1);
            }
        }
    }
    
    @Override
    public String getText() throws IOException {
        /*SL:266*/if (this._currToken != JsonToken.VALUE_STRING) {
            /*SL:273*/return this._getText2(this._currToken);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            return this._finishAndReturnString();
        }
        return this._textBuffer.contentsAsString();
    }
    
    @Override
    public int getText(final Writer v-1) throws IOException {
        final JsonToken v0 = /*EL:279*/this._currToken;
        /*SL:280*/if (v0 == JsonToken.VALUE_STRING) {
            /*SL:281*/if (this._tokenIncomplete) {
                /*SL:282*/this._tokenIncomplete = false;
                /*SL:283*/this._finishString();
            }
            /*SL:285*/return this._textBuffer.contentsToWriter(v-1);
        }
        /*SL:287*/if (v0 == JsonToken.FIELD_NAME) {
            final String a1 = /*EL:288*/this._parsingContext.getCurrentName();
            /*SL:289*/v-1.write(a1);
            /*SL:290*/return a1.length();
        }
        /*SL:292*/if (v0 == null) {
            /*SL:300*/return 0;
        }
        if (v0.isNumeric()) {
            return this._textBuffer.contentsToWriter(v-1);
        }
        final char[] v = v0.asCharArray();
        v-1.write(v);
        return v.length;
    }
    
    @Override
    public String getValueAsString() throws IOException {
        /*SL:309*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:310*/if (this._tokenIncomplete) {
                /*SL:311*/this._tokenIncomplete = false;
                /*SL:312*/return this._finishAndReturnString();
            }
            /*SL:314*/return this._textBuffer.contentsAsString();
        }
        else {
            /*SL:316*/if (this._currToken == JsonToken.FIELD_NAME) {
                /*SL:317*/return this.getCurrentName();
            }
            /*SL:319*/return super.getValueAsString(null);
        }
    }
    
    @Override
    public String getValueAsString(final String a1) throws IOException {
        /*SL:326*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:327*/if (this._tokenIncomplete) {
                /*SL:328*/this._tokenIncomplete = false;
                /*SL:329*/return this._finishAndReturnString();
            }
            /*SL:331*/return this._textBuffer.contentsAsString();
        }
        else {
            /*SL:333*/if (this._currToken == JsonToken.FIELD_NAME) {
                /*SL:334*/return this.getCurrentName();
            }
            /*SL:336*/return super.getValueAsString(a1);
        }
    }
    
    @Override
    public int getValueAsInt() throws IOException {
        final JsonToken v1 = /*EL:343*/this._currToken;
        /*SL:344*/if (v1 == JsonToken.VALUE_NUMBER_INT || v1 == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:346*/if ((this._numTypesValid & 0x1) == 0x0) {
                /*SL:347*/if (this._numTypesValid == 0) {
                    /*SL:348*/return this._parseIntValue();
                }
                /*SL:350*/if ((this._numTypesValid & 0x1) == 0x0) {
                    /*SL:351*/this.convertNumberToInt();
                }
            }
            /*SL:354*/return this._numberInt;
        }
        /*SL:356*/return super.getValueAsInt(0);
    }
    
    @Override
    public int getValueAsInt(final int a1) throws IOException {
        final JsonToken v1 = /*EL:363*/this._currToken;
        /*SL:364*/if (v1 == JsonToken.VALUE_NUMBER_INT || v1 == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:366*/if ((this._numTypesValid & 0x1) == 0x0) {
                /*SL:367*/if (this._numTypesValid == 0) {
                    /*SL:368*/return this._parseIntValue();
                }
                /*SL:370*/if ((this._numTypesValid & 0x1) == 0x0) {
                    /*SL:371*/this.convertNumberToInt();
                }
            }
            /*SL:374*/return this._numberInt;
        }
        /*SL:376*/return super.getValueAsInt(a1);
    }
    
    protected final String _getText2(final JsonToken a1) {
        /*SL:381*/if (a1 == null) {
            /*SL:382*/return null;
        }
        /*SL:384*/switch (a1.id()) {
            case 5: {
                /*SL:386*/return this._parsingContext.getCurrentName();
            }
            case 6:
            case 7:
            case 8: {
                /*SL:392*/return this._textBuffer.contentsAsString();
            }
            default: {
                /*SL:394*/return a1.asString();
            }
        }
    }
    
    @Override
    public char[] getTextCharacters() throws IOException {
        /*SL:401*/if (this._currToken == null) {
            /*SL:432*/return null;
        }
        switch (this._currToken.id()) {
            case 5: {
                if (!this._nameCopied) {
                    final String v1 = this._parsingContext.getCurrentName();
                    final int v2 = v1.length();
                    if (this._nameCopyBuffer == null) {
                        this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(v2);
                    }
                    else if (this._nameCopyBuffer.length < v2) {
                        this._nameCopyBuffer = new char[v2];
                    }
                    v1.getChars(0, v2, this._nameCopyBuffer, 0);
                    this._nameCopied = true;
                }
                return this._nameCopyBuffer;
            }
            case 6: {
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    this._finishString();
                    return this._textBuffer.getTextBuffer();
                }
                return this._textBuffer.getTextBuffer();
            }
            case 7:
            case 8: {
                return this._textBuffer.getTextBuffer();
            }
            default: {
                return this._currToken.asCharArray();
            }
        }
    }
    
    @Override
    public int getTextLength() throws IOException {
        /*SL:438*/if (this._currToken == null) {
            /*SL:457*/return 0;
        }
        switch (this._currToken.id()) {
            case 5: {
                return this._parsingContext.getCurrentName().length();
            }
            case 6: {
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    this._finishString();
                    return this._textBuffer.size();
                }
                return this._textBuffer.size();
            }
            case 7:
            case 8: {
                return this._textBuffer.size();
            }
            default: {
                return this._currToken.asCharArray().length;
            }
        }
    }
    
    @Override
    public int getTextOffset() throws IOException {
        /*SL:464*/if (this._currToken != null) {
            /*SL:465*/switch (this._currToken.id()) {
                case 5: {
                    /*SL:467*/return 0;
                }
                case 6: {
                    /*SL:469*/if (this._tokenIncomplete) {
                        /*SL:470*/this._tokenIncomplete = false;
                        /*SL:471*/this._finishString();
                        return /*EL:476*/this._textBuffer.getTextOffset();
                    }
                    return this._textBuffer.getTextOffset();
                }
                case 7:
                case 8: {
                    return this._textBuffer.getTextOffset();
                }
            }
        }
        /*SL:480*/return 0;
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant v0) throws IOException {
        /*SL:486*/if (this._currToken != JsonToken.VALUE_STRING && (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null)) {
            /*SL:488*/this._reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        /*SL:493*/if (this._tokenIncomplete) {
            try {
                /*SL:495*/this._binaryValue = this._decodeBase64(v0);
            }
            catch (IllegalArgumentException a1) {
                /*SL:497*/throw this._constructError("Failed to decode VALUE_STRING as base64 (" + v0 + "): " + a1.getMessage());
            }
            /*SL:502*/this._tokenIncomplete = false;
        }
        else/*SL:504*/ if (this._binaryValue == null) {
            final ByteArrayBuilder v = /*EL:506*/this._getByteArrayBuilder();
            /*SL:507*/this._decodeBase64(this.getText(), v, v0);
            /*SL:508*/this._binaryValue = v.toByteArray();
        }
        /*SL:511*/return this._binaryValue;
    }
    
    @Override
    public int readBinaryValue(final Base64Variant v1, final OutputStream v2) throws IOException {
        /*SL:518*/if (!this._tokenIncomplete || this._currToken != JsonToken.VALUE_STRING) {
            final byte[] a1 = /*EL:519*/this.getBinaryValue(v1);
            /*SL:520*/v2.write(a1);
            /*SL:521*/return a1.length;
        }
        final byte[] v3 = /*EL:524*/this._ioContext.allocBase64Buffer();
        try {
            /*SL:528*/return this._readBinary(v1, v2, v3);
        }
        finally {
            this._ioContext.releaseBase64Buffer(v3);
        }
    }
    
    protected int _readBinary(final Base64Variant v2, final OutputStream v3, final byte[] v4) throws IOException {
        int v5 = /*EL:535*/0;
        final int v6 = /*EL:536*/v4.length - 3;
        int v7 = /*EL:537*/0;
        while (true) {
            /*SL:543*/if (this._inputPtr >= this._inputEnd) {
                /*SL:544*/this._loadMoreGuaranteed();
            }
            int a1 = /*EL:546*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:547*/if (a1 > 32) {
                int a2 = /*EL:548*/v2.decodeBase64Char(a1);
                /*SL:549*/if (a2 < 0) {
                    /*SL:550*/if (a1 == 34) {
                        /*SL:551*/break;
                    }
                    /*SL:553*/a2 = this._decodeBase64Escape(v2, a1, 0);
                    /*SL:554*/if (a2 < 0) {
                        /*SL:555*/continue;
                    }
                }
                /*SL:560*/if (v5 > v6) {
                    /*SL:561*/v7 += v5;
                    /*SL:562*/v3.write(v4, 0, v5);
                    /*SL:563*/v5 = 0;
                }
                int a3 = /*EL:566*/a2;
                /*SL:570*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:571*/this._loadMoreGuaranteed();
                }
                /*SL:573*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                /*SL:574*/a2 = v2.decodeBase64Char(a1);
                /*SL:575*/if (a2 < 0) {
                    /*SL:576*/a2 = this._decodeBase64Escape(v2, a1, 1);
                }
                /*SL:578*/a3 = (a3 << 6 | a2);
                /*SL:581*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:582*/this._loadMoreGuaranteed();
                }
                /*SL:584*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                /*SL:585*/a2 = v2.decodeBase64Char(a1);
                /*SL:588*/if (a2 < 0) {
                    /*SL:589*/if (a2 != -2) {
                        /*SL:591*/if (a1 == 34 && !v2.usesPadding()) {
                            /*SL:592*/a3 >>= 4;
                            /*SL:593*/v4[v5++] = (byte)a3;
                            /*SL:594*/break;
                        }
                        /*SL:596*/a2 = this._decodeBase64Escape(v2, a1, 2);
                    }
                    /*SL:598*/if (a2 == -2) {
                        /*SL:600*/if (this._inputPtr >= this._inputEnd) {
                            /*SL:601*/this._loadMoreGuaranteed();
                        }
                        /*SL:603*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                        /*SL:604*/if (!v2.usesPaddingChar(a1)) {
                            /*SL:605*/throw this.reportInvalidBase64Char(v2, a1, 3, "expected padding character '" + v2.getPaddingChar() + "'");
                        }
                        /*SL:608*/a3 >>= 4;
                        /*SL:609*/v4[v5++] = (byte)a3;
                        /*SL:610*/continue;
                    }
                }
                /*SL:614*/a3 = (a3 << 6 | a2);
                /*SL:616*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:617*/this._loadMoreGuaranteed();
                }
                /*SL:619*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                /*SL:620*/a2 = v2.decodeBase64Char(a1);
                /*SL:621*/if (a2 < 0) {
                    /*SL:622*/if (a2 != -2) {
                        /*SL:624*/if (a1 == 34 && !v2.usesPadding()) {
                            /*SL:625*/a3 >>= 2;
                            /*SL:626*/v4[v5++] = (byte)(a3 >> 8);
                            /*SL:627*/v4[v5++] = (byte)a3;
                            /*SL:628*/break;
                        }
                        /*SL:630*/a2 = this._decodeBase64Escape(v2, a1, 3);
                    }
                    /*SL:632*/if (a2 == -2) {
                        /*SL:639*/a3 >>= 2;
                        /*SL:640*/v4[v5++] = (byte)(a3 >> 8);
                        /*SL:641*/v4[v5++] = (byte)a3;
                        /*SL:642*/continue;
                    }
                }
                /*SL:646*/a3 = (a3 << 6 | a2);
                /*SL:647*/v4[v5++] = (byte)(a3 >> 16);
                /*SL:648*/v4[v5++] = (byte)(a3 >> 8);
                /*SL:649*/v4[v5++] = (byte)a3;
            }
        }
        /*SL:651*/this._tokenIncomplete = false;
        /*SL:652*/if (v5 > 0) {
            /*SL:653*/v7 += v5;
            /*SL:654*/v3.write(v4, 0, v5);
        }
        /*SL:656*/return v7;
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        /*SL:676*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:677*/return this._nextAfterName();
        }
        /*SL:681*/this._numTypesValid = 0;
        /*SL:682*/if (this._tokenIncomplete) {
            /*SL:683*/this._skipString();
        }
        int v1 = /*EL:685*/this._skipWSOrEnd();
        /*SL:686*/if (v1 < 0) {
            /*SL:688*/this.close();
            /*SL:689*/return this._currToken = null;
        }
        /*SL:692*/this._binaryValue = null;
        /*SL:695*/if (v1 == 93) {
            /*SL:696*/this._closeArrayScope();
            /*SL:697*/return this._currToken = JsonToken.END_ARRAY;
        }
        /*SL:699*/if (v1 == 125) {
            /*SL:700*/this._closeObjectScope();
            /*SL:701*/return this._currToken = JsonToken.END_OBJECT;
        }
        /*SL:705*/if (this._parsingContext.expectComma()) {
            /*SL:706*/if (v1 != 44) {
                /*SL:707*/this._reportUnexpectedChar(v1, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
            }
            /*SL:709*/v1 = this._skipWS();
            /*SL:711*/if ((this._features & UTF8StreamJsonParser.FEAT_MASK_TRAILING_COMMA) != 0x0 && /*EL:712*/(v1 == 93 || v1 == 125)) {
                /*SL:713*/return this._closeScope(v1);
            }
        }
        /*SL:721*/if (!this._parsingContext.inObject()) {
            /*SL:722*/this._updateLocation();
            /*SL:723*/return this._nextTokenNotInObject(v1);
        }
        /*SL:726*/this._updateNameLocation();
        final String v2 = /*EL:727*/this._parseName(v1);
        /*SL:728*/this._parsingContext.setCurrentName(v2);
        /*SL:729*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:731*/v1 = this._skipColon();
        /*SL:732*/this._updateLocation();
        /*SL:735*/if (v1 == 34) {
            /*SL:736*/this._tokenIncomplete = true;
            /*SL:737*/this._nextToken = JsonToken.VALUE_STRING;
            /*SL:738*/return this._currToken;
        }
        JsonToken v3 = null;
        /*SL:742*/switch (v1) {
            case 45: {
                /*SL:744*/v3 = this._parseNegNumber();
                /*SL:745*/break;
            }
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:759*/v3 = this._parsePosNumber(v1);
                /*SL:760*/break;
            }
            case 102: {
                /*SL:762*/this._matchFalse();
                /*SL:763*/v3 = JsonToken.VALUE_FALSE;
                /*SL:764*/break;
            }
            case 110: {
                /*SL:766*/this._matchNull();
                /*SL:767*/v3 = JsonToken.VALUE_NULL;
                /*SL:768*/break;
            }
            case 116: {
                /*SL:770*/this._matchTrue();
                /*SL:771*/v3 = JsonToken.VALUE_TRUE;
                /*SL:772*/break;
            }
            case 91: {
                /*SL:774*/v3 = JsonToken.START_ARRAY;
                /*SL:775*/break;
            }
            case 123: {
                /*SL:777*/v3 = JsonToken.START_OBJECT;
                /*SL:778*/break;
            }
            default: {
                /*SL:781*/v3 = this._handleUnexpectedValue(v1);
                break;
            }
        }
        /*SL:783*/this._nextToken = v3;
        /*SL:784*/return this._currToken;
    }
    
    private final JsonToken _nextTokenNotInObject(final int a1) throws IOException {
        /*SL:789*/if (a1 == 34) {
            /*SL:790*/this._tokenIncomplete = true;
            /*SL:791*/return this._currToken = JsonToken.VALUE_STRING;
        }
        /*SL:793*/switch (a1) {
            case 91: {
                /*SL:795*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                /*SL:796*/return this._currToken = JsonToken.START_ARRAY;
            }
            case 123: {
                /*SL:798*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                /*SL:799*/return this._currToken = JsonToken.START_OBJECT;
            }
            case 116: {
                /*SL:801*/this._matchTrue();
                /*SL:802*/return this._currToken = JsonToken.VALUE_TRUE;
            }
            case 102: {
                /*SL:804*/this._matchFalse();
                /*SL:805*/return this._currToken = JsonToken.VALUE_FALSE;
            }
            case 110: {
                /*SL:807*/this._matchNull();
                /*SL:808*/return this._currToken = JsonToken.VALUE_NULL;
            }
            case 45: {
                /*SL:810*/return this._currToken = this._parseNegNumber();
            }
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:824*/return this._currToken = this._parsePosNumber(a1);
            }
            default: {
                /*SL:826*/return this._currToken = this._handleUnexpectedValue(a1);
            }
        }
    }
    
    private final JsonToken _nextAfterName() {
        /*SL:831*/this._nameCopied = false;
        final JsonToken v1 = /*EL:832*/this._nextToken;
        /*SL:833*/this._nextToken = null;
        /*SL:838*/if (v1 == JsonToken.START_ARRAY) {
            /*SL:839*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        }
        else/*SL:840*/ if (v1 == JsonToken.START_OBJECT) {
            /*SL:841*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        /*SL:843*/return this._currToken = v1;
    }
    
    @Override
    public void finishToken() throws IOException {
        /*SL:848*/if (this._tokenIncomplete) {
            /*SL:849*/this._tokenIncomplete = false;
            /*SL:850*/this._finishString();
        }
    }
    
    @Override
    public boolean nextFieldName(final SerializableString v-5) throws IOException {
        /*SL:864*/this._numTypesValid = 0;
        /*SL:865*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:866*/this._nextAfterName();
            /*SL:867*/return false;
        }
        /*SL:869*/if (this._tokenIncomplete) {
            /*SL:870*/this._skipString();
        }
        int n = /*EL:872*/this._skipWSOrEnd();
        /*SL:873*/if (n < 0) {
            /*SL:874*/this.close();
            /*SL:875*/this._currToken = null;
            /*SL:876*/return false;
        }
        /*SL:878*/this._binaryValue = null;
        /*SL:881*/if (n == 93) {
            /*SL:882*/this._closeArrayScope();
            /*SL:883*/this._currToken = JsonToken.END_ARRAY;
            /*SL:884*/return false;
        }
        /*SL:886*/if (n == 125) {
            /*SL:887*/this._closeObjectScope();
            /*SL:888*/this._currToken = JsonToken.END_OBJECT;
            /*SL:889*/return false;
        }
        /*SL:893*/if (this._parsingContext.expectComma()) {
            /*SL:894*/if (n != 44) {
                /*SL:895*/this._reportUnexpectedChar(n, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
            }
            /*SL:897*/n = this._skipWS();
            /*SL:900*/if ((this._features & UTF8StreamJsonParser.FEAT_MASK_TRAILING_COMMA) != 0x0 && /*EL:901*/(n == 93 || n == 125)) {
                /*SL:902*/this._closeScope(n);
                /*SL:903*/return false;
            }
        }
        /*SL:907*/if (!this._parsingContext.inObject()) {
            /*SL:908*/this._updateLocation();
            /*SL:909*/this._nextTokenNotInObject(n);
            /*SL:910*/return false;
        }
        /*SL:914*/this._updateNameLocation();
        /*SL:915*/if (n == 34) {
            final byte[] quotedUTF8 = /*EL:917*/v-5.asQuotedUTF8();
            final int length = /*EL:918*/quotedUTF8.length;
            /*SL:921*/if (this._inputPtr + length + 4 < this._inputEnd) {
                final int n2 = /*EL:923*/this._inputPtr + length;
                /*SL:924*/if (this._inputBuffer[n2] == 34) {
                    int a1 = /*EL:925*/0;
                    int v1;
                    /*SL:928*/for (v1 = this._inputPtr; v1 != n2; /*SL:938*/++v1) {
                        if (quotedUTF8[a1] != this._inputBuffer[v1]) {
                            return /*EL:943*/this._isNextTokenNameMaybe(n, v-5);
                        }
                        ++a1;
                    }
                    this._parsingContext.setCurrentName(v-5.getValue());
                    n = this._skipColonFast(v1 + 1);
                    this._isNextTokenNameYes(n);
                    return true;
                }
            }
        }
        return this._isNextTokenNameMaybe(n, v-5);
    }
    
    @Override
    public String nextFieldName() throws IOException {
        /*SL:950*/this._numTypesValid = 0;
        /*SL:951*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:952*/this._nextAfterName();
            /*SL:953*/return null;
        }
        /*SL:955*/if (this._tokenIncomplete) {
            /*SL:956*/this._skipString();
        }
        int v1 = /*EL:958*/this._skipWSOrEnd();
        /*SL:959*/if (v1 < 0) {
            /*SL:960*/this.close();
            /*SL:961*/this._currToken = null;
            /*SL:962*/return null;
        }
        /*SL:964*/this._binaryValue = null;
        /*SL:966*/if (v1 == 93) {
            /*SL:967*/this._closeArrayScope();
            /*SL:968*/this._currToken = JsonToken.END_ARRAY;
            /*SL:969*/return null;
        }
        /*SL:971*/if (v1 == 125) {
            /*SL:972*/this._closeObjectScope();
            /*SL:973*/this._currToken = JsonToken.END_OBJECT;
            /*SL:974*/return null;
        }
        /*SL:978*/if (this._parsingContext.expectComma()) {
            /*SL:979*/if (v1 != 44) {
                /*SL:980*/this._reportUnexpectedChar(v1, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
            }
            /*SL:982*/v1 = this._skipWS();
            /*SL:984*/if ((this._features & UTF8StreamJsonParser.FEAT_MASK_TRAILING_COMMA) != 0x0 && /*EL:985*/(v1 == 93 || v1 == 125)) {
                /*SL:986*/this._closeScope(v1);
                /*SL:987*/return null;
            }
        }
        /*SL:992*/if (!this._parsingContext.inObject()) {
            /*SL:993*/this._updateLocation();
            /*SL:994*/this._nextTokenNotInObject(v1);
            /*SL:995*/return null;
        }
        /*SL:998*/this._updateNameLocation();
        final String v2 = /*EL:999*/this._parseName(v1);
        /*SL:1000*/this._parsingContext.setCurrentName(v2);
        /*SL:1001*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:1003*/v1 = this._skipColon();
        /*SL:1004*/this._updateLocation();
        /*SL:1005*/if (v1 == 34) {
            /*SL:1006*/this._tokenIncomplete = true;
            /*SL:1007*/this._nextToken = JsonToken.VALUE_STRING;
            /*SL:1008*/return v2;
        }
        JsonToken v3 = null;
        /*SL:1011*/switch (v1) {
            case 45: {
                /*SL:1013*/v3 = this._parseNegNumber();
                /*SL:1014*/break;
            }
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:1025*/v3 = this._parsePosNumber(v1);
                /*SL:1026*/break;
            }
            case 102: {
                /*SL:1028*/this._matchFalse();
                /*SL:1029*/v3 = JsonToken.VALUE_FALSE;
                /*SL:1030*/break;
            }
            case 110: {
                /*SL:1032*/this._matchNull();
                /*SL:1033*/v3 = JsonToken.VALUE_NULL;
                /*SL:1034*/break;
            }
            case 116: {
                /*SL:1036*/this._matchTrue();
                /*SL:1037*/v3 = JsonToken.VALUE_TRUE;
                /*SL:1038*/break;
            }
            case 91: {
                /*SL:1040*/v3 = JsonToken.START_ARRAY;
                /*SL:1041*/break;
            }
            case 123: {
                /*SL:1043*/v3 = JsonToken.START_OBJECT;
                /*SL:1044*/break;
            }
            default: {
                /*SL:1047*/v3 = this._handleUnexpectedValue(v1);
                break;
            }
        }
        /*SL:1049*/this._nextToken = v3;
        /*SL:1050*/return v2;
    }
    
    private final int _skipColonFast(int a1) throws IOException {
        int v1 = /*EL:1056*/this._inputBuffer[a1++];
        /*SL:1057*/if (v1 == 58) {
            /*SL:1058*/v1 = this._inputBuffer[a1++];
            /*SL:1059*/if (v1 > 32) {
                /*SL:1060*/if (v1 != 47 && v1 != 35) {
                    /*SL:1061*/this._inputPtr = a1;
                    /*SL:1062*/return v1;
                }
            }
            else/*SL:1064*/ if (v1 == 32 || v1 == 9) {
                /*SL:1065*/v1 = this._inputBuffer[a1++];
                /*SL:1067*/if (v1 > 32 && v1 != 47 && v1 != 35) {
                    /*SL:1068*/this._inputPtr = a1;
                    /*SL:1069*/return v1;
                }
            }
            /*SL:1073*/this._inputPtr = a1 - 1;
            /*SL:1074*/return this._skipColon2(true);
        }
        /*SL:1076*/if (v1 == 32 || v1 == 9) {
            /*SL:1077*/v1 = this._inputBuffer[a1++];
        }
        /*SL:1079*/if (v1 == 58) {
            /*SL:1080*/v1 = this._inputBuffer[a1++];
            /*SL:1081*/if (v1 > 32) {
                /*SL:1082*/if (v1 != 47 && v1 != 35) {
                    /*SL:1083*/this._inputPtr = a1;
                    /*SL:1084*/return v1;
                }
            }
            else/*SL:1086*/ if (v1 == 32 || v1 == 9) {
                /*SL:1087*/v1 = this._inputBuffer[a1++];
                /*SL:1089*/if (v1 > 32 && v1 != 47 && v1 != 35) {
                    /*SL:1090*/this._inputPtr = a1;
                    /*SL:1091*/return v1;
                }
            }
            /*SL:1095*/this._inputPtr = a1 - 1;
            /*SL:1096*/return this._skipColon2(true);
        }
        /*SL:1098*/this._inputPtr = a1 - 1;
        /*SL:1099*/return this._skipColon2(false);
    }
    
    private final void _isNextTokenNameYes(final int a1) throws IOException {
        /*SL:1104*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:1105*/this._updateLocation();
        /*SL:1107*/switch (a1) {
            case 34: {
                /*SL:1109*/this._tokenIncomplete = true;
                /*SL:1110*/this._nextToken = JsonToken.VALUE_STRING;
            }
            case 91: {
                /*SL:1113*/this._nextToken = JsonToken.START_ARRAY;
            }
            case 123: {
                /*SL:1116*/this._nextToken = JsonToken.START_OBJECT;
            }
            case 116: {
                /*SL:1119*/this._matchTrue();
                /*SL:1120*/this._nextToken = JsonToken.VALUE_TRUE;
            }
            case 102: {
                /*SL:1123*/this._matchFalse();
                /*SL:1124*/this._nextToken = JsonToken.VALUE_FALSE;
            }
            case 110: {
                /*SL:1127*/this._matchNull();
                /*SL:1128*/this._nextToken = JsonToken.VALUE_NULL;
            }
            case 45: {
                /*SL:1131*/this._nextToken = this._parseNegNumber();
            }
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:1143*/this._nextToken = this._parsePosNumber(a1);
            }
            default: {
                /*SL:1146*/this._nextToken = this._handleUnexpectedValue(a1);
            }
        }
    }
    
    private final boolean _isNextTokenNameMaybe(int a1, final SerializableString a2) throws IOException {
        final String v1 = /*EL:1153*/this._parseName(a1);
        /*SL:1154*/this._parsingContext.setCurrentName(v1);
        final boolean v2 = /*EL:1155*/v1.equals(a2.getValue());
        /*SL:1156*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:1157*/a1 = this._skipColon();
        /*SL:1158*/this._updateLocation();
        /*SL:1161*/if (a1 == 34) {
            /*SL:1162*/this._tokenIncomplete = true;
            /*SL:1163*/this._nextToken = JsonToken.VALUE_STRING;
            /*SL:1164*/return v2;
        }
        JsonToken v3 = null;
        /*SL:1168*/switch (a1) {
            case 91: {
                /*SL:1170*/v3 = JsonToken.START_ARRAY;
                /*SL:1171*/break;
            }
            case 123: {
                /*SL:1173*/v3 = JsonToken.START_OBJECT;
                /*SL:1174*/break;
            }
            case 116: {
                /*SL:1176*/this._matchTrue();
                /*SL:1177*/v3 = JsonToken.VALUE_TRUE;
                /*SL:1178*/break;
            }
            case 102: {
                /*SL:1180*/this._matchFalse();
                /*SL:1181*/v3 = JsonToken.VALUE_FALSE;
                /*SL:1182*/break;
            }
            case 110: {
                /*SL:1184*/this._matchNull();
                /*SL:1185*/v3 = JsonToken.VALUE_NULL;
                /*SL:1186*/break;
            }
            case 45: {
                /*SL:1188*/v3 = this._parseNegNumber();
                /*SL:1189*/break;
            }
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:1200*/v3 = this._parsePosNumber(a1);
                /*SL:1201*/break;
            }
            default: {
                /*SL:1203*/v3 = this._handleUnexpectedValue(a1);
                break;
            }
        }
        /*SL:1205*/this._nextToken = v3;
        /*SL:1206*/return v2;
    }
    
    @Override
    public String nextTextValue() throws IOException {
        /*SL:1213*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:1233*/return (this.nextToken() == JsonToken.VALUE_STRING) ? this.getText() : null;
        }
        this._nameCopied = false;
        final JsonToken v1 = this._nextToken;
        this._nextToken = null;
        if ((this._currToken = v1) != JsonToken.VALUE_STRING) {
            if (v1 == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            }
            else if (v1 == JsonToken.START_OBJECT) {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            }
            return null;
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            return this._finishAndReturnString();
        }
        return this._textBuffer.contentsAsString();
    }
    
    @Override
    public int nextIntValue(final int v2) throws IOException {
        /*SL:1240*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:1256*/return (this.nextToken() == JsonToken.VALUE_NUMBER_INT) ? this.getIntValue() : v2;
        }
        this._nameCopied = false;
        final JsonToken a1 = this._nextToken;
        this._nextToken = null;
        if ((this._currToken = a1) == JsonToken.VALUE_NUMBER_INT) {
            return this.getIntValue();
        }
        if (a1 == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        }
        else if (a1 == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        return v2;
    }
    
    @Override
    public long nextLongValue(final long v2) throws IOException {
        /*SL:1263*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:1279*/return (this.nextToken() == JsonToken.VALUE_NUMBER_INT) ? this.getLongValue() : v2;
        }
        this._nameCopied = false;
        final JsonToken a1 = this._nextToken;
        this._nextToken = null;
        if ((this._currToken = a1) == JsonToken.VALUE_NUMBER_INT) {
            return this.getLongValue();
        }
        if (a1 == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        }
        else if (a1 == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        return v2;
    }
    
    @Override
    public Boolean nextBooleanValue() throws IOException {
        /*SL:1286*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:1287*/this._nameCopied = false;
            final JsonToken v1 = /*EL:1288*/this._nextToken;
            /*SL:1289*/this._nextToken = null;
            /*SL:1291*/if ((this._currToken = v1) == JsonToken.VALUE_TRUE) {
                /*SL:1292*/return Boolean.TRUE;
            }
            /*SL:1294*/if (v1 == JsonToken.VALUE_FALSE) {
                /*SL:1295*/return Boolean.FALSE;
            }
            /*SL:1297*/if (v1 == JsonToken.START_ARRAY) {
                /*SL:1298*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            }
            else/*SL:1299*/ if (v1 == JsonToken.START_OBJECT) {
                /*SL:1300*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            }
            /*SL:1302*/return null;
        }
        else {
            final JsonToken v1 = /*EL:1305*/this.nextToken();
            /*SL:1306*/if (v1 == JsonToken.VALUE_TRUE) {
                /*SL:1307*/return Boolean.TRUE;
            }
            /*SL:1309*/if (v1 == JsonToken.VALUE_FALSE) {
                /*SL:1310*/return Boolean.FALSE;
            }
            /*SL:1312*/return null;
        }
    }
    
    protected JsonToken _parsePosNumber(int a1) throws IOException {
        final char[] v1 = /*EL:1338*/this._textBuffer.emptyAndGetCurrentSegment();
        /*SL:1340*/if (a1 == 48) {
            /*SL:1341*/a1 = this._verifyNoLeadingZeroes();
        }
        /*SL:1344*/v1[0] = (char)a1;
        int v2 = /*EL:1345*/1;
        int v3 = /*EL:1346*/1;
        final int v4 = /*EL:1349*/Math.min(this._inputEnd, this._inputPtr + v1.length - 1);
        /*SL:1352*/while (this._inputPtr < v4) {
            /*SL:1355*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
            if (/*EL:1356*/a1 >= 48 && a1 <= 57) {
                /*SL:1359*/++v2;
                /*SL:1360*/v1[v3++] = (char)a1;
            }
            else {
                /*SL:1362*/if (a1 == 46 || a1 == 101 || a1 == 69) {
                    /*SL:1363*/return this._parseFloat(v1, v3, a1, false, v2);
                }
                /*SL:1365*/--this._inputPtr;
                /*SL:1366*/this._textBuffer.setCurrentLength(v3);
                /*SL:1368*/if (this._parsingContext.inRoot()) {
                    /*SL:1369*/this._verifyRootSpace(a1);
                }
                /*SL:1372*/return this.resetInt(false, v2);
            }
        }
        return this._parseNumber2(v1, v3, false, v2);
    }
    
    protected JsonToken _parseNegNumber() throws IOException {
        final char[] v1 = /*EL:1377*/this._textBuffer.emptyAndGetCurrentSegment();
        int v2 = /*EL:1378*/0;
        /*SL:1381*/v1[v2++] = '-';
        /*SL:1383*/if (this._inputPtr >= this._inputEnd) {
            /*SL:1384*/this._loadMoreGuaranteed();
        }
        int v3 = /*EL:1386*/this._inputBuffer[this._inputPtr++] & 0xFF;
        /*SL:1388*/if (v3 <= 48) {
            /*SL:1390*/if (v3 != 48) {
                /*SL:1391*/return this._handleInvalidNumberStart(v3, true);
            }
            /*SL:1393*/v3 = this._verifyNoLeadingZeroes();
        }
        else/*SL:1394*/ if (v3 > 57) {
            /*SL:1395*/return this._handleInvalidNumberStart(v3, true);
        }
        /*SL:1399*/v1[v2++] = (char)v3;
        int v4 = /*EL:1400*/1;
        final int v5 = /*EL:1404*/Math.min(this._inputEnd, this._inputPtr + v1.length - v2);
        /*SL:1407*/while (this._inputPtr < v5) {
            /*SL:1411*/v3 = (this._inputBuffer[this._inputPtr++] & 0xFF);
            if (/*EL:1412*/v3 >= 48 && v3 <= 57) {
                /*SL:1415*/++v4;
                /*SL:1416*/v1[v2++] = (char)v3;
            }
            else {
                /*SL:1418*/if (v3 == 46 || v3 == 101 || v3 == 69) {
                    /*SL:1419*/return this._parseFloat(v1, v2, v3, true, v4);
                }
                /*SL:1422*/--this._inputPtr;
                /*SL:1423*/this._textBuffer.setCurrentLength(v2);
                /*SL:1425*/if (this._parsingContext.inRoot()) {
                    /*SL:1426*/this._verifyRootSpace(v3);
                }
                /*SL:1430*/return this.resetInt(true, v4);
            }
        }
        return this._parseNumber2(v1, v2, true, v4);
    }
    
    private final JsonToken _parseNumber2(char[] a3, int a4, final boolean v1, int v2) throws IOException {
        /*SL:1442*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int a5 = /*EL:1446*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:1447*/if (a5 > 57 || a5 < 48) {
                /*SL:1448*/if (a5 == 46 || a5 == 101 || a5 == 69) {
                    /*SL:1449*/return this._parseFloat(a3, a4, a5, v1, v2);
                }
                /*SL:1460*/--this._inputPtr;
                /*SL:1461*/this._textBuffer.setCurrentLength(a4);
                /*SL:1463*/if (this._parsingContext.inRoot()) {
                    /*SL:1464*/this._verifyRootSpace(this._inputBuffer[this._inputPtr++] & 0xFF);
                }
                /*SL:1468*/return this.resetInt(v1, v2);
            }
            else {
                if (a4 >= a3.length) {
                    a3 = this._textBuffer.finishCurrentSegment();
                    a4 = 0;
                }
                a3[a4++] = (char)a5;
                ++v2;
            }
        }
        this._textBuffer.setCurrentLength(a4);
        return this.resetInt(v1, v2);
    }
    
    private final int _verifyNoLeadingZeroes() throws IOException {
        /*SL:1479*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
            /*SL:1480*/return 48;
        }
        int v1 = /*EL:1482*/this._inputBuffer[this._inputPtr] & 0xFF;
        /*SL:1484*/if (v1 < 48 || v1 > 57) {
            /*SL:1485*/return 48;
        }
        /*SL:1488*/if (!this.isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            /*SL:1489*/this.reportInvalidNumber("Leading zeroes not allowed");
        }
        /*SL:1492*/++this._inputPtr;
        /*SL:1493*/if (v1 == 48) {
            /*SL:1494*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
                /*SL:1495*/v1 = (this._inputBuffer[this._inputPtr] & 0xFF);
                /*SL:1496*/if (v1 < 48 || v1 > 57) {
                    /*SL:1497*/return 48;
                }
                /*SL:1499*/++this._inputPtr;
                /*SL:1500*/if (v1 != 48) {
                    break;
                }
            }
        }
        /*SL:1505*/return v1;
    }
    
    private final JsonToken _parseFloat(char[] a1, int a2, int a3, final boolean a4, final int a5) throws IOException {
        int v1 = /*EL:1511*/0;
        boolean v2 = /*EL:1512*/false;
        Label_0139: {
            /*SL:1515*/if (a3 == 46) {
                /*SL:1516*/if (a2 >= a1.length) {
                    /*SL:1517*/a1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1518*/a2 = 0;
                }
                /*SL:1520*/a1[a2++] = (char)a3;
                while (true) {
                    /*SL:1524*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
                        /*SL:1528*/a3 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                        /*SL:1529*/if (a3 >= 48) {
                            if (a3 <= 57) {
                                /*SL:1532*/++v1;
                                /*SL:1533*/if (a2 >= a1.length) {
                                    /*SL:1534*/a1 = this._textBuffer.finishCurrentSegment();
                                    /*SL:1535*/a2 = 0;
                                }
                                /*SL:1537*/a1[a2++] = (char)a3;
                                continue;
                            }
                        }
                        /*SL:1540*/if (v1 == 0) {
                            /*SL:1541*/this.reportUnexpectedNumberChar(a3, "Decimal point not followed by a digit");
                        }
                        break Label_0139;
                    }
                    v2 = true;
                    continue;
                }
            }
        }
        int v3 = /*EL:1545*/0;
        /*SL:1546*/if (a3 == 101 || a3 == 69) {
            /*SL:1547*/if (a2 >= a1.length) {
                /*SL:1548*/a1 = this._textBuffer.finishCurrentSegment();
                /*SL:1549*/a2 = 0;
            }
            /*SL:1551*/a1[a2++] = (char)a3;
            /*SL:1553*/if (this._inputPtr >= this._inputEnd) {
                /*SL:1554*/this._loadMoreGuaranteed();
            }
            /*SL:1556*/a3 = (this._inputBuffer[this._inputPtr++] & 0xFF);
            /*SL:1558*/if (a3 == 45 || a3 == 43) {
                /*SL:1559*/if (a2 >= a1.length) {
                    /*SL:1560*/a1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1561*/a2 = 0;
                }
                /*SL:1563*/a1[a2++] = (char)a3;
                /*SL:1565*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:1566*/this._loadMoreGuaranteed();
                }
                /*SL:1568*/a3 = (this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            /*SL:1572*/while (a3 >= 48 && a3 <= 57) {
                /*SL:1573*/++v3;
                /*SL:1574*/if (a2 >= a1.length) {
                    /*SL:1575*/a1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1576*/a2 = 0;
                }
                /*SL:1578*/a1[a2++] = (char)a3;
                /*SL:1579*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
                    /*SL:1580*/v2 = true;
                    /*SL:1581*/break;
                }
                /*SL:1583*/a3 = (this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            /*SL:1586*/if (v3 == 0) {
                /*SL:1587*/this.reportUnexpectedNumberChar(a3, "Exponent indicator not followed by a digit");
            }
        }
        /*SL:1592*/if (!v2) {
            /*SL:1593*/--this._inputPtr;
            /*SL:1595*/if (this._parsingContext.inRoot()) {
                /*SL:1596*/this._verifyRootSpace(a3);
            }
        }
        /*SL:1599*/this._textBuffer.setCurrentLength(a2);
        /*SL:1602*/return this.resetFloat(a4, a5, v1, v3);
    }
    
    private final void _verifyRootSpace(final int a1) throws IOException {
        /*SL:1615*/++this._inputPtr;
        /*SL:1617*/switch (a1) {
            case 9:
            case 32: {}
            case 13: {
                /*SL:1622*/this._skipCR();
            }
            case 10: {
                /*SL:1625*/++this._currInputRow;
                /*SL:1626*/this._currInputRowStart = this._inputPtr;
            }
            default: {
                /*SL:1629*/this._reportMissingRootWS(a1);
            }
        }
    }
    
    protected final String _parseName(int a1) throws IOException {
        /*SL:1640*/if (a1 != 34) {
            /*SL:1641*/return this._handleOddName(a1);
        }
        /*SL:1644*/if (this._inputPtr + 13 > this._inputEnd) {
            /*SL:1645*/return this.slowParseName();
        }
        final byte[] v1 = /*EL:1654*/this._inputBuffer;
        final int[] v2 = UTF8StreamJsonParser._icLatin1;
        int v3 = /*EL:1657*/v1[this._inputPtr++] & 0xFF;
        /*SL:1659*/if (v2[v3] == 0) {
            /*SL:1660*/a1 = (v1[this._inputPtr++] & 0xFF);
            /*SL:1661*/if (v2[a1] == 0) {
                /*SL:1662*/v3 = (v3 << 8 | a1);
                /*SL:1663*/a1 = (v1[this._inputPtr++] & 0xFF);
                /*SL:1664*/if (v2[a1] == 0) {
                    /*SL:1665*/v3 = (v3 << 8 | a1);
                    /*SL:1666*/a1 = (v1[this._inputPtr++] & 0xFF);
                    /*SL:1667*/if (v2[a1] == 0) {
                        /*SL:1668*/v3 = (v3 << 8 | a1);
                        /*SL:1669*/a1 = (v1[this._inputPtr++] & 0xFF);
                        /*SL:1670*/if (v2[a1] == 0) {
                            /*SL:1671*/this._quad1 = v3;
                            /*SL:1672*/return this.parseMediumName(a1);
                        }
                        /*SL:1674*/if (a1 == 34) {
                            /*SL:1675*/return this.findName(v3, 4);
                        }
                        /*SL:1677*/return this.parseName(v3, a1, 4);
                    }
                    else {
                        /*SL:1679*/if (a1 == 34) {
                            /*SL:1680*/return this.findName(v3, 3);
                        }
                        /*SL:1682*/return this.parseName(v3, a1, 3);
                    }
                }
                else {
                    /*SL:1684*/if (a1 == 34) {
                        /*SL:1685*/return this.findName(v3, 2);
                    }
                    /*SL:1687*/return this.parseName(v3, a1, 2);
                }
            }
            else {
                /*SL:1689*/if (a1 == 34) {
                    /*SL:1690*/return this.findName(v3, 1);
                }
                /*SL:1692*/return this.parseName(v3, a1, 1);
            }
        }
        else {
            /*SL:1694*/if (v3 == 34) {
                /*SL:1695*/return "";
            }
            /*SL:1697*/return this.parseName(0, v3, 0);
        }
    }
    
    protected final String parseMediumName(int a1) throws IOException {
        final byte[] v1 = /*EL:1702*/this._inputBuffer;
        final int[] v2 = UTF8StreamJsonParser._icLatin1;
        int v3 = /*EL:1706*/v1[this._inputPtr++] & 0xFF;
        /*SL:1707*/if (v2[v3] != 0) {
            /*SL:1708*/if (v3 == 34) {
                /*SL:1709*/return this.findName(this._quad1, a1, 1);
            }
            /*SL:1711*/return this.parseName(this._quad1, a1, v3, 1);
        }
        else {
            /*SL:1713*/a1 = (a1 << 8 | v3);
            /*SL:1714*/v3 = (v1[this._inputPtr++] & 0xFF);
            /*SL:1715*/if (v2[v3] != 0) {
                /*SL:1716*/if (v3 == 34) {
                    /*SL:1717*/return this.findName(this._quad1, a1, 2);
                }
                /*SL:1719*/return this.parseName(this._quad1, a1, v3, 2);
            }
            else {
                /*SL:1721*/a1 = (a1 << 8 | v3);
                /*SL:1722*/v3 = (v1[this._inputPtr++] & 0xFF);
                /*SL:1723*/if (v2[v3] != 0) {
                    /*SL:1724*/if (v3 == 34) {
                        /*SL:1725*/return this.findName(this._quad1, a1, 3);
                    }
                    /*SL:1727*/return this.parseName(this._quad1, a1, v3, 3);
                }
                else {
                    /*SL:1729*/a1 = (a1 << 8 | v3);
                    /*SL:1730*/v3 = (v1[this._inputPtr++] & 0xFF);
                    /*SL:1731*/if (v2[v3] == 0) {
                        /*SL:1737*/return this.parseMediumName2(v3, a1);
                    }
                    if (v3 == 34) {
                        return this.findName(this._quad1, a1, 4);
                    }
                    return this.parseName(this._quad1, a1, v3, 4);
                }
            }
        }
    }
    
    protected final String parseMediumName2(int a1, final int a2) throws IOException {
        final byte[] v1 = /*EL:1745*/this._inputBuffer;
        final int[] v2 = UTF8StreamJsonParser._icLatin1;
        int v3 = /*EL:1749*/v1[this._inputPtr++] & 0xFF;
        /*SL:1750*/if (v2[v3] != 0) {
            /*SL:1751*/if (v3 == 34) {
                /*SL:1752*/return this.findName(this._quad1, a2, a1, 1);
            }
            /*SL:1754*/return this.parseName(this._quad1, a2, a1, v3, 1);
        }
        else {
            /*SL:1756*/a1 = (a1 << 8 | v3);
            /*SL:1757*/v3 = (v1[this._inputPtr++] & 0xFF);
            /*SL:1758*/if (v2[v3] != 0) {
                /*SL:1759*/if (v3 == 34) {
                    /*SL:1760*/return this.findName(this._quad1, a2, a1, 2);
                }
                /*SL:1762*/return this.parseName(this._quad1, a2, a1, v3, 2);
            }
            else {
                /*SL:1764*/a1 = (a1 << 8 | v3);
                /*SL:1765*/v3 = (v1[this._inputPtr++] & 0xFF);
                /*SL:1766*/if (v2[v3] != 0) {
                    /*SL:1767*/if (v3 == 34) {
                        /*SL:1768*/return this.findName(this._quad1, a2, a1, 3);
                    }
                    /*SL:1770*/return this.parseName(this._quad1, a2, a1, v3, 3);
                }
                else {
                    /*SL:1772*/a1 = (a1 << 8 | v3);
                    /*SL:1773*/v3 = (v1[this._inputPtr++] & 0xFF);
                    /*SL:1774*/if (v2[v3] == 0) {
                        /*SL:1780*/return this.parseLongName(v3, a2, a1);
                    }
                    if (v3 == 34) {
                        return this.findName(this._quad1, a2, a1, 4);
                    }
                    return this.parseName(this._quad1, a2, a1, v3, 4);
                }
            }
        }
    }
    
    protected final String parseLongName(int a3, final int v1, final int v2) throws IOException {
        /*SL:1785*/this._quadBuffer[0] = this._quad1;
        /*SL:1786*/this._quadBuffer[1] = v1;
        /*SL:1787*/this._quadBuffer[2] = v2;
        final byte[] v3 = /*EL:1790*/this._inputBuffer;
        final int[] v4 = UTF8StreamJsonParser._icLatin1;
        int v5 = /*EL:1792*/3;
        /*SL:1794*/while (this._inputPtr + 4 <= this._inputEnd) {
            int a4 = /*EL:1795*/v3[this._inputPtr++] & 0xFF;
            /*SL:1796*/if (v4[a4] != 0) {
                /*SL:1797*/if (a4 == 34) {
                    /*SL:1798*/return this.findName(this._quadBuffer, v5, a3, 1);
                }
                /*SL:1800*/return this.parseEscapedName(this._quadBuffer, v5, a3, a4, 1);
            }
            else {
                /*SL:1803*/a3 = (a3 << 8 | a4);
                /*SL:1804*/a4 = (v3[this._inputPtr++] & 0xFF);
                /*SL:1805*/if (v4[a4] != 0) {
                    /*SL:1806*/if (a4 == 34) {
                        /*SL:1807*/return this.findName(this._quadBuffer, v5, a3, 2);
                    }
                    /*SL:1809*/return this.parseEscapedName(this._quadBuffer, v5, a3, a4, 2);
                }
                else {
                    /*SL:1812*/a3 = (a3 << 8 | a4);
                    /*SL:1813*/a4 = (v3[this._inputPtr++] & 0xFF);
                    /*SL:1814*/if (v4[a4] != 0) {
                        /*SL:1815*/if (a4 == 34) {
                            /*SL:1816*/return this.findName(this._quadBuffer, v5, a3, 3);
                        }
                        /*SL:1818*/return this.parseEscapedName(this._quadBuffer, v5, a3, a4, 3);
                    }
                    else {
                        /*SL:1821*/a3 = (a3 << 8 | a4);
                        /*SL:1822*/a4 = (v3[this._inputPtr++] & 0xFF);
                        /*SL:1823*/if (v4[a4] != 0) {
                            /*SL:1824*/if (a4 == 34) {
                                /*SL:1825*/return this.findName(this._quadBuffer, v5, a3, 4);
                            }
                            /*SL:1827*/return this.parseEscapedName(this._quadBuffer, v5, a3, a4, 4);
                        }
                        else {
                            /*SL:1831*/if (v5 >= this._quadBuffer.length) {
                                /*SL:1832*/this._quadBuffer = ParserBase.growArrayBy(this._quadBuffer, v5);
                            }
                            /*SL:1834*/this._quadBuffer[v5++] = a3;
                            /*SL:1835*/a3 = a4;
                        }
                    }
                }
            }
        }
        /*SL:1842*/return this.parseEscapedName(this._quadBuffer, v5, 0, a3, 0);
    }
    
    protected String slowParseName() throws IOException {
        /*SL:1852*/if (this._inputPtr >= this._inputEnd && /*EL:1853*/!this._loadMore()) {
            /*SL:1854*/this._reportInvalidEOF(": was expecting closing '\"' for name", JsonToken.FIELD_NAME);
        }
        final int v1 = /*EL:1857*/this._inputBuffer[this._inputPtr++] & 0xFF;
        /*SL:1858*/if (v1 == 34) {
            /*SL:1859*/return "";
        }
        /*SL:1861*/return this.parseEscapedName(this._quadBuffer, 0, 0, v1, 0);
    }
    
    private final String parseName(final int a1, final int a2, final int a3) throws IOException {
        /*SL:1865*/return this.parseEscapedName(this._quadBuffer, 0, a1, a2, a3);
    }
    
    private final String parseName(final int a1, final int a2, final int a3, final int a4) throws IOException {
        /*SL:1869*/this._quadBuffer[0] = a1;
        /*SL:1870*/return this.parseEscapedName(this._quadBuffer, 1, a2, a3, a4);
    }
    
    private final String parseName(final int a1, final int a2, final int a3, final int a4, final int a5) throws IOException {
        /*SL:1874*/this._quadBuffer[0] = a1;
        /*SL:1875*/this._quadBuffer[1] = a2;
        /*SL:1876*/return this.parseEscapedName(this._quadBuffer, 2, a3, a4, a5);
    }
    
    protected final String parseEscapedName(int[] a1, int a2, int a3, int a4, int a5) throws IOException {
        final int[] v1 = UTF8StreamJsonParser._icLatin1;
        while (true) {
            /*SL:1894*/if (v1[a4] != 0) {
                /*SL:1895*/if (a4 == 34) {
                    break;
                }
                /*SL:1899*/if (a4 != 92) {
                    /*SL:1901*/this._throwUnquotedSpace(a4, "name");
                }
                else {
                    /*SL:1904*/a4 = this._decodeEscaped();
                }
                /*SL:1909*/if (a4 > 127) {
                    /*SL:1911*/if (a5 >= 4) {
                        /*SL:1912*/if (a2 >= a1.length) {
                            /*SL:1913*/a1 = (this._quadBuffer = ParserBase.growArrayBy(a1, a1.length));
                        }
                        /*SL:1915*/a1[a2++] = a3;
                        /*SL:1916*/a3 = 0;
                        /*SL:1917*/a5 = 0;
                    }
                    /*SL:1919*/if (a4 < 2048) {
                        /*SL:1920*/a3 = (a3 << 8 | (0xC0 | a4 >> 6));
                        /*SL:1921*/++a5;
                    }
                    else {
                        /*SL:1924*/a3 = (a3 << 8 | (0xE0 | a4 >> 12));
                        /*SL:1927*/if (++a5 >= 4) {
                            /*SL:1928*/if (a2 >= a1.length) {
                                /*SL:1929*/a1 = (this._quadBuffer = ParserBase.growArrayBy(a1, a1.length));
                            }
                            /*SL:1931*/a1[a2++] = a3;
                            /*SL:1932*/a3 = 0;
                            /*SL:1933*/a5 = 0;
                        }
                        /*SL:1935*/a3 = (a3 << 8 | (0x80 | (a4 >> 6 & 0x3F)));
                        /*SL:1936*/++a5;
                    }
                    /*SL:1939*/a4 = (0x80 | (a4 & 0x3F));
                }
            }
            /*SL:1943*/if (a5 < 4) {
                /*SL:1944*/++a5;
                /*SL:1945*/a3 = (a3 << 8 | a4);
            }
            else {
                /*SL:1947*/if (a2 >= a1.length) {
                    /*SL:1948*/a1 = (this._quadBuffer = ParserBase.growArrayBy(a1, a1.length));
                }
                /*SL:1950*/a1[a2++] = a3;
                /*SL:1951*/a3 = a4;
                /*SL:1952*/a5 = 1;
            }
            /*SL:1954*/if (this._inputPtr >= this._inputEnd && /*EL:1955*/!this._loadMore()) {
                /*SL:1956*/this._reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
            }
            /*SL:1959*/a4 = (this._inputBuffer[this._inputPtr++] & 0xFF);
        }
        /*SL:1962*/if (a5 > 0) {
            /*SL:1963*/if (a2 >= a1.length) {
                /*SL:1964*/a1 = (this._quadBuffer = ParserBase.growArrayBy(a1, a1.length));
            }
            /*SL:1966*/a1[a2++] = _padLastQuad(a3, a5);
        }
        String v2 = /*EL:1968*/this._symbols.findName(a1, a2);
        /*SL:1969*/if (v2 == null) {
            /*SL:1970*/v2 = this.addName(a1, a2, a5);
        }
        /*SL:1972*/return v2;
    }
    
    protected String _handleOddName(int v2) throws IOException {
        /*SL:1984*/if (v2 == 39 && this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            /*SL:1985*/return this._parseAposName();
        }
        /*SL:1988*/if (!this.isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            final char a1 = /*EL:1989*/(char)this._decodeCharForError(v2);
            /*SL:1990*/this._reportUnexpectedChar(a1, "was expecting double-quote to start field name");
        }
        final int[] v3 = /*EL:1996*/CharTypes.getInputCodeUtf8JsNames();
        /*SL:1998*/if (v3[v2] != 0) {
            /*SL:1999*/this._reportUnexpectedChar(v2, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int[] v4 = /*EL:2005*/this._quadBuffer;
        int v5 = /*EL:2006*/0;
        int v6 = /*EL:2007*/0;
        int v7 = /*EL:2008*/0;
        while (true) {
            /*SL:2012*/if (v7 < 4) {
                /*SL:2013*/++v7;
                /*SL:2014*/v6 = (v6 << 8 | v2);
            }
            else {
                /*SL:2016*/if (v5 >= v4.length) {
                    /*SL:2017*/v4 = (this._quadBuffer = ParserBase.growArrayBy(v4, v4.length));
                }
                /*SL:2019*/v4[v5++] = v6;
                /*SL:2020*/v6 = v2;
                /*SL:2021*/v7 = 1;
            }
            /*SL:2023*/if (this._inputPtr >= this._inputEnd && /*EL:2024*/!this._loadMore()) {
                /*SL:2025*/this._reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
            }
            /*SL:2028*/v2 = (this._inputBuffer[this._inputPtr] & 0xFF);
            /*SL:2029*/if (v3[v2] != 0) {
                break;
            }
            /*SL:2032*/++this._inputPtr;
        }
        /*SL:2035*/if (v7 > 0) {
            /*SL:2036*/if (v5 >= v4.length) {
                /*SL:2037*/v4 = (this._quadBuffer = ParserBase.growArrayBy(v4, v4.length));
            }
            /*SL:2039*/v4[v5++] = v6;
        }
        String v8 = /*EL:2041*/this._symbols.findName(v4, v5);
        /*SL:2042*/if (v8 == null) {
            /*SL:2043*/v8 = this.addName(v4, v5, v7);
        }
        /*SL:2045*/return v8;
    }
    
    protected String _parseAposName() throws IOException {
        /*SL:2055*/if (this._inputPtr >= this._inputEnd && /*EL:2056*/!this._loadMore()) {
            /*SL:2057*/this._reportInvalidEOF(": was expecting closing ''' for field name", JsonToken.FIELD_NAME);
        }
        int v1 = /*EL:2060*/this._inputBuffer[this._inputPtr++] & 0xFF;
        /*SL:2061*/if (v1 == 39) {
            /*SL:2062*/return "";
        }
        int[] v2 = /*EL:2064*/this._quadBuffer;
        int v3 = /*EL:2065*/0;
        int v4 = /*EL:2066*/0;
        int v5 = /*EL:2067*/0;
        final int[] v6 = UTF8StreamJsonParser._icLatin1;
        /*SL:2074*/while (v1 != 39) {
            /*SL:2078*/if (v6[v1] != 0 && v1 != 34) {
                /*SL:2079*/if (v1 != 92) {
                    /*SL:2082*/this._throwUnquotedSpace(v1, "name");
                }
                else {
                    /*SL:2085*/v1 = this._decodeEscaped();
                }
                /*SL:2088*/if (v1 > 127) {
                    /*SL:2090*/if (v5 >= 4) {
                        /*SL:2091*/if (v3 >= v2.length) {
                            /*SL:2092*/v2 = (this._quadBuffer = ParserBase.growArrayBy(v2, v2.length));
                        }
                        /*SL:2094*/v2[v3++] = v4;
                        /*SL:2095*/v4 = 0;
                        /*SL:2096*/v5 = 0;
                    }
                    /*SL:2098*/if (v1 < 2048) {
                        /*SL:2099*/v4 = (v4 << 8 | (0xC0 | v1 >> 6));
                        /*SL:2100*/++v5;
                    }
                    else {
                        /*SL:2103*/v4 = (v4 << 8 | (0xE0 | v1 >> 12));
                        /*SL:2106*/if (++v5 >= 4) {
                            /*SL:2107*/if (v3 >= v2.length) {
                                /*SL:2108*/v2 = (this._quadBuffer = ParserBase.growArrayBy(v2, v2.length));
                            }
                            /*SL:2110*/v2[v3++] = v4;
                            /*SL:2111*/v4 = 0;
                            /*SL:2112*/v5 = 0;
                        }
                        /*SL:2114*/v4 = (v4 << 8 | (0x80 | (v1 >> 6 & 0x3F)));
                        /*SL:2115*/++v5;
                    }
                    /*SL:2118*/v1 = (0x80 | (v1 & 0x3F));
                }
            }
            /*SL:2122*/if (v5 < 4) {
                /*SL:2123*/++v5;
                /*SL:2124*/v4 = (v4 << 8 | v1);
            }
            else {
                /*SL:2126*/if (v3 >= v2.length) {
                    /*SL:2127*/v2 = (this._quadBuffer = ParserBase.growArrayBy(v2, v2.length));
                }
                /*SL:2129*/v2[v3++] = v4;
                /*SL:2130*/v4 = v1;
                /*SL:2131*/v5 = 1;
            }
            /*SL:2133*/if (this._inputPtr >= this._inputEnd && /*EL:2134*/!this._loadMore()) {
                /*SL:2135*/this._reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
            }
            /*SL:2138*/v1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
        }
        /*SL:2141*/if (v5 > 0) {
            /*SL:2142*/if (v3 >= v2.length) {
                /*SL:2143*/v2 = (this._quadBuffer = ParserBase.growArrayBy(v2, v2.length));
            }
            /*SL:2145*/v2[v3++] = _padLastQuad(v4, v5);
        }
        String v7 = /*EL:2147*/this._symbols.findName(v2, v3);
        /*SL:2148*/if (v7 == null) {
            /*SL:2149*/v7 = this.addName(v2, v3, v5);
        }
        /*SL:2151*/return v7;
    }
    
    private final String findName(int a1, final int a2) throws JsonParseException {
        /*SL:2162*/a1 = _padLastQuad(a1, a2);
        final String v1 = /*EL:2164*/this._symbols.findName(a1);
        /*SL:2165*/if (v1 != null) {
            /*SL:2166*/return v1;
        }
        /*SL:2169*/this._quadBuffer[0] = a1;
        /*SL:2170*/return this.addName(this._quadBuffer, 1, a2);
    }
    
    private final String findName(final int a1, int a2, final int a3) throws JsonParseException {
        /*SL:2175*/a2 = _padLastQuad(a2, a3);
        final String v1 = /*EL:2177*/this._symbols.findName(a1, a2);
        /*SL:2178*/if (v1 != null) {
            /*SL:2179*/return v1;
        }
        /*SL:2182*/this._quadBuffer[0] = a1;
        /*SL:2183*/this._quadBuffer[1] = a2;
        /*SL:2184*/return this.addName(this._quadBuffer, 2, a3);
    }
    
    private final String findName(final int a1, final int a2, int a3, final int a4) throws JsonParseException {
        /*SL:2189*/a3 = _padLastQuad(a3, a4);
        final String v1 = /*EL:2190*/this._symbols.findName(a1, a2, a3);
        /*SL:2191*/if (v1 != null) {
            /*SL:2192*/return v1;
        }
        final int[] v2 = /*EL:2194*/this._quadBuffer;
        /*SL:2195*/v2[0] = a1;
        /*SL:2196*/v2[1] = a2;
        /*SL:2197*/v2[2] = _padLastQuad(a3, a4);
        /*SL:2198*/return this.addName(v2, 3, a4);
    }
    
    private final String findName(int[] a1, int a2, final int a3, final int a4) throws JsonParseException {
        /*SL:2203*/if (a2 >= a1.length) {
            /*SL:2204*/a1 = (this._quadBuffer = ParserBase.growArrayBy(a1, a1.length));
        }
        /*SL:2206*/a1[a2++] = _padLastQuad(a3, a4);
        final String v1 = /*EL:2207*/this._symbols.findName(a1, a2);
        /*SL:2208*/if (v1 == null) {
            /*SL:2209*/return this.addName(a1, a2, a4);
        }
        /*SL:2211*/return v1;
    }
    
    private final String addName(final int[] v-9, final int v-8, final int v-7) throws JsonParseException {
        final int n = /*EL:2227*/(v-8 << 2) - 4 + v-7;
        final int n2;
        /*SL:2236*/if (v-7 < 4) {
            final int a1 = /*EL:2237*/v-9[v-8 - 1];
            /*SL:2239*/v-9[v-8 - 1] = a1 << (4 - v-7 << 3);
        }
        else {
            /*SL:2241*/n2 = 0;
        }
        char[] array = /*EL:2245*/this._textBuffer.emptyAndGetCurrentSegment();
        int n3 = /*EL:2246*/0;
        int i = /*EL:2248*/0;
        while (i < n) {
            int a4 = /*EL:2249*/v-9[i >> 2];
            int v0 = /*EL:2250*/i & 0x3;
            /*SL:2251*/a4 = (a4 >> (3 - v0 << 3) & 0xFF);
            /*SL:2252*/++i;
            /*SL:2254*/if (a4 > 127) {
                int v = 0;
                /*SL:2256*/if ((a4 & 0xE0) == 0xC0) {
                    /*SL:2257*/a4 &= 0x1F;
                    final int a2 = /*EL:2258*/1;
                }
                else/*SL:2259*/ if ((a4 & 0xF0) == 0xE0) {
                    /*SL:2260*/a4 &= 0xF;
                    final int a3 = /*EL:2261*/2;
                }
                else/*SL:2262*/ if ((a4 & 0xF8) == 0xF0) {
                    /*SL:2263*/a4 &= 0x7;
                    /*SL:2264*/v = 3;
                }
                else {
                    /*SL:2266*/this._reportInvalidInitial(a4);
                    /*SL:2267*/a4 = (v = 1);
                }
                /*SL:2269*/if (i + v > n) {
                    /*SL:2270*/this._reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
                }
                int v2 = /*EL:2274*/v-9[i >> 2];
                /*SL:2275*/v0 = (i & 0x3);
                /*SL:2276*/v2 >>= 3 - v0 << 3;
                /*SL:2277*/++i;
                /*SL:2279*/if ((v2 & 0xC0) != 0x80) {
                    /*SL:2280*/this._reportInvalidOther(v2);
                }
                /*SL:2282*/a4 = (a4 << 6 | (v2 & 0x3F));
                /*SL:2283*/if (v > 1) {
                    /*SL:2284*/v2 = v-9[i >> 2];
                    /*SL:2285*/v0 = (i & 0x3);
                    /*SL:2286*/v2 >>= 3 - v0 << 3;
                    /*SL:2287*/++i;
                    /*SL:2289*/if ((v2 & 0xC0) != 0x80) {
                        /*SL:2290*/this._reportInvalidOther(v2);
                    }
                    /*SL:2292*/a4 = (a4 << 6 | (v2 & 0x3F));
                    /*SL:2293*/if (v > 2) {
                        /*SL:2294*/v2 = v-9[i >> 2];
                        /*SL:2295*/v0 = (i & 0x3);
                        /*SL:2296*/v2 >>= 3 - v0 << 3;
                        /*SL:2297*/++i;
                        /*SL:2298*/if ((v2 & 0xC0) != 0x80) {
                            /*SL:2299*/this._reportInvalidOther(v2 & 0xFF);
                        }
                        /*SL:2301*/a4 = (a4 << 6 | (v2 & 0x3F));
                    }
                }
                /*SL:2304*/if (v > 2) {
                    /*SL:2305*/a4 -= 65536;
                    /*SL:2306*/if (n3 >= array.length) {
                        /*SL:2307*/array = this._textBuffer.expandCurrentSegment();
                    }
                    /*SL:2309*/array[n3++] = (char)(55296 + (a4 >> 10));
                    /*SL:2310*/a4 = (0xDC00 | (a4 & 0x3FF));
                }
            }
            /*SL:2313*/if (n3 >= array.length) {
                /*SL:2314*/array = this._textBuffer.expandCurrentSegment();
            }
            /*SL:2316*/array[n3++] = (char)a4;
        }
        final String v3 = /*EL:2320*/new String(array, 0, n3);
        /*SL:2322*/if (v-7 < 4) {
            /*SL:2323*/v-9[v-8 - 1] = n2;
        }
        /*SL:2325*/return this._symbols.addName(v3, v-9, v-8);
    }
    
    private static final int _padLastQuad(final int a1, final int a2) {
        /*SL:2332*/return (a2 == 4) ? a1 : (a1 | -1 << (a2 << 3));
    }
    
    protected void _loadMoreGuaranteed() throws IOException {
        /*SL:2342*/if (!this._loadMore()) {
            this._reportInvalidEOF();
        }
    }
    
    @Override
    protected void _finishString() throws IOException {
        int i = /*EL:2349*/this._inputPtr;
        /*SL:2350*/if (i >= this._inputEnd) {
            /*SL:2351*/this._loadMoreGuaranteed();
            /*SL:2352*/i = this._inputPtr;
        }
        int n = /*EL:2354*/0;
        final char[] emptyAndGetCurrentSegment = /*EL:2355*/this._textBuffer.emptyAndGetCurrentSegment();
        final int[] icUTF8 = UTF8StreamJsonParser._icUTF8;
        final int min = /*EL:2358*/Math.min(this._inputEnd, i + emptyAndGetCurrentSegment.length);
        final byte[] v0 = /*EL:2359*/this._inputBuffer;
        /*SL:2360*/while (i < min) {
            final int v = /*EL:2361*/v0[i] & 0xFF;
            /*SL:2362*/if (icUTF8[v] != 0) {
                /*SL:2363*/if (v == 34) {
                    /*SL:2364*/this._inputPtr = i + 1;
                    /*SL:2365*/this._textBuffer.setCurrentLength(n);
                    /*SL:2366*/return;
                }
                break;
            }
            else {
                /*SL:2370*/++i;
                /*SL:2371*/emptyAndGetCurrentSegment[n++] = (char)v;
            }
        }
        /*SL:2373*/this._inputPtr = i;
        /*SL:2374*/this._finishString2(emptyAndGetCurrentSegment, n);
    }
    
    protected String _finishAndReturnString() throws IOException {
        int i = /*EL:2383*/this._inputPtr;
        /*SL:2384*/if (i >= this._inputEnd) {
            /*SL:2385*/this._loadMoreGuaranteed();
            /*SL:2386*/i = this._inputPtr;
        }
        int n = /*EL:2388*/0;
        final char[] emptyAndGetCurrentSegment = /*EL:2389*/this._textBuffer.emptyAndGetCurrentSegment();
        final int[] icUTF8 = UTF8StreamJsonParser._icUTF8;
        final int min = /*EL:2392*/Math.min(this._inputEnd, i + emptyAndGetCurrentSegment.length);
        final byte[] v0 = /*EL:2393*/this._inputBuffer;
        /*SL:2394*/while (i < min) {
            final int v = /*EL:2395*/v0[i] & 0xFF;
            /*SL:2396*/if (icUTF8[v] != 0) {
                /*SL:2397*/if (v == 34) {
                    /*SL:2398*/this._inputPtr = i + 1;
                    /*SL:2399*/return this._textBuffer.setCurrentAndReturn(n);
                }
                break;
            }
            else {
                /*SL:2403*/++i;
                /*SL:2404*/emptyAndGetCurrentSegment[n++] = (char)v;
            }
        }
        /*SL:2406*/this._inputPtr = i;
        /*SL:2407*/this._finishString2(emptyAndGetCurrentSegment, n);
        /*SL:2408*/return this._textBuffer.contentsAsString();
    }
    
    private final void _finishString2(final char[] v-5, final int v-4) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: getstatic       com/fasterxml/jackson/core/json/UTF8StreamJsonParser._icUTF8:[I
        //     3: astore          v-2
        //     5: aload_0         /* v-6 */
        //     6: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputBuffer:[B
        //     9: astore          v-1
        //    11: aload_0         /* v-6 */
        //    12: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputPtr:I
        //    15: istore          a2
        //    17: iload           a2
        //    19: aload_0         /* v-6 */
        //    20: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputEnd:I
        //    23: if_icmplt       36
        //    26: aload_0         /* v-6 */
        //    27: invokevirtual   com/fasterxml/jackson/core/json/UTF8StreamJsonParser._loadMoreGuaranteed:()V
        //    30: aload_0         /* v-6 */
        //    31: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputPtr:I
        //    34: istore          a2
        //    36: iload_2         /* v-4 */
        //    37: aload_1         /* v-5 */
        //    38: arraylength    
        //    39: if_icmplt       52
        //    42: aload_0         /* v-6 */
        //    43: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._textBuffer:Lcom/fasterxml/jackson/core/util/TextBuffer;
        //    46: invokevirtual   com/fasterxml/jackson/core/util/TextBuffer.finishCurrentSegment:()[C
        //    49: astore_1        /* v-5 */
        //    50: iconst_0       
        //    51: istore_2        /* v-4 */
        //    52: aload_0         /* v-6 */
        //    53: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputEnd:I
        //    56: iload           a2
        //    58: aload_1         /* v-5 */
        //    59: arraylength    
        //    60: iload_2         /* v-4 */
        //    61: isub           
        //    62: iadd           
        //    63: invokestatic    java/lang/Math.min:(II)I
        //    66: istore          v1
        //    68: iload           a2
        //    70: iload           v1
        //    72: if_icmpge       115
        //    75: aload           v-1
        //    77: iload           a2
        //    79: iinc            a2, 1
        //    82: baload         
        //    83: sipush          255
        //    86: iand           
        //    87: istore_3        /* a1 */
        //    88: aload           v-2
        //    90: iload_3         /* a1 */
        //    91: iaload         
        //    92: ifeq            104
        //    95: aload_0         /* v-6 */
        //    96: iload           a2
        //    98: putfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputPtr:I
        //   101: goto            124
        //   104: aload_1         /* v-5 */
        //   105: iload_2         /* v-4 */
        //   106: iinc            v-4, 1
        //   109: iload_3         /* a1 */
        //   110: i2c            
        //   111: castore        
        //   112: goto            68
        //   115: aload_0         /* v-6 */
        //   116: iload           a2
        //   118: putfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputPtr:I
        //   121: goto            11
        //   124: iload_3         /* v-3 */
        //   125: bipush          34
        //   127: if_icmpne       133
        //   130: goto            315
        //   133: aload           v-2
        //   135: iload_3         /* v-3 */
        //   136: iaload         
        //   137: tableswitch {
        //                2: 168
        //                3: 176
        //                4: 185
        //                5: 216
        //          default: 266
        //        }
        //   168: aload_0         /* v-6 */
        //   169: invokevirtual   com/fasterxml/jackson/core/json/UTF8StreamJsonParser._decodeEscaped:()C
        //   172: istore_3        /* v-3 */
        //   173: goto            288
        //   176: aload_0         /* v-6 */
        //   177: iload_3         /* v-3 */
        //   178: invokespecial   com/fasterxml/jackson/core/json/UTF8StreamJsonParser._decodeUtf8_2:(I)I
        //   181: istore_3        /* v-3 */
        //   182: goto            288
        //   185: aload_0         /* v-6 */
        //   186: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputEnd:I
        //   189: aload_0         /* v-6 */
        //   190: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._inputPtr:I
        //   193: isub           
        //   194: iconst_2       
        //   195: if_icmplt       207
        //   198: aload_0         /* v-6 */
        //   199: iload_3         /* v-3 */
        //   200: invokespecial   com/fasterxml/jackson/core/json/UTF8StreamJsonParser._decodeUtf8_3fast:(I)I
        //   203: istore_3        /* v-3 */
        //   204: goto            288
        //   207: aload_0         /* v-6 */
        //   208: iload_3         /* v-3 */
        //   209: invokespecial   com/fasterxml/jackson/core/json/UTF8StreamJsonParser._decodeUtf8_3:(I)I
        //   212: istore_3        /* v-3 */
        //   213: goto            288
        //   216: aload_0         /* v-6 */
        //   217: iload_3         /* v-3 */
        //   218: invokespecial   com/fasterxml/jackson/core/json/UTF8StreamJsonParser._decodeUtf8_4:(I)I
        //   221: istore_3        /* v-3 */
        //   222: aload_1         /* v-5 */
        //   223: iload_2         /* v-4 */
        //   224: iinc            v-4, 1
        //   227: ldc_w           55296
        //   230: iload_3         /* v-3 */
        //   231: bipush          10
        //   233: ishr           
        //   234: ior            
        //   235: i2c            
        //   236: castore        
        //   237: iload_2         /* v-4 */
        //   238: aload_1         /* v-5 */
        //   239: arraylength    
        //   240: if_icmplt       253
        //   243: aload_0         /* v-6 */
        //   244: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._textBuffer:Lcom/fasterxml/jackson/core/util/TextBuffer;
        //   247: invokevirtual   com/fasterxml/jackson/core/util/TextBuffer.finishCurrentSegment:()[C
        //   250: astore_1        /* v-5 */
        //   251: iconst_0       
        //   252: istore_2        /* v-4 */
        //   253: ldc_w           56320
        //   256: iload_3         /* v-3 */
        //   257: sipush          1023
        //   260: iand           
        //   261: ior            
        //   262: istore_3        /* v-3 */
        //   263: goto            288
        //   266: iload_3         /* v-3 */
        //   267: bipush          32
        //   269: if_icmpge       283
        //   272: aload_0         /* v-6 */
        //   273: iload_3         /* v-3 */
        //   274: ldc_w           "string value"
        //   277: invokevirtual   com/fasterxml/jackson/core/json/UTF8StreamJsonParser._throwUnquotedSpace:(ILjava/lang/String;)V
        //   280: goto            288
        //   283: aload_0         /* v-6 */
        //   284: iload_3         /* v-3 */
        //   285: invokevirtual   com/fasterxml/jackson/core/json/UTF8StreamJsonParser._reportInvalidChar:(I)V
        //   288: iload_2         /* v-4 */
        //   289: aload_1         /* v-5 */
        //   290: arraylength    
        //   291: if_icmplt       304
        //   294: aload_0         /* v-6 */
        //   295: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._textBuffer:Lcom/fasterxml/jackson/core/util/TextBuffer;
        //   298: invokevirtual   com/fasterxml/jackson/core/util/TextBuffer.finishCurrentSegment:()[C
        //   301: astore_1        /* v-5 */
        //   302: iconst_0       
        //   303: istore_2        /* v-4 */
        //   304: aload_1         /* v-5 */
        //   305: iload_2         /* v-4 */
        //   306: iinc            v-4, 1
        //   309: iload_3         /* v-3 */
        //   310: i2c            
        //   311: castore        
        //   312: goto            11
        //   315: aload_0         /* v-6 */
        //   316: getfield        com/fasterxml/jackson/core/json/UTF8StreamJsonParser._textBuffer:Lcom/fasterxml/jackson/core/util/TextBuffer;
        //   319: iload_2         /* v-4 */
        //   320: invokevirtual   com/fasterxml/jackson/core/util/TextBuffer.setCurrentLength:(I)V
        //   323: return         
        //    Exceptions:
        //  throws java.io.IOException
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ------------------------------------------------------
        //  88     27      3     a1    I
        //  17     104     6     a2    I
        //  68     53      7     v1    I
        //  0      324     0     v-6   Lcom/fasterxml/jackson/core/json/UTF8StreamJsonParser;
        //  0      324     1     v-5   [C
        //  0      324     2     v-4   I
        //  124    200     3     v-3   I
        //  5      319     4     v-2   [I
        //  11     313     5     v-1   [B
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected void _skipString() throws IOException {
        /*SL:2502*/this._tokenIncomplete = false;
        final int[] icUTF8 = UTF8StreamJsonParser._icUTF8;
        final byte[] v0 = /*EL:2506*/this._inputBuffer;
    Block_4:
        while (true) {
            int v = /*EL:2514*/this._inputPtr;
            int v2 = /*EL:2515*/this._inputEnd;
            /*SL:2516*/if (v >= v2) {
                /*SL:2517*/this._loadMoreGuaranteed();
                /*SL:2518*/v = this._inputPtr;
                /*SL:2519*/v2 = this._inputEnd;
            }
            /*SL:2521*/while (v < v2) {
                final int v3 = /*EL:2522*/v0[v++] & 0xFF;
                /*SL:2523*/if (icUTF8[v3] != 0) {
                    /*SL:2524*/this._inputPtr = v;
                    /*SL:2531*/if (v3 == 34) {
                        break Block_4;
                    }
                    /*SL:2535*/switch (icUTF8[v3]) {
                        case 1: {
                            /*SL:2537*/this._decodeEscaped();
                            /*SL:2538*/break;
                        }
                        case 2: {
                            /*SL:2540*/this._skipUtf8_2();
                            /*SL:2541*/break;
                        }
                        case 3: {
                            /*SL:2543*/this._skipUtf8_3();
                            /*SL:2544*/break;
                        }
                        case 4: {
                            /*SL:2546*/this._skipUtf8_4(v3);
                            /*SL:2547*/break;
                        }
                        default: {
                            /*SL:2549*/if (v3 < 32) {
                                /*SL:2550*/this._throwUnquotedSpace(v3, "string value");
                                break;
                            }
                            /*SL:2553*/this._reportInvalidChar(v3);
                            break;
                        }
                    }
                    /*SL:2556*/continue Block_4;
                }
            }
            this._inputPtr = v;
        }
    }
    
    protected JsonToken _handleUnexpectedValue(final int a1) throws IOException {
        /*SL:2566*/switch (a1) {
            case 93: {
                /*SL:2575*/if (!this._parsingContext.inArray()) {
                    /*SL:2576*/break;
                }
            }
            case 44: {
                /*SL:2583*/if (this.isEnabled(Feature.ALLOW_MISSING_VALUES)) {
                    /*SL:2584*/--this._inputPtr;
                    /*SL:2585*/return JsonToken.VALUE_NULL;
                }
            }
            case 125: {
                /*SL:2591*/this._reportUnexpectedChar(a1, "expected a value");
            }
            case 39: {
                /*SL:2593*/if (this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    /*SL:2594*/return this._handleApos();
                }
                break;
            }
            case 78: {
                /*SL:2598*/this._matchToken("NaN", 1);
                /*SL:2599*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    /*SL:2600*/return this.resetAsNaN("NaN", Double.NaN);
                }
                /*SL:2602*/this._reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                /*SL:2603*/break;
            }
            case 73: {
                /*SL:2605*/this._matchToken("Infinity", 1);
                /*SL:2606*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    /*SL:2607*/return this.resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
                }
                /*SL:2609*/this._reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                /*SL:2610*/break;
            }
            case 43: {
                /*SL:2612*/if (this._inputPtr >= this._inputEnd && /*EL:2613*/!this._loadMore()) {
                    /*SL:2614*/this._reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_INT);
                }
                /*SL:2617*/return this._handleInvalidNumberStart(this._inputBuffer[this._inputPtr++] & 0xFF, false);
            }
        }
        /*SL:2620*/if (Character.isJavaIdentifierStart(a1)) {
            /*SL:2621*/this._reportInvalidToken("" + (char)a1, "('true', 'false' or 'null')");
        }
        /*SL:2624*/this._reportUnexpectedChar(a1, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        /*SL:2625*/return null;
    }
    
    protected JsonToken _handleApos() throws IOException {
        int n = /*EL:2630*/0;
        int currentLength = /*EL:2632*/0;
        char[] array = /*EL:2633*/this._textBuffer.emptyAndGetCurrentSegment();
        final int[] icUTF8 = UTF8StreamJsonParser._icUTF8;
        final byte[] inputBuffer = /*EL:2637*/this._inputBuffer;
    Block_7:
        while (true) {
            /*SL:2644*/if (this._inputPtr >= this._inputEnd) {
                /*SL:2645*/this._loadMoreGuaranteed();
            }
            /*SL:2647*/if (currentLength >= array.length) {
                /*SL:2648*/array = this._textBuffer.finishCurrentSegment();
                /*SL:2649*/currentLength = 0;
            }
            int v0 = /*EL:2651*/this._inputEnd;
            final int v = /*EL:2653*/this._inputPtr + (array.length - currentLength);
            /*SL:2654*/if (v < v0) {
                /*SL:2655*/v0 = v;
            }
            /*SL:2658*/while (this._inputPtr < v0) {
                /*SL:2659*/n = (inputBuffer[this._inputPtr++] & 0xFF);
                if (/*EL:2660*/n != 39 && icUTF8[n] == 0) {
                    /*SL:2663*/array[currentLength++] = (char)n;
                }
                else {
                    /*SL:2668*/if (n == 39) {
                        break Block_7;
                    }
                    /*SL:2672*/switch (icUTF8[n]) {
                        case 1: {
                            /*SL:2674*/n = this._decodeEscaped();
                            /*SL:2675*/break;
                        }
                        case 2: {
                            /*SL:2677*/n = this._decodeUtf8_2(n);
                            /*SL:2678*/break;
                        }
                        case 3: {
                            /*SL:2680*/if (this._inputEnd - this._inputPtr >= 2) {
                                /*SL:2681*/n = this._decodeUtf8_3fast(n);
                                break;
                            }
                            /*SL:2683*/n = this._decodeUtf8_3(n);
                            /*SL:2685*/break;
                        }
                        case 4: {
                            /*SL:2687*/n = this._decodeUtf8_4(n);
                            /*SL:2689*/array[currentLength++] = (char)(0xD800 | n >> 10);
                            /*SL:2690*/if (currentLength >= array.length) {
                                /*SL:2691*/array = this._textBuffer.finishCurrentSegment();
                                /*SL:2692*/currentLength = 0;
                            }
                            /*SL:2694*/n = (0xDC00 | (n & 0x3FF));
                            /*SL:2696*/break;
                        }
                        default: {
                            /*SL:2698*/if (n < 32) {
                                /*SL:2699*/this._throwUnquotedSpace(n, "string value");
                            }
                            /*SL:2702*/this._reportInvalidChar(n);
                            break;
                        }
                    }
                    /*SL:2705*/if (currentLength >= array.length) {
                        /*SL:2706*/array = this._textBuffer.finishCurrentSegment();
                        /*SL:2707*/currentLength = 0;
                    }
                    /*SL:2710*/array[currentLength++] = (char)n;
                    break;
                }
            }
        }
        /*SL:2712*/this._textBuffer.setCurrentLength(currentLength);
        /*SL:2714*/return JsonToken.VALUE_STRING;
    }
    
    protected JsonToken _handleInvalidNumberStart(int v2, final boolean v3) throws IOException {
        /*SL:2729*/while (v2 == 73) {
            /*SL:2730*/if (this._inputPtr >= this._inputEnd && /*EL:2731*/!this._loadMore()) {
                /*SL:2732*/this._reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_FLOAT);
            }
            /*SL:2735*/v2 = this._inputBuffer[this._inputPtr++];
            final String a2;
            /*SL:2737*/if (v2 == 78) {
                final String a1 = /*EL:2738*/v3 ? "-INF" : "+INF";
            }
            else {
                /*SL:2739*/if (v2 != 110) {
                    break;
                }
                /*SL:2740*/a2 = (v3 ? "-Infinity" : "+Infinity");
            }
            /*SL:2744*/this._matchToken(a2, 3);
            /*SL:2745*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                /*SL:2746*/return this.resetAsNaN(a2, v3 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
            }
            /*SL:2748*/this._reportError("Non-standard token '%s': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow", a2);
        }
        /*SL:2751*/this.reportUnexpectedNumberChar(v2, "expected digit (0-9) to follow minus sign, for valid numeric value");
        /*SL:2752*/return null;
    }
    
    protected final void _matchTrue() throws IOException {
        int inputPtr = /*EL:2758*/this._inputPtr;
        /*SL:2759*/if (inputPtr + 3 < this._inputEnd) {
            final byte[] v0 = /*EL:2760*/this._inputBuffer;
            /*SL:2761*/if (v0[inputPtr++] == 114 && v0[inputPtr++] == 117 && v0[inputPtr++] == 101) {
                final int v = /*EL:2764*/v0[inputPtr] & 0xFF;
                /*SL:2765*/if (v < 48 || v == 93 || v == 125) {
                    /*SL:2766*/this._inputPtr = inputPtr;
                    /*SL:2767*/return;
                }
            }
        }
        /*SL:2771*/this._matchToken2("true", 1);
    }
    
    protected final void _matchFalse() throws IOException {
        int inputPtr = /*EL:2776*/this._inputPtr;
        /*SL:2777*/if (inputPtr + 4 < this._inputEnd) {
            final byte[] v0 = /*EL:2778*/this._inputBuffer;
            /*SL:2779*/if (v0[inputPtr++] == 97 && v0[inputPtr++] == 108 && v0[inputPtr++] == 115 && v0[inputPtr++] == 101) {
                final int v = /*EL:2783*/v0[inputPtr] & 0xFF;
                /*SL:2784*/if (v < 48 || v == 93 || v == 125) {
                    /*SL:2785*/this._inputPtr = inputPtr;
                    /*SL:2786*/return;
                }
            }
        }
        /*SL:2790*/this._matchToken2("false", 1);
    }
    
    protected final void _matchNull() throws IOException {
        int inputPtr = /*EL:2795*/this._inputPtr;
        /*SL:2796*/if (inputPtr + 3 < this._inputEnd) {
            final byte[] v0 = /*EL:2797*/this._inputBuffer;
            /*SL:2798*/if (v0[inputPtr++] == 117 && v0[inputPtr++] == 108 && v0[inputPtr++] == 108) {
                final int v = /*EL:2801*/v0[inputPtr] & 0xFF;
                /*SL:2802*/if (v < 48 || v == 93 || v == 125) {
                    /*SL:2803*/this._inputPtr = inputPtr;
                    /*SL:2804*/return;
                }
            }
        }
        /*SL:2808*/this._matchToken2("null", 1);
    }
    
    protected final void _matchToken(final String a1, int a2) throws IOException {
        final int v1 = /*EL:2813*/a1.length();
        /*SL:2814*/if (this._inputPtr + v1 >= this._inputEnd) {
            /*SL:2815*/this._matchToken2(a1, a2);
            /*SL:2816*/return;
        }
        /*SL:2823*/do {
            if (this._inputBuffer[this._inputPtr] != a1.charAt(a2)) {
                this._reportInvalidToken(a1.substring(0, a2));
            }
            ++this._inputPtr;
        } while (++a2 < v1);
        final int v2 = /*EL:2825*/this._inputBuffer[this._inputPtr] & 0xFF;
        /*SL:2826*/if (v2 >= 48 && v2 != 93 && v2 != 125) {
            /*SL:2827*/this._checkMatchEnd(a1, a2, v2);
        }
    }
    
    private final void _matchToken2(final String a1, int a2) throws IOException {
        final int v1 = /*EL:2833*/a1.length();
        /*SL:2840*/do {
            if ((this._inputPtr >= this._inputEnd && !this._loadMore()) || this._inputBuffer[this._inputPtr] != a1.charAt(a2)) {
                this._reportInvalidToken(a1.substring(0, a2));
            }
            ++this._inputPtr;
        } while (++a2 < v1);
        /*SL:2843*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
            /*SL:2844*/return;
        }
        final int v2 = /*EL:2846*/this._inputBuffer[this._inputPtr] & 0xFF;
        /*SL:2847*/if (v2 >= 48 && v2 != 93 && v2 != 125) {
            /*SL:2848*/this._checkMatchEnd(a1, a2, v2);
        }
    }
    
    private final void _checkMatchEnd(final String a1, final int a2, final int a3) throws IOException {
        final char v1 = /*EL:2854*/(char)this._decodeCharForError(a3);
        /*SL:2855*/if (Character.isJavaIdentifierPart(v1)) {
            /*SL:2856*/this._reportInvalidToken(a1.substring(0, a2));
        }
    }
    
    private final int _skipWS() throws IOException {
        /*SL:2868*/while (this._inputPtr < this._inputEnd) {
            final int v1 = /*EL:2869*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:2870*/if (v1 > 32) {
                /*SL:2871*/if (v1 == 47 || v1 == 35) {
                    /*SL:2872*/--this._inputPtr;
                    /*SL:2873*/return this._skipWS2();
                }
                /*SL:2875*/return v1;
            }
            else {
                /*SL:2877*/if (v1 == 32) {
                    continue;
                }
                /*SL:2878*/if (v1 == 10) {
                    /*SL:2879*/++this._currInputRow;
                    /*SL:2880*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2881*/ if (v1 == 13) {
                    /*SL:2882*/this._skipCR();
                }
                else {
                    /*SL:2883*/if (v1 == 9) {
                        continue;
                    }
                    /*SL:2884*/this._throwInvalidSpace(v1);
                }
            }
        }
        /*SL:2888*/return this._skipWS2();
    }
    
    private final int _skipWS2() throws IOException {
        /*SL:2893*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int v1 = /*EL:2894*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:2895*/if (v1 > 32) {
                /*SL:2896*/if (v1 == 47) {
                    /*SL:2897*/this._skipComment();
                }
                else {
                    /*SL:2900*/if (v1 == 35 && /*EL:2901*/this._skipYAMLComment()) {
                        /*SL:2902*/continue;
                    }
                    /*SL:2905*/return v1;
                }
            }
            else {
                /*SL:2907*/if (v1 == 32) {
                    continue;
                }
                /*SL:2908*/if (v1 == 10) {
                    /*SL:2909*/++this._currInputRow;
                    /*SL:2910*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2911*/ if (v1 == 13) {
                    /*SL:2912*/this._skipCR();
                }
                else {
                    /*SL:2913*/if (v1 == 9) {
                        continue;
                    }
                    /*SL:2914*/this._throwInvalidSpace(v1);
                }
            }
        }
        /*SL:2918*/throw this._constructError("Unexpected end-of-input within/between " + this._parsingContext.typeDesc() + " entries");
    }
    
    private final int _skipWSOrEnd() throws IOException {
        /*SL:2925*/if (this._inputPtr >= this._inputEnd && /*EL:2926*/!this._loadMore()) {
            /*SL:2927*/return this._eofAsNextChar();
        }
        int v1 = /*EL:2930*/this._inputBuffer[this._inputPtr++] & 0xFF;
        /*SL:2931*/if (v1 <= 32) {
            /*SL:2938*/if (v1 != 32) {
                /*SL:2939*/if (v1 == 10) {
                    /*SL:2940*/++this._currInputRow;
                    /*SL:2941*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2942*/ if (v1 == 13) {
                    /*SL:2943*/this._skipCR();
                }
                else/*SL:2944*/ if (v1 != 9) {
                    /*SL:2945*/this._throwInvalidSpace(v1);
                }
            }
            /*SL:2949*/while (this._inputPtr < this._inputEnd) {
                /*SL:2950*/v1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                /*SL:2951*/if (v1 > 32) {
                    /*SL:2952*/if (v1 == 47 || v1 == 35) {
                        /*SL:2953*/--this._inputPtr;
                        /*SL:2954*/return this._skipWSOrEnd2();
                    }
                    /*SL:2956*/return v1;
                }
                else {
                    /*SL:2958*/if (v1 == 32) {
                        continue;
                    }
                    /*SL:2959*/if (v1 == 10) {
                        /*SL:2960*/++this._currInputRow;
                        /*SL:2961*/this._currInputRowStart = this._inputPtr;
                    }
                    else/*SL:2962*/ if (v1 == 13) {
                        /*SL:2963*/this._skipCR();
                    }
                    else {
                        /*SL:2964*/if (v1 == 9) {
                            continue;
                        }
                        /*SL:2965*/this._throwInvalidSpace(v1);
                    }
                }
            }
            /*SL:2969*/return this._skipWSOrEnd2();
        }
        if (v1 == 47 || v1 == 35) {
            --this._inputPtr;
            return this._skipWSOrEnd2();
        }
        return v1;
    }
    
    private final int _skipWSOrEnd2() throws IOException {
        /*SL:2974*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int v1 = /*EL:2975*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:2976*/if (v1 > 32) {
                /*SL:2977*/if (v1 == 47) {
                    /*SL:2978*/this._skipComment();
                }
                else {
                    /*SL:2981*/if (v1 == 35 && /*EL:2982*/this._skipYAMLComment()) {
                        /*SL:2983*/continue;
                    }
                    /*SL:2986*/return v1;
                }
            }
            else {
                /*SL:2987*/if (v1 == 32) {
                    continue;
                }
                /*SL:2988*/if (v1 == 10) {
                    /*SL:2989*/++this._currInputRow;
                    /*SL:2990*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2991*/ if (v1 == 13) {
                    /*SL:2992*/this._skipCR();
                }
                else {
                    /*SL:2993*/if (v1 == 9) {
                        continue;
                    }
                    /*SL:2994*/this._throwInvalidSpace(v1);
                }
            }
        }
        /*SL:2999*/return this._eofAsNextChar();
    }
    
    private final int _skipColon() throws IOException {
        /*SL:3004*/if (this._inputPtr + 4 >= this._inputEnd) {
            /*SL:3005*/return this._skipColon2(false);
        }
        int v1 = /*EL:3008*/this._inputBuffer[this._inputPtr];
        /*SL:3009*/if (v1 == 58) {
            /*SL:3010*/v1 = this._inputBuffer[++this._inputPtr];
            /*SL:3011*/if (v1 <= 32) {
                /*SL:3018*/if (v1 == 32 || v1 == 9) {
                    /*SL:3019*/v1 = this._inputBuffer[++this._inputPtr];
                    /*SL:3020*/if (v1 > 32) {
                        /*SL:3021*/if (v1 == 47 || v1 == 35) {
                            /*SL:3022*/return this._skipColon2(true);
                        }
                        /*SL:3024*/++this._inputPtr;
                        /*SL:3025*/return v1;
                    }
                }
                /*SL:3028*/return this._skipColon2(true);
            }
            if (v1 == 47 || v1 == 35) {
                return this._skipColon2(true);
            }
            ++this._inputPtr;
            return v1;
        }
        else {
            /*SL:3030*/if (v1 == 32 || v1 == 9) {
                /*SL:3031*/v1 = this._inputBuffer[++this._inputPtr];
            }
            /*SL:3033*/if (v1 != 58) {
                /*SL:3054*/return this._skipColon2(false);
            }
            v1 = this._inputBuffer[++this._inputPtr];
            if (v1 <= 32) {
                if (v1 == 32 || v1 == 9) {
                    v1 = this._inputBuffer[++this._inputPtr];
                    if (v1 > 32) {
                        if (v1 == 47 || v1 == 35) {
                            return this._skipColon2(true);
                        }
                        ++this._inputPtr;
                        return v1;
                    }
                }
                return this._skipColon2(true);
            }
            if (v1 == 47 || v1 == 35) {
                return this._skipColon2(true);
            }
            ++this._inputPtr;
            return v1;
        }
    }
    
    private final int _skipColon2(boolean v2) throws IOException {
        /*SL:3059*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int a1 = /*EL:3060*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:3062*/if (a1 > 32) {
                /*SL:3063*/if (a1 == 47) {
                    /*SL:3064*/this._skipComment();
                }
                else {
                    /*SL:3067*/if (a1 == 35 && /*EL:3068*/this._skipYAMLComment()) {
                        /*SL:3069*/continue;
                    }
                    /*SL:3072*/if (v2) {
                        /*SL:3073*/return a1;
                    }
                    /*SL:3075*/if (a1 != 58) {
                        /*SL:3076*/this._reportUnexpectedChar(a1, "was expecting a colon to separate field name and value");
                    }
                    /*SL:3078*/v2 = true;
                }
            }
            else {
                /*SL:3079*/if (a1 == 32) {
                    continue;
                }
                /*SL:3080*/if (a1 == 10) {
                    /*SL:3081*/++this._currInputRow;
                    /*SL:3082*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:3083*/ if (a1 == 13) {
                    /*SL:3084*/this._skipCR();
                }
                else {
                    /*SL:3085*/if (a1 == 9) {
                        continue;
                    }
                    /*SL:3086*/this._throwInvalidSpace(a1);
                }
            }
        }
        /*SL:3090*/this._reportInvalidEOF(" within/between " + this._parsingContext.typeDesc() + " entries", null);
        /*SL:3092*/return -1;
    }
    
    private final void _skipComment() throws IOException {
        /*SL:3097*/if (!this.isEnabled(Feature.ALLOW_COMMENTS)) {
            /*SL:3098*/this._reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        /*SL:3101*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
            /*SL:3102*/this._reportInvalidEOF(" in a comment", null);
        }
        final int v1 = /*EL:3104*/this._inputBuffer[this._inputPtr++] & 0xFF;
        /*SL:3105*/if (v1 == 47) {
            /*SL:3106*/this._skipLine();
        }
        else/*SL:3107*/ if (v1 == 42) {
            /*SL:3108*/this._skipCComment();
        }
        else {
            /*SL:3110*/this._reportUnexpectedChar(v1, "was expecting either '*' or '/' for a comment");
        }
    }
    
    private final void _skipCComment() throws IOException {
        final int[] v0 = /*EL:3117*/CharTypes.getInputCodeComment();
        /*SL:3121*/Label_0216:
        while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int v = /*EL:3122*/this._inputBuffer[this._inputPtr++] & 0xFF;
            final int v2 = /*EL:3123*/v0[v];
            /*SL:3124*/if (v2 != 0) {
                /*SL:3125*/switch (v2) {
                    case 42: {
                        /*SL:3127*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
                            break Label_0216;
                        }
                        /*SL:3130*/if (this._inputBuffer[this._inputPtr] == 47) {
                            /*SL:3131*/++this._inputPtr;
                            /*SL:3132*/return;
                        }
                        continue;
                    }
                    case 10: {
                        /*SL:3136*/++this._currInputRow;
                        /*SL:3137*/this._currInputRowStart = this._inputPtr;
                        /*SL:3138*/continue;
                    }
                    case 13: {
                        /*SL:3140*/this._skipCR();
                        /*SL:3141*/continue;
                    }
                    case 2: {
                        /*SL:3143*/this._skipUtf8_2();
                        /*SL:3144*/continue;
                    }
                    case 3: {
                        /*SL:3146*/this._skipUtf8_3();
                        /*SL:3147*/continue;
                    }
                    case 4: {
                        /*SL:3149*/this._skipUtf8_4(v);
                        /*SL:3150*/continue;
                    }
                    default: {
                        /*SL:3153*/this._reportInvalidChar(v);
                        continue;
                    }
                }
            }
        }
        /*SL:3157*/this._reportInvalidEOF(" in a comment", null);
    }
    
    private final boolean _skipYAMLComment() throws IOException {
        /*SL:3162*/if (!this.isEnabled(Feature.ALLOW_YAML_COMMENTS)) {
            /*SL:3163*/return false;
        }
        /*SL:3165*/this._skipLine();
        /*SL:3166*/return true;
    }
    
    private final void _skipLine() throws IOException {
        final int[] v0 = /*EL:3176*/CharTypes.getInputCodeComment();
        /*SL:3177*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int v = /*EL:3178*/this._inputBuffer[this._inputPtr++] & 0xFF;
            final int v2 = /*EL:3179*/v0[v];
            /*SL:3180*/if (v2 != 0) {
                /*SL:3181*/switch (v2) {
                    case 10: {
                        /*SL:3183*/++this._currInputRow;
                        /*SL:3184*/this._currInputRowStart = this._inputPtr;
                    }
                    case 13: {
                        /*SL:3187*/this._skipCR();
                    }
                    case 42: {
                        /*SL:3190*/continue;
                    }
                    case 2: {
                        /*SL:3192*/this._skipUtf8_2();
                        /*SL:3193*/continue;
                    }
                    case 3: {
                        /*SL:3195*/this._skipUtf8_3();
                        /*SL:3196*/continue;
                    }
                    case 4: {
                        /*SL:3198*/this._skipUtf8_4(v);
                        /*SL:3199*/continue;
                    }
                    default: {
                        /*SL:3201*/if (v2 < 0) {
                            /*SL:3203*/this._reportInvalidChar(v);
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    @Override
    protected char _decodeEscaped() throws IOException {
        /*SL:3213*/if (this._inputPtr >= this._inputEnd && /*EL:3214*/!this._loadMore()) {
            /*SL:3215*/this._reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
        }
        final int v-1 = /*EL:3218*/this._inputBuffer[this._inputPtr++];
        /*SL:3220*/switch (v-1) {
            case 98: {
                /*SL:3223*/return '\b';
            }
            case 116: {
                /*SL:3225*/return '\t';
            }
            case 110: {
                /*SL:3227*/return '\n';
            }
            case 102: {
                /*SL:3229*/return '\f';
            }
            case 114: {
                /*SL:3231*/return '\r';
            }
            case 34:
            case 47:
            case 92: {
                /*SL:3237*/return (char)v-1;
            }
            case 117: {
                int n = /*EL:3247*/0;
                /*SL:3248*/for (int v0 = 0; v0 < 4; ++v0) {
                    /*SL:3249*/if (this._inputPtr >= this._inputEnd && /*EL:3250*/!this._loadMore()) {
                        /*SL:3251*/this._reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
                    }
                    final int v = /*EL:3254*/this._inputBuffer[this._inputPtr++];
                    final int v2 = /*EL:3255*/CharTypes.charToHex(v);
                    /*SL:3256*/if (v2 < 0) {
                        /*SL:3257*/this._reportUnexpectedChar(v, "expected a hex-digit for character escape sequence");
                    }
                    /*SL:3259*/n = (n << 4 | v2);
                }
                /*SL:3261*/return (char)n;
            }
            default: {
                return this._handleUnrecognizedCharacterEscape((char)this._decodeCharForError(v-1));
            }
        }
    }
    
    protected int _decodeCharForError(final int v-1) throws IOException {
        int v0 = /*EL:3266*/v-1 & 0xFF;
        /*SL:3267*/if (v0 > 127) {
            int v = 0;
            /*SL:3271*/if ((v0 & 0xE0) == 0xC0) {
                /*SL:3272*/v0 &= 0x1F;
                final int a1 = /*EL:3273*/1;
            }
            else/*SL:3274*/ if ((v0 & 0xF0) == 0xE0) {
                /*SL:3275*/v0 &= 0xF;
                /*SL:3276*/v = 2;
            }
            else/*SL:3277*/ if ((v0 & 0xF8) == 0xF0) {
                /*SL:3279*/v0 &= 0x7;
                /*SL:3280*/v = 3;
            }
            else {
                /*SL:3282*/this._reportInvalidInitial(v0 & 0xFF);
                /*SL:3283*/v = 1;
            }
            int v2 = /*EL:3286*/this.nextByte();
            /*SL:3287*/if ((v2 & 0xC0) != 0x80) {
                /*SL:3288*/this._reportInvalidOther(v2 & 0xFF);
            }
            /*SL:3290*/v0 = (v0 << 6 | (v2 & 0x3F));
            /*SL:3292*/if (v > 1) {
                /*SL:3293*/v2 = this.nextByte();
                /*SL:3294*/if ((v2 & 0xC0) != 0x80) {
                    /*SL:3295*/this._reportInvalidOther(v2 & 0xFF);
                }
                /*SL:3297*/v0 = (v0 << 6 | (v2 & 0x3F));
                /*SL:3298*/if (v > 2) {
                    /*SL:3299*/v2 = this.nextByte();
                    /*SL:3300*/if ((v2 & 0xC0) != 0x80) {
                        /*SL:3301*/this._reportInvalidOther(v2 & 0xFF);
                    }
                    /*SL:3303*/v0 = (v0 << 6 | (v2 & 0x3F));
                }
            }
        }
        /*SL:3307*/return v0;
    }
    
    private final int _decodeUtf8_2(final int a1) throws IOException {
        /*SL:3318*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3319*/this._loadMoreGuaranteed();
        }
        final int v1 = /*EL:3321*/this._inputBuffer[this._inputPtr++];
        /*SL:3322*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3323*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3325*/return (a1 & 0x1F) << 6 | (v1 & 0x3F);
    }
    
    private final int _decodeUtf8_3(int a1) throws IOException {
        /*SL:3330*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3331*/this._loadMoreGuaranteed();
        }
        /*SL:3333*/a1 &= 0xF;
        int v1 = /*EL:3334*/this._inputBuffer[this._inputPtr++];
        /*SL:3335*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3336*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        int v2 = /*EL:3338*/a1 << 6 | (v1 & 0x3F);
        /*SL:3339*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3340*/this._loadMoreGuaranteed();
        }
        /*SL:3342*/v1 = this._inputBuffer[this._inputPtr++];
        /*SL:3343*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3344*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3346*/v2 = (v2 << 6 | (v1 & 0x3F));
        /*SL:3347*/return v2;
    }
    
    private final int _decodeUtf8_3fast(int a1) throws IOException {
        /*SL:3352*/a1 &= 0xF;
        int v1 = /*EL:3353*/this._inputBuffer[this._inputPtr++];
        /*SL:3354*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3355*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        int v2 = /*EL:3357*/a1 << 6 | (v1 & 0x3F);
        /*SL:3358*/v1 = this._inputBuffer[this._inputPtr++];
        /*SL:3359*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3360*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3362*/v2 = (v2 << 6 | (v1 & 0x3F));
        /*SL:3363*/return v2;
    }
    
    private final int _decodeUtf8_4(int a1) throws IOException {
        /*SL:3372*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3373*/this._loadMoreGuaranteed();
        }
        int v1 = /*EL:3375*/this._inputBuffer[this._inputPtr++];
        /*SL:3376*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3377*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3379*/a1 = ((a1 & 0x7) << 6 | (v1 & 0x3F));
        /*SL:3381*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3382*/this._loadMoreGuaranteed();
        }
        /*SL:3384*/v1 = this._inputBuffer[this._inputPtr++];
        /*SL:3385*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3386*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3388*/a1 = (a1 << 6 | (v1 & 0x3F));
        /*SL:3389*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3390*/this._loadMoreGuaranteed();
        }
        /*SL:3392*/v1 = this._inputBuffer[this._inputPtr++];
        /*SL:3393*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3394*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3400*/return (a1 << 6 | (v1 & 0x3F)) - 65536;
    }
    
    private final void _skipUtf8_2() throws IOException {
        /*SL:3405*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3406*/this._loadMoreGuaranteed();
        }
        final int v1 = /*EL:3408*/this._inputBuffer[this._inputPtr++];
        /*SL:3409*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3410*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
    }
    
    private final void _skipUtf8_3() throws IOException {
        /*SL:3419*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3420*/this._loadMoreGuaranteed();
        }
        int v1 = /*EL:3423*/this._inputBuffer[this._inputPtr++];
        /*SL:3424*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3425*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3427*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3428*/this._loadMoreGuaranteed();
        }
        /*SL:3430*/v1 = this._inputBuffer[this._inputPtr++];
        /*SL:3431*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3432*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
    }
    
    private final void _skipUtf8_4(final int a1) throws IOException {
        /*SL:3438*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3439*/this._loadMoreGuaranteed();
        }
        int v1 = /*EL:3441*/this._inputBuffer[this._inputPtr++];
        /*SL:3442*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3443*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3445*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3446*/this._loadMoreGuaranteed();
        }
        /*SL:3448*/v1 = this._inputBuffer[this._inputPtr++];
        /*SL:3449*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3450*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
        /*SL:3452*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3453*/this._loadMoreGuaranteed();
        }
        /*SL:3455*/v1 = this._inputBuffer[this._inputPtr++];
        /*SL:3456*/if ((v1 & 0xC0) != 0x80) {
            /*SL:3457*/this._reportInvalidOther(v1 & 0xFF, this._inputPtr);
        }
    }
    
    protected final void _skipCR() throws IOException {
        /*SL:3473*/if ((this._inputPtr < this._inputEnd || this._loadMore()) && /*EL:3474*/this._inputBuffer[this._inputPtr] == 10) {
            /*SL:3475*/++this._inputPtr;
        }
        /*SL:3478*/++this._currInputRow;
        /*SL:3479*/this._currInputRowStart = this._inputPtr;
    }
    
    private int nextByte() throws IOException {
        /*SL:3484*/if (this._inputPtr >= this._inputEnd) {
            /*SL:3485*/this._loadMoreGuaranteed();
        }
        /*SL:3487*/return this._inputBuffer[this._inputPtr++] & 0xFF;
    }
    
    protected void _reportInvalidToken(final String a1, final int a2) throws IOException {
        /*SL:3497*/this._inputPtr = a2;
        /*SL:3498*/this._reportInvalidToken(a1, "'null', 'true', 'false' or NaN");
    }
    
    protected void _reportInvalidToken(final String a1) throws IOException {
        /*SL:3502*/this._reportInvalidToken(a1, "'null', 'true', 'false' or NaN");
    }
    
    protected void _reportInvalidToken(final String v2, final String v3) throws IOException {
        final StringBuilder v4 = /*EL:3511*/new StringBuilder(v2);
        /*SL:3512*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int a1 = /*EL:3513*/this._inputBuffer[this._inputPtr++];
            final char a2 = /*EL:3514*/(char)this._decodeCharForError(a1);
            /*SL:3515*/if (!Character.isJavaIdentifierPart(a2)) {
                /*SL:3518*/break;
            }
            /*SL:3520*/v4.append(a2);
            /*SL:3521*/if (v4.length() >= 256) {
                /*SL:3522*/v4.append("...");
                /*SL:3523*/break;
            }
        }
        /*SL:3526*/this._reportError("Unrecognized token '%s': was expecting %s", v4, v3);
    }
    
    protected void _reportInvalidChar(final int a1) throws JsonParseException {
        /*SL:3532*/if (a1 < 32) {
            /*SL:3533*/this._throwInvalidSpace(a1);
        }
        /*SL:3535*/this._reportInvalidInitial(a1);
    }
    
    protected void _reportInvalidInitial(final int a1) throws JsonParseException {
        /*SL:3539*/this._reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(a1));
    }
    
    protected void _reportInvalidOther(final int a1) throws JsonParseException {
        /*SL:3543*/this._reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(a1));
    }
    
    protected void _reportInvalidOther(final int a1, final int a2) throws JsonParseException {
        /*SL:3549*/this._inputPtr = a2;
        /*SL:3550*/this._reportInvalidOther(a1);
    }
    
    protected final byte[] _decodeBase64(final Base64Variant v-2) throws IOException {
        final ByteArrayBuilder getByteArrayBuilder = /*EL:3566*/this._getByteArrayBuilder();
        while (true) {
            /*SL:3572*/if (this._inputPtr >= this._inputEnd) {
                /*SL:3573*/this._loadMoreGuaranteed();
            }
            int a1 = /*EL:3575*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:3576*/if (a1 > 32) {
                int v1 = /*EL:3577*/v-2.decodeBase64Char(a1);
                /*SL:3578*/if (v1 < 0) {
                    /*SL:3579*/if (a1 == 34) {
                        /*SL:3580*/return getByteArrayBuilder.toByteArray();
                    }
                    /*SL:3582*/v1 = this._decodeBase64Escape(v-2, a1, 0);
                    /*SL:3583*/if (v1 < 0) {
                        /*SL:3584*/continue;
                    }
                }
                int v2 = /*EL:3587*/v1;
                /*SL:3591*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:3592*/this._loadMoreGuaranteed();
                }
                /*SL:3594*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                /*SL:3595*/v1 = v-2.decodeBase64Char(a1);
                /*SL:3596*/if (v1 < 0) {
                    /*SL:3597*/v1 = this._decodeBase64Escape(v-2, a1, 1);
                }
                /*SL:3599*/v2 = (v2 << 6 | v1);
                /*SL:3602*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:3603*/this._loadMoreGuaranteed();
                }
                /*SL:3605*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                /*SL:3606*/v1 = v-2.decodeBase64Char(a1);
                /*SL:3609*/if (v1 < 0) {
                    /*SL:3610*/if (v1 != -2) {
                        /*SL:3612*/if (a1 == 34 && !v-2.usesPadding()) {
                            /*SL:3613*/v2 >>= 4;
                            /*SL:3614*/getByteArrayBuilder.append(v2);
                            /*SL:3615*/return getByteArrayBuilder.toByteArray();
                        }
                        /*SL:3617*/v1 = this._decodeBase64Escape(v-2, a1, 2);
                    }
                    /*SL:3619*/if (v1 == -2) {
                        /*SL:3621*/if (this._inputPtr >= this._inputEnd) {
                            /*SL:3622*/this._loadMoreGuaranteed();
                        }
                        /*SL:3624*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                        /*SL:3625*/if (!v-2.usesPaddingChar(a1)) {
                            /*SL:3626*/throw this.reportInvalidBase64Char(v-2, a1, 3, "expected padding character '" + v-2.getPaddingChar() + "'");
                        }
                        /*SL:3629*/v2 >>= 4;
                        /*SL:3630*/getByteArrayBuilder.append(v2);
                        /*SL:3631*/continue;
                    }
                }
                /*SL:3635*/v2 = (v2 << 6 | v1);
                /*SL:3637*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:3638*/this._loadMoreGuaranteed();
                }
                /*SL:3640*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
                /*SL:3641*/v1 = v-2.decodeBase64Char(a1);
                /*SL:3642*/if (v1 < 0) {
                    /*SL:3643*/if (v1 != -2) {
                        /*SL:3645*/if (a1 == 34 && !v-2.usesPadding()) {
                            /*SL:3646*/v2 >>= 2;
                            /*SL:3647*/getByteArrayBuilder.appendTwoBytes(v2);
                            /*SL:3648*/return getByteArrayBuilder.toByteArray();
                        }
                        /*SL:3650*/v1 = this._decodeBase64Escape(v-2, a1, 3);
                    }
                    /*SL:3652*/if (v1 == -2) {
                        /*SL:3659*/v2 >>= 2;
                        /*SL:3660*/getByteArrayBuilder.appendTwoBytes(v2);
                        /*SL:3661*/continue;
                    }
                }
                /*SL:3665*/v2 = (v2 << 6 | v1);
                /*SL:3666*/getByteArrayBuilder.appendThreeBytes(v2);
            }
        }
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        /*SL:3680*/if (this._currToken == JsonToken.FIELD_NAME) {
            final long v1 = /*EL:3681*/this._currInputProcessed + (this._nameStartOffset - 1);
            /*SL:3682*/return new JsonLocation(this._getSourceReference(), v1, -1L, this._nameStartRow, this._nameStartCol);
        }
        /*SL:3685*/return new JsonLocation(this._getSourceReference(), this._tokenInputTotal - 1L, -1L, this._tokenInputRow, this._tokenInputCol);
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        final int v1 = /*EL:3693*/this._inputPtr - this._currInputRowStart + 1;
        /*SL:3694*/return new JsonLocation(this._getSourceReference(), this._currInputProcessed + this._inputPtr, -1L, this._currInputRow, v1);
    }
    
    private final void _updateLocation() {
        /*SL:3702*/this._tokenInputRow = this._currInputRow;
        final int v1 = /*EL:3703*/this._inputPtr;
        /*SL:3704*/this._tokenInputTotal = this._currInputProcessed + v1;
        /*SL:3705*/this._tokenInputCol = v1 - this._currInputRowStart;
    }
    
    private final void _updateNameLocation() {
        /*SL:3711*/this._nameStartRow = this._currInputRow;
        final int v1 = /*EL:3712*/this._inputPtr;
        /*SL:3713*/this._nameStartOffset = v1;
        /*SL:3714*/this._nameStartCol = v1 - this._currInputRowStart;
    }
    
    private final JsonToken _closeScope(final int a1) throws JsonParseException {
        /*SL:3724*/if (a1 == 125) {
            /*SL:3725*/this._closeObjectScope();
            /*SL:3726*/return this._currToken = JsonToken.END_OBJECT;
        }
        /*SL:3728*/this._closeArrayScope();
        /*SL:3729*/return this._currToken = JsonToken.END_ARRAY;
    }
    
    private final void _closeArrayScope() throws JsonParseException {
        /*SL:3733*/this._updateLocation();
        /*SL:3734*/if (!this._parsingContext.inArray()) {
            /*SL:3735*/this._reportMismatchedEndMarker(93, '}');
        }
        /*SL:3737*/this._parsingContext = this._parsingContext.clearAndGetParent();
    }
    
    private final void _closeObjectScope() throws JsonParseException {
        /*SL:3741*/this._updateLocation();
        /*SL:3742*/if (!this._parsingContext.inObject()) {
            /*SL:3743*/this._reportMismatchedEndMarker(125, ']');
        }
        /*SL:3745*/this._parsingContext = this._parsingContext.clearAndGetParent();
    }
    
    static {
        _icUTF8 = CharTypes.getInputCodeUtf8();
        _icLatin1 = CharTypes.getInputCodeLatin1();
        FEAT_MASK_TRAILING_COMMA = Feature.ALLOW_TRAILING_COMMA.getMask();
    }
}
