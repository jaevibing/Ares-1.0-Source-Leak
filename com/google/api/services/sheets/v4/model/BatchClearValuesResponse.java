package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BatchClearValuesResponse extends GenericJson
{
    @Key
    private List<String> clearedRanges;
    @Key
    private String spreadsheetId;
    
    public List<String> getClearedRanges() {
        /*SL:59*/return this.clearedRanges;
    }
    
    public BatchClearValuesResponse setClearedRanges(final List<String> clearedRanges) {
        /*SL:69*/this.clearedRanges = clearedRanges;
        /*SL:70*/return this;
    }
    
    public String getSpreadsheetId() {
        /*SL:78*/return this.spreadsheetId;
    }
    
    public BatchClearValuesResponse setSpreadsheetId(final String spreadsheetId) {
        /*SL:86*/this.spreadsheetId = spreadsheetId;
        /*SL:87*/return this;
    }
    
    public BatchClearValuesResponse set(final String a1, final Object a2) {
        /*SL:92*/return (BatchClearValuesResponse)super.set(a1, a2);
    }
    
    public BatchClearValuesResponse clone() {
        /*SL:97*/return (BatchClearValuesResponse)super.clone();
    }
}
