package org.spongepowered.tools.obfuscation.mirror;

import org.spongepowered.asm.obfuscation.mapping.IMapping;
import com.google.common.base.Strings;
import org.spongepowered.tools.obfuscation.mirror.mapping.ResolvableMappingMethod;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;

public class MethodHandle extends MemberHandle<MappingMethod>
{
    private final ExecutableElement element;
    private final TypeHandle ownerHandle;
    
    public MethodHandle(final TypeHandle a1, final ExecutableElement a2) {
        this(a1, a2, TypeUtils.getName(a2), TypeUtils.getDescriptor(a2));
    }
    
    public MethodHandle(final TypeHandle a1, final String a2, final String a3) {
        this(a1, null, a2, a3);
    }
    
    private MethodHandle(final TypeHandle a1, final ExecutableElement a2, final String a3, final String a4) {
        super((a1 != null) ? a1.getName() : null, a3, a4);
        this.element = a2;
        this.ownerHandle = a1;
    }
    
    public boolean isImaginary() {
        /*SL:67*/return this.element == null;
    }
    
    public ExecutableElement getElement() {
        /*SL:74*/return this.element;
    }
    
    @Override
    public Visibility getVisibility() {
        /*SL:79*/return TypeUtils.getVisibility(this.element);
    }
    
    @Override
    public MappingMethod asMapping(final boolean a1) {
        /*SL:84*/if (!a1) {
            /*SL:90*/return new MappingMethod(null, this.getName(), this.getDesc());
        }
        if (this.ownerHandle != null) {
            return new ResolvableMappingMethod(this.ownerHandle, this.getName(), this.getDesc());
        }
        return new MappingMethod(this.getOwner(), this.getName(), this.getDesc());
    }
    
    @Override
    public String toString() {
        final String v1 = /*EL:95*/(this.getOwner() != null) ? ("L" + this.getOwner() + ";") : "";
        final String v2 = /*EL:96*/Strings.nullToEmpty(this.getName());
        final String v3 = /*EL:97*/Strings.nullToEmpty(this.getDesc());
        /*SL:98*/return String.format("%s%s%s", v1, v2, v3);
    }
}
