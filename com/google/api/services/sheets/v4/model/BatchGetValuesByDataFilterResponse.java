package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BatchGetValuesByDataFilterResponse extends GenericJson
{
    @Key
    private String spreadsheetId;
    @Key
    private List<MatchedValueRange> valueRanges;
    
    public String getSpreadsheetId() {
        /*SL:56*/return this.spreadsheetId;
    }
    
    public BatchGetValuesByDataFilterResponse setSpreadsheetId(final String spreadsheetId) {
        /*SL:64*/this.spreadsheetId = spreadsheetId;
        /*SL:65*/return this;
    }
    
    public List<MatchedValueRange> getValueRanges() {
        /*SL:73*/return this.valueRanges;
    }
    
    public BatchGetValuesByDataFilterResponse setValueRanges(final List<MatchedValueRange> valueRanges) {
        /*SL:81*/this.valueRanges = valueRanges;
        /*SL:82*/return this;
    }
    
    public BatchGetValuesByDataFilterResponse set(final String a1, final Object a2) {
        /*SL:87*/return (BatchGetValuesByDataFilterResponse)super.set(a1, a2);
    }
    
    public BatchGetValuesByDataFilterResponse clone() {
        /*SL:92*/return (BatchGetValuesByDataFilterResponse)super.clone();
    }
}
