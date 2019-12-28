package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BatchGetValuesByDataFilterRequest extends GenericJson
{
    @Key
    private List<DataFilter> dataFilters;
    @Key
    private String dateTimeRenderOption;
    @Key
    private String majorDimension;
    @Key
    private String valueRenderOption;
    
    public List<DataFilter> getDataFilters() {
        /*SL:78*/return this.dataFilters;
    }
    
    public BatchGetValuesByDataFilterRequest setDataFilters(final List<DataFilter> dataFilters) {
        /*SL:87*/this.dataFilters = dataFilters;
        /*SL:88*/return this;
    }
    
    public String getDateTimeRenderOption() {
        /*SL:98*/return this.dateTimeRenderOption;
    }
    
    public BatchGetValuesByDataFilterRequest setDateTimeRenderOption(final String dateTimeRenderOption) {
        /*SL:108*/this.dateTimeRenderOption = dateTimeRenderOption;
        /*SL:109*/return this;
    }
    
    public String getMajorDimension() {
        /*SL:121*/return this.majorDimension;
    }
    
    public BatchGetValuesByDataFilterRequest setMajorDimension(final String majorDimension) {
        /*SL:133*/this.majorDimension = majorDimension;
        /*SL:134*/return this;
    }
    
    public String getValueRenderOption() {
        /*SL:143*/return this.valueRenderOption;
    }
    
    public BatchGetValuesByDataFilterRequest setValueRenderOption(final String valueRenderOption) {
        /*SL:152*/this.valueRenderOption = valueRenderOption;
        /*SL:153*/return this;
    }
    
    public BatchGetValuesByDataFilterRequest set(final String a1, final Object a2) {
        /*SL:158*/return (BatchGetValuesByDataFilterRequest)super.set(a1, a2);
    }
    
    public BatchGetValuesByDataFilterRequest clone() {
        /*SL:163*/return (BatchGetValuesByDataFilterRequest)super.clone();
    }
}
