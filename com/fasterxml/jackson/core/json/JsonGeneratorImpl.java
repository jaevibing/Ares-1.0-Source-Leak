package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.io.CharTypes;
import java.io.IOException;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.base.GeneratorBase;

public abstract class JsonGeneratorImpl extends GeneratorBase
{
    protected static final int[] sOutputEscapes;
    protected final IOContext _ioContext;
    protected int[] _outputEscapes;
    protected int _maximumNonEscapedChar;
    protected CharacterEscapes _characterEscapes;
    protected SerializableString _rootValueSeparator;
    protected boolean _cfgUnqNames;
    
    public JsonGeneratorImpl(final IOContext a1, final int a2, final ObjectCodec a3) {
        super(a2, a3);
        this._outputEscapes = JsonGeneratorImpl.sOutputEscapes;
        this._rootValueSeparator = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
        this._ioContext = a1;
        if (Feature.ESCAPE_NON_ASCII.enabledIn(a2)) {
            this._maximumNonEscapedChar = 127;
        }
        this._cfgUnqNames = !Feature.QUOTE_FIELD_NAMES.enabledIn(a2);
    }
    
    @Override
    public Version version() {
        /*SL:120*/return VersionUtil.versionFor(this.getClass());
    }
    
    @Override
    public JsonGenerator enable(final Feature a1) {
        /*SL:131*/super.enable(a1);
        /*SL:132*/if (a1 == Feature.QUOTE_FIELD_NAMES) {
            /*SL:133*/this._cfgUnqNames = false;
        }
        /*SL:135*/return this;
    }
    
    @Override
    public JsonGenerator disable(final Feature a1) {
        /*SL:140*/super.disable(a1);
        /*SL:141*/if (a1 == Feature.QUOTE_FIELD_NAMES) {
            /*SL:142*/this._cfgUnqNames = true;
        }
        /*SL:144*/return this;
    }
    
    @Override
    protected void _checkStdFeatureChanges(final int a1, final int a2) {
        /*SL:149*/super._checkStdFeatureChanges(a1, a2);
        /*SL:150*/this._cfgUnqNames = !Feature.QUOTE_FIELD_NAMES.enabledIn(a1);
    }
    
    @Override
    public JsonGenerator setHighestNonEscapedChar(final int a1) {
        /*SL:155*/this._maximumNonEscapedChar = ((a1 < 0) ? 0 : a1);
        /*SL:156*/return this;
    }
    
    @Override
    public int getHighestEscapedChar() {
        /*SL:161*/return this._maximumNonEscapedChar;
    }
    
    @Override
    public JsonGenerator setCharacterEscapes(final CharacterEscapes a1) {
        /*SL:167*/this._characterEscapes = a1;
        /*SL:168*/if (a1 == null) {
            /*SL:169*/this._outputEscapes = JsonGeneratorImpl.sOutputEscapes;
        }
        else {
            /*SL:171*/this._outputEscapes = a1.getEscapeCodesForAscii();
        }
        /*SL:173*/return this;
    }
    
    @Override
    public CharacterEscapes getCharacterEscapes() {
        /*SL:182*/return this._characterEscapes;
    }
    
    @Override
    public JsonGenerator setRootValueSeparator(final SerializableString a1) {
        /*SL:187*/this._rootValueSeparator = a1;
        /*SL:188*/return this;
    }
    
    @Override
    public final void writeStringField(final String a1, final String a2) throws IOException {
        /*SL:202*/this.writeFieldName(a1);
        /*SL:203*/this.writeString(a2);
    }
    
    protected void _verifyPrettyValueWrite(final String a1, final int a2) throws IOException {
        /*SL:215*/switch (a2) {
            case 1: {
                /*SL:217*/this._cfgPrettyPrinter.writeArrayValueSeparator(this);
                /*SL:218*/break;
            }
            case 2: {
                /*SL:220*/this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
                /*SL:221*/break;
            }
            case 3: {
                /*SL:223*/this._cfgPrettyPrinter.writeRootValueSeparator(this);
                /*SL:224*/break;
            }
            case 0: {
                /*SL:227*/if (this._writeContext.inArray()) {
                    /*SL:228*/this._cfgPrettyPrinter.beforeArrayValues(this);
                    break;
                }
                /*SL:229*/if (this._writeContext.inObject()) {
                    /*SL:230*/this._cfgPrettyPrinter.beforeObjectEntries(this);
                    break;
                }
                break;
            }
            case 5: {
                /*SL:234*/this._reportCantWriteValueExpectName(a1);
                /*SL:235*/break;
            }
            default: {
                /*SL:237*/this._throwInternal();
                break;
            }
        }
    }
    
    protected void _reportCantWriteValueExpectName(final String a1) throws IOException {
        /*SL:244*/this._reportError(String.format("Can not %s, expecting field name (context: %s)", a1, this._writeContext.typeDesc()));
    }
    
    static {
        sOutputEscapes = CharTypes.get7BitOutputEscapes();
    }
}
