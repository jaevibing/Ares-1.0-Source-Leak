package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BasicChartDomain extends GenericJson
{
    @Key
    private ChartData domain;
    @Key
    private Boolean reversed;
    
    public ChartData getDomain() {
        /*SL:57*/return this.domain;
    }
    
    public BasicChartDomain setDomain(final ChartData domain) {
        /*SL:66*/this.domain = domain;
        /*SL:67*/return this;
    }
    
    public Boolean getReversed() {
        /*SL:75*/return this.reversed;
    }
    
    public BasicChartDomain setReversed(final Boolean reversed) {
        /*SL:83*/this.reversed = reversed;
        /*SL:84*/return this;
    }
    
    public BasicChartDomain set(final String a1, final Object a2) {
        /*SL:89*/return (BasicChartDomain)super.set(a1, a2);
    }
    
    public BasicChartDomain clone() {
        /*SL:94*/return (BasicChartDomain)super.clone();
    }
}
