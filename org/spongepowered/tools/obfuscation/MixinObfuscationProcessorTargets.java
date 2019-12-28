package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.Iterator;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.ElementKind;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import javax.tools.Diagnostic;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.Shadow;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import javax.annotation.processing.SupportedAnnotationTypes;

@SupportedAnnotationTypes({ "org.spongepowered.asm.mixin.Mixin", "org.spongepowered.asm.mixin.Shadow", "org.spongepowered.asm.mixin.Overwrite", "org.spongepowered.asm.mixin.gen.Accessor", "org.spongepowered.asm.mixin.Implements" })
public class MixinObfuscationProcessorTargets extends MixinObfuscationProcessor
{
    @Override
    public boolean process(final Set<? extends TypeElement> a1, final RoundEnvironment a2) {
        /*SL:66*/if (a2.processingOver()) {
            /*SL:67*/this.postProcess(a2);
            /*SL:68*/return true;
        }
        /*SL:71*/this.processMixins(a2);
        /*SL:72*/this.processShadows(a2);
        /*SL:73*/this.processOverwrites(a2);
        /*SL:74*/this.processAccessors(a2);
        /*SL:75*/this.processInvokers(a2);
        /*SL:76*/this.processImplements(a2);
        /*SL:77*/this.postProcess(a2);
        /*SL:79*/return true;
    }
    
    @Override
    protected void postProcess(final RoundEnvironment v2) {
        /*SL:84*/super.postProcess(v2);
        try {
            /*SL:87*/this.mixins.writeReferences();
            /*SL:88*/this.mixins.writeMappings();
        }
        catch (Exception a1) {
            /*SL:90*/a1.printStackTrace();
        }
    }
    
    private void processShadows(final RoundEnvironment v-3) {
        /*SL:99*/for (final Element a2 : v-3.getElementsAnnotatedWith(Shadow.class)) {
            final Element a1 = /*EL:100*/a2.getEnclosingElement();
            /*SL:101*/if (!(a1 instanceof TypeElement)) {
                /*SL:102*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(a1), a2);
            }
            else {
                final AnnotationHandle v1 = /*EL:106*/AnnotationHandle.of(a2, Shadow.class);
                /*SL:108*/if (a2.getKind() == ElementKind.FIELD) {
                    /*SL:109*/this.mixins.registerShadow((TypeElement)a1, (VariableElement)a2, v1);
                }
                else/*SL:110*/ if (a2.getKind() == ElementKind.METHOD) {
                    /*SL:111*/this.mixins.registerShadow((TypeElement)a1, (ExecutableElement)a2, v1);
                }
                else {
                    /*SL:113*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Element is not a method or field", a2);
                }
            }
        }
    }
    
    private void processOverwrites(final RoundEnvironment v-1) {
        /*SL:123*/for (final Element v1 : v-1.getElementsAnnotatedWith(Overwrite.class)) {
            final Element a1 = /*EL:124*/v1.getEnclosingElement();
            /*SL:125*/if (!(a1 instanceof TypeElement)) {
                /*SL:126*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(a1), v1);
            }
            else/*SL:130*/ if (v1.getKind() == ElementKind.METHOD) {
                /*SL:131*/this.mixins.registerOverwrite((TypeElement)a1, (ExecutableElement)v1);
            }
            else {
                /*SL:133*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Element is not a method", v1);
            }
        }
    }
    
    private void processAccessors(final RoundEnvironment v-1) {
        /*SL:143*/for (final Element v1 : v-1.getElementsAnnotatedWith(Accessor.class)) {
            final Element a1 = /*EL:144*/v1.getEnclosingElement();
            /*SL:145*/if (!(a1 instanceof TypeElement)) {
                /*SL:146*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(a1), v1);
            }
            else/*SL:150*/ if (v1.getKind() == ElementKind.METHOD) {
                /*SL:151*/this.mixins.registerAccessor((TypeElement)a1, (ExecutableElement)v1);
            }
            else {
                /*SL:153*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Element is not a method", v1);
            }
        }
    }
    
    private void processInvokers(final RoundEnvironment v-1) {
        /*SL:163*/for (final Element v1 : v-1.getElementsAnnotatedWith(Invoker.class)) {
            final Element a1 = /*EL:164*/v1.getEnclosingElement();
            /*SL:165*/if (!(a1 instanceof TypeElement)) {
                /*SL:166*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Unexpected parent with type " + TypeUtils.getElementType(a1), v1);
            }
            else/*SL:170*/ if (v1.getKind() == ElementKind.METHOD) {
                /*SL:171*/this.mixins.registerInvoker((TypeElement)a1, (ExecutableElement)v1);
            }
            else {
                /*SL:173*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Element is not a method", v1);
            }
        }
    }
    
    private void processImplements(final RoundEnvironment v-1) {
        /*SL:183*/for (final Element v1 : v-1.getElementsAnnotatedWith(Implements.class)) {
            /*SL:184*/if (v1.getKind() == ElementKind.CLASS || v1.getKind() == ElementKind.INTERFACE) {
                final AnnotationHandle a1 = /*EL:185*/AnnotationHandle.of(v1, Implements.class);
                /*SL:186*/this.mixins.registerSoftImplements((TypeElement)v1, a1);
            }
            else {
                /*SL:188*/this.mixins.printMessage(Diagnostic.Kind.ERROR, "Found an @Implements annotation on an element which is not a class or interface", v1);
            }
        }
    }
}
