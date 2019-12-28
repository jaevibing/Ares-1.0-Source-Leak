package org.spongepowered.asm.mixin.injection.points;

import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("HEAD")
public class MethodHead extends InjectionPoint
{
    public MethodHead(final InjectionPointData a1) {
        super(a1);
    }
    
    @Override
    public boolean checkPriority(final int a1, final int a2) {
        /*SL:55*/return true;
    }
    
    @Override
    public boolean find(final String a1, final InsnList a2, final Collection<AbstractInsnNode> a3) {
        /*SL:60*/a3.add(a2.getFirst());
        /*SL:61*/return true;
    }
}
