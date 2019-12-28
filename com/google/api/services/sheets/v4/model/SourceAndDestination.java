package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class SourceAndDestination extends GenericJson
{
    @Key
    private String dimension;
    @Key
    private Integer fillLength;
    @Key
    private GridRange source;
    
    public String getDimension() {
        /*SL:64*/return this.dimension;
    }
    
    public SourceAndDestination setDimension(final String dimension) {
        /*SL:72*/this.dimension = dimension;
        /*SL:73*/return this;
    }
    
    public Integer getFillLength() {
        /*SL:83*/return this.fillLength;
    }
    
    public SourceAndDestination setFillLength(final Integer fillLength) {
        /*SL:93*/this.fillLength = fillLength;
        /*SL:94*/return this;
    }
    
    public GridRange getSource() {
        /*SL:102*/return this.source;
    }
    
    public SourceAndDestination setSource(final GridRange source) {
        /*SL:110*/this.source = source;
        /*SL:111*/return this;
    }
    
    public SourceAndDestination set(final String a1, final Object a2) {
        /*SL:116*/return (SourceAndDestination)super.set(a1, a2);
    }
    
    public SourceAndDestination clone() {
        /*SL:121*/return (SourceAndDestination)super.clone();
    }
}
