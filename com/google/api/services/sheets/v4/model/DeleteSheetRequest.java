package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class DeleteSheetRequest extends GenericJson
{
    @Key
    private Integer sheetId;
    
    public Integer getSheetId() {
        /*SL:48*/return this.sheetId;
    }
    
    public DeleteSheetRequest setSheetId(final Integer sheetId) {
        /*SL:56*/this.sheetId = sheetId;
        /*SL:57*/return this;
    }
    
    public DeleteSheetRequest set(final String a1, final Object a2) {
        /*SL:62*/return (DeleteSheetRequest)super.set(a1, a2);
    }
    
    public DeleteSheetRequest clone() {
        /*SL:67*/return (DeleteSheetRequest)super.clone();
    }
}
