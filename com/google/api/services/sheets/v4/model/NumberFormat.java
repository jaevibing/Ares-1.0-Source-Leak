package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class NumberFormat extends GenericJson
{
    @Key
    private String pattern;
    @Key
    private String type;
    
    public String getPattern() {
        /*SL:59*/return this.pattern;
    }
    
    public NumberFormat setPattern(final String pattern) {
        /*SL:69*/this.pattern = pattern;
        /*SL:70*/return this;
    }
    
    public String getType() {
        /*SL:78*/return this.type;
    }
    
    public NumberFormat setType(final String type) {
        /*SL:86*/this.type = type;
        /*SL:87*/return this;
    }
    
    public NumberFormat set(final String a1, final Object a2) {
        /*SL:92*/return (NumberFormat)super.set(a1, a2);
    }
    
    public NumberFormat clone() {
        /*SL:97*/return (NumberFormat)super.clone();
    }
}
