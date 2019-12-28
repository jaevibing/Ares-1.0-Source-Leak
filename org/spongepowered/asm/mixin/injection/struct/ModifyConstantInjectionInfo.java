package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.injection.Constant;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.invoke.ModifyConstantInjector;
import org.spongepowered.asm.mixin.injection.code.Injector;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
import org.spongepowered.asm.lib.Type;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

public class ModifyConstantInjectionInfo extends InjectionInfo
{
    private static final String CONSTANT_ANNOTATION_CLASS;
    
    public ModifyConstantInjectionInfo(final MixinTargetContext a1, final MethodNode a2, final AnnotationNode a3) {
        super(a1, a2, a3, "constant");
    }
    
    @Override
    protected List<AnnotationNode> readInjectionPoints(final String v2) {
        List<AnnotationNode> v3 = /*EL:54*/super.readInjectionPoints(v2);
        /*SL:55*/if (v3.isEmpty()) {
            final AnnotationNode a1 = /*EL:56*/new AnnotationNode(ModifyConstantInjectionInfo.CONSTANT_ANNOTATION_CLASS);
            /*SL:57*/a1.visit("log", Boolean.TRUE);
            /*SL:58*/v3 = ImmutableList.<AnnotationNode>of(a1);
        }
        /*SL:60*/return v3;
    }
    
    @Override
    protected void parseInjectionPoints(final List<AnnotationNode> v2) {
        final Type v3 = /*EL:65*/Type.getReturnType(this.method.desc);
        /*SL:67*/for (final AnnotationNode a1 : v2) {
            /*SL:68*/this.injectionPoints.add(new BeforeConstant(this.getContext(), a1, v3.getDescriptor()));
        }
    }
    
    @Override
    protected Injector parseInjector(final AnnotationNode a1) {
        /*SL:74*/return new ModifyConstantInjector(this);
    }
    
    @Override
    protected String getDescription() {
        /*SL:79*/return "Constant modifier method";
    }
    
    @Override
    public String getSliceId(final String a1) {
        /*SL:84*/return Strings.nullToEmpty(a1);
    }
    
    static {
        CONSTANT_ANNOTATION_CLASS = Constant.class.getName().replace('.', '/');
    }
}
