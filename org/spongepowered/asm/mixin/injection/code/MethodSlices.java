package org.spongepowered.asm.mixin.injection.code;

import java.util.Iterator;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.throwables.InvalidSliceException;
import java.util.HashMap;
import java.util.Map;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public final class MethodSlices
{
    private final InjectionInfo info;
    private final Map<String, MethodSlice> slices;
    
    private MethodSlices(final InjectionInfo a1) {
        this.slices = new HashMap<String, MethodSlice>(4);
        this.info = a1;
    }
    
    private void add(final MethodSlice a1) {
        final String v1 = /*EL:67*/this.info.getSliceId(a1.getId());
        /*SL:68*/if (this.slices.containsKey(v1)) {
            /*SL:69*/throw new InvalidSliceException((ISliceContext)this.info, a1 + " has a duplicate id, '" + v1 + "' was already defined");
        }
        /*SL:71*/this.slices.put(v1, a1);
    }
    
    public MethodSlice get(final String a1) {
        /*SL:82*/return this.slices.get(a1);
    }
    
    @Override
    public String toString() {
        /*SL:90*/return String.format("MethodSlices%s", this.slices.keySet());
    }
    
    public static MethodSlices parse(final InjectionInfo v-3) {
        final MethodSlices methodSlices = /*EL:100*/new MethodSlices(v-3);
        final AnnotationNode annotation = /*EL:102*/v-3.getAnnotation();
        /*SL:103*/if (annotation != null) {
            /*SL:104*/for (final AnnotationNode v1 : Annotations.<AnnotationNode>getValue(annotation, "slice", true)) {
                final MethodSlice a1 = /*EL:105*/MethodSlice.parse(v-3, v1);
                /*SL:106*/methodSlices.add(a1);
            }
        }
        /*SL:110*/return methodSlices;
    }
}
