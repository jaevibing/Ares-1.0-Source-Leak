package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class LookupSwitchInsnNode extends AbstractInsnNode
{
    public LabelNode dflt;
    public List<Integer> keys;
    public List<LabelNode> labels;
    
    public LookupSwitchInsnNode(final LabelNode a3, final int[] v1, final LabelNode[] v2) {
        super(171);
        this.dflt = a3;
        this.keys = new ArrayList<Integer>((v1 == null) ? 0 : v1.length);
        this.labels = new ArrayList<LabelNode>((v2 == null) ? 0 : v2.length);
        if (v1 != null) {
            for (int a4 = 0; a4 < v1.length; ++a4) {
                this.keys.add(v1[a4]);
            }
        }
        if (v2 != null) {
            this.labels.addAll(Arrays.<LabelNode>asList(v2));
        }
    }
    
    public int getType() {
        /*SL:94*/return 12;
    }
    
    public void accept(final MethodVisitor v-2) {
        final int[] a2 = /*EL:99*/new int[this.keys.size()];
        /*SL:100*/for (int a1 = 0; a1 < a2.length; ++a1) {
            /*SL:101*/a2[a1] = this.keys.get(a1);
        }
        final Label[] v0 = /*EL:103*/new Label[this.labels.size()];
        /*SL:104*/for (int v = 0; v < v0.length; ++v) {
            /*SL:105*/v0[v] = this.labels.get(v).getLabel();
        }
        /*SL:107*/v-2.visitLookupSwitchInsn(this.dflt.getLabel(), a2, v0);
        /*SL:108*/this.acceptAnnotations(v-2);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        final LookupSwitchInsnNode v1 = /*EL:114*/new LookupSwitchInsnNode(AbstractInsnNode.clone(this.dflt, a1), null, AbstractInsnNode.clone(this.labels, a1));
        /*SL:115*/v1.keys.addAll(this.keys);
        /*SL:116*/return v1.cloneAnnotations(this);
    }
}
