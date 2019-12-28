package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class GradientRule extends GenericJson
{
    @Key
    private InterpolationPoint maxpoint;
    @Key
    private InterpolationPoint midpoint;
    @Key
    private InterpolationPoint minpoint;
    
    public InterpolationPoint getMaxpoint() {
        /*SL:64*/return this.maxpoint;
    }
    
    public GradientRule setMaxpoint(final InterpolationPoint maxpoint) {
        /*SL:72*/this.maxpoint = maxpoint;
        /*SL:73*/return this;
    }
    
    public InterpolationPoint getMidpoint() {
        /*SL:81*/return this.midpoint;
    }
    
    public GradientRule setMidpoint(final InterpolationPoint midpoint) {
        /*SL:89*/this.midpoint = midpoint;
        /*SL:90*/return this;
    }
    
    public InterpolationPoint getMinpoint() {
        /*SL:98*/return this.minpoint;
    }
    
    public GradientRule setMinpoint(final InterpolationPoint minpoint) {
        /*SL:106*/this.minpoint = minpoint;
        /*SL:107*/return this;
    }
    
    public GradientRule set(final String a1, final Object a2) {
        /*SL:112*/return (GradientRule)super.set(a1, a2);
    }
    
    public GradientRule clone() {
        /*SL:117*/return (GradientRule)super.clone();
    }
}
