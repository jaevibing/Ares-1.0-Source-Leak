package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class Spreadsheet extends GenericJson
{
    @Key
    private List<DeveloperMetadata> developerMetadata;
    @Key
    private List<NamedRange> namedRanges;
    @Key
    private SpreadsheetProperties properties;
    @Key
    private List<Sheet> sheets;
    @Key
    private String spreadsheetId;
    @Key
    private String spreadsheetUrl;
    
    public List<DeveloperMetadata> getDeveloperMetadata() {
        /*SL:95*/return this.developerMetadata;
    }
    
    public Spreadsheet setDeveloperMetadata(final List<DeveloperMetadata> developerMetadata) {
        /*SL:103*/this.developerMetadata = developerMetadata;
        /*SL:104*/return this;
    }
    
    public List<NamedRange> getNamedRanges() {
        /*SL:112*/return this.namedRanges;
    }
    
    public Spreadsheet setNamedRanges(final List<NamedRange> namedRanges) {
        /*SL:120*/this.namedRanges = namedRanges;
        /*SL:121*/return this;
    }
    
    public SpreadsheetProperties getProperties() {
        /*SL:129*/return this.properties;
    }
    
    public Spreadsheet setProperties(final SpreadsheetProperties properties) {
        /*SL:137*/this.properties = properties;
        /*SL:138*/return this;
    }
    
    public List<Sheet> getSheets() {
        /*SL:146*/return this.sheets;
    }
    
    public Spreadsheet setSheets(final List<Sheet> sheets) {
        /*SL:154*/this.sheets = sheets;
        /*SL:155*/return this;
    }
    
    public String getSpreadsheetId() {
        /*SL:163*/return this.spreadsheetId;
    }
    
    public Spreadsheet setSpreadsheetId(final String spreadsheetId) {
        /*SL:171*/this.spreadsheetId = spreadsheetId;
        /*SL:172*/return this;
    }
    
    public String getSpreadsheetUrl() {
        /*SL:180*/return this.spreadsheetUrl;
    }
    
    public Spreadsheet setSpreadsheetUrl(final String spreadsheetUrl) {
        /*SL:188*/this.spreadsheetUrl = spreadsheetUrl;
        /*SL:189*/return this;
    }
    
    public Spreadsheet set(final String a1, final Object a2) {
        /*SL:194*/return (Spreadsheet)super.set(a1, a2);
    }
    
    public Spreadsheet clone() {
        /*SL:199*/return (Spreadsheet)super.clone();
    }
    
    static {
        Data.<Object>nullOf(DeveloperMetadata.class);
        Data.<Object>nullOf(Sheet.class);
    }
}
