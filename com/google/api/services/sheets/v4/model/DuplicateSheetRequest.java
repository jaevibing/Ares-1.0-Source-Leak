package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DuplicateSheetRequest extends GenericJson
{
    @Key
    private Integer insertSheetIndex;
    @Key
    private Integer newSheetId;
    @Key
    private String newSheetName;
    @Key
    private Integer sourceSheetId;
    
    public Integer getInsertSheetIndex() {
        /*SL:72*/return this.insertSheetIndex;
    }
    
    public DuplicateSheetRequest setInsertSheetIndex(final Integer insertSheetIndex) {
        /*SL:81*/this.insertSheetIndex = insertSheetIndex;
        /*SL:82*/return this;
    }
    
    public Integer getNewSheetId() {
        /*SL:91*/return this.newSheetId;
    }
    
    public DuplicateSheetRequest setNewSheetId(final Integer newSheetId) {
        /*SL:100*/this.newSheetId = newSheetId;
        /*SL:101*/return this;
    }
    
    public String getNewSheetName() {
        /*SL:109*/return this.newSheetName;
    }
    
    public DuplicateSheetRequest setNewSheetName(final String newSheetName) {
        /*SL:117*/this.newSheetName = newSheetName;
        /*SL:118*/return this;
    }
    
    public Integer getSourceSheetId() {
        /*SL:126*/return this.sourceSheetId;
    }
    
    public DuplicateSheetRequest setSourceSheetId(final Integer sourceSheetId) {
        /*SL:134*/this.sourceSheetId = sourceSheetId;
        /*SL:135*/return this;
    }
    
    public DuplicateSheetRequest set(final String a1, final Object a2) {
        /*SL:140*/return (DuplicateSheetRequest)super.set(a1, a2);
    }
    
    public DuplicateSheetRequest clone() {
        /*SL:145*/return (DuplicateSheetRequest)super.clone();
    }
}
