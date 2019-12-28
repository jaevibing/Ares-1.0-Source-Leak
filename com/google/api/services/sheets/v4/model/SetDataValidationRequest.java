package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class SetDataValidationRequest extends GenericJson
{
    @Key
    private GridRange range;
    @Key
    private DataValidationRule rule;
    
    public GridRange getRange() {
        /*SL:57*/return this.range;
    }
    
    public SetDataValidationRequest setRange(final GridRange range) {
        /*SL:65*/this.range = range;
        /*SL:66*/return this;
    }
    
    public DataValidationRule getRule() {
        /*SL:75*/return this.rule;
    }
    
    public SetDataValidationRequest setRule(final DataValidationRule rule) {
        /*SL:84*/this.rule = rule;
        /*SL:85*/return this;
    }
    
    public SetDataValidationRequest set(final String a1, final Object a2) {
        /*SL:90*/return (SetDataValidationRequest)super.set(a1, a2);
    }
    
    public SetDataValidationRequest clone() {
        /*SL:95*/return (SetDataValidationRequest)super.clone();
    }
}
