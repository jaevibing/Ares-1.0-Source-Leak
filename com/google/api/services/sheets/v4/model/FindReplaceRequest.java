package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class FindReplaceRequest extends GenericJson
{
    @Key
    private Boolean allSheets;
    @Key
    private String find;
    @Key
    private Boolean includeFormulas;
    @Key
    private Boolean matchCase;
    @Key
    private Boolean matchEntireCell;
    @Key
    private GridRange range;
    @Key
    private String replacement;
    @Key
    private Boolean searchByRegex;
    @Key
    private Integer sheetId;
    
    public Boolean getAllSheets() {
        /*SL:109*/return this.allSheets;
    }
    
    public FindReplaceRequest setAllSheets(final Boolean allSheets) {
        /*SL:117*/this.allSheets = allSheets;
        /*SL:118*/return this;
    }
    
    public String getFind() {
        /*SL:126*/return this.find;
    }
    
    public FindReplaceRequest setFind(final String find) {
        /*SL:134*/this.find = find;
        /*SL:135*/return this;
    }
    
    public Boolean getIncludeFormulas() {
        /*SL:143*/return this.includeFormulas;
    }
    
    public FindReplaceRequest setIncludeFormulas(final Boolean includeFormulas) {
        /*SL:151*/this.includeFormulas = includeFormulas;
        /*SL:152*/return this;
    }
    
    public Boolean getMatchCase() {
        /*SL:160*/return this.matchCase;
    }
    
    public FindReplaceRequest setMatchCase(final Boolean matchCase) {
        /*SL:168*/this.matchCase = matchCase;
        /*SL:169*/return this;
    }
    
    public Boolean getMatchEntireCell() {
        /*SL:177*/return this.matchEntireCell;
    }
    
    public FindReplaceRequest setMatchEntireCell(final Boolean matchEntireCell) {
        /*SL:185*/this.matchEntireCell = matchEntireCell;
        /*SL:186*/return this;
    }
    
    public GridRange getRange() {
        /*SL:194*/return this.range;
    }
    
    public FindReplaceRequest setRange(final GridRange range) {
        /*SL:202*/this.range = range;
        /*SL:203*/return this;
    }
    
    public String getReplacement() {
        /*SL:211*/return this.replacement;
    }
    
    public FindReplaceRequest setReplacement(final String replacement) {
        /*SL:219*/this.replacement = replacement;
        /*SL:220*/return this;
    }
    
    public Boolean getSearchByRegex() {
        /*SL:233*/return this.searchByRegex;
    }
    
    public FindReplaceRequest setSearchByRegex(final Boolean searchByRegex) {
        /*SL:246*/this.searchByRegex = searchByRegex;
        /*SL:247*/return this;
    }
    
    public Integer getSheetId() {
        /*SL:255*/return this.sheetId;
    }
    
    public FindReplaceRequest setSheetId(final Integer sheetId) {
        /*SL:263*/this.sheetId = sheetId;
        /*SL:264*/return this;
    }
    
    public FindReplaceRequest set(final String a1, final Object a2) {
        /*SL:269*/return (FindReplaceRequest)super.set(a1, a2);
    }
    
    public FindReplaceRequest clone() {
        /*SL:274*/return (FindReplaceRequest)super.clone();
    }
}
