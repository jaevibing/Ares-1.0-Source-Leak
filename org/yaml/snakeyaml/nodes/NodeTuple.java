package org.yaml.snakeyaml.nodes;

public final class NodeTuple
{
    private Node keyNode;
    private Node valueNode;
    
    public NodeTuple(final Node a1, final Node a2) {
        if (a1 == null || a2 == null) {
            throw new NullPointerException("Nodes must be provided.");
        }
        this.keyNode = a1;
        this.valueNode = a2;
    }
    
    public Node getKeyNode() {
        /*SL:40*/return this.keyNode;
    }
    
    public Node getValueNode() {
        /*SL:49*/return this.valueNode;
    }
    
    @Override
    public String toString() {
        /*SL:54*/return "<NodeTuple keyNode=" + this.keyNode.toString() + "; valueNode=" + this.valueNode.toString() + ">";
    }
}
