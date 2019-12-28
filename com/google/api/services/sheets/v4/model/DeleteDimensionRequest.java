package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteDimensionRequest extends GenericJson
{
    @Key
    private DimensionRange range;
    
    public DimensionRange getRange() {
        /*SL:48*/return this.range;
    }
    
    public DeleteDimensionRequest setRange(final DimensionRange range) {
        /*SL:56*/this.range = range;
        /*SL:57*/return this;
    }
    
    public DeleteDimensionRequest set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteDimensionRequest)super.set(a1, a2);
    }
    
    public DeleteDimensionRequest clone() {
        /*SL:67*/return (DeleteDimensionRequest)super.clone();
    }
}
