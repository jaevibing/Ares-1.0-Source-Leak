package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class InsnNode extends AbstractInsnNode
{
    public InsnNode(final int a1) {
        super(a1);
    }
    
    public int getType() {
        /*SL:69*/return 0;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:80*/a1.visitInsn(this.opcode);
        /*SL:81*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:86*/return new InsnNode(this.opcode).cloneAnnotations(this);
    }
}
