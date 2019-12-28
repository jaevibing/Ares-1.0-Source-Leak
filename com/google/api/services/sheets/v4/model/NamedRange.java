package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class NamedRange extends GenericJson
{
    @Key
    private String name;
    @Key
    private String namedRangeId;
    @Key
    private GridRange range;
    
    public String getName() {
        /*SL:62*/return this.name;
    }
    
    public NamedRange setName(final String name) {
        /*SL:70*/this.name = name;
        /*SL:71*/return this;
    }
    
    public String getNamedRangeId() {
        /*SL:79*/return this.namedRangeId;
    }
    
    public NamedRange setNamedRangeId(final String namedRangeId) {
        /*SL:87*/this.namedRangeId = namedRangeId;
        /*SL:88*/return this;
    }
    
    public GridRange getRange() {
        /*SL:96*/return this.range;
    }
    
    public NamedRange setRange(final GridRange range) {
        /*SL:104*/this.range = range;
        /*SL:105*/return this;
    }
    
    public NamedRange set(final String a1, final Object a2) {
        /*SL:110*/return (NamedRange)super.set(a1, a2);
    }
    
    public NamedRange clone() {
        /*SL:115*/return (NamedRange)super.clone();
    }
}
