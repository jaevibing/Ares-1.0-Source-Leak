package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class FieldInsnNode extends AbstractInsnNode
{
    public String owner;
    public String name;
    public String desc;
    
    public FieldInsnNode(final int a1, final String a2, final String a3, final String a4) {
        super(a1);
        this.owner = a2;
        this.name = a3;
        this.desc = a4;
    }
    
    public void setOpcode(final int a1) {
        /*SL:91*/this.opcode = a1;
    }
    
    public int getType() {
        /*SL:96*/return 4;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:101*/a1.visitFieldInsn(this.opcode, this.owner, this.name, this.desc);
        /*SL:102*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:107*/return new FieldInsnNode(this.opcode, this.owner, this.name, this.desc).cloneAnnotations(this);
    }
}
