package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class GridRange extends GenericJson
{
    @Key
    private Integer endColumnIndex;
    @Key
    private Integer endRowIndex;
    @Key
    private Integer sheetId;
    @Key
    private Integer startColumnIndex;
    @Key
    private Integer startRowIndex;
    
    public Integer getEndColumnIndex() {
        /*SL:97*/return this.endColumnIndex;
    }
    
    public GridRange setEndColumnIndex(final Integer endColumnIndex) {
        /*SL:105*/this.endColumnIndex = endColumnIndex;
        /*SL:106*/return this;
    }
    
    public Integer getEndRowIndex() {
        /*SL:114*/return this.endRowIndex;
    }
    
    public GridRange setEndRowIndex(final Integer endRowIndex) {
        /*SL:122*/this.endRowIndex = endRowIndex;
        /*SL:123*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:131*/return this.sheetId;
    }
    
    public GridRange setSheetId(final Integer sheetId) {
        /*SL:139*/this.sheetId = sheetId;
        /*SL:140*/return this;
    }
    
    public Integer getStartColumnIndex() {
        /*SL:148*/return this.startColumnIndex;
    }
    
    public GridRange setStartColumnIndex(final Integer startColumnIndex) {
        /*SL:156*/this.startColumnIndex = startColumnIndex;
        /*SL:157*/return this;
    }
    
    public Integer getStartRowIndex() {
        /*SL:165*/return this.startRowIndex;
    }
    
    public GridRange setStartRowIndex(final Integer startRowIndex) {
        /*SL:173*/this.startRowIndex = startRowIndex;
        /*SL:174*/return this;
    }
    
    public GridRange set(final String a1, final Object a2) {
        /*SL:179*/return (GridRange)super.set(a1, a2);
    }
    
    public GridRange clone() {
        /*SL:184*/return (GridRange)super.clone();
    }
}
