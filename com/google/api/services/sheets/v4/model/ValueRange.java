package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ValueRange extends GenericJson
{
    @Key
    private String majorDimension;
    @Key
    private String range;
    @Key
    private List<List<Object>> values;
    
    public String getMajorDimension() {
        /*SL:92*/return this.majorDimension;
    }
    
    public ValueRange setMajorDimension(final String majorDimension) {
        /*SL:110*/this.majorDimension = majorDimension;
        /*SL:111*/return this;
    }
    
    public String getRange() {
        /*SL:122*/return this.range;
    }
    
    public ValueRange setRange(final String range) {
        /*SL:133*/this.range = range;
        /*SL:134*/return this;
    }
    
    public List<List<Object>> getValues() {
        /*SL:149*/return this.values;
    }
    
    public ValueRange setValues(final List<List<Object>> values) {
        /*SL:164*/this.values = values;
        /*SL:165*/return this;
    }
    
    public ValueRange set(final String a1, final Object a2) {
        /*SL:170*/return (ValueRange)super.set(a1, a2);
    }
    
    public ValueRange clone() {
        /*SL:175*/return (ValueRange)super.clone();
    }
}
