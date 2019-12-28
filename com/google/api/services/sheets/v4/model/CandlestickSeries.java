package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CandlestickSeries extends GenericJson
{
    @Key
    private ChartData data;
    
    public ChartData getData() {
        /*SL:48*/return this.data;
    }
    
    public CandlestickSeries setData(final ChartData data) {
        /*SL:56*/this.data = data;
        /*SL:57*/return this;
    }
    
    public CandlestickSeries set(final String a1, final Object a2) {
        /*SL:62*/return (CandlestickSeries)super.set(a1, a2);
    }
    
    public CandlestickSeries clone() {
        /*SL:67*/return (CandlestickSeries)super.clone();
    }
}
