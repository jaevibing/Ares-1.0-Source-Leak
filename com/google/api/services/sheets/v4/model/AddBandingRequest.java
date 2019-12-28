package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddBandingRequest extends GenericJson
{
    @Key
    private BandedRange bandedRange;
    
    public BandedRange getBandedRange() {
        /*SL:50*/return this.bandedRange;
    }
    
    public AddBandingRequest setBandedRange(final BandedRange bandedRange) {
        /*SL:59*/this.bandedRange = bandedRange;
        /*SL:60*/return this;
    }
    
    public AddBandingRequest set(final String a1, final Object a2) {
        /*SL:65*/return (AddBandingRequest)super.set(a1, a2);
    }
    
    public AddBandingRequest clone() {
        /*SL:70*/return (AddBandingRequest)super.clone();
    }
}
