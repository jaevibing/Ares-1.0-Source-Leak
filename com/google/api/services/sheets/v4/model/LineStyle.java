package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class LineStyle extends GenericJson
{
    @Key
    private String type;
    @Key
    private Integer width;
    
    public String getType() {
        /*SL:55*/return this.type;
    }
    
    public LineStyle setType(final String type) {
        /*SL:63*/this.type = type;
        /*SL:64*/return this;
    }
    
    public Integer getWidth() {
        /*SL:72*/return this.width;
    }
    
    public LineStyle setWidth(final Integer width) {
        /*SL:80*/this.width = width;
        /*SL:81*/return this;
    }
    
    public LineStyle set(final String a1, final Object a2) {
        /*SL:86*/return (LineStyle)super.set(a1, a2);
    }
    
    public LineStyle clone() {
        /*SL:91*/return (LineStyle)super.clone();
    }
}
