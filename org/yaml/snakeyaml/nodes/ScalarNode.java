package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.error.Mark;

public class ScalarNode extends Node
{
    private Character style;
    private String value;
    
    public ScalarNode(final Tag a1, final String a2, final Mark a3, final Mark a4, final Character a5) {
        this(a1, true, a2, a3, a4, a5);
    }
    
    public ScalarNode(final Tag a1, final boolean a2, final String a3, final Mark a4, final Mark a5, final Character a6) {
        super(a1, a4, a5);
        if (a3 == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = a3;
        this.style = a6;
        this.resolved = a2;
    }
    
    public Character getStyle() {
        /*SL:54*/return this.style;
    }
    
    @Override
    public NodeId getNodeId() {
        /*SL:59*/return NodeId.scalar;
    }
    
    public String getValue() {
        /*SL:68*/return this.value;
    }
    
    @Override
    public String toString() {
        /*SL:72*/return "<" + this.getClass().getName() + " (tag=" + this.getTag() + ", value=" + this.getValue() + ")>";
    }
}
