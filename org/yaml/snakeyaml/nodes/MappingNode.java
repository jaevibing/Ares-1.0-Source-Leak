package org.yaml.snakeyaml.nodes;

import java.util.Iterator;
import org.yaml.snakeyaml.error.Mark;
import java.util.List;

public class MappingNode extends CollectionNode<NodeTuple>
{
    private List<NodeTuple> value;
    private boolean merged;
    
    public MappingNode(final Tag a1, final boolean a2, final List<NodeTuple> a3, final Mark a4, final Mark a5, final Boolean a6) {
        super(a1, a4, a5, a6);
        this.merged = false;
        if (a3 == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = a3;
        this.resolved = a2;
    }
    
    public MappingNode(final Tag a1, final List<NodeTuple> a2, final Boolean a3) {
        this(a1, true, a2, null, null, a3);
    }
    
    @Override
    public NodeId getNodeId() {
        /*SL:48*/return NodeId.mapping;
    }
    
    @Override
    public List<NodeTuple> getValue() {
        /*SL:57*/return this.value;
    }
    
    public void setValue(final List<NodeTuple> a1) {
        /*SL:61*/this.value = a1;
    }
    
    public void setOnlyKeyType(final Class<?> v2) {
        /*SL:65*/for (final NodeTuple a1 : this.value) {
            /*SL:66*/a1.getKeyNode().setType(v2);
        }
    }
    
    public void setTypes(final Class<?> v1, final Class<?> v2) {
        /*SL:71*/for (final NodeTuple a1 : this.value) {
            /*SL:72*/a1.getValueNode().setType(v2);
            /*SL:73*/a1.getKeyNode().setType(v1);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = /*EL:80*/new StringBuilder();
        /*SL:81*/for (final NodeTuple v1 : this.getValue()) {
            /*SL:82*/sb.append("{ key=");
            /*SL:83*/sb.append(v1.getKeyNode());
            /*SL:84*/sb.append("; value=");
            /*SL:85*/if (v1.getValueNode() instanceof CollectionNode) {
                /*SL:87*/sb.append(System.identityHashCode(v1.getValueNode()));
            }
            else {
                /*SL:89*/sb.append(v1.toString());
            }
            /*SL:91*/sb.append(" }");
        }
        final String string = /*EL:93*/sb.toString();
        /*SL:94*/return "<" + this.getClass().getName() + " (tag=" + this.getTag() + ", values=" + string + ")>";
    }
    
    public void setMerged(final boolean a1) {
        /*SL:102*/this.merged = a1;
    }
    
    public boolean isMerged() {
        /*SL:109*/return this.merged;
    }
}
