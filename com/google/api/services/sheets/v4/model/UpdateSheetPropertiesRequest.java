package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class UpdateSheetPropertiesRequest extends GenericJson
{
    @Key
    private String fields;
    @Key
    private SheetProperties properties;
    
    public String getFields() {
        /*SL:59*/return this.fields;
    }
    
    public UpdateSheetPropertiesRequest setFields(final String fields) {
        /*SL:69*/this.fields = fields;
        /*SL:70*/return this;
    }
    
    public SheetProperties getProperties() {
        /*SL:78*/return this.properties;
    }
    
    public UpdateSheetPropertiesRequest setProperties(final SheetProperties properties) {
        /*SL:86*/this.properties = properties;
        /*SL:87*/return this;
    }
    
    public UpdateSheetPropertiesRequest set(final String a1, final Object a2) {
        /*SL:92*/return (UpdateSheetPropertiesRequest)super.set(a1, a2);
    }
    
    public UpdateSheetPropertiesRequest clone() {
        /*SL:97*/return (UpdateSheetPropertiesRequest)super.clone();
    }
}
