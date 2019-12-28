package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.element.VariableElement;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.tools.obfuscation.mirror.mapping.ResolvableMappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import javax.lang.model.element.Modifier;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.element.ElementKind;
import java.util.List;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.PackageElement;

public class TypeHandle
{
    private final String name;
    private final PackageElement pkg;
    private final TypeElement element;
    private TypeReference reference;
    
    public TypeHandle(final PackageElement a1, final String a2) {
        this.name = a2.replace('.', '/');
        this.pkg = a1;
        this.element = null;
    }
    
    public TypeHandle(final TypeElement a1) {
        this.pkg = TypeUtils.getPackage(a1);
        this.name = TypeUtils.getInternalName(a1);
        this.element = a1;
    }
    
    public TypeHandle(final DeclaredType a1) {
        this((TypeElement)a1.asElement());
    }
    
    @Override
    public final String toString() {
        /*SL:115*/return this.name.replace('/', '.');
    }
    
    public final String getName() {
        /*SL:122*/return this.name;
    }
    
    public final PackageElement getPackage() {
        /*SL:129*/return this.pkg;
    }
    
    public final TypeElement getElement() {
        /*SL:136*/return this.element;
    }
    
    protected TypeElement getTargetElement() {
        /*SL:144*/return this.element;
    }
    
    public AnnotationHandle getAnnotation(final Class<? extends Annotation> a1) {
        /*SL:155*/return AnnotationHandle.of(this.getTargetElement(), a1);
    }
    
    public final List<? extends Element> getEnclosedElements() {
        /*SL:162*/return getEnclosedElements(this.getTargetElement());
    }
    
    public <T extends java.lang.Object> List<T> getEnclosedElements(final ElementKind... a1) {
        /*SL:172*/return TypeHandle.<T>getEnclosedElements(this.getTargetElement(), a1);
    }
    
    public TypeMirror getType() {
        /*SL:180*/return (this.getTargetElement() != null) ? this.getTargetElement().asType() : null;
    }
    
    public TypeHandle getSuperclass() {
        final TypeElement v1 = /*EL:188*/this.getTargetElement();
        /*SL:189*/if (v1 == null) {
            /*SL:190*/return null;
        }
        final TypeMirror v2 = /*EL:193*/v1.getSuperclass();
        /*SL:194*/if (v2 == null || v2.getKind() == TypeKind.NONE) {
            /*SL:195*/return null;
        }
        /*SL:198*/return new TypeHandle((DeclaredType)v2);
    }
    
    public List<TypeHandle> getInterfaces() {
        /*SL:205*/if (this.getTargetElement() == null) {
            /*SL:206*/return Collections.<TypeHandle>emptyList();
        }
        final ImmutableList.Builder<TypeHandle> builder = /*EL:209*/ImmutableList.<TypeHandle>builder();
        /*SL:210*/for (final TypeMirror v1 : this.getTargetElement().getInterfaces()) {
            /*SL:211*/builder.add(new TypeHandle((DeclaredType)v1));
        }
        /*SL:214*/return builder.build();
    }
    
    public boolean isPublic() {
        /*SL:221*/return this.getTargetElement() != null && this.getTargetElement().getModifiers().contains(Modifier.PUBLIC);
    }
    
    public boolean isImaginary() {
        /*SL:228*/return this.getTargetElement() == null;
    }
    
    public boolean isSimulated() {
        /*SL:235*/return false;
    }
    
    public final TypeReference getReference() {
        /*SL:242*/if (this.reference == null) {
            /*SL:243*/this.reference = new TypeReference(this);
        }
        /*SL:245*/return this.reference;
    }
    
    public MappingMethod getMappingMethod(final String a1, final String a2) {
        /*SL:258*/return new ResolvableMappingMethod(this, a1, a2);
    }
    
    public String findDescriptor(final MemberInfo v2) {
        String v3 = /*EL:268*/v2.desc;
        /*SL:269*/if (v3 == null) {
            /*SL:270*/for (final ExecutableElement a1 : this.<ExecutableElement>getEnclosedElements(ElementKind.METHOD)) {
                /*SL:271*/if (a1.getSimpleName().toString().equals(v2.name)) {
                    /*SL:272*/v3 = TypeUtils.getDescriptor(a1);
                    /*SL:273*/break;
                }
            }
        }
        /*SL:277*/return v3;
    }
    
    public final FieldHandle findField(final VariableElement a1) {
        /*SL:288*/return this.findField(a1, true);
    }
    
    public final FieldHandle findField(final VariableElement a1, final boolean a2) {
        /*SL:300*/return this.findField(a1.getSimpleName().toString(), TypeUtils.getTypeName(a1.asType()), a2);
    }
    
    public final FieldHandle findField(final String a1, final String a2) {
        /*SL:312*/return this.findField(a1, a2, true);
    }
    
    public FieldHandle findField(final String a3, final String v1, final boolean v2) {
        final String v3 = /*EL:325*/TypeUtils.stripGenerics(v1);
        /*SL:327*/for (final VariableElement a4 : this.<VariableElement>getEnclosedElements(ElementKind.FIELD)) {
            /*SL:328*/if (compareElement(a4, a3, v1, v2)) {
                /*SL:329*/return new FieldHandle(this.getTargetElement(), a4);
            }
            /*SL:330*/if (compareElement(a4, a3, v3, v2)) {
                /*SL:331*/return new FieldHandle(this.getTargetElement(), a4, true);
            }
        }
        /*SL:335*/return null;
    }
    
    public final MethodHandle findMethod(final ExecutableElement a1) {
        /*SL:346*/return this.findMethod(a1, true);
    }
    
    public final MethodHandle findMethod(final ExecutableElement a1, final boolean a2) {
        /*SL:358*/return this.findMethod(a1.getSimpleName().toString(), TypeUtils.getJavaSignature(a1), a2);
    }
    
    public final MethodHandle findMethod(final String a1, final String a2) {
        /*SL:370*/return this.findMethod(a1, a2, true);
    }
    
    public MethodHandle findMethod(final String a1, final String a2, final boolean a3) {
        final String v1 = /*EL:383*/TypeUtils.stripGenerics(a2);
        /*SL:384*/return findMethod(this, a1, a2, v1, a3);
    }
    
    protected static MethodHandle findMethod(final TypeHandle a2, final String a3, final String a4, final String a5, final boolean v1) {
        /*SL:388*/for (final ExecutableElement a6 : TypeHandle.<ExecutableElement>getEnclosedElements(a2.getTargetElement(), ElementKind.CONSTRUCTOR, ElementKind.METHOD)) {
            /*SL:390*/if (compareElement(a6, a3, a4, v1) || compareElement(a6, a3, a5, v1)) {
                /*SL:391*/return new MethodHandle(a2, a6);
            }
        }
        /*SL:394*/return null;
    }
    
    protected static boolean compareElement(final Element v-3, final String v-2, final String v-1, final boolean v0) {
        try {
            final String a1 = /*EL:399*/v-3.getSimpleName().toString();
            final String a2 = /*EL:400*/TypeUtils.getJavaSignature(v-3);
            final String a3 = /*EL:401*/TypeUtils.stripGenerics(a2);
            final boolean a4 = /*EL:402*/v0 ? v-2.equals(a1) : v-2.equalsIgnoreCase(a1);
            /*SL:403*/return a4 && (v-1.length() == 0 || v-1.equals(a2) || v-1.equals(a3));
        }
        catch (NullPointerException v) {
            /*SL:405*/return false;
        }
    }
    
    protected static <T extends java.lang.Object> List<T> getEnclosedElements(final TypeElement v1, final ElementKind... v2) {
        /*SL:411*/if (v2 == null || v2.length < 1) {
            /*SL:412*/return (List<T>)getEnclosedElements(v1);
        }
        /*SL:415*/if (v1 == null) {
            /*SL:416*/return Collections.<T>emptyList();
        }
        final ImmutableList.Builder<T> v3 = /*EL:419*/ImmutableList.<T>builder();
        /*SL:420*/for (ElementKind a2 : v1.getEnclosedElements()) {
            /*SL:421*/for (final ElementKind a2 : v2) {
                /*SL:422*/if (a2.getKind() == a2) {
                    /*SL:423*/v3.add((T)a2);
                    /*SL:424*/break;
                }
            }
        }
        /*SL:429*/return v3.build();
    }
    
    protected static List<? extends Element> getEnclosedElements(final TypeElement a1) {
        /*SL:433*/return (a1 != null) ? a1.getEnclosedElements() : Collections.<? extends Element>emptyList();
    }
}
