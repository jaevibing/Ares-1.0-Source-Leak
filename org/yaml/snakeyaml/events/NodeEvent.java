package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

public abstract class NodeEvent extends Event
{
    private final String anchor;
    
    public NodeEvent(final String a1, final Mark a2, final Mark a3) {
        super(a2, a3);
        this.anchor = a1;
    }
    
    public String getAnchor() {
        /*SL:42*/return this.anchor;
    }
    
    @Override
    protected String getArguments() {
        /*SL:47*/return "anchor=" + this.anchor;
    }
}
