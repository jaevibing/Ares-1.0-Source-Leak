package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddDimensionGroupRequest extends GenericJson
{
    @Key
    private DimensionRange range;
    
    public DimensionRange getRange() {
        /*SL:59*/return this.range;
    }
    
    public AddDimensionGroupRequest setRange(final DimensionRange range) {
        /*SL:67*/this.range = range;
        /*SL:68*/return this;
    }
    
    public AddDimensionGroupRequest set(final String a1, final Object a2) {
        /*SL:73*/return (AddDimensionGroupRequest)super.set(a1, a2);
    }
    
    public AddDimensionGroupRequest clone() {
        /*SL:78*/return (AddDimensionGroupRequest)super.clone();
    }
}
