package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class MultiANewArrayInsnNode extends AbstractInsnNode
{
    public String desc;
    public int dims;
    
    public MultiANewArrayInsnNode(final String a1, final int a2) {
        super(197);
        this.desc = a1;
        this.dims = a2;
    }
    
    public int getType() {
        /*SL:70*/return 13;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:75*/a1.visitMultiANewArrayInsn(this.desc, this.dims);
        /*SL:76*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:81*/return new MultiANewArrayInsnNode(this.desc, this.dims).cloneAnnotations(this);
    }
}
