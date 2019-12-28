package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ChartData extends GenericJson
{
    @Key
    private ChartSourceRange sourceRange;
    
    public ChartSourceRange getSourceRange() {
        /*SL:48*/return this.sourceRange;
    }
    
    public ChartData setSourceRange(final ChartSourceRange sourceRange) {
        /*SL:56*/this.sourceRange = sourceRange;
        /*SL:57*/return this;
    }
    
    public ChartData set(final String a1, final Object a2) {
        /*SL:62*/return (ChartData)super.set(a1, a2);
    }
    
    public ChartData clone() {
        /*SL:67*/return (ChartData)super.clone();
    }
}
