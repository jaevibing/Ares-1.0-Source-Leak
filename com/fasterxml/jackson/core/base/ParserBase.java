package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.JsonStreamContext;
import java.util.Arrays;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import java.io.IOException;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.JsonParser;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.core.io.IOContext;

public abstract class ParserBase extends ParserMinimalBase
{
    protected final IOContext _ioContext;
    protected boolean _closed;
    protected int _inputPtr;
    protected int _inputEnd;
    protected long _currInputProcessed;
    protected int _currInputRow;
    protected int _currInputRowStart;
    protected long _tokenInputTotal;
    protected int _tokenInputRow;
    protected int _tokenInputCol;
    protected JsonReadContext _parsingContext;
    protected JsonToken _nextToken;
    protected final TextBuffer _textBuffer;
    protected char[] _nameCopyBuffer;
    protected boolean _nameCopied;
    protected ByteArrayBuilder _byteArrayBuilder;
    protected byte[] _binaryValue;
    protected int _numTypesValid;
    protected int _numberInt;
    protected long _numberLong;
    protected double _numberDouble;
    protected BigInteger _numberBigInt;
    protected BigDecimal _numberBigDecimal;
    protected boolean _numberNegative;
    protected int _intLength;
    protected int _fractLength;
    protected int _expLength;
    
    protected ParserBase(final IOContext a1, final int a2) {
        super(a2);
        this._currInputRow = 1;
        this._tokenInputRow = 1;
        this._numTypesValid = 0;
        this._ioContext = a1;
        this._textBuffer = a1.constructTextBuffer();
        final DupDetector v1 = Feature.STRICT_DUPLICATE_DETECTION.enabledIn(a2) ? DupDetector.rootDetector(this) : null;
        this._parsingContext = JsonReadContext.createRootContext(v1);
    }
    
    @Override
    public Version version() {
        /*SL:240*/return PackageVersion.VERSION;
    }
    
    @Override
    public Object getCurrentValue() {
        /*SL:244*/return this._parsingContext.getCurrentValue();
    }
    
    @Override
    public void setCurrentValue(final Object a1) {
        /*SL:249*/this._parsingContext.setCurrentValue(a1);
    }
    
    @Override
    public JsonParser enable(final Feature a1) {
        /*SL:260*/this._features |= a1.getMask();
        /*SL:261*/if (a1 == Feature.STRICT_DUPLICATE_DETECTION && /*EL:262*/this._parsingContext.getDupDetector() == null) {
            /*SL:263*/this._parsingContext = this._parsingContext.withDupDetector(DupDetector.rootDetector(this));
        }
        /*SL:266*/return this;
    }
    
    @Override
    public JsonParser disable(final Feature a1) {
        /*SL:271*/this._features &= ~a1.getMask();
        /*SL:272*/if (a1 == Feature.STRICT_DUPLICATE_DETECTION) {
            /*SL:273*/this._parsingContext = this._parsingContext.withDupDetector(null);
        }
        /*SL:275*/return this;
    }
    
    @Deprecated
    @Override
    public JsonParser setFeatureMask(final int a1) {
        final int v1 = /*EL:281*/this._features ^ a1;
        /*SL:282*/if (v1 != 0) {
            /*SL:284*/this._checkStdFeatureChanges(this._features = a1, v1);
        }
        /*SL:286*/return this;
    }
    
    @Override
    public JsonParser overrideStdFeatures(final int a1, final int a2) {
        final int v1 = /*EL:291*/this._features;
        final int v2 = /*EL:292*/(v1 & ~a2) | (a1 & a2);
        final int v3 = /*EL:293*/v1 ^ v2;
        /*SL:294*/if (v3 != 0) {
            /*SL:296*/this._checkStdFeatureChanges(this._features = v2, v3);
        }
        /*SL:298*/return this;
    }
    
    protected void _checkStdFeatureChanges(final int a1, final int a2) {
        final int v1 = Feature.STRICT_DUPLICATE_DETECTION.getMask();
        /*SL:314*/if ((a2 & v1) != 0x0 && /*EL:315*/(a1 & v1) != 0x0) {
            /*SL:316*/if (this._parsingContext.getDupDetector() == null) {
                /*SL:317*/this._parsingContext = this._parsingContext.withDupDetector(DupDetector.rootDetector(this));
            }
            else {
                /*SL:319*/this._parsingContext = this._parsingContext.withDupDetector(null);
            }
        }
    }
    
    @Override
    public String getCurrentName() throws IOException {
        /*SL:337*/if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            final JsonReadContext v1 = /*EL:338*/this._parsingContext.getParent();
            /*SL:339*/if (v1 != null) {
                /*SL:340*/return v1.getCurrentName();
            }
        }
        /*SL:343*/return this._parsingContext.getCurrentName();
    }
    
    @Override
    public void overrideCurrentName(final String v2) {
        JsonReadContext v3 = /*EL:348*/this._parsingContext;
        /*SL:349*/if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            /*SL:350*/v3 = v3.getParent();
        }
        try {
            /*SL:356*/v3.setCurrentName(v2);
        }
        catch (IOException a1) {
            /*SL:358*/throw new IllegalStateException(a1);
        }
    }
    
    @Override
    public void close() throws IOException {
        /*SL:363*/if (!this._closed) {
            /*SL:365*/this._inputPtr = Math.max(this._inputPtr, this._inputEnd);
            /*SL:366*/this._closed = true;
            try {
                /*SL:368*/this._closeInput();
            }
            finally {
                /*SL:372*/this._releaseBuffers();
            }
        }
    }
    
    @Override
    public boolean isClosed() {
        /*SL:377*/return this._closed;
    }
    
    @Override
    public JsonReadContext getParsingContext() {
        /*SL:378*/return this._parsingContext;
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        /*SL:387*/return new JsonLocation(this._getSourceReference(), -1L, this.getTokenCharacterOffset(), this.getTokenLineNr(), this.getTokenColumnNr());
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        final int v1 = /*EL:399*/this._inputPtr - this._currInputRowStart + 1;
        /*SL:400*/return new JsonLocation(this._getSourceReference(), -1L, this._currInputProcessed + this._inputPtr, this._currInputRow, v1);
    }
    
    @Override
    public boolean hasTextCharacters() {
        /*SL:413*/return this._currToken == JsonToken.VALUE_STRING || /*EL:414*/(this._currToken == JsonToken.FIELD_NAME && this._nameCopied);
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant v2) throws IOException {
        /*SL:422*/if (this._binaryValue == null) {
            /*SL:423*/if (this._currToken != JsonToken.VALUE_STRING) {
                /*SL:424*/this._reportError("Current token (" + this._currToken + ") not VALUE_STRING, can not access as binary");
            }
            final ByteArrayBuilder a1 = /*EL:426*/this._getByteArrayBuilder();
            /*SL:427*/this._decodeBase64(this.getText(), a1, v2);
            /*SL:428*/this._binaryValue = a1.toByteArray();
        }
        /*SL:430*/return this._binaryValue;
    }
    
    public long getTokenCharacterOffset() {
        /*SL:439*/return this._tokenInputTotal;
    }
    
    public int getTokenLineNr() {
        /*SL:440*/return this._tokenInputRow;
    }
    
    public int getTokenColumnNr() {
        final int v1 = /*EL:443*/this._tokenInputCol;
        /*SL:444*/return (v1 < 0) ? v1 : (v1 + 1);
    }
    
    protected abstract void _closeInput() throws IOException;
    
    protected void _releaseBuffers() throws IOException {
        /*SL:468*/this._textBuffer.releaseBuffers();
        final char[] v1 = /*EL:469*/this._nameCopyBuffer;
        /*SL:470*/if (v1 != null) {
            /*SL:471*/this._nameCopyBuffer = null;
            /*SL:472*/this._ioContext.releaseNameCopyBuffer(v1);
        }
    }
    
    @Override
    protected void _handleEOF() throws JsonParseException {
        /*SL:483*/if (!this._parsingContext.inRoot()) {
            final String v1 = /*EL:484*/this._parsingContext.inArray() ? "Array" : "Object";
            /*SL:485*/this._reportInvalidEOF(String.format(": expected close marker for %s (start marker at %s)", v1, this._parsingContext.getStartLocation(this._getSourceReference())), null);
        }
    }
    
    protected final int _eofAsNextChar() throws JsonParseException {
        /*SL:497*/this._handleEOF();
        /*SL:498*/return -1;
    }
    
    public ByteArrayBuilder _getByteArrayBuilder() {
        /*SL:509*/if (this._byteArrayBuilder == null) {
            /*SL:510*/this._byteArrayBuilder = new ByteArrayBuilder();
        }
        else {
            /*SL:512*/this._byteArrayBuilder.reset();
        }
        /*SL:514*/return this._byteArrayBuilder;
    }
    
    protected final JsonToken reset(final boolean a1, final int a2, final int a3, final int a4) {
        /*SL:527*/if (a3 < 1 && a4 < 1) {
            /*SL:528*/return this.resetInt(a1, a2);
        }
        /*SL:530*/return this.resetFloat(a1, a2, a3, a4);
    }
    
    protected final JsonToken resetInt(final boolean a1, final int a2) {
        /*SL:535*/this._numberNegative = a1;
        /*SL:536*/this._intLength = a2;
        /*SL:537*/this._fractLength = 0;
        /*SL:538*/this._expLength = 0;
        /*SL:539*/this._numTypesValid = 0;
        /*SL:540*/return JsonToken.VALUE_NUMBER_INT;
    }
    
    protected final JsonToken resetFloat(final boolean a1, final int a2, final int a3, final int a4) {
        /*SL:545*/this._numberNegative = a1;
        /*SL:546*/this._intLength = a2;
        /*SL:547*/this._fractLength = a3;
        /*SL:548*/this._expLength = a4;
        /*SL:549*/this._numTypesValid = 0;
        /*SL:550*/return JsonToken.VALUE_NUMBER_FLOAT;
    }
    
    protected final JsonToken resetAsNaN(final String a1, final double a2) {
        /*SL:555*/this._textBuffer.resetWithString(a1);
        /*SL:556*/this._numberDouble = a2;
        /*SL:557*/this._numTypesValid = 8;
        /*SL:558*/return JsonToken.VALUE_NUMBER_FLOAT;
    }
    
    @Override
    public boolean isNaN() {
        /*SL:563*/if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT && /*EL:564*/(this._numTypesValid & 0x8) != 0x0) {
            final double v1 = /*EL:566*/this._numberDouble;
            /*SL:567*/return Double.isNaN(v1) || Double.isInfinite(v1);
        }
        /*SL:570*/return false;
    }
    
    @Override
    public Number getNumberValue() throws IOException {
        /*SL:582*/if (this._numTypesValid == 0) {
            /*SL:583*/this._parseNumericValue(0);
        }
        /*SL:586*/if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
            /*SL:587*/if ((this._numTypesValid & 0x1) != 0x0) {
                /*SL:588*/return this._numberInt;
            }
            /*SL:590*/if ((this._numTypesValid & 0x2) != 0x0) {
                /*SL:591*/return this._numberLong;
            }
            /*SL:593*/if ((this._numTypesValid & 0x4) != 0x0) {
                /*SL:594*/return this._numberBigInt;
            }
            /*SL:597*/return this._numberBigDecimal;
        }
        else {
            /*SL:603*/if ((this._numTypesValid & 0x10) != 0x0) {
                /*SL:604*/return this._numberBigDecimal;
            }
            /*SL:606*/if ((this._numTypesValid & 0x8) == 0x0) {
                /*SL:607*/this._throwInternal();
            }
            /*SL:609*/return this._numberDouble;
        }
    }
    
    @Override
    public NumberType getNumberType() throws IOException {
        /*SL:615*/if (this._numTypesValid == 0) {
            /*SL:616*/this._parseNumericValue(0);
        }
        /*SL:618*/if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
            /*SL:619*/if ((this._numTypesValid & 0x1) != 0x0) {
                /*SL:620*/return NumberType.INT;
            }
            /*SL:622*/if ((this._numTypesValid & 0x2) != 0x0) {
                /*SL:623*/return NumberType.LONG;
            }
            /*SL:625*/return NumberType.BIG_INTEGER;
        }
        else {
            /*SL:634*/if ((this._numTypesValid & 0x10) != 0x0) {
                /*SL:635*/return NumberType.BIG_DECIMAL;
            }
            /*SL:637*/return NumberType.DOUBLE;
        }
    }
    
    @Override
    public int getIntValue() throws IOException {
        /*SL:643*/if ((this._numTypesValid & 0x1) == 0x0) {
            /*SL:644*/if (this._numTypesValid == 0) {
                /*SL:645*/return this._parseIntValue();
            }
            /*SL:647*/if ((this._numTypesValid & 0x1) == 0x0) {
                /*SL:648*/this.convertNumberToInt();
            }
        }
        /*SL:651*/return this._numberInt;
    }
    
    @Override
    public long getLongValue() throws IOException {
        /*SL:657*/if ((this._numTypesValid & 0x2) == 0x0) {
            /*SL:658*/if (this._numTypesValid == 0) {
                /*SL:659*/this._parseNumericValue(2);
            }
            /*SL:661*/if ((this._numTypesValid & 0x2) == 0x0) {
                /*SL:662*/this.convertNumberToLong();
            }
        }
        /*SL:665*/return this._numberLong;
    }
    
    @Override
    public BigInteger getBigIntegerValue() throws IOException {
        /*SL:671*/if ((this._numTypesValid & 0x4) == 0x0) {
            /*SL:672*/if (this._numTypesValid == 0) {
                /*SL:673*/this._parseNumericValue(4);
            }
            /*SL:675*/if ((this._numTypesValid & 0x4) == 0x0) {
                /*SL:676*/this.convertNumberToBigInteger();
            }
        }
        /*SL:679*/return this._numberBigInt;
    }
    
    @Override
    public float getFloatValue() throws IOException {
        final double v1 = /*EL:685*/this.getDoubleValue();
        /*SL:694*/return (float)v1;
    }
    
    @Override
    public double getDoubleValue() throws IOException {
        /*SL:700*/if ((this._numTypesValid & 0x8) == 0x0) {
            /*SL:701*/if (this._numTypesValid == 0) {
                /*SL:702*/this._parseNumericValue(8);
            }
            /*SL:704*/if ((this._numTypesValid & 0x8) == 0x0) {
                /*SL:705*/this.convertNumberToDouble();
            }
        }
        /*SL:708*/return this._numberDouble;
    }
    
    @Override
    public BigDecimal getDecimalValue() throws IOException {
        /*SL:714*/if ((this._numTypesValid & 0x10) == 0x0) {
            /*SL:715*/if (this._numTypesValid == 0) {
                /*SL:716*/this._parseNumericValue(16);
            }
            /*SL:718*/if ((this._numTypesValid & 0x10) == 0x0) {
                /*SL:719*/this.convertNumberToBigDecimal();
            }
        }
        /*SL:722*/return this._numberBigDecimal;
    }
    
    protected void _parseNumericValue(final int v-1) throws IOException {
        /*SL:743*/if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
            final int v0 = /*EL:744*/this._intLength;
            /*SL:746*/if (v0 <= 9) {
                final int a1 = /*EL:747*/this._textBuffer.contentsAsInt(this._numberNegative);
                /*SL:748*/this._numberInt = a1;
                /*SL:749*/this._numTypesValid = 1;
                /*SL:750*/return;
            }
            /*SL:752*/if (v0 <= 18) {
                final long v = /*EL:753*/this._textBuffer.contentsAsLong(this._numberNegative);
                /*SL:755*/if (v0 == 10) {
                    /*SL:756*/if (this._numberNegative) {
                        /*SL:757*/if (v >= -2147483648L) {
                            /*SL:758*/this._numberInt = (int)v;
                            /*SL:759*/this._numTypesValid = 1;
                            /*SL:760*/return;
                        }
                    }
                    else/*SL:763*/ if (v <= 2147483647L) {
                        /*SL:764*/this._numberInt = (int)v;
                        /*SL:765*/this._numTypesValid = 1;
                        /*SL:766*/return;
                    }
                }
                /*SL:770*/this._numberLong = v;
                /*SL:771*/this._numTypesValid = 2;
                /*SL:772*/return;
            }
            /*SL:774*/this._parseSlowInt(v-1);
        }
        else {
            /*SL:777*/if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT) {
                /*SL:778*/this._parseSlowFloat(v-1);
                /*SL:779*/return;
            }
            /*SL:781*/this._reportError("Current token (%s) not numeric, can not use numeric value accessors", this._currToken);
        }
    }
    
    protected int _parseIntValue() throws IOException {
        /*SL:790*/if (this._currToken == JsonToken.VALUE_NUMBER_INT && /*EL:791*/this._intLength <= 9) {
            final int v1 = /*EL:792*/this._textBuffer.contentsAsInt(this._numberNegative);
            /*SL:793*/this._numberInt = v1;
            /*SL:794*/this._numTypesValid = 1;
            /*SL:795*/return v1;
        }
        /*SL:799*/this._parseNumericValue(1);
        /*SL:800*/if ((this._numTypesValid & 0x1) == 0x0) {
            /*SL:801*/this.convertNumberToInt();
        }
        /*SL:803*/return this._numberInt;
    }
    
    private void _parseSlowFloat(final int v2) throws IOException {
        try {
            /*SL:816*/if (v2 == 16) {
                /*SL:817*/this._numberBigDecimal = this._textBuffer.contentsAsDecimal();
                /*SL:818*/this._numTypesValid = 16;
            }
            else {
                /*SL:821*/this._numberDouble = this._textBuffer.contentsAsDouble();
                /*SL:822*/this._numTypesValid = 8;
            }
        }
        catch (NumberFormatException a1) {
            /*SL:826*/this._wrapError("Malformed numeric value '" + this._textBuffer.contentsAsString() + "'", a1);
        }
    }
    
    private void _parseSlowInt(final int v-2) throws IOException {
        final String contentsAsString = /*EL:832*/this._textBuffer.contentsAsString();
        try {
            final int a1 = /*EL:834*/this._intLength;
            final char[] v1 = /*EL:835*/this._textBuffer.getTextBuffer();
            int v2 = /*EL:836*/this._textBuffer.getTextOffset();
            /*SL:837*/if (this._numberNegative) {
                /*SL:838*/++v2;
            }
            /*SL:841*/if (NumberInput.inLongRange(v1, v2, a1, this._numberNegative)) {
                /*SL:843*/this._numberLong = Long.parseLong(contentsAsString);
                /*SL:844*/this._numTypesValid = 2;
            }
            else {
                /*SL:847*/this._numberBigInt = new BigInteger(contentsAsString);
                /*SL:848*/this._numTypesValid = 4;
            }
        }
        catch (NumberFormatException v3) {
            /*SL:852*/this._wrapError("Malformed numeric value '" + contentsAsString + "'", v3);
        }
    }
    
    protected void convertNumberToInt() throws IOException {
        /*SL:865*/if ((this._numTypesValid & 0x2) != 0x0) {
            final int v1 = /*EL:867*/(int)this._numberLong;
            /*SL:868*/if (v1 != this._numberLong) {
                /*SL:869*/this._reportError("Numeric value (" + this.getText() + ") out of range of int");
            }
            /*SL:871*/this._numberInt = v1;
        }
        else/*SL:872*/ if ((this._numTypesValid & 0x4) != 0x0) {
            /*SL:873*/if (ParserBase.BI_MIN_INT.compareTo(this._numberBigInt) > 0 || ParserBase.BI_MAX_INT.compareTo(this._numberBigInt) < 0) {
                /*SL:875*/this.reportOverflowInt();
            }
            /*SL:877*/this._numberInt = this._numberBigInt.intValue();
        }
        else/*SL:878*/ if ((this._numTypesValid & 0x8) != 0x0) {
            /*SL:880*/if (this._numberDouble < -2.147483648E9 || this._numberDouble > 2.147483647E9) {
                /*SL:881*/this.reportOverflowInt();
            }
            /*SL:883*/this._numberInt = (int)this._numberDouble;
        }
        else/*SL:884*/ if ((this._numTypesValid & 0x10) != 0x0) {
            /*SL:885*/if (ParserBase.BD_MIN_INT.compareTo(this._numberBigDecimal) > 0 || ParserBase.BD_MAX_INT.compareTo(this._numberBigDecimal) < 0) {
                /*SL:887*/this.reportOverflowInt();
            }
            /*SL:889*/this._numberInt = this._numberBigDecimal.intValue();
        }
        else {
            /*SL:891*/this._throwInternal();
        }
        /*SL:893*/this._numTypesValid |= 0x1;
    }
    
    protected void convertNumberToLong() throws IOException {
        /*SL:898*/if ((this._numTypesValid & 0x1) != 0x0) {
            /*SL:899*/this._numberLong = this._numberInt;
        }
        else/*SL:900*/ if ((this._numTypesValid & 0x4) != 0x0) {
            /*SL:901*/if (ParserBase.BI_MIN_LONG.compareTo(this._numberBigInt) > 0 || ParserBase.BI_MAX_LONG.compareTo(this._numberBigInt) < 0) {
                /*SL:903*/this.reportOverflowLong();
            }
            /*SL:905*/this._numberLong = this._numberBigInt.longValue();
        }
        else/*SL:906*/ if ((this._numTypesValid & 0x8) != 0x0) {
            /*SL:908*/if (this._numberDouble < -9.223372036854776E18 || this._numberDouble > 9.223372036854776E18) {
                /*SL:909*/this.reportOverflowLong();
            }
            /*SL:911*/this._numberLong = (long)this._numberDouble;
        }
        else/*SL:912*/ if ((this._numTypesValid & 0x10) != 0x0) {
            /*SL:913*/if (ParserBase.BD_MIN_LONG.compareTo(this._numberBigDecimal) > 0 || ParserBase.BD_MAX_LONG.compareTo(this._numberBigDecimal) < 0) {
                /*SL:915*/this.reportOverflowLong();
            }
            /*SL:917*/this._numberLong = this._numberBigDecimal.longValue();
        }
        else {
            /*SL:919*/this._throwInternal();
        }
        /*SL:921*/this._numTypesValid |= 0x2;
    }
    
    protected void convertNumberToBigInteger() throws IOException {
        /*SL:926*/if ((this._numTypesValid & 0x10) != 0x0) {
            /*SL:928*/this._numberBigInt = this._numberBigDecimal.toBigInteger();
        }
        else/*SL:929*/ if ((this._numTypesValid & 0x2) != 0x0) {
            /*SL:930*/this._numberBigInt = BigInteger.valueOf(this._numberLong);
        }
        else/*SL:931*/ if ((this._numTypesValid & 0x1) != 0x0) {
            /*SL:932*/this._numberBigInt = BigInteger.valueOf(this._numberInt);
        }
        else/*SL:933*/ if ((this._numTypesValid & 0x8) != 0x0) {
            /*SL:934*/this._numberBigInt = BigDecimal.valueOf(this._numberDouble).toBigInteger();
        }
        else {
            /*SL:936*/this._throwInternal();
        }
        /*SL:938*/this._numTypesValid |= 0x4;
    }
    
    protected void convertNumberToDouble() throws IOException {
        /*SL:949*/if ((this._numTypesValid & 0x10) != 0x0) {
            /*SL:950*/this._numberDouble = this._numberBigDecimal.doubleValue();
        }
        else/*SL:951*/ if ((this._numTypesValid & 0x4) != 0x0) {
            /*SL:952*/this._numberDouble = this._numberBigInt.doubleValue();
        }
        else/*SL:953*/ if ((this._numTypesValid & 0x2) != 0x0) {
            /*SL:954*/this._numberDouble = this._numberLong;
        }
        else/*SL:955*/ if ((this._numTypesValid & 0x1) != 0x0) {
            /*SL:956*/this._numberDouble = this._numberInt;
        }
        else {
            /*SL:958*/this._throwInternal();
        }
        /*SL:960*/this._numTypesValid |= 0x8;
    }
    
    protected void convertNumberToBigDecimal() throws IOException {
        /*SL:971*/if ((this._numTypesValid & 0x8) != 0x0) {
            /*SL:975*/this._numberBigDecimal = NumberInput.parseBigDecimal(this.getText());
        }
        else/*SL:976*/ if ((this._numTypesValid & 0x4) != 0x0) {
            /*SL:977*/this._numberBigDecimal = new BigDecimal(this._numberBigInt);
        }
        else/*SL:978*/ if ((this._numTypesValid & 0x2) != 0x0) {
            /*SL:979*/this._numberBigDecimal = BigDecimal.valueOf(this._numberLong);
        }
        else/*SL:980*/ if ((this._numTypesValid & 0x1) != 0x0) {
            /*SL:981*/this._numberBigDecimal = BigDecimal.valueOf(this._numberInt);
        }
        else {
            /*SL:983*/this._throwInternal();
        }
        /*SL:985*/this._numTypesValid |= 0x10;
    }
    
    protected void _reportMismatchedEndMarker(final int a1, final char a2) throws JsonParseException {
        final JsonReadContext v1 = /*EL:995*/this.getParsingContext();
        /*SL:996*/this._reportError(String.format("Unexpected close marker '%s': expected '%c' (for %s starting at %s)", (char)a1, a2, v1.typeDesc(), v1.getStartLocation(this._getSourceReference())));
    }
    
    protected char _decodeEscaped() throws IOException {
        /*SL:1013*/throw new UnsupportedOperationException();
    }
    
    protected final int _decodeBase64Escape(final Base64Variant a1, final int a2, final int a3) throws IOException {
        /*SL:1019*/if (a2 != 92) {
            /*SL:1020*/throw this.reportInvalidBase64Char(a1, a2, a3);
        }
        final int v1 = /*EL:1022*/this._decodeEscaped();
        /*SL:1024*/if (v1 <= 32 && /*EL:1025*/a3 == 0) {
            /*SL:1026*/return -1;
        }
        final int v2 = /*EL:1030*/a1.decodeBase64Char(v1);
        /*SL:1031*/if (v2 < 0) {
            /*SL:1032*/throw this.reportInvalidBase64Char(a1, v1, a3);
        }
        /*SL:1034*/return v2;
    }
    
    protected final int _decodeBase64Escape(final Base64Variant a1, final char a2, final int a3) throws IOException {
        /*SL:1039*/if (a2 != '\\') {
            /*SL:1040*/throw this.reportInvalidBase64Char(a1, a2, a3);
        }
        final char v1 = /*EL:1042*/this._decodeEscaped();
        /*SL:1044*/if (v1 <= ' ' && /*EL:1045*/a3 == 0) {
            /*SL:1046*/return -1;
        }
        final int v2 = /*EL:1050*/a1.decodeBase64Char(v1);
        /*SL:1051*/if (v2 < 0) {
            /*SL:1052*/throw this.reportInvalidBase64Char(a1, v1, a3);
        }
        /*SL:1054*/return v2;
    }
    
    protected IllegalArgumentException reportInvalidBase64Char(final Base64Variant a1, final int a2, final int a3) throws IllegalArgumentException {
        /*SL:1058*/return this.reportInvalidBase64Char(a1, a2, a3, null);
    }
    
    protected IllegalArgumentException reportInvalidBase64Char(final Base64Variant v1, final int v2, final int v3, final String v4) throws IllegalArgumentException {
        String v5 = null;
        /*SL:1067*/if (v2 <= 32) {
            final String a1 = /*EL:1068*/String.format("Illegal white space character (code 0x%s) as character #%d of 4-char base64 unit: can only used between units", Integer.toHexString(v2), v3 + 1);
        }
        else/*SL:1070*/ if (v1.usesPaddingChar(v2)) {
            final String a2 = /*EL:1071*/"Unexpected padding character ('" + v1.getPaddingChar() + "') as character #" + (v3 + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        }
        else/*SL:1072*/ if (!Character.isDefined(v2) || Character.isISOControl(v2)) {
            final String a3 = /*EL:1074*/"Illegal character (code 0x" + Integer.toHexString(v2) + ") in base64 content";
        }
        else {
            /*SL:1076*/v5 = "Illegal character '" + (char)v2 + "' (code 0x" + Integer.toHexString(v2) + ") in base64 content";
        }
        /*SL:1078*/if (v4 != null) {
            /*SL:1079*/v5 = v5 + ": " + v4;
        }
        /*SL:1081*/return new IllegalArgumentException(v5);
    }
    
    protected Object _getSourceReference() {
        /*SL:1097*/if (Feature.INCLUDE_SOURCE_IN_LOCATION.enabledIn(this._features)) {
            /*SL:1098*/return this._ioContext.getSourceReference();
        }
        /*SL:1100*/return null;
    }
    
    protected static int[] growArrayBy(final int[] a1, final int a2) {
        /*SL:1105*/if (a1 == null) {
            /*SL:1106*/return new int[a2];
        }
        /*SL:1108*/return Arrays.copyOf(a1, a1.length + a2);
    }
    
    @Deprecated
    protected void loadMoreGuaranteed() throws IOException {
        /*SL:1120*/if (!this.loadMore()) {
            this._reportInvalidEOF();
        }
    }
    
    @Deprecated
    protected boolean loadMore() throws IOException {
        /*SL:1124*/return false;
    }
    
    protected void _finishString() throws IOException {
    }
}
