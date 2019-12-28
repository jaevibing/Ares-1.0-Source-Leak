package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BatchGetValuesResponse extends GenericJson
{
    @Key
    private String spreadsheetId;
    @Key
    private List<ValueRange> valueRanges;
    
    public String getSpreadsheetId() {
        /*SL:56*/return this.spreadsheetId;
    }
    
    public BatchGetValuesResponse setSpreadsheetId(final String spreadsheetId) {
        /*SL:64*/this.spreadsheetId = spreadsheetId;
        /*SL:65*/return this;
    }
    
    public List<ValueRange> getValueRanges() {
        /*SL:74*/return this.valueRanges;
    }
    
    public BatchGetValuesResponse setValueRanges(final List<ValueRange> valueRanges) {
        /*SL:83*/this.valueRanges = valueRanges;
        /*SL:84*/return this;
    }
    
    public BatchGetValuesResponse set(final String a1, final Object a2) {
        /*SL:89*/return (BatchGetValuesResponse)super.set(a1, a2);
    }
    
    public BatchGetValuesResponse clone() {
        /*SL:94*/return (BatchGetValuesResponse)super.clone();
    }
}
