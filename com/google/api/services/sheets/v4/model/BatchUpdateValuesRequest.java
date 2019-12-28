package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BatchUpdateValuesRequest extends GenericJson
{
    @Key
    private List<ValueRange> data;
    @Key
    private Boolean includeValuesInResponse;
    @Key
    private String responseDateTimeRenderOption;
    @Key
    private String responseValueRenderOption;
    @Key
    private String valueInputOption;
    
    public List<ValueRange> getData() {
        /*SL:83*/return this.data;
    }
    
    public BatchUpdateValuesRequest setData(final List<ValueRange> data) {
        /*SL:91*/this.data = data;
        /*SL:92*/return this;
    }
    
    public Boolean getIncludeValuesInResponse() {
        /*SL:104*/return this.includeValuesInResponse;
    }
    
    public BatchUpdateValuesRequest setIncludeValuesInResponse(final Boolean includeValuesInResponse) {
        /*SL:116*/this.includeValuesInResponse = includeValuesInResponse;
        /*SL:117*/return this;
    }
    
    public String getResponseDateTimeRenderOption() {
        /*SL:127*/return this.responseDateTimeRenderOption;
    }
    
    public BatchUpdateValuesRequest setResponseDateTimeRenderOption(final String responseDateTimeRenderOption) {
        /*SL:137*/this.responseDateTimeRenderOption = responseDateTimeRenderOption;
        /*SL:138*/return this;
    }
    
    public String getResponseValueRenderOption() {
        /*SL:147*/return this.responseValueRenderOption;
    }
    
    public BatchUpdateValuesRequest setResponseValueRenderOption(final String responseValueRenderOption) {
        /*SL:156*/this.responseValueRenderOption = responseValueRenderOption;
        /*SL:157*/return this;
    }
    
    public String getValueInputOption() {
        /*SL:165*/return this.valueInputOption;
    }
    
    public BatchUpdateValuesRequest setValueInputOption(final String valueInputOption) {
        /*SL:173*/this.valueInputOption = valueInputOption;
        /*SL:174*/return this;
    }
    
    public BatchUpdateValuesRequest set(final String a1, final Object a2) {
        /*SL:179*/return (BatchUpdateValuesRequest)super.set(a1, a2);
    }
    
    public BatchUpdateValuesRequest clone() {
        /*SL:184*/return (BatchUpdateValuesRequest)super.clone();
    }
}
