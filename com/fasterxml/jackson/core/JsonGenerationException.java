package com.fasterxml.jackson.core;

public class JsonGenerationException extends JsonProcessingException
{
    private static final long serialVersionUID = 123L;
    protected transient JsonGenerator _processor;
    
    public JsonGenerationException(final Throwable a1) {
        super(a1);
    }
    
    public JsonGenerationException(final String a1) {
        super(a1, (JsonLocation)null);
    }
    
    public JsonGenerationException(final String a1, final Throwable a2) {
        super(a1, null, a2);
    }
    
    public JsonGenerationException(final Throwable a1, final JsonGenerator a2) {
        super(a1);
        this._processor = a2;
    }
    
    public JsonGenerationException(final String a1, final JsonGenerator a2) {
        super(a1, (JsonLocation)null);
        this._processor = a2;
    }
    
    public JsonGenerationException(final String a1, final Throwable a2, final JsonGenerator a3) {
        super(a1, null, a2);
        this._processor = a3;
    }
    
    public JsonGenerationException withGenerator(final JsonGenerator a1) {
        /*SL:67*/this._processor = a1;
        /*SL:68*/return this;
    }
    
    @Override
    public JsonGenerator getProcessor() {
        /*SL:72*/return this._processor;
    }
}
