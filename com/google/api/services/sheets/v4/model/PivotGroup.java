package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class PivotGroup extends GenericJson
{
    @Key
    private PivotGroupRule groupRule;
    @Key
    private String label;
    @Key
    private Boolean repeatHeadings;
    @Key
    private Boolean showTotals;
    @Key
    private String sortOrder;
    @Key
    private Integer sourceColumnOffset;
    @Key
    private PivotGroupSortValueBucket valueBucket;
    @Key
    private List<PivotGroupValueMetadata> valueMetadata;
    
    public PivotGroupRule getGroupRule() {
        /*SL:126*/return this.groupRule;
    }
    
    public PivotGroup setGroupRule(final PivotGroupRule groupRule) {
        /*SL:134*/this.groupRule = groupRule;
        /*SL:135*/return this;
    }
    
    public String getLabel() {
        /*SL:153*/return this.label;
    }
    
    public PivotGroup setLabel(final String label) {
        /*SL:171*/this.label = label;
        /*SL:172*/return this;
    }
    
    public Boolean getRepeatHeadings() {
        /*SL:189*/return this.repeatHeadings;
    }
    
    public PivotGroup setRepeatHeadings(final Boolean repeatHeadings) {
        /*SL:206*/this.repeatHeadings = repeatHeadings;
        /*SL:207*/return this;
    }
    
    public Boolean getShowTotals() {
        /*SL:215*/return this.showTotals;
    }
    
    public PivotGroup setShowTotals(final Boolean showTotals) {
        /*SL:223*/this.showTotals = showTotals;
        /*SL:224*/return this;
    }
    
    public String getSortOrder() {
        /*SL:232*/return this.sortOrder;
    }
    
    public PivotGroup setSortOrder(final String sortOrder) {
        /*SL:240*/this.sortOrder = sortOrder;
        /*SL:241*/return this;
    }
    
    public Integer getSourceColumnOffset() {
        /*SL:252*/return this.sourceColumnOffset;
    }
    
    public PivotGroup setSourceColumnOffset(final Integer sourceColumnOffset) {
        /*SL:263*/this.sourceColumnOffset = sourceColumnOffset;
        /*SL:264*/return this;
    }
    
    public PivotGroupSortValueBucket getValueBucket() {
        /*SL:273*/return this.valueBucket;
    }
    
    public PivotGroup setValueBucket(final PivotGroupSortValueBucket valueBucket) {
        /*SL:282*/this.valueBucket = valueBucket;
        /*SL:283*/return this;
    }
    
    public List<PivotGroupValueMetadata> getValueMetadata() {
        /*SL:291*/return this.valueMetadata;
    }
    
    public PivotGroup setValueMetadata(final List<PivotGroupValueMetadata> valueMetadata) {
        /*SL:299*/this.valueMetadata = valueMetadata;
        /*SL:300*/return this;
    }
    
    public PivotGroup set(final String a1, final Object a2) {
        /*SL:305*/return (PivotGroup)super.set(a1, a2);
    }
    
    public PivotGroup clone() {
        /*SL:310*/return (PivotGroup)super.clone();
    }
    
    static {
        Data.<Object>nullOf(PivotGroupValueMetadata.class);
    }
}
