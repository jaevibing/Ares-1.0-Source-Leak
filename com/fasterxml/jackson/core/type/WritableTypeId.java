package com.fasterxml.jackson.core.type;

import com.fasterxml.jackson.core.JsonToken;

public class WritableTypeId
{
    public Object forValue;
    public Class<?> forValueType;
    public Object id;
    public String asProperty;
    public Inclusion include;
    public JsonToken valueShape;
    public boolean wrapperWritten;
    public Object extra;
    
    public WritableTypeId() {
    }
    
    public WritableTypeId(final Object a1, final JsonToken a2) {
        this(a1, a2, null);
    }
    
    public WritableTypeId(final Object a1, final Class<?> a2, final JsonToken a3) {
        this(a1, a3, null);
        this.forValueType = a2;
    }
    
    public WritableTypeId(final Object a1, final JsonToken a2, final Object a3) {
        this.forValue = a1;
        this.id = a3;
        this.valueShape = a2;
    }
    
    public enum Inclusion
    {
        WRAPPER_ARRAY, 
        WRAPPER_OBJECT, 
        METADATA_PROPERTY, 
        PAYLOAD_PROPERTY, 
        PARENT_PROPERTY;
        
        public boolean requiresObjectContext() {
            /*SL:95*/return this == Inclusion.METADATA_PROPERTY || this == Inclusion.PAYLOAD_PROPERTY;
        }
    }
}
