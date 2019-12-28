package com.fasterxml.jackson.core.base;

import java.math.BigDecimal;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TreeNode;
import java.io.InputStream;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.SerializableString;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonGenerator;

public abstract class GeneratorBase extends JsonGenerator
{
    public static final int SURR1_FIRST = 55296;
    public static final int SURR1_LAST = 56319;
    public static final int SURR2_FIRST = 56320;
    public static final int SURR2_LAST = 57343;
    protected static final int DERIVED_FEATURES_MASK;
    protected static final String WRITE_BINARY = "write a binary value";
    protected static final String WRITE_BOOLEAN = "write a boolean value";
    protected static final String WRITE_NULL = "write a null";
    protected static final String WRITE_NUMBER = "write a number";
    protected static final String WRITE_RAW = "write a raw (unencoded) value";
    protected static final String WRITE_STRING = "write a string";
    protected static final int MAX_BIG_DECIMAL_SCALE = 9999;
    protected ObjectCodec _objectCodec;
    protected int _features;
    protected boolean _cfgNumbersAsStrings;
    protected JsonWriteContext _writeContext;
    protected boolean _closed;
    
    protected GeneratorBase(final int a1, final ObjectCodec a2) {
        this._features = a1;
        this._objectCodec = a2;
        final DupDetector v1 = Feature.STRICT_DUPLICATE_DETECTION.enabledIn(a1) ? DupDetector.rootDetector(this) : null;
        this._writeContext = JsonWriteContext.createRootContext(v1);
        this._cfgNumbersAsStrings = Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(a1);
    }
    
    protected GeneratorBase(final int a1, final ObjectCodec a2, final JsonWriteContext a3) {
        this._features = a1;
        this._objectCodec = a2;
        this._writeContext = a3;
        this._cfgNumbersAsStrings = Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(a1);
    }
    
    @Override
    public Version version() {
        /*SL:129*/return PackageVersion.VERSION;
    }
    
    @Override
    public Object getCurrentValue() {
        /*SL:133*/return this._writeContext.getCurrentValue();
    }
    
    @Override
    public void setCurrentValue(final Object a1) {
        /*SL:138*/this._writeContext.setCurrentValue(a1);
    }
    
    @Override
    public final boolean isEnabled(final Feature a1) {
        /*SL:148*/return (this._features & a1.getMask()) != 0x0;
    }
    
    @Override
    public int getFeatureMask() {
        /*SL:149*/return this._features;
    }
    
    @Override
    public JsonGenerator enable(final Feature a1) {
        final int v1 = /*EL:155*/a1.getMask();
        /*SL:156*/this._features |= v1;
        /*SL:157*/if ((v1 & GeneratorBase.DERIVED_FEATURES_MASK) != 0x0) {
            /*SL:159*/if (a1 == Feature.WRITE_NUMBERS_AS_STRINGS) {
                /*SL:160*/this._cfgNumbersAsStrings = true;
            }
            else/*SL:161*/ if (a1 == Feature.ESCAPE_NON_ASCII) {
                /*SL:162*/this.setHighestNonEscapedChar(127);
            }
            else/*SL:163*/ if (a1 == Feature.STRICT_DUPLICATE_DETECTION && /*EL:164*/this._writeContext.getDupDetector() == null) {
                /*SL:165*/this._writeContext = this._writeContext.withDupDetector(DupDetector.rootDetector(this));
            }
        }
        /*SL:169*/return this;
    }
    
    @Override
    public JsonGenerator disable(final Feature a1) {
        final int v1 = /*EL:174*/a1.getMask();
        /*SL:175*/this._features &= ~v1;
        /*SL:176*/if ((v1 & GeneratorBase.DERIVED_FEATURES_MASK) != 0x0) {
            /*SL:177*/if (a1 == Feature.WRITE_NUMBERS_AS_STRINGS) {
                /*SL:178*/this._cfgNumbersAsStrings = false;
            }
            else/*SL:179*/ if (a1 == Feature.ESCAPE_NON_ASCII) {
                /*SL:180*/this.setHighestNonEscapedChar(0);
            }
            else/*SL:181*/ if (a1 == Feature.STRICT_DUPLICATE_DETECTION) {
                /*SL:182*/this._writeContext = this._writeContext.withDupDetector(null);
            }
        }
        /*SL:185*/return this;
    }
    
    @Deprecated
    @Override
    public JsonGenerator setFeatureMask(final int a1) {
        final int v1 = /*EL:191*/a1 ^ this._features;
        /*SL:192*/this._features = a1;
        /*SL:193*/if (v1 != 0) {
            /*SL:194*/this._checkStdFeatureChanges(a1, v1);
        }
        /*SL:196*/return this;
    }
    
    @Override
    public JsonGenerator overrideStdFeatures(final int a1, final int a2) {
        final int v1 = /*EL:201*/this._features;
        final int v2 = /*EL:202*/(v1 & ~a2) | (a1 & a2);
        final int v3 = /*EL:203*/v1 ^ v2;
        /*SL:204*/if (v3 != 0) {
            /*SL:206*/this._checkStdFeatureChanges(this._features = v2, v3);
        }
        /*SL:208*/return this;
    }
    
    protected void _checkStdFeatureChanges(final int a1, final int a2) {
        /*SL:222*/if ((a2 & GeneratorBase.DERIVED_FEATURES_MASK) == 0x0) {
            /*SL:223*/return;
        }
        /*SL:225*/this._cfgNumbersAsStrings = Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(a1);
        /*SL:226*/if (Feature.ESCAPE_NON_ASCII.enabledIn(a2)) {
            /*SL:227*/if (Feature.ESCAPE_NON_ASCII.enabledIn(a1)) {
                /*SL:228*/this.setHighestNonEscapedChar(127);
            }
            else {
                /*SL:230*/this.setHighestNonEscapedChar(0);
            }
        }
        /*SL:233*/if (Feature.STRICT_DUPLICATE_DETECTION.enabledIn(a2)) {
            /*SL:234*/if (Feature.STRICT_DUPLICATE_DETECTION.enabledIn(a1)) {
                /*SL:235*/if (this._writeContext.getDupDetector() == null) {
                    /*SL:236*/this._writeContext = this._writeContext.withDupDetector(DupDetector.rootDetector(this));
                }
            }
            else {
                /*SL:239*/this._writeContext = this._writeContext.withDupDetector(null);
            }
        }
    }
    
    @Override
    public JsonGenerator useDefaultPrettyPrinter() {
        /*SL:246*/if (this.getPrettyPrinter() != null) {
            /*SL:247*/return this;
        }
        /*SL:249*/return this.setPrettyPrinter(this._constructDefaultPrettyPrinter());
    }
    
    @Override
    public JsonGenerator setCodec(final ObjectCodec a1) {
        /*SL:253*/this._objectCodec = a1;
        /*SL:254*/return this;
    }
    
    @Override
    public ObjectCodec getCodec() {
        /*SL:257*/return this._objectCodec;
    }
    
    @Override
    public JsonStreamContext getOutputContext() {
        /*SL:270*/return this._writeContext;
    }
    
    @Override
    public void writeStartObject(final Object a1) throws IOException {
        /*SL:286*/this.writeStartObject();
        /*SL:287*/if (this._writeContext != null && a1 != null) {
            /*SL:288*/this._writeContext.setCurrentValue(a1);
        }
        /*SL:290*/this.setCurrentValue(a1);
    }
    
    @Override
    public void writeFieldName(final SerializableString a1) throws IOException {
        /*SL:300*/this.writeFieldName(a1.getValue());
    }
    
    @Override
    public void writeString(final SerializableString a1) throws IOException {
        /*SL:315*/this.writeString(a1.getValue());
    }
    
    @Override
    public void writeRawValue(final String a1) throws IOException {
        /*SL:319*/this._verifyValueWrite("write raw value");
        /*SL:320*/this.writeRaw(a1);
    }
    
    @Override
    public void writeRawValue(final String a1, final int a2, final int a3) throws IOException {
        /*SL:324*/this._verifyValueWrite("write raw value");
        /*SL:325*/this.writeRaw(a1, a2, a3);
    }
    
    @Override
    public void writeRawValue(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:329*/this._verifyValueWrite("write raw value");
        /*SL:330*/this.writeRaw(a1, a2, a3);
    }
    
    @Override
    public void writeRawValue(final SerializableString a1) throws IOException {
        /*SL:334*/this._verifyValueWrite("write raw value");
        /*SL:335*/this.writeRaw(a1);
    }
    
    @Override
    public int writeBinary(final Base64Variant a1, final InputStream a2, final int a3) throws IOException {
        /*SL:341*/this._reportUnsupportedOperation();
        /*SL:342*/return 0;
    }
    
    @Override
    public void writeObject(final Object a1) throws IOException {
        /*SL:371*/if (a1 == null) {
            /*SL:373*/this.writeNull();
        }
        else {
            /*SL:380*/if (this._objectCodec != null) {
                /*SL:381*/this._objectCodec.writeValue(this, a1);
                /*SL:382*/return;
            }
            /*SL:384*/this._writeSimpleObject(a1);
        }
    }
    
    @Override
    public void writeTree(final TreeNode a1) throws IOException {
        /*SL:391*/if (a1 == null) {
            /*SL:392*/this.writeNull();
        }
        else {
            /*SL:394*/if (this._objectCodec == null) {
                /*SL:395*/throw new IllegalStateException("No ObjectCodec defined");
            }
            /*SL:397*/this._objectCodec.writeValue(this, a1);
        }
    }
    
    @Override
    public abstract void flush() throws IOException;
    
    @Override
    public void close() throws IOException {
        /*SL:408*/this._closed = true;
    }
    
    @Override
    public boolean isClosed() {
        /*SL:409*/return this._closed;
    }
    
    protected abstract void _releaseBuffers();
    
    protected abstract void _verifyValueWrite(final String p0) throws IOException;
    
    protected PrettyPrinter _constructDefaultPrettyPrinter() {
        /*SL:440*/return new DefaultPrettyPrinter();
    }
    
    protected String _asString(final BigDecimal v2) throws IOException {
        /*SL:450*/if (Feature.WRITE_BIGDECIMAL_AS_PLAIN.enabledIn(this._features)) {
            final int a1 = /*EL:452*/v2.scale();
            /*SL:453*/if (a1 < -9999 || a1 > 9999) {
                /*SL:454*/this._reportError(String.format("Attempt to write plain `java.math.BigDecimal` (see JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN) with illegal scale (%d): needs to be between [-%d, %d]", a1, 9999, 9999));
            }
            /*SL:458*/return v2.toPlainString();
        }
        /*SL:460*/return v2.toString();
    }
    
    protected final int _decodeSurrogate(final int v1, final int v2) throws IOException {
        /*SL:475*/if (v2 < 56320 || v2 > 57343) {
            final String a1 = /*EL:476*/"Incomplete surrogate pair: first char 0x" + Integer.toHexString(v1) + ", second 0x" + Integer.toHexString(v2);
            /*SL:477*/this._reportError(a1);
        }
        final int v3 = /*EL:479*/65536 + (v1 - 55296 << 10) + (v2 - 56320);
        /*SL:480*/return v3;
    }
    
    static {
        DERIVED_FEATURES_MASK = (Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask());
    }
}
