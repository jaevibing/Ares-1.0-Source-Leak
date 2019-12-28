package org.spongepowered.tools.obfuscation.mirror.mapping;

import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;

public final class ResolvableMappingMethod extends MappingMethod
{
    private final TypeHandle ownerHandle;
    
    public ResolvableMappingMethod(final TypeHandle a1, final String a2, final String a3) {
        super(a1.getName(), a2, a3);
        this.ownerHandle = a1;
    }
    
    @Override
    public MappingMethod getSuper() {
        /*SL:51*/if (this.ownerHandle == null) {
            /*SL:52*/return super.getSuper();
        }
        final String simpleName = /*EL:55*/this.getSimpleName();
        final String desc = /*EL:56*/this.getDesc();
        final String javaSignature = /*EL:57*/TypeUtils.getJavaSignature(desc);
        final TypeHandle superclass = /*EL:59*/this.ownerHandle.getSuperclass();
        /*SL:60*/if (superclass != null && /*EL:62*/superclass.findMethod(simpleName, javaSignature) != null) {
            /*SL:63*/return superclass.getMappingMethod(simpleName, desc);
        }
        /*SL:68*/for (final TypeHandle v1 : this.ownerHandle.getInterfaces()) {
            /*SL:69*/if (v1.findMethod(simpleName, javaSignature) != null) {
                /*SL:70*/return v1.getMappingMethod(simpleName, desc);
            }
        }
        /*SL:75*/if (superclass != null) {
            /*SL:76*/return superclass.getMappingMethod(simpleName, desc).getSuper();
        }
        /*SL:79*/return super.getSuper();
    }
    
    public MappingMethod move(final TypeHandle a1) {
        /*SL:90*/return new ResolvableMappingMethod(a1, this.getSimpleName(), this.getDesc());
    }
    
    @Override
    public MappingMethod remap(final String a1) {
        /*SL:95*/return new ResolvableMappingMethod(this.ownerHandle, a1, this.getDesc());
    }
    
    @Override
    public MappingMethod transform(final String a1) {
        /*SL:100*/return new ResolvableMappingMethod(this.ownerHandle, this.getSimpleName(), a1);
    }
    
    @Override
    public MappingMethod copy() {
        /*SL:105*/return new ResolvableMappingMethod(this.ownerHandle, this.getSimpleName(), this.getDesc());
    }
}
