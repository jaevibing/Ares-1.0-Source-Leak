package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteRangeRequest extends GenericJson
{
    @Key
    private GridRange range;
    @Key
    private String shiftDimension;
    
    public GridRange getRange() {
        /*SL:57*/return this.range;
    }
    
    public DeleteRangeRequest setRange(final GridRange range) {
        /*SL:65*/this.range = range;
        /*SL:66*/return this;
    }
    
    public String getShiftDimension() {
        /*SL:76*/return this.shiftDimension;
    }
    
    public DeleteRangeRequest setShiftDimension(final String shiftDimension) {
        /*SL:86*/this.shiftDimension = shiftDimension;
        /*SL:87*/return this;
    }
    
    public DeleteRangeRequest set(final String a1, final Object a2) {
        /*SL:92*/return (DeleteRangeRequest)super.set(a1, a2);
    }
    
    public DeleteRangeRequest clone() {
        /*SL:97*/return (DeleteRangeRequest)super.clone();
    }
}
