package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.ElementKind;
import java.util.Iterator;
import java.util.List;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.lang.model.type.DeclaredType;
import org.spongepowered.asm.mixin.Interface;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

public class AnnotatedMixinElementHandlerSoftImplements extends AnnotatedMixinElementHandler
{
    AnnotatedMixinElementHandlerSoftImplements(final IMixinAnnotationProcessor a1, final AnnotatedMixin a2) {
        super(a1, a2);
    }
    
    public void process(final AnnotationHandle v-5) {
        /*SL:62*/if (!this.mixin.remap()) {
            /*SL:63*/return;
        }
        final List<AnnotationHandle> annotationList = /*EL:66*/v-5.getAnnotationList("value");
        /*SL:69*/if (annotationList.size() < 1) {
            /*SL:70*/this.ap.printMessage(Diagnostic.Kind.WARNING, "Empty @Implements annotation", this.mixin.getMixin(), v-5.asMirror());
            /*SL:71*/return;
        }
        /*SL:74*/for (final AnnotationHandle annotationHandle : annotationList) {
            final Interface.Remap v3 = /*EL:75*/annotationHandle.<Interface.Remap>getValue("remap", Interface.Remap.ALL);
            /*SL:76*/if (v3 == Interface.Remap.NONE) {
                /*SL:77*/continue;
            }
            try {
                final TypeHandle a1 = /*EL:81*/new TypeHandle(annotationHandle.<DeclaredType>getValue("iface"));
                final String v1 = /*EL:82*/annotationHandle.<String>getValue("prefix");
                /*SL:83*/this.processSoftImplements(v3, a1, v1);
            }
            catch (Exception v2) {
                /*SL:85*/this.ap.printMessage(Diagnostic.Kind.ERROR, "Unexpected error: " + v2.getClass().getName() + ": " + v2.getMessage(), this.mixin.getMixin(), annotationHandle.asMirror());
            }
        }
    }
    
    private void processSoftImplements(final Interface.Remap v1, final TypeHandle v2, final String v3) {
        /*SL:100*/for (final ExecutableElement a1 : v2.<ExecutableElement>getEnclosedElements(ElementKind.METHOD)) {
            /*SL:101*/this.processMethod(v1, v2, v3, a1);
        }
        /*SL:104*/for (final TypeHandle a2 : v2.getInterfaces()) {
            /*SL:105*/this.processSoftImplements(v1, a2, v3);
        }
    }
    
    private void processMethod(final Interface.Remap a4, final TypeHandle v1, final String v2, final ExecutableElement v3) {
        final String v4 = /*EL:120*/v3.getSimpleName().toString();
        final String v5 = /*EL:121*/TypeUtils.getJavaSignature(v3);
        final String v6 = /*EL:122*/TypeUtils.getDescriptor(v3);
        /*SL:124*/if (a4 != Interface.Remap.ONLY_PREFIXED) {
            final MethodHandle a5 = /*EL:125*/this.mixin.getHandle().findMethod(v4, v5);
            /*SL:126*/if (a5 != null) {
                /*SL:127*/this.addInterfaceMethodMapping(a4, v1, null, a5, v4, v6);
            }
        }
        /*SL:131*/if (v2 != null) {
            final MethodHandle a6 = /*EL:132*/this.mixin.getHandle().findMethod(v2 + v4, v5);
            /*SL:133*/if (a6 != null) {
                /*SL:134*/this.addInterfaceMethodMapping(a4, v1, v2, a6, v4, v6);
            }
        }
    }
    
    private void addInterfaceMethodMapping(final Interface.Remap a1, final TypeHandle a2, final String a3, final MethodHandle a4, final String a5, final String a6) {
        final MappingMethod v1 = /*EL:152*/new MappingMethod(a2.getName(), a5, a6);
        final ObfuscationData<MappingMethod> v2 = /*EL:153*/this.obf.getDataProvider().getObfMethod(v1);
        /*SL:154*/if (v2.isEmpty()) {
            /*SL:155*/if (a1.forceRemap()) {
                /*SL:156*/this.ap.printMessage(Diagnostic.Kind.ERROR, "No obfuscation mapping for soft-implementing method", a4.getElement());
            }
            /*SL:158*/return;
        }
        /*SL:160*/this.addMethodMappings(a4.getName(), a6, this.applyPrefix(v2, a3));
    }
    
    private ObfuscationData<MappingMethod> applyPrefix(final ObfuscationData<MappingMethod> v2, final String v3) {
        /*SL:172*/if (v3 == null) {
            /*SL:173*/return v2;
        }
        final ObfuscationData<MappingMethod> v4 = /*EL:176*/new ObfuscationData<MappingMethod>();
        /*SL:177*/for (MappingMethod a2 : v2) {
            /*SL:178*/a2 = v2.get(a2);
            /*SL:179*/v4.put(a2, a2.addPrefix(v3));
        }
        /*SL:181*/return v4;
    }
}
