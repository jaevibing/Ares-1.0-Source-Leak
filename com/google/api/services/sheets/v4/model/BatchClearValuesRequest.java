package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BatchClearValuesRequest extends GenericJson
{
    @Key
    private List<String> ranges;
    
    public List<String> getRanges() {
        /*SL:48*/return this.ranges;
    }
    
    public BatchClearValuesRequest setRanges(final List<String> ranges) {
        /*SL:56*/this.ranges = ranges;
        /*SL:57*/return this;
    }
    
    public BatchClearValuesRequest set(final String a1, final Object a2) {
        /*SL:62*/return (BatchClearValuesRequest)super.set(a1, a2);
    }
    
    public BatchClearValuesRequest clone() {
        /*SL:67*/return (BatchClearValuesRequest)super.clone();
    }
}
