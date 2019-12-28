package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class PasteDataRequest extends GenericJson
{
    @Key
    private GridCoordinate coordinate;
    @Key
    private String data;
    @Key
    private String delimiter;
    @Key
    private Boolean html;
    @Key
    private String type;
    
    public GridCoordinate getCoordinate() {
        /*SL:76*/return this.coordinate;
    }
    
    public PasteDataRequest setCoordinate(final GridCoordinate coordinate) {
        /*SL:84*/this.coordinate = coordinate;
        /*SL:85*/return this;
    }
    
    public String getData() {
        /*SL:93*/return this.data;
    }
    
    public PasteDataRequest setData(final String data) {
        /*SL:101*/this.data = data;
        /*SL:102*/return this;
    }
    
    public String getDelimiter() {
        /*SL:110*/return this.delimiter;
    }
    
    public PasteDataRequest setDelimiter(final String delimiter) {
        /*SL:118*/this.delimiter = delimiter;
        /*SL:119*/return this;
    }
    
    public Boolean getHtml() {
        /*SL:127*/return this.html;
    }
    
    public PasteDataRequest setHtml(final Boolean html) {
        /*SL:135*/this.html = html;
        /*SL:136*/return this;
    }
    
    public String getType() {
        /*SL:144*/return this.type;
    }
    
    public PasteDataRequest setType(final String type) {
        /*SL:152*/this.type = type;
        /*SL:153*/return this;
    }
    
    public PasteDataRequest set(final String a1, final Object a2) {
        /*SL:158*/return (PasteDataRequest)super.set(a1, a2);
    }
    
    public PasteDataRequest clone() {
        /*SL:163*/return (PasteDataRequest)super.clone();
    }
}
