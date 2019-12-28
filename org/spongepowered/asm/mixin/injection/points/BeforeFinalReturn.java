package org.spongepowered.asm.mixin.injection.points;

import java.util.ListIterator;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("TAIL")
public class BeforeFinalReturn extends InjectionPoint
{
    private final IMixinContext context;
    
    public BeforeFinalReturn(final InjectionPointData a1) {
        super(a1);
        this.context = a1.getContext();
    }
    
    @Override
    public boolean checkPriority(final int a1, final int a2) {
        /*SL:71*/return true;
    }
    
    @Override
    public boolean find(final String a3, final InsnList v1, final Collection<AbstractInsnNode> v2) {
        AbstractInsnNode v3 = /*EL:76*/null;
        final int v4 = /*EL:79*/Type.getReturnType(a3).getOpcode(172);
        /*SL:81*/for (final AbstractInsnNode a4 : v1) {
            /*SL:84*/if (a4 instanceof InsnNode && a4.getOpcode() == v4) {
                /*SL:85*/v3 = a4;
            }
        }
        /*SL:90*/if (v3 == null) {
            /*SL:91*/throw new InvalidInjectionException(this.context, "TAIL could not locate a valid RETURN in the target method!");
        }
        /*SL:94*/v2.add(v3);
        /*SL:95*/return true;
    }
}
