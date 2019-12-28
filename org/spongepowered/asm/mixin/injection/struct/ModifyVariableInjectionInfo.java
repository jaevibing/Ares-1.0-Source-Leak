package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.injection.modify.ModifyVariableInjector;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

public class ModifyVariableInjectionInfo extends InjectionInfo
{
    public ModifyVariableInjectionInfo(final MixinTargetContext a1, final MethodNode a2, final AnnotationNode a3) {
        super(a1, a2, a3);
    }
    
    @Override
    protected Injector parseInjector(final AnnotationNode a1) {
        /*SL:46*/return new ModifyVariableInjector(this, LocalVariableDiscriminator.parse(a1));
    }
    
    @Override
    protected String getDescription() {
        /*SL:51*/return "Variable modifier method";
    }
}
