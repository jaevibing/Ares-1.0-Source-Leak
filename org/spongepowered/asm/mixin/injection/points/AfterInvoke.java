package org.spongepowered.asm.mixin.injection.points;

import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("INVOKE_ASSIGN")
public class AfterInvoke extends BeforeInvoke
{
    public AfterInvoke(final InjectionPointData a1) {
        super(a1);
    }
    
    @Override
    protected boolean addInsn(final InsnList a1, final Collection<AbstractInsnNode> a2, AbstractInsnNode a3) {
        final MethodInsnNode v1 = /*EL:78*/(MethodInsnNode)a3;
        /*SL:79*/if (Type.getReturnType(v1.desc) == Type.VOID_TYPE) {
            /*SL:80*/return false;
        }
        /*SL:83*/a3 = InjectionPoint.nextNode(a1, a3);
        /*SL:84*/if (a3 instanceof VarInsnNode && a3.getOpcode() >= 54) {
            /*SL:85*/a3 = InjectionPoint.nextNode(a1, a3);
        }
        /*SL:88*/a2.add(a3);
        /*SL:89*/return true;
    }
}
