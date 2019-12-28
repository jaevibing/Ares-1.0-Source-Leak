package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteProtectedRangeRequest extends GenericJson
{
    @Key
    private Integer protectedRangeId;
    
    public Integer getProtectedRangeId() {
        /*SL:48*/return this.protectedRangeId;
    }
    
    public DeleteProtectedRangeRequest setProtectedRangeId(final Integer protectedRangeId) {
        /*SL:56*/this.protectedRangeId = protectedRangeId;
        /*SL:57*/return this;
    }
    
    public DeleteProtectedRangeRequest set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteProtectedRangeRequest)super.set(a1, a2);
    }
    
    public DeleteProtectedRangeRequest clone() {
        /*SL:67*/return (DeleteProtectedRangeRequest)super.clone();
    }
}
