package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateProtectedRangeRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private ProtectedRange protectedRange;
    
    public String getFields() {
        /*SL:59*/return this.fields;
    }
    
    public UpdateProtectedRangeRequest setFields(final String fields) {
        /*SL:69*/this.fields = fields;
        /*SL:70*/return this;
    }
    
    public ProtectedRange getProtectedRange() {
        /*SL:78*/return this.protectedRange;
    }
    
    public UpdateProtectedRangeRequest setProtectedRange(final ProtectedRange protectedRange) {
        /*SL:86*/this.protectedRange = protectedRange;
        /*SL:87*/return this;
    }
    
    public UpdateProtectedRangeRequest set(final String a1, final Object a2) {
        /*SL:92*/return (UpdateProtectedRangeRequest)super.set(a1, a2);
    }
    
    public UpdateProtectedRangeRequest clone() {
        /*SL:97*/return (UpdateProtectedRangeRequest)super.clone();
    }
}
