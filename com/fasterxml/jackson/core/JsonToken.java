package com.fasterxml.jackson.core;

public enum JsonToken
{
    NOT_AVAILABLE((String)null, -1), 
    START_OBJECT("{", 1), 
    END_OBJECT("}", 2), 
    START_ARRAY("[", 3), 
    END_ARRAY("]", 4), 
    FIELD_NAME((String)null, 5), 
    VALUE_EMBEDDED_OBJECT((String)null, 12), 
    VALUE_STRING((String)null, 6), 
    VALUE_NUMBER_INT((String)null, 7), 
    VALUE_NUMBER_FLOAT((String)null, 8), 
    VALUE_TRUE("true", 9), 
    VALUE_FALSE("false", 10), 
    VALUE_NULL("null", 11);
    
    final String _serialized;
    final char[] _serializedChars;
    final byte[] _serializedBytes;
    final int _id;
    final boolean _isStructStart;
    final boolean _isStructEnd;
    final boolean _isNumber;
    final boolean _isBoolean;
    final boolean _isScalar;
    
    private JsonToken(final String a4, final int v1) {
        super(s, n);
        if (a4 == null) {
            this._serialized = null;
            this._serializedChars = null;
            this._serializedBytes = null;
        }
        else {
            this._serialized = a4;
            this._serializedChars = a4.toCharArray();
            final int a5 = this._serializedChars.length;
            this._serializedBytes = new byte[a5];
            for (int a6 = 0; a6 < a5; ++a6) {
                this._serializedBytes[a6] = (byte)this._serializedChars[a6];
            }
        }
        this._id = v1;
        this._isBoolean = (v1 == 10 || v1 == 9);
        this._isNumber = (v1 == 7 || v1 == 8);
        this._isStructStart = (v1 == 1 || v1 == 3);
        this._isStructEnd = (v1 == 2 || v1 == 4);
        this._isScalar = (!this._isStructStart && !this._isStructEnd && v1 != 5 && v1 != -1);
    }
    
    public final int id() {
        /*SL:169*/return this._id;
    }
    
    public final String asString() {
        /*SL:171*/return this._serialized;
    }
    
    public final char[] asCharArray() {
        /*SL:172*/return this._serializedChars;
    }
    
    public final byte[] asByteArray() {
        /*SL:173*/return this._serializedBytes;
    }
    
    public final boolean isNumeric() {
        /*SL:175*/return this._isNumber;
    }
    
    public final boolean isStructStart() {
        /*SL:185*/return this._isStructStart;
    }
    
    public final boolean isStructEnd() {
        /*SL:195*/return this._isStructEnd;
    }
    
    public final boolean isScalarValue() {
        /*SL:202*/return this._isScalar;
    }
    
    public final boolean isBoolean() {
        /*SL:203*/return this._isBoolean;
    }
}
