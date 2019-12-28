package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddNamedRangeRequest extends GenericJson
{
    @Key
    private NamedRange namedRange;
    
    public NamedRange getNamedRange() {
        /*SL:50*/return this.namedRange;
    }
    
    public AddNamedRangeRequest setNamedRange(final NamedRange namedRange) {
        /*SL:59*/this.namedRange = namedRange;
        /*SL:60*/return this;
    }
    
    public AddNamedRangeRequest set(final String a1, final Object a2) {
        /*SL:65*/return (AddNamedRangeRequest)super.set(a1, a2);
    }
    
    public AddNamedRangeRequest clone() {
        /*SL:70*/return (AddNamedRangeRequest)super.clone();
    }
}
