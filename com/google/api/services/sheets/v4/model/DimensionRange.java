package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DimensionRange extends GenericJson
{
    @Key
    private String dimension;
    @Key
    private Integer endIndex;
    @Key
    private Integer sheetId;
    @Key
    private Integer startIndex;
    
    public String getDimension() {
        /*SL:71*/return this.dimension;
    }
    
    public DimensionRange setDimension(final String dimension) {
        /*SL:79*/this.dimension = dimension;
        /*SL:80*/return this;
    }
    
    public Integer getEndIndex() {
        /*SL:88*/return this.endIndex;
    }
    
    public DimensionRange setEndIndex(final Integer endIndex) {
        /*SL:96*/this.endIndex = endIndex;
        /*SL:97*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:105*/return this.sheetId;
    }
    
    public DimensionRange setSheetId(final Integer sheetId) {
        /*SL:113*/this.sheetId = sheetId;
        /*SL:114*/return this;
    }
    
    public Integer getStartIndex() {
        /*SL:122*/return this.startIndex;
    }
    
    public DimensionRange setStartIndex(final Integer startIndex) {
        /*SL:130*/this.startIndex = startIndex;
        /*SL:131*/return this;
    }
    
    public DimensionRange set(final String a1, final Object a2) {
        /*SL:136*/return (DimensionRange)super.set(a1, a2);
    }
    
    public DimensionRange clone() {
        /*SL:141*/return (DimensionRange)super.clone();
    }
}
