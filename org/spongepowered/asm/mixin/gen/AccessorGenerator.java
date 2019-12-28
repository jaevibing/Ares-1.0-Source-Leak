package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.util.ArrayList;
import org.spongepowered.asm.lib.tree.MethodNode;

public abstract class AccessorGenerator
{
    protected final AccessorInfo info;
    
    public AccessorGenerator(final AccessorInfo a1) {
        this.info = a1;
    }
    
    protected final MethodNode createMethod(final int a1, final int a2) {
        final MethodNode v1 = /*EL:55*/this.info.getMethod();
        final MethodNode v2 = /*EL:56*/new MethodNode(327680, (v1.access & 0xFFFFFBFF) | 0x1000, v1.name, v1.desc, null, null);
        /*SL:58*/(v2.visibleAnnotations = new ArrayList<AnnotationNode>()).add(/*EL:59*/this.info.getAnnotation());
        /*SL:60*/v2.maxLocals = a1;
        /*SL:61*/v2.maxStack = a2;
        /*SL:62*/return v2;
    }
    
    public abstract MethodNode generate();
}
