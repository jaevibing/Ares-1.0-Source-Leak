package org.spongepowered.tools.obfuscation.validation;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.DeclaredType;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import javax.lang.model.type.TypeMirror;
import java.util.Iterator;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.gen.Accessor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Collection;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.MixinValidator;

public class TargetValidator extends MixinValidator
{
    public TargetValidator(final IMixinAnnotationProcessor a1) {
        super(a1, IMixinValidator.ValidationPass.LATE);
    }
    
    public boolean validate(final TypeElement a1, final AnnotationHandle a2, final Collection<TypeHandle> a3) {
        /*SL:67*/if ("true".equalsIgnoreCase(this.options.getOption("disableTargetValidator"))) {
            /*SL:68*/return true;
        }
        /*SL:71*/if (a1.getKind() == ElementKind.INTERFACE) {
            /*SL:72*/this.validateInterfaceMixin(a1, a3);
        }
        else {
            /*SL:74*/this.validateClassMixin(a1, a3);
        }
        /*SL:77*/return true;
    }
    
    private void validateInterfaceMixin(final TypeElement v-3, final Collection<TypeHandle> v-2) {
        boolean b = /*EL:81*/false;
        /*SL:82*/for (final Element v1 : v-3.getEnclosedElements()) {
            /*SL:83*/if (v1.getKind() == ElementKind.METHOD) {
                final boolean a1 = /*EL:84*/AnnotationHandle.of(v1, Accessor.class).exists();
                final boolean a2 = /*EL:85*/AnnotationHandle.of(v1, Invoker.class).exists();
                /*SL:86*/b |= (!a1 && !a2);
            }
        }
        /*SL:90*/if (!b) {
            /*SL:91*/return;
        }
        /*SL:94*/for (final TypeHandle v2 : v-2) {
            final TypeElement v3 = /*EL:95*/v2.getElement();
            /*SL:96*/if (v3 != null && v3.getKind() != ElementKind.INTERFACE) {
                /*SL:97*/this.error("Targetted type '" + v2 + " of " + v-3 + " is not an interface", v-3);
            }
        }
    }
    
    private void validateClassMixin(final TypeElement v2, final Collection<TypeHandle> v3) {
        final TypeMirror v4 = /*EL:103*/v2.getSuperclass();
        /*SL:105*/for (TypeMirror a2 : v3) {
            /*SL:106*/a2 = a2.getType();
            /*SL:107*/if (a2 != null && !this.validateSuperClass(a2, v4)) {
                /*SL:108*/this.error("Superclass " + v4 + " of " + v2 + " was not found in the hierarchy of target class " + a2, v2);
            }
        }
    }
    
    private boolean validateSuperClass(final TypeMirror a1, final TypeMirror a2) {
        /*SL:114*/return TypeUtils.isAssignable(this.processingEnv, a1, a2) || /*EL:118*/this.validateSuperClassRecursive(a1, a2);
    }
    
    private boolean validateSuperClassRecursive(final TypeMirror a1, final TypeMirror a2) {
        /*SL:122*/if (!(a1 instanceof DeclaredType)) {
            /*SL:123*/return false;
        }
        /*SL:126*/if (TypeUtils.isAssignable(this.processingEnv, a1, a2)) {
            /*SL:127*/return true;
        }
        final TypeElement v1 = /*EL:130*/(TypeElement)((DeclaredType)a1).asElement();
        final TypeMirror v2 = /*EL:131*/v1.getSuperclass();
        /*SL:132*/return v2.getKind() != TypeKind.NONE && /*EL:136*/(this.checkMixinsFor(v2, a2) || /*EL:140*/this.validateSuperClassRecursive(v2, a2));
    }
    
    private boolean checkMixinsFor(final TypeMirror v1, final TypeMirror v2) {
        /*SL:144*/for (final TypeMirror a1 : this.getMixinsTargeting(v1)) {
            /*SL:145*/if (TypeUtils.isAssignable(this.processingEnv, a1, v2)) {
                /*SL:146*/return true;
            }
        }
        /*SL:150*/return false;
    }
}
