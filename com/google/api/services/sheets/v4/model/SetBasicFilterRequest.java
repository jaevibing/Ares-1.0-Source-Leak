package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class SetBasicFilterRequest extends GenericJson
{
    @Key
    private BasicFilter filter;
    
    public BasicFilter getFilter() {
        /*SL:48*/return this.filter;
    }
    
    public SetBasicFilterRequest setFilter(final BasicFilter filter) {
        /*SL:56*/this.filter = filter;
        /*SL:57*/return this;
    }
    
    public SetBasicFilterRequest set(final String a1, final Object a2) {
        /*SL:62*/return (SetBasicFilterRequest)super.set(a1, a2);
    }
    
    public SetBasicFilterRequest clone() {
        /*SL:67*/return (SetBasicFilterRequest)super.clone();
    }
}
