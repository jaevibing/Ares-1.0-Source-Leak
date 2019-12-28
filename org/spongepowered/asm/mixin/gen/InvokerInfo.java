package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.lib.Type;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

public class InvokerInfo extends AccessorInfo
{
    public InvokerInfo(final MixinTargetContext a1, final MethodNode a2) {
        super(a1, a2, Invoker.class);
    }
    
    @Override
    protected AccessorType initType() {
        /*SL:43*/return AccessorType.METHOD_PROXY;
    }
    
    @Override
    protected Type initTargetFieldType() {
        /*SL:48*/return null;
    }
    
    @Override
    protected MemberInfo initTarget() {
        /*SL:53*/return new MemberInfo(this.getTargetName(), null, this.method.desc);
    }
    
    @Override
    public void locate() {
        /*SL:58*/this.targetMethod = this.findTargetMethod();
    }
    
    private MethodNode findTargetMethod() {
        /*SL:62*/return this.<MethodNode>findTarget(this.classNode.methods);
    }
}
