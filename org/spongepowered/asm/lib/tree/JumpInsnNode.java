package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class JumpInsnNode extends AbstractInsnNode
{
    public LabelNode label;
    
    public JumpInsnNode(final int a1, final LabelNode a2) {
        super(a1);
        this.label = a2;
    }
    
    public void setOpcode(final int a1) {
        /*SL:78*/this.opcode = a1;
    }
    
    public int getType() {
        /*SL:83*/return 7;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:88*/a1.visitJumpInsn(this.opcode, this.label.getLabel());
        /*SL:89*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:94*/return new JumpInsnNode(this.opcode, AbstractInsnNode.clone(this.label, a1)).cloneAnnotations(this);
    }
}
