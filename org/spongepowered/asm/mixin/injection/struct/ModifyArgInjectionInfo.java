package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.injection.invoke.ModifyArgInjector;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

public class ModifyArgInjectionInfo extends InjectionInfo
{
    public ModifyArgInjectionInfo(final MixinTargetContext a1, final MethodNode a2, final AnnotationNode a3) {
        super(a1, a2, a3);
    }
    
    @Override
    protected Injector parseInjector(final AnnotationNode a1) {
        final int v1 = /*EL:46*/Annotations.<Integer>getValue(a1, "index", Integer.valueOf(-1));
        /*SL:48*/return new ModifyArgInjector(this, v1);
    }
    
    @Override
    protected String getDescription() {
        /*SL:53*/return "Argument modifier method";
    }
}
