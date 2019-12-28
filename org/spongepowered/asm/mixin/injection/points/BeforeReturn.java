package org.spongepowered.asm.mixin.injection.points;

import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("RETURN")
public class BeforeReturn extends InjectionPoint
{
    private final int ordinal;
    
    public BeforeReturn(final InjectionPointData a1) {
        super(a1);
        this.ordinal = a1.getOrdinal();
    }
    
    @Override
    public boolean checkPriority(final int a1, final int a2) {
        /*SL:84*/return true;
    }
    
    @Override
    public boolean find(final String a3, final InsnList v1, final Collection<AbstractInsnNode> v2) {
        boolean v3 = /*EL:89*/false;
        final int v4 = /*EL:92*/Type.getReturnType(a3).getOpcode(172);
        int v5 = /*EL:93*/0;
        /*SL:95*/for (final AbstractInsnNode a4 : v1) {
            /*SL:99*/if (a4 instanceof InsnNode && a4.getOpcode() == v4) {
                /*SL:100*/if (this.ordinal == -1 || this.ordinal == v5) {
                    /*SL:101*/v2.add(a4);
                    /*SL:102*/v3 = true;
                }
                /*SL:105*/++v5;
            }
        }
        /*SL:109*/return v3;
    }
}
