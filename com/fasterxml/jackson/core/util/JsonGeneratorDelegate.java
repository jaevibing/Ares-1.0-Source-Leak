package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.InputStream;
import com.fasterxml.jackson.core.Base64Variant;
import java.io.Reader;
import java.io.IOException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonGenerator;

public class JsonGeneratorDelegate extends JsonGenerator
{
    protected JsonGenerator delegate;
    protected boolean delegateCopyMethods;
    
    public JsonGeneratorDelegate(final JsonGenerator a1) {
        this(a1, true);
    }
    
    public JsonGeneratorDelegate(final JsonGenerator a1, final boolean a2) {
        this.delegate = a1;
        this.delegateCopyMethods = a2;
    }
    
    @Override
    public Object getCurrentValue() {
        /*SL:48*/return this.delegate.getCurrentValue();
    }
    
    @Override
    public void setCurrentValue(final Object a1) {
        /*SL:53*/this.delegate.setCurrentValue(a1);
    }
    
    public JsonGenerator getDelegate() {
        /*SL:62*/return this.delegate;
    }
    
    @Override
    public ObjectCodec getCodec() {
        /*SL:70*/return this.delegate.getCodec();
    }
    
    @Override
    public JsonGenerator setCodec(final ObjectCodec a1) {
        /*SL:73*/this.delegate.setCodec(a1);
        /*SL:74*/return this;
    }
    
    @Override
    public void setSchema(final FormatSchema a1) {
        /*SL:77*/this.delegate.setSchema(a1);
    }
    
    @Override
    public FormatSchema getSchema() {
        /*SL:78*/return this.delegate.getSchema();
    }
    
    @Override
    public Version version() {
        /*SL:79*/return this.delegate.version();
    }
    
    @Override
    public Object getOutputTarget() {
        /*SL:80*/return this.delegate.getOutputTarget();
    }
    
    @Override
    public int getOutputBuffered() {
        /*SL:81*/return this.delegate.getOutputBuffered();
    }
    
    @Override
    public boolean canUseSchema(final FormatSchema a1) {
        /*SL:90*/return this.delegate.canUseSchema(a1);
    }
    
    @Override
    public boolean canWriteTypeId() {
        /*SL:93*/return this.delegate.canWriteTypeId();
    }
    
    @Override
    public boolean canWriteObjectId() {
        /*SL:96*/return this.delegate.canWriteObjectId();
    }
    
    @Override
    public boolean canWriteBinaryNatively() {
        /*SL:99*/return this.delegate.canWriteBinaryNatively();
    }
    
    @Override
    public boolean canOmitFields() {
        /*SL:102*/return this.delegate.canOmitFields();
    }
    
    @Override
    public JsonGenerator enable(final Feature a1) {
        /*SL:112*/this.delegate.enable(a1);
        /*SL:113*/return this;
    }
    
    @Override
    public JsonGenerator disable(final Feature a1) {
        /*SL:118*/this.delegate.disable(a1);
        /*SL:119*/return this;
    }
    
    @Override
    public boolean isEnabled(final Feature a1) {
        /*SL:123*/return this.delegate.isEnabled(a1);
    }
    
    @Override
    public int getFeatureMask() {
        /*SL:129*/return this.delegate.getFeatureMask();
    }
    
    @Deprecated
    @Override
    public JsonGenerator setFeatureMask(final int a1) {
        /*SL:134*/this.delegate.setFeatureMask(a1);
        /*SL:135*/return this;
    }
    
    @Override
    public JsonGenerator overrideStdFeatures(final int a1, final int a2) {
        /*SL:140*/this.delegate.overrideStdFeatures(a1, a2);
        /*SL:141*/return this;
    }
    
    @Override
    public JsonGenerator overrideFormatFeatures(final int a1, final int a2) {
        /*SL:146*/this.delegate.overrideFormatFeatures(a1, a2);
        /*SL:147*/return this;
    }
    
    @Override
    public JsonGenerator setPrettyPrinter(final PrettyPrinter a1) {
        /*SL:158*/this.delegate.setPrettyPrinter(a1);
        /*SL:159*/return this;
    }
    
    @Override
    public PrettyPrinter getPrettyPrinter() {
        /*SL:163*/return this.delegate.getPrettyPrinter();
    }
    
    @Override
    public JsonGenerator useDefaultPrettyPrinter() {
        /*SL:166*/this.delegate.useDefaultPrettyPrinter();
        /*SL:167*/return this;
    }
    
    @Override
    public JsonGenerator setHighestNonEscapedChar(final int a1) {
        /*SL:170*/this.delegate.setHighestNonEscapedChar(a1);
        /*SL:171*/return this;
    }
    
    @Override
    public int getHighestEscapedChar() {
        /*SL:174*/return this.delegate.getHighestEscapedChar();
    }
    
    @Override
    public CharacterEscapes getCharacterEscapes() {
        /*SL:177*/return this.delegate.getCharacterEscapes();
    }
    
    @Override
    public JsonGenerator setCharacterEscapes(final CharacterEscapes a1) {
        /*SL:180*/this.delegate.setCharacterEscapes(a1);
        /*SL:181*/return this;
    }
    
    @Override
    public JsonGenerator setRootValueSeparator(final SerializableString a1) {
        /*SL:184*/this.delegate.setRootValueSeparator(a1);
        /*SL:185*/return this;
    }
    
    @Override
    public void writeStartArray() throws IOException {
        /*SL:194*/this.delegate.writeStartArray();
    }
    
    @Override
    public void writeStartArray(final int a1) throws IOException {
        /*SL:197*/this.delegate.writeStartArray(a1);
    }
    
    @Override
    public void writeEndArray() throws IOException {
        /*SL:200*/this.delegate.writeEndArray();
    }
    
    @Override
    public void writeStartObject() throws IOException {
        /*SL:203*/this.delegate.writeStartObject();
    }
    
    @Override
    public void writeStartObject(final Object a1) throws IOException {
        /*SL:206*/this.delegate.writeStartObject(a1);
    }
    
    @Override
    public void writeEndObject() throws IOException {
        /*SL:209*/this.delegate.writeEndObject();
    }
    
    @Override
    public void writeFieldName(final String a1) throws IOException {
        /*SL:213*/this.delegate.writeFieldName(a1);
    }
    
    @Override
    public void writeFieldName(final SerializableString a1) throws IOException {
        /*SL:218*/this.delegate.writeFieldName(a1);
    }
    
    @Override
    public void writeFieldId(final long a1) throws IOException {
        /*SL:223*/this.delegate.writeFieldId(a1);
    }
    
    @Override
    public void writeArray(final int[] a1, final int a2, final int a3) throws IOException {
        /*SL:228*/this.delegate.writeArray(a1, a2, a3);
    }
    
    @Override
    public void writeArray(final long[] a1, final int a2, final int a3) throws IOException {
        /*SL:233*/this.delegate.writeArray(a1, a2, a3);
    }
    
    @Override
    public void writeArray(final double[] a1, final int a2, final int a3) throws IOException {
        /*SL:238*/this.delegate.writeArray(a1, a2, a3);
    }
    
    @Override
    public void writeString(final String a1) throws IOException {
        /*SL:248*/this.delegate.writeString(a1);
    }
    
    @Override
    public void writeString(final Reader a1, final int a2) throws IOException {
        /*SL:252*/this.delegate.writeString(a1, a2);
    }
    
    @Override
    public void writeString(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:256*/this.delegate.writeString(a1, a2, a3);
    }
    
    @Override
    public void writeString(final SerializableString a1) throws IOException {
        /*SL:259*/this.delegate.writeString(a1);
    }
    
    @Override
    public void writeRawUTF8String(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:262*/this.delegate.writeRawUTF8String(a1, a2, a3);
    }
    
    @Override
    public void writeUTF8String(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:265*/this.delegate.writeUTF8String(a1, a2, a3);
    }
    
    @Override
    public void writeRaw(final String a1) throws IOException {
        /*SL:274*/this.delegate.writeRaw(a1);
    }
    
    @Override
    public void writeRaw(final String a1, final int a2, final int a3) throws IOException {
        /*SL:277*/this.delegate.writeRaw(a1, a2, a3);
    }
    
    @Override
    public void writeRaw(final SerializableString a1) throws IOException {
        /*SL:280*/this.delegate.writeRaw(a1);
    }
    
    @Override
    public void writeRaw(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:283*/this.delegate.writeRaw(a1, a2, a3);
    }
    
    @Override
    public void writeRaw(final char a1) throws IOException {
        /*SL:286*/this.delegate.writeRaw(a1);
    }
    
    @Override
    public void writeRawValue(final String a1) throws IOException {
        /*SL:289*/this.delegate.writeRawValue(a1);
    }
    
    @Override
    public void writeRawValue(final String a1, final int a2, final int a3) throws IOException {
        /*SL:292*/this.delegate.writeRawValue(a1, a2, a3);
    }
    
    @Override
    public void writeRawValue(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:295*/this.delegate.writeRawValue(a1, a2, a3);
    }
    
    @Override
    public void writeBinary(final Base64Variant a1, final byte[] a2, final int a3, final int a4) throws IOException {
        /*SL:298*/this.delegate.writeBinary(a1, a2, a3, a4);
    }
    
    @Override
    public int writeBinary(final Base64Variant a1, final InputStream a2, final int a3) throws IOException {
        /*SL:301*/return this.delegate.writeBinary(a1, a2, a3);
    }
    
    @Override
    public void writeNumber(final short a1) throws IOException {
        /*SL:310*/this.delegate.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final int a1) throws IOException {
        /*SL:313*/this.delegate.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final long a1) throws IOException {
        /*SL:316*/this.delegate.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final BigInteger a1) throws IOException {
        /*SL:319*/this.delegate.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final double a1) throws IOException {
        /*SL:322*/this.delegate.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final float a1) throws IOException {
        /*SL:325*/this.delegate.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final BigDecimal a1) throws IOException {
        /*SL:328*/this.delegate.writeNumber(a1);
    }
    
    @Override
    public void writeNumber(final String a1) throws IOException, UnsupportedOperationException {
        /*SL:331*/this.delegate.writeNumber(a1);
    }
    
    @Override
    public void writeBoolean(final boolean a1) throws IOException {
        /*SL:334*/this.delegate.writeBoolean(a1);
    }
    
    @Override
    public void writeNull() throws IOException {
        /*SL:337*/this.delegate.writeNull();
    }
    
    @Override
    public void writeOmittedField(final String a1) throws IOException {
        /*SL:346*/this.delegate.writeOmittedField(a1);
    }
    
    @Override
    public void writeObjectId(final Object a1) throws IOException {
        /*SL:355*/this.delegate.writeObjectId(a1);
    }
    
    @Override
    public void writeObjectRef(final Object a1) throws IOException {
        /*SL:358*/this.delegate.writeObjectRef(a1);
    }
    
    @Override
    public void writeTypeId(final Object a1) throws IOException {
        /*SL:361*/this.delegate.writeTypeId(a1);
    }
    
    @Override
    public void writeEmbeddedObject(final Object a1) throws IOException {
        /*SL:364*/this.delegate.writeEmbeddedObject(a1);
    }
    
    @Override
    public void writeObject(final Object v2) throws IOException {
        /*SL:374*/if (this.delegateCopyMethods) {
            /*SL:375*/this.delegate.writeObject(v2);
            /*SL:376*/return;
        }
        /*SL:378*/if (v2 == null) {
            /*SL:379*/this.writeNull();
        }
        else {
            final ObjectCodec a1 = /*EL:381*/this.getCodec();
            /*SL:382*/if (a1 != null) {
                /*SL:383*/a1.writeValue(this, v2);
                /*SL:384*/return;
            }
            /*SL:386*/this._writeSimpleObject(v2);
        }
    }
    
    @Override
    public void writeTree(final TreeNode v2) throws IOException {
        /*SL:392*/if (this.delegateCopyMethods) {
            /*SL:393*/this.delegate.writeTree(v2);
            /*SL:394*/return;
        }
        /*SL:397*/if (v2 == null) {
            /*SL:398*/this.writeNull();
        }
        else {
            final ObjectCodec a1 = /*EL:400*/this.getCodec();
            /*SL:401*/if (a1 == null) {
                /*SL:402*/throw new IllegalStateException("No ObjectCodec defined");
            }
            /*SL:404*/a1.writeTree(this, v2);
        }
    }
    
    @Override
    public void copyCurrentEvent(final JsonParser a1) throws IOException {
        /*SL:424*/if (this.delegateCopyMethods) {
            this.delegate.copyCurrentEvent(a1);
        }
        else {
            /*SL:425*/super.copyCurrentEvent(a1);
        }
    }
    
    @Override
    public void copyCurrentStructure(final JsonParser a1) throws IOException {
        /*SL:430*/if (this.delegateCopyMethods) {
            this.delegate.copyCurrentStructure(a1);
        }
        else {
            /*SL:431*/super.copyCurrentStructure(a1);
        }
    }
    
    @Override
    public JsonStreamContext getOutputContext() {
        /*SL:440*/return this.delegate.getOutputContext();
    }
    
    @Override
    public void flush() throws IOException {
        /*SL:448*/this.delegate.flush();
    }
    
    @Override
    public void close() throws IOException {
        /*SL:449*/this.delegate.close();
    }
    
    @Override
    public boolean isClosed() {
        /*SL:457*/return this.delegate.isClosed();
    }
}
