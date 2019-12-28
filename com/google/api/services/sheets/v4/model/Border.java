package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class Border extends GenericJson
{
    @Key
    private Color color;
    @Key
    private String style;
    @Key
    private Integer width;
    
    public Color getColor() {
        /*SL:62*/return this.color;
    }
    
    public Border setColor(final Color color) {
        /*SL:70*/this.color = color;
        /*SL:71*/return this;
    }
    
    public String getStyle() {
        /*SL:79*/return this.style;
    }
    
    public Border setStyle(final String style) {
        /*SL:87*/this.style = style;
        /*SL:88*/return this;
    }
    
    public Integer getWidth() {
        /*SL:96*/return this.width;
    }
    
    public Border setWidth(final Integer width) {
        /*SL:104*/this.width = width;
        /*SL:105*/return this;
    }
    
    public Border set(final String a1, final Object a2) {
        /*SL:110*/return (Border)super.set(a1, a2);
    }
    
    public Border clone() {
        /*SL:115*/return (Border)super.clone();
    }
}
