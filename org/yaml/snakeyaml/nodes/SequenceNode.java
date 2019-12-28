package org.yaml.snakeyaml.nodes;

import java.util.Iterator;
import org.yaml.snakeyaml.error.Mark;
import java.util.List;

public class SequenceNode extends CollectionNode<Node>
{
    private final List<Node> value;
    
    public SequenceNode(final Tag a1, final boolean a2, final List<Node> a3, final Mark a4, final Mark a5, final Boolean a6) {
        super(a1, a4, a5, a6);
        if (a3 == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = a3;
        this.resolved = a2;
    }
    
    public SequenceNode(final Tag a1, final List<Node> a2, final Boolean a3) {
        this(a1, true, a2, null, null, a3);
    }
    
    @Override
    public NodeId getNodeId() {
        /*SL:47*/return NodeId.sequence;
    }
    
    @Override
    public List<Node> getValue() {
        /*SL:56*/return this.value;
    }
    
    public void setListType(final Class<?> v2) {
        /*SL:60*/for (final Node a1 : this.value) {
            /*SL:61*/a1.setType(v2);
        }
    }
    
    @Override
    public String toString() {
        /*SL:66*/return "<" + this.getClass().getName() + " (tag=" + this.getTag() + ", value=" + this.getValue() + ")>";
    }
}
