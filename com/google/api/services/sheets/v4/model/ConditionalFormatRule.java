package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ConditionalFormatRule extends GenericJson
{
    @Key
    private BooleanRule booleanRule;
    @Key
    private GradientRule gradientRule;
    @Key
    private List<GridRange> ranges;
    
    public BooleanRule getBooleanRule() {
        /*SL:63*/return this.booleanRule;
    }
    
    public ConditionalFormatRule setBooleanRule(final BooleanRule booleanRule) {
        /*SL:71*/this.booleanRule = booleanRule;
        /*SL:72*/return this;
    }
    
    public GradientRule getGradientRule() {
        /*SL:80*/return this.gradientRule;
    }
    
    public ConditionalFormatRule setGradientRule(final GradientRule gradientRule) {
        /*SL:88*/this.gradientRule = gradientRule;
        /*SL:89*/return this;
    }
    
    public List<GridRange> getRanges() {
        /*SL:98*/return this.ranges;
    }
    
    public ConditionalFormatRule setRanges(final List<GridRange> ranges) {
        /*SL:107*/this.ranges = ranges;
        /*SL:108*/return this;
    }
    
    public ConditionalFormatRule set(final String a1, final Object a2) {
        /*SL:113*/return (ConditionalFormatRule)super.set(a1, a2);
    }
    
    public ConditionalFormatRule clone() {
        /*SL:118*/return (ConditionalFormatRule)super.clone();
    }
}
