package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class MoveDimensionRequest extends GenericJson
{
    @Key
    private Integer destinationIndex;
    @Key
    private DimensionRange source;
    
    public Integer getDestinationIndex() {
        /*SL:69*/return this.destinationIndex;
    }
    
    public MoveDimensionRequest setDestinationIndex(final Integer destinationIndex) {
        /*SL:84*/this.destinationIndex = destinationIndex;
        /*SL:85*/return this;
    }
    
    public DimensionRange getSource() {
        /*SL:93*/return this.source;
    }
    
    public MoveDimensionRequest setSource(final DimensionRange source) {
        /*SL:101*/this.source = source;
        /*SL:102*/return this;
    }
    
    public MoveDimensionRequest set(final String a1, final Object a2) {
        /*SL:107*/return (MoveDimensionRequest)super.set(a1, a2);
    }
    
    public MoveDimensionRequest clone() {
        /*SL:112*/return (MoveDimensionRequest)super.clone();
    }
}
