package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class IterativeCalculationSettings extends GenericJson
{
    @Key
    private Double convergenceThreshold;
    @Key
    private Integer maxIterations;
    
    public Double getConvergenceThreshold() {
        /*SL:57*/return this.convergenceThreshold;
    }
    
    public IterativeCalculationSettings setConvergenceThreshold(final Double convergenceThreshold) {
        /*SL:66*/this.convergenceThreshold = convergenceThreshold;
        /*SL:67*/return this;
    }
    
    public Integer getMaxIterations() {
        /*SL:75*/return this.maxIterations;
    }
    
    public IterativeCalculationSettings setMaxIterations(final Integer maxIterations) {
        /*SL:83*/this.maxIterations = maxIterations;
        /*SL:84*/return this;
    }
    
    public IterativeCalculationSettings set(final String a1, final Object a2) {
        /*SL:89*/return (IterativeCalculationSettings)super.set(a1, a2);
    }
    
    public IterativeCalculationSettings clone() {
        /*SL:94*/return (IterativeCalculationSettings)super.clone();
    }
}
