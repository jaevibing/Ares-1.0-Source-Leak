package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AutoResizeDimensionsRequest extends GenericJson
{
    @Key
    private DimensionRange dimensions;
    
    public DimensionRange getDimensions() {
        /*SL:49*/return this.dimensions;
    }
    
    public AutoResizeDimensionsRequest setDimensions(final DimensionRange dimensions) {
        /*SL:57*/this.dimensions = dimensions;
        /*SL:58*/return this;
    }
    
    public AutoResizeDimensionsRequest set(final String a1, final Object a2) {
        /*SL:63*/return (AutoResizeDimensionsRequest)super.set(a1, a2);
    }
    
    public AutoResizeDimensionsRequest clone() {
        /*SL:68*/return (AutoResizeDimensionsRequest)super.clone();
    }
}
