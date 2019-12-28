package com.fasterxml.jackson.core;

import java.util.Iterator;
import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;
import com.fasterxml.jackson.core.async.NonBlockingInputFeeder;
import com.fasterxml.jackson.core.util.RequestPayload;
import java.io.Closeable;

public abstract class JsonParser implements Closeable, Versioned
{
    private static final int MIN_BYTE_I = -128;
    private static final int MAX_BYTE_I = 255;
    private static final int MIN_SHORT_I = -32768;
    private static final int MAX_SHORT_I = 32767;
    protected int _features;
    protected transient RequestPayload _requestPayload;
    
    protected JsonParser() {
    }
    
    protected JsonParser(final int a1) {
        this._features = a1;
    }
    
    public abstract ObjectCodec getCodec();
    
    public abstract void setCodec(final ObjectCodec p0);
    
    public Object getInputSource() {
        /*SL:378*/return null;
    }
    
    public Object getCurrentValue() {
        final JsonStreamContext v1 = /*EL:394*/this.getParsingContext();
        /*SL:395*/return (v1 == null) ? null : v1.getCurrentValue();
    }
    
    public void setCurrentValue(final Object a1) {
        final JsonStreamContext v1 = /*EL:407*/this.getParsingContext();
        /*SL:408*/if (v1 != null) {
            /*SL:409*/v1.setCurrentValue(a1);
        }
    }
    
    public void setRequestPayloadOnError(final RequestPayload a1) {
        /*SL:419*/this._requestPayload = a1;
    }
    
    public void setRequestPayloadOnError(final byte[] a1, final String a2) {
        /*SL:428*/this._requestPayload = ((a1 == null) ? null : new RequestPayload(a1, a2));
    }
    
    public void setRequestPayloadOnError(final String a1) {
        /*SL:437*/this._requestPayload = ((a1 == null) ? null : new RequestPayload(a1));
    }
    
    public void setSchema(final FormatSchema a1) {
        /*SL:461*/throw new UnsupportedOperationException("Parser of type " + this.getClass().getName() + " does not support schema of type '" + a1.getSchemaType() + "'");
    }
    
    public FormatSchema getSchema() {
        /*SL:471*/return null;
    }
    
    public boolean canUseSchema(final FormatSchema a1) {
        /*SL:481*/return false;
    }
    
    public boolean requiresCustomCodec() {
        /*SL:502*/return false;
    }
    
    public boolean canParseAsync() {
        /*SL:517*/return false;
    }
    
    public NonBlockingInputFeeder getNonBlockingInputFeeder() {
        /*SL:527*/return null;
    }
    
    @Override
    public abstract Version version();
    
    @Override
    public abstract void close() throws IOException;
    
    public abstract boolean isClosed();
    
    public abstract JsonStreamContext getParsingContext();
    
    public abstract JsonLocation getTokenLocation();
    
    public abstract JsonLocation getCurrentLocation();
    
    public int releaseBuffered(final OutputStream a1) throws IOException {
        /*SL:629*/return -1;
    }
    
    public int releaseBuffered(final Writer a1) throws IOException {
        /*SL:647*/return -1;
    }
    
    public JsonParser enable(final Feature a1) {
        /*SL:660*/this._features |= a1.getMask();
        /*SL:661*/return this;
    }
    
    public JsonParser disable(final Feature a1) {
        /*SL:669*/this._features &= ~a1.getMask();
        /*SL:670*/return this;
    }
    
    public JsonParser configure(final Feature a1, final boolean a2) {
        /*SL:678*/if (a2) {
            this.enable(a1);
        }
        else {
            this.disable(a1);
        }
        /*SL:679*/return this;
    }
    
    public boolean isEnabled(final Feature a1) {
        /*SL:685*/return a1.enabledIn(this._features);
    }
    
    public int getFeatureMask() {
        /*SL:694*/return this._features;
    }
    
    @Deprecated
    public JsonParser setFeatureMask(final int a1) {
        /*SL:707*/this._features = a1;
        /*SL:708*/return this;
    }
    
    public JsonParser overrideStdFeatures(final int a1, final int a2) {
        final int v1 = /*EL:727*/(this._features & ~a2) | (a1 & a2);
        /*SL:728*/return this.setFeatureMask(v1);
    }
    
    public int getFormatFeatures() {
        /*SL:740*/return 0;
    }
    
    public JsonParser overrideFormatFeatures(final int a1, final int a2) {
        /*SL:757*/throw new IllegalArgumentException("No FormatFeatures defined for parser of type " + this.getClass().getName());
    }
    
    public abstract JsonToken nextToken() throws IOException;
    
    public abstract JsonToken nextValue() throws IOException;
    
    public boolean nextFieldName(final SerializableString a1) throws IOException {
        /*SL:814*/return this.nextToken() == JsonToken.FIELD_NAME && a1.getValue().equals(this.getCurrentName());
    }
    
    public String nextFieldName() throws IOException {
        /*SL:825*/return (this.nextToken() == JsonToken.FIELD_NAME) ? this.getCurrentName() : null;
    }
    
    public String nextTextValue() throws IOException {
        /*SL:840*/return (this.nextToken() == JsonToken.VALUE_STRING) ? this.getText() : null;
    }
    
    public int nextIntValue(final int a1) throws IOException {
        /*SL:855*/return (this.nextToken() == JsonToken.VALUE_NUMBER_INT) ? this.getIntValue() : a1;
    }
    
    public long nextLongValue(final long a1) throws IOException {
        /*SL:870*/return (this.nextToken() == JsonToken.VALUE_NUMBER_INT) ? this.getLongValue() : a1;
    }
    
    public Boolean nextBooleanValue() throws IOException {
        final JsonToken v1 = /*EL:888*/this.nextToken();
        /*SL:889*/if (v1 == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        /*SL:890*/if (v1 == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        /*SL:891*/return null;
    }
    
    public abstract JsonParser skipChildren() throws IOException;
    
    public void finishToken() throws IOException {
    }
    
    public JsonToken currentToken() {
        /*SL:948*/return this.getCurrentToken();
    }
    
    public int currentTokenId() {
        /*SL:965*/return this.getCurrentTokenId();
    }
    
    public abstract JsonToken getCurrentToken();
    
    public abstract int getCurrentTokenId();
    
    public abstract boolean hasCurrentToken();
    
    public abstract boolean hasTokenId(final int p0);
    
    public abstract boolean hasToken(final JsonToken p0);
    
    public boolean isExpectedStartArrayToken() {
        /*SL:1040*/return this.currentToken() == JsonToken.START_ARRAY;
    }
    
    public boolean isExpectedStartObjectToken() {
        /*SL:1048*/return this.currentToken() == JsonToken.START_OBJECT;
    }
    
    public boolean isNaN() throws IOException {
        /*SL:1061*/return false;
    }
    
    public abstract void clearCurrentToken();
    
    public abstract JsonToken getLastClearedToken();
    
    public abstract void overrideCurrentName(final String p0);
    
    public abstract String getCurrentName() throws IOException;
    
    public String currentName() throws IOException {
        /*SL:1124*/return this.getCurrentName();
    }
    
    public abstract String getText() throws IOException;
    
    public int getText(final Writer a1) throws IOException, UnsupportedOperationException {
        final String v1 = /*EL:1152*/this.getText();
        /*SL:1153*/if (v1 == null) {
            /*SL:1154*/return 0;
        }
        /*SL:1156*/a1.write(v1);
        /*SL:1157*/return v1.length();
    }
    
    public abstract char[] getTextCharacters() throws IOException;
    
    public abstract int getTextLength() throws IOException;
    
    public abstract int getTextOffset() throws IOException;
    
    public abstract boolean hasTextCharacters();
    
    public abstract Number getNumberValue() throws IOException;
    
    public abstract NumberType getNumberType() throws IOException;
    
    public byte getByteValue() throws IOException {
        final int v1 = /*EL:1262*/this.getIntValue();
        /*SL:1266*/if (v1 < -128 || v1 > 255) {
            /*SL:1267*/throw this._constructError("Numeric value (" + this.getText() + ") out of range of Java byte");
        }
        /*SL:1269*/return (byte)v1;
    }
    
    public short getShortValue() throws IOException {
        final int v1 = /*EL:1287*/this.getIntValue();
        /*SL:1288*/if (v1 < -32768 || v1 > 32767) {
            /*SL:1289*/throw this._constructError("Numeric value (" + this.getText() + ") out of range of Java short");
        }
        /*SL:1291*/return (short)v1;
    }
    
    public abstract int getIntValue() throws IOException;
    
    public abstract long getLongValue() throws IOException;
    
    public abstract BigInteger getBigIntegerValue() throws IOException;
    
    public abstract float getFloatValue() throws IOException;
    
    public abstract double getDoubleValue() throws IOException;
    
    public abstract BigDecimal getDecimalValue() throws IOException;
    
    public boolean getBooleanValue() throws IOException {
        final JsonToken v1 = /*EL:1390*/this.currentToken();
        /*SL:1391*/if (v1 == JsonToken.VALUE_TRUE) {
            return true;
        }
        /*SL:1392*/if (v1 == JsonToken.VALUE_FALSE) {
            return false;
        }
        /*SL:1393*/throw new JsonParseException(this, String.format("Current token (%s) not of boolean type", v1)).withRequestPayload(this._requestPayload);
    }
    
    public Object getEmbeddedObject() throws IOException {
        /*SL:1410*/return null;
    }
    
    public abstract byte[] getBinaryValue(final Base64Variant p0) throws IOException;
    
    public byte[] getBinaryValue() throws IOException {
        /*SL:1448*/return this.getBinaryValue(Base64Variants.getDefaultVariant());
    }
    
    public int readBinaryValue(final OutputStream a1) throws IOException {
        /*SL:1466*/return this.readBinaryValue(Base64Variants.getDefaultVariant(), a1);
    }
    
    public int readBinaryValue(final Base64Variant a1, final OutputStream a2) throws IOException {
        /*SL:1481*/this._reportUnsupportedOperation();
        /*SL:1482*/return 0;
    }
    
    public int getValueAsInt() throws IOException {
        /*SL:1503*/return this.getValueAsInt(0);
    }
    
    public int getValueAsInt(final int a1) throws IOException {
        /*SL:1517*/return a1;
    }
    
    public long getValueAsLong() throws IOException {
        /*SL:1531*/return this.getValueAsLong(0L);
    }
    
    public long getValueAsLong(final long a1) throws IOException {
        /*SL:1546*/return a1;
    }
    
    public double getValueAsDouble() throws IOException {
        /*SL:1561*/return this.getValueAsDouble(0.0);
    }
    
    public double getValueAsDouble(final double a1) throws IOException {
        /*SL:1576*/return a1;
    }
    
    public boolean getValueAsBoolean() throws IOException {
        /*SL:1591*/return this.getValueAsBoolean(false);
    }
    
    public boolean getValueAsBoolean(final boolean a1) throws IOException {
        /*SL:1606*/return a1;
    }
    
    public String getValueAsString() throws IOException {
        /*SL:1621*/return this.getValueAsString(null);
    }
    
    public abstract String getValueAsString(final String p0) throws IOException;
    
    public boolean canReadObjectId() {
        /*SL:1655*/return false;
    }
    
    public boolean canReadTypeId() {
        /*SL:1669*/return false;
    }
    
    public Object getObjectId() throws IOException {
        /*SL:1684*/return null;
    }
    
    public Object getTypeId() throws IOException {
        /*SL:1699*/return null;
    }
    
    public <T> T readValueAs(final Class<T> a1) throws IOException {
        /*SL:1729*/return this._codec().<T>readValue(this, a1);
    }
    
    public <T> T readValueAs(final TypeReference<?> a1) throws IOException {
        /*SL:1752*/return this._codec().<T>readValue(this, a1);
    }
    
    public <T> Iterator<T> readValuesAs(final Class<T> a1) throws IOException {
        /*SL:1760*/return this._codec().<T>readValues(this, a1);
    }
    
    public <T> Iterator<T> readValuesAs(final TypeReference<?> a1) throws IOException {
        /*SL:1768*/return this._codec().<T>readValues(this, a1);
    }
    
    public <T extends java.lang.Object> T readValueAsTree() throws IOException {
        /*SL:1782*/return (T)this._codec().readTree(this);
    }
    
    protected ObjectCodec _codec() {
        final ObjectCodec v1 = /*EL:1786*/this.getCodec();
        /*SL:1787*/if (v1 == null) {
            /*SL:1788*/throw new IllegalStateException("No ObjectCodec defined for parser, needed for deserialization");
        }
        /*SL:1790*/return v1;
    }
    
    protected JsonParseException _constructError(final String a1) {
        /*SL:1804*/return new JsonParseException(this, a1).withRequestPayload(this._requestPayload);
    }
    
    protected void _reportUnsupportedOperation() {
        /*SL:1815*/throw new UnsupportedOperationException("Operation not supported by parser of type " + this.getClass().getName());
    }
    
    public enum NumberType
    {
        INT, 
        LONG, 
        BIG_INTEGER, 
        FLOAT, 
        DOUBLE, 
        BIG_DECIMAL;
    }
    
    public enum Feature
    {
        AUTO_CLOSE_SOURCE(true), 
        ALLOW_COMMENTS(false), 
        ALLOW_YAML_COMMENTS(false), 
        ALLOW_UNQUOTED_FIELD_NAMES(false), 
        ALLOW_SINGLE_QUOTES(false), 
        ALLOW_UNQUOTED_CONTROL_CHARS(false), 
        ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER(false), 
        ALLOW_NUMERIC_LEADING_ZEROS(false), 
        ALLOW_NON_NUMERIC_NUMBERS(false), 
        ALLOW_MISSING_VALUES(false), 
        ALLOW_TRAILING_COMMA(false), 
        STRICT_DUPLICATE_DETECTION(false), 
        IGNORE_UNDEFINED(false), 
        INCLUDE_SOURCE_IN_LOCATION(true);
        
        private final boolean _defaultState;
        private final int _mask;
        
        public static int collectDefaults() {
            int n = /*EL:296*/0;
            /*SL:297*/for (final Feature v : values()) {
                /*SL:298*/if (v.enabledByDefault()) {
                    /*SL:299*/n |= v.getMask();
                }
            }
            /*SL:302*/return n;
        }
        
        private Feature(final boolean a1) {
            this._mask = 1 << this.ordinal();
            this._defaultState = a1;
        }
        
        public boolean enabledByDefault() {
            /*SL:310*/return this._defaultState;
        }
        
        public boolean enabledIn(final int a1) {
            /*SL:315*/return (a1 & this._mask) != 0x0;
        }
        
        public int getMask() {
            /*SL:317*/return this._mask;
        }
    }
}
