package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class ProtectedRange extends GenericJson
{
    @Key
    private String description;
    @Key
    private Editors editors;
    @Key
    private String namedRangeId;
    @Key
    private Integer protectedRangeId;
    @Key
    private GridRange range;
    @Key
    private Boolean requestingUserCanEdit;
    @Key
    private List<GridRange> unprotectedRanges;
    @Key
    private Boolean warningOnly;
    
    public String getDescription() {
        /*SL:118*/return this.description;
    }
    
    public ProtectedRange setDescription(final String description) {
        /*SL:126*/this.description = description;
        /*SL:127*/return this;
    }
    
    public Editors getEditors() {
        /*SL:137*/return this.editors;
    }
    
    public ProtectedRange setEditors(final Editors editors) {
        /*SL:147*/this.editors = editors;
        /*SL:148*/return this;
    }
    
    public String getNamedRangeId() {
        /*SL:158*/return this.namedRangeId;
    }
    
    public ProtectedRange setNamedRangeId(final String namedRangeId) {
        /*SL:168*/this.namedRangeId = namedRangeId;
        /*SL:169*/return this;
    }
    
    public Integer getProtectedRangeId() {
        /*SL:177*/return this.protectedRangeId;
    }
    
    public ProtectedRange setProtectedRangeId(final Integer protectedRangeId) {
        /*SL:185*/this.protectedRangeId = protectedRangeId;
        /*SL:186*/return this;
    }
    
    public GridRange getRange() {
        /*SL:197*/return this.range;
    }
    
    public ProtectedRange setRange(final GridRange range) {
        /*SL:208*/this.range = range;
        /*SL:209*/return this;
    }
    
    public Boolean getRequestingUserCanEdit() {
        /*SL:218*/return this.requestingUserCanEdit;
    }
    
    public ProtectedRange setRequestingUserCanEdit(final Boolean requestingUserCanEdit) {
        /*SL:227*/this.requestingUserCanEdit = requestingUserCanEdit;
        /*SL:228*/return this;
    }
    
    public List<GridRange> getUnprotectedRanges() {
        /*SL:237*/return this.unprotectedRanges;
    }
    
    public ProtectedRange setUnprotectedRanges(final List<GridRange> unprotectedRanges) {
        /*SL:246*/this.unprotectedRanges = unprotectedRanges;
        /*SL:247*/return this;
    }
    
    public Boolean getWarningOnly() {
        /*SL:261*/return this.warningOnly;
    }
    
    public ProtectedRange setWarningOnly(final Boolean warningOnly) {
        /*SL:275*/this.warningOnly = warningOnly;
        /*SL:276*/return this;
    }
    
    public ProtectedRange set(final String a1, final Object a2) {
        /*SL:281*/return (ProtectedRange)super.set(a1, a2);
    }
    
    public ProtectedRange clone() {
        /*SL:286*/return (ProtectedRange)super.clone();
    }
    
    static {
        Data.<Object>nullOf(GridRange.class);
    }
}
