package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;

public class AccessorGeneratorFieldGetter extends AccessorGeneratorField
{
    public AccessorGeneratorFieldGetter(final AccessorInfo a1) {
        super(a1);
    }
    
    @Override
    public MethodNode generate() {
        final MethodNode v1 = /*EL:47*/this.createMethod(this.targetType.getSize(), this.targetType.getSize());
        /*SL:48*/if (this.isInstanceField) {
            /*SL:49*/v1.instructions.add(new VarInsnNode(25, 0));
        }
        final int v2 = /*EL:51*/this.isInstanceField ? 180 : 178;
        /*SL:52*/v1.instructions.add(new FieldInsnNode(v2, this.info.getClassNode().name, this.targetField.name, this.targetField.desc));
        /*SL:53*/v1.instructions.add(new InsnNode(this.targetType.getOpcode(172)));
        /*SL:54*/return v1;
    }
}
