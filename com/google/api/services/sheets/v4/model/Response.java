package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class Response extends GenericJson
{
    @Key
    private AddBandingResponse addBanding;
    @Key
    private AddChartResponse addChart;
    @Key
    private AddDimensionGroupResponse addDimensionGroup;
    @Key
    private AddFilterViewResponse addFilterView;
    @Key
    private AddNamedRangeResponse addNamedRange;
    @Key
    private AddProtectedRangeResponse addProtectedRange;
    @Key
    private AddSheetResponse addSheet;
    @Key
    private CreateDeveloperMetadataResponse createDeveloperMetadata;
    @Key
    private DeleteConditionalFormatRuleResponse deleteConditionalFormatRule;
    @Key
    private DeleteDeveloperMetadataResponse deleteDeveloperMetadata;
    @Key
    private DeleteDimensionGroupResponse deleteDimensionGroup;
    @Key
    private DuplicateFilterViewResponse duplicateFilterView;
    @Key
    private DuplicateSheetResponse duplicateSheet;
    @Key
    private FindReplaceResponse findReplace;
    @Key
    private UpdateConditionalFormatRuleResponse updateConditionalFormatRule;
    @Key
    private UpdateDeveloperMetadataResponse updateDeveloperMetadata;
    @Key
    private UpdateEmbeddedObjectPositionResponse updateEmbeddedObjectPosition;
    
    public AddBandingResponse getAddBanding() {
        /*SL:160*/return this.addBanding;
    }
    
    public Response setAddBanding(final AddBandingResponse addBanding) {
        /*SL:168*/this.addBanding = addBanding;
        /*SL:169*/return this;
    }
    
    public AddChartResponse getAddChart() {
        /*SL:177*/return this.addChart;
    }
    
    public Response setAddChart(final AddChartResponse addChart) {
        /*SL:185*/this.addChart = addChart;
        /*SL:186*/return this;
    }
    
    public AddDimensionGroupResponse getAddDimensionGroup() {
        /*SL:194*/return this.addDimensionGroup;
    }
    
    public Response setAddDimensionGroup(final AddDimensionGroupResponse addDimensionGroup) {
        /*SL:202*/this.addDimensionGroup = addDimensionGroup;
        /*SL:203*/return this;
    }
    
    public AddFilterViewResponse getAddFilterView() {
        /*SL:211*/return this.addFilterView;
    }
    
    public Response setAddFilterView(final AddFilterViewResponse addFilterView) {
        /*SL:219*/this.addFilterView = addFilterView;
        /*SL:220*/return this;
    }
    
    public AddNamedRangeResponse getAddNamedRange() {
        /*SL:228*/return this.addNamedRange;
    }
    
    public Response setAddNamedRange(final AddNamedRangeResponse addNamedRange) {
        /*SL:236*/this.addNamedRange = addNamedRange;
        /*SL:237*/return this;
    }
    
    public AddProtectedRangeResponse getAddProtectedRange() {
        /*SL:245*/return this.addProtectedRange;
    }
    
    public Response setAddProtectedRange(final AddProtectedRangeResponse addProtectedRange) {
        /*SL:253*/this.addProtectedRange = addProtectedRange;
        /*SL:254*/return this;
    }
    
    public AddSheetResponse getAddSheet() {
        /*SL:262*/return this.addSheet;
    }
    
    public Response setAddSheet(final AddSheetResponse addSheet) {
        /*SL:270*/this.addSheet = addSheet;
        /*SL:271*/return this;
    }
    
    public CreateDeveloperMetadataResponse getCreateDeveloperMetadata() {
        /*SL:279*/return this.createDeveloperMetadata;
    }
    
    public Response setCreateDeveloperMetadata(final CreateDeveloperMetadataResponse createDeveloperMetadata) {
        /*SL:287*/this.createDeveloperMetadata = createDeveloperMetadata;
        /*SL:288*/return this;
    }
    
    public DeleteConditionalFormatRuleResponse getDeleteConditionalFormatRule() {
        /*SL:296*/return this.deleteConditionalFormatRule;
    }
    
    public Response setDeleteConditionalFormatRule(final DeleteConditionalFormatRuleResponse deleteConditionalFormatRule) {
        /*SL:304*/this.deleteConditionalFormatRule = deleteConditionalFormatRule;
        /*SL:305*/return this;
    }
    
    public DeleteDeveloperMetadataResponse getDeleteDeveloperMetadata() {
        /*SL:313*/return this.deleteDeveloperMetadata;
    }
    
    public Response setDeleteDeveloperMetadata(final DeleteDeveloperMetadataResponse deleteDeveloperMetadata) {
        /*SL:321*/this.deleteDeveloperMetadata = deleteDeveloperMetadata;
        /*SL:322*/return this;
    }
    
    public DeleteDimensionGroupResponse getDeleteDimensionGroup() {
        /*SL:330*/return this.deleteDimensionGroup;
    }
    
    public Response setDeleteDimensionGroup(final DeleteDimensionGroupResponse deleteDimensionGroup) {
        /*SL:338*/this.deleteDimensionGroup = deleteDimensionGroup;
        /*SL:339*/return this;
    }
    
    public DuplicateFilterViewResponse getDuplicateFilterView() {
        /*SL:347*/return this.duplicateFilterView;
    }
    
    public Response setDuplicateFilterView(final DuplicateFilterViewResponse duplicateFilterView) {
        /*SL:355*/this.duplicateFilterView = duplicateFilterView;
        /*SL:356*/return this;
    }
    
    public DuplicateSheetResponse getDuplicateSheet() {
        /*SL:364*/return this.duplicateSheet;
    }
    
    public Response setDuplicateSheet(final DuplicateSheetResponse duplicateSheet) {
        /*SL:372*/this.duplicateSheet = duplicateSheet;
        /*SL:373*/return this;
    }
    
    public FindReplaceResponse getFindReplace() {
        /*SL:381*/return this.findReplace;
    }
    
    public Response setFindReplace(final FindReplaceResponse findReplace) {
        /*SL:389*/this.findReplace = findReplace;
        /*SL:390*/return this;
    }
    
    public UpdateConditionalFormatRuleResponse getUpdateConditionalFormatRule() {
        /*SL:398*/return this.updateConditionalFormatRule;
    }
    
    public Response setUpdateConditionalFormatRule(final UpdateConditionalFormatRuleResponse updateConditionalFormatRule) {
        /*SL:406*/this.updateConditionalFormatRule = updateConditionalFormatRule;
        /*SL:407*/return this;
    }
    
    public UpdateDeveloperMetadataResponse getUpdateDeveloperMetadata() {
        /*SL:415*/return this.updateDeveloperMetadata;
    }
    
    public Response setUpdateDeveloperMetadata(final UpdateDeveloperMetadataResponse updateDeveloperMetadata) {
        /*SL:423*/this.updateDeveloperMetadata = updateDeveloperMetadata;
        /*SL:424*/return this;
    }
    
    public UpdateEmbeddedObjectPositionResponse getUpdateEmbeddedObjectPosition() {
        /*SL:432*/return this.updateEmbeddedObjectPosition;
    }
    
    public Response setUpdateEmbeddedObjectPosition(final UpdateEmbeddedObjectPositionResponse updateEmbeddedObjectPosition) {
        /*SL:440*/this.updateEmbeddedObjectPosition = updateEmbeddedObjectPosition;
        /*SL:441*/return this;
    }
    
    public Response set(final String a1, final Object a2) {
        /*SL:446*/return (Response)super.set(a1, a2);
    }
    
    public Response clone() {
        /*SL:451*/return (Response)super.clone();
    }
}
