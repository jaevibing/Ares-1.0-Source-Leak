package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.util.RequestPayload;

public class JsonParseException extends JsonProcessingException
{
    private static final long serialVersionUID = 2L;
    protected transient JsonParser _processor;
    protected RequestPayload _requestPayload;
    
    public JsonParseException(final String a1, final JsonLocation a2) {
        super(a1, a2);
    }
    
    public JsonParseException(final String a1, final JsonLocation a2, final Throwable a3) {
        super(a1, a2, a3);
    }
    
    public JsonParseException(final JsonParser a1, final String a2) {
        super(a2, (a1 == null) ? null : a1.getCurrentLocation());
        this._processor = a1;
    }
    
    public JsonParseException(final JsonParser a1, final String a2, final Throwable a3) {
        super(a2, (a1 == null) ? null : a1.getCurrentLocation(), a3);
        this._processor = a1;
    }
    
    public JsonParseException(final JsonParser a1, final String a2, final JsonLocation a3) {
        super(a2, a3);
        this._processor = a1;
    }
    
    public JsonParseException(final JsonParser a1, final String a2, final JsonLocation a3, final Throwable a4) {
        super(a2, a3, a4);
        this._processor = a1;
    }
    
    public JsonParseException withParser(final JsonParser a1) {
        /*SL:86*/this._processor = a1;
        /*SL:87*/return this;
    }
    
    public JsonParseException withRequestPayload(final RequestPayload a1) {
        /*SL:99*/this._requestPayload = a1;
        /*SL:100*/return this;
    }
    
    @Override
    public JsonParser getProcessor() {
        /*SL:105*/return this._processor;
    }
    
    public RequestPayload getRequestPayload() {
        /*SL:117*/return this._requestPayload;
    }
    
    public String getRequestPayloadAsString() {
        /*SL:129*/return (this._requestPayload != null) ? this._requestPayload.toString() : null;
    }
    
    @Override
    public String getMessage() {
        String v1 = /*EL:137*/super.getMessage();
        /*SL:138*/if (this._requestPayload != null) {
            /*SL:139*/v1 = v1 + "\nRequest payload : " + this._requestPayload.toString();
        }
        /*SL:141*/return v1;
    }
}
