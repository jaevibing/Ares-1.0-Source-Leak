package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class TextFormatRun extends GenericJson
{
    @Key
    private TextFormat format;
    @Key
    private Integer startIndex;
    
    public TextFormat getFormat() {
        /*SL:56*/return this.format;
    }
    
    public TextFormatRun setFormat(final TextFormat format) {
        /*SL:64*/this.format = format;
        /*SL:65*/return this;
    }
    
    public Integer getStartIndex() {
        /*SL:73*/return this.startIndex;
    }
    
    public TextFormatRun setStartIndex(final Integer startIndex) {
        /*SL:81*/this.startIndex = startIndex;
        /*SL:82*/return this;
    }
    
    public TextFormatRun set(final String a1, final Object a2) {
        /*SL:87*/return (TextFormatRun)super.set(a1, a2);
    }
    
    public TextFormatRun clone() {
        /*SL:92*/return (TextFormatRun)super.clone();
    }
}
