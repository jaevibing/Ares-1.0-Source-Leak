package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class VarInsnNode extends AbstractInsnNode
{
    public int var;
    
    public VarInsnNode(final int a1, final int a2) {
        super(a1);
        this.var = a2;
    }
    
    public void setOpcode(final int a1) {
        /*SL:76*/this.opcode = a1;
    }
    
    public int getType() {
        /*SL:81*/return 2;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:86*/a1.visitVarInsn(this.opcode, this.var);
        /*SL:87*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:92*/return new VarInsnNode(this.opcode, this.var).cloneAnnotations(this);
    }
}
