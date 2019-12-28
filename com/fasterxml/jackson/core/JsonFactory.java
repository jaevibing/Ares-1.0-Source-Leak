package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.io.DataOutputAsStream;
import com.fasterxml.jackson.core.util.BufferRecyclers;
import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.OutputStreamWriter;
import com.fasterxml.jackson.core.io.UTF8Writer;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import com.fasterxml.jackson.core.json.WriterBasedJsonGenerator;
import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import java.io.DataOutput;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.OutputStream;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import java.io.DataInput;
import java.io.CharArrayReader;
import java.io.StringReader;
import java.io.Reader;
import java.net.URL;
import java.io.InputStream;
import com.fasterxml.jackson.core.io.IOContext;
import java.io.FileInputStream;
import java.io.File;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.json.ByteSourceJsonBootstrapper;
import java.io.IOException;
import com.fasterxml.jackson.core.format.MatchStrength;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.io.OutputDecorator;
import com.fasterxml.jackson.core.io.InputDecorator;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import java.io.Serializable;

public class JsonFactory implements Versioned, Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String FORMAT_NAME_JSON = "JSON";
    protected static final int DEFAULT_FACTORY_FEATURE_FLAGS;
    protected static final int DEFAULT_PARSER_FEATURE_FLAGS;
    protected static final int DEFAULT_GENERATOR_FEATURE_FLAGS;
    private static final SerializableString DEFAULT_ROOT_VALUE_SEPARATOR;
    protected final transient CharsToNameCanonicalizer _rootCharSymbols;
    protected final transient ByteQuadsCanonicalizer _byteSymbolCanonicalizer;
    protected ObjectCodec _objectCodec;
    protected int _factoryFeatures;
    protected int _parserFeatures;
    protected int _generatorFeatures;
    protected CharacterEscapes _characterEscapes;
    protected InputDecorator _inputDecorator;
    protected OutputDecorator _outputDecorator;
    protected SerializableString _rootValueSeparator;
    
    public JsonFactory() {
        this(null);
    }
    
    public JsonFactory(final ObjectCodec a1) {
        this._rootCharSymbols = CharsToNameCanonicalizer.createRoot();
        this._byteSymbolCanonicalizer = ByteQuadsCanonicalizer.createRoot();
        this._factoryFeatures = JsonFactory.DEFAULT_FACTORY_FEATURE_FLAGS;
        this._parserFeatures = JsonFactory.DEFAULT_PARSER_FEATURE_FLAGS;
        this._generatorFeatures = JsonFactory.DEFAULT_GENERATOR_FEATURE_FLAGS;
        this._rootValueSeparator = JsonFactory.DEFAULT_ROOT_VALUE_SEPARATOR;
        this._objectCodec = a1;
    }
    
    protected JsonFactory(final JsonFactory a1, final ObjectCodec a2) {
        this._rootCharSymbols = CharsToNameCanonicalizer.createRoot();
        this._byteSymbolCanonicalizer = ByteQuadsCanonicalizer.createRoot();
        this._factoryFeatures = JsonFactory.DEFAULT_FACTORY_FEATURE_FLAGS;
        this._parserFeatures = JsonFactory.DEFAULT_PARSER_FEATURE_FLAGS;
        this._generatorFeatures = JsonFactory.DEFAULT_GENERATOR_FEATURE_FLAGS;
        this._rootValueSeparator = JsonFactory.DEFAULT_ROOT_VALUE_SEPARATOR;
        this._objectCodec = a2;
        this._factoryFeatures = a1._factoryFeatures;
        this._parserFeatures = a1._parserFeatures;
        this._generatorFeatures = a1._generatorFeatures;
        this._characterEscapes = a1._characterEscapes;
        this._inputDecorator = a1._inputDecorator;
        this._outputDecorator = a1._outputDecorator;
        this._rootValueSeparator = a1._rootValueSeparator;
    }
    
    public JsonFactory copy() {
        /*SL:320*/this._checkInvalidCopy(JsonFactory.class);
        /*SL:322*/return new JsonFactory(this, null);
    }
    
    protected void _checkInvalidCopy(final Class<?> a1) {
        /*SL:331*/if (this.getClass() != a1) {
            /*SL:332*/throw new IllegalStateException("Failed copy(): " + this.getClass().getName() + " (version: " + this.version() + ") does not override copy(); it has to");
        }
    }
    
    protected Object readResolve() {
        /*SL:349*/return new JsonFactory(this, this._objectCodec);
    }
    
    public boolean requiresPropertyOrdering() {
        /*SL:373*/return false;
    }
    
    public boolean canHandleBinaryNatively() {
        /*SL:387*/return false;
    }
    
    public boolean canUseCharArrays() {
        /*SL:401*/return true;
    }
    
    public boolean canParseAsync() {
        /*SL:414*/return this._isJSONFactory();
    }
    
    public Class<? extends FormatFeature> getFormatReadFeatureType() {
        /*SL:425*/return null;
    }
    
    public Class<? extends FormatFeature> getFormatWriteFeatureType() {
        /*SL:436*/return null;
    }
    
    public boolean canUseSchema(final FormatSchema a1) {
        /*SL:455*/if (a1 == null) {
            /*SL:456*/return false;
        }
        final String v1 = /*EL:458*/this.getFormatName();
        /*SL:459*/return v1 != null && v1.equals(a1.getSchemaType());
    }
    
    public String getFormatName() {
        /*SL:475*/if (this.getClass() == JsonFactory.class) {
            /*SL:476*/return "JSON";
        }
        /*SL:478*/return null;
    }
    
    public MatchStrength hasFormat(final InputAccessor a1) throws IOException {
        /*SL:488*/if (this.getClass() == JsonFactory.class) {
            /*SL:489*/return this.hasJSONFormat(a1);
        }
        /*SL:491*/return null;
    }
    
    public boolean requiresCustomCodec() {
        /*SL:508*/return false;
    }
    
    protected MatchStrength hasJSONFormat(final InputAccessor a1) throws IOException {
        /*SL:517*/return ByteSourceJsonBootstrapper.hasJSONFormat(a1);
    }
    
    @Override
    public Version version() {
        /*SL:528*/return PackageVersion.VERSION;
    }
    
    public final JsonFactory configure(final Feature a1, final boolean a2) {
        /*SL:542*/return a2 ? this.enable(a1) : this.disable(a1);
    }
    
    public JsonFactory enable(final Feature a1) {
        /*SL:550*/this._factoryFeatures |= a1.getMask();
        /*SL:551*/return this;
    }
    
    public JsonFactory disable(final Feature a1) {
        /*SL:559*/this._factoryFeatures &= ~a1.getMask();
        /*SL:560*/return this;
    }
    
    public final boolean isEnabled(final Feature a1) {
        /*SL:567*/return (this._factoryFeatures & a1.getMask()) != 0x0;
    }
    
    public final JsonFactory configure(final JsonParser.Feature a1, final boolean a2) {
        /*SL:581*/return a2 ? this.enable(a1) : this.disable(a1);
    }
    
    public JsonFactory enable(final JsonParser.Feature a1) {
        /*SL:589*/this._parserFeatures |= a1.getMask();
        /*SL:590*/return this;
    }
    
    public JsonFactory disable(final JsonParser.Feature a1) {
        /*SL:598*/this._parserFeatures &= ~a1.getMask();
        /*SL:599*/return this;
    }
    
    public final boolean isEnabled(final JsonParser.Feature a1) {
        /*SL:606*/return (this._parserFeatures & a1.getMask()) != 0x0;
    }
    
    public InputDecorator getInputDecorator() {
        /*SL:614*/return this._inputDecorator;
    }
    
    public JsonFactory setInputDecorator(final InputDecorator a1) {
        /*SL:621*/this._inputDecorator = a1;
        /*SL:622*/return this;
    }
    
    public final JsonFactory configure(final JsonGenerator.Feature a1, final boolean a2) {
        /*SL:636*/return a2 ? this.enable(a1) : this.disable(a1);
    }
    
    public JsonFactory enable(final JsonGenerator.Feature a1) {
        /*SL:645*/this._generatorFeatures |= a1.getMask();
        /*SL:646*/return this;
    }
    
    public JsonFactory disable(final JsonGenerator.Feature a1) {
        /*SL:654*/this._generatorFeatures &= ~a1.getMask();
        /*SL:655*/return this;
    }
    
    public final boolean isEnabled(final JsonGenerator.Feature a1) {
        /*SL:662*/return (this._generatorFeatures & a1.getMask()) != 0x0;
    }
    
    public CharacterEscapes getCharacterEscapes() {
        /*SL:669*/return this._characterEscapes;
    }
    
    public JsonFactory setCharacterEscapes(final CharacterEscapes a1) {
        /*SL:676*/this._characterEscapes = a1;
        /*SL:677*/return this;
    }
    
    public OutputDecorator getOutputDecorator() {
        /*SL:685*/return this._outputDecorator;
    }
    
    public JsonFactory setOutputDecorator(final OutputDecorator a1) {
        /*SL:692*/this._outputDecorator = a1;
        /*SL:693*/return this;
    }
    
    public JsonFactory setRootValueSeparator(final String a1) {
        /*SL:706*/this._rootValueSeparator = ((a1 == null) ? null : new SerializedString(a1));
        /*SL:707*/return this;
    }
    
    public String getRootValueSeparator() {
        /*SL:714*/return (this._rootValueSeparator == null) ? null : this._rootValueSeparator.getValue();
    }
    
    public JsonFactory setCodec(final ObjectCodec a1) {
        /*SL:731*/this._objectCodec = a1;
        /*SL:732*/return this;
    }
    
    public ObjectCodec getCodec() {
        /*SL:735*/return this._objectCodec;
    }
    
    public JsonParser createParser(final File a1) throws IOException, JsonParseException {
        final IOContext v1 = /*EL:765*/this._createContext(a1, true);
        final InputStream v2 = /*EL:766*/new FileInputStream(a1);
        /*SL:767*/return this._createParser(this._decorate(v2, v1), v1);
    }
    
    public JsonParser createParser(final URL a1) throws IOException, JsonParseException {
        final IOContext v1 = /*EL:792*/this._createContext(a1, true);
        final InputStream v2 = /*EL:793*/this._optimizedStreamFromURL(a1);
        /*SL:794*/return this._createParser(this._decorate(v2, v1), v1);
    }
    
    public JsonParser createParser(final InputStream a1) throws IOException, JsonParseException {
        final IOContext v1 = /*EL:819*/this._createContext(a1, false);
        /*SL:820*/return this._createParser(this._decorate(a1, v1), v1);
    }
    
    public JsonParser createParser(final Reader a1) throws IOException, JsonParseException {
        final IOContext v1 = /*EL:839*/this._createContext(a1, false);
        /*SL:840*/return this._createParser(this._decorate(a1, v1), v1);
    }
    
    public JsonParser createParser(final byte[] v2) throws IOException, JsonParseException {
        final IOContext v3 = /*EL:850*/this._createContext(v2, true);
        /*SL:851*/if (this._inputDecorator != null) {
            final InputStream a1 = /*EL:852*/this._inputDecorator.decorate(v3, v2, 0, v2.length);
            /*SL:853*/if (a1 != null) {
                /*SL:854*/return this._createParser(a1, v3);
            }
        }
        /*SL:857*/return this._createParser(v2, 0, v2.length, v3);
    }
    
    public JsonParser createParser(final byte[] a3, final int v1, final int v2) throws IOException, JsonParseException {
        final IOContext v3 = /*EL:871*/this._createContext(a3, true);
        /*SL:873*/if (this._inputDecorator != null) {
            final InputStream a4 = /*EL:874*/this._inputDecorator.decorate(v3, a3, v1, v2);
            /*SL:875*/if (a4 != null) {
                /*SL:876*/return this._createParser(a4, v3);
            }
        }
        /*SL:879*/return this._createParser(a3, v1, v2, v3);
    }
    
    public JsonParser createParser(final String a1) throws IOException, JsonParseException {
        final int v1 = /*EL:889*/a1.length();
        /*SL:891*/if (this._inputDecorator != null || v1 > 32768 || !this.canUseCharArrays()) {
            /*SL:894*/return this.createParser(new StringReader(a1));
        }
        final IOContext v2 = /*EL:896*/this._createContext(a1, true);
        final char[] v3 = /*EL:897*/v2.allocTokenBuffer(v1);
        /*SL:898*/a1.getChars(0, v1, v3, 0);
        /*SL:899*/return this._createParser(v3, 0, v1, v2, true);
    }
    
    public JsonParser createParser(final char[] a1) throws IOException {
        /*SL:909*/return this.createParser(a1, 0, a1.length);
    }
    
    public JsonParser createParser(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:918*/if (this._inputDecorator != null) {
            /*SL:919*/return this.createParser(new CharArrayReader(a1, a2, a3));
        }
        /*SL:921*/return this._createParser(a1, a2, a3, this._createContext(a1, true), false);
    }
    
    public JsonParser createParser(final DataInput a1) throws IOException {
        final IOContext v1 = /*EL:936*/this._createContext(a1, false);
        /*SL:937*/return this._createParser(this._decorate(a1, v1), v1);
    }
    
    public JsonParser createNonBlockingByteArrayParser() throws IOException {
        /*SL:962*/this._requireJSONFactory("Non-blocking source not (yet?) support for this format (%s)");
        final IOContext v1 = /*EL:963*/this._createContext(null, false);
        final ByteQuadsCanonicalizer v2 = /*EL:964*/this._byteSymbolCanonicalizer.makeChild(this._factoryFeatures);
        /*SL:965*/return new NonBlockingJsonParser(v1, this._parserFeatures, v2);
    }
    
    @Deprecated
    public JsonParser createJsonParser(final File a1) throws IOException, JsonParseException {
        /*SL:995*/return this.createParser(a1);
    }
    
    @Deprecated
    public JsonParser createJsonParser(final URL a1) throws IOException, JsonParseException {
        /*SL:1020*/return this.createParser(a1);
    }
    
    @Deprecated
    public JsonParser createJsonParser(final InputStream a1) throws IOException, JsonParseException {
        /*SL:1046*/return this.createParser(a1);
    }
    
    @Deprecated
    public JsonParser createJsonParser(final Reader a1) throws IOException, JsonParseException {
        /*SL:1065*/return this.createParser(a1);
    }
    
    @Deprecated
    public JsonParser createJsonParser(final byte[] a1) throws IOException, JsonParseException {
        /*SL:1075*/return this.createParser(a1);
    }
    
    @Deprecated
    public JsonParser createJsonParser(final byte[] a1, final int a2, final int a3) throws IOException, JsonParseException {
        /*SL:1090*/return this.createParser(a1, a2, a3);
    }
    
    @Deprecated
    public JsonParser createJsonParser(final String a1) throws IOException, JsonParseException {
        /*SL:1101*/return this.createParser(a1);
    }
    
    public JsonGenerator createGenerator(final OutputStream a1, final JsonEncoding a2) throws IOException {
        final IOContext v1 = /*EL:1136*/this._createContext(a1, false);
        /*SL:1137*/v1.setEncoding(a2);
        /*SL:1138*/if (a2 == JsonEncoding.UTF8) {
            /*SL:1139*/return this._createUTF8Generator(this._decorate(a1, v1), v1);
        }
        final Writer v2 = /*EL:1141*/this._createWriter(a1, a2, v1);
        /*SL:1142*/return this._createGenerator(this._decorate(v2, v1), v1);
    }
    
    public JsonGenerator createGenerator(final OutputStream a1) throws IOException {
        /*SL:1154*/return this.createGenerator(a1, JsonEncoding.UTF8);
    }
    
    public JsonGenerator createGenerator(final Writer a1) throws IOException {
        final IOContext v1 = /*EL:1173*/this._createContext(a1, false);
        /*SL:1174*/return this._createGenerator(this._decorate(a1, v1), v1);
    }
    
    public JsonGenerator createGenerator(final File a1, final JsonEncoding a2) throws IOException {
        final OutputStream v1 = /*EL:1195*/new FileOutputStream(a1);
        final IOContext v2 = /*EL:1197*/this._createContext(v1, true);
        /*SL:1198*/v2.setEncoding(a2);
        /*SL:1199*/if (a2 == JsonEncoding.UTF8) {
            /*SL:1200*/return this._createUTF8Generator(this._decorate(v1, v2), v2);
        }
        final Writer v3 = /*EL:1202*/this._createWriter(v1, a2, v2);
        /*SL:1203*/return this._createGenerator(this._decorate(v3, v2), v2);
    }
    
    public JsonGenerator createGenerator(final DataOutput a1, final JsonEncoding a2) throws IOException {
        /*SL:1213*/return this.createGenerator(this._createDataOutputWrapper(a1), a2);
    }
    
    public JsonGenerator createGenerator(final DataOutput a1) throws IOException {
        /*SL:1225*/return this.createGenerator(this._createDataOutputWrapper(a1), JsonEncoding.UTF8);
    }
    
    @Deprecated
    public JsonGenerator createJsonGenerator(final OutputStream a1, final JsonEncoding a2) throws IOException {
        /*SL:1258*/return this.createGenerator(a1, a2);
    }
    
    @Deprecated
    public JsonGenerator createJsonGenerator(final Writer a1) throws IOException {
        /*SL:1278*/return this.createGenerator(a1);
    }
    
    @Deprecated
    public JsonGenerator createJsonGenerator(final OutputStream a1) throws IOException {
        /*SL:1291*/return this.createGenerator(a1, JsonEncoding.UTF8);
    }
    
    protected JsonParser _createParser(final InputStream a1, final IOContext a2) throws IOException {
        /*SL:1315*/return new ByteSourceJsonBootstrapper(a2, a1).constructParser(this._parserFeatures, this._objectCodec, this._byteSymbolCanonicalizer, this._rootCharSymbols, this._factoryFeatures);
    }
    
    protected JsonParser _createParser(final Reader a1, final IOContext a2) throws IOException {
        /*SL:1332*/return new ReaderBasedJsonParser(a2, this._parserFeatures, a1, this._objectCodec, this._rootCharSymbols.makeChild(this._factoryFeatures));
    }
    
    protected JsonParser _createParser(final char[] a1, final int a2, final int a3, final IOContext a4, final boolean a5) throws IOException {
        /*SL:1344*/return new ReaderBasedJsonParser(a4, this._parserFeatures, null, this._objectCodec, this._rootCharSymbols.makeChild(this._factoryFeatures), a1, a2, a2 + a3, a5);
    }
    
    protected JsonParser _createParser(final byte[] a1, final int a2, final int a3, final IOContext a4) throws IOException {
        /*SL:1362*/return new ByteSourceJsonBootstrapper(a4, a1, a2, a3).constructParser(this._parserFeatures, this._objectCodec, this._byteSymbolCanonicalizer, this._rootCharSymbols, this._factoryFeatures);
    }
    
    protected JsonParser _createParser(final DataInput a1, final IOContext a2) throws IOException {
        /*SL:1375*/this._requireJSONFactory("InputData source not (yet?) support for this format (%s)");
        final int v1 = /*EL:1378*/ByteSourceJsonBootstrapper.skipUTF8BOM(a1);
        final ByteQuadsCanonicalizer v2 = /*EL:1379*/this._byteSymbolCanonicalizer.makeChild(this._factoryFeatures);
        /*SL:1380*/return new UTF8DataInputJsonParser(a2, this._parserFeatures, a1, this._objectCodec, v2, v1);
    }
    
    protected JsonGenerator _createGenerator(final Writer a1, final IOContext a2) throws IOException {
        final WriterBasedJsonGenerator v1 = /*EL:1403*/new WriterBasedJsonGenerator(a2, this._generatorFeatures, this._objectCodec, a1);
        /*SL:1405*/if (this._characterEscapes != null) {
            /*SL:1406*/v1.setCharacterEscapes(this._characterEscapes);
        }
        final SerializableString v2 = /*EL:1408*/this._rootValueSeparator;
        /*SL:1409*/if (v2 != JsonFactory.DEFAULT_ROOT_VALUE_SEPARATOR) {
            /*SL:1410*/v1.setRootValueSeparator(v2);
        }
        /*SL:1412*/return v1;
    }
    
    protected JsonGenerator _createUTF8Generator(final OutputStream a1, final IOContext a2) throws IOException {
        final UTF8JsonGenerator v1 = /*EL:1426*/new UTF8JsonGenerator(a2, this._generatorFeatures, this._objectCodec, a1);
        /*SL:1428*/if (this._characterEscapes != null) {
            /*SL:1429*/v1.setCharacterEscapes(this._characterEscapes);
        }
        final SerializableString v2 = /*EL:1431*/this._rootValueSeparator;
        /*SL:1432*/if (v2 != JsonFactory.DEFAULT_ROOT_VALUE_SEPARATOR) {
            /*SL:1433*/v1.setRootValueSeparator(v2);
        }
        /*SL:1435*/return v1;
    }
    
    protected Writer _createWriter(final OutputStream a1, final JsonEncoding a2, final IOContext a3) throws IOException {
        /*SL:1441*/if (a2 == JsonEncoding.UTF8) {
            /*SL:1442*/return new UTF8Writer(a3, a1);
        }
        /*SL:1445*/return new OutputStreamWriter(a1, a2.getJavaName());
    }
    
    protected final InputStream _decorate(final InputStream v1, final IOContext v2) throws IOException {
        /*SL:1458*/if (this._inputDecorator != null) {
            final InputStream a1 = /*EL:1459*/this._inputDecorator.decorate(v2, v1);
            /*SL:1460*/if (a1 != null) {
                /*SL:1461*/return a1;
            }
        }
        /*SL:1464*/return v1;
    }
    
    protected final Reader _decorate(final Reader v1, final IOContext v2) throws IOException {
        /*SL:1471*/if (this._inputDecorator != null) {
            final Reader a1 = /*EL:1472*/this._inputDecorator.decorate(v2, v1);
            /*SL:1473*/if (a1 != null) {
                /*SL:1474*/return a1;
            }
        }
        /*SL:1477*/return v1;
    }
    
    protected final DataInput _decorate(final DataInput v1, final IOContext v2) throws IOException {
        /*SL:1484*/if (this._inputDecorator != null) {
            final DataInput a1 = /*EL:1485*/this._inputDecorator.decorate(v2, v1);
            /*SL:1486*/if (a1 != null) {
                /*SL:1487*/return a1;
            }
        }
        /*SL:1490*/return v1;
    }
    
    protected final OutputStream _decorate(final OutputStream v1, final IOContext v2) throws IOException {
        /*SL:1497*/if (this._outputDecorator != null) {
            final OutputStream a1 = /*EL:1498*/this._outputDecorator.decorate(v2, v1);
            /*SL:1499*/if (a1 != null) {
                /*SL:1500*/return a1;
            }
        }
        /*SL:1503*/return v1;
    }
    
    protected final Writer _decorate(final Writer v1, final IOContext v2) throws IOException {
        /*SL:1510*/if (this._outputDecorator != null) {
            final Writer a1 = /*EL:1511*/this._outputDecorator.decorate(v2, v1);
            /*SL:1512*/if (a1 != null) {
                /*SL:1513*/return a1;
            }
        }
        /*SL:1516*/return v1;
    }
    
    public BufferRecycler _getBufferRecycler() {
        /*SL:1537*/if (Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING.enabledIn(this._factoryFeatures)) {
            /*SL:1538*/return BufferRecyclers.getBufferRecycler();
        }
        /*SL:1540*/return new BufferRecycler();
    }
    
    protected IOContext _createContext(final Object a1, final boolean a2) {
        /*SL:1548*/return new IOContext(this._getBufferRecycler(), a1, a2);
    }
    
    protected OutputStream _createDataOutputWrapper(final DataOutput a1) {
        /*SL:1555*/return new DataOutputAsStream(a1);
    }
    
    protected InputStream _optimizedStreamFromURL(final URL v0) throws IOException {
        /*SL:1564*/if ("file".equals(v0.getProtocol())) {
            final String v = /*EL:1571*/v0.getHost();
            /*SL:1572*/if (v == null || v.length() == 0) {
                final String a1 = /*EL:1574*/v0.getPath();
                /*SL:1575*/if (a1.indexOf(37) < 0) {
                    /*SL:1576*/return new FileInputStream(v0.getPath());
                }
            }
        }
        /*SL:1582*/return v0.openStream();
    }
    
    private final void _requireJSONFactory(final String a1) {
        /*SL:1604*/if (!this._isJSONFactory()) {
            /*SL:1605*/throw new UnsupportedOperationException(String.format(a1, this.getFormatName()));
        }
    }
    
    private final boolean _isJSONFactory() {
        /*SL:1612*/return this.getFormatName() == "JSON";
    }
    
    static {
        DEFAULT_FACTORY_FEATURE_FLAGS = Feature.collectDefaults();
        DEFAULT_PARSER_FEATURE_FLAGS = JsonParser.Feature.collectDefaults();
        DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
        DEFAULT_ROOT_VALUE_SEPARATOR = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
    }
    
    public enum Feature
    {
        INTERN_FIELD_NAMES(true), 
        CANONICALIZE_FIELD_NAMES(true), 
        FAIL_ON_SYMBOL_HASH_OVERFLOW(true), 
        USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING(true);
        
        private final boolean _defaultState;
        
        public static int collectDefaults() {
            int n = /*EL:135*/0;
            /*SL:136*/for (final Feature v : values()) {
                /*SL:137*/if (v.enabledByDefault()) {
                    n |= v.getMask();
                }
            }
            /*SL:139*/return n;
        }
        
        private Feature(final boolean a1) {
            this._defaultState = a1;
        }
        
        public boolean enabledByDefault() {
            /*SL:144*/return this._defaultState;
        }
        
        public boolean enabledIn(final int a1) {
            /*SL:145*/return (a1 & this.getMask()) != 0x0;
        }
        
        public int getMask() {
            /*SL:146*/return 1 << this.ordinal();
        }
    }
}
