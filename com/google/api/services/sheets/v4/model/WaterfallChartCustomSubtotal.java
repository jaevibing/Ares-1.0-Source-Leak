package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class WaterfallChartCustomSubtotal extends GenericJson
{
    @Key
    private Boolean dataIsSubtotal;
    @Key
    private String label;
    @Key
    private Integer subtotalIndex;
    
    public Boolean getDataIsSubtotal() {
        /*SL:69*/return this.dataIsSubtotal;
    }
    
    public WaterfallChartCustomSubtotal setDataIsSubtotal(final Boolean dataIsSubtotal) {
        /*SL:78*/this.dataIsSubtotal = dataIsSubtotal;
        /*SL:79*/return this;
    }
    
    public String getLabel() {
        /*SL:87*/return this.label;
    }
    
    public WaterfallChartCustomSubtotal setLabel(final String label) {
        /*SL:95*/this.label = label;
        /*SL:96*/return this;
    }
    
    public Integer getSubtotalIndex() {
        /*SL:109*/return this.subtotalIndex;
    }
    
    public WaterfallChartCustomSubtotal setSubtotalIndex(final Integer subtotalIndex) {
        /*SL:122*/this.subtotalIndex = subtotalIndex;
        /*SL:123*/return this;
    }
    
    public WaterfallChartCustomSubtotal set(final String a1, final Object a2) {
        /*SL:128*/return (WaterfallChartCustomSubtotal)super.set(a1, a2);
    }
    
    public WaterfallChartCustomSubtotal clone() {
        /*SL:133*/return (WaterfallChartCustomSubtotal)super.clone();
    }
}
