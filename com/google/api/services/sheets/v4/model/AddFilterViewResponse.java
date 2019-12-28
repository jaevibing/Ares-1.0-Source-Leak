package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddFilterViewResponse extends GenericJson
{
    @Key
    private FilterView filter;
    
    public FilterView getFilter() {
        /*SL:48*/return this.filter;
    }
    
    public AddFilterViewResponse setFilter(final FilterView filter) {
        /*SL:56*/this.filter = filter;
        /*SL:57*/return this;
    }
    
    public AddFilterViewResponse set(final String a1, final Object a2) {
        /*SL:62*/return (AddFilterViewResponse)super.set(a1, a2);
    }
    
    public AddFilterViewResponse clone() {
        /*SL:67*/return (AddFilterViewResponse)super.clone();
    }
}
