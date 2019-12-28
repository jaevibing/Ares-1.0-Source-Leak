package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateChartSpecRequest extends GenericJson
{
    @Key
    private Integer chartId;
    @Key
    private ChartSpec spec;
    
    public Integer getChartId() {
        /*SL:56*/return this.chartId;
    }
    
    public UpdateChartSpecRequest setChartId(final Integer chartId) {
        /*SL:64*/this.chartId = chartId;
        /*SL:65*/return this;
    }
    
    public ChartSpec getSpec() {
        /*SL:73*/return this.spec;
    }
    
    public UpdateChartSpecRequest setSpec(final ChartSpec spec) {
        /*SL:81*/this.spec = spec;
        /*SL:82*/return this;
    }
    
    public UpdateChartSpecRequest set(final String a1, final Object a2) {
        /*SL:87*/return (UpdateChartSpecRequest)super.set(a1, a2);
    }
    
    public UpdateChartSpecRequest clone() {
        /*SL:92*/return (UpdateChartSpecRequest)super.clone();
    }
}
