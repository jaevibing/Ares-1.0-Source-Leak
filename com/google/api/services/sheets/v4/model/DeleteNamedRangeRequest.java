package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteNamedRangeRequest extends GenericJson
{
    @Key
    private String namedRangeId;
    
    public String getNamedRangeId() {
        /*SL:48*/return this.namedRangeId;
    }
    
    public DeleteNamedRangeRequest setNamedRangeId(final String namedRangeId) {
        /*SL:56*/this.namedRangeId = namedRangeId;
        /*SL:57*/return this;
    }
    
    public DeleteNamedRangeRequest set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteNamedRangeRequest)super.set(a1, a2);
    }
    
    public DeleteNamedRangeRequest clone() {
        /*SL:67*/return (DeleteNamedRangeRequest)super.clone();
    }
}
