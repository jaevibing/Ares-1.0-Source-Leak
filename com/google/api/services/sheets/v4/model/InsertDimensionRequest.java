package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class InsertDimensionRequest extends GenericJson
{
    @Key
    private Boolean inheritFromBefore;
    @Key
    private DimensionRange range;
    
    public Boolean getInheritFromBefore() {
        /*SL:71*/return this.inheritFromBefore;
    }
    
    public InsertDimensionRequest setInheritFromBefore(final Boolean inheritFromBefore) {
        /*SL:87*/this.inheritFromBefore = inheritFromBefore;
        /*SL:88*/return this;
    }
    
    public DimensionRange getRange() {
        /*SL:96*/return this.range;
    }
    
    public InsertDimensionRequest setRange(final DimensionRange range) {
        /*SL:104*/this.range = range;
        /*SL:105*/return this;
    }
    
    public InsertDimensionRequest set(final String a1, final Object a2) {
        /*SL:110*/return (InsertDimensionRequest)super.set(a1, a2);
    }
    
    public InsertDimensionRequest clone() {
        /*SL:115*/return (InsertDimensionRequest)super.clone();
    }
}
