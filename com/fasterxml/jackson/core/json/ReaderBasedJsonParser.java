package com.fasterxml.jackson.core.json;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.SerializableString;
import java.io.OutputStream;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.io.Writer;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import com.fasterxml.jackson.core.ObjectCodec;
import java.io.Reader;
import com.fasterxml.jackson.core.base.ParserBase;

public class ReaderBasedJsonParser extends ParserBase
{
    protected static final int FEAT_MASK_TRAILING_COMMA;
    protected static final int[] _icLatin1;
    protected Reader _reader;
    protected char[] _inputBuffer;
    protected boolean _bufferRecyclable;
    protected ObjectCodec _objectCodec;
    protected final CharsToNameCanonicalizer _symbols;
    protected final int _hashSeed;
    protected boolean _tokenIncomplete;
    protected long _nameStartOffset;
    protected int _nameStartRow;
    protected int _nameStartCol;
    
    public ReaderBasedJsonParser(final IOContext a1, final int a2, final Reader a3, final ObjectCodec a4, final CharsToNameCanonicalizer a5, final char[] a6, final int a7, final int a8, final boolean a9) {
        super(a1, a2);
        this._reader = a3;
        this._inputBuffer = a6;
        this._inputPtr = a7;
        this._inputEnd = a8;
        this._objectCodec = a4;
        this._symbols = a5;
        this._hashSeed = a5.hashSeed();
        this._bufferRecyclable = a9;
    }
    
    public ReaderBasedJsonParser(final IOContext a1, final int a2, final Reader a3, final ObjectCodec a4, final CharsToNameCanonicalizer a5) {
        super(a1, a2);
        this._reader = a3;
        this._inputBuffer = a1.allocTokenBuffer();
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._objectCodec = a4;
        this._symbols = a5;
        this._hashSeed = a5.hashSeed();
        this._bufferRecyclable = true;
    }
    
    @Override
    public ObjectCodec getCodec() {
        /*SL:153*/return this._objectCodec;
    }
    
    @Override
    public void setCodec(final ObjectCodec a1) {
        /*SL:154*/this._objectCodec = a1;
    }
    
    @Override
    public int releaseBuffered(final Writer a1) throws IOException {
        final int v1 = /*EL:158*/this._inputEnd - this._inputPtr;
        /*SL:159*/if (v1 < 1) {
            return 0;
        }
        final int v2 = /*EL:161*/this._inputPtr;
        /*SL:162*/a1.write(this._inputBuffer, v2, v1);
        /*SL:163*/return v1;
    }
    
    @Override
    public Object getInputSource() {
        /*SL:166*/return this._reader;
    }
    
    @Deprecated
    protected char getNextChar(final String a1) throws IOException {
        /*SL:170*/return this.getNextChar(a1, null);
    }
    
    protected char getNextChar(final String a1, final JsonToken a2) throws IOException {
        /*SL:174*/if (this._inputPtr >= this._inputEnd && /*EL:175*/!this._loadMore()) {
            /*SL:176*/this._reportInvalidEOF(a1, a2);
        }
        /*SL:179*/return this._inputBuffer[this._inputPtr++];
    }
    
    @Override
    protected void _closeInput() throws IOException {
        /*SL:191*/if (this._reader != null) {
            /*SL:192*/if (this._ioContext.isResourceManaged() || this.isEnabled(Feature.AUTO_CLOSE_SOURCE)) {
                /*SL:193*/this._reader.close();
            }
            /*SL:195*/this._reader = null;
        }
    }
    
    @Override
    protected void _releaseBuffers() throws IOException {
        /*SL:207*/super._releaseBuffers();
        /*SL:209*/this._symbols.release();
        /*SL:211*/if (this._bufferRecyclable) {
            final char[] v1 = /*EL:212*/this._inputBuffer;
            /*SL:213*/if (v1 != null) {
                /*SL:214*/this._inputBuffer = null;
                /*SL:215*/this._ioContext.releaseTokenBuffer(v1);
            }
        }
    }
    
    protected void _loadMoreGuaranteed() throws IOException {
        /*SL:227*/if (!this._loadMore()) {
            this._reportInvalidEOF();
        }
    }
    
    protected boolean _loadMore() throws IOException {
        final int v0 = /*EL:232*/this._inputEnd;
        /*SL:234*/this._currInputProcessed += v0;
        /*SL:235*/this._currInputRowStart -= v0;
        /*SL:240*/this._nameStartOffset -= v0;
        /*SL:242*/if (this._reader != null) {
            final int v = /*EL:243*/this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
            /*SL:244*/if (v > 0) {
                /*SL:245*/this._inputPtr = 0;
                /*SL:246*/this._inputEnd = v;
                /*SL:247*/return true;
            }
            /*SL:250*/this._closeInput();
            /*SL:252*/if (v == 0) {
                /*SL:253*/throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
            }
        }
        /*SL:256*/return false;
    }
    
    @Override
    public final String getText() throws IOException {
        final JsonToken v1 = /*EL:274*/this._currToken;
        /*SL:275*/if (v1 == JsonToken.VALUE_STRING) {
            /*SL:276*/if (this._tokenIncomplete) {
                /*SL:277*/this._tokenIncomplete = false;
                /*SL:278*/this._finishString();
            }
            /*SL:280*/return this._textBuffer.contentsAsString();
        }
        /*SL:282*/return this._getText2(v1);
    }
    
    @Override
    public int getText(final Writer v-1) throws IOException {
        final JsonToken v0 = /*EL:288*/this._currToken;
        /*SL:289*/if (v0 == JsonToken.VALUE_STRING) {
            /*SL:290*/if (this._tokenIncomplete) {
                /*SL:291*/this._tokenIncomplete = false;
                /*SL:292*/this._finishString();
            }
            /*SL:294*/return this._textBuffer.contentsToWriter(v-1);
        }
        /*SL:296*/if (v0 == JsonToken.FIELD_NAME) {
            final String a1 = /*EL:297*/this._parsingContext.getCurrentName();
            /*SL:298*/v-1.write(a1);
            /*SL:299*/return a1.length();
        }
        /*SL:301*/if (v0 == null) {
            /*SL:309*/return 0;
        }
        if (v0.isNumeric()) {
            return this._textBuffer.contentsToWriter(v-1);
        }
        final char[] v = v0.asCharArray();
        v-1.write(v);
        return v.length;
    }
    
    @Override
    public final String getValueAsString() throws IOException {
        /*SL:318*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:319*/if (this._tokenIncomplete) {
                /*SL:320*/this._tokenIncomplete = false;
                /*SL:321*/this._finishString();
            }
            /*SL:323*/return this._textBuffer.contentsAsString();
        }
        /*SL:325*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:326*/return this.getCurrentName();
        }
        /*SL:328*/return super.getValueAsString(null);
    }
    
    @Override
    public final String getValueAsString(final String a1) throws IOException {
        /*SL:334*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:335*/if (this._tokenIncomplete) {
                /*SL:336*/this._tokenIncomplete = false;
                /*SL:337*/this._finishString();
            }
            /*SL:339*/return this._textBuffer.contentsAsString();
        }
        /*SL:341*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:342*/return this.getCurrentName();
        }
        /*SL:344*/return super.getValueAsString(a1);
    }
    
    protected final String _getText2(final JsonToken a1) {
        /*SL:348*/if (a1 == null) {
            /*SL:349*/return null;
        }
        /*SL:351*/switch (a1.id()) {
            case 5: {
                /*SL:353*/return this._parsingContext.getCurrentName();
            }
            case 6:
            case 7:
            case 8: {
                /*SL:359*/return this._textBuffer.contentsAsString();
            }
            default: {
                /*SL:361*/return a1.asString();
            }
        }
    }
    
    @Override
    public final char[] getTextCharacters() throws IOException {
        /*SL:368*/if (this._currToken == null) {
            /*SL:396*/return null;
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
    public final int getTextLength() throws IOException {
        /*SL:402*/if (this._currToken == null) {
            /*SL:419*/return 0;
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
    public final int getTextOffset() throws IOException {
        /*SL:426*/if (this._currToken != null) {
            /*SL:427*/switch (this._currToken.id()) {
                case 5: {
                    /*SL:429*/return 0;
                }
                case 6: {
                    /*SL:431*/if (this._tokenIncomplete) {
                        /*SL:432*/this._tokenIncomplete = false;
                        /*SL:433*/this._finishString();
                        return /*EL:438*/this._textBuffer.getTextOffset();
                    }
                    return this._textBuffer.getTextOffset();
                }
                case 7:
                case 8: {
                    return this._textBuffer.getTextOffset();
                }
            }
        }
        /*SL:442*/return 0;
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant v0) throws IOException {
        /*SL:448*/if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT && this._binaryValue != null) {
            /*SL:449*/return this._binaryValue;
        }
        /*SL:451*/if (this._currToken != JsonToken.VALUE_STRING) {
            /*SL:452*/this._reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
        }
        /*SL:455*/if (this._tokenIncomplete) {
            try {
                /*SL:457*/this._binaryValue = this._decodeBase64(v0);
            }
            catch (IllegalArgumentException a1) {
                /*SL:459*/throw this._constructError("Failed to decode VALUE_STRING as base64 (" + v0 + "): " + a1.getMessage());
            }
            /*SL:464*/this._tokenIncomplete = false;
        }
        else/*SL:466*/ if (this._binaryValue == null) {
            final ByteArrayBuilder v = /*EL:468*/this._getByteArrayBuilder();
            /*SL:469*/this._decodeBase64(this.getText(), v, v0);
            /*SL:470*/this._binaryValue = v.toByteArray();
        }
        /*SL:473*/return this._binaryValue;
    }
    
    @Override
    public int readBinaryValue(final Base64Variant v1, final OutputStream v2) throws IOException {
        /*SL:480*/if (!this._tokenIncomplete || this._currToken != JsonToken.VALUE_STRING) {
            final byte[] a1 = /*EL:481*/this.getBinaryValue(v1);
            /*SL:482*/v2.write(a1);
            /*SL:483*/return a1.length;
        }
        final byte[] v3 = /*EL:486*/this._ioContext.allocBase64Buffer();
        try {
            /*SL:490*/return this._readBinary(v1, v2, v3);
        }
        finally {
            this._ioContext.releaseBase64Buffer(v3);
        }
    }
    
    protected int _readBinary(final Base64Variant v2, final OutputStream v3, final byte[] v4) throws IOException {
        int v5 = /*EL:496*/0;
        final int v6 = /*EL:497*/v4.length - 3;
        int v7 = /*EL:498*/0;
        while (true) {
            /*SL:504*/if (this._inputPtr >= this._inputEnd) {
                /*SL:505*/this._loadMoreGuaranteed();
            }
            char a1 = /*EL:507*/this._inputBuffer[this._inputPtr++];
            /*SL:508*/if (a1 > ' ') {
                int a2 = /*EL:509*/v2.decodeBase64Char(a1);
                /*SL:510*/if (a2 < 0) {
                    /*SL:511*/if (a1 == '\"') {
                        /*SL:512*/break;
                    }
                    /*SL:514*/a2 = this._decodeBase64Escape(v2, a1, 0);
                    /*SL:515*/if (a2 < 0) {
                        /*SL:516*/continue;
                    }
                }
                /*SL:521*/if (v5 > v6) {
                    /*SL:522*/v7 += v5;
                    /*SL:523*/v3.write(v4, 0, v5);
                    /*SL:524*/v5 = 0;
                }
                int a3 = /*EL:527*/a2;
                /*SL:531*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:532*/this._loadMoreGuaranteed();
                }
                /*SL:534*/a1 = this._inputBuffer[this._inputPtr++];
                /*SL:535*/a2 = v2.decodeBase64Char(a1);
                /*SL:536*/if (a2 < 0) {
                    /*SL:537*/a2 = this._decodeBase64Escape(v2, a1, 1);
                }
                /*SL:539*/a3 = (a3 << 6 | a2);
                /*SL:542*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:543*/this._loadMoreGuaranteed();
                }
                /*SL:545*/a1 = this._inputBuffer[this._inputPtr++];
                /*SL:546*/a2 = v2.decodeBase64Char(a1);
                /*SL:549*/if (a2 < 0) {
                    /*SL:550*/if (a2 != -2) {
                        /*SL:552*/if (a1 == '\"' && !v2.usesPadding()) {
                            /*SL:553*/a3 >>= 4;
                            /*SL:554*/v4[v5++] = (byte)a3;
                            /*SL:555*/break;
                        }
                        /*SL:557*/a2 = this._decodeBase64Escape(v2, a1, 2);
                    }
                    /*SL:559*/if (a2 == -2) {
                        /*SL:561*/if (this._inputPtr >= this._inputEnd) {
                            /*SL:562*/this._loadMoreGuaranteed();
                        }
                        /*SL:564*/a1 = this._inputBuffer[this._inputPtr++];
                        /*SL:565*/if (!v2.usesPaddingChar(a1)) {
                            /*SL:566*/throw this.reportInvalidBase64Char(v2, a1, 3, "expected padding character '" + v2.getPaddingChar() + "'");
                        }
                        /*SL:569*/a3 >>= 4;
                        /*SL:570*/v4[v5++] = (byte)a3;
                        /*SL:571*/continue;
                    }
                }
                /*SL:575*/a3 = (a3 << 6 | a2);
                /*SL:577*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:578*/this._loadMoreGuaranteed();
                }
                /*SL:580*/a1 = this._inputBuffer[this._inputPtr++];
                /*SL:581*/a2 = v2.decodeBase64Char(a1);
                /*SL:582*/if (a2 < 0) {
                    /*SL:583*/if (a2 != -2) {
                        /*SL:585*/if (a1 == '\"' && !v2.usesPadding()) {
                            /*SL:586*/a3 >>= 2;
                            /*SL:587*/v4[v5++] = (byte)(a3 >> 8);
                            /*SL:588*/v4[v5++] = (byte)a3;
                            /*SL:589*/break;
                        }
                        /*SL:591*/a2 = this._decodeBase64Escape(v2, a1, 3);
                    }
                    /*SL:593*/if (a2 == -2) {
                        /*SL:600*/a3 >>= 2;
                        /*SL:601*/v4[v5++] = (byte)(a3 >> 8);
                        /*SL:602*/v4[v5++] = (byte)a3;
                        /*SL:603*/continue;
                    }
                }
                /*SL:607*/a3 = (a3 << 6 | a2);
                /*SL:608*/v4[v5++] = (byte)(a3 >> 16);
                /*SL:609*/v4[v5++] = (byte)(a3 >> 8);
                /*SL:610*/v4[v5++] = (byte)a3;
            }
        }
        /*SL:612*/this._tokenIncomplete = false;
        /*SL:613*/if (v5 > 0) {
            /*SL:614*/v7 += v5;
            /*SL:615*/v3.write(v4, 0, v5);
        }
        /*SL:617*/return v7;
    }
    
    @Override
    public final JsonToken nextToken() throws IOException {
        /*SL:637*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:638*/return this._nextAfterName();
        }
        /*SL:642*/this._numTypesValid = 0;
        /*SL:643*/if (this._tokenIncomplete) {
            /*SL:644*/this._skipString();
        }
        int a1 = /*EL:646*/this._skipWSOrEnd();
        /*SL:647*/if (a1 < 0) {
            /*SL:650*/this.close();
            /*SL:651*/return this._currToken = null;
        }
        /*SL:654*/this._binaryValue = null;
        /*SL:657*/if (a1 == 93 || a1 == 125) {
            /*SL:658*/this._closeScope(a1);
            /*SL:659*/return this._currToken;
        }
        /*SL:663*/if (this._parsingContext.expectComma()) {
            /*SL:664*/a1 = this._skipComma(a1);
            /*SL:667*/if ((this._features & ReaderBasedJsonParser.FEAT_MASK_TRAILING_COMMA) != 0x0 && /*EL:668*/(a1 == 93 || a1 == 125)) {
                /*SL:669*/this._closeScope(a1);
                /*SL:670*/return this._currToken;
            }
        }
        final boolean v0 = /*EL:678*/this._parsingContext.inObject();
        /*SL:679*/if (v0) {
            /*SL:681*/this._updateNameLocation();
            final String v = /*EL:682*/(a1 == 34) ? this._parseName() : this._handleOddName(a1);
            /*SL:683*/this._parsingContext.setCurrentName(v);
            /*SL:684*/this._currToken = JsonToken.FIELD_NAME;
            /*SL:685*/a1 = this._skipColon();
        }
        /*SL:687*/this._updateLocation();
        JsonToken v2 = null;
        /*SL:693*/switch (a1) {
            case 34: {
                /*SL:695*/this._tokenIncomplete = true;
                /*SL:696*/v2 = JsonToken.VALUE_STRING;
                /*SL:697*/break;
            }
            case 91: {
                /*SL:699*/if (!v0) {
                    /*SL:700*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                }
                /*SL:702*/v2 = JsonToken.START_ARRAY;
                /*SL:703*/break;
            }
            case 123: {
                /*SL:705*/if (!v0) {
                    /*SL:706*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                }
                /*SL:708*/v2 = JsonToken.START_OBJECT;
                /*SL:709*/break;
            }
            case 125: {
                /*SL:713*/this._reportUnexpectedChar(a1, "expected a value");
            }
            case 116: {
                /*SL:715*/this._matchTrue();
                /*SL:716*/v2 = JsonToken.VALUE_TRUE;
                /*SL:717*/break;
            }
            case 102: {
                /*SL:719*/this._matchFalse();
                /*SL:720*/v2 = JsonToken.VALUE_FALSE;
                /*SL:721*/break;
            }
            case 110: {
                /*SL:723*/this._matchNull();
                /*SL:724*/v2 = JsonToken.VALUE_NULL;
                /*SL:725*/break;
            }
            case 45: {
                /*SL:732*/v2 = this._parseNegNumber();
                /*SL:733*/break;
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
                /*SL:744*/v2 = this._parsePosNumber(a1);
                /*SL:745*/break;
            }
            default: {
                /*SL:747*/v2 = this._handleOddValue(a1);
                break;
            }
        }
        /*SL:751*/if (v0) {
            /*SL:752*/this._nextToken = v2;
            /*SL:753*/return this._currToken;
        }
        /*SL:756*/return this._currToken = v2;
    }
    
    private final JsonToken _nextAfterName() {
        /*SL:761*/this._nameCopied = false;
        final JsonToken v1 = /*EL:762*/this._nextToken;
        /*SL:763*/this._nextToken = null;
        /*SL:768*/if (v1 == JsonToken.START_ARRAY) {
            /*SL:769*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        }
        else/*SL:770*/ if (v1 == JsonToken.START_OBJECT) {
            /*SL:771*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        /*SL:773*/return this._currToken = v1;
    }
    
    @Override
    public void finishToken() throws IOException {
        /*SL:778*/if (this._tokenIncomplete) {
            /*SL:779*/this._tokenIncomplete = false;
            /*SL:780*/this._finishString();
        }
    }
    
    @Override
    public boolean nextFieldName(final SerializableString v-5) throws IOException {
        /*SL:796*/this._numTypesValid = 0;
        /*SL:797*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:798*/this._nextAfterName();
            /*SL:799*/return false;
        }
        /*SL:801*/if (this._tokenIncomplete) {
            /*SL:802*/this._skipString();
        }
        int n = /*EL:804*/this._skipWSOrEnd();
        /*SL:805*/if (n < 0) {
            /*SL:806*/this.close();
            /*SL:807*/this._currToken = null;
            /*SL:808*/return false;
        }
        /*SL:810*/this._binaryValue = null;
        /*SL:813*/if (n == 93 || n == 125) {
            /*SL:814*/this._closeScope(n);
            /*SL:815*/return false;
        }
        /*SL:818*/if (this._parsingContext.expectComma()) {
            /*SL:819*/n = this._skipComma(n);
            /*SL:822*/if ((this._features & ReaderBasedJsonParser.FEAT_MASK_TRAILING_COMMA) != 0x0 && /*EL:823*/(n == 93 || n == 125)) {
                /*SL:824*/this._closeScope(n);
                /*SL:825*/return false;
            }
        }
        /*SL:830*/if (!this._parsingContext.inObject()) {
            /*SL:831*/this._updateLocation();
            /*SL:832*/this._nextTokenNotInObject(n);
            /*SL:833*/return false;
        }
        /*SL:836*/this._updateNameLocation();
        /*SL:837*/if (n == 34) {
            final char[] quotedChars = /*EL:839*/v-5.asQuotedChars();
            final int length = /*EL:840*/quotedChars.length;
            /*SL:843*/if (this._inputPtr + length + 4 < this._inputEnd) {
                final int n2 = /*EL:845*/this._inputPtr + length;
                /*SL:846*/if (this._inputBuffer[n2] == '\"') {
                    int a1 = /*EL:847*/0;
                    int v1;
                    /*SL:850*/for (v1 = this._inputPtr; v1 != n2; /*SL:859*/++v1) {
                        if (quotedChars[a1] != this._inputBuffer[v1]) {
                            return /*EL:864*/this._isNextTokenNameMaybe(n, v-5.getValue());
                        }
                        ++a1;
                    }
                    this._parsingContext.setCurrentName(v-5.getValue());
                    this._isNextTokenNameYes(this._skipColonFast(v1 + 1));
                    return true;
                }
            }
        }
        return this._isNextTokenNameMaybe(n, v-5.getValue());
    }
    
    @Override
    public String nextFieldName() throws IOException {
        /*SL:872*/this._numTypesValid = 0;
        /*SL:873*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:874*/this._nextAfterName();
            /*SL:875*/return null;
        }
        /*SL:877*/if (this._tokenIncomplete) {
            /*SL:878*/this._skipString();
        }
        int v1 = /*EL:880*/this._skipWSOrEnd();
        /*SL:881*/if (v1 < 0) {
            /*SL:882*/this.close();
            /*SL:883*/this._currToken = null;
            /*SL:884*/return null;
        }
        /*SL:886*/this._binaryValue = null;
        /*SL:887*/if (v1 == 93 || v1 == 125) {
            /*SL:888*/this._closeScope(v1);
            /*SL:889*/return null;
        }
        /*SL:891*/if (this._parsingContext.expectComma()) {
            /*SL:892*/v1 = this._skipComma(v1);
            /*SL:893*/if ((this._features & ReaderBasedJsonParser.FEAT_MASK_TRAILING_COMMA) != 0x0 && /*EL:894*/(v1 == 93 || v1 == 125)) {
                /*SL:895*/this._closeScope(v1);
                /*SL:896*/return null;
            }
        }
        /*SL:900*/if (!this._parsingContext.inObject()) {
            /*SL:901*/this._updateLocation();
            /*SL:902*/this._nextTokenNotInObject(v1);
            /*SL:903*/return null;
        }
        /*SL:906*/this._updateNameLocation();
        final String v2 = /*EL:907*/(v1 == 34) ? this._parseName() : this._handleOddName(v1);
        /*SL:908*/this._parsingContext.setCurrentName(v2);
        /*SL:909*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:910*/v1 = this._skipColon();
        /*SL:912*/this._updateLocation();
        /*SL:913*/if (v1 == 34) {
            /*SL:914*/this._tokenIncomplete = true;
            /*SL:915*/this._nextToken = JsonToken.VALUE_STRING;
            /*SL:916*/return v2;
        }
        JsonToken v3 = null;
        /*SL:923*/switch (v1) {
            case 45: {
                /*SL:925*/v3 = this._parseNegNumber();
                /*SL:926*/break;
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
                /*SL:937*/v3 = this._parsePosNumber(v1);
                /*SL:938*/break;
            }
            case 102: {
                /*SL:940*/this._matchFalse();
                /*SL:941*/v3 = JsonToken.VALUE_FALSE;
                /*SL:942*/break;
            }
            case 110: {
                /*SL:944*/this._matchNull();
                /*SL:945*/v3 = JsonToken.VALUE_NULL;
                /*SL:946*/break;
            }
            case 116: {
                /*SL:948*/this._matchTrue();
                /*SL:949*/v3 = JsonToken.VALUE_TRUE;
                /*SL:950*/break;
            }
            case 91: {
                /*SL:952*/v3 = JsonToken.START_ARRAY;
                /*SL:953*/break;
            }
            case 123: {
                /*SL:955*/v3 = JsonToken.START_OBJECT;
                /*SL:956*/break;
            }
            default: {
                /*SL:958*/v3 = this._handleOddValue(v1);
                break;
            }
        }
        /*SL:961*/this._nextToken = v3;
        /*SL:962*/return v2;
    }
    
    private final void _isNextTokenNameYes(final int a1) throws IOException {
        /*SL:967*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:968*/this._updateLocation();
        /*SL:970*/switch (a1) {
            case 34: {
                /*SL:972*/this._tokenIncomplete = true;
                /*SL:973*/this._nextToken = JsonToken.VALUE_STRING;
            }
            case 91: {
                /*SL:976*/this._nextToken = JsonToken.START_ARRAY;
            }
            case 123: {
                /*SL:979*/this._nextToken = JsonToken.START_OBJECT;
            }
            case 116: {
                /*SL:982*/this._matchToken("true", 1);
                /*SL:983*/this._nextToken = JsonToken.VALUE_TRUE;
            }
            case 102: {
                /*SL:986*/this._matchToken("false", 1);
                /*SL:987*/this._nextToken = JsonToken.VALUE_FALSE;
            }
            case 110: {
                /*SL:990*/this._matchToken("null", 1);
                /*SL:991*/this._nextToken = JsonToken.VALUE_NULL;
            }
            case 45: {
                /*SL:994*/this._nextToken = this._parseNegNumber();
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
                /*SL:1006*/this._nextToken = this._parsePosNumber(a1);
            }
            default: {
                /*SL:1009*/this._nextToken = this._handleOddValue(a1);
            }
        }
    }
    
    protected boolean _isNextTokenNameMaybe(int a1, final String a2) throws IOException {
        final String v1 = /*EL:1015*/(a1 == 34) ? this._parseName() : this._handleOddName(a1);
        /*SL:1016*/this._parsingContext.setCurrentName(v1);
        /*SL:1017*/this._currToken = JsonToken.FIELD_NAME;
        /*SL:1018*/a1 = this._skipColon();
        /*SL:1019*/this._updateLocation();
        /*SL:1020*/if (a1 == 34) {
            /*SL:1021*/this._tokenIncomplete = true;
            /*SL:1022*/this._nextToken = JsonToken.VALUE_STRING;
            /*SL:1023*/return a2.equals(v1);
        }
        JsonToken v2 = null;
        /*SL:1027*/switch (a1) {
            case 45: {
                /*SL:1029*/v2 = this._parseNegNumber();
                /*SL:1030*/break;
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
                /*SL:1041*/v2 = this._parsePosNumber(a1);
                /*SL:1042*/break;
            }
            case 102: {
                /*SL:1044*/this._matchFalse();
                /*SL:1045*/v2 = JsonToken.VALUE_FALSE;
                /*SL:1046*/break;
            }
            case 110: {
                /*SL:1048*/this._matchNull();
                /*SL:1049*/v2 = JsonToken.VALUE_NULL;
                /*SL:1050*/break;
            }
            case 116: {
                /*SL:1052*/this._matchTrue();
                /*SL:1053*/v2 = JsonToken.VALUE_TRUE;
                /*SL:1054*/break;
            }
            case 91: {
                /*SL:1056*/v2 = JsonToken.START_ARRAY;
                /*SL:1057*/break;
            }
            case 123: {
                /*SL:1059*/v2 = JsonToken.START_OBJECT;
                /*SL:1060*/break;
            }
            default: {
                /*SL:1062*/v2 = this._handleOddValue(a1);
                break;
            }
        }
        /*SL:1065*/this._nextToken = v2;
        /*SL:1066*/return a2.equals(v1);
    }
    
    private final JsonToken _nextTokenNotInObject(final int a1) throws IOException {
        /*SL:1071*/if (a1 == 34) {
            /*SL:1072*/this._tokenIncomplete = true;
            /*SL:1073*/return this._currToken = JsonToken.VALUE_STRING;
        }
        /*SL:1075*/switch (a1) {
            case 91: {
                /*SL:1077*/this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                /*SL:1078*/return this._currToken = JsonToken.START_ARRAY;
            }
            case 123: {
                /*SL:1080*/this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                /*SL:1081*/return this._currToken = JsonToken.START_OBJECT;
            }
            case 116: {
                /*SL:1083*/this._matchToken("true", 1);
                /*SL:1084*/return this._currToken = JsonToken.VALUE_TRUE;
            }
            case 102: {
                /*SL:1086*/this._matchToken("false", 1);
                /*SL:1087*/return this._currToken = JsonToken.VALUE_FALSE;
            }
            case 110: {
                /*SL:1089*/this._matchToken("null", 1);
                /*SL:1090*/return this._currToken = JsonToken.VALUE_NULL;
            }
            case 45: {
                /*SL:1092*/return this._currToken = this._parseNegNumber();
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
                /*SL:1107*/return this._currToken = this._parsePosNumber(a1);
            }
            case 44:
            case 93: {
                /*SL:1118*/if (this.isEnabled(Feature.ALLOW_MISSING_VALUES)) {
                    /*SL:1119*/--this._inputPtr;
                    /*SL:1120*/return this._currToken = JsonToken.VALUE_NULL;
                }
                break;
            }
        }
        /*SL:1123*/return this._currToken = this._handleOddValue(a1);
    }
    
    @Override
    public final String nextTextValue() throws IOException {
        /*SL:1130*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:1150*/return (this.nextToken() == JsonToken.VALUE_STRING) ? this.getText() : null;
        }
        this._nameCopied = false;
        final JsonToken v1 = this._nextToken;
        this._nextToken = null;
        if ((this._currToken = v1) == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                this._finishString();
            }
            return this._textBuffer.contentsAsString();
        }
        if (v1 == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        }
        else if (v1 == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        return null;
    }
    
    @Override
    public final int nextIntValue(final int v2) throws IOException {
        /*SL:1157*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:1173*/return (this.nextToken() == JsonToken.VALUE_NUMBER_INT) ? this.getIntValue() : v2;
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
    public final long nextLongValue(final long v2) throws IOException {
        /*SL:1180*/if (this._currToken != JsonToken.FIELD_NAME) {
            /*SL:1196*/return (this.nextToken() == JsonToken.VALUE_NUMBER_INT) ? this.getLongValue() : v2;
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
    public final Boolean nextBooleanValue() throws IOException {
        /*SL:1203*/if (this._currToken != JsonToken.FIELD_NAME) {
            final JsonToken v1 = /*EL:1221*/this.nextToken();
            /*SL:1222*/if (v1 != null) {
                final int v2 = /*EL:1223*/v1.id();
                /*SL:1224*/if (v2 == 9) {
                    return Boolean.TRUE;
                }
                /*SL:1225*/if (v2 == 10) {
                    return Boolean.FALSE;
                }
            }
            /*SL:1227*/return null;
        }
        this._nameCopied = false;
        final JsonToken v1 = this._nextToken;
        this._nextToken = null;
        if ((this._currToken = v1) == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (v1 == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        if (v1 == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        }
        else if (v1 == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        return null;
    }
    
    protected final JsonToken _parsePosNumber(int a1) throws IOException {
        int v1 = /*EL:1258*/this._inputPtr;
        final int v2 = /*EL:1259*/v1 - 1;
        final int v3 = /*EL:1260*/this._inputEnd;
        /*SL:1263*/if (a1 == 48) {
            /*SL:1264*/return this._parseNumber2(false, v2);
        }
        int v4 = /*EL:1273*/1;
        /*SL:1278*/while (v1 < v3) {
            /*SL:1282*/a1 = this._inputBuffer[v1++];
            if (/*EL:1283*/a1 >= 48 && a1 <= 57) {
                /*SL:1286*/++v4;
            }
            else {
                /*SL:1288*/if (a1 == 46 || a1 == 101 || a1 == 69) {
                    /*SL:1289*/this._inputPtr = v1;
                    /*SL:1290*/return this._parseFloat(a1, v2, v1, false, v4);
                }
                /*SL:1293*/--v1;
                /*SL:1294*/this._inputPtr = v1;
                /*SL:1296*/if (this._parsingContext.inRoot()) {
                    /*SL:1297*/this._verifyRootSpace(a1);
                }
                final int v5 = /*EL:1299*/v1 - v2;
                /*SL:1300*/this._textBuffer.resetWithShared(this._inputBuffer, v2, v5);
                /*SL:1301*/return this.resetInt(false, v4);
            }
        }
        this._inputPtr = v2;
        return this._parseNumber2(false, v2);
    }
    
    private final JsonToken _parseFloat(int a1, final int a2, int a3, final boolean a4, final int a5) throws IOException {
        final int v1 = /*EL:1307*/this._inputEnd;
        int v2 = /*EL:1308*/0;
        Label_0073: {
            /*SL:1311*/if (a1 == 46) {
                /*SL:1314*/while (a3 < v1) {
                    /*SL:1317*/a1 = this._inputBuffer[a3++];
                    if (/*EL:1318*/a1 >= 48 && a1 <= 57) {
                        /*SL:1321*/++v2;
                    }
                    else {
                        /*SL:1324*/if (v2 == 0) {
                            /*SL:1325*/this.reportUnexpectedNumberChar(a1, "Decimal point not followed by a digit");
                        }
                        break Label_0073;
                    }
                }
                return this._parseNumber2(a4, a2);
            }
        }
        int v3 = /*EL:1328*/0;
        /*SL:1329*/if (a1 == 101 || a1 == 69) {
            /*SL:1330*/if (a3 >= v1) {
                /*SL:1331*/this._inputPtr = a2;
                /*SL:1332*/return this._parseNumber2(a4, a2);
            }
            /*SL:1335*/a1 = this._inputBuffer[a3++];
            /*SL:1336*/if (a1 == 45 || a1 == 43) {
                /*SL:1337*/if (a3 >= v1) {
                    /*SL:1338*/this._inputPtr = a2;
                    /*SL:1339*/return this._parseNumber2(a4, a2);
                }
                /*SL:1341*/a1 = this._inputBuffer[a3++];
            }
            /*SL:1343*/while (a1 <= 57 && a1 >= 48) {
                /*SL:1344*/++v3;
                /*SL:1345*/if (a3 >= v1) {
                    /*SL:1346*/this._inputPtr = a2;
                    /*SL:1347*/return this._parseNumber2(a4, a2);
                }
                /*SL:1349*/a1 = this._inputBuffer[a3++];
            }
            /*SL:1352*/if (v3 == 0) {
                /*SL:1353*/this.reportUnexpectedNumberChar(a1, "Exponent indicator not followed by a digit");
            }
        }
        /*SL:1356*/--a3;
        /*SL:1357*/this._inputPtr = a3;
        /*SL:1359*/if (this._parsingContext.inRoot()) {
            /*SL:1360*/this._verifyRootSpace(a1);
        }
        final int v4 = /*EL:1362*/a3 - a2;
        /*SL:1363*/this._textBuffer.resetWithShared(this._inputBuffer, a2, v4);
        /*SL:1365*/return this.resetFloat(a4, a5, v2, v3);
    }
    
    protected final JsonToken _parseNegNumber() throws IOException {
        int v1 = /*EL:1370*/this._inputPtr;
        final int v2 = /*EL:1371*/v1 - 1;
        final int v3 = /*EL:1372*/this._inputEnd;
        /*SL:1374*/if (v1 >= v3) {
            /*SL:1375*/return this._parseNumber2(true, v2);
        }
        int v4 = /*EL:1377*/this._inputBuffer[v1++];
        /*SL:1379*/if (v4 > 57 || v4 < 48) {
            /*SL:1380*/this._inputPtr = v1;
            /*SL:1381*/return this._handleInvalidNumberStart(v4, true);
        }
        /*SL:1384*/if (v4 == 48) {
            /*SL:1385*/return this._parseNumber2(true, v2);
        }
        int v5 = /*EL:1387*/1;
        /*SL:1392*/while (v1 < v3) {
            /*SL:1395*/v4 = this._inputBuffer[v1++];
            if (/*EL:1396*/v4 >= 48 && v4 <= 57) {
                /*SL:1399*/++v5;
            }
            else {
                /*SL:1402*/if (v4 == 46 || v4 == 101 || v4 == 69) {
                    /*SL:1403*/this._inputPtr = v1;
                    /*SL:1404*/return this._parseFloat(v4, v2, v1, true, v5);
                }
                /*SL:1406*/--v1;
                /*SL:1407*/this._inputPtr = v1;
                /*SL:1408*/if (this._parsingContext.inRoot()) {
                    /*SL:1409*/this._verifyRootSpace(v4);
                }
                final int v6 = /*EL:1411*/v1 - v2;
                /*SL:1412*/this._textBuffer.resetWithShared(this._inputBuffer, v2, v6);
                /*SL:1413*/return this.resetInt(true, v5);
            }
        }
        return this._parseNumber2(true, v2);
    }
    
    private final JsonToken _parseNumber2(final boolean a1, final int a2) throws IOException {
        /*SL:1425*/this._inputPtr = (a1 ? (a2 + 1) : a2);
        char[] v1 = /*EL:1426*/this._textBuffer.emptyAndGetCurrentSegment();
        int v2 = /*EL:1427*/0;
        /*SL:1430*/if (a1) {
            /*SL:1431*/v1[v2++] = '-';
        }
        int v3 = /*EL:1435*/0;
        char v4 = /*EL:1436*/(this._inputPtr < this._inputEnd) ? this._inputBuffer[this._inputPtr++] : this.getNextChar("No digit following minus sign", JsonToken.VALUE_NUMBER_INT);
        /*SL:1438*/if (v4 == '0') {
            /*SL:1439*/v4 = this._verifyNoLeadingZeroes();
        }
        boolean v5 = /*EL:1441*/false;
        /*SL:1445*/while (v4 >= '0' && v4 <= '9') {
            /*SL:1446*/++v3;
            /*SL:1447*/if (v2 >= v1.length) {
                /*SL:1448*/v1 = this._textBuffer.finishCurrentSegment();
                /*SL:1449*/v2 = 0;
            }
            /*SL:1451*/v1[v2++] = v4;
            /*SL:1452*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
                /*SL:1454*/v4 = '\0';
                /*SL:1455*/v5 = true;
                /*SL:1456*/break;
            }
            /*SL:1458*/v4 = this._inputBuffer[this._inputPtr++];
        }
        /*SL:1461*/if (v3 == 0) {
            /*SL:1462*/return this._handleInvalidNumberStart(v4, a1);
        }
        int v6 = /*EL:1465*/0;
        Label_0348: {
            /*SL:1467*/if (v4 == '.') {
                /*SL:1468*/if (v2 >= v1.length) {
                    /*SL:1469*/v1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1470*/v2 = 0;
                }
                /*SL:1472*/v1[v2++] = v4;
                while (true) {
                    /*SL:1476*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
                        /*SL:1480*/v4 = this._inputBuffer[this._inputPtr++];
                        /*SL:1481*/if (v4 >= '0') {
                            if (v4 <= '9') {
                                /*SL:1484*/++v6;
                                /*SL:1485*/if (v2 >= v1.length) {
                                    /*SL:1486*/v1 = this._textBuffer.finishCurrentSegment();
                                    /*SL:1487*/v2 = 0;
                                }
                                /*SL:1489*/v1[v2++] = v4;
                                continue;
                            }
                        }
                        /*SL:1492*/if (v6 == 0) {
                            /*SL:1493*/this.reportUnexpectedNumberChar(v4, "Decimal point not followed by a digit");
                        }
                        break Label_0348;
                    }
                    v5 = true;
                    continue;
                }
            }
        }
        int v7 = /*EL:1497*/0;
        /*SL:1498*/if (v4 == 'e' || v4 == 'E') {
            /*SL:1499*/if (v2 >= v1.length) {
                /*SL:1500*/v1 = this._textBuffer.finishCurrentSegment();
                /*SL:1501*/v2 = 0;
            }
            /*SL:1503*/v1[v2++] = v4;
            /*SL:1505*/v4 = ((this._inputPtr < this._inputEnd) ? this._inputBuffer[this._inputPtr++] : this.getNextChar("expected a digit for number exponent"));
            /*SL:1508*/if (v4 == '-' || v4 == '+') {
                /*SL:1509*/if (v2 >= v1.length) {
                    /*SL:1510*/v1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1511*/v2 = 0;
                }
                /*SL:1513*/v1[v2++] = v4;
                /*SL:1515*/v4 = ((this._inputPtr < this._inputEnd) ? this._inputBuffer[this._inputPtr++] : this.getNextChar("expected a digit for number exponent"));
            }
            /*SL:1520*/while (v4 <= '9' && v4 >= '0') {
                /*SL:1521*/++v7;
                /*SL:1522*/if (v2 >= v1.length) {
                    /*SL:1523*/v1 = this._textBuffer.finishCurrentSegment();
                    /*SL:1524*/v2 = 0;
                }
                /*SL:1526*/v1[v2++] = v4;
                /*SL:1527*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
                    /*SL:1528*/v5 = true;
                    /*SL:1529*/break;
                }
                /*SL:1531*/v4 = this._inputBuffer[this._inputPtr++];
            }
            /*SL:1534*/if (v7 == 0) {
                /*SL:1535*/this.reportUnexpectedNumberChar(v4, "Exponent indicator not followed by a digit");
            }
        }
        /*SL:1540*/if (!v5) {
            /*SL:1541*/--this._inputPtr;
            /*SL:1542*/if (this._parsingContext.inRoot()) {
                /*SL:1543*/this._verifyRootSpace(v4);
            }
        }
        /*SL:1546*/this._textBuffer.setCurrentLength(v2);
        /*SL:1548*/return this.reset(a1, v3, v6, v7);
    }
    
    private final char _verifyNoLeadingZeroes() throws IOException {
        /*SL:1558*/if (this._inputPtr < this._inputEnd) {
            final char v1 = /*EL:1559*/this._inputBuffer[this._inputPtr];
            /*SL:1561*/if (v1 < '0' || v1 > '9') {
                /*SL:1562*/return '0';
            }
        }
        /*SL:1566*/return this._verifyNLZ2();
    }
    
    private char _verifyNLZ2() throws IOException {
        /*SL:1571*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
            /*SL:1572*/return '0';
        }
        char v1 = /*EL:1574*/this._inputBuffer[this._inputPtr];
        /*SL:1575*/if (v1 < '0' || v1 > '9') {
            /*SL:1576*/return '0';
        }
        /*SL:1578*/if (!this.isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            /*SL:1579*/this.reportInvalidNumber("Leading zeroes not allowed");
        }
        /*SL:1582*/++this._inputPtr;
        /*SL:1583*/if (v1 == '0') {
            /*SL:1584*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
                /*SL:1585*/v1 = this._inputBuffer[this._inputPtr];
                /*SL:1586*/if (v1 < '0' || v1 > '9') {
                    /*SL:1587*/return '0';
                }
                /*SL:1589*/++this._inputPtr;
                /*SL:1590*/if (v1 != '0') {
                    break;
                }
            }
        }
        /*SL:1595*/return v1;
    }
    
    protected JsonToken _handleInvalidNumberStart(int v2, final boolean v3) throws IOException {
        /*SL:1604*/if (v2 == 73) {
            /*SL:1605*/if (this._inputPtr >= this._inputEnd && /*EL:1606*/!this._loadMore()) {
                /*SL:1607*/this._reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_INT);
            }
            /*SL:1610*/v2 = this._inputBuffer[this._inputPtr++];
            /*SL:1611*/if (v2 == 78) {
                final String a1 = /*EL:1612*/v3 ? "-INF" : "+INF";
                /*SL:1613*/this._matchToken(a1, 3);
                /*SL:1614*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    /*SL:1615*/return this.resetAsNaN(a1, v3 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
                }
                /*SL:1617*/this._reportError("Non-standard token '" + a1 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
            }
            else/*SL:1618*/ if (v2 == 110) {
                final String a2 = /*EL:1619*/v3 ? "-Infinity" : "+Infinity";
                /*SL:1620*/this._matchToken(a2, 3);
                /*SL:1621*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    /*SL:1622*/return this.resetAsNaN(a2, v3 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
                }
                /*SL:1624*/this._reportError("Non-standard token '" + a2 + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
            }
        }
        /*SL:1627*/this.reportUnexpectedNumberChar(v2, "expected digit (0-9) to follow minus sign, for valid numeric value");
        /*SL:1628*/return null;
    }
    
    private final void _verifyRootSpace(final int a1) throws IOException {
        /*SL:1641*/++this._inputPtr;
        /*SL:1642*/switch (a1) {
            case 9:
            case 32: {}
            case 13: {
                /*SL:1647*/this._skipCR();
            }
            case 10: {
                /*SL:1650*/++this._currInputRow;
                /*SL:1651*/this._currInputRowStart = this._inputPtr;
            }
            default: {
                /*SL:1654*/this._reportMissingRootWS(a1);
            }
        }
    }
    
    protected final String _parseName() throws IOException {
        int i = /*EL:1667*/this._inputPtr;
        int hashSeed = /*EL:1668*/this._hashSeed;
        final int[] icLatin1 = ReaderBasedJsonParser._icLatin1;
        /*SL:1671*/while (i < this._inputEnd) {
            final int v0 = /*EL:1672*/this._inputBuffer[i];
            /*SL:1673*/if (v0 < icLatin1.length && icLatin1[v0] != 0) {
                /*SL:1674*/if (v0 == 34) {
                    final int v = /*EL:1675*/this._inputPtr;
                    /*SL:1676*/this._inputPtr = i + 1;
                    /*SL:1677*/return this._symbols.findSymbol(this._inputBuffer, v, i - v, hashSeed);
                }
                break;
            }
            else {
                /*SL:1681*/hashSeed = hashSeed * 33 + v0;
                /*SL:1682*/++i;
            }
        }
        final int v0 = /*EL:1684*/this._inputPtr;
        /*SL:1685*/this._inputPtr = i;
        /*SL:1686*/return this._parseName2(v0, hashSeed, 34);
    }
    
    private String _parseName2(final int v-5, int v-4, final int v-3) throws IOException {
        /*SL:1691*/this._textBuffer.resetWithShared(this._inputBuffer, v-5, this._inputPtr - v-5);
        char[] array = /*EL:1696*/this._textBuffer.getCurrentSegment();
        int currentSegmentSize = /*EL:1697*/this._textBuffer.getCurrentSegmentSize();
        while (true) {
            /*SL:1700*/if (this._inputPtr >= this._inputEnd && /*EL:1701*/!this._loadMore()) {
                /*SL:1702*/this._reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
            }
            final int a2;
            char a1 = /*EL:1706*/(char)(a2 = this._inputBuffer[this._inputPtr++]);
            /*SL:1707*/if (a2 <= 92) {
                /*SL:1708*/if (a2 == 92) {
                    /*SL:1713*/a1 = this._decodeEscaped();
                }
                else/*SL:1714*/ if (a2 <= v-3) {
                    /*SL:1715*/if (a2 == v-3) {
                        break;
                    }
                    /*SL:1718*/if (a2 < 32) {
                        /*SL:1719*/this._throwUnquotedSpace(a2, "name");
                    }
                }
            }
            /*SL:1723*/v-4 = v-4 * 33 + a1;
            /*SL:1725*/array[currentSegmentSize++] = a1;
            /*SL:1728*/if (currentSegmentSize >= array.length) {
                /*SL:1729*/array = this._textBuffer.finishCurrentSegment();
                /*SL:1730*/currentSegmentSize = 0;
            }
        }
        /*SL:1733*/this._textBuffer.setCurrentLength(currentSegmentSize);
        final TextBuffer a3 = /*EL:1735*/this._textBuffer;
        final char[] v1 = /*EL:1736*/a3.getTextBuffer();
        final int v2 = /*EL:1737*/a3.getTextOffset();
        final int v3 = /*EL:1738*/a3.size();
        /*SL:1739*/return this._symbols.findSymbol(v1, v2, v3, v-4);
    }
    
    protected String _handleOddName(final int v-7) throws IOException {
        /*SL:1752*/if (v-7 == 39 && this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            /*SL:1753*/return this._parseAposName();
        }
        /*SL:1756*/if (!this.isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            /*SL:1757*/this._reportUnexpectedChar(v-7, "was expecting double-quote to start field name");
        }
        final int[] inputCodeLatin1JsNames = /*EL:1759*/CharTypes.getInputCodeLatin1JsNames();
        final int length = /*EL:1760*/inputCodeLatin1JsNames.length;
        final boolean javaIdentifierPart;
        /*SL:1765*/if (v-7 < length) {
            final boolean a1 = /*EL:1766*/inputCodeLatin1JsNames[v-7] == 0;
        }
        else {
            /*SL:1768*/javaIdentifierPart = Character.isJavaIdentifierPart((char)v-7);
        }
        /*SL:1770*/if (!javaIdentifierPart) {
            /*SL:1771*/this._reportUnexpectedChar(v-7, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int inputPtr = /*EL:1773*/this._inputPtr;
        int hashSeed = /*EL:1774*/this._hashSeed;
        final int inputEnd = /*EL:1775*/this._inputEnd;
        /*SL:1777*/if (inputPtr < inputEnd) {
            /*SL:1793*/do {
                final int v0 = this._inputBuffer[inputPtr];
                if (v0 < length) {
                    if (inputCodeLatin1JsNames[v0] != 0) {
                        final int v = this._inputPtr - 1;
                        this._inputPtr = inputPtr;
                        return this._symbols.findSymbol(this._inputBuffer, v, inputPtr - v, hashSeed);
                    }
                }
                else if (!Character.isJavaIdentifierPart((char)v0)) {
                    final int v = this._inputPtr - 1;
                    this._inputPtr = inputPtr;
                    return this._symbols.findSymbol(this._inputBuffer, v, inputPtr - v, hashSeed);
                }
                hashSeed = hashSeed * 33 + v0;
            } while (++inputPtr < inputEnd);
        }
        final int v0 = /*EL:1795*/this._inputPtr - 1;
        /*SL:1796*/this._inputPtr = inputPtr;
        /*SL:1797*/return this._handleOddName2(v0, hashSeed, inputCodeLatin1JsNames);
    }
    
    protected String _parseAposName() throws IOException {
        int inputPtr = /*EL:1803*/this._inputPtr;
        int hashSeed = /*EL:1804*/this._hashSeed;
        final int inputEnd = /*EL:1805*/this._inputEnd;
        /*SL:1807*/if (inputPtr < inputEnd) {
            final int[] icLatin1 = ReaderBasedJsonParser._icLatin1;
            final int length = /*EL:1809*/icLatin1.length;
            /*SL:1823*/do {
                final int v0 = this._inputBuffer[inputPtr];
                if (v0 == 39) {
                    final int v = this._inputPtr;
                    this._inputPtr = inputPtr + 1;
                    return this._symbols.findSymbol(this._inputBuffer, v, inputPtr - v, hashSeed);
                }
                if (v0 < length && icLatin1[v0] != 0) {
                    break;
                }
                hashSeed = hashSeed * 33 + v0;
            } while (++inputPtr < inputEnd);
        }
        final int inputPtr2 = /*EL:1826*/this._inputPtr;
        /*SL:1827*/this._inputPtr = inputPtr;
        /*SL:1829*/return this._parseName2(inputPtr2, hashSeed, 39);
    }
    
    protected JsonToken _handleOddValue(final int a1) throws IOException {
        /*SL:1839*/switch (a1) {
            case 39: {
                /*SL:1846*/if (this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    /*SL:1847*/return this._handleApos();
                }
                break;
            }
            case 93: {
                /*SL:1855*/if (!this._parsingContext.inArray()) {
                    /*SL:1856*/break;
                }
            }
            case 44: {
                /*SL:1860*/if (this.isEnabled(Feature.ALLOW_MISSING_VALUES)) {
                    /*SL:1861*/--this._inputPtr;
                    /*SL:1862*/return JsonToken.VALUE_NULL;
                }
                break;
            }
            case 78: {
                /*SL:1866*/this._matchToken("NaN", 1);
                /*SL:1867*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    /*SL:1868*/return this.resetAsNaN("NaN", Double.NaN);
                }
                /*SL:1870*/this._reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                /*SL:1871*/break;
            }
            case 73: {
                /*SL:1873*/this._matchToken("Infinity", 1);
                /*SL:1874*/if (this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    /*SL:1875*/return this.resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
                }
                /*SL:1877*/this._reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                /*SL:1878*/break;
            }
            case 43: {
                /*SL:1880*/if (this._inputPtr >= this._inputEnd && /*EL:1881*/!this._loadMore()) {
                    /*SL:1882*/this._reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_INT);
                }
                /*SL:1885*/return this._handleInvalidNumberStart(this._inputBuffer[this._inputPtr++], false);
            }
        }
        /*SL:1888*/if (Character.isJavaIdentifierStart(a1)) {
            /*SL:1889*/this._reportInvalidToken("" + (char)a1, "('true', 'false' or 'null')");
        }
        /*SL:1892*/this._reportUnexpectedChar(a1, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        /*SL:1893*/return null;
    }
    
    protected JsonToken _handleApos() throws IOException {
        char[] array = /*EL:1898*/this._textBuffer.emptyAndGetCurrentSegment();
        int v0 = /*EL:1899*/this._textBuffer.getCurrentSegmentSize();
        while (true) {
            /*SL:1902*/if (this._inputPtr >= this._inputEnd && /*EL:1903*/!this._loadMore()) {
                /*SL:1904*/this._reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
            }
            final int v2;
            char v = /*EL:1909*/(char)(v2 = this._inputBuffer[this._inputPtr++]);
            /*SL:1910*/if (v2 <= 92) {
                /*SL:1911*/if (v2 == 92) {
                    /*SL:1916*/v = this._decodeEscaped();
                }
                else/*SL:1917*/ if (v2 <= 39) {
                    /*SL:1918*/if (v2 == 39) {
                        break;
                    }
                    /*SL:1921*/if (v2 < 32) {
                        /*SL:1922*/this._throwUnquotedSpace(v2, "string value");
                    }
                }
            }
            /*SL:1927*/if (v0 >= array.length) {
                /*SL:1928*/array = this._textBuffer.finishCurrentSegment();
                /*SL:1929*/v0 = 0;
            }
            /*SL:1932*/array[v0++] = v;
        }
        /*SL:1934*/this._textBuffer.setCurrentLength(v0);
        /*SL:1935*/return JsonToken.VALUE_STRING;
    }
    
    private String _handleOddName2(final int v-6, int v-5, final int[] v-4) throws IOException {
        /*SL:1940*/this._textBuffer.resetWithShared(this._inputBuffer, v-6, this._inputPtr - v-6);
        char[] array = /*EL:1941*/this._textBuffer.getCurrentSegment();
        int currentSegmentSize = /*EL:1942*/this._textBuffer.getCurrentSegmentSize();
        final int length = /*EL:1943*/v-4.length;
        while (true) {
            /*SL:1946*/while (this._inputPtr < this._inputEnd || /*EL:1947*/this._loadMore()) {
                final int a2;
                final char a1 = /*EL:1952*/(char)(a2 = this._inputBuffer[this._inputPtr]);
                /*SL:1953*/if (a2 <= length) {
                    /*SL:1954*/if (v-4[a2] != 0) {
                        /*SL:1955*/break;
                    }
                }
                else/*SL:1957*/ if (!Character.isJavaIdentifierPart(a1)) {
                    /*SL:1958*/break;
                }
                /*SL:1960*/++this._inputPtr;
                /*SL:1961*/v-5 = v-5 * 33 + a2;
                /*SL:1963*/array[currentSegmentSize++] = a1;
                /*SL:1966*/if (currentSegmentSize < array.length) {
                    continue;
                }
                /*SL:1967*/array = this._textBuffer.finishCurrentSegment();
                /*SL:1968*/currentSegmentSize = 0;
                /*SL:1970*/continue;
                /*SL:1971*/this._textBuffer.setCurrentLength(currentSegmentSize);
                final TextBuffer a3 = /*EL:1973*/this._textBuffer;
                final char[] v1 = /*EL:1974*/a3.getTextBuffer();
                final int v2 = /*EL:1975*/a3.getTextOffset();
                final int v3 = /*EL:1976*/a3.size();
                /*SL:1978*/return this._symbols.findSymbol(v1, v2, v3, v-5);
            }
            continue;
        }
    }
    
    @Override
    protected final void _finishString() throws IOException {
        int inputPtr = /*EL:1989*/this._inputPtr;
        final int inputEnd = /*EL:1990*/this._inputEnd;
        /*SL:1992*/if (inputPtr < inputEnd) {
            final int[] icLatin1 = ReaderBasedJsonParser._icLatin1;
            final int v0 = /*EL:1994*/icLatin1.length;
            /*SL:2008*/do {
                final int v = this._inputBuffer[inputPtr];
                if (v < v0 && icLatin1[v] != 0) {
                    if (v == 34) {
                        this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, inputPtr - this._inputPtr);
                        this._inputPtr = inputPtr + 1;
                        return;
                    }
                    break;
                }
            } while (++inputPtr < inputEnd);
        }
        /*SL:2014*/this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, inputPtr - this._inputPtr);
        /*SL:2015*/this._inputPtr = inputPtr;
        /*SL:2016*/this._finishString2();
    }
    
    protected void _finishString2() throws IOException {
        char[] array = /*EL:2021*/this._textBuffer.getCurrentSegment();
        int currentSegmentSize = /*EL:2022*/this._textBuffer.getCurrentSegmentSize();
        final int[] icLatin1 = ReaderBasedJsonParser._icLatin1;
        final int v0 = /*EL:2024*/icLatin1.length;
        while (true) {
            /*SL:2027*/if (this._inputPtr >= this._inputEnd && /*EL:2028*/!this._loadMore()) {
                /*SL:2029*/this._reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
            }
            final int v2;
            char v = /*EL:2034*/(char)(v2 = this._inputBuffer[this._inputPtr++]);
            /*SL:2035*/if (v2 < v0 && icLatin1[v2] != 0) {
                /*SL:2036*/if (v2 == 34) {
                    break;
                }
                /*SL:2038*/if (v2 == 92) {
                    /*SL:2043*/v = this._decodeEscaped();
                }
                else/*SL:2044*/ if (v2 < 32) {
                    /*SL:2045*/this._throwUnquotedSpace(v2, "string value");
                }
            }
            /*SL:2049*/if (currentSegmentSize >= array.length) {
                /*SL:2050*/array = this._textBuffer.finishCurrentSegment();
                /*SL:2051*/currentSegmentSize = 0;
            }
            /*SL:2054*/array[currentSegmentSize++] = v;
        }
        /*SL:2056*/this._textBuffer.setCurrentLength(currentSegmentSize);
    }
    
    protected final void _skipString() throws IOException {
        /*SL:2066*/this._tokenIncomplete = false;
        int n = /*EL:2068*/this._inputPtr;
        int n2 = /*EL:2069*/this._inputEnd;
        final char[] v0 = /*EL:2070*/this._inputBuffer;
        while (true) {
            /*SL:2073*/if (n >= n2) {
                /*SL:2074*/this._inputPtr = n;
                /*SL:2075*/if (!this._loadMore()) {
                    /*SL:2076*/this._reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
                }
                /*SL:2079*/n = this._inputPtr;
                /*SL:2080*/n2 = this._inputEnd;
            }
            final int v2;
            final char v = /*EL:2083*/(char)(v2 = v0[n++]);
            /*SL:2084*/if (v2 <= 92) {
                /*SL:2085*/if (v2 == 92) {
                    /*SL:2088*/this._inputPtr = n;
                    /*SL:2089*/this._decodeEscaped();
                    /*SL:2090*/n = this._inputPtr;
                    /*SL:2091*/n2 = this._inputEnd;
                }
                else {
                    /*SL:2092*/if (v2 > 34) {
                        continue;
                    }
                    /*SL:2093*/if (v2 == 34) {
                        break;
                    }
                    /*SL:2097*/if (v2 >= 32) {
                        continue;
                    }
                    /*SL:2098*/this._inputPtr = n;
                    /*SL:2099*/this._throwUnquotedSpace(v2, "string value");
                }
            }
        }
        this._inputPtr = n;
    }
    
    protected final void _skipCR() throws IOException {
        /*SL:2117*/if ((this._inputPtr < this._inputEnd || this._loadMore()) && /*EL:2118*/this._inputBuffer[this._inputPtr] == '\n') {
            /*SL:2119*/++this._inputPtr;
        }
        /*SL:2122*/++this._currInputRow;
        /*SL:2123*/this._currInputRowStart = this._inputPtr;
    }
    
    private final int _skipColon() throws IOException {
        /*SL:2128*/if (this._inputPtr + 4 >= this._inputEnd) {
            /*SL:2129*/return this._skipColon2(false);
        }
        char v0 = /*EL:2131*/this._inputBuffer[this._inputPtr];
        /*SL:2132*/if (v0 == ':') {
            int v = /*EL:2133*/this._inputBuffer[++this._inputPtr];
            /*SL:2134*/if (v <= 32) {
                /*SL:2141*/if (v == 32 || v == 9) {
                    /*SL:2142*/v = this._inputBuffer[++this._inputPtr];
                    /*SL:2143*/if (v > 32) {
                        /*SL:2144*/if (v == 47 || v == 35) {
                            /*SL:2145*/return this._skipColon2(true);
                        }
                        /*SL:2147*/++this._inputPtr;
                        /*SL:2148*/return v;
                    }
                }
                /*SL:2151*/return this._skipColon2(true);
            }
            if (v == 47 || v == 35) {
                return this._skipColon2(true);
            }
            ++this._inputPtr;
            return v;
        }
        else {
            /*SL:2153*/if (v0 == ' ' || v0 == '\t') {
                /*SL:2154*/v0 = this._inputBuffer[++this._inputPtr];
            }
            /*SL:2156*/if (v0 != ':') {
                /*SL:2177*/return this._skipColon2(false);
            }
            int v = this._inputBuffer[++this._inputPtr];
            if (v <= 32) {
                if (v == 32 || v == 9) {
                    v = this._inputBuffer[++this._inputPtr];
                    if (v > 32) {
                        if (v == 47 || v == 35) {
                            return this._skipColon2(true);
                        }
                        ++this._inputPtr;
                        return v;
                    }
                }
                return this._skipColon2(true);
            }
            if (v == 47 || v == 35) {
                return this._skipColon2(true);
            }
            ++this._inputPtr;
            return v;
        }
    }
    
    private final int _skipColon2(boolean v2) throws IOException {
        /*SL:2182*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int a1 = /*EL:2183*/this._inputBuffer[this._inputPtr++];
            /*SL:2184*/if (a1 > 32) {
                /*SL:2185*/if (a1 == 47) {
                    /*SL:2186*/this._skipComment();
                }
                else {
                    /*SL:2189*/if (a1 == 35 && /*EL:2190*/this._skipYAMLComment()) {
                        /*SL:2191*/continue;
                    }
                    /*SL:2194*/if (v2) {
                        /*SL:2195*/return a1;
                    }
                    /*SL:2197*/if (a1 != 58) {
                        /*SL:2198*/this._reportUnexpectedChar(a1, "was expecting a colon to separate field name and value");
                    }
                    /*SL:2200*/v2 = true;
                }
            }
            else {
                /*SL:2203*/if (a1 >= 32) {
                    continue;
                }
                /*SL:2204*/if (a1 == 10) {
                    /*SL:2205*/++this._currInputRow;
                    /*SL:2206*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2207*/ if (a1 == 13) {
                    /*SL:2208*/this._skipCR();
                }
                else {
                    /*SL:2209*/if (a1 == 9) {
                        continue;
                    }
                    /*SL:2210*/this._throwInvalidSpace(a1);
                }
            }
        }
        /*SL:2214*/this._reportInvalidEOF(" within/between " + this._parsingContext.typeDesc() + " entries", null);
        /*SL:2216*/return -1;
    }
    
    private final int _skipColonFast(int a1) throws IOException {
        int v1 = /*EL:2222*/this._inputBuffer[a1++];
        /*SL:2223*/if (v1 == 58) {
            /*SL:2224*/v1 = this._inputBuffer[a1++];
            /*SL:2225*/if (v1 > 32) {
                /*SL:2226*/if (v1 != 47 && v1 != 35) {
                    /*SL:2227*/this._inputPtr = a1;
                    /*SL:2228*/return v1;
                }
            }
            else/*SL:2230*/ if (v1 == 32 || v1 == 9) {
                /*SL:2231*/v1 = this._inputBuffer[a1++];
                /*SL:2233*/if (v1 > 32 && v1 != 47 && v1 != 35) {
                    /*SL:2234*/this._inputPtr = a1;
                    /*SL:2235*/return v1;
                }
            }
            /*SL:2239*/this._inputPtr = a1 - 1;
            /*SL:2240*/return this._skipColon2(true);
        }
        /*SL:2242*/if (v1 == 32 || v1 == 9) {
            /*SL:2243*/v1 = this._inputBuffer[a1++];
        }
        final boolean v2 = /*EL:2245*/v1 == 58;
        /*SL:2246*/if (v2) {
            /*SL:2247*/v1 = this._inputBuffer[a1++];
            /*SL:2248*/if (v1 > 32) {
                /*SL:2249*/if (v1 != 47 && v1 != 35) {
                    /*SL:2250*/this._inputPtr = a1;
                    /*SL:2251*/return v1;
                }
            }
            else/*SL:2253*/ if (v1 == 32 || v1 == 9) {
                /*SL:2254*/v1 = this._inputBuffer[a1++];
                /*SL:2256*/if (v1 > 32 && v1 != 47 && v1 != 35) {
                    /*SL:2257*/this._inputPtr = a1;
                    /*SL:2258*/return v1;
                }
            }
        }
        /*SL:2263*/this._inputPtr = a1 - 1;
        /*SL:2264*/return this._skipColon2(v2);
    }
    
    private final int _skipComma(int a1) throws IOException {
        /*SL:2270*/if (a1 != 44) {
            /*SL:2271*/this._reportUnexpectedChar(a1, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
        }
        /*SL:2273*/while (this._inputPtr < this._inputEnd) {
            /*SL:2274*/a1 = this._inputBuffer[this._inputPtr++];
            /*SL:2275*/if (a1 > 32) {
                /*SL:2276*/if (a1 == 47 || a1 == 35) {
                    /*SL:2277*/--this._inputPtr;
                    /*SL:2278*/return this._skipAfterComma2();
                }
                /*SL:2280*/return a1;
            }
            else {
                /*SL:2282*/if (a1 >= 32) {
                    continue;
                }
                /*SL:2283*/if (a1 == 10) {
                    /*SL:2284*/++this._currInputRow;
                    /*SL:2285*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2286*/ if (a1 == 13) {
                    /*SL:2287*/this._skipCR();
                }
                else {
                    /*SL:2288*/if (a1 == 9) {
                        continue;
                    }
                    /*SL:2289*/this._throwInvalidSpace(a1);
                }
            }
        }
        /*SL:2293*/return this._skipAfterComma2();
    }
    
    private final int _skipAfterComma2() throws IOException {
        /*SL:2298*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int v1 = /*EL:2299*/this._inputBuffer[this._inputPtr++];
            /*SL:2300*/if (v1 > 32) {
                /*SL:2301*/if (v1 == 47) {
                    /*SL:2302*/this._skipComment();
                }
                else {
                    /*SL:2305*/if (v1 == 35 && /*EL:2306*/this._skipYAMLComment()) {
                        /*SL:2307*/continue;
                    }
                    /*SL:2310*/return v1;
                }
            }
            else {
                /*SL:2312*/if (v1 >= 32) {
                    continue;
                }
                /*SL:2313*/if (v1 == 10) {
                    /*SL:2314*/++this._currInputRow;
                    /*SL:2315*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2316*/ if (v1 == 13) {
                    /*SL:2317*/this._skipCR();
                }
                else {
                    /*SL:2318*/if (v1 == 9) {
                        continue;
                    }
                    /*SL:2319*/this._throwInvalidSpace(v1);
                }
            }
        }
        /*SL:2323*/throw this._constructError("Unexpected end-of-input within/between " + this._parsingContext.typeDesc() + " entries");
    }
    
    private final int _skipWSOrEnd() throws IOException {
        /*SL:2330*/if (this._inputPtr >= this._inputEnd && /*EL:2331*/!this._loadMore()) {
            /*SL:2332*/return this._eofAsNextChar();
        }
        int v1 = /*EL:2335*/this._inputBuffer[this._inputPtr++];
        /*SL:2336*/if (v1 <= 32) {
            /*SL:2343*/if (v1 != 32) {
                /*SL:2344*/if (v1 == 10) {
                    /*SL:2345*/++this._currInputRow;
                    /*SL:2346*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2347*/ if (v1 == 13) {
                    /*SL:2348*/this._skipCR();
                }
                else/*SL:2349*/ if (v1 != 9) {
                    /*SL:2350*/this._throwInvalidSpace(v1);
                }
            }
            /*SL:2354*/while (this._inputPtr < this._inputEnd) {
                /*SL:2355*/v1 = this._inputBuffer[this._inputPtr++];
                /*SL:2356*/if (v1 > 32) {
                    /*SL:2357*/if (v1 == 47 || v1 == 35) {
                        /*SL:2358*/--this._inputPtr;
                        /*SL:2359*/return this._skipWSOrEnd2();
                    }
                    /*SL:2361*/return v1;
                }
                else {
                    /*SL:2363*/if (v1 == 32) {
                        continue;
                    }
                    /*SL:2364*/if (v1 == 10) {
                        /*SL:2365*/++this._currInputRow;
                        /*SL:2366*/this._currInputRowStart = this._inputPtr;
                    }
                    else/*SL:2367*/ if (v1 == 13) {
                        /*SL:2368*/this._skipCR();
                    }
                    else {
                        /*SL:2369*/if (v1 == 9) {
                            continue;
                        }
                        /*SL:2370*/this._throwInvalidSpace(v1);
                    }
                }
            }
            /*SL:2374*/return this._skipWSOrEnd2();
        }
        if (v1 == 47 || v1 == 35) {
            --this._inputPtr;
            return this._skipWSOrEnd2();
        }
        return v1;
    }
    
    private int _skipWSOrEnd2() throws IOException {
        /*SL:2380*/while (this._inputPtr < this._inputEnd || /*EL:2381*/this._loadMore()) {
            final int v1 = /*EL:2385*/this._inputBuffer[this._inputPtr++];
            /*SL:2386*/if (v1 > 32) {
                /*SL:2387*/if (v1 == 47) {
                    /*SL:2388*/this._skipComment();
                }
                else {
                    /*SL:2391*/if (v1 == 35 && /*EL:2392*/this._skipYAMLComment()) {
                        /*SL:2393*/continue;
                    }
                    /*SL:2396*/return v1;
                }
            }
            else {
                /*SL:2397*/if (v1 == 32) {
                    continue;
                }
                /*SL:2398*/if (v1 == 10) {
                    /*SL:2399*/++this._currInputRow;
                    /*SL:2400*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:2401*/ if (v1 == 13) {
                    /*SL:2402*/this._skipCR();
                }
                else {
                    /*SL:2403*/if (v1 == 9) {
                        continue;
                    }
                    /*SL:2404*/this._throwInvalidSpace(v1);
                }
            }
        }
        return this._eofAsNextChar();
    }
    
    private void _skipComment() throws IOException {
        /*SL:2412*/if (!this.isEnabled(Feature.ALLOW_COMMENTS)) {
            /*SL:2413*/this._reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        /*SL:2416*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
            /*SL:2417*/this._reportInvalidEOF(" in a comment", null);
        }
        final char v1 = /*EL:2419*/this._inputBuffer[this._inputPtr++];
        /*SL:2420*/if (v1 == '/') {
            /*SL:2421*/this._skipLine();
        }
        else/*SL:2422*/ if (v1 == '*') {
            /*SL:2423*/this._skipCComment();
        }
        else {
            /*SL:2425*/this._reportUnexpectedChar(v1, "was expecting either '*' or '/' for a comment");
        }
    }
    
    private void _skipCComment() throws IOException {
        /*SL:2432*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int v1 = /*EL:2433*/this._inputBuffer[this._inputPtr++];
            /*SL:2434*/if (v1 <= 42) {
                /*SL:2435*/if (v1 == 42) {
                    /*SL:2436*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
                        /*SL:2437*/break;
                    }
                    /*SL:2439*/if (this._inputBuffer[this._inputPtr] == '/') {
                        /*SL:2440*/++this._inputPtr;
                        /*SL:2441*/return;
                    }
                    continue;
                }
                else {
                    /*SL:2445*/if (v1 >= 32) {
                        continue;
                    }
                    /*SL:2446*/if (v1 == 10) {
                        /*SL:2447*/++this._currInputRow;
                        /*SL:2448*/this._currInputRowStart = this._inputPtr;
                    }
                    else/*SL:2449*/ if (v1 == 13) {
                        /*SL:2450*/this._skipCR();
                    }
                    else {
                        /*SL:2451*/if (v1 == 9) {
                            continue;
                        }
                        /*SL:2452*/this._throwInvalidSpace(v1);
                    }
                }
            }
        }
        /*SL:2457*/this._reportInvalidEOF(" in a comment", null);
    }
    
    private boolean _skipYAMLComment() throws IOException {
        /*SL:2462*/if (!this.isEnabled(Feature.ALLOW_YAML_COMMENTS)) {
            /*SL:2463*/return false;
        }
        /*SL:2465*/this._skipLine();
        /*SL:2466*/return true;
    }
    
    private void _skipLine() throws IOException {
        /*SL:2472*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final int v1 = /*EL:2473*/this._inputBuffer[this._inputPtr++];
            /*SL:2474*/if (v1 < 32) {
                /*SL:2475*/if (v1 == 10) {
                    /*SL:2476*/++this._currInputRow;
                    /*SL:2477*/this._currInputRowStart = this._inputPtr;
                    /*SL:2478*/break;
                }
                /*SL:2479*/if (v1 == 13) {
                    /*SL:2480*/this._skipCR();
                    /*SL:2481*/break;
                }
                /*SL:2482*/if (v1 == 9) {
                    continue;
                }
                /*SL:2483*/this._throwInvalidSpace(v1);
            }
        }
    }
    
    @Override
    protected char _decodeEscaped() throws IOException {
        /*SL:2492*/if (this._inputPtr >= this._inputEnd && /*EL:2493*/!this._loadMore()) {
            /*SL:2494*/this._reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
        }
        final char a1 = /*EL:2497*/this._inputBuffer[this._inputPtr++];
        /*SL:2499*/switch (a1) {
            case 'b': {
                /*SL:2502*/return '\b';
            }
            case 't': {
                /*SL:2504*/return '\t';
            }
            case 'n': {
                /*SL:2506*/return '\n';
            }
            case 'f': {
                /*SL:2508*/return '\f';
            }
            case 'r': {
                /*SL:2510*/return '\r';
            }
            case '\"':
            case '/':
            case '\\': {
                /*SL:2516*/return a1;
            }
            case 'u': {
                int n = /*EL:2526*/0;
                /*SL:2527*/for (int v0 = 0; v0 < 4; ++v0) {
                    /*SL:2528*/if (this._inputPtr >= this._inputEnd && /*EL:2529*/!this._loadMore()) {
                        /*SL:2530*/this._reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
                    }
                    final int v = /*EL:2533*/this._inputBuffer[this._inputPtr++];
                    final int v2 = /*EL:2534*/CharTypes.charToHex(v);
                    /*SL:2535*/if (v2 < 0) {
                        /*SL:2536*/this._reportUnexpectedChar(v, "expected a hex-digit for character escape sequence");
                    }
                    /*SL:2538*/n = (n << 4 | v2);
                }
                /*SL:2540*/return (char)n;
            }
            default: {
                return this._handleUnrecognizedCharacterEscape(a1);
            }
        }
    }
    
    private final void _matchTrue() throws IOException {
        int inputPtr = /*EL:2544*/this._inputPtr;
        /*SL:2545*/if (inputPtr + 3 < this._inputEnd) {
            final char[] v0 = /*EL:2546*/this._inputBuffer;
            /*SL:2547*/if (v0[inputPtr] == 'r' && v0[++inputPtr] == 'u' && v0[++inputPtr] == 'e') {
                final char v = /*EL:2548*/v0[++inputPtr];
                /*SL:2549*/if (v < '0' || v == ']' || v == '}') {
                    /*SL:2550*/this._inputPtr = inputPtr;
                    /*SL:2551*/return;
                }
            }
        }
        /*SL:2556*/this._matchToken("true", 1);
    }
    
    private final void _matchFalse() throws IOException {
        int inputPtr = /*EL:2560*/this._inputPtr;
        /*SL:2561*/if (inputPtr + 4 < this._inputEnd) {
            final char[] v0 = /*EL:2562*/this._inputBuffer;
            /*SL:2563*/if (v0[inputPtr] == 'a' && v0[++inputPtr] == 'l' && v0[++inputPtr] == 's' && v0[++inputPtr] == 'e') {
                final char v = /*EL:2564*/v0[++inputPtr];
                /*SL:2565*/if (v < '0' || v == ']' || v == '}') {
                    /*SL:2566*/this._inputPtr = inputPtr;
                    /*SL:2567*/return;
                }
            }
        }
        /*SL:2572*/this._matchToken("false", 1);
    }
    
    private final void _matchNull() throws IOException {
        int inputPtr = /*EL:2576*/this._inputPtr;
        /*SL:2577*/if (inputPtr + 3 < this._inputEnd) {
            final char[] v0 = /*EL:2578*/this._inputBuffer;
            /*SL:2579*/if (v0[inputPtr] == 'u' && v0[++inputPtr] == 'l' && v0[++inputPtr] == 'l') {
                final char v = /*EL:2580*/v0[++inputPtr];
                /*SL:2581*/if (v < '0' || v == ']' || v == '}') {
                    /*SL:2582*/this._inputPtr = inputPtr;
                    /*SL:2583*/return;
                }
            }
        }
        /*SL:2588*/this._matchToken("null", 1);
    }
    
    protected final void _matchToken(final String a1, int a2) throws IOException {
        final int v1 = /*EL:2596*/a1.length();
        /*SL:2597*/if (this._inputPtr + v1 >= this._inputEnd) {
            /*SL:2598*/this._matchToken2(a1, a2);
            /*SL:2599*/return;
        }
        /*SL:2607*/do {
            if (this._inputBuffer[this._inputPtr] != a1.charAt(a2)) {
                this._reportInvalidToken(a1.substring(0, a2));
            }
            ++this._inputPtr;
        } while (++a2 < v1);
        final int v2 = /*EL:2608*/this._inputBuffer[this._inputPtr];
        /*SL:2609*/if (v2 >= 48 && v2 != 93 && v2 != 125) {
            /*SL:2610*/this._checkMatchEnd(a1, a2, v2);
        }
    }
    
    private final void _matchToken2(final String a1, int a2) throws IOException {
        final int v1 = /*EL:2616*/a1.length();
        /*SL:2623*/do {
            if ((this._inputPtr >= this._inputEnd && !this._loadMore()) || this._inputBuffer[this._inputPtr] != a1.charAt(a2)) {
                this._reportInvalidToken(a1.substring(0, a2));
            }
            ++this._inputPtr;
        } while (++a2 < v1);
        /*SL:2626*/if (this._inputPtr >= this._inputEnd && !this._loadMore()) {
            /*SL:2627*/return;
        }
        final int v2 = /*EL:2629*/this._inputBuffer[this._inputPtr];
        /*SL:2630*/if (v2 >= 48 && v2 != 93 && v2 != 125) {
            /*SL:2631*/this._checkMatchEnd(a1, a2, v2);
        }
    }
    
    private final void _checkMatchEnd(final String a1, final int a2, final int a3) throws IOException {
        final char v1 = /*EL:2637*/(char)a3;
        /*SL:2638*/if (Character.isJavaIdentifierPart(v1)) {
            /*SL:2639*/this._reportInvalidToken(a1.substring(0, a2));
        }
    }
    
    protected byte[] _decodeBase64(final Base64Variant v-2) throws IOException {
        final ByteArrayBuilder getByteArrayBuilder = /*EL:2656*/this._getByteArrayBuilder();
        while (true) {
            /*SL:2663*/if (this._inputPtr >= this._inputEnd) {
                /*SL:2664*/this._loadMoreGuaranteed();
            }
            char a1 = /*EL:2666*/this._inputBuffer[this._inputPtr++];
            /*SL:2667*/if (a1 > ' ') {
                int v1 = /*EL:2668*/v-2.decodeBase64Char(a1);
                /*SL:2669*/if (v1 < 0) {
                    /*SL:2670*/if (a1 == '\"') {
                        /*SL:2671*/return getByteArrayBuilder.toByteArray();
                    }
                    /*SL:2673*/v1 = this._decodeBase64Escape(v-2, a1, 0);
                    /*SL:2674*/if (v1 < 0) {
                        /*SL:2675*/continue;
                    }
                }
                int v2 = /*EL:2678*/v1;
                /*SL:2682*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:2683*/this._loadMoreGuaranteed();
                }
                /*SL:2685*/a1 = this._inputBuffer[this._inputPtr++];
                /*SL:2686*/v1 = v-2.decodeBase64Char(a1);
                /*SL:2687*/if (v1 < 0) {
                    /*SL:2688*/v1 = this._decodeBase64Escape(v-2, a1, 1);
                }
                /*SL:2690*/v2 = (v2 << 6 | v1);
                /*SL:2693*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:2694*/this._loadMoreGuaranteed();
                }
                /*SL:2696*/a1 = this._inputBuffer[this._inputPtr++];
                /*SL:2697*/v1 = v-2.decodeBase64Char(a1);
                /*SL:2700*/if (v1 < 0) {
                    /*SL:2701*/if (v1 != -2) {
                        /*SL:2703*/if (a1 == '\"' && !v-2.usesPadding()) {
                            /*SL:2704*/v2 >>= 4;
                            /*SL:2705*/getByteArrayBuilder.append(v2);
                            /*SL:2706*/return getByteArrayBuilder.toByteArray();
                        }
                        /*SL:2708*/v1 = this._decodeBase64Escape(v-2, a1, 2);
                    }
                    /*SL:2710*/if (v1 == -2) {
                        /*SL:2712*/if (this._inputPtr >= this._inputEnd) {
                            /*SL:2713*/this._loadMoreGuaranteed();
                        }
                        /*SL:2715*/a1 = this._inputBuffer[this._inputPtr++];
                        /*SL:2716*/if (!v-2.usesPaddingChar(a1)) {
                            /*SL:2717*/throw this.reportInvalidBase64Char(v-2, a1, 3, "expected padding character '" + v-2.getPaddingChar() + "'");
                        }
                        /*SL:2720*/v2 >>= 4;
                        /*SL:2721*/getByteArrayBuilder.append(v2);
                        /*SL:2722*/continue;
                    }
                }
                /*SL:2727*/v2 = (v2 << 6 | v1);
                /*SL:2729*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:2730*/this._loadMoreGuaranteed();
                }
                /*SL:2732*/a1 = this._inputBuffer[this._inputPtr++];
                /*SL:2733*/v1 = v-2.decodeBase64Char(a1);
                /*SL:2734*/if (v1 < 0) {
                    /*SL:2735*/if (v1 != -2) {
                        /*SL:2737*/if (a1 == '\"' && !v-2.usesPadding()) {
                            /*SL:2738*/v2 >>= 2;
                            /*SL:2739*/getByteArrayBuilder.appendTwoBytes(v2);
                            /*SL:2740*/return getByteArrayBuilder.toByteArray();
                        }
                        /*SL:2742*/v1 = this._decodeBase64Escape(v-2, a1, 3);
                    }
                    /*SL:2744*/if (v1 == -2) {
                        /*SL:2750*/v2 >>= 2;
                        /*SL:2751*/getByteArrayBuilder.appendTwoBytes(v2);
                        /*SL:2752*/continue;
                    }
                }
                /*SL:2757*/v2 = (v2 << 6 | v1);
                /*SL:2758*/getByteArrayBuilder.appendThreeBytes(v2);
            }
        }
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        /*SL:2771*/if (this._currToken == JsonToken.FIELD_NAME) {
            final long v1 = /*EL:2772*/this._currInputProcessed + (this._nameStartOffset - 1L);
            /*SL:2773*/return new JsonLocation(this._getSourceReference(), -1L, v1, this._nameStartRow, this._nameStartCol);
        }
        /*SL:2776*/return new JsonLocation(this._getSourceReference(), -1L, this._tokenInputTotal - 1L, this._tokenInputRow, this._tokenInputCol);
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        final int v1 = /*EL:2782*/this._inputPtr - this._currInputRowStart + 1;
        /*SL:2783*/return new JsonLocation(this._getSourceReference(), -1L, this._currInputProcessed + this._inputPtr, this._currInputRow, v1);
    }
    
    private final void _updateLocation() {
        final int v1 = /*EL:2791*/this._inputPtr;
        /*SL:2792*/this._tokenInputTotal = this._currInputProcessed + v1;
        /*SL:2793*/this._tokenInputRow = this._currInputRow;
        /*SL:2794*/this._tokenInputCol = v1 - this._currInputRowStart;
    }
    
    private final void _updateNameLocation() {
        final int v1 = /*EL:2800*/this._inputPtr;
        /*SL:2801*/this._nameStartOffset = v1;
        /*SL:2802*/this._nameStartRow = this._currInputRow;
        /*SL:2803*/this._nameStartCol = v1 - this._currInputRowStart;
    }
    
    protected void _reportInvalidToken(final String a1) throws IOException {
        /*SL:2813*/this._reportInvalidToken(a1, "'null', 'true', 'false' or NaN");
    }
    
    protected void _reportInvalidToken(final String v1, final String v2) throws IOException {
        final StringBuilder v3 = /*EL:2822*/new StringBuilder(v1);
        /*SL:2823*/while (this._inputPtr < this._inputEnd || this._loadMore()) {
            final char a1 = /*EL:2824*/this._inputBuffer[this._inputPtr];
            /*SL:2825*/if (!Character.isJavaIdentifierPart(a1)) {
                /*SL:2826*/break;
            }
            /*SL:2828*/++this._inputPtr;
            /*SL:2829*/v3.append(a1);
            /*SL:2830*/if (v3.length() >= 256) {
                /*SL:2831*/v3.append("...");
                /*SL:2832*/break;
            }
        }
        /*SL:2835*/this._reportError("Unrecognized token '%s': was expecting %s", v3, v2);
    }
    
    private void _closeScope(final int a1) throws JsonParseException {
        /*SL:2845*/if (a1 == 93) {
            /*SL:2846*/this._updateLocation();
            /*SL:2847*/if (!this._parsingContext.inArray()) {
                /*SL:2848*/this._reportMismatchedEndMarker(a1, '}');
            }
            /*SL:2850*/this._parsingContext = this._parsingContext.clearAndGetParent();
            /*SL:2851*/this._currToken = JsonToken.END_ARRAY;
        }
        /*SL:2853*/if (a1 == 125) {
            /*SL:2854*/this._updateLocation();
            /*SL:2855*/if (!this._parsingContext.inObject()) {
                /*SL:2856*/this._reportMismatchedEndMarker(a1, ']');
            }
            /*SL:2858*/this._parsingContext = this._parsingContext.clearAndGetParent();
            /*SL:2859*/this._currToken = JsonToken.END_OBJECT;
        }
    }
    
    static {
        FEAT_MASK_TRAILING_COMMA = Feature.ALLOW_TRAILING_COMMA.getMask();
        _icLatin1 = CharTypes.getInputCodeLatin1();
    }
}
