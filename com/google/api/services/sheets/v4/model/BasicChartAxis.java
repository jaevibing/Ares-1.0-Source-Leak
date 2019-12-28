package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BasicChartAxis extends GenericJson
{
    @Key
    private TextFormat format;
    @Key
    private String position;
    @Key
    private String title;
    @Key
    private TextPosition titleTextPosition;
    
    public TextFormat getFormat() {
        /*SL:69*/return this.format;
    }
    
    public BasicChartAxis setFormat(final TextFormat format) {
        /*SL:77*/this.format = format;
        /*SL:78*/return this;
    }
    
    public String getPosition() {
        /*SL:86*/return this.position;
    }
    
    public BasicChartAxis setPosition(final String position) {
        /*SL:94*/this.position = position;
        /*SL:95*/return this;
    }
    
    public String getTitle() {
        /*SL:103*/return this.title;
    }
    
    public BasicChartAxis setTitle(final String title) {
        /*SL:111*/this.title = title;
        /*SL:112*/return this;
    }
    
    public TextPosition getTitleTextPosition() {
        /*SL:120*/return this.titleTextPosition;
    }
    
    public BasicChartAxis setTitleTextPosition(final TextPosition titleTextPosition) {
        /*SL:128*/this.titleTextPosition = titleTextPosition;
        /*SL:129*/return this;
    }
    
    public BasicChartAxis set(final String a1, final Object a2) {
        /*SL:134*/return (BasicChartAxis)super.set(a1, a2);
    }
    
    public BasicChartAxis clone() {
        /*SL:139*/return (BasicChartAxis)super.clone();
    }
}
