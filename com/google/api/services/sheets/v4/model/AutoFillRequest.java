package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AutoFillRequest extends GenericJson
{
    @Key
    private GridRange range;
    @Key
    private SourceAndDestination sourceAndDestination;
    @Key
    private Boolean useAlternateSeries;
    
    public GridRange getRange() {
        /*SL:66*/return this.range;
    }
    
    public AutoFillRequest setRange(final GridRange range) {
        /*SL:75*/this.range = range;
        /*SL:76*/return this;
    }
    
    public SourceAndDestination getSourceAndDestination() {
        /*SL:85*/return this.sourceAndDestination;
    }
    
    public AutoFillRequest setSourceAndDestination(final SourceAndDestination sourceAndDestination) {
        /*SL:94*/this.sourceAndDestination = sourceAndDestination;
        /*SL:95*/return this;
    }
    
    public Boolean getUseAlternateSeries() {
        /*SL:104*/return this.useAlternateSeries;
    }
    
    public AutoFillRequest setUseAlternateSeries(final Boolean useAlternateSeries) {
        /*SL:113*/this.useAlternateSeries = useAlternateSeries;
        /*SL:114*/return this;
    }
    
    public AutoFillRequest set(final String a1, final Object a2) {
        /*SL:119*/return (AutoFillRequest)super.set(a1, a2);
    }
    
    public AutoFillRequest clone() {
        /*SL:124*/return (AutoFillRequest)super.clone();
    }
}
