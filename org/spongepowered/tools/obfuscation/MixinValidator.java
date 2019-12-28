package org.spongepowered.tools.obfuscation;

import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Collection;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IOptionProvider;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;

public abstract class MixinValidator implements IMixinValidator
{
    protected final ProcessingEnvironment processingEnv;
    protected final Messager messager;
    protected final IOptionProvider options;
    protected final ValidationPass pass;
    
    public MixinValidator(final IMixinAnnotationProcessor a1, final ValidationPass a2) {
        this.processingEnv = a1.getProcessingEnvironment();
        this.messager = a1;
        this.options = a1;
        this.pass = a2;
    }
    
    @Override
    public final boolean validate(final ValidationPass a1, final TypeElement a2, final AnnotationHandle a3, final Collection<TypeHandle> a4) {
        /*SL:90*/return a1 != this.pass || /*EL:94*/this.validate(a2, a3, a4);
    }
    
    protected abstract boolean validate(final TypeElement p0, final AnnotationHandle p1, final Collection<TypeHandle> p2);
    
    protected final void note(final String a1, final Element a2) {
        /*SL:106*/this.messager.printMessage(Diagnostic.Kind.NOTE, a1, a2);
    }
    
    protected final void warning(final String a1, final Element a2) {
        /*SL:116*/this.messager.printMessage(Diagnostic.Kind.WARNING, a1, a2);
    }
    
    protected final void error(final String a1, final Element a2) {
        /*SL:126*/this.messager.printMessage(Diagnostic.Kind.ERROR, a1, a2);
    }
    
    protected final Collection<TypeMirror> getMixinsTargeting(final TypeMirror a1) {
        /*SL:130*/return AnnotatedMixins.getMixinsForEnvironment(this.processingEnv).getMixinsTargeting(a1);
    }
}
