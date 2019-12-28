package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class PivotGroupSortValueBucket extends GenericJson
{
    @Key
    private List<ExtendedValue> buckets;
    @Key
    private Integer valuesIndex;
    
    public List<ExtendedValue> getBuckets() {
        /*SL:67*/return this.buckets;
    }
    
    public PivotGroupSortValueBucket setBuckets(final List<ExtendedValue> buckets) {
        /*SL:81*/this.buckets = buckets;
        /*SL:82*/return this;
    }
    
    public Integer getValuesIndex() {
        /*SL:90*/return this.valuesIndex;
    }
    
    public PivotGroupSortValueBucket setValuesIndex(final Integer valuesIndex) {
        /*SL:98*/this.valuesIndex = valuesIndex;
        /*SL:99*/return this;
    }
    
    public PivotGroupSortValueBucket set(final String a1, final Object a2) {
        /*SL:104*/return (PivotGroupSortValueBucket)super.set(a1, a2);
    }
    
    public PivotGroupSortValueBucket clone() {
        /*SL:109*/return (PivotGroupSortValueBucket)super.clone();
    }
}
