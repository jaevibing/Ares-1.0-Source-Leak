package org.spongepowered.tools.obfuscation.validation;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.ElementKind;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Collection;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.MixinValidator;

public class ParentValidator extends MixinValidator
{
    public ParentValidator(final IMixinAnnotationProcessor a1) {
        super(a1, IMixinValidator.ValidationPass.EARLY);
    }
    
    public boolean validate(final TypeElement a1, final AnnotationHandle a2, final Collection<TypeHandle> a3) {
        /*SL:60*/if (a1.getEnclosingElement().getKind() != ElementKind.PACKAGE && !a1.getModifiers().contains(Modifier.STATIC)) {
            /*SL:61*/this.error("Inner class mixin must be declared static", a1);
        }
        /*SL:64*/return true;
    }
}
