package org.yaml.snakeyaml.nodes;

import java.util.List;
import org.yaml.snakeyaml.error.Mark;

public abstract class CollectionNode<T> extends Node
{
    private Boolean flowStyle;
    
    public CollectionNode(final Tag a1, final Mark a2, final Mark a3, final Boolean a4) {
        super(a1, a2, a3);
        this.flowStyle = a4;
    }
    
    public abstract List<T> getValue();
    
    public Boolean getFlowStyle() {
        /*SL:48*/return this.flowStyle;
    }
    
    public void setFlowStyle(final Boolean a1) {
        /*SL:52*/this.flowStyle = a1;
    }
    
    public void setEndMark(final Mark a1) {
        /*SL:56*/this.endMark = a1;
    }
}
