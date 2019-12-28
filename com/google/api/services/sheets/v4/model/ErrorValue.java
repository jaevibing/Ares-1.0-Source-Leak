package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ErrorValue extends GenericJson
{
    @Key
    private String message;
    @Key
    private String type;
    
    public String getMessage() {
        /*SL:55*/return this.message;
    }
    
    public ErrorValue setMessage(final String message) {
        /*SL:63*/this.message = message;
        /*SL:64*/return this;
    }
    
    public String getType() {
        /*SL:72*/return this.type;
    }
    
    public ErrorValue setType(final String type) {
        /*SL:80*/this.type = type;
        /*SL:81*/return this;
    }
    
    public ErrorValue set(final String a1, final Object a2) {
        /*SL:86*/return (ErrorValue)super.set(a1, a2);
    }
    
    public ErrorValue clone() {
        /*SL:91*/return (ErrorValue)super.clone();
    }
}
