package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class AddSheetRequest extends GenericJson
{
    @Key
    private SheetProperties properties;
    
    public SheetProperties getProperties() {
        /*SL:54*/return this.properties;
    }
    
    public AddSheetRequest setProperties(final SheetProperties properties) {
        /*SL:64*/this.properties = properties;
        /*SL:65*/return this;
    }
    
    public AddSheetRequest set(final String a1, final Object a2) {
        /*SL:70*/return (AddSheetRequest)super.set(a1, a2);
    }
    
    public AddSheetRequest clone() {
        /*SL:75*/return (AddSheetRequest)super.clone();
    }
}
