package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddProtectedRangeRequest extends GenericJson
{
    @Key
    private ProtectedRange protectedRange;
    
    public ProtectedRange getProtectedRange() {
        /*SL:52*/return this.protectedRange;
    }
    
    public AddProtectedRangeRequest setProtectedRange(final ProtectedRange protectedRange) {
        /*SL:62*/this.protectedRange = protectedRange;
        /*SL:63*/return this;
    }
    
    public AddProtectedRangeRequest set(final String a1, final Object a2) {
        /*SL:68*/return (AddProtectedRangeRequest)super.set(a1, a2);
    }
    
    public AddProtectedRangeRequest clone() {
        /*SL:73*/return (AddProtectedRangeRequest)super.clone();
    }
}
