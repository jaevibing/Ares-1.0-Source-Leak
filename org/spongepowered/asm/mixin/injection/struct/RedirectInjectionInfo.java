package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.injection.invoke.RedirectInjector;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

public class RedirectInjectionInfo extends InjectionInfo
{
    public RedirectInjectionInfo(final MixinTargetContext a1, final MethodNode a2, final AnnotationNode a3) {
        super(a1, a2, a3);
    }
    
    @Override
    protected Injector parseInjector(final AnnotationNode a1) {
        /*SL:44*/return new RedirectInjector(this);
    }
    
    @Override
    protected String getDescription() {
        /*SL:49*/return "Redirector";
    }
}
