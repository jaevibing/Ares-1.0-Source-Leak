package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class MethodInsnNode extends AbstractInsnNode
{
    public String owner;
    public String name;
    public String desc;
    public boolean itf;
    
    public MethodInsnNode(final int a1, final String a2, final String a3, final String a4) {
        this(a1, a2, a3, a4, a1 == 185);
    }
    
    public MethodInsnNode(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        super(a1);
        this.owner = a2;
        this.name = a3;
        this.desc = a4;
        this.itf = a5;
    }
    
    public void setOpcode(final int a1) {
        /*SL:123*/this.opcode = a1;
    }
    
    public int getType() {
        /*SL:128*/return 5;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:133*/a1.visitMethodInsn(this.opcode, this.owner, this.name, this.desc, this.itf);
        /*SL:134*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:139*/return new MethodInsnNode(this.opcode, this.owner, this.name, this.desc, this.itf);
    }
}
