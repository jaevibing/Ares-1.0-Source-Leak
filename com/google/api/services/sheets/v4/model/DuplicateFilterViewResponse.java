package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DuplicateFilterViewResponse extends GenericJson
{
    @Key
    private FilterView filter;
    
    public FilterView getFilter() {
        /*SL:48*/return this.filter;
    }
    
    public DuplicateFilterViewResponse setFilter(final FilterView filter) {
        /*SL:56*/this.filter = filter;
        /*SL:57*/return this;
    }
    
    public DuplicateFilterViewResponse set(final String a1, final Object a2) {
        /*SL:62*/return (DuplicateFilterViewResponse)super.set(a1, a2);
    }
    
    public DuplicateFilterViewResponse clone() {
        /*SL:67*/return (DuplicateFilterViewResponse)super.clone();
    }
}
