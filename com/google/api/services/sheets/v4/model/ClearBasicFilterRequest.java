package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ClearBasicFilterRequest extends GenericJson
{
    @Key
    private Integer sheetId;
    
    public Integer getSheetId() {
        /*SL:48*/return this.sheetId;
    }
    
    public ClearBasicFilterRequest setSheetId(final Integer sheetId) {
        /*SL:56*/this.sheetId = sheetId;
        /*SL:57*/return this;
    }
    
    public ClearBasicFilterRequest set(final String a1, final Object a2) {
        /*SL:62*/return (ClearBasicFilterRequest)super.set(a1, a2);
    }
    
    public ClearBasicFilterRequest clone() {
        /*SL:67*/return (ClearBasicFilterRequest)super.clone();
    }
}
