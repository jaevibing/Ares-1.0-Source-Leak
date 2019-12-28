package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.error.Mark;

public abstract class Node
{
    private Tag tag;
    private Mark startMark;
    protected Mark endMark;
    private Class<?> type;
    private boolean twoStepsConstruction;
    private String anchor;
    protected boolean resolved;
    protected Boolean useClassConstructor;
    
    public Node(final Tag a1, final Mark a2, final Mark a3) {
        this.setTag(a1);
        this.startMark = a2;
        this.endMark = a3;
        this.type = Object.class;
        this.twoStepsConstruction = false;
        this.resolved = true;
        this.useClassConstructor = null;
    }
    
    public Tag getTag() {
        /*SL:65*/return this.tag;
    }
    
    public Mark getEndMark() {
        /*SL:69*/return this.endMark;
    }
    
    public abstract NodeId getNodeId();
    
    public Mark getStartMark() {
        /*SL:81*/return this.startMark;
    }
    
    public void setTag(final Tag a1) {
        /*SL:85*/if (a1 == null) {
            /*SL:86*/throw new NullPointerException("tag in a Node is required.");
        }
        /*SL:88*/this.tag = a1;
    }
    
    @Override
    public final boolean equals(final Object a1) {
        /*SL:96*/return super.equals(a1);
    }
    
    public Class<?> getType() {
        /*SL:100*/return this.type;
    }
    
    public void setType(final Class<?> a1) {
        /*SL:104*/if (!a1.isAssignableFrom(this.type)) {
            /*SL:105*/this.type = a1;
        }
    }
    
    public void setTwoStepsConstruction(final boolean a1) {
        /*SL:110*/this.twoStepsConstruction = a1;
    }
    
    public boolean isTwoStepsConstruction() {
        /*SL:131*/return this.twoStepsConstruction;
    }
    
    @Override
    public final int hashCode() {
        /*SL:136*/return super.hashCode();
    }
    
    public boolean useClassConstructor() {
        /*SL:140*/if (this.useClassConstructor == null) {
            /*SL:141*/return (!this.tag.isSecondary() && this.isResolved() && !Object.class.equals(this.type) && !this.tag.equals(Tag.NULL)) || /*EL:144*/this.tag.isCompatible(this.getType());
        }
        /*SL:152*/return this.useClassConstructor;
    }
    
    public void setUseClassConstructor(final Boolean a1) {
        /*SL:156*/this.useClassConstructor = a1;
    }
    
    public boolean isResolved() {
        /*SL:166*/return this.resolved;
    }
    
    public String getAnchor() {
        /*SL:170*/return this.anchor;
    }
    
    public void setAnchor(final String a1) {
        /*SL:174*/this.anchor = a1;
    }
}
