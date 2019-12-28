package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AppendValuesResponse extends GenericJson
{
    @Key
    private String spreadsheetId;
    @Key
    private String tableRange;
    @Key
    private UpdateValuesResponse updates;
    
    public String getSpreadsheetId() {
        /*SL:63*/return this.spreadsheetId;
    }
    
    public AppendValuesResponse setSpreadsheetId(final String spreadsheetId) {
        /*SL:71*/this.spreadsheetId = spreadsheetId;
        /*SL:72*/return this;
    }
    
    public String getTableRange() {
        /*SL:81*/return this.tableRange;
    }
    
    public AppendValuesResponse setTableRange(final String tableRange) {
        /*SL:90*/this.tableRange = tableRange;
        /*SL:91*/return this;
    }
    
    public UpdateValuesResponse getUpdates() {
        /*SL:99*/return this.updates;
    }
    
    public AppendValuesResponse setUpdates(final UpdateValuesResponse updates) {
        /*SL:107*/this.updates = updates;
        /*SL:108*/return this;
    }
    
    public AppendValuesResponse set(final String a1, final Object a2) {
        /*SL:113*/return (AppendValuesResponse)super.set(a1, a2);
    }
    
    public AppendValuesResponse clone() {
        /*SL:118*/return (AppendValuesResponse)super.clone();
    }
}
