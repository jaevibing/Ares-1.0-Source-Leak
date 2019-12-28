package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class CandlestickChartSpec extends GenericJson
{
    @Key
    private List<CandlestickData> data;
    @Key
    private CandlestickDomain domain;
    
    public List<CandlestickData> getData() {
        /*SL:56*/return this.data;
    }
    
    public CandlestickChartSpec setData(final List<CandlestickData> data) {
        /*SL:64*/this.data = data;
        /*SL:65*/return this;
    }
    
    public CandlestickDomain getDomain() {
        /*SL:74*/return this.domain;
    }
    
    public CandlestickChartSpec setDomain(final CandlestickDomain domain) {
        /*SL:83*/this.domain = domain;
        /*SL:84*/return this;
    }
    
    public CandlestickChartSpec set(final String a1, final Object a2) {
        /*SL:89*/return (CandlestickChartSpec)super.set(a1, a2);
    }
    
    public CandlestickChartSpec clone() {
        /*SL:94*/return (CandlestickChartSpec)super.clone();
    }
}
