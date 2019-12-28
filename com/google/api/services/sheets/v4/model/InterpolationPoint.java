package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class InterpolationPoint extends GenericJson
{
    @Key
    private Color color;
    @Key
    private String type;
    @Key
    private String value;
    
    public Color getColor() {
        /*SL:63*/return this.color;
    }
    
    public InterpolationPoint setColor(final Color color) {
        /*SL:71*/this.color = color;
        /*SL:72*/return this;
    }
    
    public String getType() {
        /*SL:80*/return this.type;
    }
    
    public InterpolationPoint setType(final String type) {
        /*SL:88*/this.type = type;
        /*SL:89*/return this;
    }
    
    public String getValue() {
        /*SL:97*/return this.value;
    }
    
    public InterpolationPoint setValue(final String value) {
        /*SL:105*/this.value = value;
        /*SL:106*/return this;
    }
    
    public InterpolationPoint set(final String a1, final Object a2) {
        /*SL:111*/return (InterpolationPoint)super.set(a1, a2);
    }
    
    public InterpolationPoint clone() {
        /*SL:116*/return (InterpolationPoint)super.clone();
    }
}
