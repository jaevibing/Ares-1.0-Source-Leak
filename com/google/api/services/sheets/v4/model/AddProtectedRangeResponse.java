package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddProtectedRangeResponse extends GenericJson
{
    @Key
    private ProtectedRange protectedRange;
    
    public ProtectedRange getProtectedRange() {
        /*SL:48*/return this.protectedRange;
    }
    
    public AddProtectedRangeResponse setProtectedRange(final ProtectedRange protectedRange) {
        /*SL:56*/this.protectedRange = protectedRange;
        /*SL:57*/return this;
    }
    
    public AddProtectedRangeResponse set(final String a1, final Object a2) {
        /*SL:62*/return (AddProtectedRangeResponse)super.set(a1, a2);
    }
    
    public AddProtectedRangeResponse clone() {
        /*SL:67*/return (AddProtectedRangeResponse)super.clone();
    }
}
