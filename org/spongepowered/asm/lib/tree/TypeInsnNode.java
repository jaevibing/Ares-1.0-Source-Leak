package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class TypeInsnNode extends AbstractInsnNode
{
    public String desc;
    
    public TypeInsnNode(final int a1, final String a2) {
        super(a1);
        this.desc = a2;
    }
    
    public void setOpcode(final int a1) {
        /*SL:73*/this.opcode = a1;
    }
    
    public int getType() {
        /*SL:78*/return 3;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:83*/a1.visitTypeInsn(this.opcode, this.desc);
        /*SL:84*/this.acceptAnnotations(a1);
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> a1) {
        /*SL:89*/return new TypeInsnNode(this.opcode, this.desc).cloneAnnotations(this);
    }
}
