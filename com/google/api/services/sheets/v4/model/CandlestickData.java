package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CandlestickData extends GenericJson
{
    @Key
    private CandlestickSeries closeSeries;
    @Key
    private CandlestickSeries highSeries;
    @Key
    private CandlestickSeries lowSeries;
    @Key
    private CandlestickSeries openSeries;
    
    public CandlestickSeries getCloseSeries() {
        /*SL:77*/return this.closeSeries;
    }
    
    public CandlestickData setCloseSeries(final CandlestickSeries closeSeries) {
        /*SL:87*/this.closeSeries = closeSeries;
        /*SL:88*/return this;
    }
    
    public CandlestickSeries getHighSeries() {
        /*SL:97*/return this.highSeries;
    }
    
    public CandlestickData setHighSeries(final CandlestickSeries highSeries) {
        /*SL:106*/this.highSeries = highSeries;
        /*SL:107*/return this;
    }
    
    public CandlestickSeries getLowSeries() {
        /*SL:116*/return this.lowSeries;
    }
    
    public CandlestickData setLowSeries(final CandlestickSeries lowSeries) {
        /*SL:125*/this.lowSeries = lowSeries;
        /*SL:126*/return this;
    }
    
    public CandlestickSeries getOpenSeries() {
        /*SL:136*/return this.openSeries;
    }
    
    public CandlestickData setOpenSeries(final CandlestickSeries openSeries) {
        /*SL:146*/this.openSeries = openSeries;
        /*SL:147*/return this;
    }
    
    public CandlestickData set(final String a1, final Object a2) {
        /*SL:152*/return (CandlestickData)super.set(a1, a2);
    }
    
    public CandlestickData clone() {
        /*SL:157*/return (CandlestickData)super.clone();
    }
}
