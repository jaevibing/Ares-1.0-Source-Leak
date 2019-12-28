package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class CellFormat extends GenericJson
{
    @Key
    private Color backgroundColor;
    @Key
    private Borders borders;
    @Key
    private String horizontalAlignment;
    @Key
    private String hyperlinkDisplayType;
    @Key
    private NumberFormat numberFormat;
    @Key
    private Padding padding;
    @Key
    private String textDirection;
    @Key
    private TextFormat textFormat;
    @Key
    private TextRotation textRotation;
    @Key
    private String verticalAlignment;
    @Key
    private String wrapStrategy;
    
    public Color getBackgroundColor() {
        /*SL:118*/return this.backgroundColor;
    }
    
    public CellFormat setBackgroundColor(final Color backgroundColor) {
        /*SL:126*/this.backgroundColor = backgroundColor;
        /*SL:127*/return this;
    }
    
    public Borders getBorders() {
        /*SL:135*/return this.borders;
    }
    
    public CellFormat setBorders(final Borders borders) {
        /*SL:143*/this.borders = borders;
        /*SL:144*/return this;
    }
    
    public String getHorizontalAlignment() {
        /*SL:152*/return this.horizontalAlignment;
    }
    
    public CellFormat setHorizontalAlignment(final String horizontalAlignment) {
        /*SL:160*/this.horizontalAlignment = horizontalAlignment;
        /*SL:161*/return this;
    }
    
    public String getHyperlinkDisplayType() {
        /*SL:169*/return this.hyperlinkDisplayType;
    }
    
    public CellFormat setHyperlinkDisplayType(final String hyperlinkDisplayType) {
        /*SL:177*/this.hyperlinkDisplayType = hyperlinkDisplayType;
        /*SL:178*/return this;
    }
    
    public NumberFormat getNumberFormat() {
        /*SL:186*/return this.numberFormat;
    }
    
    public CellFormat setNumberFormat(final NumberFormat numberFormat) {
        /*SL:194*/this.numberFormat = numberFormat;
        /*SL:195*/return this;
    }
    
    public Padding getPadding() {
        /*SL:203*/return this.padding;
    }
    
    public CellFormat setPadding(final Padding padding) {
        /*SL:211*/this.padding = padding;
        /*SL:212*/return this;
    }
    
    public String getTextDirection() {
        /*SL:220*/return this.textDirection;
    }
    
    public CellFormat setTextDirection(final String textDirection) {
        /*SL:228*/this.textDirection = textDirection;
        /*SL:229*/return this;
    }
    
    public TextFormat getTextFormat() {
        /*SL:237*/return this.textFormat;
    }
    
    public CellFormat setTextFormat(final TextFormat textFormat) {
        /*SL:245*/this.textFormat = textFormat;
        /*SL:246*/return this;
    }
    
    public TextRotation getTextRotation() {
        /*SL:254*/return this.textRotation;
    }
    
    public CellFormat setTextRotation(final TextRotation textRotation) {
        /*SL:262*/this.textRotation = textRotation;
        /*SL:263*/return this;
    }
    
    public String getVerticalAlignment() {
        /*SL:271*/return this.verticalAlignment;
    }
    
    public CellFormat setVerticalAlignment(final String verticalAlignment) {
        /*SL:279*/this.verticalAlignment = verticalAlignment;
        /*SL:280*/return this;
    }
    
    public String getWrapStrategy() {
        /*SL:288*/return this.wrapStrategy;
    }
    
    public CellFormat setWrapStrategy(final String wrapStrategy) {
        /*SL:296*/this.wrapStrategy = wrapStrategy;
        /*SL:297*/return this;
    }
    
    public CellFormat set(final String a1, final Object a2) {
        /*SL:302*/return (CellFormat)super.set(a1, a2);
    }
    
    public CellFormat clone() {
        /*SL:307*/return (CellFormat)super.clone();
    }
}
