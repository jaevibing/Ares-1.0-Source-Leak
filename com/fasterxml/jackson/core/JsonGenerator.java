package com.fasterxml.jackson.core;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.core.type.WritableTypeId;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.InputStream;
import java.io.Reader;
import java.io.IOException;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import java.io.Flushable;
import java.io.Closeable;

public abstract class JsonGenerator implements Closeable, Flushable, Versioned
{
    protected PrettyPrinter _cfgPrettyPrinter;
    
    public abstract JsonGenerator setCodec(final ObjectCodec p0);
    
    public abstract ObjectCodec getCodec();
    
    @Override
    public abstract Version version();
    
    public abstract JsonGenerator enable(final Feature p0);
    
    public abstract JsonGenerator disable(final Feature p0);
    
    public final JsonGenerator configure(final Feature a1, final boolean a2) {
        /*SL:322*/if (a2) {
            this.enable(a1);
        }
        else {
            this.disable(a1);
        }
        /*SL:323*/return this;
    }
    
    public abstract boolean isEnabled(final Feature p0);
    
    public abstract int getFeatureMask();
    
    @Deprecated
    public abstract JsonGenerator setFeatureMask(final int p0);
    
    public JsonGenerator overrideStdFeatures(final int a1, final int a2) {
        final int v1 = /*EL:373*/this.getFeatureMask();
        final int v2 = /*EL:374*/(v1 & ~a2) | (a1 & a2);
        /*SL:375*/return this.setFeatureMask(v2);
    }
    
    public int getFormatFeatures() {
        /*SL:387*/return 0;
    }
    
    public JsonGenerator overrideFormatFeatures(final int a1, final int a2) {
        /*SL:404*/throw new IllegalArgumentException("No FormatFeatures defined for generator of type " + this.getClass().getName());
    }
    
    public void setSchema(final FormatSchema a1) {
        /*SL:434*/throw new UnsupportedOperationException("Generator of type " + this.getClass().getName() + " does not support schema of type '" + a1.getSchemaType() + "'");
    }
    
    public FormatSchema getSchema() {
        /*SL:444*/return null;
    }
    
    public JsonGenerator setPrettyPrinter(final PrettyPrinter a1) {
        /*SL:464*/this._cfgPrettyPrinter = a1;
        /*SL:465*/return this;
    }
    
    public PrettyPrinter getPrettyPrinter() {
        /*SL:475*/return this._cfgPrettyPrinter;
    }
    
    public abstract JsonGenerator useDefaultPrettyPrinter();
    
    public JsonGenerator setHighestNonEscapedChar(final int a1) {
        /*SL:508*/return this;
    }
    
    public int getHighestEscapedChar() {
        /*SL:522*/return 0;
    }
    
    public CharacterEscapes getCharacterEscapes() {
        /*SL:528*/return null;
    }
    
    public JsonGenerator setCharacterEscapes(final CharacterEscapes a1) {
        /*SL:536*/return this;
    }
    
    public JsonGenerator setRootValueSeparator(final SerializableString a1) {
        /*SL:550*/throw new UnsupportedOperationException();
    }
    
    public Object getOutputTarget() {
        /*SL:575*/return null;
    }
    
    public int getOutputBuffered() {
        /*SL:597*/return -1;
    }
    
    public Object getCurrentValue() {
        final JsonStreamContext v1 = /*EL:614*/this.getOutputContext();
        /*SL:615*/return (v1 == null) ? null : v1.getCurrentValue();
    }
    
    public void setCurrentValue(final Object a1) {
        final JsonStreamContext v1 = /*EL:627*/this.getOutputContext();
        /*SL:628*/if (v1 != null) {
            /*SL:629*/v1.setCurrentValue(a1);
        }
    }
    
    public boolean canUseSchema(final FormatSchema a1) {
        /*SL:647*/return false;
    }
    
    public boolean canWriteObjectId() {
        /*SL:663*/return false;
    }
    
    public boolean canWriteTypeId() {
        /*SL:679*/return false;
    }
    
    public boolean canWriteBinaryNatively() {
        /*SL:691*/return false;
    }
    
    public boolean canOmitFields() {
        /*SL:701*/return true;
    }
    
    public boolean canWriteFormattedNumbers() {
        /*SL:715*/return false;
    }
    
    public abstract void writeStartArray() throws IOException;
    
    public void writeStartArray(final int a1) throws IOException {
        /*SL:750*/this.writeStartArray();
    }
    
    public abstract void writeEndArray() throws IOException;
    
    public abstract void writeStartObject() throws IOException;
    
    public void writeStartObject(final Object a1) throws IOException {
        /*SL:790*/this.writeStartObject();
        /*SL:791*/this.setCurrentValue(a1);
    }
    
    public abstract void writeEndObject() throws IOException;
    
    public abstract void writeFieldName(final String p0) throws IOException;
    
    public abstract void writeFieldName(final SerializableString p0) throws IOException;
    
    public void writeFieldId(final long a1) throws IOException {
        /*SL:841*/this.writeFieldName(Long.toString(a1));
    }
    
    public void writeArray(final int[] v1, final int v2, final int v3) throws IOException {
        /*SL:863*/if (v1 == null) {
            /*SL:864*/throw new IllegalArgumentException("null array");
        }
        /*SL:866*/this._verifyOffsets(v1.length, v2, v3);
        /*SL:867*/this.writeStartArray();
        /*SL:868*/for (int a1 = v2, a2 = v2 + v3; a1 < a2; ++a1) {
            /*SL:869*/this.writeNumber(v1[a1]);
        }
        /*SL:871*/this.writeEndArray();
    }
    
    public void writeArray(final long[] v1, final int v2, final int v3) throws IOException {
        /*SL:887*/if (v1 == null) {
            /*SL:888*/throw new IllegalArgumentException("null array");
        }
        /*SL:890*/this._verifyOffsets(v1.length, v2, v3);
        /*SL:891*/this.writeStartArray();
        /*SL:892*/for (int a1 = v2, a2 = v2 + v3; a1 < a2; ++a1) {
            /*SL:893*/this.writeNumber(v1[a1]);
        }
        /*SL:895*/this.writeEndArray();
    }
    
    public void writeArray(final double[] v1, final int v2, final int v3) throws IOException {
        /*SL:911*/if (v1 == null) {
            /*SL:912*/throw new IllegalArgumentException("null array");
        }
        /*SL:914*/this._verifyOffsets(v1.length, v2, v3);
        /*SL:915*/this.writeStartArray();
        /*SL:916*/for (int a1 = v2, a2 = v2 + v3; a1 < a2; ++a1) {
            /*SL:917*/this.writeNumber(v1[a1]);
        }
        /*SL:919*/this.writeEndArray();
    }
    
    public abstract void writeString(final String p0) throws IOException;
    
    public void writeString(final Reader a1, final int a2) throws IOException {
        /*SL:951*/this._reportUnsupportedOperation();
    }
    
    public abstract void writeString(final char[] p0, final int p1, final int p2) throws IOException;
    
    public abstract void writeString(final SerializableString p0) throws IOException;
    
    public abstract void writeRawUTF8String(final byte[] p0, final int p1, final int p2) throws IOException;
    
    public abstract void writeUTF8String(final byte[] p0, final int p1, final int p2) throws IOException;
    
    public abstract void writeRaw(final String p0) throws IOException;
    
    public abstract void writeRaw(final String p0, final int p1, final int p2) throws IOException;
    
    public abstract void writeRaw(final char[] p0, final int p1, final int p2) throws IOException;
    
    public abstract void writeRaw(final char p0) throws IOException;
    
    public void writeRaw(final SerializableString a1) throws IOException {
        /*SL:1095*/this.writeRaw(a1.getValue());
    }
    
    public abstract void writeRawValue(final String p0) throws IOException;
    
    public abstract void writeRawValue(final String p0, final int p1, final int p2) throws IOException;
    
    public abstract void writeRawValue(final char[] p0, final int p1, final int p2) throws IOException;
    
    public void writeRawValue(final SerializableString a1) throws IOException {
        /*SL:1120*/this.writeRawValue(a1.getValue());
    }
    
    public abstract void writeBinary(final Base64Variant p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    public void writeBinary(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:1153*/this.writeBinary(Base64Variants.getDefaultVariant(), a1, a2, a3);
    }
    
    public void writeBinary(final byte[] a1) throws IOException {
        /*SL:1163*/this.writeBinary(Base64Variants.getDefaultVariant(), a1, 0, a1.length);
    }
    
    public int writeBinary(final InputStream a1, final int a2) throws IOException {
        /*SL:1181*/return this.writeBinary(Base64Variants.getDefaultVariant(), a1, a2);
    }
    
    public abstract int writeBinary(final Base64Variant p0, final InputStream p1, final int p2) throws IOException;
    
    public void writeNumber(final short a1) throws IOException {
        /*SL:1225*/this.writeNumber((int)a1);
    }
    
    public abstract void writeNumber(final int p0) throws IOException;
    
    public abstract void writeNumber(final long p0) throws IOException;
    
    public abstract void writeNumber(final BigInteger p0) throws IOException;
    
    public abstract void writeNumber(final double p0) throws IOException;
    
    public abstract void writeNumber(final float p0) throws IOException;
    
    public abstract void writeNumber(final BigDecimal p0) throws IOException;
    
    public abstract void writeNumber(final String p0) throws IOException;
    
    public abstract void writeBoolean(final boolean p0) throws IOException;
    
    public abstract void writeNull() throws IOException;
    
    public void writeEmbeddedObject(final Object a1) throws IOException {
        /*SL:1349*/if (a1 == null) {
            /*SL:1350*/this.writeNull();
            /*SL:1351*/return;
        }
        /*SL:1353*/if (a1 instanceof byte[]) {
            /*SL:1354*/this.writeBinary((byte[])a1);
            /*SL:1355*/return;
        }
        /*SL:1357*/throw new JsonGenerationException("No native support for writing embedded objects of type " + a1.getClass().getName(), this);
    }
    
    public void writeObjectId(final Object a1) throws IOException {
        /*SL:1380*/throw new JsonGenerationException("No native support for writing Object Ids", this);
    }
    
    public void writeObjectRef(final Object a1) throws IOException {
        /*SL:1393*/throw new JsonGenerationException("No native support for writing Object Ids", this);
    }
    
    public void writeTypeId(final Object a1) throws IOException {
        /*SL:1408*/throw new JsonGenerationException("No native support for writing Type Ids", this);
    }
    
    public WritableTypeId writeTypePrefix(final WritableTypeId v-3) throws IOException {
        final Object id = /*EL:1427*/v-3.id;
        final JsonToken valueShape = /*EL:1429*/v-3.valueShape;
        /*SL:1430*/if (this.canWriteTypeId()) {
            /*SL:1431*/v-3.wrapperWritten = false;
            /*SL:1433*/this.writeTypeId(id);
        }
        else {
            final String a1 = /*EL:1437*/(String)((id instanceof String) ? id : String.valueOf(id));
            /*SL:1438*/v-3.wrapperWritten = true;
            WritableTypeId.Inclusion v1 = /*EL:1440*/v-3.include;
            /*SL:1442*/if (valueShape != JsonToken.START_OBJECT && v1.requiresObjectContext()) {
                /*SL:1444*/v1 = (v-3.include = WritableTypeId.Inclusion.WRAPPER_ARRAY);
            }
            /*SL:1447*/switch (v1) {
                case PARENT_PROPERTY: {
                    /*SL:1450*/break;
                }
                case PAYLOAD_PROPERTY: {
                    /*SL:1454*/break;
                }
                case METADATA_PROPERTY: {
                    /*SL:1459*/this.writeStartObject(v-3.forValue);
                    /*SL:1460*/this.writeStringField(v-3.asProperty, a1);
                    /*SL:1461*/return v-3;
                }
                case WRAPPER_OBJECT: {
                    /*SL:1465*/this.writeStartObject();
                    /*SL:1466*/this.writeFieldName(a1);
                    /*SL:1467*/break;
                }
                default: {
                    /*SL:1470*/this.writeStartArray();
                    /*SL:1471*/this.writeString(a1);
                    break;
                }
            }
        }
        /*SL:1475*/if (valueShape == JsonToken.START_OBJECT) {
            /*SL:1476*/this.writeStartObject(v-3.forValue);
        }
        else/*SL:1477*/ if (valueShape == JsonToken.START_ARRAY) {
            /*SL:1479*/this.writeStartArray();
        }
        /*SL:1481*/return v-3;
    }
    
    public WritableTypeId writeTypeSuffix(final WritableTypeId v-2) throws IOException {
        final JsonToken valueShape = /*EL:1489*/v-2.valueShape;
        /*SL:1491*/if (valueShape == JsonToken.START_OBJECT) {
            /*SL:1492*/this.writeEndObject();
        }
        else/*SL:1493*/ if (valueShape == JsonToken.START_ARRAY) {
            /*SL:1494*/this.writeEndArray();
        }
        /*SL:1497*/if (v-2.wrapperWritten) {
            /*SL:1498*/switch (v-2.include) {
                case WRAPPER_ARRAY: {
                    /*SL:1500*/this.writeEndArray();
                    /*SL:1501*/break;
                }
                case PARENT_PROPERTY: {
                    final Object a1 = /*EL:1505*/v-2.id;
                    final String v1 = /*EL:1506*/(String)((a1 instanceof String) ? a1 : String.valueOf(a1));
                    /*SL:1507*/this.writeStringField(v-2.asProperty, v1);
                    /*SL:1509*/break;
                }
                case PAYLOAD_PROPERTY:
                case METADATA_PROPERTY: {
                    /*SL:1513*/break;
                }
                default: {
                    /*SL:1516*/this.writeEndObject();
                    break;
                }
            }
        }
        /*SL:1520*/return v-2;
    }
    
    public abstract void writeObject(final Object p0) throws IOException;
    
    public abstract void writeTree(final TreeNode p0) throws IOException;
    
    public void writeStringField(final String a1, final String a2) throws IOException {
        /*SL:1569*/this.writeFieldName(a1);
        /*SL:1570*/this.writeString(a2);
    }
    
    public final void writeBooleanField(final String a1, final boolean a2) throws IOException {
        /*SL:1582*/this.writeFieldName(a1);
        /*SL:1583*/this.writeBoolean(a2);
    }
    
    public final void writeNullField(final String a1) throws IOException {
        /*SL:1595*/this.writeFieldName(a1);
        /*SL:1596*/this.writeNull();
    }
    
    public final void writeNumberField(final String a1, final int a2) throws IOException {
        /*SL:1608*/this.writeFieldName(a1);
        /*SL:1609*/this.writeNumber(a2);
    }
    
    public final void writeNumberField(final String a1, final long a2) throws IOException {
        /*SL:1621*/this.writeFieldName(a1);
        /*SL:1622*/this.writeNumber(a2);
    }
    
    public final void writeNumberField(final String a1, final double a2) throws IOException {
        /*SL:1634*/this.writeFieldName(a1);
        /*SL:1635*/this.writeNumber(a2);
    }
    
    public final void writeNumberField(final String a1, final float a2) throws IOException {
        /*SL:1647*/this.writeFieldName(a1);
        /*SL:1648*/this.writeNumber(a2);
    }
    
    public final void writeNumberField(final String a1, final BigDecimal a2) throws IOException {
        /*SL:1661*/this.writeFieldName(a1);
        /*SL:1662*/this.writeNumber(a2);
    }
    
    public final void writeBinaryField(final String a1, final byte[] a2) throws IOException {
        /*SL:1675*/this.writeFieldName(a1);
        /*SL:1676*/this.writeBinary(a2);
    }
    
    public final void writeArrayFieldStart(final String a1) throws IOException {
        /*SL:1693*/this.writeFieldName(a1);
        /*SL:1694*/this.writeStartArray();
    }
    
    public final void writeObjectFieldStart(final String a1) throws IOException {
        /*SL:1711*/this.writeFieldName(a1);
        /*SL:1712*/this.writeStartObject();
    }
    
    public final void writeObjectField(final String a1, final Object a2) throws IOException {
        /*SL:1725*/this.writeFieldName(a1);
        /*SL:1726*/this.writeObject(a2);
    }
    
    public void writeOmittedField(final String a1) throws IOException {
    }
    
    public void copyCurrentEvent(final JsonParser v-1) throws IOException {
        final JsonToken v0 = /*EL:1758*/v-1.currentToken();
        /*SL:1760*/if (v0 == null) {
            /*SL:1761*/this._reportError("No current event to copy");
        }
        /*SL:1763*/switch (v0.id()) {
            case -1: {
                /*SL:1765*/this._reportError("No current event to copy");
                /*SL:1766*/break;
            }
            case 1: {
                /*SL:1768*/this.writeStartObject();
                /*SL:1769*/break;
            }
            case 2: {
                /*SL:1771*/this.writeEndObject();
                /*SL:1772*/break;
            }
            case 3: {
                /*SL:1774*/this.writeStartArray();
                /*SL:1775*/break;
            }
            case 4: {
                /*SL:1777*/this.writeEndArray();
                /*SL:1778*/break;
            }
            case 5: {
                /*SL:1780*/this.writeFieldName(v-1.getCurrentName());
                /*SL:1781*/break;
            }
            case 6: {
                /*SL:1783*/if (v-1.hasTextCharacters()) {
                    /*SL:1784*/this.writeString(v-1.getTextCharacters(), v-1.getTextOffset(), v-1.getTextLength());
                    break;
                }
                /*SL:1786*/this.writeString(v-1.getText());
                /*SL:1788*/break;
            }
            case 7: {
                final JsonParser.NumberType a1 = /*EL:1791*/v-1.getNumberType();
                /*SL:1792*/if (a1 == JsonParser.NumberType.INT) {
                    /*SL:1793*/this.writeNumber(v-1.getIntValue());
                    break;
                }
                /*SL:1794*/if (a1 == JsonParser.NumberType.BIG_INTEGER) {
                    /*SL:1795*/this.writeNumber(v-1.getBigIntegerValue());
                    break;
                }
                /*SL:1797*/this.writeNumber(v-1.getLongValue());
                /*SL:1799*/break;
            }
            case 8: {
                final JsonParser.NumberType v = /*EL:1803*/v-1.getNumberType();
                /*SL:1804*/if (v == JsonParser.NumberType.BIG_DECIMAL) {
                    /*SL:1805*/this.writeNumber(v-1.getDecimalValue());
                    break;
                }
                /*SL:1806*/if (v == JsonParser.NumberType.FLOAT) {
                    /*SL:1807*/this.writeNumber(v-1.getFloatValue());
                    break;
                }
                /*SL:1809*/this.writeNumber(v-1.getDoubleValue());
                /*SL:1811*/break;
            }
            case 9: {
                /*SL:1814*/this.writeBoolean(true);
                /*SL:1815*/break;
            }
            case 10: {
                /*SL:1817*/this.writeBoolean(false);
                /*SL:1818*/break;
            }
            case 11: {
                /*SL:1820*/this.writeNull();
                /*SL:1821*/break;
            }
            case 12: {
                /*SL:1823*/this.writeObject(v-1.getEmbeddedObject());
                /*SL:1824*/break;
            }
            default: {
                /*SL:1826*/this._throwInternal();
                break;
            }
        }
    }
    
    public void copyCurrentStructure(final JsonParser a1) throws IOException {
        JsonToken v1 = /*EL:1862*/a1.currentToken();
        /*SL:1863*/if (v1 == null) {
            /*SL:1864*/this._reportError("No current event to copy");
        }
        int v2 = /*EL:1867*/v1.id();
        /*SL:1868*/if (v2 == 5) {
            /*SL:1869*/this.writeFieldName(a1.getCurrentName());
            /*SL:1870*/v1 = a1.nextToken();
            /*SL:1871*/v2 = v1.id();
        }
        /*SL:1874*/switch (v2) {
            case 1: {
                /*SL:1876*/this.writeStartObject();
                /*SL:1877*/while (a1.nextToken() != JsonToken.END_OBJECT) {
                    /*SL:1878*/this.copyCurrentStructure(a1);
                }
                /*SL:1880*/this.writeEndObject();
                /*SL:1881*/break;
            }
            case 3: {
                /*SL:1883*/this.writeStartArray();
                /*SL:1884*/while (a1.nextToken() != JsonToken.END_ARRAY) {
                    /*SL:1885*/this.copyCurrentStructure(a1);
                }
                /*SL:1887*/this.writeEndArray();
                /*SL:1888*/break;
            }
            default: {
                /*SL:1890*/this.copyCurrentEvent(a1);
                break;
            }
        }
    }
    
    public abstract JsonStreamContext getOutputContext();
    
    @Override
    public abstract void flush() throws IOException;
    
    public abstract boolean isClosed();
    
    @Override
    public abstract void close() throws IOException;
    
    protected void _reportError(final String a1) throws JsonGenerationException {
        /*SL:1961*/throw new JsonGenerationException(a1, this);
    }
    
    protected final void _throwInternal() {
        /*SL:1964*/VersionUtil.throwInternal();
    }
    
    protected void _reportUnsupportedOperation() {
        /*SL:1967*/throw new UnsupportedOperationException("Operation not supported by generator of type " + this.getClass().getName());
    }
    
    protected final void _verifyOffsets(final int a1, final int a2, final int a3) {
        /*SL:1975*/if (a2 < 0 || a2 + a3 > a1) {
            /*SL:1976*/throw new IllegalArgumentException(String.format("invalid argument(s) (offset=%d, length=%d) for input array of %d element", a2, a3, a1));
        }
    }
    
    protected void _writeSimpleObject(final Object v2) throws IOException {
        /*SL:1995*/if (v2 == null) {
            /*SL:1996*/this.writeNull();
            /*SL:1997*/return;
        }
        /*SL:1999*/if (v2 instanceof String) {
            /*SL:2000*/this.writeString((String)v2);
            /*SL:2001*/return;
        }
        /*SL:2003*/if (v2 instanceof Number) {
            final Number a1 = /*EL:2004*/(Number)v2;
            /*SL:2005*/if (a1 instanceof Integer) {
                /*SL:2006*/this.writeNumber(a1.intValue());
                /*SL:2007*/return;
            }
            /*SL:2008*/if (a1 instanceof Long) {
                /*SL:2009*/this.writeNumber(a1.longValue());
                /*SL:2010*/return;
            }
            /*SL:2011*/if (a1 instanceof Double) {
                /*SL:2012*/this.writeNumber(a1.doubleValue());
                /*SL:2013*/return;
            }
            /*SL:2014*/if (a1 instanceof Float) {
                /*SL:2015*/this.writeNumber(a1.floatValue());
                /*SL:2016*/return;
            }
            /*SL:2017*/if (a1 instanceof Short) {
                /*SL:2018*/this.writeNumber(a1.shortValue());
                /*SL:2019*/return;
            }
            /*SL:2020*/if (a1 instanceof Byte) {
                /*SL:2021*/this.writeNumber(a1.byteValue());
                /*SL:2022*/return;
            }
            /*SL:2023*/if (a1 instanceof BigInteger) {
                /*SL:2024*/this.writeNumber((BigInteger)a1);
                /*SL:2025*/return;
            }
            /*SL:2026*/if (a1 instanceof BigDecimal) {
                /*SL:2027*/this.writeNumber((BigDecimal)a1);
                /*SL:2028*/return;
            }
            /*SL:2031*/if (a1 instanceof AtomicInteger) {
                /*SL:2032*/this.writeNumber(((AtomicInteger)a1).get());
                /*SL:2033*/return;
            }
            /*SL:2034*/if (a1 instanceof AtomicLong) {
                /*SL:2035*/this.writeNumber(((AtomicLong)a1).get());
                /*SL:2036*/return;
            }
        }
        else {
            /*SL:2038*/if (v2 instanceof byte[]) {
                /*SL:2039*/this.writeBinary((byte[])v2);
                /*SL:2040*/return;
            }
            /*SL:2041*/if (v2 instanceof Boolean) {
                /*SL:2042*/this.writeBoolean((boolean)v2);
                /*SL:2043*/return;
            }
            /*SL:2044*/if (v2 instanceof AtomicBoolean) {
                /*SL:2045*/this.writeBoolean(((AtomicBoolean)v2).get());
                /*SL:2046*/return;
            }
        }
        /*SL:2048*/throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed " + v2.getClass().getName() + ")");
    }
    
    public enum Feature
    {
        AUTO_CLOSE_TARGET(true), 
        AUTO_CLOSE_JSON_CONTENT(true), 
        FLUSH_PASSED_TO_STREAM(true), 
        QUOTE_FIELD_NAMES(true), 
        QUOTE_NON_NUMERIC_NUMBERS(true), 
        WRITE_NUMBERS_AS_STRINGS(false), 
        WRITE_BIGDECIMAL_AS_PLAIN(false), 
        ESCAPE_NON_ASCII(false), 
        STRICT_DUPLICATE_DETECTION(false), 
        IGNORE_UNKNOWN(false);
        
        private final boolean _defaultState;
        private final int _mask;
        
        public static int collectDefaults() {
            int n = /*EL:226*/0;
            /*SL:227*/for (final Feature v : values()) {
                /*SL:228*/if (v.enabledByDefault()) {
                    /*SL:229*/n |= v.getMask();
                }
            }
            /*SL:232*/return n;
        }
        
        private Feature(final boolean a1) {
            this._defaultState = a1;
            this._mask = 1 << this.ordinal();
        }
        
        public boolean enabledByDefault() {
            /*SL:240*/return this._defaultState;
        }
        
        public boolean enabledIn(final int a1) {
            /*SL:245*/return (a1 & this._mask) != 0x0;
        }
        
        public int getMask() {
            /*SL:247*/return this._mask;
        }
    }
}
