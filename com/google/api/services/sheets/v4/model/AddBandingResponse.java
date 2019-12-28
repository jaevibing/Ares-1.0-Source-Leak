package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddBandingResponse extends GenericJson
{
    @Key
    private BandedRange bandedRange;
    
    public BandedRange getBandedRange() {
        /*SL:48*/return this.bandedRange;
    }
    
    public AddBandingResponse setBandedRange(final BandedRange bandedRange) {
        /*SL:56*/this.bandedRange = bandedRange;
        /*SL:57*/return this;
    }
    
    public AddBandingResponse set(final String a1, final Object a2) {
        /*SL:62*/return (AddBandingResponse)super.set(a1, a2);
    }
    
    public AddBandingResponse clone() {
        /*SL:67*/return (AddBandingResponse)super.clone();
    }
}
