package org.yaml.snakeyaml.nodes;

public class AnchorNode extends Node
{
    private Node realNode;
    
    public AnchorNode(final Node a1) {
        super(a1.getTag(), a1.getStartMark(), a1.getEndMark());
        this.realNode = a1;
    }
    
    @Override
    public NodeId getNodeId() {
        /*SL:29*/return NodeId.anchor;
    }
    
    public Node getRealNode() {
        /*SL:33*/return this.realNode;
    }
}
