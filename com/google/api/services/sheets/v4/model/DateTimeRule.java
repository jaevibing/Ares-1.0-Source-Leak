package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DateTimeRule extends GenericJson
{
    @Key
    private String type;
    
    public String getType() {
        /*SL:59*/return this.type;
    }
    
    public DateTimeRule setType(final String type) {
        /*SL:67*/this.type = type;
        /*SL:68*/return this;
    }
    
    public DateTimeRule set(final String a1, final Object a2) {
        /*SL:73*/return (DateTimeRule)super.set(a1, a2);
    }
    
    public DateTimeRule clone() {
        /*SL:78*/return (DateTimeRule)super.clone();
    }
}
