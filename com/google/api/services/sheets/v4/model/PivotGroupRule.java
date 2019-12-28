package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class PivotGroupRule extends GenericJson
{
    @Key
    private DateTimeRule dateTimeRule;
    @Key
    private HistogramRule histogramRule;
    @Key
    private ManualRule manualRule;
    
    public DateTimeRule getDateTimeRule() {
        /*SL:65*/return this.dateTimeRule;
    }
    
    public PivotGroupRule setDateTimeRule(final DateTimeRule dateTimeRule) {
        /*SL:73*/this.dateTimeRule = dateTimeRule;
        /*SL:74*/return this;
    }
    
    public HistogramRule getHistogramRule() {
        /*SL:82*/return this.histogramRule;
    }
    
    public PivotGroupRule setHistogramRule(final HistogramRule histogramRule) {
        /*SL:90*/this.histogramRule = histogramRule;
        /*SL:91*/return this;
    }
    
    public ManualRule getManualRule() {
        /*SL:99*/return this.manualRule;
    }
    
    public PivotGroupRule setManualRule(final ManualRule manualRule) {
        /*SL:107*/this.manualRule = manualRule;
        /*SL:108*/return this;
    }
    
    public PivotGroupRule set(final String a1, final Object a2) {
        /*SL:113*/return (PivotGroupRule)super.set(a1, a2);
    }
    
    public PivotGroupRule clone() {
        /*SL:118*/return (PivotGroupRule)super.clone();
    }
}
