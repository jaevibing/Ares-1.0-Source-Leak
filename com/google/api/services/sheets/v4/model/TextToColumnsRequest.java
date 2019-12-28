package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class TextToColumnsRequest extends GenericJson
{
    @Key
    private String delimiter;
    @Key
    private String delimiterType;
    @Key
    private GridRange source;
    
    public String getDelimiter() {
        /*SL:62*/return this.delimiter;
    }
    
    public TextToColumnsRequest setDelimiter(final String delimiter) {
        /*SL:70*/this.delimiter = delimiter;
        /*SL:71*/return this;
    }
    
    public String getDelimiterType() {
        /*SL:79*/return this.delimiterType;
    }
    
    public TextToColumnsRequest setDelimiterType(final String delimiterType) {
        /*SL:87*/this.delimiterType = delimiterType;
        /*SL:88*/return this;
    }
    
    public GridRange getSource() {
        /*SL:96*/return this.source;
    }
    
    public TextToColumnsRequest setSource(final GridRange source) {
        /*SL:104*/this.source = source;
        /*SL:105*/return this;
    }
    
    public TextToColumnsRequest set(final String a1, final Object a2) {
        /*SL:110*/return (TextToColumnsRequest)super.set(a1, a2);
    }
    
    public TextToColumnsRequest clone() {
        /*SL:115*/return (TextToColumnsRequest)super.clone();
    }
}
