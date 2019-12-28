package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import javax.lang.model.element.VariableElement;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

class AnnotatedMixinElementHandlerShadow extends AnnotatedMixinElementHandler
{
    AnnotatedMixinElementHandlerShadow(final IMixinAnnotationProcessor a1, final AnnotatedMixin a2) {
        super(a1, a2);
    }
    
    public void registerShadow(final AnnotatedElementShadow<?, ?> v2) {
        /*SL:153*/this.validateTarget(v2.getElement(), v2.getAnnotation(), v2.getName(), "@Shadow");
        /*SL:155*/if (!v2.shouldRemap()) {
            /*SL:156*/return;
        }
        /*SL:159*/for (final TypeHandle a1 : this.mixin.getTargets()) {
            /*SL:160*/this.registerShadowForTarget(v2, a1);
        }
    }
    
    private void registerShadowForTarget(final AnnotatedElementShadow<?, ?> v-3, final TypeHandle v-2) {
        final ObfuscationData<? extends IMapping<?>> obfuscationData = /*EL:165*/(ObfuscationData<? extends IMapping<?>>)v-3.getObfuscationData(this.obf.getDataProvider(), v-2);
        /*SL:167*/if (obfuscationData.isEmpty()) {
            final String a1 = /*EL:168*/this.mixin.isMultiTarget() ? (" in target " + v-2) : "";
            /*SL:169*/if (v-2.isSimulated()) {
                /*SL:170*/v-3.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + a1 + " for @Shadow " + v-3);
            }
            else {
                /*SL:172*/v-3.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + a1 + " for @Shadow " + v-3);
            }
            /*SL:174*/return;
        }
        /*SL:177*/for (final ObfuscationType v1 : obfuscationData) {
            try {
                /*SL:179*/v-3.addMapping(v1, (IMapping<?>)obfuscationData.get(v1));
            }
            catch (Mappings.MappingConflictException a2) {
                /*SL:181*/v-3.printMessage(this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Shadow " + v-3 + ": " + a2.getNew().getSimpleName() + " for target " + v-2 + " conflicts with existing mapping " + a2.getOld().getSimpleName());
            }
        }
    }
    
    abstract static class AnnotatedElementShadow<E extends java.lang.Object, M extends java.lang.Object> extends AnnotatedElement<E>
    {
        private final boolean shouldRemap;
        private final ShadowElementName name;
        private final IMapping.Type type;
        
        protected AnnotatedElementShadow(final E a1, final AnnotationHandle a2, final boolean a3, final IMapping.Type a4) {
            super((Element)a1, a2);
            this.shouldRemap = a3;
            this.name = new ShadowElementName((Element)a1, a2);
            this.type = a4;
        }
        
        public boolean shouldRemap() {
            /*SL:69*/return this.shouldRemap;
        }
        
        public ShadowElementName getName() {
            /*SL:73*/return this.name;
        }
        
        public IMapping.Type getElementType() {
            /*SL:77*/return this.type;
        }
        
        @Override
        public String toString() {
            /*SL:82*/return this.getElementType().name().toLowerCase();
        }
        
        public ShadowElementName setObfuscatedName(final IMapping<?> a1) {
            /*SL:86*/return this.setObfuscatedName(a1.getSimpleName());
        }
        
        public ShadowElementName setObfuscatedName(final String a1) {
            /*SL:90*/return this.getName().setObfuscatedName(a1);
        }
        
        public ObfuscationData<M> getObfuscationData(final IObfuscationDataProvider a1, final TypeHandle a2) {
            /*SL:94*/return a1.<M>getObfEntry((IMapping<M>)this.getMapping(a2, this.getName().toString(), this.getDesc()));
        }
        
        public abstract M getMapping(final TypeHandle p0, final String p1, final String p2);
        
        public abstract void addMapping(final ObfuscationType p0, final IMapping<?> p1);
    }
    
    class AnnotatedElementShadowField extends AnnotatedElementShadow<VariableElement, MappingField>
    {
        public AnnotatedElementShadowField(final VariableElement a2, final AnnotationHandle a3, final boolean a4) {
            super((Element)a2, a3, a4, IMapping.Type.FIELD);
        }
        
        @Override
        public MappingField getMapping(final TypeHandle a1, final String a2, final String a3) {
            /*SL:114*/return new MappingField(a1.getName(), a2, a3);
        }
        
        @Override
        public void addMapping(final ObfuscationType a1, final IMapping<?> a2) {
            /*SL:119*/AnnotatedMixinElementHandlerShadow.this.addFieldMapping(a1, this.setObfuscatedName(a2), this.getDesc(), a2.getDesc());
        }
    }
    
    class AnnotatedElementShadowMethod extends AnnotatedElementShadow<ExecutableElement, MappingMethod>
    {
        public AnnotatedElementShadowMethod(final ExecutableElement a2, final AnnotationHandle a3, final boolean a4) {
            super((Element)a2, a3, a4, IMapping.Type.METHOD);
        }
        
        @Override
        public MappingMethod getMapping(final TypeHandle a1, final String a2, final String a3) {
            /*SL:135*/return a1.getMappingMethod(a2, a3);
        }
        
        @Override
        public void addMapping(final ObfuscationType a1, final IMapping<?> a2) {
            /*SL:140*/AnnotatedMixinElementHandlerShadow.this.addMethodMapping(a1, this.setObfuscatedName(a2), this.getDesc(), a2.getDesc());
        }
    }
}
