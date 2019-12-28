package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BatchUpdateSpreadsheetResponse extends GenericJson
{
    @Key
    private List<Response> replies;
    @Key
    private String spreadsheetId;
    @Key
    private Spreadsheet updatedSpreadsheet;
    
    public List<Response> getReplies() {
        /*SL:71*/return this.replies;
    }
    
    public BatchUpdateSpreadsheetResponse setReplies(final List<Response> replies) {
        /*SL:80*/this.replies = replies;
        /*SL:81*/return this;
    }
    
    public String getSpreadsheetId() {
        /*SL:89*/return this.spreadsheetId;
    }
    
    public BatchUpdateSpreadsheetResponse setSpreadsheetId(final String spreadsheetId) {
        /*SL:97*/this.spreadsheetId = spreadsheetId;
        /*SL:98*/return this;
    }
    
    public Spreadsheet getUpdatedSpreadsheet() {
        /*SL:107*/return this.updatedSpreadsheet;
    }
    
    public BatchUpdateSpreadsheetResponse setUpdatedSpreadsheet(final Spreadsheet updatedSpreadsheet) {
        /*SL:116*/this.updatedSpreadsheet = updatedSpreadsheet;
        /*SL:117*/return this;
    }
    
    public BatchUpdateSpreadsheetResponse set(final String a1, final Object a2) {
        /*SL:122*/return (BatchUpdateSpreadsheetResponse)super.set(a1, a2);
    }
    
    public BatchUpdateSpreadsheetResponse clone() {
        /*SL:127*/return (BatchUpdateSpreadsheetResponse)super.clone();
    }
    
    static {
        Data.<Object>nullOf(Response.class);
    }
}
