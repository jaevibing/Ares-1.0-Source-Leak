package org.spongepowered.asm.mixin.injection.struct;

import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.callback.CallbackInjector;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

public class CallbackInjectionInfo extends InjectionInfo
{
    protected CallbackInjectionInfo(final MixinTargetContext a1, final MethodNode a2, final AnnotationNode a3) {
        super(a1, a2, a3);
    }
    
    @Override
    protected Injector parseInjector(final AnnotationNode a1) {
        final boolean v1 = /*EL:49*/Annotations.<Boolean>getValue(a1, "cancellable", Boolean.FALSE);
        final LocalCapture v2 = /*EL:50*/Annotations.<LocalCapture>getValue(a1, "locals", LocalCapture.class, LocalCapture.NO_CAPTURE);
        final String v3 = /*EL:51*/Annotations.<String>getValue(a1, "id", "");
        /*SL:53*/return new CallbackInjector(this, v1, v2, v3);
    }
    
    @Override
    public String getSliceId(final String a1) {
        /*SL:58*/return Strings.nullToEmpty(a1);
    }
}
