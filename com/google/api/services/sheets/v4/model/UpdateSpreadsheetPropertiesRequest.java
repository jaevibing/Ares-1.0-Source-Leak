package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateSpreadsheetPropertiesRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private SpreadsheetProperties properties;
    
    public String getFields() {
        /*SL:59*/return this.fields;
    }
    
    public UpdateSpreadsheetPropertiesRequest setFields(final String fields) {
        /*SL:69*/this.fields = fields;
        /*SL:70*/return this;
    }
    
    public SpreadsheetProperties getProperties() {
        /*SL:78*/return this.properties;
    }
    
    public UpdateSpreadsheetPropertiesRequest setProperties(final SpreadsheetProperties properties) {
        /*SL:86*/this.properties = properties;
        /*SL:87*/return this;
    }
    
    public UpdateSpreadsheetPropertiesRequest set(final String a1, final Object a2) {
        /*SL:92*/return (UpdateSpreadsheetPropertiesRequest)super.set(a1, a2);
    }
    
    public UpdateSpreadsheetPropertiesRequest clone() {
        /*SL:97*/return (UpdateSpreadsheetPropertiesRequest)super.clone();
    }
}
