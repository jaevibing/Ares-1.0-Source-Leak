package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteBandingRequest extends GenericJson
{
    @Key
    private Integer bandedRangeId;
    
    public Integer getBandedRangeId() {
        /*SL:48*/return this.bandedRangeId;
    }
    
    public DeleteBandingRequest setBandedRangeId(final Integer bandedRangeId) {
        /*SL:56*/this.bandedRangeId = bandedRangeId;
        /*SL:57*/return this;
    }
    
    public DeleteBandingRequest set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteBandingRequest)super.set(a1, a2);
    }
    
    public DeleteBandingRequest clone() {
        /*SL:67*/return (DeleteBandingRequest)super.clone();
    }
}
