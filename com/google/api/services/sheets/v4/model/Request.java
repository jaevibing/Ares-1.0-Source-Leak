package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class Request extends GenericJson
{
    @Key
    private AddBandingRequest addBanding;
    @Key
    private AddChartRequest addChart;
    @Key
    private AddConditionalFormatRuleRequest addConditionalFormatRule;
    @Key
    private AddDimensionGroupRequest addDimensionGroup;
    @Key
    private AddFilterViewRequest addFilterView;
    @Key
    private AddNamedRangeRequest addNamedRange;
    @Key
    private AddProtectedRangeRequest addProtectedRange;
    @Key
    private AddSheetRequest addSheet;
    @Key
    private AppendCellsRequest appendCells;
    @Key
    private AppendDimensionRequest appendDimension;
    @Key
    private AutoFillRequest autoFill;
    @Key
    private AutoResizeDimensionsRequest autoResizeDimensions;
    @Key
    private ClearBasicFilterRequest clearBasicFilter;
    @Key
    private CopyPasteRequest copyPaste;
    @Key
    private CreateDeveloperMetadataRequest createDeveloperMetadata;
    @Key
    private CutPasteRequest cutPaste;
    @Key
    private DeleteBandingRequest deleteBanding;
    @Key
    private DeleteConditionalFormatRuleRequest deleteConditionalFormatRule;
    @Key
    private DeleteDeveloperMetadataRequest deleteDeveloperMetadata;
    @Key
    private DeleteDimensionRequest deleteDimension;
    @Key
    private DeleteDimensionGroupRequest deleteDimensionGroup;
    @Key
    private DeleteEmbeddedObjectRequest deleteEmbeddedObject;
    @Key
    private DeleteFilterViewRequest deleteFilterView;
    @Key
    private DeleteNamedRangeRequest deleteNamedRange;
    @Key
    private DeleteProtectedRangeRequest deleteProtectedRange;
    @Key
    private DeleteRangeRequest deleteRange;
    @Key
    private DeleteSheetRequest deleteSheet;
    @Key
    private DuplicateFilterViewRequest duplicateFilterView;
    @Key
    private DuplicateSheetRequest duplicateSheet;
    @Key
    private FindReplaceRequest findReplace;
    @Key
    private InsertDimensionRequest insertDimension;
    @Key
    private InsertRangeRequest insertRange;
    @Key
    private MergeCellsRequest mergeCells;
    @Key
    private MoveDimensionRequest moveDimension;
    @Key
    private PasteDataRequest pasteData;
    @Key
    private RandomizeRangeRequest randomizeRange;
    @Key
    private RepeatCellRequest repeatCell;
    @Key
    private SetBasicFilterRequest setBasicFilter;
    @Key
    private SetDataValidationRequest setDataValidation;
    @Key
    private SortRangeRequest sortRange;
    @Key
    private TextToColumnsRequest textToColumns;
    @Key
    private UnmergeCellsRequest unmergeCells;
    @Key
    private UpdateBandingRequest updateBanding;
    @Key
    private UpdateBordersRequest updateBorders;
    @Key
    private UpdateCellsRequest updateCells;
    @Key
    private UpdateChartSpecRequest updateChartSpec;
    @Key
    private UpdateConditionalFormatRuleRequest updateConditionalFormatRule;
    @Key
    private UpdateDeveloperMetadataRequest updateDeveloperMetadata;
    @Key
    private UpdateDimensionGroupRequest updateDimensionGroup;
    @Key
    private UpdateDimensionPropertiesRequest updateDimensionProperties;
    @Key
    private UpdateEmbeddedObjectPositionRequest updateEmbeddedObjectPosition;
    @Key
    private UpdateFilterViewRequest updateFilterView;
    @Key
    private UpdateNamedRangeRequest updateNamedRange;
    @Key
    private UpdateProtectedRangeRequest updateProtectedRange;
    @Key
    private UpdateSheetPropertiesRequest updateSheetProperties;
    @Key
    private UpdateSpreadsheetPropertiesRequest updateSpreadsheetProperties;
    
    public AddBandingRequest getAddBanding() {
        /*SL:434*/return this.addBanding;
    }
    
    public Request setAddBanding(final AddBandingRequest addBanding) {
        /*SL:442*/this.addBanding = addBanding;
        /*SL:443*/return this;
    }
    
    public AddChartRequest getAddChart() {
        /*SL:451*/return this.addChart;
    }
    
    public Request setAddChart(final AddChartRequest addChart) {
        /*SL:459*/this.addChart = addChart;
        /*SL:460*/return this;
    }
    
    public AddConditionalFormatRuleRequest getAddConditionalFormatRule() {
        /*SL:468*/return this.addConditionalFormatRule;
    }
    
    public Request setAddConditionalFormatRule(final AddConditionalFormatRuleRequest addConditionalFormatRule) {
        /*SL:476*/this.addConditionalFormatRule = addConditionalFormatRule;
        /*SL:477*/return this;
    }
    
    public AddDimensionGroupRequest getAddDimensionGroup() {
        /*SL:485*/return this.addDimensionGroup;
    }
    
    public Request setAddDimensionGroup(final AddDimensionGroupRequest addDimensionGroup) {
        /*SL:493*/this.addDimensionGroup = addDimensionGroup;
        /*SL:494*/return this;
    }
    
    public AddFilterViewRequest getAddFilterView() {
        /*SL:502*/return this.addFilterView;
    }
    
    public Request setAddFilterView(final AddFilterViewRequest addFilterView) {
        /*SL:510*/this.addFilterView = addFilterView;
        /*SL:511*/return this;
    }
    
    public AddNamedRangeRequest getAddNamedRange() {
        /*SL:519*/return this.addNamedRange;
    }
    
    public Request setAddNamedRange(final AddNamedRangeRequest addNamedRange) {
        /*SL:527*/this.addNamedRange = addNamedRange;
        /*SL:528*/return this;
    }
    
    public AddProtectedRangeRequest getAddProtectedRange() {
        /*SL:536*/return this.addProtectedRange;
    }
    
    public Request setAddProtectedRange(final AddProtectedRangeRequest addProtectedRange) {
        /*SL:544*/this.addProtectedRange = addProtectedRange;
        /*SL:545*/return this;
    }
    
    public AddSheetRequest getAddSheet() {
        /*SL:553*/return this.addSheet;
    }
    
    public Request setAddSheet(final AddSheetRequest addSheet) {
        /*SL:561*/this.addSheet = addSheet;
        /*SL:562*/return this;
    }
    
    public AppendCellsRequest getAppendCells() {
        /*SL:570*/return this.appendCells;
    }
    
    public Request setAppendCells(final AppendCellsRequest appendCells) {
        /*SL:578*/this.appendCells = appendCells;
        /*SL:579*/return this;
    }
    
    public AppendDimensionRequest getAppendDimension() {
        /*SL:587*/return this.appendDimension;
    }
    
    public Request setAppendDimension(final AppendDimensionRequest appendDimension) {
        /*SL:595*/this.appendDimension = appendDimension;
        /*SL:596*/return this;
    }
    
    public AutoFillRequest getAutoFill() {
        /*SL:604*/return this.autoFill;
    }
    
    public Request setAutoFill(final AutoFillRequest autoFill) {
        /*SL:612*/this.autoFill = autoFill;
        /*SL:613*/return this;
    }
    
    public AutoResizeDimensionsRequest getAutoResizeDimensions() {
        /*SL:622*/return this.autoResizeDimensions;
    }
    
    public Request setAutoResizeDimensions(final AutoResizeDimensionsRequest autoResizeDimensions) {
        /*SL:631*/this.autoResizeDimensions = autoResizeDimensions;
        /*SL:632*/return this;
    }
    
    public ClearBasicFilterRequest getClearBasicFilter() {
        /*SL:640*/return this.clearBasicFilter;
    }
    
    public Request setClearBasicFilter(final ClearBasicFilterRequest clearBasicFilter) {
        /*SL:648*/this.clearBasicFilter = clearBasicFilter;
        /*SL:649*/return this;
    }
    
    public CopyPasteRequest getCopyPaste() {
        /*SL:657*/return this.copyPaste;
    }
    
    public Request setCopyPaste(final CopyPasteRequest copyPaste) {
        /*SL:665*/this.copyPaste = copyPaste;
        /*SL:666*/return this;
    }
    
    public CreateDeveloperMetadataRequest getCreateDeveloperMetadata() {
        /*SL:674*/return this.createDeveloperMetadata;
    }
    
    public Request setCreateDeveloperMetadata(final CreateDeveloperMetadataRequest createDeveloperMetadata) {
        /*SL:682*/this.createDeveloperMetadata = createDeveloperMetadata;
        /*SL:683*/return this;
    }
    
    public CutPasteRequest getCutPaste() {
        /*SL:691*/return this.cutPaste;
    }
    
    public Request setCutPaste(final CutPasteRequest cutPaste) {
        /*SL:699*/this.cutPaste = cutPaste;
        /*SL:700*/return this;
    }
    
    public DeleteBandingRequest getDeleteBanding() {
        /*SL:708*/return this.deleteBanding;
    }
    
    public Request setDeleteBanding(final DeleteBandingRequest deleteBanding) {
        /*SL:716*/this.deleteBanding = deleteBanding;
        /*SL:717*/return this;
    }
    
    public DeleteConditionalFormatRuleRequest getDeleteConditionalFormatRule() {
        /*SL:725*/return this.deleteConditionalFormatRule;
    }
    
    public Request setDeleteConditionalFormatRule(final DeleteConditionalFormatRuleRequest deleteConditionalFormatRule) {
        /*SL:733*/this.deleteConditionalFormatRule = deleteConditionalFormatRule;
        /*SL:734*/return this;
    }
    
    public DeleteDeveloperMetadataRequest getDeleteDeveloperMetadata() {
        /*SL:742*/return this.deleteDeveloperMetadata;
    }
    
    public Request setDeleteDeveloperMetadata(final DeleteDeveloperMetadataRequest deleteDeveloperMetadata) {
        /*SL:750*/this.deleteDeveloperMetadata = deleteDeveloperMetadata;
        /*SL:751*/return this;
    }
    
    public DeleteDimensionRequest getDeleteDimension() {
        /*SL:759*/return this.deleteDimension;
    }
    
    public Request setDeleteDimension(final DeleteDimensionRequest deleteDimension) {
        /*SL:767*/this.deleteDimension = deleteDimension;
        /*SL:768*/return this;
    }
    
    public DeleteDimensionGroupRequest getDeleteDimensionGroup() {
        /*SL:776*/return this.deleteDimensionGroup;
    }
    
    public Request setDeleteDimensionGroup(final DeleteDimensionGroupRequest deleteDimensionGroup) {
        /*SL:784*/this.deleteDimensionGroup = deleteDimensionGroup;
        /*SL:785*/return this;
    }
    
    public DeleteEmbeddedObjectRequest getDeleteEmbeddedObject() {
        /*SL:793*/return this.deleteEmbeddedObject;
    }
    
    public Request setDeleteEmbeddedObject(final DeleteEmbeddedObjectRequest deleteEmbeddedObject) {
        /*SL:801*/this.deleteEmbeddedObject = deleteEmbeddedObject;
        /*SL:802*/return this;
    }
    
    public DeleteFilterViewRequest getDeleteFilterView() {
        /*SL:810*/return this.deleteFilterView;
    }
    
    public Request setDeleteFilterView(final DeleteFilterViewRequest deleteFilterView) {
        /*SL:818*/this.deleteFilterView = deleteFilterView;
        /*SL:819*/return this;
    }
    
    public DeleteNamedRangeRequest getDeleteNamedRange() {
        /*SL:827*/return this.deleteNamedRange;
    }
    
    public Request setDeleteNamedRange(final DeleteNamedRangeRequest deleteNamedRange) {
        /*SL:835*/this.deleteNamedRange = deleteNamedRange;
        /*SL:836*/return this;
    }
    
    public DeleteProtectedRangeRequest getDeleteProtectedRange() {
        /*SL:844*/return this.deleteProtectedRange;
    }
    
    public Request setDeleteProtectedRange(final DeleteProtectedRangeRequest deleteProtectedRange) {
        /*SL:852*/this.deleteProtectedRange = deleteProtectedRange;
        /*SL:853*/return this;
    }
    
    public DeleteRangeRequest getDeleteRange() {
        /*SL:861*/return this.deleteRange;
    }
    
    public Request setDeleteRange(final DeleteRangeRequest deleteRange) {
        /*SL:869*/this.deleteRange = deleteRange;
        /*SL:870*/return this;
    }
    
    public DeleteSheetRequest getDeleteSheet() {
        /*SL:878*/return this.deleteSheet;
    }
    
    public Request setDeleteSheet(final DeleteSheetRequest deleteSheet) {
        /*SL:886*/this.deleteSheet = deleteSheet;
        /*SL:887*/return this;
    }
    
    public DuplicateFilterViewRequest getDuplicateFilterView() {
        /*SL:895*/return this.duplicateFilterView;
    }
    
    public Request setDuplicateFilterView(final DuplicateFilterViewRequest duplicateFilterView) {
        /*SL:903*/this.duplicateFilterView = duplicateFilterView;
        /*SL:904*/return this;
    }
    
    public DuplicateSheetRequest getDuplicateSheet() {
        /*SL:912*/return this.duplicateSheet;
    }
    
    public Request setDuplicateSheet(final DuplicateSheetRequest duplicateSheet) {
        /*SL:920*/this.duplicateSheet = duplicateSheet;
        /*SL:921*/return this;
    }
    
    public FindReplaceRequest getFindReplace() {
        /*SL:929*/return this.findReplace;
    }
    
    public Request setFindReplace(final FindReplaceRequest findReplace) {
        /*SL:937*/this.findReplace = findReplace;
        /*SL:938*/return this;
    }
    
    public InsertDimensionRequest getInsertDimension() {
        /*SL:946*/return this.insertDimension;
    }
    
    public Request setInsertDimension(final InsertDimensionRequest insertDimension) {
        /*SL:954*/this.insertDimension = insertDimension;
        /*SL:955*/return this;
    }
    
    public InsertRangeRequest getInsertRange() {
        /*SL:963*/return this.insertRange;
    }
    
    public Request setInsertRange(final InsertRangeRequest insertRange) {
        /*SL:971*/this.insertRange = insertRange;
        /*SL:972*/return this;
    }
    
    public MergeCellsRequest getMergeCells() {
        /*SL:980*/return this.mergeCells;
    }
    
    public Request setMergeCells(final MergeCellsRequest mergeCells) {
        /*SL:988*/this.mergeCells = mergeCells;
        /*SL:989*/return this;
    }
    
    public MoveDimensionRequest getMoveDimension() {
        /*SL:997*/return this.moveDimension;
    }
    
    public Request setMoveDimension(final MoveDimensionRequest moveDimension) {
        /*SL:1005*/this.moveDimension = moveDimension;
        /*SL:1006*/return this;
    }
    
    public PasteDataRequest getPasteData() {
        /*SL:1014*/return this.pasteData;
    }
    
    public Request setPasteData(final PasteDataRequest pasteData) {
        /*SL:1022*/this.pasteData = pasteData;
        /*SL:1023*/return this;
    }
    
    public RandomizeRangeRequest getRandomizeRange() {
        /*SL:1031*/return this.randomizeRange;
    }
    
    public Request setRandomizeRange(final RandomizeRangeRequest randomizeRange) {
        /*SL:1039*/this.randomizeRange = randomizeRange;
        /*SL:1040*/return this;
    }
    
    public RepeatCellRequest getRepeatCell() {
        /*SL:1048*/return this.repeatCell;
    }
    
    public Request setRepeatCell(final RepeatCellRequest repeatCell) {
        /*SL:1056*/this.repeatCell = repeatCell;
        /*SL:1057*/return this;
    }
    
    public SetBasicFilterRequest getSetBasicFilter() {
        /*SL:1065*/return this.setBasicFilter;
    }
    
    public Request setSetBasicFilter(final SetBasicFilterRequest setBasicFilter) {
        /*SL:1073*/this.setBasicFilter = setBasicFilter;
        /*SL:1074*/return this;
    }
    
    public SetDataValidationRequest getSetDataValidation() {
        /*SL:1082*/return this.setDataValidation;
    }
    
    public Request setSetDataValidation(final SetDataValidationRequest setDataValidation) {
        /*SL:1090*/this.setDataValidation = setDataValidation;
        /*SL:1091*/return this;
    }
    
    public SortRangeRequest getSortRange() {
        /*SL:1099*/return this.sortRange;
    }
    
    public Request setSortRange(final SortRangeRequest sortRange) {
        /*SL:1107*/this.sortRange = sortRange;
        /*SL:1108*/return this;
    }
    
    public TextToColumnsRequest getTextToColumns() {
        /*SL:1116*/return this.textToColumns;
    }
    
    public Request setTextToColumns(final TextToColumnsRequest textToColumns) {
        /*SL:1124*/this.textToColumns = textToColumns;
        /*SL:1125*/return this;
    }
    
    public UnmergeCellsRequest getUnmergeCells() {
        /*SL:1133*/return this.unmergeCells;
    }
    
    public Request setUnmergeCells(final UnmergeCellsRequest unmergeCells) {
        /*SL:1141*/this.unmergeCells = unmergeCells;
        /*SL:1142*/return this;
    }
    
    public UpdateBandingRequest getUpdateBanding() {
        /*SL:1150*/return this.updateBanding;
    }
    
    public Request setUpdateBanding(final UpdateBandingRequest updateBanding) {
        /*SL:1158*/this.updateBanding = updateBanding;
        /*SL:1159*/return this;
    }
    
    public UpdateBordersRequest getUpdateBorders() {
        /*SL:1167*/return this.updateBorders;
    }
    
    public Request setUpdateBorders(final UpdateBordersRequest updateBorders) {
        /*SL:1175*/this.updateBorders = updateBorders;
        /*SL:1176*/return this;
    }
    
    public UpdateCellsRequest getUpdateCells() {
        /*SL:1184*/return this.updateCells;
    }
    
    public Request setUpdateCells(final UpdateCellsRequest updateCells) {
        /*SL:1192*/this.updateCells = updateCells;
        /*SL:1193*/return this;
    }
    
    public UpdateChartSpecRequest getUpdateChartSpec() {
        /*SL:1201*/return this.updateChartSpec;
    }
    
    public Request setUpdateChartSpec(final UpdateChartSpecRequest updateChartSpec) {
        /*SL:1209*/this.updateChartSpec = updateChartSpec;
        /*SL:1210*/return this;
    }
    
    public UpdateConditionalFormatRuleRequest getUpdateConditionalFormatRule() {
        /*SL:1218*/return this.updateConditionalFormatRule;
    }
    
    public Request setUpdateConditionalFormatRule(final UpdateConditionalFormatRuleRequest updateConditionalFormatRule) {
        /*SL:1226*/this.updateConditionalFormatRule = updateConditionalFormatRule;
        /*SL:1227*/return this;
    }
    
    public UpdateDeveloperMetadataRequest getUpdateDeveloperMetadata() {
        /*SL:1235*/return this.updateDeveloperMetadata;
    }
    
    public Request setUpdateDeveloperMetadata(final UpdateDeveloperMetadataRequest updateDeveloperMetadata) {
        /*SL:1243*/this.updateDeveloperMetadata = updateDeveloperMetadata;
        /*SL:1244*/return this;
    }
    
    public UpdateDimensionGroupRequest getUpdateDimensionGroup() {
        /*SL:1252*/return this.updateDimensionGroup;
    }
    
    public Request setUpdateDimensionGroup(final UpdateDimensionGroupRequest updateDimensionGroup) {
        /*SL:1260*/this.updateDimensionGroup = updateDimensionGroup;
        /*SL:1261*/return this;
    }
    
    public UpdateDimensionPropertiesRequest getUpdateDimensionProperties() {
        /*SL:1269*/return this.updateDimensionProperties;
    }
    
    public Request setUpdateDimensionProperties(final UpdateDimensionPropertiesRequest updateDimensionProperties) {
        /*SL:1277*/this.updateDimensionProperties = updateDimensionProperties;
        /*SL:1278*/return this;
    }
    
    public UpdateEmbeddedObjectPositionRequest getUpdateEmbeddedObjectPosition() {
        /*SL:1286*/return this.updateEmbeddedObjectPosition;
    }
    
    public Request setUpdateEmbeddedObjectPosition(final UpdateEmbeddedObjectPositionRequest updateEmbeddedObjectPosition) {
        /*SL:1294*/this.updateEmbeddedObjectPosition = updateEmbeddedObjectPosition;
        /*SL:1295*/return this;
    }
    
    public UpdateFilterViewRequest getUpdateFilterView() {
        /*SL:1303*/return this.updateFilterView;
    }
    
    public Request setUpdateFilterView(final UpdateFilterViewRequest updateFilterView) {
        /*SL:1311*/this.updateFilterView = updateFilterView;
        /*SL:1312*/return this;
    }
    
    public UpdateNamedRangeRequest getUpdateNamedRange() {
        /*SL:1320*/return this.updateNamedRange;
    }
    
    public Request setUpdateNamedRange(final UpdateNamedRangeRequest updateNamedRange) {
        /*SL:1328*/this.updateNamedRange = updateNamedRange;
        /*SL:1329*/return this;
    }
    
    public UpdateProtectedRangeRequest getUpdateProtectedRange() {
        /*SL:1337*/return this.updateProtectedRange;
    }
    
    public Request setUpdateProtectedRange(final UpdateProtectedRangeRequest updateProtectedRange) {
        /*SL:1345*/this.updateProtectedRange = updateProtectedRange;
        /*SL:1346*/return this;
    }
    
    public UpdateSheetPropertiesRequest getUpdateSheetProperties() {
        /*SL:1354*/return this.updateSheetProperties;
    }
    
    public Request setUpdateSheetProperties(final UpdateSheetPropertiesRequest updateSheetProperties) {
        /*SL:1362*/this.updateSheetProperties = updateSheetProperties;
        /*SL:1363*/return this;
    }
    
    public UpdateSpreadsheetPropertiesRequest getUpdateSpreadsheetProperties() {
        /*SL:1371*/return this.updateSpreadsheetProperties;
    }
    
    public Request setUpdateSpreadsheetProperties(final UpdateSpreadsheetPropertiesRequest updateSpreadsheetProperties) {
        /*SL:1379*/this.updateSpreadsheetProperties = updateSpreadsheetProperties;
        /*SL:1380*/return this;
    }
    
    public Request set(final String a1, final Object a2) {
        /*SL:1385*/return (Request)super.set(a1, a2);
    }
    
    public Request clone() {
        /*SL:1390*/return (Request)super.clone();
    }
}
