package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class TextPosition extends GenericJson
{
    @Key
    private String horizontalAlignment;
    
    public String getHorizontalAlignment() {
        /*SL:48*/return this.horizontalAlignment;
    }
    
    public TextPosition setHorizontalAlignment(final String horizontalAlignment) {
        /*SL:56*/this.horizontalAlignment = horizontalAlignment;
        /*SL:57*/return this;
    }
    
    public TextPosition set(final String a1, final Object a2) {
        /*SL:62*/return (TextPosition)super.set(a1, a2);
    }
    
    public TextPosition clone() {
        /*SL:67*/return (TextPosition)super.clone();
    }
}
