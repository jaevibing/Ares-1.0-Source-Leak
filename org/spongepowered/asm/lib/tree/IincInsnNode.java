package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class IincInsnNode extends AbstractInsnNode
{
    public int var;
    public int incr;
    
    public IincInsnNode(final int a1, final int a2) {
        super(132);
        this.var = a1;
        this.incr = a2;
    }
    
    public int getType() {
        /*SL:70*/return 10;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:75*/a1.visitIincInsn(this.var, this.incr);
        /*SL:76*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:81*/return new IincInsnNode(this.var, this.incr).cloneAnnotations(this);
    }
}
