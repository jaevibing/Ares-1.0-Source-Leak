package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class BatchUpdateValuesByDataFilterRequest extends GenericJson
{
    @Key
    private List<DataFilterValueRange> data;
    @Key
    private Boolean includeValuesInResponse;
    @Key
    private String responseDateTimeRenderOption;
    @Key
    private String responseValueRenderOption;
    @Key
    private String valueInputOption;
    
    public List<DataFilterValueRange> getData() {
        /*SL:85*/return this.data;
    }
    
    public BatchUpdateValuesByDataFilterRequest setData(final List<DataFilterValueRange> data) {
        /*SL:94*/this.data = data;
        /*SL:95*/return this;
    }
    
    public Boolean getIncludeValuesInResponse() {
        /*SL:107*/return this.includeValuesInResponse;
    }
    
    public BatchUpdateValuesByDataFilterRequest setIncludeValuesInResponse(final Boolean includeValuesInResponse) {
        /*SL:119*/this.includeValuesInResponse = includeValuesInResponse;
        /*SL:120*/return this;
    }
    
    public String getResponseDateTimeRenderOption() {
        /*SL:130*/return this.responseDateTimeRenderOption;
    }
    
    public BatchUpdateValuesByDataFilterRequest setResponseDateTimeRenderOption(final String responseDateTimeRenderOption) {
        /*SL:140*/this.responseDateTimeRenderOption = responseDateTimeRenderOption;
        /*SL:141*/return this;
    }
    
    public String getResponseValueRenderOption() {
        /*SL:150*/return this.responseValueRenderOption;
    }
    
    public BatchUpdateValuesByDataFilterRequest setResponseValueRenderOption(final String responseValueRenderOption) {
        /*SL:159*/this.responseValueRenderOption = responseValueRenderOption;
        /*SL:160*/return this;
    }
    
    public String getValueInputOption() {
        /*SL:168*/return this.valueInputOption;
    }
    
    public BatchUpdateValuesByDataFilterRequest setValueInputOption(final String valueInputOption) {
        /*SL:176*/this.valueInputOption = valueInputOption;
        /*SL:177*/return this;
    }
    
    public BatchUpdateValuesByDataFilterRequest set(final String a1, final Object a2) {
        /*SL:182*/return (BatchUpdateValuesByDataFilterRequest)super.set(a1, a2);
    }
    
    public BatchUpdateValuesByDataFilterRequest clone() {
        /*SL:187*/return (BatchUpdateValuesByDataFilterRequest)super.clone();
    }
}
