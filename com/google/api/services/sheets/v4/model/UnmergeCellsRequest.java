package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UnmergeCellsRequest extends GenericJson
{
    @Key
    private GridRange range;
    
    public GridRange getRange() {
        /*SL:50*/return this.range;
    }
    
    public UnmergeCellsRequest setRange(final GridRange range) {
        /*SL:59*/this.range = range;
        /*SL:60*/return this;
    }
    
    public UnmergeCellsRequest set(final String a1, final Object a2) {
        /*SL:65*/return (UnmergeCellsRequest)super.set(a1, a2);
    }
    
    public UnmergeCellsRequest clone() {
        /*SL:70*/return (UnmergeCellsRequest)super.clone();
    }
}
