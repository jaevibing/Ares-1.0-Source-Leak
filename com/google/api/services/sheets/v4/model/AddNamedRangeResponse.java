package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddNamedRangeResponse extends GenericJson
{
    @Key
    private NamedRange namedRange;
    
    public NamedRange getNamedRange() {
        /*SL:48*/return this.namedRange;
    }
    
    public AddNamedRangeResponse setNamedRange(final NamedRange namedRange) {
        /*SL:56*/this.namedRange = namedRange;
        /*SL:57*/return this;
    }
    
    public AddNamedRangeResponse set(final String a1, final Object a2) {
        /*SL:62*/return (AddNamedRangeResponse)super.set(a1, a2);
    }
    
    public AddNamedRangeResponse clone() {
        /*SL:67*/return (AddNamedRangeResponse)super.clone();
    }
}
