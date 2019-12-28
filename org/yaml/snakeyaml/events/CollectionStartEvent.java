package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public abstract class CollectionStartEvent extends NodeEvent
{
    private final String tag;
    private final boolean implicit;
    private final Boolean flowStyle;
    
    public CollectionStartEvent(final String a1, final String a2, final boolean a3, final Mark a4, final Mark a5, final Boolean a6) {
        super(a1, a4, a5);
        this.tag = a2;
        this.implicit = a3;
        this.flowStyle = a6;
    }
    
    public String getTag() {
        /*SL:46*/return this.tag;
    }
    
    public boolean getImplicit() {
        /*SL:56*/return this.implicit;
    }
    
    public Boolean getFlowStyle() {
        /*SL:66*/return this.flowStyle;
    }
    
    @Override
    protected String getArguments() {
        /*SL:71*/return super.getArguments() + ", tag=" + this.tag + ", implicit=" + this.implicit;
    }
}
