package org.spongepowered.tools.obfuscation;

import java.lang.reflect.Method;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import javax.annotation.processing.Messager;
import java.util.Iterator;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

class AnnotatedMixinElementHandlerOverwrite extends AnnotatedMixinElementHandler
{
    AnnotatedMixinElementHandlerOverwrite(final IMixinAnnotationProcessor a1, final AnnotatedMixin a2) {
        super(a1, a2);
    }
    
    public void registerMerge(final ExecutableElement a1) {
        /*SL:66*/this.validateTargetMethod(a1, null, new AliasedElementName(a1, AnnotationHandle.MISSING), "overwrite", true, true);
    }
    
    public void registerOverwrite(final AnnotatedElementOverwrite v-1) {
        final AliasedElementName v0 = /*EL:70*/new AliasedElementName(v-1.getElement(), v-1.getAnnotation());
        /*SL:71*/this.validateTargetMethod((ExecutableElement)v-1.getElement(), v-1.getAnnotation(), v0, "@Overwrite", true, false);
        /*SL:72*/this.checkConstraints((ExecutableElement)v-1.getElement(), v-1.getAnnotation());
        /*SL:74*/if (v-1.shouldRemap()) {
            /*SL:75*/for (final TypeHandle a1 : this.mixin.getTargets()) {
                /*SL:76*/if (!this.registerOverwriteForTarget(v-1, a1)) {
                    /*SL:77*/return;
                }
            }
        }
        /*SL:82*/if (!"true".equalsIgnoreCase(this.ap.getOption("disableOverwriteChecker"))) {
            final Diagnostic.Kind v = /*EL:83*/"error".equalsIgnoreCase(this.ap.getOption("overwriteErrorLevel")) ? Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
            final String v2 = /*EL:86*/this.ap.getJavadocProvider().getJavadoc(v-1.getElement());
            /*SL:87*/if (v2 == null) {
                /*SL:88*/this.ap.printMessage(v, "@Overwrite is missing javadoc comment", v-1.getElement());
                /*SL:89*/return;
            }
            /*SL:92*/if (!v2.toLowerCase().contains("@author")) {
                /*SL:93*/this.ap.printMessage(v, "@Overwrite is missing an @author tag", v-1.getElement());
            }
            /*SL:96*/if (!v2.toLowerCase().contains("@reason")) {
                /*SL:97*/this.ap.printMessage(v, "@Overwrite is missing an @reason tag", v-1.getElement());
            }
        }
    }
    
    private boolean registerOverwriteForTarget(final AnnotatedElementOverwrite v-3, final TypeHandle v-2) {
        final MappingMethod mappingMethod = /*EL:103*/v-2.getMappingMethod(v-3.getSimpleName(), v-3.getDesc());
        final ObfuscationData<MappingMethod> v0 = /*EL:104*/this.obf.getDataProvider().getObfMethod(mappingMethod);
        /*SL:106*/if (v0.isEmpty()) {
            Diagnostic.Kind a2 = Diagnostic.Kind.ERROR;
            try {
                /*SL:111*/a2 = ((ExecutableElement)v-3.getElement()).getClass().getMethod("isStatic", (Class<?>[])new Class[0]);
                /*SL:112*/if (a2.invoke(v-3.getElement(), new Object[0])) {
                    /*SL:113*/a2 = Diagnostic.Kind.WARNING;
                }
            }
            catch (Exception ex) {}
            /*SL:119*/this.ap.printMessage(a2, "No obfuscation mapping for @Overwrite method", v-3.getElement());
            /*SL:120*/return false;
        }
        try {
            /*SL:124*/this.addMethodMappings(v-3.getSimpleName(), v-3.getDesc(), v0);
        }
        catch (Mappings.MappingConflictException v) {
            /*SL:126*/v-3.printMessage(this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Overwrite method: " + v.getNew().getSimpleName() + " for target " + v-2 + " conflicts with existing mapping " + v.getOld().getSimpleName());
            /*SL:128*/return false;
        }
        /*SL:130*/return true;
    }
    
    static class AnnotatedElementOverwrite extends AnnotatedElement<ExecutableElement>
    {
        private final boolean shouldRemap;
        
        public AnnotatedElementOverwrite(final ExecutableElement a1, final AnnotationHandle a2, final boolean a3) {
            super((Element)a1, a2);
            this.shouldRemap = a3;
        }
        
        public boolean shouldRemap() {
            /*SL:56*/return this.shouldRemap;
        }
    }
}
