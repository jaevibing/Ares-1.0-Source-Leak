package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonLocation;
import java.util.Arrays;
import java.io.EOFException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.Base64Variant;
import java.io.Writer;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.io.OutputStream;
import com.fasterxml.jackson.core.io.IOContext;
import java.io.DataInput;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;

public class UTF8DataInputJsonParser extends ParserBase
{
    static final byte BYTE_LF = 10;
    private static final int[] _icUTF8;
    protected static final int[] _icLatin1;
    protected ObjectCodec _objectCodec;
    protected final ByteQuadsCanonicalizer _symbols;
    protected int[] _quadBuffer;
    protected boolean _tokenIncomplete;
    private int _quad1;
    protected DataInput _inputData;
    protected int _nextByte;
    
    public UTF8DataInputJsonParser(final IOContext a1, final int a2, final DataInput a3, final ObjectCodec a4, final ByteQuadsCanonicalizer a5, final int a6) {
        super(a1, a2);
        this._quadBuffer = new int[16];
        this._nextByte = -1;
        this._objectCodec = a4;
        this._symbols = a5;
        this._inputData = a3;
        this._nextByte = a6;
    }
    
    @Override
    public ObjectCodec getCodec() {
        /*SL:121*/return this._objectCodec;
    }
    
    @Override
    public void setCodec(final ObjectCodec a1) {
        /*SL:126*/this._objectCodec = a1;
    }
    
    @Override
    public int releaseBuffered(final OutputStream a1) throws IOException {
        /*SL:137*/return 0;
    }
    
    @Override
    public Object getInputSource() {
        /*SL:142*/return this._inputData;
    }
    
    @Override
    protected void _closeInput() throws IOException {
    }
    
    @Override
    protected void _releaseBuffers() throws IOException {
        /*SL:163*/super._releaseBuffers();
        /*SL:165*/this._symbols.release();
    }
    
    @Override
    public String getText() throws IOException {
        /*SL:177*/if (this._currToken != JsonToken.VALUE_STRING) {
            /*SL:184*/return this._getText2(this._currToken);
        }
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            return this._finishAndReturnString();
        }
        return this._textBuffer.contentsAsString();
    }
    
    @Override
    public int getText(final Writer v-1) throws IOException {
        final JsonToken v0 = /*EL:190*/this._currToken;
        /*SL:191*/if (v0 == JsonToken.VALUE_STRING) {
            /*SL:192*/if (this._tokenIncomplete) {
                /*SL:193*/this._tokenIncomplete = false;
                /*SL:194*/this._finishString();
            }
            /*SL:196*/return this._textBuffer.contentsToWriter(v-1);
        }
        /*SL:198*/if (v0 == JsonToken.FIELD_NAME) {
            final String a1 = /*EL:199*/this._parsingContext.getCurrentName();
            /*SL:200*/v-1.write(a1);
            /*SL:201*/return a1.length();
        }
        /*SL:203*/if (v0 == null) {
            /*SL:211*/return 0;
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
        /*SL:218*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:219*/if (this._tokenIncomplete) {
                /*SL:220*/this._tokenIncomplete = false;
                /*SL:221*/return this._finishAndReturnString();
            }
            /*SL:223*/return this._textBuffer.contentsAsString();
        }
        else {
            /*SL:225*/if (this._currToken == JsonToken.FIELD_NAME) {
                /*SL:226*/return this.getCurrentName();
            }
            /*SL:228*/return super.getValueAsString(null);
        }
    }
    
    @Override
    public String getValueAsString(final String a1) throws IOException {
        /*SL:234*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:235*/if (this._tokenIncomplete) {
                /*SL:236*/this._tokenIncomplete = false;
                /*SL:237*/return this._finishAndReturnString();
            }
            /*SL:239*/return this._textBuffer.contentsAsString();
        }
        else {
            /*SL:241*/if (this._currToken == JsonToken.FIELD_NAME) {
                /*SL:242*/return this.getCurrentName();
            }
            /*SL:244*/return super.getValueAsString(a1);
        }
    }
    
    @Override
    public int getValueAsInt() throws IOException {
        final JsonToken v1 = /*EL:250*/this._currToken;
        /*SL:251*/if (v1 == JsonToken.VALUE_NUMBER_INT || v1 == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:253*/if ((this._numTypesValid & 0x1) == 0x0) {
                /*SL:254*/if (this._numTypesValid == 0) {
                    /*SL:255*/return this._parseIntValue();
                }
                /*SL:257*/if ((this._numTypesValid & 0x1) == 0x0) {
                    /*SL:258*/this.convertNumberToInt();
                }
            }
            /*SL:261*/return this._numberInt;
        }
        /*SL:263*/return super.getValueAsInt(0);
    }
    
    @Override
    public int getValueAsInt(final int a1) throws IOException {
        final JsonToken v1 = /*EL:269*/this._currToken;
        /*SL:270*/if (v1 == JsonToken.VALUE_NUMBER_INT || v1 == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:272*/if ((this._numTypesValid & 0x1) == 0x0) {
                /*SL:273*/if (this._numTypesValid == 0) {
                    /*SL:274*/return this._parseIntValue();
                }
                /*SL:276*/if ((this._numTypesValid & 0x1) == 0x0) {
                    /*SL:277*/this.convertNumberToInt();
                }
            }
            /*SL:280*/return this._numberInt;
        }
        /*SL:282*/return super.getValueAsInt(a1);
    }
    
    protected final String _getText2(final JsonToken a1) {
        /*SL:287*/if (a1 == null) {
            /*SL:288*/return null;
        }
        /*SL:290*/switch (a1.id()) {
            case 5: {
                /*SL:292*/return this._parsingContext.getCurrentName();
            }
            case 6:
            case 7:
            case 8: {
                /*SL:298*/return this._textBuffer.contentsAsString();
            }
            default: {
                /*SL:300*/return a1.asString();
            }
        }
    }
    
    @Override
    public char[] getTextCharacters() throws IOException {
        /*SL:307*/if (this._currToken == null) {
            /*SL:338*/return null;
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
        /*SL:344*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:345*/if (this._tokenIncomplete) {
                /*SL:346*/this._tokenIncomplete = false;
                /*SL:347*/this._finishString();
            }
            /*SL:349*/return this._textBuffer.size();
        }
        /*SL:351*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:352*/return this._parsingContext.getCurrentName().length();
        }
        /*SL:354*/if (this._currToken == null) {
            /*SL:360*/return 0;
        }
        if (this._currToken.isNumeric()) {
            return this._textBuffer.size();
        }
        return this._currToken.asCharArray().length;
    }
    
    @Override
    public int getTextOffset() throws IOException {
        /*SL:367*/if (this._currToken != null) {
            /*SL:368*/switch (this._currToken.id()) {
                case 5: {
                    /*SL:370*/return 0;
                }
                case 6: {
                    /*SL:372*/if (this._tokenIncomplete) {
                        /*SL:373*/this._tokenIncomplete = false;
                        /*SL:374*/this._finishString();
                        return /*EL:379*/this._textBuffer.getTextOffset();
                    }
                    return this._textBuffer.getTextOffset();
                }
                case 7:
                case 8: {
                    return this._textBuffer.getTextOffset();
                }
            }
        }
        /*SL:383*/return 0;
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant v0) throws IOException {
        /*SL:389*/if (this._currToken != JsonToken.VALUE_STRING && (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null)) {
            /*SL:391*/this._reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        /*SL:396*/if (this._tokenIncomplete) {
            try {
                /*SL:398*/this._binaryValue = this._decodeBase64(v0);
            }
            catch (IllegalArgumentException a1) {
                /*SL:400*/throw this._constructError("Failed to decode VALUE_STRING as base64 (" + v0 + "): " + a1.getMessage());
            }
            /*SL:405*/this._tokenIncomplete = false;
        }
        else/*SL:407*/ if (this._binaryValue == null) {
            final ByteArrayBuilder v = /*EL:409*/this._getByteArrayBuilder();
            /*SL:410*/this._decodeBase64(this.getText(), v, v0);
            /*SL:411*/this._binaryValue = v.toByteArray();
        }
        /*SL:414*/return this._binaryValue;
    }
    
    @Override
    public int readBinaryValue(final Base64Variant v1, final OutputStream v2) throws IOException {
        /*SL:421*/if (!this._tokenIncomplete || this._currToken != JsonToken.VALUE_STRING) {
            final byte[] a1 = /*EL:422*/this.getBinaryValue(v1);
            /*SL:423*/v2.write(a1);
            /*SL:424*/return a1.length;
        }
        final byte[] v3 = /*EL:427*/this._ioContext.allocBase64Buffer();
        try {
            /*SL:431*/return this._readBinary(v1, v2, v3);
        }
        finally {
            this._ioContext.releaseBase64Buffer(v3);
        }
    }
    
    protected int _readBinary(final Base64Variant v2, final OutputStream v3, final byte[] v4) throws IOException {
        int v5 = /*EL:438*/0;
        final int v6 = /*EL:439*/v4.length - 3;
        int v7 = /*EL:440*/0;
        while (true) {
            int a1 = /*EL:446*/this._inputData.readUnsignedByte();
            /*SL:447*/if (a1 > 32) {
                int a2 = /*EL:448*/v2.decodeBase64Char(a1);
                /*SL:449*/if (a2 < 0) {
                    /*SL:450*/if (a1 == 34) {
                        /*SL:451*/break;
                    }
                    /*SL:453*/a2 = this._decodeBase64Escape(v2, a1, 0);
                    /*SL:454*/if (a2 < 0) {
                        /*SL:455*/continue;
                    }
                }
                /*SL:460*/if (v5 > v6) {
                    /*SL:461*/v7 += v5;
                    /*SL:462*/v3.write(v4, 0, v5);
                    /*SL:463*/v5 = 0;
                }
                int a3 = /*EL:466*/a2;
                /*SL:469*/a1 = this._inputData.readUnsignedByte();
                /*SL:470*/a2 = v2.decodeBase64Char(a1);
                /*SL:471*/if (a2 < 0) {
                    /*SL:472*/a2 = this._decodeBase64Escape(v2, a1, 1);
                }
                /*SL:474*/a3 = (a3 << 6 | a2);
                /*SL:477*/a1 = this._inputData.readUnsignedByte();
                /*SL:478*/a2 = v2.decodeBase64Char(a1);
                /*SL:481*/if (a2 < 0) {
                    /*SL:482*/if (a2 != -2) {
                        /*SL:484*/if (a1 == 34 && !v2.usesPadding()) {
                            /*SL:485*/a3 >>= 4;
                            /*SL:486*/v4[v5++] = (byte)a3;
                            /*SL:487*/break;
                        }
                        /*SL:489*/a2 = this._decodeBase64Escape(v2, a1, 2);
                    }
                    /*SL:491*/if (a2 == -2) {
                        /*SL:493*/a1 = this._inputData.readUnsignedByte();
                        /*SL:494*/if (!v2.usesPaddingChar(a1)) {
                            /*SL:495*/throw this.reportInvalidBase64Char(v2, a1, 3, "expected padding character '" + v2.getPaddingChar() + "'");
                        }
                        /*SL:498*/a3 >>= 4;
                        /*SL:499*/v4[v5++] = (byte)a3;
                        /*SL:500*/continue;
                    }
                }
                /*SL:504*/a3 = (a3 << 6 | a2);
                /*SL:506*/a1 = this._inputData.readUnsignedByte();
                /*SL:507*/a2 = v2.decodeBase64Char(a1);
                /*SL:508*/if (a2 < 0) {
                    /*SL:509*/if (a2 != -2) {
                        /*SL:511*/if (a1 == 34 && !v2.usesPadding()) {
                            /*SL:512*/a3 >>= 2;
                            /*SL:513*/v4[v5++] = (byte)(a3 >> 8);
                            /*SL:514*/v4[v5++] = (byte)a3;
                            /*SL:515*/break;
                        }
                        /*SL:517*/a2 = this._decodeBase64Escape(v2, a1, 3);
                    }
                    /*SL:519*/if (a2 == -2) {
                        /*SL:526*/a3 >>= 2;
                        /*SL:527*/v4[v5++] = (byte)(a3 >> 8);
                        /*SL:528*/v4[v5++] = (byte)a3;
                        /*SL:529*/continue;
                    }
                }
                /*SL:533*/a3 = (a3 << 6 | a2);
                /*SL:534*/v4[v5++] = (byte)(a3 >> 16);
                /*SL:535*/v4[v5++] = (byte)(a3 >> 8);
                /*SL:536*/v4[v5++] = (byte)a3;
            }
        }
        /*SL:538*/this._tokenIncomplete = false;
        /*SL:539*/if (v5 > 0) {
            /*SL:540*/v7 += v5;
            /*SL:541*/v3.write(v4, 0, v5);
        }
        /*SL:543*/return v7;
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        /*SL:559*/if (this._closed) {
            /*SL:560*/return null;
        }
        /*SL:566*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:567*/return this._nextAfterName();
        }
        /*SL:571*/this._numTypesValid = 0;
        /*SL:572*/if (this._tokenIncomplete) {
            /*SL:573*/this._skipString();
        }
        int v1 = /*EL:575*/this._skipWSOrEnd();
        /*SL:576*/if (v1 < 0) {
            /*SL:578*/this.close();
            /*SL:579*/return this._currToken = null;
        }
        /*SL:582*/this._binaryValue = null;
        /*SL:583*/this._tokenInputRow = this._currInputRow;
        /*SL:586*/if (v1 == 93 || v1 == 125) {
            /*SL:587*/this._closeScope(v1);
            /*SL:588*/return this._currToken;
        }
        /*SL:592*/if (this._parsingContext.expectComma()) {
            /*SL:593*/if (v1 != 44) {
                /*SL:594*/this._reportUnexpectedChar(v1, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
            }
            /*SL:596*/v1 = this._skipWS();
            /*SL:599*/if (Feature.ALLOW_TRAILING_COMMA.enabledIn(this._features) && /*EL:600*/(v1 == 93 || v1 == 125)) {
                /*SL:601*/this._closeScope(v1);
                /*SL:602*/return this._currToken;
            }
        }
        /*SL:611*/if (!this._parsingContext.inObject()) {
            /*SL:612*/return this._nextTokenNotInObject(v1);
        }
        final String v2 = /*EL:615*/this._parseName(v1);
        /*SL:616*/this._parsingContext.setCurrentName(v2);
        /*SL:617*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:619*/v1 = this._skipColon();
        /*SL:622*/if (v1 == 34) {
            /*SL:623*/this._tokenIncomplete = true;
            /*SL:624*/this._nextToken = JsonToken.VALUE_STRING;
            /*SL:625*/return this._currToken;
        }
        JsonToken v3 = null;
        /*SL:629*/switch (v1) {
            case 45: {
                /*SL:631*/v3 = this._parseNegNumber();
                /*SL:632*/break;
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
                /*SL:648*/v3 = this._parsePosNumber(v1);
                /*SL:649*/break;
            }
            case 102: {
                /*SL:651*/this._matchToken("false", 1);
                /*SL:652*/v3 = JsonToken.VALUE_FALSE;
                /*SL:653*/break;
            }
            case 110: {
                /*SL:655*/this._matchToken("null", 1);
                /*SL:656*/v3 = JsonToken.VALUE_NULL;
                /*SL:657*/break;
            }
            case 116: {
                /*SL:659*/this._matchToken("true", 1);
                /*SL:660*/v3 = JsonToken.VALUE_TRUE;
                /*SL:661*/break;
            }
            case 91: {
                /*SL:663*/v3 = JsonToken.START_ARRAY;
                /*SL:664*/break;
            }
            case 123: {
                /*SL:666*/v3 = JsonToken.START_OBJECT;
                /*SL:667*/break;
            }
            default: {
                /*SL:670*/v3 = this._handleUnexpectedValue(v1);
                break;
            }
        }
        /*SL:672*/this._nextToken = v3;
        /*SL:673*/return this._currToken;
    }
    
    private final JsonToken _nextTokenNotInObject(final int a1) throws IOException {
        /*SL:678*/if (a1 == 34) {
            /*SL:679*/this._tokenIncomplete = true;
            /*SL:680*/return this._currToken = JsonToken.VALUE_STRING;
        }
        /*SL:682*/switch (a1) {
            case 91: {
                /*SL:684*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                /*SL:685*/return this._currToken = JsonToken.START_ARRAY;
            }
            case 123: {
                /*SL:687*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                /*SL:688*/return this._currToken = JsonToken.START_OBJECT;
            }
            case 116: {
                /*SL:690*/this._matchToken("true", 1);
                /*SL:691*/return this._currToken = JsonToken.VALUE_TRUE;
            }
            case 102: {
                /*SL:693*/this._matchToken("false", 1);
                /*SL:694*/return this._currToken = JsonToken.VALUE_FALSE;
            }
            case 110: {
                /*SL:696*/this._matchToken("null", 1);
                /*SL:697*/return this._currToken = JsonToken.VALUE_NULL;
            }
            case 45: {
                /*SL:699*/return this._currToken = this._parseNegNumber();
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
                /*SL:714*/return this._currToken = this._parsePosNumber(a1);
            }
            default: {
                /*SL:716*/return this._currToken = this._handleUnexpectedValue(a1);
            }
        }
    }
    
    private final JsonToken _nextAfterName() {
        /*SL:721*/this._nameCopied = false;
        final JsonToken v1 = /*EL:722*/this._nextToken;
        /*SL:723*/this._nextToken = null;
        /*SL:726*/if (v1 == JsonToken.START_ARRAY) {
            /*SL:727*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        }
        else/*SL:728*/ if (v1 == JsonToken.START_OBJECT) {
            /*SL:729*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        /*SL:731*/return this._currToken = v1;
    }
    
    @Override
    public void finishToken() throws IOException {
        /*SL:736*/if (this._tokenIncomplete) {
            /*SL:737*/this._tokenIncomplete = false;
            /*SL:738*/this._finishString();
        }
    }
    
    @Override
    public String nextFieldName() throws IOException {
        /*SL:756*/this._numTypesValid = 0;
        /*SL:757*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:758*/this._nextAfterName();
            /*SL:759*/return null;
        }
        /*SL:761*/if (this._tokenIncomplete) {
            /*SL:762*/this._skipString();
        }
        int v1 = /*EL:764*/this._skipWS();
        /*SL:765*/this._binaryValue = null;
        /*SL:766*/this._tokenInputRow = this._currInputRow;
        /*SL:768*/if (v1 == 93 || v1 == 125) {
            /*SL:769*/this._closeScope(v1);
            /*SL:770*/return null;
        }
        /*SL:774*/if (this._parsingContext.expectComma()) {
            /*SL:775*/if (v1 != 44) {
                /*SL:776*/this._reportUnexpectedChar(v1, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
            }
            /*SL:778*/v1 = this._skipWS();
            /*SL:781*/if (Feature.ALLOW_TRAILING_COMMA.enabledIn(this._features) && /*EL:782*/(v1 == 93 || v1 == 125)) {
                /*SL:783*/this._closeScope(v1);
                /*SL:784*/return null;
            }
        }
        /*SL:789*/if (!this._parsingContext.inObject()) {
            /*SL:790*/this._nextTokenNotInObject(v1);
            /*SL:791*/return null;
        }
        final String v2 = /*EL:794*/this._parseName(v1);
        /*SL:795*/this._parsingContext.setCurrentName(v2);
        /*SL:796*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:798*/v1 = this._skipColon();
        /*SL:799*/if (v1 == 34) {
            /*SL:800*/this._tokenIncomplete = true;
            /*SL:801*/this._nextToken = JsonToken.VALUE_STRING;
            /*SL:802*/return v2;
        }
        JsonToken v3 = null;
        /*SL:805*/switch (v1) {
            case 45: {
                /*SL:807*/v3 = this._parseNegNumber();
                /*SL:808*/break;
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
                /*SL:819*/v3 = this._parsePosNumber(v1);
                /*SL:820*/break;
            }
            case 102: {
                /*SL:822*/this._matchToken("false", 1);
                /*SL:823*/v3 = JsonToken.VALUE_FALSE;
                /*SL:824*/break;
            }
            case 110: {
                /*SL:826*/this._matchToken("null", 1);
                /*SL:827*/v3 = JsonToken.VALUE_NULL;
                /*SL:828*/break;
            }
            case 116: {
                /*SL:830*/this._matchToken("true", 1);
                /*SL:831*/v3 = JsonToken.VALUE_TRUE;
                /*SL:832*/break;
            }
            case 91: {
                /*SL:834*/v3 = JsonToken.START_ARRAY;
                /*SL:835*/break;
            }
            case 123: {
                /*SL:837*/v3 = JsonToken.START_OBJECT;
                /*SL:838*/break;
            }
            default: {
                /*SL:841*/v3 = this._handleUnexpectedValue(v1);
                break;
            }
        }
        /*SL:843*/this._nextToken = v3;
        /*SL:844*/return v2;
    }
    
    @Override
    public String nextTextValue() throws IOException {
        /*SL:851*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:870*/return (this.nextToken() == JsonToken.VALUE_STRING) ? this.getText() : null;
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
        /*SL:877*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:892*/return (this.nextToken() == JsonToken.VALUE_NUMBER_INT) ? this.getIntValue() : v2;
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
        /*SL:899*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:914*/return (this.nextToken() == JsonToken.VALUE_NUMBER_INT) ? this.getLongValue() : v2;
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
        /*SL:921*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:922*/this._nameCopied = false;
            final JsonToken v1 = /*EL:923*/this._nextToken;
            /*SL:924*/this._nextToken = null;
            /*SL:926*/if ((this._currToken = v1) == JsonToken.VALUE_TRUE) {
                /*SL:927*/return Boolean.TRUE;
            }
            /*SL:929*/if (v1 == JsonToken.VALUE_FALSE) {
                /*SL:930*/return Boolean.FALSE;
            }
            /*SL:932*/if (v1 == JsonToken.START_ARRAY) {
                /*SL:933*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            }
            else/*SL:934*/ if (v1 == JsonToken.START_OBJECT) {
                /*SL:935*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            }
            /*SL:937*/return null;
        }
        else {
            final JsonToken v1 = /*EL:940*/this.nextToken();
            /*SL:941*/if (v1 == JsonToken.VALUE_TRUE) {
                /*SL:942*/return Boolean.TRUE;
            }
            /*SL:944*/if (v1 == JsonToken.VALUE_FALSE) {
                /*SL:945*/return Boolean.FALSE;
            }
            /*SL:947*/return null;
        }
    }
    
    protected JsonToken _parsePosNumber(int v-1) throws IOException {
        final char[] v0 = /*EL:973*/this._textBuffer.emptyAndGetCurrentSegment();
        int v = 0;
        /*SL:978*/if (v-1 == 48) {
            /*SL:979*/v-1 = this._handleLeadingZeroes();
            /*SL:980*/if (v-1 <= 57 && v-1 >= 48) {
                final int a1 = /*EL:981*/0;
            }
            else {
                /*SL:983*/v0[0] = '0';
                /*SL:984*/v = 1;
            }
        }
        else {
            /*SL:987*/v0[0] = (char)v-1;
            /*SL:988*/v-1 = this._inputData.readUnsignedByte();
            /*SL:989*/v = 1;
        }
        int v2 = /*EL:991*/v;
        /*SL:994*/while (v-1 <= 57 && v-1 >= 48) {
            /*SL:995*/++v2;
            /*SL:996*/v0[v++] = (char)v-1;
            /*SL:997*/v-1 = this._inputData.readUnsignedByte();
        }
        /*SL:999*/if (v-1 == 46 || v-1 == 101 || v-1 == 69) {
            /*SL:1000*/return this._parseFloat(v0, v, v-1, false, v2);
        }
        /*SL:1002*/this._textBuffer.setCurrentLength(v);
        /*SL:1004*/if (this._parsingContext.inRoot()) {
            /*SL:1005*/this._verifyRootSpace();
        }
        else {
            /*SL:1007*/this._nextByte = v-1;
        }
        /*SL:1010*/return this.resetInt(false, v2);
    }
    
    protected JsonToken _parseNegNumber() throws IOException {
        final char[] v1 = /*EL:1015*/this._textBuffer.emptyAndGetCurrentSegment();
        int v2 = /*EL:1016*/0;
        /*SL:1019*/v1[v2++] = '-';
        int v3 = /*EL:1020*/this._inputData.readUnsignedByte();
        /*SL:1021*/v1[v2++] = (char)v3;
        /*SL:1023*/if (v3 <= 48) {
            /*SL:1025*/if (v3 != 48) {
                /*SL:1028*/return this._handleInvalidNumberStart(v3, true);
            }
            v3 = this._handleLeadingZeroes();
        }
        else {
            /*SL:1031*/if (v3 > 57) {
                /*SL:1032*/return this._handleInvalidNumberStart(v3, true);
            }
            /*SL:1034*/v3 = this._inputData.readUnsignedByte();
        }
        int v4 = /*EL:1037*/1;
        /*SL:1040*/while (v3 <= 57 && v3 >= 48) {
            /*SL:1041*/++v4;
            /*SL:1042*/v1[v2++] = (char)v3;
            /*SL:1043*/v3 = this._inputData.readUnsignedByte();
        }
        /*SL:1045*/if (v3 == 46 || v3 == 101 || v3 == 69) {
            /*SL:1046*/return this._parseFloat(v1, v2, v3, true, v4);
        }
        /*SL:1048*/this._textBuffer.setCurrentLength(v2);
        /*SL:1050*/this._nextByte = v3;
        /*SL:1051*/if (this._parsingContext.inRoot()) {
            /*SL:1052*/this._verifyRootSpace();
        }
        /*SL:1055*/return this.resetInt(true, v4);
    }
    
    private final int _handleLeadingZeroes() throws IOException {
        int v1 = /*EL:1067*/this._inputData.readUnsignedByte();
        /*SL:1069*/if (v1 < 48 || v1 > 57) {
            /*SL:1070*/return v1;
        }
        /*SL:1073*/if (!this.isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            /*SL:1074*/this.reportInvalidNumber("Leading zeroes not allowed");
        }
        /*SL:1077*/while (v1 == 48) {
            /*SL:1078*/v1 = this._inputData.readUnsignedByte();
        }
        /*SL:1080*/return v1;
    }
    
    private final JsonToken _parseFloat(char[] a1, int a2, int a3, final boolean a4, final int a5) throws IOException {
        int v1 = /*EL:1086*/0;
        /*SL:1089*/if (a3 == 46) {
            /*SL:1090*/a1[a2++] = (char)a3;
            while (true) {
                /*SL:1094*/a3 = this._inputData.readUnsignedByte();
                if (/*EL:1095*/a3 < 48 || a3 > 57) {
                    break;
                }
                /*SL:1098*/++v1;
                /*SL:1099*/if (a2 >= a1.length) {
                    /*SL:1100*/a1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1101*/a2 = 0;
                }
                /*SL:1103*/a1[a2++] = (char)a3;
            }
            /*SL:1106*/if (v1 == 0) {
                /*SL:1107*/this.reportUnexpectedNumberChar(a3, "Decimal point not followed by a digit");
            }
        }
        int v2 = /*EL:1111*/0;
        /*SL:1112*/if (a3 == 101 || a3 == 69) {
            /*SL:1113*/if (a2 >= a1.length) {
                /*SL:1114*/a1 = this._textBuffer.finishCurrentSegment();
                /*SL:1115*/a2 = 0;
            }
            /*SL:1117*/a1[a2++] = (char)a3;
            /*SL:1118*/a3 = this._inputData.readUnsignedByte();
            /*SL:1120*/if (a3 == 45 || a3 == 43) {
                /*SL:1121*/if (a2 >= a1.length) {
                    /*SL:1122*/a1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1123*/a2 = 0;
                }
                /*SL:1125*/a1[a2++] = (char)a3;
                /*SL:1126*/a3 = this._inputData.readUnsignedByte();
            }
            /*SL:1128*/while (a3 <= 57 && a3 >= 48) {
                /*SL:1129*/++v2;
                /*SL:1130*/if (a2 >= a1.length) {
                    /*SL:1131*/a1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1132*/a2 = 0;
                }
                /*SL:1134*/a1[a2++] = (char)a3;
                /*SL:1135*/a3 = this._inputData.readUnsignedByte();
            }
            /*SL:1138*/if (v2 == 0) {
                /*SL:1139*/this.reportUnexpectedNumberChar(a3, "Exponent indicator not followed by a digit");
            }
        }
        /*SL:1145*/this._nextByte = a3;
        /*SL:1146*/if (this._parsingContext.inRoot()) {
            /*SL:1147*/this._verifyRootSpace();
        }
        /*SL:1149*/this._textBuffer.setCurrentLength(a2);
        /*SL:1152*/return this.resetFloat(a4, a5, v1, v2);
    }
    
    private final void _verifyRootSpace() throws IOException {
        final int v1 = /*EL:1165*/this._nextByte;
        /*SL:1166*/if (v1 <= 32) {
            /*SL:1167*/this._nextByte = -1;
            /*SL:1168*/if (v1 == 13 || v1 == 10) {
                /*SL:1169*/++this._currInputRow;
            }
            /*SL:1171*/return;
        }
        /*SL:1173*/this._reportMissingRootWS(v1);
    }
    
    protected final String _parseName(int a1) throws IOException {
        /*SL:1184*/if (a1 != 34) {
            /*SL:1185*/return this._handleOddName(a1);
        }
        final int[] v1 = UTF8DataInputJsonParser._icLatin1;
        int v2 = /*EL:1195*/this._inputData.readUnsignedByte();
        /*SL:1197*/if (v1[v2] == 0) {
            /*SL:1198*/a1 = this._inputData.readUnsignedByte();
            /*SL:1199*/if (v1[a1] == 0) {
                /*SL:1200*/v2 = (v2 << 8 | a1);
                /*SL:1201*/a1 = this._inputData.readUnsignedByte();
                /*SL:1202*/if (v1[a1] == 0) {
                    /*SL:1203*/v2 = (v2 << 8 | a1);
                    /*SL:1204*/a1 = this._inputData.readUnsignedByte();
                    /*SL:1205*/if (v1[a1] == 0) {
                        /*SL:1206*/v2 = (v2 << 8 | a1);
                        /*SL:1207*/a1 = this._inputData.readUnsignedByte();
                        /*SL:1208*/if (v1[a1] == 0) {
                            /*SL:1209*/this._quad1 = v2;
                            /*SL:1210*/return this._parseMediumName(a1);
                        }
                        /*SL:1212*/if (a1 == 34) {
                            /*SL:1213*/return this.findName(v2, 4);
                        }
                        /*SL:1215*/return this.parseName(v2, a1, 4);
                    }
                    else {
                        /*SL:1217*/if (a1 == 34) {
                            /*SL:1218*/return this.findName(v2, 3);
                        }
                        /*SL:1220*/return this.parseName(v2, a1, 3);
                    }
                }
                else {
                    /*SL:1222*/if (a1 == 34) {
                        /*SL:1223*/return this.findName(v2, 2);
                    }
                    /*SL:1225*/return this.parseName(v2, a1, 2);
                }
            }
            else {
                /*SL:1227*/if (a1 == 34) {
                    /*SL:1228*/return this.findName(v2, 1);
                }
                /*SL:1230*/return this.parseName(v2, a1, 1);
            }
        }
        else {
            /*SL:1232*/if (v2 == 34) {
                /*SL:1233*/return "";
            }
            /*SL:1235*/return this.parseName(0, v2, 0);
        }
    }
    
    private final String _parseMediumName(int a1) throws IOException {
        final int[] v1 = UTF8DataInputJsonParser._icLatin1;
        int v2 = /*EL:1243*/this._inputData.readUnsignedByte();
        /*SL:1244*/if (v1[v2] != 0) {
            /*SL:1245*/if (v2 == 34) {
                /*SL:1246*/return this.findName(this._quad1, a1, 1);
            }
            /*SL:1248*/return this.parseName(this._quad1, a1, v2, 1);
        }
        else {
            /*SL:1250*/a1 = (a1 << 8 | v2);
            /*SL:1251*/v2 = this._inputData.readUnsignedByte();
            /*SL:1252*/if (v1[v2] != 0) {
                /*SL:1253*/if (v2 == 34) {
                    /*SL:1254*/return this.findName(this._quad1, a1, 2);
                }
                /*SL:1256*/return this.parseName(this._quad1, a1, v2, 2);
            }
            else {
                /*SL:1258*/a1 = (a1 << 8 | v2);
                /*SL:1259*/v2 = this._inputData.readUnsignedByte();
                /*SL:1260*/if (v1[v2] != 0) {
                    /*SL:1261*/if (v2 == 34) {
                        /*SL:1262*/return this.findName(this._quad1, a1, 3);
                    }
                    /*SL:1264*/return this.parseName(this._quad1, a1, v2, 3);
                }
                else {
                    /*SL:1266*/a1 = (a1 << 8 | v2);
                    /*SL:1267*/v2 = this._inputData.readUnsignedByte();
                    /*SL:1268*/if (v1[v2] == 0) {
                        /*SL:1274*/return this._parseMediumName2(v2, a1);
                    }
                    if (v2 == 34) {
                        return this.findName(this._quad1, a1, 4);
                    }
                    return this.parseName(this._quad1, a1, v2, 4);
                }
            }
        }
    }
    
    private final String _parseMediumName2(int a1, final int a2) throws IOException {
        final int[] v1 = UTF8DataInputJsonParser._icLatin1;
        int v2 = /*EL:1282*/this._inputData.readUnsignedByte();
        /*SL:1283*/if (v1[v2] != 0) {
            /*SL:1284*/if (v2 == 34) {
                /*SL:1285*/return this.findName(this._quad1, a2, a1, 1);
            }
            /*SL:1287*/return this.parseName(this._quad1, a2, a1, v2, 1);
        }
        else {
            /*SL:1289*/a1 = (a1 << 8 | v2);
            /*SL:1290*/v2 = this._inputData.readUnsignedByte();
            /*SL:1291*/if (v1[v2] != 0) {
                /*SL:1292*/if (v2 == 34) {
                    /*SL:1293*/return this.findName(this._quad1, a2, a1, 2);
                }
                /*SL:1295*/return this.parseName(this._quad1, a2, a1, v2, 2);
            }
            else {
                /*SL:1297*/a1 = (a1 << 8 | v2);
                /*SL:1298*/v2 = this._inputData.readUnsignedByte();
                /*SL:1299*/if (v1[v2] != 0) {
                    /*SL:1300*/if (v2 == 34) {
                        /*SL:1301*/return this.findName(this._quad1, a2, a1, 3);
                    }
                    /*SL:1303*/return this.parseName(this._quad1, a2, a1, v2, 3);
                }
                else {
                    /*SL:1305*/a1 = (a1 << 8 | v2);
                    /*SL:1306*/v2 = this._inputData.readUnsignedByte();
                    /*SL:1307*/if (v1[v2] == 0) {
                        /*SL:1313*/return this._parseLongName(v2, a2, a1);
                    }
                    if (v2 == 34) {
                        return this.findName(this._quad1, a2, a1, 4);
                    }
                    return this.parseName(this._quad1, a2, a1, v2, 4);
                }
            }
        }
    }
    
    private final String _parseLongName(int a3, final int v1, final int v2) throws IOException {
        /*SL:1318*/this._quadBuffer[0] = this._quad1;
        /*SL:1319*/this._quadBuffer[1] = v1;
        /*SL:1320*/this._quadBuffer[2] = v2;
        final int[] v3 = UTF8DataInputJsonParser._icLatin1;
        int v4 = /*EL:1324*/3;
        while (true) {
            int a4 = /*EL:1327*/this._inputData.readUnsignedByte();
            /*SL:1328*/if (v3[a4] != 0) {
                /*SL:1329*/if (a4 == 34) {
                    /*SL:1330*/return this.findName(this._quadBuffer, v4, a3, 1);
                }
                /*SL:1332*/return this.parseEscapedName(this._quadBuffer, v4, a3, a4, 1);
            }
            else {
                /*SL:1335*/a3 = (a3 << 8 | a4);
                /*SL:1336*/a4 = this._inputData.readUnsignedByte();
                /*SL:1337*/if (v3[a4] != 0) {
                    /*SL:1338*/if (a4 == 34) {
                        /*SL:1339*/return this.findName(this._quadBuffer, v4, a3, 2);
                    }
                    /*SL:1341*/return this.parseEscapedName(this._quadBuffer, v4, a3, a4, 2);
                }
                else {
                    /*SL:1344*/a3 = (a3 << 8 | a4);
                    /*SL:1345*/a4 = this._inputData.readUnsignedByte();
                    /*SL:1346*/if (v3[a4] != 0) {
                        /*SL:1347*/if (a4 == 34) {
                            /*SL:1348*/return this.findName(this._quadBuffer, v4, a3, 3);
                        }
                        /*SL:1350*/return this.parseEscapedName(this._quadBuffer, v4, a3, a4, 3);
                    }
                    else {
                        /*SL:1353*/a3 = (a3 << 8 | a4);
                        /*SL:1354*/a4 = this._inputData.readUnsignedByte();
                        /*SL:1355*/if (v3[a4] != 0) {
                            /*SL:1356*/if (a4 == 34) {
                                /*SL:1357*/return this.findName(this._quadBuffer, v4, a3, 4);
                            }
                            /*SL:1359*/return this.parseEscapedName(this._quadBuffer, v4, a3, a4, 4);
                        }
                        else {
                            /*SL:1363*/if (v4 >= this._quadBuffer.length) {
                                /*SL:1364*/this._quadBuffer = _growArrayBy(this._quadBuffer, v4);
                            }
                            /*SL:1366*/this._quadBuffer[v4++] = a3;
                            /*SL:1367*/a3 = a4;
                        }
                    }
                }
            }
        }
    }
    
    private final String parseName(final int a1, final int a2, final int a3) throws IOException {
        /*SL:1372*/return this.parseEscapedName(this._quadBuffer, 0, a1, a2, a3);
    }
    
    private final String parseName(final int a1, final int a2, final int a3, final int a4) throws IOException {
        /*SL:1376*/this._quadBuffer[0] = a1;
        /*SL:1377*/return this.parseEscapedName(this._quadBuffer, 1, a2, a3, a4);
    }
    
    private final String parseName(final int a1, final int a2, final int a3, final int a4, final int a5) throws IOException {
        /*SL:1381*/this._quadBuffer[0] = a1;
        /*SL:1382*/this._quadBuffer[1] = a2;
        /*SL:1383*/return this.parseEscapedName(this._quadBuffer, 2, a3, a4, a5);
    }
    
    protected final String parseEscapedName(int[] a1, int a2, int a3, int a4, int a5) throws IOException {
        final int[] v1 = UTF8DataInputJsonParser._icLatin1;
        while (true) {
            /*SL:1403*/if (v1[a4] != 0) {
                /*SL:1404*/if (a4 == 34) {
                    break;
                }
                /*SL:1408*/if (a4 != 92) {
                    /*SL:1410*/this._throwUnquotedSpace(a4, "name");
                }
                else {
                    /*SL:1413*/a4 = this._decodeEscaped();
                }
                /*SL:1420*/if (a4 > 127) {
                    /*SL:1422*/if (a5 >= 4) {
                        /*SL:1423*/if (a2 >= a1.length) {
                            /*SL:1424*/a1 = (this._quadBuffer = _growArrayBy(a1, a1.length));
                        }
                        /*SL:1426*/a1[a2++] = a3;
                        /*SL:1427*/a3 = 0;
                        /*SL:1428*/a5 = 0;
                    }
                    /*SL:1430*/if (a4 < 2048) {
                        /*SL:1431*/a3 = (a3 << 8 | (0xC0 | a4 >> 6));
                        /*SL:1432*/++a5;
                    }
                    else {
                        /*SL:1435*/a3 = (a3 << 8 | (0xE0 | a4 >> 12));
                        /*SL:1438*/if (++a5 >= 4) {
                            /*SL:1439*/if (a2 >= a1.length) {
                                /*SL:1440*/a1 = (this._quadBuffer = _growArrayBy(a1, a1.length));
                            }
                            /*SL:1442*/a1[a2++] = a3;
                            /*SL:1443*/a3 = 0;
                            /*SL:1444*/a5 = 0;
                        }
                        /*SL:1446*/a3 = (a3 << 8 | (0x80 | (a4 >> 6 & 0x3F)));
                        /*SL:1447*/++a5;
                    }
                    /*SL:1450*/a4 = (0x80 | (a4 & 0x3F));
                }
            }
            /*SL:1454*/if (a5 < 4) {
                /*SL:1455*/++a5;
                /*SL:1456*/a3 = (a3 << 8 | a4);
            }
            else {
                /*SL:1458*/if (a2 >= a1.length) {
                    /*SL:1459*/a1 = (this._quadBuffer = _growArrayBy(a1, a1.length));
                }
                /*SL:1461*/a1[a2++] = a3;
                /*SL:1462*/a3 = a4;
                /*SL:1463*/a5 = 1;
            }
            /*SL:1465*/a4 = this._inputData.readUnsignedByte();
        }
        /*SL:1468*/if (a5 > 0) {
            /*SL:1469*/if (a2 >= a1.length) {
                /*SL:1470*/a1 = (this._quadBuffer = _growArrayBy(a1, a1.length));
            }
            /*SL:1472*/a1[a2++] = pad(a3, a5);
        }
        String v2 = /*EL:1474*/this._symbols.findName(a1, a2);
        /*SL:1475*/if (v2 == null) {
            /*SL:1476*/v2 = this.addName(a1, a2, a5);
        }
        /*SL:1478*/return v2;
    }
    
    protected String _handleOddName(int v2) throws IOException {
        /*SL:1489*/if (v2 == 39 && this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            /*SL:1490*/return this._parseAposName();
        }
        /*SL:1492*/if (!this.isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            final char a1 = /*EL:1493*/(char)this._decodeCharForError(v2);
            /*SL:1494*/this._reportUnexpectedChar(a1, "was expecting double-quote to start field name");
        }
        final int[] v3 = /*EL:1500*/CharTypes.getInputCodeUtf8JsNames();
        /*SL:1502*/if (v3[v2] != 0) {
            /*SL:1503*/this._reportUnexpectedChar(v2, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int[] v4 = /*EL:1510*/this._quadBuffer;
        int v5 = /*EL:1511*/0;
        int v6 = /*EL:1512*/0;
        int v7 = /*EL:1513*/0;
        /*SL:1529*/do {
            if (v7 < 4) {
                ++v7;
                v6 = (v6 << 8 | v2);
            }
            else {
                if (v5 >= v4.length) {
                    v4 = (this._quadBuffer = _growArrayBy(v4, v4.length));
                }
                v4[v5++] = v6;
                v6 = v2;
                v7 = 1;
            }
            v2 = this._inputData.readUnsignedByte();
        } while (v3[v2] == 0);
        /*SL:1534*/this._nextByte = v2;
        /*SL:1535*/if (v7 > 0) {
            /*SL:1536*/if (v5 >= v4.length) {
                /*SL:1537*/v4 = (this._quadBuffer = _growArrayBy(v4, v4.length));
            }
            /*SL:1539*/v4[v5++] = v6;
        }
        String v8 = /*EL:1541*/this._symbols.findName(v4, v5);
        /*SL:1542*/if (v8 == null) {
            /*SL:1543*/v8 = this.addName(v4, v5, v7);
        }
        /*SL:1545*/return v8;
    }
    
    protected String _parseAposName() throws IOException {
        int v1 = /*EL:1556*/this._inputData.readUnsignedByte();
        /*SL:1557*/if (v1 == 39) {
            /*SL:1558*/return "";
        }
        int[] v2 = /*EL:1560*/this._quadBuffer;
        int v3 = /*EL:1561*/0;
        int v4 = /*EL:1562*/0;
        int v5 = /*EL:1563*/0;
        final int[] v6 = UTF8DataInputJsonParser._icLatin1;
        /*SL:1570*/while (v1 != 39) {
            /*SL:1574*/if (v1 != 34 && v6[v1] != 0) {
                /*SL:1575*/if (v1 != 92) {
                    /*SL:1578*/this._throwUnquotedSpace(v1, "name");
                }
                else {
                    /*SL:1581*/v1 = this._decodeEscaped();
                }
                /*SL:1587*/if (v1 > 127) {
                    /*SL:1589*/if (v5 >= 4) {
                        /*SL:1590*/if (v3 >= v2.length) {
                            /*SL:1591*/v2 = (this._quadBuffer = _growArrayBy(v2, v2.length));
                        }
                        /*SL:1593*/v2[v3++] = v4;
                        /*SL:1594*/v4 = 0;
                        /*SL:1595*/v5 = 0;
                    }
                    /*SL:1597*/if (v1 < 2048) {
                        /*SL:1598*/v4 = (v4 << 8 | (0xC0 | v1 >> 6));
                        /*SL:1599*/++v5;
                    }
                    else {
                        /*SL:1602*/v4 = (v4 << 8 | (0xE0 | v1 >> 12));
                        /*SL:1605*/if (++v5 >= 4) {
                            /*SL:1606*/if (v3 >= v2.length) {
                                /*SL:1607*/v2 = (this._quadBuffer = _growArrayBy(v2, v2.length));
                            }
                            /*SL:1609*/v2[v3++] = v4;
                            /*SL:1610*/v4 = 0;
                            /*SL:1611*/v5 = 0;
                        }
                        /*SL:1613*/v4 = (v4 << 8 | (0x80 | (v1 >> 6 & 0x3F)));
                        /*SL:1614*/++v5;
                    }
                    /*SL:1617*/v1 = (0x80 | (v1 & 0x3F));
                }
            }
            /*SL:1621*/if (v5 < 4) {
                /*SL:1622*/++v5;
                /*SL:1623*/v4 = (v4 << 8 | v1);
            }
            else {
                /*SL:1625*/if (v3 >= v2.length) {
                    /*SL:1626*/v2 = (this._quadBuffer = _growArrayBy(v2, v2.length));
                }
                /*SL:1628*/v2[v3++] = v4;
                /*SL:1629*/v4 = v1;
                /*SL:1630*/v5 = 1;
            }
            /*SL:1632*/v1 = this._inputData.readUnsignedByte();
        }
        /*SL:1635*/if (v5 > 0) {
            /*SL:1636*/if (v3 >= v2.length) {
                /*SL:1637*/v2 = (this._quadBuffer = _growArrayBy(v2, v2.length));
            }
            /*SL:1639*/v2[v3++] = pad(v4, v5);
        }
        String v7 = /*EL:1641*/this._symbols.findName(v2, v3);
        /*SL:1642*/if (v7 == null) {
            /*SL:1643*/v7 = this.addName(v2, v3, v5);
        }
        /*SL:1645*/return v7;
    }
    
    private final String findName(int a1, final int a2) throws JsonParseException {
        /*SL:1656*/a1 = pad(a1, a2);
        final String v1 = /*EL:1658*/this._symbols.findName(a1);
        /*SL:1659*/if (v1 != null) {
            /*SL:1660*/return v1;
        }
        /*SL:1663*/this._quadBuffer[0] = a1;
        /*SL:1664*/return this.addName(this._quadBuffer, 1, a2);
    }
    
    private final String findName(final int a1, int a2, final int a3) throws JsonParseException {
        /*SL:1669*/a2 = pad(a2, a3);
        final String v1 = /*EL:1671*/this._symbols.findName(a1, a2);
        /*SL:1672*/if (v1 != null) {
            /*SL:1673*/return v1;
        }
        /*SL:1676*/this._quadBuffer[0] = a1;
        /*SL:1677*/this._quadBuffer[1] = a2;
        /*SL:1678*/return this.addName(this._quadBuffer, 2, a3);
    }
    
    private final String findName(final int a1, final int a2, int a3, final int a4) throws JsonParseException {
        /*SL:1683*/a3 = pad(a3, a4);
        final String v1 = /*EL:1684*/this._symbols.findName(a1, a2, a3);
        /*SL:1685*/if (v1 != null) {
            /*SL:1686*/return v1;
        }
        final int[] v2 = /*EL:1688*/this._quadBuffer;
        /*SL:1689*/v2[0] = a1;
        /*SL:1690*/v2[1] = a2;
        /*SL:1691*/v2[2] = pad(a3, a4);
        /*SL:1692*/return this.addName(v2, 3, a4);
    }
    
    private final String findName(int[] a1, int a2, final int a3, final int a4) throws JsonParseException {
        /*SL:1697*/if (a2 >= a1.length) {
            /*SL:1698*/a1 = (this._quadBuffer = _growArrayBy(a1, a1.length));
        }
        /*SL:1700*/a1[a2++] = pad(a3, a4);
        final String v1 = /*EL:1701*/this._symbols.findName(a1, a2);
        /*SL:1702*/if (v1 == null) {
            /*SL:1703*/return this.addName(a1, a2, a4);
        }
        /*SL:1705*/return v1;
    }
    
    private final String addName(final int[] v-9, final int v-8, final int v-7) throws JsonParseException {
        final int n = /*EL:1721*/(v-8 << 2) - 4 + v-7;
        final int n2;
        /*SL:1730*/if (v-7 < 4) {
            final int a1 = /*EL:1731*/v-9[v-8 - 1];
            /*SL:1733*/v-9[v-8 - 1] = a1 << (4 - v-7 << 3);
        }
        else {
            /*SL:1735*/n2 = 0;
        }
        char[] array = /*EL:1739*/this._textBuffer.emptyAndGetCurrentSegment();
        int n3 = /*EL:1740*/0;
        int i = /*EL:1742*/0;
        while (i < n) {
            int a4 = /*EL:1743*/v-9[i >> 2];
            int v0 = /*EL:1744*/i & 0x3;
            /*SL:1745*/a4 = (a4 >> (3 - v0 << 3) & 0xFF);
            /*SL:1746*/++i;
            /*SL:1748*/if (a4 > 127) {
                int v = 0;
                /*SL:1750*/if ((a4 & 0xE0) == 0xC0) {
                    /*SL:1751*/a4 &= 0x1F;
                    final int a2 = /*EL:1752*/1;
                }
                else/*SL:1753*/ if ((a4 & 0xF0) == 0xE0) {
                    /*SL:1754*/a4 &= 0xF;
                    final int a3 = /*EL:1755*/2;
                }
                else/*SL:1756*/ if ((a4 & 0xF8) == 0xF0) {
                    /*SL:1757*/a4 &= 0x7;
                    /*SL:1758*/v = 3;
                }
                else {
                    /*SL:1760*/this._reportInvalidInitial(a4);
                    /*SL:1761*/a4 = (v = 1);
                }
                /*SL:1763*/if (i + v > n) {
                    /*SL:1764*/this._reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
                }
                int v2 = /*EL:1768*/v-9[i >> 2];
                /*SL:1769*/v0 = (i & 0x3);
                /*SL:1770*/v2 >>= 3 - v0 << 3;
                /*SL:1771*/++i;
                /*SL:1773*/if ((v2 & 0xC0) != 0x80) {
                    /*SL:1774*/this._reportInvalidOther(v2);
                }
                /*SL:1776*/a4 = (a4 << 6 | (v2 & 0x3F));
                /*SL:1777*/if (v > 1) {
                    /*SL:1778*/v2 = v-9[i >> 2];
                    /*SL:1779*/v0 = (i & 0x3);
                    /*SL:1780*/v2 >>= 3 - v0 << 3;
                    /*SL:1781*/++i;
                    /*SL:1783*/if ((v2 & 0xC0) != 0x80) {
                        /*SL:1784*/this._reportInvalidOther(v2);
                    }
                    /*SL:1786*/a4 = (a4 << 6 | (v2 & 0x3F));
                    /*SL:1787*/if (v > 2) {
                        /*SL:1788*/v2 = v-9[i >> 2];
                        /*SL:1789*/v0 = (i & 0x3);
                        /*SL:1790*/v2 >>= 3 - v0 << 3;
                        /*SL:1791*/++i;
                        /*SL:1792*/if ((v2 & 0xC0) != 0x80) {
                            /*SL:1793*/this._reportInvalidOther(v2 & 0xFF);
                        }
                        /*SL:1795*/a4 = (a4 << 6 | (v2 & 0x3F));
                    }
                }
                /*SL:1798*/if (v > 2) {
                    /*SL:1799*/a4 -= 65536;
                    /*SL:1800*/if (n3 >= array.length) {
                        /*SL:1801*/array = this._textBuffer.expandCurrentSegment();
                    }
                    /*SL:1803*/array[n3++] = (char)(55296 + (a4 >> 10));
                    /*SL:1804*/a4 = (0xDC00 | (a4 & 0x3FF));
                }
            }
            /*SL:1807*/if (n3 >= array.length) {
                /*SL:1808*/array = this._textBuffer.expandCurrentSegment();
            }
            /*SL:1810*/array[n3++] = (char)a4;
        }
        final String v3 = /*EL:1814*/new String(array, 0, n3);
        /*SL:1816*/if (v-7 < 4) {
            /*SL:1817*/v-9[v-8 - 1] = n2;
        }
        /*SL:1819*/return this._symbols.addName(v3, v-9, v-8);
    }
    
    @Override
    protected void _finishString() throws IOException {
        int i = /*EL:1831*/0;
        final char[] emptyAndGetCurrentSegment = /*EL:1832*/this._textBuffer.emptyAndGetCurrentSegment();
        final int[] icUTF8 = UTF8DataInputJsonParser._icUTF8;
        final int v0 = /*EL:1834*/emptyAndGetCurrentSegment.length;
        /*SL:1847*/do {
            final int v = this._inputData.readUnsignedByte();
            if (icUTF8[v] != 0) {
                if (v == 34) {
                    this._textBuffer.setCurrentLength(i);
                    return;
                }
                this._finishString2(emptyAndGetCurrentSegment, i, v);
                return;
            }
            else {
                emptyAndGetCurrentSegment[i++] = (char)v;
            }
        } while (i < v0);
        /*SL:1848*/this._finishString2(emptyAndGetCurrentSegment, i, this._inputData.readUnsignedByte());
    }
    
    private String _finishAndReturnString() throws IOException {
        int i = /*EL:1853*/0;
        final char[] emptyAndGetCurrentSegment = /*EL:1854*/this._textBuffer.emptyAndGetCurrentSegment();
        final int[] icUTF8 = UTF8DataInputJsonParser._icUTF8;
        final int v0 = /*EL:1856*/emptyAndGetCurrentSegment.length;
        /*SL:1868*/do {
            final int v = this._inputData.readUnsignedByte();
            if (icUTF8[v] != 0) {
                if (v == 34) {
                    return this._textBuffer.setCurrentAndReturn(i);
                }
                this._finishString2(emptyAndGetCurrentSegment, i, v);
                return this._textBuffer.contentsAsString();
            }
            else {
                emptyAndGetCurrentSegment[i++] = (char)v;
            }
        } while (i < v0);
        /*SL:1869*/this._finishString2(emptyAndGetCurrentSegment, i, this._inputData.readUnsignedByte());
        /*SL:1870*/return this._textBuffer.contentsAsString();
    }
    
    private final void _finishString2(char[] a1, int a2, int a3) throws IOException {
        final int[] v1 = UTF8DataInputJsonParser._icUTF8;
        int v2 = /*EL:1878*/a1.length;
        while (true) {
            /*SL:1883*/if (v1[a3] == 0) {
                /*SL:1884*/if (a2 >= v2) {
                    /*SL:1885*/a1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1886*/a2 = 0;
                    /*SL:1887*/v2 = a1.length;
                }
                /*SL:1889*/a1[a2++] = (char)a3;
                /*SL:1890*/a3 = this._inputData.readUnsignedByte();
            }
            else {
                /*SL:1893*/if (a3 == 34) {
                    break;
                }
                /*SL:1896*/switch (v1[a3]) {
                    case 1: {
                        /*SL:1898*/a3 = this._decodeEscaped();
                        /*SL:1899*/break;
                    }
                    case 2: {
                        /*SL:1901*/a3 = this._decodeUtf8_2(a3);
                        /*SL:1902*/break;
                    }
                    case 3: {
                        /*SL:1904*/a3 = this._decodeUtf8_3(a3);
                        /*SL:1905*/break;
                    }
                    case 4: {
                        /*SL:1907*/a3 = this._decodeUtf8_4(a3);
                        /*SL:1909*/a1[a2++] = (char)(0xD800 | a3 >> 10);
                        /*SL:1910*/if (a2 >= a1.length) {
                            /*SL:1911*/a1 = this._textBuffer.finishCurrentSegment();
                            /*SL:1912*/a2 = 0;
                            /*SL:1913*/v2 = a1.length;
                        }
                        /*SL:1915*/a3 = (0xDC00 | (a3 & 0x3FF));
                        /*SL:1917*/break;
                    }
                    default: {
                        /*SL:1919*/if (a3 < 32) {
                            /*SL:1920*/this._throwUnquotedSpace(a3, "string value");
                            break;
                        }
                        /*SL:1923*/this._reportInvalidChar(a3);
                        break;
                    }
                }
                /*SL:1927*/if (a2 >= a1.length) {
                    /*SL:1928*/a1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1929*/a2 = 0;
                    /*SL:1930*/v2 = a1.length;
                }
                /*SL:1933*/a1[a2++] = (char)a3;
                a3 = this._inputData.readUnsignedByte();
            }
        }
        /*SL:1935*/this._textBuffer.setCurrentLength(a2);
    }
    
    protected void _skipString() throws IOException {
        /*SL:1945*/this._tokenIncomplete = false;
        final int[] v0 = UTF8DataInputJsonParser._icUTF8;
        while (true) {
            final int v = /*EL:1956*/this._inputData.readUnsignedByte();
            /*SL:1957*/if (v0[v] != 0) {
                /*SL:1962*/if (v == 34) {
                    break;
                }
                /*SL:1966*/switch (v0[v]) {
                    case 1: {
                        /*SL:1968*/this._decodeEscaped();
                        /*SL:1969*/continue;
                    }
                    case 2: {
                        /*SL:1971*/this._skipUtf8_2();
                        /*SL:1972*/continue;
                    }
                    case 3: {
                        /*SL:1974*/this._skipUtf8_3();
                        /*SL:1975*/continue;
                    }
                    case 4: {
                        /*SL:1977*/this._skipUtf8_4();
                        /*SL:1978*/continue;
                    }
                    default: {
                        /*SL:1980*/if (v < 32) {
                            /*SL:1981*/this._throwUnquotedSpace(v, "string value");
                            continue;
                        }
                        /*SL:1984*/this._reportInvalidChar(v);
                        continue;
                    }
                }
            }
        }
    }
    
    protected JsonToken _handleUnexpectedValue(final int a1) throws IOException {
        /*SL:1998*/switch (a1) {
            case 93: {
                /*SL:2000*/if (!this._parsingContext.inArray()) {
                    /*SL:2001*/break;
                }
            }
            case 44: {
                /*SL:2008*/if (this.isEnabled(Feature.ALLOW_MISSING_VALUES)) {
                    /*SL:2010*/this._nextByte = a1;
                    /*SL:2011*/return JsonToken.VALUE_NULL;
                }
            }
            case 125: {
                /*SL:2017*/this._reportUnexpectedChar(a1, "expected a value");
            }
            case 39: {
                /*SL:2019*/if (this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    /*SL:2020*/return this._handleApos();
                }
                break;
            }
            case 78: {
                /*SL:2024*/this._matchToken("NaN", 1);
                /*SL:2025*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    /*SL:2026*/return this.resetAsNaN("NaN", Double.NaN);
                }
                /*SL:2028*/this._reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                /*SL:2029*/break;
            }
            case 73: {
                /*SL:2031*/this._matchToken("Infinity", 1);
                /*SL:2032*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    /*SL:2033*/return this.resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
                }
                /*SL:2035*/this._reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                /*SL:2036*/break;
            }
            case 43: {
                /*SL:2038*/return this._handleInvalidNumberStart(this._inputData.readUnsignedByte(), false);
            }
        }
        /*SL:2041*/if (Character.isJavaIdentifierStart(a1)) {
            /*SL:2042*/this._reportInvalidToken(a1, "" + (char)a1, "('true', 'false' or 'null')");
        }
        /*SL:2045*/this._reportUnexpectedChar(a1, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        /*SL:2046*/return null;
    }
    
    protected JsonToken _handleApos() throws IOException {
        int a1 = /*EL:2051*/0;
        int i = /*EL:2053*/0;
        char[] array = /*EL:2054*/this._textBuffer.emptyAndGetCurrentSegment();
        final int[] v0 = UTF8DataInputJsonParser._icUTF8;
    Block_2:
        while (true) {
            int v = /*EL:2064*/array.length;
            /*SL:2065*/if (i >= array.length) {
                /*SL:2066*/array = this._textBuffer.finishCurrentSegment();
                /*SL:2067*/i = 0;
                /*SL:2068*/v = array.length;
            }
            /*SL:2079*/do {
                a1 = this._inputData.readUnsignedByte();
                if (a1 == 39) {
                    break Block_2;
                }
                if (v0[a1] != 0) {
                    /*SL:2081*/switch (v0[a1]) {
                        case 1: {
                            /*SL:2083*/a1 = this._decodeEscaped();
                            /*SL:2084*/break;
                        }
                        case 2: {
                            /*SL:2086*/a1 = this._decodeUtf8_2(a1);
                            /*SL:2087*/break;
                        }
                        case 3: {
                            /*SL:2089*/a1 = this._decodeUtf8_3(a1);
                            /*SL:2090*/break;
                        }
                        case 4: {
                            /*SL:2092*/a1 = this._decodeUtf8_4(a1);
                            /*SL:2094*/array[i++] = (char)(0xD800 | a1 >> 10);
                            /*SL:2095*/if (i >= array.length) {
                                /*SL:2096*/array = this._textBuffer.finishCurrentSegment();
                                /*SL:2097*/i = 0;
                            }
                            /*SL:2099*/a1 = (0xDC00 | (a1 & 0x3FF));
                            /*SL:2101*/break;
                        }
                        default: {
                            /*SL:2103*/if (a1 < 32) {
                                /*SL:2104*/this._throwUnquotedSpace(a1, "string value");
                            }
                            /*SL:2107*/this._reportInvalidChar(a1);
                            break;
                        }
                    }
                    /*SL:2110*/if (i >= array.length) {
                        /*SL:2111*/array = this._textBuffer.finishCurrentSegment();
                        /*SL:2112*/i = 0;
                    }
                    /*SL:2115*/array[i++] = (char)a1;
                    break;
                }
                array[i++] = (char)a1;
            } while (i < v);
        }
        /*SL:2117*/this._textBuffer.setCurrentLength(i);
        /*SL:2119*/return JsonToken.VALUE_STRING;
    }
    
    protected JsonToken _handleInvalidNumberStart(int v2, final boolean v3) throws IOException {
        /*SL:2129*/while (v2 == 73) {
            /*SL:2130*/v2 = this._inputData.readUnsignedByte();
            final String a2;
            /*SL:2132*/if (v2 == 78) {
                final String a1 = /*EL:2133*/v3 ? "-INF" : "+INF";
            }
            else {
                /*SL:2134*/if (v2 != 110) {
                    break;
                }
                /*SL:2135*/a2 = (v3 ? "-Infinity" : "+Infinity");
            }
            /*SL:2139*/this._matchToken(a2, 3);
            /*SL:2140*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                /*SL:2141*/return this.resetAsNaN(a2, v3 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
            }
            /*SL:2143*/this._reportError("Non-standard token '" + a2 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
        }
        /*SL:2145*/this.reportUnexpectedNumberChar(v2, "expected digit (0-9) to follow minus sign, for valid numeric value");
        /*SL:2146*/return null;
    }
    
    protected final void _matchToken(final String v1, int v2) throws IOException {
        final int v3 = /*EL:2151*/v1.length();
        /*SL:2157*/do {
            final int a1 = this._inputData.readUnsignedByte();
            if (a1 != v1.charAt(v2)) {
                this._reportInvalidToken(a1, v1.substring(0, v2));
            }
        } while (++v2 < v3);
        final int v4 = /*EL:2159*/this._inputData.readUnsignedByte();
        /*SL:2160*/if (v4 >= 48 && v4 != 93 && v4 != 125) {
            /*SL:2161*/this._checkMatchEnd(v1, v2, v4);
        }
        /*SL:2163*/this._nextByte = v4;
    }
    
    private final void _checkMatchEnd(final String a1, final int a2, final int a3) throws IOException {
        final char v1 = /*EL:2168*/(char)this._decodeCharForError(a3);
        /*SL:2169*/if (Character.isJavaIdentifierPart(v1)) {
            /*SL:2170*/this._reportInvalidToken(v1, a1.substring(0, a2));
        }
    }
    
    private final int _skipWS() throws IOException {
        int v1 = /*EL:2182*/this._nextByte;
        /*SL:2183*/if (v1 < 0) {
            /*SL:2184*/v1 = this._inputData.readUnsignedByte();
        }
        else {
            /*SL:2186*/this._nextByte = -1;
        }
        /*SL:2189*/while (v1 <= 32) {
            /*SL:2197*/if (v1 == 13 || v1 == 10) {
                /*SL:2198*/++this._currInputRow;
            }
            /*SL:2201*/v1 = this._inputData.readUnsignedByte();
        }
        if (v1 == 47 || v1 == 35) {
            return this._skipWSComment(v1);
        }
        return v1;
    }
    
    private final int _skipWSOrEnd() throws IOException {
        int v0 = /*EL:2213*/this._nextByte;
        while (true) {
            /*SL:2214*/if (v0 < 0) {
                try {
                    /*SL:2216*/v0 = this._inputData.readUnsignedByte();
                    break Label_0033;
                }
                catch (EOFException v) {
                    /*SL:2218*/return this._eofAsNextChar();
                }
            }
            Label_0028: {
                break Label_0028;
                /*SL:2224*/while (v0 <= 32) {
                    /*SL:2232*/if (v0 == 13 || v0 == 10) {
                        /*SL:2233*/++this._currInputRow;
                    }
                    try {
                        /*SL:2237*/v0 = this._inputData.readUnsignedByte();
                    }
                    catch (EOFException v) {
                        /*SL:2239*/return this._eofAsNextChar();
                    }
                }
                if (v0 == 47 || v0 == 35) {
                    return this._skipWSComment(v0);
                }
                return v0;
            }
            this._nextByte = -1;
            continue;
        }
    }
    
    private final int _skipWSComment(int a1) throws IOException {
        while (true) {
            /*SL:2247*/if (a1 > 32) {
                /*SL:2248*/if (a1 == 47) {
                    /*SL:2249*/this._skipComment();
                }
                else {
                    /*SL:2250*/if (a1 != 35) {
                        /*SL:2255*/return a1;
                    }
                    if (!this._skipYAMLComment()) {
                        return a1;
                    }
                }
            }
            else/*SL:2260*/ if (a1 == 13 || a1 == 10) {
                /*SL:2261*/++this._currInputRow;
            }
            /*SL:2269*/a1 = this._inputData.readUnsignedByte();
        }
    }
    
    private final int _skipColon() throws IOException {
        int v1 = /*EL:2275*/this._nextByte;
        /*SL:2276*/if (v1 < 0) {
            /*SL:2277*/v1 = this._inputData.readUnsignedByte();
        }
        else {
            /*SL:2279*/this._nextByte = -1;
        }
        /*SL:2282*/if (v1 == 58) {
            /*SL:2283*/v1 = this._inputData.readUnsignedByte();
            /*SL:2284*/if (v1 <= 32) {
                /*SL:2290*/if (v1 == 32 || v1 == 9) {
                    /*SL:2291*/v1 = this._inputData.readUnsignedByte();
                    /*SL:2292*/if (v1 > 32) {
                        /*SL:2293*/if (v1 == 47 || v1 == 35) {
                            /*SL:2294*/return this._skipColon2(v1, true);
                        }
                        /*SL:2296*/return v1;
                    }
                }
                /*SL:2299*/return this._skipColon2(v1, true);
            }
            if (v1 == 47 || v1 == 35) {
                return this._skipColon2(v1, true);
            }
            return v1;
        }
        else {
            /*SL:2301*/if (v1 == 32 || v1 == 9) {
                /*SL:2302*/v1 = this._inputData.readUnsignedByte();
            }
            /*SL:2304*/if (v1 != 58) {
                /*SL:2323*/return this._skipColon2(v1, false);
            }
            v1 = this._inputData.readUnsignedByte();
            if (v1 <= 32) {
                if (v1 == 32 || v1 == 9) {
                    v1 = this._inputData.readUnsignedByte();
                    if (v1 > 32) {
                        if (v1 == 47 || v1 == 35) {
                            return this._skipColon2(v1, true);
                        }
                        return v1;
                    }
                }
                return this._skipColon2(v1, true);
            }
            if (v1 == 47 || v1 == 35) {
                return this._skipColon2(v1, true);
            }
            return v1;
        }
    }
    
    private final int _skipColon2(int a1, boolean a2) throws IOException {
        while (true) {
            /*SL:2329*/if (a1 > 32) {
                /*SL:2330*/if (a1 == 47) {
                    /*SL:2331*/this._skipComment();
                }
                else/*SL:2334*/ if (a1 != 35 || /*EL:2335*/!this._skipYAMLComment()) {
                    /*SL:2339*/if (a2) {
                        break;
                    }
                    /*SL:2342*/if (a1 != 58) {
                        /*SL:2343*/this._reportUnexpectedChar(a1, "was expecting a colon to separate field name and value");
                    }
                    /*SL:2345*/a2 = true;
                }
            }
            else/*SL:2349*/ if (a1 == 13 || a1 == 10) {
                /*SL:2350*/++this._currInputRow;
            }
            a1 = this._inputData.readUnsignedByte();
        }
        return a1;
    }
    
    private final void _skipComment() throws IOException {
        /*SL:2358*/if (!this.isEnabled(Feature.ALLOW_COMMENTS)) {
            /*SL:2359*/this._reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        final int v1 = /*EL:2361*/this._inputData.readUnsignedByte();
        /*SL:2362*/if (v1 == 47) {
            /*SL:2363*/this._skipLine();
        }
        else/*SL:2364*/ if (v1 == 42) {
            /*SL:2365*/this._skipCComment();
        }
        else {
            /*SL:2367*/this._reportUnexpectedChar(v1, "was expecting either '*' or '/' for a comment");
        }
    }
    
    private final void _skipCComment() throws IOException {
        final int[] inputCodeComment = /*EL:2374*/CharTypes.getInputCodeComment();
        int v0 = /*EL:2375*/this._inputData.readUnsignedByte();
    Block_2:
        while (true) {
            final int v = /*EL:2380*/inputCodeComment[v0];
            /*SL:2381*/if (v != 0) {
                /*SL:2382*/switch (v) {
                    case 42: {
                        /*SL:2384*/v0 = this._inputData.readUnsignedByte();
                        /*SL:2385*/if (v0 == 47) {
                            break Block_2;
                        }
                        continue;
                    }
                    case 10:
                    case 13: {
                        /*SL:2391*/++this._currInputRow;
                        /*SL:2392*/break;
                    }
                    case 2: {
                        /*SL:2394*/this._skipUtf8_2();
                        /*SL:2395*/break;
                    }
                    case 3: {
                        /*SL:2397*/this._skipUtf8_3();
                        /*SL:2398*/break;
                    }
                    case 4: {
                        /*SL:2400*/this._skipUtf8_4();
                        /*SL:2401*/break;
                    }
                    default: {
                        /*SL:2404*/this._reportInvalidChar(v0);
                        break;
                    }
                }
            }
            /*SL:2407*/v0 = this._inputData.readUnsignedByte();
        }
    }
    
    private final boolean _skipYAMLComment() throws IOException {
        /*SL:2413*/if (!this.isEnabled(Feature.ALLOW_YAML_COMMENTS)) {
            /*SL:2414*/return false;
        }
        /*SL:2416*/this._skipLine();
        /*SL:2417*/return true;
    }
    
    private final void _skipLine() throws IOException {
        final int[] v0 = /*EL:2427*/CharTypes.getInputCodeComment();
    Label_0080:
        while (true) {
            final int v = /*EL:2429*/this._inputData.readUnsignedByte();
            final int v2 = /*EL:2430*/v0[v];
            /*SL:2431*/if (v2 != 0) {
                /*SL:2432*/switch (v2) {
                    case 10:
                    case 13: {
                        break Label_0080;
                    }
                    case 42: {
                        /*SL:2438*/continue;
                    }
                    case 2: {
                        /*SL:2440*/this._skipUtf8_2();
                        /*SL:2441*/continue;
                    }
                    case 3: {
                        /*SL:2443*/this._skipUtf8_3();
                        /*SL:2444*/continue;
                    }
                    case 4: {
                        /*SL:2446*/this._skipUtf8_4();
                        /*SL:2447*/continue;
                    }
                    default: {
                        /*SL:2449*/if (v2 < 0) {
                            /*SL:2451*/this._reportInvalidChar(v);
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
        ++this._currInputRow;
    }
    
    @Override
    protected char _decodeEscaped() throws IOException {
        final int unsignedByte = /*EL:2461*/this._inputData.readUnsignedByte();
        /*SL:2463*/switch (unsignedByte) {
            case 98: {
                /*SL:2466*/return '\b';
            }
            case 116: {
                /*SL:2468*/return '\t';
            }
            case 110: {
                /*SL:2470*/return '\n';
            }
            case 102: {
                /*SL:2472*/return '\f';
            }
            case 114: {
                /*SL:2474*/return '\r';
            }
            case 34:
            case 47:
            case 92: {
                /*SL:2480*/return (char)unsignedByte;
            }
            case 117: {
                int n = /*EL:2490*/0;
                /*SL:2491*/for (int v0 = 0; v0 < 4; ++v0) {
                    final int v = /*EL:2492*/this._inputData.readUnsignedByte();
                    final int v2 = /*EL:2493*/CharTypes.charToHex(v);
                    /*SL:2494*/if (v2 < 0) {
                        /*SL:2495*/this._reportUnexpectedChar(v, "expected a hex-digit for character escape sequence");
                    }
                    /*SL:2497*/n = (n << 4 | v2);
                }
                /*SL:2499*/return (char)n;
            }
            default: {
                return this._handleUnrecognizedCharacterEscape((char)this._decodeCharForError(unsignedByte));
            }
        }
    }
    
    protected int _decodeCharForError(final int v-1) throws IOException {
        int v0 = /*EL:2504*/v-1 & 0xFF;
        /*SL:2505*/if (v0 > 127) {
            int v = 0;
            /*SL:2509*/if ((v0 & 0xE0) == 0xC0) {
                /*SL:2510*/v0 &= 0x1F;
                final int a1 = /*EL:2511*/1;
            }
            else/*SL:2512*/ if ((v0 & 0xF0) == 0xE0) {
                /*SL:2513*/v0 &= 0xF;
                /*SL:2514*/v = 2;
            }
            else/*SL:2515*/ if ((v0 & 0xF8) == 0xF0) {
                /*SL:2517*/v0 &= 0x7;
                /*SL:2518*/v = 3;
            }
            else {
                /*SL:2520*/this._reportInvalidInitial(v0 & 0xFF);
                /*SL:2521*/v = 1;
            }
            int v2 = /*EL:2524*/this._inputData.readUnsignedByte();
            /*SL:2525*/if ((v2 & 0xC0) != 0x80) {
                /*SL:2526*/this._reportInvalidOther(v2 & 0xFF);
            }
            /*SL:2528*/v0 = (v0 << 6 | (v2 & 0x3F));
            /*SL:2530*/if (v > 1) {
                /*SL:2531*/v2 = this._inputData.readUnsignedByte();
                /*SL:2532*/if ((v2 & 0xC0) != 0x80) {
                    /*SL:2533*/this._reportInvalidOther(v2 & 0xFF);
                }
                /*SL:2535*/v0 = (v0 << 6 | (v2 & 0x3F));
                /*SL:2536*/if (v > 2) {
                    /*SL:2537*/v2 = this._inputData.readUnsignedByte();
                    /*SL:2538*/if ((v2 & 0xC0) != 0x80) {
                        /*SL:2539*/this._reportInvalidOther(v2 & 0xFF);
                    }
                    /*SL:2541*/v0 = (v0 << 6 | (v2 & 0x3F));
                }
            }
        }
        /*SL:2545*/return v0;
    }
    
    private final int _decodeUtf8_2(final int a1) throws IOException {
        final int v1 = /*EL:2556*/this._inputData.readUnsignedByte();
        /*SL:2557*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2558*/this._reportInvalidOther(v1 & 0xFF);
        }
        /*SL:2560*/return (a1 & 0x1F) << 6 | (v1 & 0x3F);
    }
    
    private final int _decodeUtf8_3(int a1) throws IOException {
        /*SL:2565*/a1 &= 0xF;
        int v1 = /*EL:2566*/this._inputData.readUnsignedByte();
        /*SL:2567*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2568*/this._reportInvalidOther(v1 & 0xFF);
        }
        int v2 = /*EL:2570*/a1 << 6 | (v1 & 0x3F);
        /*SL:2571*/v1 = this._inputData.readUnsignedByte();
        /*SL:2572*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2573*/this._reportInvalidOther(v1 & 0xFF);
        }
        /*SL:2575*/v2 = (v2 << 6 | (v1 & 0x3F));
        /*SL:2576*/return v2;
    }
    
    private final int _decodeUtf8_4(int a1) throws IOException {
        int v1 = /*EL:2585*/this._inputData.readUnsignedByte();
        /*SL:2586*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2587*/this._reportInvalidOther(v1 & 0xFF);
        }
        /*SL:2589*/a1 = ((a1 & 0x7) << 6 | (v1 & 0x3F));
        /*SL:2590*/v1 = this._inputData.readUnsignedByte();
        /*SL:2591*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2592*/this._reportInvalidOther(v1 & 0xFF);
        }
        /*SL:2594*/a1 = (a1 << 6 | (v1 & 0x3F));
        /*SL:2595*/v1 = this._inputData.readUnsignedByte();
        /*SL:2596*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2597*/this._reportInvalidOther(v1 & 0xFF);
        }
        /*SL:2603*/return (a1 << 6 | (v1 & 0x3F)) - 65536;
    }
    
    private final void _skipUtf8_2() throws IOException {
        final int v1 = /*EL:2608*/this._inputData.readUnsignedByte();
        /*SL:2609*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2610*/this._reportInvalidOther(v1 & 0xFF);
        }
    }
    
    private final void _skipUtf8_3() throws IOException {
        int v1 = /*EL:2620*/this._inputData.readUnsignedByte();
        /*SL:2621*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2622*/this._reportInvalidOther(v1 & 0xFF);
        }
        /*SL:2624*/v1 = this._inputData.readUnsignedByte();
        /*SL:2625*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2626*/this._reportInvalidOther(v1 & 0xFF);
        }
    }
    
    private final void _skipUtf8_4() throws IOException {
        int v1 = /*EL:2632*/this._inputData.readUnsignedByte();
        /*SL:2633*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2634*/this._reportInvalidOther(v1 & 0xFF);
        }
        /*SL:2636*/v1 = this._inputData.readUnsignedByte();
        /*SL:2637*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2638*/this._reportInvalidOther(v1 & 0xFF);
        }
        /*SL:2640*/v1 = this._inputData.readUnsignedByte();
        /*SL:2641*/if ((v1 & 0xC0) != 0x80) {
            /*SL:2642*/this._reportInvalidOther(v1 & 0xFF);
        }
    }
    
    protected void _reportInvalidToken(final int a1, final String a2) throws IOException {
        /*SL:2654*/this._reportInvalidToken(a1, a2, "'null', 'true', 'false' or NaN");
    }
    
    protected void _reportInvalidToken(int a3, final String v1, final String v2) throws IOException {
        final StringBuilder v3 = /*EL:2660*/new StringBuilder(v1);
        while (true) {
            final char a4 = /*EL:2667*/(char)this._decodeCharForError(a3);
            /*SL:2668*/if (!Character.isJavaIdentifierPart(a4)) {
                break;
            }
            /*SL:2671*/v3.append(a4);
            /*SL:2672*/a3 = this._inputData.readUnsignedByte();
        }
        /*SL:2674*/this._reportError("Unrecognized token '" + v3.toString() + "': was expecting " + v2);
    }
    
    protected void _reportInvalidChar(final int a1) throws JsonParseException {
        /*SL:2681*/if (a1 < 32) {
            /*SL:2682*/this._throwInvalidSpace(a1);
        }
        /*SL:2684*/this._reportInvalidInitial(a1);
    }
    
    protected void _reportInvalidInitial(final int a1) throws JsonParseException {
        /*SL:2690*/this._reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(a1));
    }
    
    private void _reportInvalidOther(final int a1) throws JsonParseException {
        /*SL:2696*/this._reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(a1));
    }
    
    private static int[] _growArrayBy(final int[] a1, final int a2) {
        /*SL:2701*/if (a1 == null) {
            /*SL:2702*/return new int[a2];
        }
        /*SL:2704*/return Arrays.copyOf(a1, a1.length + a2);
    }
    
    protected final byte[] _decodeBase64(final Base64Variant v-2) throws IOException {
        final ByteArrayBuilder getByteArrayBuilder = /*EL:2720*/this._getByteArrayBuilder();
        while (true) {
            int a1 = /*EL:2727*/this._inputData.readUnsignedByte();
            /*SL:2728*/if (a1 > 32) {
                int v1 = /*EL:2729*/v-2.decodeBase64Char(a1);
                /*SL:2730*/if (v1 < 0) {
                    /*SL:2731*/if (a1 == 34) {
                        /*SL:2732*/return getByteArrayBuilder.toByteArray();
                    }
                    /*SL:2734*/v1 = this._decodeBase64Escape(v-2, a1, 0);
                    /*SL:2735*/if (v1 < 0) {
                        /*SL:2736*/continue;
                    }
                }
                int v2 = /*EL:2739*/v1;
                /*SL:2742*/a1 = this._inputData.readUnsignedByte();
                /*SL:2743*/v1 = v-2.decodeBase64Char(a1);
                /*SL:2744*/if (v1 < 0) {
                    /*SL:2745*/v1 = this._decodeBase64Escape(v-2, a1, 1);
                }
                /*SL:2747*/v2 = (v2 << 6 | v1);
                /*SL:2749*/a1 = this._inputData.readUnsignedByte();
                /*SL:2750*/v1 = v-2.decodeBase64Char(a1);
                /*SL:2753*/if (v1 < 0) {
                    /*SL:2754*/if (v1 != -2) {
                        /*SL:2756*/if (a1 == 34 && !v-2.usesPadding()) {
                            /*SL:2757*/v2 >>= 4;
                            /*SL:2758*/getByteArrayBuilder.append(v2);
                            /*SL:2759*/return getByteArrayBuilder.toByteArray();
                        }
                        /*SL:2761*/v1 = this._decodeBase64Escape(v-2, a1, 2);
                    }
                    /*SL:2763*/if (v1 == -2) {
                        /*SL:2764*/a1 = this._inputData.readUnsignedByte();
                        /*SL:2765*/if (!v-2.usesPaddingChar(a1)) {
                            /*SL:2766*/throw this.reportInvalidBase64Char(v-2, a1, 3, "expected padding character '" + v-2.getPaddingChar() + "'");
                        }
                        /*SL:2769*/v2 >>= 4;
                        /*SL:2770*/getByteArrayBuilder.append(v2);
                        /*SL:2771*/continue;
                    }
                }
                /*SL:2775*/v2 = (v2 << 6 | v1);
                /*SL:2777*/a1 = this._inputData.readUnsignedByte();
                /*SL:2778*/v1 = v-2.decodeBase64Char(a1);
                /*SL:2779*/if (v1 < 0) {
                    /*SL:2780*/if (v1 != -2) {
                        /*SL:2782*/if (a1 == 34 && !v-2.usesPadding()) {
                            /*SL:2783*/v2 >>= 2;
                            /*SL:2784*/getByteArrayBuilder.appendTwoBytes(v2);
                            /*SL:2785*/return getByteArrayBuilder.toByteArray();
                        }
                        /*SL:2787*/v1 = this._decodeBase64Escape(v-2, a1, 3);
                    }
                    /*SL:2789*/if (v1 == -2) {
                        /*SL:2796*/v2 >>= 2;
                        /*SL:2797*/getByteArrayBuilder.appendTwoBytes(v2);
                        /*SL:2798*/continue;
                    }
                }
                /*SL:2802*/v2 = (v2 << 6 | v1);
                /*SL:2803*/getByteArrayBuilder.appendThreeBytes(v2);
            }
        }
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        /*SL:2815*/return new JsonLocation(this._getSourceReference(), -1L, -1L, this._tokenInputRow, -1);
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        /*SL:2820*/return new JsonLocation(this._getSourceReference(), -1L, -1L, this._currInputRow, -1);
    }
    
    private void _closeScope(final int a1) throws JsonParseException {
        /*SL:2830*/if (a1 == 93) {
            /*SL:2831*/if (!this._parsingContext.inArray()) {
                /*SL:2832*/this._reportMismatchedEndMarker(a1, '}');
            }
            /*SL:2834*/this._parsingContext = this._parsingContext.clearAndGetParent();
            /*SL:2835*/this._currToken = JsonToken.END_ARRAY;
        }
        /*SL:2837*/if (a1 == 125) {
            /*SL:2838*/if (!this._parsingContext.inObject()) {
                /*SL:2839*/this._reportMismatchedEndMarker(a1, ']');
            }
            /*SL:2841*/this._parsingContext = this._parsingContext.clearAndGetParent();
            /*SL:2842*/this._currToken = JsonToken.END_OBJECT;
        }
    }
    
    private static final int pad(final int a1, final int a2) {
        /*SL:2850*/return (a2 == 4) ? a1 : (a1 | -1 << (a2 << 3));
    }
    
    static {
        _icUTF8 = CharTypes.getInputCodeUtf8();
        _icLatin1 = CharTypes.getInputCodeLatin1();
    }
}
