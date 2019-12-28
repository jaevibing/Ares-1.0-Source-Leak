package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class BandingProperties extends GenericJson
{
    @Key
    private Color firstBandColor;
    @Key
    private Color footerColor;
    @Key
    private Color headerColor;
    @Key
    private Color secondBandColor;
    
    public Color getFirstBandColor() {
        /*SL:85*/return this.firstBandColor;
    }
    
    public BandingProperties setFirstBandColor(final Color firstBandColor) {
        /*SL:93*/this.firstBandColor = firstBandColor;
        /*SL:94*/return this;
    }
    
    public Color getFooterColor() {
        /*SL:104*/return this.footerColor;
    }
    
    public BandingProperties setFooterColor(final Color footerColor) {
        /*SL:114*/this.footerColor = footerColor;
        /*SL:115*/return this;
    }
    
    public Color getHeaderColor() {
        /*SL:127*/return this.headerColor;
    }
    
    public BandingProperties setHeaderColor(final Color headerColor) {
        /*SL:139*/this.headerColor = headerColor;
        /*SL:140*/return this;
    }
    
    public Color getSecondBandColor() {
        /*SL:148*/return this.secondBandColor;
    }
    
    public BandingProperties setSecondBandColor(final Color secondBandColor) {
        /*SL:156*/this.secondBandColor = secondBandColor;
        /*SL:157*/return this;
    }
    
    public BandingProperties set(final String a1, final Object a2) {
        /*SL:162*/return (BandingProperties)super.set(a1, a2);
    }
    
    public BandingProperties clone() {
        /*SL:167*/return (BandingProperties)super.clone();
    }
}
