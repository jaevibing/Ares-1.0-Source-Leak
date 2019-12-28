package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ClearValuesResponse extends GenericJson
{
    @Key
    private String clearedRange;
    @Key
    private String spreadsheetId;
    
    public String getClearedRange() {
        /*SL:59*/return this.clearedRange;
    }
    
    public ClearValuesResponse setClearedRange(final String clearedRange) {
        /*SL:69*/this.clearedRange = clearedRange;
        /*SL:70*/return this;
    }
    
    public String getSpreadsheetId() {
        /*SL:78*/return this.spreadsheetId;
    }
    
    public ClearValuesResponse setSpreadsheetId(final String spreadsheetId) {
        /*SL:86*/this.spreadsheetId = spreadsheetId;
        /*SL:87*/return this;
    }
    
    public ClearValuesResponse set(final String a1, final Object a2) {
        /*SL:92*/return (ClearValuesResponse)super.set(a1, a2);
    }
    
    public ClearValuesResponse clone() {
        /*SL:97*/return (ClearValuesResponse)super.clone();
    }
}
