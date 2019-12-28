package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BooleanRule extends GenericJson
{
    @Key
    private BooleanCondition condition;
    @Key
    private CellFormat format;
    
    public BooleanCondition getCondition() {
        /*SL:56*/return this.condition;
    }
    
    public BooleanRule setCondition(final BooleanCondition condition) {
        /*SL:64*/this.condition = condition;
        /*SL:65*/return this;
    }
    
    public CellFormat getFormat() {
        /*SL:74*/return this.format;
    }
    
    public BooleanRule setFormat(final CellFormat format) {
        /*SL:83*/this.format = format;
        /*SL:84*/return this;
    }
    
    public BooleanRule set(final String a1, final Object a2) {
        /*SL:89*/return (BooleanRule)super.set(a1, a2);
    }
    
    public BooleanRule clone() {
        /*SL:94*/return (BooleanRule)super.clone();
    }
}
