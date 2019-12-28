package org.spongepowered.tools.obfuscation.mirror;

import org.spongepowered.asm.obfuscation.mapping.IMapping;
import com.google.common.base.Strings;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;

public class FieldHandle extends MemberHandle<MappingField>
{
    private final VariableElement element;
    private final boolean rawType;
    
    public FieldHandle(final TypeElement a1, final VariableElement a2) {
        this(TypeUtils.getInternalName(a1), a2);
    }
    
    public FieldHandle(final String a1, final VariableElement a2) {
        this(a1, a2, false);
    }
    
    public FieldHandle(final TypeElement a1, final VariableElement a2, final boolean a3) {
        this(TypeUtils.getInternalName(a1), a2, a3);
    }
    
    public FieldHandle(final String a1, final VariableElement a2, final boolean a3) {
        this(a1, a2, a3, TypeUtils.getName(a2), TypeUtils.getInternalName(a2));
    }
    
    public FieldHandle(final String a1, final String a2, final String a3) {
        this(a1, null, false, a2, a3);
    }
    
    private FieldHandle(final String a1, final VariableElement a2, final boolean a3, final String a4, final String a5) {
        super(a1, a4, a5);
        this.element = a2;
        this.rawType = a3;
    }
    
    public boolean isImaginary() {
        /*SL:80*/return this.element == null;
    }
    
    public VariableElement getElement() {
        /*SL:87*/return this.element;
    }
    
    @Override
    public Visibility getVisibility() {
        /*SL:92*/return TypeUtils.getVisibility(this.element);
    }
    
    public boolean isRawType() {
        /*SL:100*/return this.rawType;
    }
    
    @Override
    public MappingField asMapping(final boolean a1) {
        /*SL:105*/return new MappingField(a1 ? this.getOwner() : null, this.getName(), this.getDesc());
    }
    
    @Override
    public String toString() {
        final String v1 = /*EL:110*/(this.getOwner() != null) ? ("L" + this.getOwner() + ";") : "";
        final String v2 = /*EL:111*/Strings.nullToEmpty(this.getName());
        final String v3 = /*EL:112*/Strings.nullToEmpty(this.getDesc());
        /*SL:113*/return String.format("%s%s:%s", v1, v2, v3);
    }
}
