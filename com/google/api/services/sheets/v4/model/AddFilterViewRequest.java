package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddFilterViewRequest extends GenericJson
{
    @Key
    private FilterView filter;
    
    public FilterView getFilter() {
        /*SL:50*/return this.filter;
    }
    
    public AddFilterViewRequest setFilter(final FilterView filter) {
        /*SL:59*/this.filter = filter;
        /*SL:60*/return this;
    }
    
    public AddFilterViewRequest set(final String a1, final Object a2) {
        /*SL:65*/return (AddFilterViewRequest)super.set(a1, a2);
    }
    
    public AddFilterViewRequest clone() {
        /*SL:70*/return (AddFilterViewRequest)super.clone();
    }
}
