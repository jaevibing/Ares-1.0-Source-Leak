package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AppendDimensionRequest extends GenericJson
{
    @Key
    private String dimension;
    @Key
    private Integer length;
    @Key
    private Integer sheetId;
    
    public String getDimension() {
        /*SL:62*/return this.dimension;
    }
    
    public AppendDimensionRequest setDimension(final String dimension) {
        /*SL:70*/this.dimension = dimension;
        /*SL:71*/return this;
    }
    
    public Integer getLength() {
        /*SL:79*/return this.length;
    }
    
    public AppendDimensionRequest setLength(final Integer length) {
        /*SL:87*/this.length = length;
        /*SL:88*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:96*/return this.sheetId;
    }
    
    public AppendDimensionRequest setSheetId(final Integer sheetId) {
        /*SL:104*/this.sheetId = sheetId;
        /*SL:105*/return this;
    }
    
    public AppendDimensionRequest set(final String a1, final Object a2) {
        /*SL:110*/return (AppendDimensionRequest)super.set(a1, a2);
    }
    
    public AppendDimensionRequest clone() {
        /*SL:115*/return (AppendDimensionRequest)super.clone();
    }
}
