package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class Color extends GenericJson
{
    @Key
    private Float alpha;
    @Key
    private Float blue;
    @Key
    private Float green;
    @Key
    private Float red;
    
    public Float getAlpha() {
        /*SL:152*/return this.alpha;
    }
    
    public Color setAlpha(final Float alpha) {
        /*SL:169*/this.alpha = alpha;
        /*SL:170*/return this;
    }
    
    public Float getBlue() {
        /*SL:178*/return this.blue;
    }
    
    public Color setBlue(final Float blue) {
        /*SL:186*/this.blue = blue;
        /*SL:187*/return this;
    }
    
    public Float getGreen() {
        /*SL:195*/return this.green;
    }
    
    public Color setGreen(final Float green) {
        /*SL:203*/this.green = green;
        /*SL:204*/return this;
    }
    
    public Float getRed() {
        /*SL:212*/return this.red;
    }
    
    public Color setRed(final Float red) {
        /*SL:220*/this.red = red;
        /*SL:221*/return this;
    }
    
    public Color set(final String a1, final Object a2) {
        /*SL:226*/return (Color)super.set(a1, a2);
    }
    
    public Color clone() {
        /*SL:231*/return (Color)super.clone();
    }
}
