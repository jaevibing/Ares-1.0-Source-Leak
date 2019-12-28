package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateFilterViewRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private FilterView filter;
    
    public String getFields() {
        /*SL:59*/return this.fields;
    }
    
    public UpdateFilterViewRequest setFields(final String fields) {
        /*SL:69*/this.fields = fields;
        /*SL:70*/return this;
    }
    
    public FilterView getFilter() {
        /*SL:78*/return this.filter;
    }
    
    public UpdateFilterViewRequest setFilter(final FilterView filter) {
        /*SL:86*/this.filter = filter;
        /*SL:87*/return this;
    }
    
    public UpdateFilterViewRequest set(final String a1, final Object a2) {
        /*SL:92*/return (UpdateFilterViewRequest)super.set(a1, a2);
    }
    
    public UpdateFilterViewRequest clone() {
        /*SL:97*/return (UpdateFilterViewRequest)super.clone();
    }
}
