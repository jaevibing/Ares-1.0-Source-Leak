package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BatchUpdateSpreadsheetRequest extends GenericJson
{
    @Key
    private Boolean includeSpreadsheetInResponse;
    @Key
    private List<Request> requests;
    @Key
    private Boolean responseIncludeGridData;
    @Key
    private List<String> responseRanges;
    
    public Boolean getIncludeSpreadsheetInResponse() {
        /*SL:78*/return this.includeSpreadsheetInResponse;
    }
    
    public BatchUpdateSpreadsheetRequest setIncludeSpreadsheetInResponse(final Boolean includeSpreadsheetInResponse) {
        /*SL:86*/this.includeSpreadsheetInResponse = includeSpreadsheetInResponse;
        /*SL:87*/return this;
    }
    
    public List<Request> getRequests() {
        /*SL:96*/return this.requests;
    }
    
    public BatchUpdateSpreadsheetRequest setRequests(final List<Request> requests) {
        /*SL:105*/this.requests = requests;
        /*SL:106*/return this;
    }
    
    public Boolean getResponseIncludeGridData() {
        /*SL:115*/return this.responseIncludeGridData;
    }
    
    public BatchUpdateSpreadsheetRequest setResponseIncludeGridData(final Boolean responseIncludeGridData) {
        /*SL:124*/this.responseIncludeGridData = responseIncludeGridData;
        /*SL:125*/return this;
    }
    
    public List<String> getResponseRanges() {
        /*SL:134*/return this.responseRanges;
    }
    
    public BatchUpdateSpreadsheetRequest setResponseRanges(final List<String> responseRanges) {
        /*SL:143*/this.responseRanges = responseRanges;
        /*SL:144*/return this;
    }
    
    public BatchUpdateSpreadsheetRequest set(final String a1, final Object a2) {
        /*SL:149*/return (BatchUpdateSpreadsheetRequest)super.set(a1, a2);
    }
    
    public BatchUpdateSpreadsheetRequest clone() {
        /*SL:154*/return (BatchUpdateSpreadsheetRequest)super.clone();
    }
    
    static {
        Data.<Object>nullOf(Request.class);
    }
}
