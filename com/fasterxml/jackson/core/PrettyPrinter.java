package com.fasterxml.jackson.core;

import java.io.IOException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.Separators;

public interface PrettyPrinter
{
    public static final Separators DEFAULT_SEPARATORS = Separators.createDefaultInstance();
    public static final SerializedString DEFAULT_ROOT_VALUE_SEPARATOR = new SerializedString(" ");
    
    void writeRootValueSeparator(JsonGenerator p0) throws IOException;
    
    void writeStartObject(JsonGenerator p0) throws IOException;
    
    void writeEndObject(JsonGenerator p0, int p1) throws IOException;
    
    void writeObjectEntrySeparator(JsonGenerator p0) throws IOException;
    
    void writeObjectFieldValueSeparator(JsonGenerator p0) throws IOException;
    
    void writeStartArray(JsonGenerator p0) throws IOException;
    
    void writeEndArray(JsonGenerator p0, int p1) throws IOException;
    
    void writeArrayValueSeparator(JsonGenerator p0) throws IOException;
    
    void beforeArrayValues(JsonGenerator p0) throws IOException;
    
    void beforeObjectEntries(JsonGenerator p0) throws IOException;
}
