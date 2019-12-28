package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateBandingRequest extends GenericJson
{
    @Key
    private BandedRange bandedRange;
    @Key
    private String fields;
    
    public BandedRange getBandedRange() {
        /*SL:57*/return this.bandedRange;
    }
    
    public UpdateBandingRequest setBandedRange(final BandedRange bandedRange) {
        /*SL:65*/this.bandedRange = bandedRange;
        /*SL:66*/return this;
    }
    
    public String getFields() {
        /*SL:76*/return this.fields;
    }
    
    public UpdateBandingRequest setFields(final String fields) {
        /*SL:86*/this.fields = fields;
        /*SL:87*/return this;
    }
    
    public UpdateBandingRequest set(final String a1, final Object a2) {
        /*SL:92*/return (UpdateBandingRequest)super.set(a1, a2);
    }
    
    public UpdateBandingRequest clone() {
        /*SL:97*/return (UpdateBandingRequest)super.clone();
    }
}
