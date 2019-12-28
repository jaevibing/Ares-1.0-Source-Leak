package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class RandomizeRangeRequest extends GenericJson
{
    @Key
    private GridRange range;
    
    public GridRange getRange() {
        /*SL:48*/return this.range;
    }
    
    public RandomizeRangeRequest setRange(final GridRange range) {
        /*SL:56*/this.range = range;
        /*SL:57*/return this;
    }
    
    public RandomizeRangeRequest set(final String a1, final Object a2) {
        /*SL:62*/return (RandomizeRangeRequest)super.set(a1, a2);
    }
    
    public RandomizeRangeRequest clone() {
        /*SL:67*/return (RandomizeRangeRequest)super.clone();
    }
}
