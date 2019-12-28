package org.spongepowered.asm.mixin.struct;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;

public abstract class SpecialMethodInfo implements IInjectionPointContext
{
    protected final AnnotationNode annotation;
    protected final ClassNode classNode;
    protected final MethodNode method;
    protected final MixinTargetContext mixin;
    
    public SpecialMethodInfo(final MixinTargetContext a1, final MethodNode a2, final AnnotationNode a3) {
        this.mixin = a1;
        this.method = a2;
        this.annotation = a3;
        this.classNode = a1.getTargetClassNode();
    }
    
    @Override
    public final IMixinContext getContext() {
        /*SL:73*/return this.mixin;
    }
    
    @Override
    public final AnnotationNode getAnnotation() {
        /*SL:83*/return this.annotation;
    }
    
    public final ClassNode getClassNode() {
        /*SL:92*/return this.classNode;
    }
    
    @Override
    public final MethodNode getMethod() {
        /*SL:102*/return this.method;
    }
}
