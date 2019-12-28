package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateNamedRangeRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private NamedRange namedRange;
    
    public String getFields() {
        /*SL:59*/return this.fields;
    }
    
    public UpdateNamedRangeRequest setFields(final String fields) {
        /*SL:69*/this.fields = fields;
        /*SL:70*/return this;
    }
    
    public NamedRange getNamedRange() {
        /*SL:78*/return this.namedRange;
    }
    
    public UpdateNamedRangeRequest setNamedRange(final NamedRange namedRange) {
        /*SL:86*/this.namedRange = namedRange;
        /*SL:87*/return this;
    }
    
    public UpdateNamedRangeRequest set(final String a1, final Object a2) {
        /*SL:92*/return (UpdateNamedRangeRequest)super.set(a1, a2);
    }
    
    public UpdateNamedRangeRequest clone() {
        /*SL:97*/return (UpdateNamedRangeRequest)super.clone();
    }
}
