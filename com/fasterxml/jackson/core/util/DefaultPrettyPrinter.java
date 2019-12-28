package com.fasterxml.jackson.core.util;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import java.io.Serializable;
import com.fasterxml.jackson.core.PrettyPrinter;

public class DefaultPrettyPrinter implements PrettyPrinter, Instantiatable<DefaultPrettyPrinter>, Serializable
{
    private static final long serialVersionUID = 1L;
    public static final SerializedString DEFAULT_ROOT_VALUE_SEPARATOR;
    protected Indenter _arrayIndenter;
    protected Indenter _objectIndenter;
    protected final SerializableString _rootSeparator;
    protected boolean _spacesInObjectEntries;
    protected transient int _nesting;
    protected Separators _separators;
    protected String _objectFieldValueSeparatorWithSpaces;
    
    public DefaultPrettyPrinter() {
        this(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
    }
    
    public DefaultPrettyPrinter(final String a1) {
        this((a1 == null) ? null : new SerializedString(a1));
    }
    
    public DefaultPrettyPrinter(final SerializableString a1) {
        this._arrayIndenter = FixedSpaceIndenter.instance;
        this._objectIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        this._spacesInObjectEntries = true;
        this._rootSeparator = a1;
        this.withSeparators(DefaultPrettyPrinter.DEFAULT_SEPARATORS);
    }
    
    public DefaultPrettyPrinter(final DefaultPrettyPrinter a1) {
        this(a1, a1._rootSeparator);
    }
    
    public DefaultPrettyPrinter(final DefaultPrettyPrinter a1, final SerializableString a2) {
        this._arrayIndenter = FixedSpaceIndenter.instance;
        this._objectIndenter = DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
        this._spacesInObjectEntries = true;
        this._arrayIndenter = a1._arrayIndenter;
        this._objectIndenter = a1._objectIndenter;
        this._spacesInObjectEntries = a1._spacesInObjectEntries;
        this._nesting = a1._nesting;
        this._separators = a1._separators;
        this._objectFieldValueSeparatorWithSpaces = a1._objectFieldValueSeparatorWithSpaces;
        this._rootSeparator = a2;
    }
    
    public DefaultPrettyPrinter withRootSeparator(final SerializableString a1) {
        /*SL:152*/if (this._rootSeparator == a1 || (a1 != null && a1.equals(this._rootSeparator))) {
            /*SL:154*/return this;
        }
        /*SL:156*/return new DefaultPrettyPrinter(this, a1);
    }
    
    public DefaultPrettyPrinter withRootSeparator(final String a1) {
        /*SL:163*/return this.withRootSeparator((a1 == null) ? null : new SerializedString(a1));
    }
    
    public void indentArraysWith(final Indenter a1) {
        /*SL:167*/this._arrayIndenter = ((a1 == null) ? NopIndenter.instance : a1);
    }
    
    public void indentObjectsWith(final Indenter a1) {
        /*SL:171*/this._objectIndenter = ((a1 == null) ? NopIndenter.instance : a1);
    }
    
    public DefaultPrettyPrinter withArrayIndenter(Indenter a1) {
        /*SL:178*/if (a1 == null) {
            /*SL:179*/a1 = NopIndenter.instance;
        }
        /*SL:181*/if (this._arrayIndenter == a1) {
            /*SL:182*/return this;
        }
        final DefaultPrettyPrinter v1 = /*EL:184*/new DefaultPrettyPrinter(this);
        /*SL:185*/v1._arrayIndenter = a1;
        /*SL:186*/return v1;
    }
    
    public DefaultPrettyPrinter withObjectIndenter(Indenter a1) {
        /*SL:193*/if (a1 == null) {
            /*SL:194*/a1 = NopIndenter.instance;
        }
        /*SL:196*/if (this._objectIndenter == a1) {
            /*SL:197*/return this;
        }
        final DefaultPrettyPrinter v1 = /*EL:199*/new DefaultPrettyPrinter(this);
        /*SL:200*/v1._objectIndenter = a1;
        /*SL:201*/return v1;
    }
    
    public DefaultPrettyPrinter withSpacesInObjectEntries() {
        /*SL:213*/return this._withSpaces(true);
    }
    
    public DefaultPrettyPrinter withoutSpacesInObjectEntries() {
        /*SL:225*/return this._withSpaces(false);
    }
    
    protected DefaultPrettyPrinter _withSpaces(final boolean a1) {
        /*SL:230*/if (this._spacesInObjectEntries == a1) {
            /*SL:231*/return this;
        }
        final DefaultPrettyPrinter v1 = /*EL:233*/new DefaultPrettyPrinter(this);
        /*SL:234*/v1._spacesInObjectEntries = a1;
        /*SL:235*/return v1;
    }
    
    public DefaultPrettyPrinter withSeparators(final Separators a1) {
        /*SL:242*/this._separators = a1;
        /*SL:243*/this._objectFieldValueSeparatorWithSpaces = " " + a1.getObjectFieldValueSeparator() + " ";
        /*SL:244*/return this;
    }
    
    @Override
    public DefaultPrettyPrinter createInstance() {
        /*SL:255*/return new DefaultPrettyPrinter(this);
    }
    
    @Override
    public void writeRootValueSeparator(final JsonGenerator a1) throws IOException {
        /*SL:267*/if (this._rootSeparator != null) {
            /*SL:268*/a1.writeRaw(this._rootSeparator);
        }
    }
    
    @Override
    public void writeStartObject(final JsonGenerator a1) throws IOException {
        /*SL:275*/a1.writeRaw('{');
        /*SL:276*/if (!this._objectIndenter.isInline()) {
            /*SL:277*/++this._nesting;
        }
    }
    
    @Override
    public void beforeObjectEntries(final JsonGenerator a1) throws IOException {
        /*SL:284*/this._objectIndenter.writeIndentation(a1, this._nesting);
    }
    
    @Override
    public void writeObjectFieldValueSeparator(final JsonGenerator a1) throws IOException {
        /*SL:299*/if (this._spacesInObjectEntries) {
            /*SL:300*/a1.writeRaw(this._objectFieldValueSeparatorWithSpaces);
        }
        else {
            /*SL:302*/a1.writeRaw(this._separators.getObjectFieldValueSeparator());
        }
    }
    
    @Override
    public void writeObjectEntrySeparator(final JsonGenerator a1) throws IOException {
        /*SL:318*/a1.writeRaw(this._separators.getObjectEntrySeparator());
        /*SL:319*/this._objectIndenter.writeIndentation(a1, this._nesting);
    }
    
    @Override
    public void writeEndObject(final JsonGenerator a1, final int a2) throws IOException {
        /*SL:325*/if (!this._objectIndenter.isInline()) {
            /*SL:326*/--this._nesting;
        }
        /*SL:328*/if (a2 > 0) {
            /*SL:329*/this._objectIndenter.writeIndentation(a1, this._nesting);
        }
        else {
            /*SL:331*/a1.writeRaw(' ');
        }
        /*SL:333*/a1.writeRaw('}');
    }
    
    @Override
    public void writeStartArray(final JsonGenerator a1) throws IOException {
        /*SL:339*/if (!this._arrayIndenter.isInline()) {
            /*SL:340*/++this._nesting;
        }
        /*SL:342*/a1.writeRaw('[');
    }
    
    @Override
    public void beforeArrayValues(final JsonGenerator a1) throws IOException {
        /*SL:347*/this._arrayIndenter.writeIndentation(a1, this._nesting);
    }
    
    @Override
    public void writeArrayValueSeparator(final JsonGenerator a1) throws IOException {
        /*SL:362*/a1.writeRaw(this._separators.getArrayValueSeparator());
        /*SL:363*/this._arrayIndenter.writeIndentation(a1, this._nesting);
    }
    
    @Override
    public void writeEndArray(final JsonGenerator a1, final int a2) throws IOException {
        /*SL:369*/if (!this._arrayIndenter.isInline()) {
            /*SL:370*/--this._nesting;
        }
        /*SL:372*/if (a2 > 0) {
            /*SL:373*/this._arrayIndenter.writeIndentation(a1, this._nesting);
        }
        else {
            /*SL:375*/a1.writeRaw(' ');
        }
        /*SL:377*/a1.writeRaw(']');
    }
    
    static {
        DEFAULT_ROOT_VALUE_SEPARATOR = new SerializedString(" ");
    }
    
    public static class NopIndenter implements Indenter, Serializable
    {
        public static final NopIndenter instance;
        
        @Override
        public void writeIndentation(final JsonGenerator a1, final int a2) throws IOException {
        }
        
        @Override
        public boolean isInline() {
            /*SL:398*/return true;
        }
        
        static {
            instance = new NopIndenter();
        }
    }
    
    public static class FixedSpaceIndenter extends NopIndenter
    {
        public static final FixedSpaceIndenter instance;
        
        @Override
        public void writeIndentation(final JsonGenerator a1, final int a2) throws IOException {
            /*SL:413*/a1.writeRaw(' ');
        }
        
        @Override
        public boolean isInline() {
            /*SL:417*/return true;
        }
        
        static {
            instance = new FixedSpaceIndenter();
        }
    }
    
    public interface Indenter
    {
        void writeIndentation(JsonGenerator p0, int p1) throws IOException;
        
        boolean isInline();
    }
}
