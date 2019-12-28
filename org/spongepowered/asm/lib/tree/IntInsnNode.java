package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class IntInsnNode extends AbstractInsnNode
{
    public int operand;
    
    public IntInsnNode(final int a1, final int a2) {
        super(a1);
        this.operand = a2;
    }
    
    public void setOpcode(final int a1) {
        /*SL:70*/this.opcode = a1;
    }
    
    public int getType() {
        /*SL:75*/return 1;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:80*/a1.visitIntInsn(this.opcode, this.operand);
        /*SL:81*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:86*/return new IntInsnNode(this.opcode, this.operand).cloneAnnotations(this);
    }
}
