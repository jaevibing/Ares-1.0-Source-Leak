package org.spongepowered.tools.obfuscation.mirror;

import java.util.Iterator;
import javax.lang.model.type.TypeKind;
import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import java.lang.annotation.Annotation;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.element.TypeElement;

public class TypeHandleSimulated extends TypeHandle
{
    private final TypeElement simulatedType;
    
    public TypeHandleSimulated(final String a1, final TypeMirror a2) {
        this(TypeUtils.getPackage(a2), a1, a2);
    }
    
    public TypeHandleSimulated(final PackageElement a1, final String a2, final TypeMirror a3) {
        super(a1, a2);
        this.simulatedType = (TypeElement)((DeclaredType)a3).asElement();
    }
    
    @Override
    protected TypeElement getTargetElement() {
        /*SL:66*/return this.simulatedType;
    }
    
    @Override
    public boolean isPublic() {
        /*SL:75*/return true;
    }
    
    @Override
    public boolean isImaginary() {
        /*SL:84*/return false;
    }
    
    @Override
    public boolean isSimulated() {
        /*SL:93*/return true;
    }
    
    @Override
    public AnnotationHandle getAnnotation(final Class<? extends Annotation> a1) {
        /*SL:103*/return null;
    }
    
    @Override
    public TypeHandle getSuperclass() {
        /*SL:112*/return null;
    }
    
    @Override
    public String findDescriptor(final MemberInfo a1) {
        /*SL:122*/return (a1 != null) ? a1.desc : null;
    }
    
    @Override
    public FieldHandle findField(final String a1, final String a2, final boolean a3) {
        /*SL:131*/return new FieldHandle(null, a1, a2);
    }
    
    @Override
    public MethodHandle findMethod(final String a1, final String a2, final boolean a3) {
        /*SL:141*/return new MethodHandle((TypeHandle)null, a1, a2);
    }
    
    @Override
    public MappingMethod getMappingMethod(final String a1, final String a2) {
        final String v1 = /*EL:151*/new SignaturePrinter(a1, a2).setFullyQualified(true).toDescriptor();
        final String v2 = /*EL:152*/TypeUtils.stripGenerics(v1);
        final MethodHandle v3 = findMethodRecursive(/*EL:155*/this, a1, v1, v2, true);
        /*SL:158*/return (v3 != null) ? v3.asMapping(true) : super.getMappingMethod(a1, a2);
    }
    
    private static MethodHandle findMethodRecursive(final TypeHandle a2, final String a3, final String a4, final String a5, final boolean v1) {
        final TypeElement v2 = /*EL:163*/a2.getTargetElement();
        /*SL:164*/if (v2 == null) {
            /*SL:165*/return null;
        }
        MethodHandle v3 = /*EL:168*/TypeHandle.findMethod(a2, a3, a4, a5, v1);
        /*SL:169*/if (v3 != null) {
            /*SL:170*/return v3;
        }
        /*SL:173*/for (final TypeMirror a6 : v2.getInterfaces()) {
            /*SL:174*/v3 = findMethodRecursive(a6, a3, a4, a5, v1);
            /*SL:175*/if (v3 != null) {
                /*SL:176*/return v3;
            }
        }
        final TypeMirror v4 = /*EL:180*/v2.getSuperclass();
        /*SL:181*/if (v4 == null || v4.getKind() == TypeKind.NONE) {
            /*SL:182*/return null;
        }
        /*SL:185*/return findMethodRecursive(v4, a3, a4, a5, v1);
    }
    
    private static MethodHandle findMethodRecursive(final TypeMirror a1, final String a2, final String a3, final String a4, final boolean a5) {
        /*SL:189*/if (!(a1 instanceof DeclaredType)) {
            /*SL:190*/return null;
        }
        final TypeElement v1 = /*EL:192*/(TypeElement)((DeclaredType)a1).asElement();
        /*SL:193*/return findMethodRecursive(new TypeHandle(v1), a2, a3, a4, a5);
    }
}
