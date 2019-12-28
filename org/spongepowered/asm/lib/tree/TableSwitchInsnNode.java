package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class TableSwitchInsnNode extends AbstractInsnNode
{
    public int min;
    public int max;
    public LabelNode dflt;
    public List<LabelNode> labels;
    
    public TableSwitchInsnNode(final int a1, final int a2, final LabelNode a3, final LabelNode... a4) {
        super(170);
        this.min = a1;
        this.max = a2;
        this.dflt = a3;
        this.labels = new ArrayList<LabelNode>();
        if (a4 != null) {
            this.labels.addAll(Arrays.<LabelNode>asList(a4));
        }
    }
    
    public int getType() {
        /*SL:96*/return 11;
    }
    
    public void accept(final MethodVisitor v2) {
        final Label[] v3 = /*EL:101*/new Label[this.labels.size()];
        /*SL:102*/for (int a1 = 0; a1 < v3.length; ++a1) {
            /*SL:103*/v3[a1] = this.labels.get(a1).getLabel();
        }
        /*SL:105*/v2.visitTableSwitchInsn(this.min, this.max, this.dflt.getLabel(), v3);
        /*SL:106*/this.acceptAnnotations(v2);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:111*/return new TableSwitchInsnNode(this.min, this.max, AbstractInsnNode.clone(this.dflt, a1), AbstractInsnNode.clone(this.labels, a1)).cloneAnnotations(this);
    }
}
