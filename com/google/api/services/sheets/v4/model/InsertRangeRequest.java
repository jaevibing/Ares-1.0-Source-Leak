package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class InsertRangeRequest extends GenericJson
{
    @Key
    private GridRange range;
    @Key
    private String shiftDimension;
    
    public GridRange getRange() {
        /*SL:56*/return this.range;
    }
    
    public InsertRangeRequest setRange(final GridRange range) {
        /*SL:64*/this.range = range;
        /*SL:65*/return this;
    }
    
    public String getShiftDimension() {
        /*SL:74*/return this.shiftDimension;
    }
    
    public InsertRangeRequest setShiftDimension(final String shiftDimension) {
        /*SL:83*/this.shiftDimension = shiftDimension;
        /*SL:84*/return this;
    }
    
    public InsertRangeRequest set(final String a1, final Object a2) {
        /*SL:89*/return (InsertRangeRequest)super.set(a1, a2);
    }
    
    public InsertRangeRequest clone() {
        /*SL:94*/return (InsertRangeRequest)super.clone();
    }
}
