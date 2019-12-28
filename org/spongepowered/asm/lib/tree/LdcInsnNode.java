package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class LdcInsnNode extends AbstractInsnNode
{
    public Object cst;
    
    public LdcInsnNode(final Object a1) {
        super(18);
        this.cst = a1;
    }
    
    public int getType() {
        /*SL:66*/return 9;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:71*/a1.visitLdcInsn(this.cst);
        /*SL:72*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:77*/return new LdcInsnNode(this.cst).cloneAnnotations(this);
    }
}
