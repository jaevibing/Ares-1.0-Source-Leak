package com.fasterxml.jackson.core.json.async;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.Base64Variant;
import java.io.Writer;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.io.OutputStream;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.base.ParserBase;

public abstract class NonBlockingJsonParserBase extends ParserBase
{
    protected static final int MAJOR_INITIAL = 0;
    protected static final int MAJOR_ROOT = 1;
    protected static final int MAJOR_OBJECT_FIELD_FIRST = 2;
    protected static final int MAJOR_OBJECT_FIELD_NEXT = 3;
    protected static final int MAJOR_OBJECT_VALUE = 4;
    protected static final int MAJOR_ARRAY_ELEMENT_FIRST = 5;
    protected static final int MAJOR_ARRAY_ELEMENT_NEXT = 6;
    protected static final int MAJOR_CLOSED = 7;
    protected static final int MINOR_ROOT_BOM = 1;
    protected static final int MINOR_ROOT_NEED_SEPARATOR = 2;
    protected static final int MINOR_ROOT_GOT_SEPARATOR = 3;
    protected static final int MINOR_FIELD_LEADING_WS = 4;
    protected static final int MINOR_FIELD_LEADING_COMMA = 5;
    protected static final int MINOR_FIELD_NAME = 7;
    protected static final int MINOR_FIELD_NAME_ESCAPE = 8;
    protected static final int MINOR_FIELD_APOS_NAME = 9;
    protected static final int MINOR_FIELD_UNQUOTED_NAME = 10;
    protected static final int MINOR_VALUE_LEADING_WS = 12;
    protected static final int MINOR_VALUE_EXPECTING_COMMA = 13;
    protected static final int MINOR_VALUE_EXPECTING_COLON = 14;
    protected static final int MINOR_VALUE_WS_AFTER_COMMA = 15;
    protected static final int MINOR_VALUE_TOKEN_NULL = 16;
    protected static final int MINOR_VALUE_TOKEN_TRUE = 17;
    protected static final int MINOR_VALUE_TOKEN_FALSE = 18;
    protected static final int MINOR_VALUE_TOKEN_NON_STD = 19;
    protected static final int MINOR_NUMBER_MINUS = 23;
    protected static final int MINOR_NUMBER_ZERO = 24;
    protected static final int MINOR_NUMBER_MINUSZERO = 25;
    protected static final int MINOR_NUMBER_INTEGER_DIGITS = 26;
    protected static final int MINOR_NUMBER_FRACTION_DIGITS = 30;
    protected static final int MINOR_NUMBER_EXPONENT_MARKER = 31;
    protected static final int MINOR_NUMBER_EXPONENT_DIGITS = 32;
    protected static final int MINOR_VALUE_STRING = 40;
    protected static final int MINOR_VALUE_STRING_ESCAPE = 41;
    protected static final int MINOR_VALUE_STRING_UTF8_2 = 42;
    protected static final int MINOR_VALUE_STRING_UTF8_3 = 43;
    protected static final int MINOR_VALUE_STRING_UTF8_4 = 44;
    protected static final int MINOR_VALUE_APOS_STRING = 45;
    protected static final int MINOR_VALUE_TOKEN_ERROR = 50;
    protected static final int MINOR_COMMENT_LEADING_SLASH = 51;
    protected static final int MINOR_COMMENT_CLOSING_ASTERISK = 52;
    protected static final int MINOR_COMMENT_C = 53;
    protected static final int MINOR_COMMENT_CPP = 54;
    protected static final int MINOR_COMMENT_YAML = 55;
    protected final ByteQuadsCanonicalizer _symbols;
    protected int[] _quadBuffer;
    protected int _quadLength;
    protected int _quad1;
    protected int _pending32;
    protected int _pendingBytes;
    protected int _quoted32;
    protected int _quotedDigits;
    protected int _majorState;
    protected int _majorStateAfterValue;
    protected int _minorState;
    protected int _minorStateAfterSplit;
    protected boolean _endOfInput;
    protected static final int NON_STD_TOKEN_NAN = 0;
    protected static final int NON_STD_TOKEN_INFINITY = 1;
    protected static final int NON_STD_TOKEN_PLUS_INFINITY = 2;
    protected static final int NON_STD_TOKEN_MINUS_INFINITY = 3;
    protected static final String[] NON_STD_TOKENS;
    protected static final double[] NON_STD_TOKEN_VALUES;
    protected int _nonStdTokenType;
    protected int _currBufferStart;
    protected int _currInputRowAlt;
    
    public NonBlockingJsonParserBase(final IOContext a1, final int a2, final ByteQuadsCanonicalizer a3) {
        super(a1, a2);
        this._quadBuffer = new int[8];
        this._endOfInput = false;
        this._currBufferStart = 0;
        this._currInputRowAlt = 1;
        this._symbols = a3;
        this._currToken = null;
        this._majorState = 0;
        this._majorStateAfterValue = 1;
    }
    
    @Override
    public ObjectCodec getCodec() {
        /*SL:261*/return null;
    }
    
    @Override
    public void setCodec(final ObjectCodec a1) {
        /*SL:266*/throw new UnsupportedOperationException("Can not use ObjectMapper with non-blocking parser");
    }
    
    @Override
    public boolean canParseAsync() {
        /*SL:273*/return true;
    }
    
    protected ByteQuadsCanonicalizer symbolTableForTests() {
        /*SL:282*/return this._symbols;
    }
    
    @Override
    public abstract int releaseBuffered(final OutputStream p0) throws IOException;
    
    @Override
    protected void _releaseBuffers() throws IOException {
        /*SL:297*/super._releaseBuffers();
        /*SL:299*/this._symbols.release();
    }
    
    @Override
    public Object getInputSource() {
        /*SL:306*/return null;
    }
    
    @Override
    protected void _closeInput() throws IOException {
        /*SL:314*/this._currBufferStart = 0;
        /*SL:315*/this._inputEnd = 0;
    }
    
    @Override
    public boolean hasTextCharacters() {
        /*SL:327*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:329*/return this._textBuffer.hasTextAsCharacters();
        }
        /*SL:331*/return this._currToken == JsonToken.FIELD_NAME && /*EL:333*/this._nameCopied;
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        final int v1 = /*EL:342*/this._inputPtr - this._currInputRowStart + 1;
        final int v2 = /*EL:344*/Math.max(this._currInputRow, this._currInputRowAlt);
        /*SL:345*/return new JsonLocation(this._getSourceReference(), this._currInputProcessed + (this._inputPtr - this._currBufferStart), -1L, v2, v1);
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        /*SL:353*/return new JsonLocation(this._getSourceReference(), this._tokenInputTotal, -1L, this._tokenInputRow, this._tokenInputCol);
    }
    
    @Override
    public String getText() throws IOException {
        /*SL:372*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:373*/return this._textBuffer.contentsAsString();
        }
        /*SL:375*/return this._getText2(this._currToken);
    }
    
    protected final String _getText2(final JsonToken a1) {
        /*SL:380*/if (a1 == null) {
            /*SL:381*/return null;
        }
        /*SL:383*/switch (a1.id()) {
            case -1: {
                /*SL:385*/return null;
            }
            case 5: {
                /*SL:387*/return this._parsingContext.getCurrentName();
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
    public int getText(final Writer v-1) throws IOException {
        final JsonToken v0 = /*EL:401*/this._currToken;
        /*SL:402*/if (v0 == JsonToken.VALUE_STRING) {
            /*SL:403*/return this._textBuffer.contentsToWriter(v-1);
        }
        /*SL:405*/if (v0 == JsonToken.FIELD_NAME) {
            final String a1 = /*EL:406*/this._parsingContext.getCurrentName();
            /*SL:407*/v-1.write(a1);
            /*SL:408*/return a1.length();
        }
        /*SL:410*/if (v0 == null) {
            /*SL:421*/return 0;
        }
        if (v0.isNumeric()) {
            return this._textBuffer.contentsToWriter(v-1);
        }
        if (v0 == JsonToken.NOT_AVAILABLE) {
            this._reportError("Current token not available: can not call this method");
        }
        final char[] v = v0.asCharArray();
        v-1.write(v);
        return v.length;
    }
    
    @Override
    public String getValueAsString() throws IOException {
        /*SL:430*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:431*/return this._textBuffer.contentsAsString();
        }
        /*SL:433*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:434*/return this.getCurrentName();
        }
        /*SL:436*/return super.getValueAsString(null);
    }
    
    @Override
    public String getValueAsString(final String a1) throws IOException {
        /*SL:443*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:444*/return this._textBuffer.contentsAsString();
        }
        /*SL:446*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:447*/return this.getCurrentName();
        }
        /*SL:449*/return super.getValueAsString(a1);
    }
    
    @Override
    public char[] getTextCharacters() throws IOException {
        /*SL:455*/if (this._currToken == null) {
            /*SL:482*/return null;
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
            case 6:
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
        /*SL:488*/if (this._currToken == null) {
            /*SL:503*/return 0;
        }
        switch (this._currToken.id()) {
            case 5: {
                return this._parsingContext.getCurrentName().length();
            }
            case 6:
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
        /*SL:510*/if (this._currToken != null) {
            /*SL:511*/switch (this._currToken.id()) {
                case 5: {
                    /*SL:513*/return 0;
                }
                case 6:
                case 7:
                case 8: {
                    /*SL:518*/return this._textBuffer.getTextOffset();
                }
            }
        }
        /*SL:522*/return 0;
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant v2) throws IOException {
        /*SL:534*/if (this._currToken != JsonToken.VALUE_STRING) {
            /*SL:535*/this._reportError("Current token (%s) not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary", this._currToken);
        }
        /*SL:538*/if (this._binaryValue == null) {
            final ByteArrayBuilder a1 = /*EL:540*/this._getByteArrayBuilder();
            /*SL:541*/this._decodeBase64(this.getText(), a1, v2);
            /*SL:542*/this._binaryValue = a1.toByteArray();
        }
        /*SL:544*/return this._binaryValue;
    }
    
    @Override
    public int readBinaryValue(final Base64Variant a1, final OutputStream a2) throws IOException {
        final byte[] v1 = /*EL:550*/this.getBinaryValue(a1);
        /*SL:551*/a2.write(v1);
        /*SL:552*/return v1.length;
    }
    
    @Override
    public Object getEmbeddedObject() throws IOException {
        /*SL:558*/if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
            /*SL:559*/return this._binaryValue;
        }
        /*SL:561*/return null;
    }
    
    protected final JsonToken _startArrayScope() throws IOException {
        /*SL:572*/this._parsingContext = this._parsingContext.createChildArrayContext(-1, -1);
        /*SL:573*/this._majorState = 5;
        /*SL:574*/this._majorStateAfterValue = 6;
        /*SL:575*/return this._currToken = JsonToken.START_ARRAY;
    }
    
    protected final JsonToken _startObjectScope() throws IOException {
        /*SL:580*/this._parsingContext = this._parsingContext.createChildObjectContext(-1, -1);
        /*SL:581*/this._majorState = 2;
        /*SL:582*/this._majorStateAfterValue = 3;
        /*SL:583*/return this._currToken = JsonToken.START_OBJECT;
    }
    
    protected final JsonToken _closeArrayScope() throws IOException {
        /*SL:588*/if (!this._parsingContext.inArray()) {
            /*SL:589*/this._reportMismatchedEndMarker(93, '}');
        }
        final JsonReadContext v0 = /*EL:591*/this._parsingContext.getParent();
        /*SL:592*/this._parsingContext = v0;
        int v;
        /*SL:594*/if (v0.inObject()) {
            /*SL:595*/v = 3;
        }
        else/*SL:596*/ if (v0.inArray()) {
            /*SL:597*/v = 6;
        }
        else {
            /*SL:599*/v = 1;
        }
        /*SL:601*/this._majorState = v;
        /*SL:602*/this._majorStateAfterValue = v;
        /*SL:603*/return this._currToken = JsonToken.END_ARRAY;
    }
    
    protected final JsonToken _closeObjectScope() throws IOException {
        /*SL:608*/if (!this._parsingContext.inObject()) {
            /*SL:609*/this._reportMismatchedEndMarker(125, ']');
        }
        final JsonReadContext v0 = /*EL:611*/this._parsingContext.getParent();
        /*SL:612*/this._parsingContext = v0;
        int v;
        /*SL:614*/if (v0.inObject()) {
            /*SL:615*/v = 3;
        }
        else/*SL:616*/ if (v0.inArray()) {
            /*SL:617*/v = 6;
        }
        else {
            /*SL:619*/v = 1;
        }
        /*SL:621*/this._majorState = v;
        /*SL:622*/this._majorStateAfterValue = v;
        /*SL:623*/return this._currToken = JsonToken.END_OBJECT;
    }
    
    protected final String _findName(int a1, final int a2) throws JsonParseException {
        /*SL:634*/a1 = _padLastQuad(a1, a2);
        final String v1 = /*EL:636*/this._symbols.findName(a1);
        /*SL:637*/if (v1 != null) {
            /*SL:638*/return v1;
        }
        /*SL:641*/this._quadBuffer[0] = a1;
        /*SL:642*/return this._addName(this._quadBuffer, 1, a2);
    }
    
    protected final String _findName(final int a1, int a2, final int a3) throws JsonParseException {
        /*SL:647*/a2 = _padLastQuad(a2, a3);
        final String v1 = /*EL:649*/this._symbols.findName(a1, a2);
        /*SL:650*/if (v1 != null) {
            /*SL:651*/return v1;
        }
        /*SL:654*/this._quadBuffer[0] = a1;
        /*SL:655*/this._quadBuffer[1] = a2;
        /*SL:656*/return this._addName(this._quadBuffer, 2, a3);
    }
    
    protected final String _findName(final int a1, final int a2, int a3, final int a4) throws JsonParseException {
        /*SL:661*/a3 = _padLastQuad(a3, a4);
        final String v1 = /*EL:662*/this._symbols.findName(a1, a2, a3);
        /*SL:663*/if (v1 != null) {
            /*SL:664*/return v1;
        }
        final int[] v2 = /*EL:666*/this._quadBuffer;
        /*SL:667*/v2[0] = a1;
        /*SL:668*/v2[1] = a2;
        /*SL:669*/v2[2] = _padLastQuad(a3, a4);
        /*SL:670*/return this._addName(v2, 3, a4);
    }
    
    protected final String _addName(final int[] v-9, final int v-8, final int v-7) throws JsonParseException {
        final int n = /*EL:686*/(v-8 << 2) - 4 + v-7;
        final int n2;
        /*SL:695*/if (v-7 < 4) {
            final int a1 = /*EL:696*/v-9[v-8 - 1];
            /*SL:698*/v-9[v-8 - 1] = a1 << (4 - v-7 << 3);
        }
        else {
            /*SL:700*/n2 = 0;
        }
        char[] array = /*EL:704*/this._textBuffer.emptyAndGetCurrentSegment();
        int n3 = /*EL:705*/0;
        int i = /*EL:707*/0;
        while (i < n) {
            int a4 = /*EL:708*/v-9[i >> 2];
            int v0 = /*EL:709*/i & 0x3;
            /*SL:710*/a4 = (a4 >> (3 - v0 << 3) & 0xFF);
            /*SL:711*/++i;
            /*SL:713*/if (a4 > 127) {
                int v = 0;
                /*SL:715*/if ((a4 & 0xE0) == 0xC0) {
                    /*SL:716*/a4 &= 0x1F;
                    final int a2 = /*EL:717*/1;
                }
                else/*SL:718*/ if ((a4 & 0xF0) == 0xE0) {
                    /*SL:719*/a4 &= 0xF;
                    final int a3 = /*EL:720*/2;
                }
                else/*SL:721*/ if ((a4 & 0xF8) == 0xF0) {
                    /*SL:722*/a4 &= 0x7;
                    /*SL:723*/v = 3;
                }
                else {
                    /*SL:725*/this._reportInvalidInitial(a4);
                    /*SL:726*/a4 = (v = 1);
                }
                /*SL:728*/if (i + v > n) {
                    /*SL:729*/this._reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
                }
                int v2 = /*EL:733*/v-9[i >> 2];
                /*SL:734*/v0 = (i & 0x3);
                /*SL:735*/v2 >>= 3 - v0 << 3;
                /*SL:736*/++i;
                /*SL:738*/if ((v2 & 0xC0) != 0x80) {
                    /*SL:739*/this._reportInvalidOther(v2);
                }
                /*SL:741*/a4 = (a4 << 6 | (v2 & 0x3F));
                /*SL:742*/if (v > 1) {
                    /*SL:743*/v2 = v-9[i >> 2];
                    /*SL:744*/v0 = (i & 0x3);
                    /*SL:745*/v2 >>= 3 - v0 << 3;
                    /*SL:746*/++i;
                    /*SL:748*/if ((v2 & 0xC0) != 0x80) {
                        /*SL:749*/this._reportInvalidOther(v2);
                    }
                    /*SL:751*/a4 = (a4 << 6 | (v2 & 0x3F));
                    /*SL:752*/if (v > 2) {
                        /*SL:753*/v2 = v-9[i >> 2];
                        /*SL:754*/v0 = (i & 0x3);
                        /*SL:755*/v2 >>= 3 - v0 << 3;
                        /*SL:756*/++i;
                        /*SL:757*/if ((v2 & 0xC0) != 0x80) {
                            /*SL:758*/this._reportInvalidOther(v2 & 0xFF);
                        }
                        /*SL:760*/a4 = (a4 << 6 | (v2 & 0x3F));
                    }
                }
                /*SL:763*/if (v > 2) {
                    /*SL:764*/a4 -= 65536;
                    /*SL:765*/if (n3 >= array.length) {
                        /*SL:766*/array = this._textBuffer.expandCurrentSegment();
                    }
                    /*SL:768*/array[n3++] = (char)(55296 + (a4 >> 10));
                    /*SL:769*/a4 = (0xDC00 | (a4 & 0x3FF));
                }
            }
            /*SL:772*/if (n3 >= array.length) {
                /*SL:773*/array = this._textBuffer.expandCurrentSegment();
            }
            /*SL:775*/array[n3++] = (char)a4;
        }
        final String v3 = /*EL:779*/new String(array, 0, n3);
        /*SL:781*/if (v-7 < 4) {
            /*SL:782*/v-9[v-8 - 1] = n2;
        }
        /*SL:784*/return this._symbols.addName(v3, v-9, v-8);
    }
    
    protected static final int _padLastQuad(final int a1, final int a2) {
        /*SL:791*/return (a2 == 4) ? a1 : (a1 | -1 << (a2 << 3));
    }
    
    protected final JsonToken _eofAsNextToken() throws IOException {
        /*SL:805*/this._majorState = 7;
        /*SL:806*/if (!this._parsingContext.inRoot()) {
            /*SL:807*/this._handleEOF();
        }
        /*SL:809*/this.close();
        /*SL:810*/return this._currToken = null;
    }
    
    protected final JsonToken _fieldComplete(final String a1) throws IOException {
        /*SL:815*/this._majorState = 4;
        /*SL:816*/this._parsingContext.setCurrentName(a1);
        /*SL:817*/return this._currToken = JsonToken.FIELD_NAME;
    }
    
    protected final JsonToken _valueComplete(final JsonToken a1) throws IOException {
        /*SL:822*/this._majorState = this._majorStateAfterValue;
        /*SL:824*/return this._currToken = a1;
    }
    
    protected final JsonToken _valueCompleteInt(final int a1, final String a2) throws IOException {
        /*SL:829*/this._textBuffer.resetWithString(a2);
        /*SL:830*/this._intLength = a2.length();
        /*SL:831*/this._numTypesValid = 1;
        /*SL:832*/this._numberInt = a1;
        /*SL:833*/this._majorState = this._majorStateAfterValue;
        final JsonToken v1 = JsonToken.VALUE_NUMBER_INT;
        /*SL:836*/return this._currToken = v1;
    }
    
    protected final JsonToken _valueNonStdNumberComplete(final int a1) throws IOException {
        final String v1 = /*EL:841*/NonBlockingJsonParserBase.NON_STD_TOKENS[a1];
        /*SL:842*/this._textBuffer.resetWithString(v1);
        /*SL:843*/if (!this.isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
            /*SL:844*/this._reportError("Non-standard token '%s': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow", v1);
        }
        /*SL:847*/this._intLength = 0;
        /*SL:848*/this._numTypesValid = 8;
        /*SL:849*/this._numberDouble = NonBlockingJsonParserBase.NON_STD_TOKEN_VALUES[a1];
        /*SL:850*/this._majorState = this._majorStateAfterValue;
        /*SL:851*/return this._currToken = JsonToken.VALUE_NUMBER_FLOAT;
    }
    
    protected final String _nonStdToken(final int a1) {
        /*SL:855*/return NonBlockingJsonParserBase.NON_STD_TOKENS[a1];
    }
    
    protected final void _updateTokenLocation() {
        /*SL:866*/this._tokenInputRow = Math.max(this._currInputRow, this._currInputRowAlt);
        final int v1 = /*EL:867*/this._inputPtr;
        /*SL:868*/this._tokenInputCol = v1 - this._currInputRowStart;
        /*SL:869*/this._tokenInputTotal = this._currInputProcessed + (v1 - this._currBufferStart);
    }
    
    protected void _reportInvalidChar(final int a1) throws JsonParseException {
        /*SL:874*/if (a1 < 32) {
            /*SL:875*/this._throwInvalidSpace(a1);
        }
        /*SL:877*/this._reportInvalidInitial(a1);
    }
    
    protected void _reportInvalidInitial(final int a1) throws JsonParseException {
        /*SL:881*/this._reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(a1));
    }
    
    protected void _reportInvalidOther(final int a1, final int a2) throws JsonParseException {
        /*SL:885*/this._inputPtr = a2;
        /*SL:886*/this._reportInvalidOther(a1);
    }
    
    protected void _reportInvalidOther(final int a1) throws JsonParseException {
        /*SL:890*/this._reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(a1));
    }
    
    static {
        NON_STD_TOKENS = new String[] { "NaN", "Infinity", "+Infinity", "-Infinity" };
        NON_STD_TOKEN_VALUES = new double[] { Double.NaN, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY };
    }
}
