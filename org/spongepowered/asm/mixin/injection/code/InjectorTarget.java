package org.spongepowered.asm.mixin.injection.code;

import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import java.util.HashMap;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.util.Map;

public class InjectorTarget
{
    private final ISliceContext context;
    private final Map<String, ReadOnlyInsnList> cache;
    private final Target target;
    private final String mergedBy;
    private final int mergedPriority;
    
    public InjectorTarget(final ISliceContext a1, final Target a2) {
        this.cache = new HashMap<String, ReadOnlyInsnList>();
        this.context = a1;
        this.target = a2;
        final AnnotationNode v1 = Annotations.getVisible(a2.method, MixinMerged.class);
        this.mergedBy = Annotations.<String>getValue(v1, "mixin");
        this.mergedPriority = Annotations.<Integer>getValue(v1, "priority", Integer.valueOf(1000));
    }
    
    @Override
    public String toString() {
        /*SL:81*/return this.target.toString();
    }
    
    public Target getTarget() {
        /*SL:88*/return this.target;
    }
    
    public MethodNode getMethod() {
        /*SL:95*/return this.target.method;
    }
    
    public boolean isMerged() {
        /*SL:102*/return this.mergedBy != null;
    }
    
    public String getMergedBy() {
        /*SL:110*/return this.mergedBy;
    }
    
    public int getMergedPriority() {
        /*SL:118*/return this.mergedPriority;
    }
    
    public InsnList getSlice(final String v2) {
        ReadOnlyInsnList v3 = /*EL:128*/this.cache.get(v2);
        /*SL:129*/if (v3 == null) {
            final MethodSlice a1 = /*EL:130*/this.context.getSlice(v2);
            /*SL:131*/if (a1 != null) {
                /*SL:132*/v3 = a1.getSlice(this.target.method);
            }
            else {
                /*SL:135*/v3 = new ReadOnlyInsnList(this.target.method.instructions);
            }
            /*SL:137*/this.cache.put(v2, v3);
        }
        /*SL:140*/return v3;
    }
    
    public InsnList getSlice(final InjectionPoint a1) {
        /*SL:150*/return this.getSlice(a1.getSlice());
    }
    
    public void dispose() {
        /*SL:157*/for (final ReadOnlyInsnList v1 : this.cache.values()) {
            /*SL:158*/v1.dispose();
        }
        /*SL:161*/this.cache.clear();
    }
}
