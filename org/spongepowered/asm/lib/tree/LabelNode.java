package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Label;

public class LabelNode extends AbstractInsnNode
{
    private Label label;
    
    public LabelNode() {
        super(-1);
    }
    
    public LabelNode(final Label a1) {
        super(-1);
        this.label = a1;
    }
    
    public int getType() {
        /*SL:55*/return 8;
    }
    
    public Label getLabel() {
        /*SL:59*/if (this.label == null) {
            /*SL:60*/this.label = new Label();
        }
        /*SL:62*/return this.label;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:67*/a1.visitLabel(this.getLabel());
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:72*/return a1.get(this);
    }
    
    public void resetLabel() {
        /*SL:76*/this.label = null;
    }
}
