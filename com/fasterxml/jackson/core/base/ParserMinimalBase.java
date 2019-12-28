package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonParseException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonParser;

public abstract class ParserMinimalBase extends JsonParser
{
    protected static final int INT_TAB = 9;
    protected static final int INT_LF = 10;
    protected static final int INT_CR = 13;
    protected static final int INT_SPACE = 32;
    protected static final int INT_LBRACKET = 91;
    protected static final int INT_RBRACKET = 93;
    protected static final int INT_LCURLY = 123;
    protected static final int INT_RCURLY = 125;
    protected static final int INT_QUOTE = 34;
    protected static final int INT_APOS = 39;
    protected static final int INT_BACKSLASH = 92;
    protected static final int INT_SLASH = 47;
    protected static final int INT_ASTERISK = 42;
    protected static final int INT_COLON = 58;
    protected static final int INT_COMMA = 44;
    protected static final int INT_HASH = 35;
    protected static final int INT_0 = 48;
    protected static final int INT_9 = 57;
    protected static final int INT_MINUS = 45;
    protected static final int INT_PLUS = 43;
    protected static final int INT_PERIOD = 46;
    protected static final int INT_e = 101;
    protected static final int INT_E = 69;
    protected static final char CHAR_NULL = '\0';
    protected static final byte[] NO_BYTES;
    protected static final int[] NO_INTS;
    protected static final int NR_UNKNOWN = 0;
    protected static final int NR_INT = 1;
    protected static final int NR_LONG = 2;
    protected static final int NR_BIGINT = 4;
    protected static final int NR_DOUBLE = 8;
    protected static final int NR_BIGDECIMAL = 16;
    protected static final int NR_FLOAT = 32;
    protected static final BigInteger BI_MIN_INT;
    protected static final BigInteger BI_MAX_INT;
    protected static final BigInteger BI_MIN_LONG;
    protected static final BigInteger BI_MAX_LONG;
    protected static final BigDecimal BD_MIN_LONG;
    protected static final BigDecimal BD_MAX_LONG;
    protected static final BigDecimal BD_MIN_INT;
    protected static final BigDecimal BD_MAX_INT;
    protected static final long MIN_INT_L = -2147483648L;
    protected static final long MAX_INT_L = 2147483647L;
    protected static final double MIN_LONG_D = -9.223372036854776E18;
    protected static final double MAX_LONG_D = 9.223372036854776E18;
    protected static final double MIN_INT_D = -2.147483648E9;
    protected static final double MAX_INT_D = 2.147483647E9;
    protected static final int MAX_ERROR_TOKEN_LENGTH = 256;
    protected JsonToken _currToken;
    protected JsonToken _lastClearedToken;
    
    protected ParserMinimalBase() {
    }
    
    protected ParserMinimalBase(final int a1) {
        super(a1);
    }
    
    @Override
    public abstract JsonToken nextToken() throws IOException;
    
    @Override
    public JsonToken currentToken() {
        /*SL:186*/return this._currToken;
    }
    
    @Override
    public int currentTokenId() {
        final JsonToken v1 = /*EL:188*/this._currToken;
        /*SL:189*/return (v1 == null) ? 0 : v1.id();
    }
    
    @Override
    public JsonToken getCurrentToken() {
        /*SL:192*/return this._currToken;
    }
    
    @Override
    public int getCurrentTokenId() {
        final JsonToken v1 = /*EL:194*/this._currToken;
        /*SL:195*/return (v1 == null) ? 0 : v1.id();
    }
    
    @Override
    public boolean hasCurrentToken() {
        /*SL:198*/return this._currToken != null;
    }
    
    @Override
    public boolean hasTokenId(final int a1) {
        final JsonToken v1 = /*EL:200*/this._currToken;
        /*SL:201*/if (v1 == null) {
            /*SL:202*/return 0 == a1;
        }
        /*SL:204*/return v1.id() == a1;
    }
    
    @Override
    public boolean hasToken(final JsonToken a1) {
        /*SL:208*/return this._currToken == a1;
    }
    
    @Override
    public boolean isExpectedStartArrayToken() {
        /*SL:211*/return this._currToken == JsonToken.START_ARRAY;
    }
    
    @Override
    public boolean isExpectedStartObjectToken() {
        /*SL:212*/return this._currToken == JsonToken.START_OBJECT;
    }
    
    @Override
    public JsonToken nextValue() throws IOException {
        JsonToken v1 = /*EL:218*/this.nextToken();
        /*SL:219*/if (v1 == JsonToken.FIELD_NAME) {
            /*SL:220*/v1 = this.nextToken();
        }
        /*SL:222*/return v1;
    }
    
    @Override
    public JsonParser skipChildren() throws IOException {
        /*SL:228*/if (this._currToken != JsonToken.START_OBJECT && this._currToken != JsonToken.START_ARRAY) {
            /*SL:230*/return this;
        }
        int v0 = /*EL:232*/1;
        while (true) {
            final JsonToken v = /*EL:237*/this.nextToken();
            /*SL:238*/if (v == null) {
                /*SL:239*/this._handleEOF();
                /*SL:244*/return this;
            }
            /*SL:246*/if (v.isStructStart()) {
                /*SL:247*/++v0;
            }
            else/*SL:248*/ if (v.isStructEnd()) {
                /*SL:249*/if (--v0 == 0) {
                    /*SL:250*/return this;
                }
                continue;
            }
            else {
                /*SL:253*/if (v != JsonToken.NOT_AVAILABLE) {
                    continue;
                }
                /*SL:256*/this._reportError("Not enough content available for `skipChildren()`: non-blocking parser? (%s)", this.getClass().getName());
            }
        }
    }
    
    protected abstract void _handleEOF() throws JsonParseException;
    
    @Override
    public abstract String getCurrentName() throws IOException;
    
    @Override
    public abstract void close() throws IOException;
    
    @Override
    public abstract boolean isClosed();
    
    @Override
    public abstract JsonStreamContext getParsingContext();
    
    @Override
    public void clearCurrentToken() {
        /*SL:287*/if (this._currToken != null) {
            /*SL:288*/this._lastClearedToken = this._currToken;
            /*SL:289*/this._currToken = null;
        }
    }
    
    @Override
    public JsonToken getLastClearedToken() {
        /*SL:293*/return this._lastClearedToken;
    }
    
    @Override
    public abstract void overrideCurrentName(final String p0);
    
    @Override
    public abstract String getText() throws IOException;
    
    @Override
    public abstract char[] getTextCharacters() throws IOException;
    
    @Override
    public abstract boolean hasTextCharacters();
    
    @Override
    public abstract int getTextLength() throws IOException;
    
    @Override
    public abstract int getTextOffset() throws IOException;
    
    @Override
    public abstract byte[] getBinaryValue(final Base64Variant p0) throws IOException;
    
    @Override
    public boolean getValueAsBoolean(final boolean v-2) throws IOException {
        final JsonToken currToken = /*EL:326*/this._currToken;
        /*SL:327*/if (currToken != null) {
            /*SL:328*/switch (currToken.id()) {
                case 6: {
                    final String a1 = /*EL:330*/this.getText().trim();
                    /*SL:331*/if ("true".equals(a1)) {
                        /*SL:332*/return true;
                    }
                    /*SL:334*/if ("false".equals(a1)) {
                        /*SL:335*/return false;
                    }
                    /*SL:337*/if (this._hasTextualNull(a1)) {
                        /*SL:338*/return false;
                    }
                    break;
                }
                case 7: {
                    /*SL:342*/return this.getIntValue() != 0;
                }
                case 9: {
                    /*SL:344*/return true;
                }
                case 10:
                case 11: {
                    /*SL:347*/return false;
                }
                case 12: {
                    final Object v1 = /*EL:349*/this.getEmbeddedObject();
                    /*SL:350*/if (v1 instanceof Boolean) {
                        /*SL:351*/return (boolean)v1;
                    }
                    break;
                }
            }
        }
        /*SL:357*/return v-2;
    }
    
    @Override
    public int getValueAsInt() throws IOException {
        final JsonToken v1 = /*EL:363*/this._currToken;
        /*SL:364*/if (v1 == JsonToken.VALUE_NUMBER_INT || v1 == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:365*/return this.getIntValue();
        }
        /*SL:367*/return this.getValueAsInt(0);
    }
    
    @Override
    public int getValueAsInt(final int v-2) throws IOException {
        final JsonToken currToken = /*EL:373*/this._currToken;
        /*SL:374*/if (currToken == JsonToken.VALUE_NUMBER_INT || currToken == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:375*/return this.getIntValue();
        }
        /*SL:377*/if (currToken != null) {
            /*SL:378*/switch (currToken.id()) {
                case 6: {
                    final String a1 = /*EL:380*/this.getText();
                    /*SL:381*/if (this._hasTextualNull(a1)) {
                        /*SL:382*/return 0;
                    }
                    /*SL:384*/return NumberInput.parseAsInt(a1, v-2);
                }
                case 9: {
                    /*SL:386*/return 1;
                }
                case 10: {
                    /*SL:388*/return 0;
                }
                case 11: {
                    /*SL:390*/return 0;
                }
                case 12: {
                    final Object v1 = /*EL:392*/this.getEmbeddedObject();
                    /*SL:393*/if (v1 instanceof Number) {
                        /*SL:394*/return ((Number)v1).intValue();
                    }
                    break;
                }
            }
        }
        /*SL:398*/return v-2;
    }
    
    @Override
    public long getValueAsLong() throws IOException {
        final JsonToken v1 = /*EL:404*/this._currToken;
        /*SL:405*/if (v1 == JsonToken.VALUE_NUMBER_INT || v1 == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:406*/return this.getLongValue();
        }
        /*SL:408*/return this.getValueAsLong(0L);
    }
    
    @Override
    public long getValueAsLong(final long v-3) throws IOException {
        final JsonToken currToken = /*EL:414*/this._currToken;
        /*SL:415*/if (currToken == JsonToken.VALUE_NUMBER_INT || currToken == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:416*/return this.getLongValue();
        }
        /*SL:418*/if (currToken != null) {
            /*SL:419*/switch (currToken.id()) {
                case 6: {
                    final String a1 = /*EL:421*/this.getText();
                    /*SL:422*/if (this._hasTextualNull(a1)) {
                        /*SL:423*/return 0L;
                    }
                    /*SL:425*/return NumberInput.parseAsLong(a1, v-3);
                }
                case 9: {
                    /*SL:427*/return 1L;
                }
                case 10:
                case 11: {
                    /*SL:430*/return 0L;
                }
                case 12: {
                    final Object v1 = /*EL:432*/this.getEmbeddedObject();
                    /*SL:433*/if (v1 instanceof Number) {
                        /*SL:434*/return ((Number)v1).longValue();
                    }
                    break;
                }
            }
        }
        /*SL:438*/return v-3;
    }
    
    @Override
    public double getValueAsDouble(final double v-3) throws IOException {
        final JsonToken currToken = /*EL:444*/this._currToken;
        /*SL:445*/if (currToken != null) {
            /*SL:446*/switch (currToken.id()) {
                case 6: {
                    final String a1 = /*EL:448*/this.getText();
                    /*SL:449*/if (this._hasTextualNull(a1)) {
                        /*SL:450*/return 0.0;
                    }
                    /*SL:452*/return NumberInput.parseAsDouble(a1, v-3);
                }
                case 7:
                case 8: {
                    /*SL:455*/return this.getDoubleValue();
                }
                case 9: {
                    /*SL:457*/return 1.0;
                }
                case 10:
                case 11: {
                    /*SL:460*/return 0.0;
                }
                case 12: {
                    final Object v1 = /*EL:462*/this.getEmbeddedObject();
                    /*SL:463*/if (v1 instanceof Number) {
                        /*SL:464*/return ((Number)v1).doubleValue();
                    }
                    break;
                }
            }
        }
        /*SL:468*/return v-3;
    }
    
    @Override
    public String getValueAsString() throws IOException {
        /*SL:473*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:474*/return this.getText();
        }
        /*SL:476*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:477*/return this.getCurrentName();
        }
        /*SL:479*/return this.getValueAsString(null);
    }
    
    @Override
    public String getValueAsString(final String a1) throws IOException {
        /*SL:484*/if (this._currToken == JsonToken.VALUE_STRING) {
            /*SL:485*/return this.getText();
        }
        /*SL:487*/if (this._currToken == JsonToken.FIELD_NAME) {
            /*SL:488*/return this.getCurrentName();
        }
        /*SL:490*/if (this._currToken == null || this._currToken == JsonToken.VALUE_NULL || !this._currToken.isScalarValue()) {
            /*SL:491*/return a1;
        }
        /*SL:493*/return this.getText();
    }
    
    protected void _decodeBase64(final String a3, final ByteArrayBuilder v1, final Base64Variant v2) throws IOException {
        try {
            /*SL:509*/v2.decode(a3, v1);
        }
        catch (IllegalArgumentException a4) {
            /*SL:511*/this._reportError(a4.getMessage());
        }
    }
    
    protected boolean _hasTextualNull(final String a1) {
        /*SL:528*/return "null".equals(a1);
    }
    
    protected void reportUnexpectedNumberChar(final int a1, final String a2) throws JsonParseException {
        String v1 = /*EL:537*/String.format("Unexpected character (%s) in numeric value", _getCharDesc(a1));
        /*SL:538*/if (a2 != null) {
            /*SL:539*/v1 = v1 + ": " + a2;
        }
        /*SL:541*/this._reportError(v1);
    }
    
    protected void reportInvalidNumber(final String a1) throws JsonParseException {
        /*SL:545*/this._reportError("Invalid numeric value: " + a1);
    }
    
    protected void reportOverflowInt() throws IOException {
        /*SL:549*/this._reportError(String.format("Numeric value (%s) out of range of int (%d - %s)", this.getText(), Integer.MIN_VALUE, Integer.MAX_VALUE));
    }
    
    protected void reportOverflowLong() throws IOException {
        /*SL:554*/this._reportError(String.format("Numeric value (%s) out of range of long (%d - %s)", this.getText(), Long.MIN_VALUE, Long.MAX_VALUE));
    }
    
    protected void _reportUnexpectedChar(final int a1, final String a2) throws JsonParseException {
        /*SL:560*/if (a1 < 0) {
            /*SL:561*/this._reportInvalidEOF();
        }
        String v1 = /*EL:563*/String.format("Unexpected character (%s)", _getCharDesc(a1));
        /*SL:564*/if (a2 != null) {
            /*SL:565*/v1 = v1 + ": " + a2;
        }
        /*SL:567*/this._reportError(v1);
    }
    
    protected void _reportInvalidEOF() throws JsonParseException {
        /*SL:571*/this._reportInvalidEOF(" in " + this._currToken, this._currToken);
    }
    
    protected void _reportInvalidEOFInValue(final JsonToken v0) throws JsonParseException {
        String v = null;
        /*SL:579*/if (v0 == JsonToken.VALUE_STRING) {
            final String a1 = /*EL:580*/" in a String value";
        }
        else/*SL:581*/ if (v0 == JsonToken.VALUE_NUMBER_INT || v0 == JsonToken.VALUE_NUMBER_FLOAT) {
            /*SL:583*/v = " in a Number value";
        }
        else {
            /*SL:585*/v = " in a value";
        }
        /*SL:587*/this._reportInvalidEOF(v, v0);
    }
    
    protected void _reportInvalidEOF(final String a1, final JsonToken a2) throws JsonParseException {
        /*SL:594*/throw new JsonEOFException(this, a2, "Unexpected end-of-input" + a1);
    }
    
    @Deprecated
    protected void _reportInvalidEOFInValue() throws JsonParseException {
        /*SL:602*/this._reportInvalidEOF(" in a value");
    }
    
    @Deprecated
    protected void _reportInvalidEOF(final String a1) throws JsonParseException {
        /*SL:610*/throw new JsonEOFException(this, null, "Unexpected end-of-input" + a1);
    }
    
    protected void _reportMissingRootWS(final int a1) throws JsonParseException {
        /*SL:614*/this._reportUnexpectedChar(a1, "Expected space separating root-level values");
    }
    
    protected void _throwInvalidSpace(final int a1) throws JsonParseException {
        final char v1 = /*EL:618*/(char)a1;
        final String v2 = /*EL:619*/"Illegal character (" + _getCharDesc(v1) + "): only regular white space (\\r, \\n, \\t) is allowed between tokens";
        /*SL:620*/this._reportError(v2);
    }
    
    protected void _throwUnquotedSpace(final int v2, final String v3) throws JsonParseException {
        /*SL:630*/if (!this.isEnabled(Feature.ALLOW_UNQUOTED_CONTROL_CHARS) || v2 > 32) {
            final char a1 = /*EL:631*/(char)v2;
            final String a2 = /*EL:632*/"Illegal unquoted character (" + _getCharDesc(a1) + "): has to be escaped using backslash to be included in " + v3;
            /*SL:633*/this._reportError(a2);
        }
    }
    
    protected char _handleUnrecognizedCharacterEscape(final char a1) throws JsonProcessingException {
        /*SL:639*/if (this.isEnabled(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)) {
            /*SL:640*/return a1;
        }
        /*SL:643*/if (a1 == '\'' && this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            /*SL:644*/return a1;
        }
        /*SL:646*/this._reportError("Unrecognized character escape " + _getCharDesc(a1));
        /*SL:647*/return a1;
    }
    
    protected static final String _getCharDesc(final int a1) {
        final char v1 = /*EL:658*/(char)a1;
        /*SL:659*/if (Character.isISOControl(v1)) {
            /*SL:660*/return "(CTRL-CHAR, code " + a1 + ")";
        }
        /*SL:662*/if (a1 > 255) {
            /*SL:663*/return "'" + v1 + "' (code " + a1 + " / 0x" + Integer.toHexString(a1) + ")";
        }
        /*SL:665*/return "'" + v1 + "' (code " + a1 + ")";
    }
    
    protected final void _reportError(final String a1) throws JsonParseException {
        /*SL:669*/throw this._constructError(a1);
    }
    
    protected final void _reportError(final String a1, final Object a2) throws JsonParseException {
        /*SL:674*/throw this._constructError(String.format(a1, a2));
    }
    
    protected final void _reportError(final String a1, final Object a2, final Object a3) throws JsonParseException {
        /*SL:679*/throw this._constructError(String.format(a1, a2, a3));
    }
    
    protected final void _wrapError(final String a1, final Throwable a2) throws JsonParseException {
        /*SL:683*/throw this._constructError(a1, a2);
    }
    
    protected final void _throwInternal() {
        /*SL:687*/VersionUtil.throwInternal();
    }
    
    protected final JsonParseException _constructError(final String a1, final Throwable a2) {
        /*SL:691*/return new JsonParseException(this, a1, a2);
    }
    
    protected static byte[] _asciiBytes(final String v-2) {
        final byte[] array = /*EL:695*/new byte[v-2.length()];
        /*SL:696*/for (int a1 = 0, v1 = v-2.length(); a1 < v1; ++a1) {
            /*SL:697*/array[a1] = (byte)v-2.charAt(a1);
        }
        /*SL:699*/return array;
    }
    
    protected static String _ascii(final byte[] v1) {
        try {
            /*SL:704*/return new String(v1, "US-ASCII");
        }
        catch (IOException a1) {
            /*SL:706*/throw new RuntimeException(a1);
        }
    }
    
    static {
        NO_BYTES = new byte[0];
        NO_INTS = new int[0];
        BI_MIN_INT = BigInteger.valueOf(-2147483648L);
        BI_MAX_INT = BigInteger.valueOf(2147483647L);
        BI_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
        BI_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
        BD_MIN_LONG = new BigDecimal(ParserMinimalBase.BI_MIN_LONG);
        BD_MAX_LONG = new BigDecimal(ParserMinimalBase.BI_MAX_LONG);
        BD_MIN_INT = new BigDecimal(ParserMinimalBase.BI_MIN_INT);
        BD_MAX_INT = new BigDecimal(ParserMinimalBase.BI_MAX_INT);
    }
}
