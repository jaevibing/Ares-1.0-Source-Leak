package org.spongepowered.tools.obfuscation;

import java.util.Set;
import javax.lang.model.SourceVersion;
import java.util.Iterator;
import javax.tools.Diagnostic;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.Mixin;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.AbstractProcessor;

public abstract class MixinObfuscationProcessor extends AbstractProcessor
{
    protected AnnotatedMixins mixins;
    
    @Override
    public synchronized void init(final ProcessingEnvironment a1) {
        /*SL:56*/super.init(a1);
        /*SL:57*/this.mixins = AnnotatedMixins.getMixinsForEnvironment(a1);
    }
    
    protected void processMixins(final RoundEnvironment v2) {
        /*SL:66*/this.mixins.onPassStarted();
        /*SL:68*/for (final Element a1 : v2.getElementsAnnotatedWith(Mixin.class)) {
            /*SL:69*/if (a1.getKind() == ElementKind.CLASS || a1.getKind() == ElementKind.INTERFACE) {
                /*SL:70*/this.mixins.registerMixin((TypeElement)a1);
            }
            else {
                /*SL:72*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Found an @Mixin annotation on an element which is not a class or interface", a1);
            }
        }
    }
    
    protected void postProcess(final RoundEnvironment a1) {
        /*SL:78*/this.mixins.onPassCompleted(a1);
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        try {
            /*SL:84*/return SourceVersion.valueOf("RELEASE_8");
        }
        catch (IllegalArgumentException ex) {
            /*SL:89*/return super.getSupportedSourceVersion();
        }
    }
    
    @Override
    public Set<String> getSupportedOptions() {
        /*SL:94*/return SupportedOptions.getAllOptions();
    }
}
