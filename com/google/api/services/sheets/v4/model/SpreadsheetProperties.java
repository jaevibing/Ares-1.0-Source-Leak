package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class SpreadsheetProperties extends GenericJson
{
    @Key
    private String autoRecalc;
    @Key
    private CellFormat defaultFormat;
    @Key
    private IterativeCalculationSettings iterativeCalculationSettings;
    @Key
    private String locale;
    @Key
    private String timeZone;
    @Key
    private String title;
    
    public String getAutoRecalc() {
        /*SL:94*/return this.autoRecalc;
    }
    
    public SpreadsheetProperties setAutoRecalc(final String autoRecalc) {
        /*SL:102*/this.autoRecalc = autoRecalc;
        /*SL:103*/return this;
    }
    
    public CellFormat getDefaultFormat() {
        /*SL:112*/return this.defaultFormat;
    }
    
    public SpreadsheetProperties setDefaultFormat(final CellFormat defaultFormat) {
        /*SL:121*/this.defaultFormat = defaultFormat;
        /*SL:122*/return this;
    }
    
    public IterativeCalculationSettings getIterativeCalculationSettings() {
        /*SL:131*/return this.iterativeCalculationSettings;
    }
    
    public SpreadsheetProperties setIterativeCalculationSettings(final IterativeCalculationSettings iterativeCalculationSettings) {
        /*SL:140*/this.iterativeCalculationSettings = iterativeCalculationSettings;
        /*SL:141*/return this;
    }
    
    public String getLocale() {
        /*SL:157*/return this.locale;
    }
    
    public SpreadsheetProperties setLocale(final String locale) {
        /*SL:173*/this.locale = locale;
        /*SL:174*/return this;
    }
    
    public String getTimeZone() {
        /*SL:183*/return this.timeZone;
    }
    
    public SpreadsheetProperties setTimeZone(final String timeZone) {
        /*SL:192*/this.timeZone = timeZone;
        /*SL:193*/return this;
    }
    
    public String getTitle() {
        /*SL:201*/return this.title;
    }
    
    public SpreadsheetProperties setTitle(final String title) {
        /*SL:209*/this.title = title;
        /*SL:210*/return this;
    }
    
    public SpreadsheetProperties set(final String a1, final Object a2) {
        /*SL:215*/return (SpreadsheetProperties)super.set(a1, a2);
    }
    
    public SpreadsheetProperties clone() {
        /*SL:220*/return (SpreadsheetProperties)super.clone();
    }
}
