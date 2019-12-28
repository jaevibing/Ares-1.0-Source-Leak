package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DuplicateFilterViewRequest extends GenericJson
{
    @Key
    private Integer filterId;
    
    public Integer getFilterId() {
        /*SL:48*/return this.filterId;
    }
    
    public DuplicateFilterViewRequest setFilterId(final Integer filterId) {
        /*SL:56*/this.filterId = filterId;
        /*SL:57*/return this;
    }
    
    public DuplicateFilterViewRequest set(final String a1, final Object a2) {
        /*SL:62*/return (DuplicateFilterViewRequest)super.set(a1, a2);
    }
    
    public DuplicateFilterViewRequest clone() {
        /*SL:67*/return (DuplicateFilterViewRequest)super.clone();
    }
}
