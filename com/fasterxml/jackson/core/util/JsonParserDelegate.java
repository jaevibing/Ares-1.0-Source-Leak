package com.fasterxml.jackson.core.util;

import java.io.OutputStream;
import com.fasterxml.jackson.core.Base64Variant;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.Writer;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonParser;

public class JsonParserDelegate extends JsonParser
{
    protected JsonParser delegate;
    
    public JsonParserDelegate(final JsonParser a1) {
        this.delegate = a1;
    }
    
    @Override
    public Object getCurrentValue() {
        /*SL:31*/return this.delegate.getCurrentValue();
    }
    
    @Override
    public void setCurrentValue(final Object a1) {
        /*SL:36*/this.delegate.setCurrentValue(a1);
    }
    
    @Override
    public void setCodec(final ObjectCodec a1) {
        /*SL:45*/this.delegate.setCodec(a1);
    }
    
    @Override
    public ObjectCodec getCodec() {
        /*SL:46*/return this.delegate.getCodec();
    }
    
    @Override
    public JsonParser enable(final Feature a1) {
        /*SL:50*/this.delegate.enable(a1);
        /*SL:51*/return this;
    }
    
    @Override
    public JsonParser disable(final Feature a1) {
        /*SL:56*/this.delegate.disable(a1);
        /*SL:57*/return this;
    }
    
    @Override
    public boolean isEnabled(final Feature a1) {
        /*SL:60*/return this.delegate.isEnabled(a1);
    }
    
    @Override
    public int getFeatureMask() {
        /*SL:61*/return this.delegate.getFeatureMask();
    }
    
    @Deprecated
    @Override
    public JsonParser setFeatureMask(final int a1) {
        /*SL:66*/this.delegate.setFeatureMask(a1);
        /*SL:67*/return this;
    }
    
    @Override
    public JsonParser overrideStdFeatures(final int a1, final int a2) {
        /*SL:72*/this.delegate.overrideStdFeatures(a1, a2);
        /*SL:73*/return this;
    }
    
    @Override
    public JsonParser overrideFormatFeatures(final int a1, final int a2) {
        /*SL:78*/this.delegate.overrideFormatFeatures(a1, a2);
        /*SL:79*/return this;
    }
    
    @Override
    public FormatSchema getSchema() {
        /*SL:82*/return this.delegate.getSchema();
    }
    
    @Override
    public void setSchema(final FormatSchema a1) {
        /*SL:83*/this.delegate.setSchema(a1);
    }
    
    @Override
    public boolean canUseSchema(final FormatSchema a1) {
        /*SL:84*/return this.delegate.canUseSchema(a1);
    }
    
    @Override
    public Version version() {
        /*SL:85*/return this.delegate.version();
    }
    
    @Override
    public Object getInputSource() {
        /*SL:86*/return this.delegate.getInputSource();
    }
    
    @Override
    public boolean requiresCustomCodec() {
        /*SL:94*/return this.delegate.requiresCustomCodec();
    }
    
    @Override
    public void close() throws IOException {
        /*SL:102*/this.delegate.close();
    }
    
    @Override
    public boolean isClosed() {
        /*SL:103*/return this.delegate.isClosed();
    }
    
    @Override
    public JsonToken currentToken() {
        /*SL:111*/return this.delegate.currentToken();
    }
    
    @Override
    public int currentTokenId() {
        /*SL:112*/return this.delegate.currentTokenId();
    }
    
    @Override
    public JsonToken getCurrentToken() {
        /*SL:114*/return this.delegate.getCurrentToken();
    }
    
    @Override
    public int getCurrentTokenId() {
        /*SL:115*/return this.delegate.getCurrentTokenId();
    }
    
    @Override
    public boolean hasCurrentToken() {
        /*SL:116*/return this.delegate.hasCurrentToken();
    }
    
    @Override
    public boolean hasTokenId(final int a1) {
        /*SL:117*/return this.delegate.hasTokenId(a1);
    }
    
    @Override
    public boolean hasToken(final JsonToken a1) {
        /*SL:118*/return this.delegate.hasToken(a1);
    }
    
    @Override
    public String getCurrentName() throws IOException {
        /*SL:120*/return this.delegate.getCurrentName();
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        /*SL:121*/return this.delegate.getCurrentLocation();
    }
    
    @Override
    public JsonStreamContext getParsingContext() {
        /*SL:122*/return this.delegate.getParsingContext();
    }
    
    @Override
    public boolean isExpectedStartArrayToken() {
        /*SL:123*/return this.delegate.isExpectedStartArrayToken();
    }
    
    @Override
    public boolean isExpectedStartObjectToken() {
        /*SL:124*/return this.delegate.isExpectedStartObjectToken();
    }
    
    @Override
    public boolean isNaN() throws IOException {
        /*SL:125*/return this.delegate.isNaN();
    }
    
    @Override
    public void clearCurrentToken() {
        /*SL:133*/this.delegate.clearCurrentToken();
    }
    
    @Override
    public JsonToken getLastClearedToken() {
        /*SL:134*/return this.delegate.getLastClearedToken();
    }
    
    @Override
    public void overrideCurrentName(final String a1) {
        /*SL:135*/this.delegate.overrideCurrentName(a1);
    }
    
    @Override
    public String getText() throws IOException {
        /*SL:143*/return this.delegate.getText();
    }
    
    @Override
    public boolean hasTextCharacters() {
        /*SL:144*/return this.delegate.hasTextCharacters();
    }
    
    @Override
    public char[] getTextCharacters() throws IOException {
        /*SL:145*/return this.delegate.getTextCharacters();
    }
    
    @Override
    public int getTextLength() throws IOException {
        /*SL:146*/return this.delegate.getTextLength();
    }
    
    @Override
    public int getTextOffset() throws IOException {
        /*SL:147*/return this.delegate.getTextOffset();
    }
    
    @Override
    public int getText(final Writer a1) throws IOException, UnsupportedOperationException {
        /*SL:148*/return this.delegate.getText(a1);
    }
    
    @Override
    public BigInteger getBigIntegerValue() throws IOException {
        /*SL:157*/return this.delegate.getBigIntegerValue();
    }
    
    @Override
    public boolean getBooleanValue() throws IOException {
        /*SL:160*/return this.delegate.getBooleanValue();
    }
    
    @Override
    public byte getByteValue() throws IOException {
        /*SL:163*/return this.delegate.getByteValue();
    }
    
    @Override
    public short getShortValue() throws IOException {
        /*SL:166*/return this.delegate.getShortValue();
    }
    
    @Override
    public BigDecimal getDecimalValue() throws IOException {
        /*SL:169*/return this.delegate.getDecimalValue();
    }
    
    @Override
    public double getDoubleValue() throws IOException {
        /*SL:172*/return this.delegate.getDoubleValue();
    }
    
    @Override
    public float getFloatValue() throws IOException {
        /*SL:175*/return this.delegate.getFloatValue();
    }
    
    @Override
    public int getIntValue() throws IOException {
        /*SL:178*/return this.delegate.getIntValue();
    }
    
    @Override
    public long getLongValue() throws IOException {
        /*SL:181*/return this.delegate.getLongValue();
    }
    
    @Override
    public NumberType getNumberType() throws IOException {
        /*SL:184*/return this.delegate.getNumberType();
    }
    
    @Override
    public Number getNumberValue() throws IOException {
        /*SL:187*/return this.delegate.getNumberValue();
    }
    
    @Override
    public int getValueAsInt() throws IOException {
        /*SL:195*/return this.delegate.getValueAsInt();
    }
    
    @Override
    public int getValueAsInt(final int a1) throws IOException {
        /*SL:196*/return this.delegate.getValueAsInt(a1);
    }
    
    @Override
    public long getValueAsLong() throws IOException {
        /*SL:197*/return this.delegate.getValueAsLong();
    }
    
    @Override
    public long getValueAsLong(final long a1) throws IOException {
        /*SL:198*/return this.delegate.getValueAsLong(a1);
    }
    
    @Override
    public double getValueAsDouble() throws IOException {
        /*SL:199*/return this.delegate.getValueAsDouble();
    }
    
    @Override
    public double getValueAsDouble(final double a1) throws IOException {
        /*SL:200*/return this.delegate.getValueAsDouble(a1);
    }
    
    @Override
    public boolean getValueAsBoolean() throws IOException {
        /*SL:201*/return this.delegate.getValueAsBoolean();
    }
    
    @Override
    public boolean getValueAsBoolean(final boolean a1) throws IOException {
        /*SL:202*/return this.delegate.getValueAsBoolean(a1);
    }
    
    @Override
    public String getValueAsString() throws IOException {
        /*SL:203*/return this.delegate.getValueAsString();
    }
    
    @Override
    public String getValueAsString(final String a1) throws IOException {
        /*SL:204*/return this.delegate.getValueAsString(a1);
    }
    
    @Override
    public Object getEmbeddedObject() throws IOException {
        /*SL:212*/return this.delegate.getEmbeddedObject();
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant a1) throws IOException {
        /*SL:213*/return this.delegate.getBinaryValue(a1);
    }
    
    @Override
    public int readBinaryValue(final Base64Variant a1, final OutputStream a2) throws IOException {
        /*SL:214*/return this.delegate.readBinaryValue(a1, a2);
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        /*SL:215*/return this.delegate.getTokenLocation();
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        /*SL:217*/return this.delegate.nextToken();
    }
    
    @Override
    public JsonToken nextValue() throws IOException {
        /*SL:219*/return this.delegate.nextValue();
    }
    
    @Override
    public void finishToken() throws IOException {
        /*SL:221*/this.delegate.finishToken();
    }
    
    @Override
    public JsonParser skipChildren() throws IOException {
        /*SL:225*/this.delegate.skipChildren();
        /*SL:227*/return this;
    }
    
    @Override
    public boolean canReadObjectId() {
        /*SL:236*/return this.delegate.canReadObjectId();
    }
    
    @Override
    public boolean canReadTypeId() {
        /*SL:237*/return this.delegate.canReadTypeId();
    }
    
    @Override
    public Object getObjectId() throws IOException {
        /*SL:238*/return this.delegate.getObjectId();
    }
    
    @Override
    public Object getTypeId() throws IOException {
        /*SL:239*/return this.delegate.getTypeId();
    }
}
