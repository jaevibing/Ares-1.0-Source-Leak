package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Handle;

public class InvokeDynamicInsnNode extends AbstractInsnNode
{
    public String name;
    public String desc;
    public Handle bsm;
    public Object[] bsmArgs;
    
    public InvokeDynamicInsnNode(final String a1, final String a2, final Handle a3, final Object... a4) {
        super(186);
        this.name = a1;
        this.desc = a2;
        this.bsm = a3;
        this.bsmArgs = a4;
    }
    
    public int getType() {
        /*SL:88*/return 6;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:93*/a1.visitInvokeDynamicInsn(this.name, this.desc, this.bsm, this.bsmArgs);
        /*SL:94*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:99*/return new InvokeDynamicInsnNode(this.name, this.desc, this.bsm, this.bsmArgs).cloneAnnotations(this);
    }
}
