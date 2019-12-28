package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class TextFormat extends GenericJson
{
    @Key
    private Boolean bold;
    @Key
    private String fontFamily;
    @Key
    private Integer fontSize;
    @Key
    private Color foregroundColor;
    @Key
    private Boolean italic;
    @Key
    private Boolean strikethrough;
    @Key
    private Boolean underline;
    
    public Boolean getBold() {
        /*SL:90*/return this.bold;
    }
    
    public TextFormat setBold(final Boolean bold) {
        /*SL:98*/this.bold = bold;
        /*SL:99*/return this;
    }
    
    public String getFontFamily() {
        /*SL:107*/return this.fontFamily;
    }
    
    public TextFormat setFontFamily(final String fontFamily) {
        /*SL:115*/this.fontFamily = fontFamily;
        /*SL:116*/return this;
    }
    
    public Integer getFontSize() {
        /*SL:124*/return this.fontSize;
    }
    
    public TextFormat setFontSize(final Integer fontSize) {
        /*SL:132*/this.fontSize = fontSize;
        /*SL:133*/return this;
    }
    
    public Color getForegroundColor() {
        /*SL:141*/return this.foregroundColor;
    }
    
    public TextFormat setForegroundColor(final Color foregroundColor) {
        /*SL:149*/this.foregroundColor = foregroundColor;
        /*SL:150*/return this;
    }
    
    public Boolean getItalic() {
        /*SL:158*/return this.italic;
    }
    
    public TextFormat setItalic(final Boolean italic) {
        /*SL:166*/this.italic = italic;
        /*SL:167*/return this;
    }
    
    public Boolean getStrikethrough() {
        /*SL:175*/return this.strikethrough;
    }
    
    public TextFormat setStrikethrough(final Boolean strikethrough) {
        /*SL:183*/this.strikethrough = strikethrough;
        /*SL:184*/return this;
    }
    
    public Boolean getUnderline() {
        /*SL:192*/return this.underline;
    }
    
    public TextFormat setUnderline(final Boolean underline) {
        /*SL:200*/this.underline = underline;
        /*SL:201*/return this;
    }
    
    public TextFormat set(final String a1, final Object a2) {
        /*SL:206*/return (TextFormat)super.set(a1, a2);
    }
    
    public TextFormat clone() {
        /*SL:211*/return (TextFormat)super.clone();
    }
}
