package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public final class ScalarEvent extends NodeEvent
{
    private final String tag;
    private final Character style;
    private final String value;
    private final ImplicitTuple implicit;
    
    public ScalarEvent(final String a1, final String a2, final ImplicitTuple a3, final String a4, final Mark a5, final Mark a6, final Character a7) {
        super(a1, a5, a6);
        this.tag = a2;
        this.implicit = a3;
        this.value = a4;
        this.style = a7;
    }
    
    public String getTag() {
        /*SL:50*/return this.tag;
    }
    
    public Character getStyle() {
        /*SL:73*/return this.style;
    }
    
    public String getValue() {
        /*SL:85*/return this.value;
    }
    
    public ImplicitTuple getImplicit() {
        /*SL:89*/return this.implicit;
    }
    
    @Override
    protected String getArguments() {
        /*SL:94*/return super.getArguments() + ", tag=" + this.tag + ", " + this.implicit + ", value=" + this.value;
    }
    
    @Override
    public boolean is(final ID a1) {
        /*SL:99*/return ID.Scalar == a1;
    }
}
