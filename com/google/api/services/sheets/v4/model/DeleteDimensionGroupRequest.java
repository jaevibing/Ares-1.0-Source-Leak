package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteDimensionGroupRequest extends GenericJson
{
    @Key
    private DimensionRange range;
    
    public DimensionRange getRange() {
        /*SL:52*/return this.range;
    }
    
    public DeleteDimensionGroupRequest setRange(final DimensionRange range) {
        /*SL:60*/this.range = range;
        /*SL:61*/return this;
    }
    
    public DeleteDimensionGroupRequest set(final String a1, final Object a2) {
        /*SL:66*/return (DeleteDimensionGroupRequest)super.set(a1, a2);
    }
    
    public DeleteDimensionGroupRequest clone() {
        /*SL:71*/return (DeleteDimensionGroupRequest)super.clone();
    }
}
