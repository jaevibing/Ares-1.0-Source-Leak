package org.spongepowered.tools.obfuscation;

import java.util.Iterator;
import javax.tools.Diagnostic;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.ElementKind;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import javax.lang.model.element.Element;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.injection.Inject;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import javax.annotation.processing.SupportedAnnotationTypes;

@SupportedAnnotationTypes({ "org.spongepowered.asm.mixin.injection.Inject", "org.spongepowered.asm.mixin.injection.ModifyArg", "org.spongepowered.asm.mixin.injection.ModifyArgs", "org.spongepowered.asm.mixin.injection.Redirect", "org.spongepowered.asm.mixin.injection.At" })
public class MixinObfuscationProcessorInjection extends MixinObfuscationProcessor
{
    @Override
    public boolean process(final Set<? extends TypeElement> a1, final RoundEnvironment a2) {
        /*SL:68*/if (a2.processingOver()) {
            /*SL:69*/this.postProcess(a2);
            /*SL:70*/return true;
        }
        /*SL:73*/this.processMixins(a2);
        /*SL:74*/this.processInjectors(a2, Inject.class);
        /*SL:75*/this.processInjectors(a2, ModifyArg.class);
        /*SL:76*/this.processInjectors(a2, ModifyArgs.class);
        /*SL:77*/this.processInjectors(a2, Redirect.class);
        /*SL:78*/this.processInjectors(a2, ModifyVariable.class);
        /*SL:79*/this.processInjectors(a2, ModifyConstant.class);
        /*SL:80*/this.postProcess(a2);
        /*SL:82*/return true;
    }
    
    @Override
    protected void postProcess(final RoundEnvironment v2) {
        /*SL:87*/super.postProcess(v2);
        try {
            /*SL:90*/this.mixins.writeReferences();
        }
        catch (Exception a1) {
            /*SL:92*/a1.printStackTrace();
        }
    }
    
    private void processInjectors(final RoundEnvironment v-2, final Class<? extends Annotation> v-1) {
        /*SL:101*/for (final Element v1 : v-2.getElementsAnnotatedWith(v-1)) {
            final Element a1 = /*EL:102*/v1.getEnclosingElement();
            /*SL:103*/if (!(a1 instanceof TypeElement)) {
                /*SL:104*/throw new IllegalStateException("@" + v-1.getSimpleName() + " element has unexpected parent with type " + /*EL:105*/TypeUtils.getElementType(a1));
            }
            final AnnotationHandle a2 = /*EL:108*/AnnotationHandle.of(v1, v-1);
            /*SL:110*/if (v1.getKind() == ElementKind.METHOD) {
                /*SL:111*/this.mixins.registerInjector((TypeElement)a1, (ExecutableElement)v1, a2);
            }
            else {
                /*SL:113*/this.mixins.printMessage(Diagnostic.Kind.WARNING, "Found an @" + v-1.getSimpleName() + /*EL:114*/" annotation on an element which is not a method: " + v1.toString());
            }
        }
    }
}
