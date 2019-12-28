package org.spongepowered.tools.obfuscation;

import javax.annotation.processing.Messager;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import java.util.List;
import org.spongepowered.tools.obfuscation.mirror.FieldHandle;
import org.spongepowered.tools.obfuscation.mirror.Visibility;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.util.throwables.InvalidConstraintException;
import org.spongepowered.asm.util.throwables.ConstraintViolationException;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import org.spongepowered.asm.util.ConstraintParser;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import java.util.Iterator;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

abstract class AnnotatedMixinElementHandler
{
    protected final AnnotatedMixin mixin;
    protected final String classRef;
    protected final IMixinAnnotationProcessor ap;
    protected final IObfuscationManager obf;
    private IMappingConsumer mappings;
    
    AnnotatedMixinElementHandler(final IMixinAnnotationProcessor a1, final AnnotatedMixin a2) {
        this.ap = a1;
        this.mixin = a2;
        this.classRef = a2.getClassRef();
        this.obf = a1.getObfuscationManager();
    }
    
    private IMappingConsumer getMappings() {
        /*SL:303*/if (this.mappings == null) {
            final IMappingConsumer v1 = /*EL:304*/this.mixin.getMappings();
            /*SL:305*/if (v1 instanceof Mappings) {
                /*SL:306*/this.mappings = ((Mappings)v1).asUnique();
            }
            else {
                /*SL:308*/this.mappings = v1;
            }
        }
        /*SL:311*/return this.mappings;
    }
    
    protected final void addFieldMappings(final String v1, final String v2, final ObfuscationData<MappingField> v3) {
        /*SL:315*/for (MappingField a2 : v3) {
            /*SL:316*/a2 = v3.get(a2);
            /*SL:317*/this.addFieldMapping(a2, v1, a2.getSimpleName(), v2, a2.getDesc());
        }
    }
    
    protected final void addFieldMapping(final ObfuscationType a1, final ShadowElementName a2, final String a3, final String a4) {
        /*SL:325*/this.addFieldMapping(a1, a2.name(), a2.obfuscated(), a3, a4);
    }
    
    protected final void addFieldMapping(final ObfuscationType a1, final String a2, final String a3, final String a4, final String a5) {
        final MappingField v1 = /*EL:332*/new MappingField(this.classRef, a2, a4);
        final MappingField v2 = /*EL:333*/new MappingField(this.classRef, a3, a5);
        /*SL:334*/this.getMappings().addFieldMapping(a1, v1, v2);
    }
    
    protected final void addMethodMappings(final String v1, final String v2, final ObfuscationData<MappingMethod> v3) {
        /*SL:338*/for (MappingMethod a2 : v3) {
            /*SL:339*/a2 = v3.get(a2);
            /*SL:340*/this.addMethodMapping(a2, v1, a2.getSimpleName(), v2, a2.getDesc());
        }
    }
    
    protected final void addMethodMapping(final ObfuscationType a1, final ShadowElementName a2, final String a3, final String a4) {
        /*SL:348*/this.addMethodMapping(a1, a2.name(), a2.obfuscated(), a3, a4);
    }
    
    protected final void addMethodMapping(final ObfuscationType a1, final String a2, final String a3, final String a4, final String a5) {
        final MappingMethod v1 = /*EL:355*/new MappingMethod(this.classRef, a2, a4);
        final MappingMethod v2 = /*EL:356*/new MappingMethod(this.classRef, a3, a5);
        /*SL:357*/this.getMappings().addMethodMapping(a1, v1, v2);
    }
    
    protected final void checkConstraints(final ExecutableElement v-1, final AnnotationHandle v0) {
        try {
            final ConstraintParser.Constraint a2 = /*EL:369*/ConstraintParser.parse(v0.<String>getValue("constraints"));
            try {
                /*SL:371*/a2.check(this.ap.getTokenProvider());
            }
            catch (ConstraintViolationException a2) {
                /*SL:373*/this.ap.printMessage(Diagnostic.Kind.ERROR, a2.getMessage(), v-1, v0.asMirror());
            }
        }
        catch (InvalidConstraintException v) {
            /*SL:376*/this.ap.printMessage(Diagnostic.Kind.WARNING, v.getMessage(), v-1, v0.asMirror());
        }
    }
    
    protected final void validateTarget(final Element a1, final AnnotationHandle a2, final AliasedElementName a3, final String a4) {
        /*SL:381*/if (a1 instanceof ExecutableElement) {
            /*SL:382*/this.validateTargetMethod((ExecutableElement)a1, a2, a3, a4, false, false);
        }
        else/*SL:383*/ if (a1 instanceof VariableElement) {
            /*SL:384*/this.validateTargetField((VariableElement)a1, a2, a3, a4);
        }
    }
    
    protected final void validateTargetMethod(final ExecutableElement a5, final AnnotationHandle a6, final AliasedElementName v1, final String v2, final boolean v3, final boolean v4) {
        final String v5 = /*EL:394*/TypeUtils.getJavaSignature(a5);
        /*SL:396*/for (final TypeHandle a7 : this.mixin.getTargets()) {
            /*SL:397*/if (a7.isImaginary()) {
                /*SL:398*/continue;
            }
            MethodHandle a8 = /*EL:402*/a7.findMethod(a5);
            /*SL:405*/if (a8 == null && v1.hasPrefix()) {
                /*SL:406*/a8 = a7.findMethod(v1.baseName(), v5);
            }
            /*SL:410*/if (a8 == null && v1.hasAliases()) {
                /*SL:411*/for (final String a9 : v1.getAliases()) {
                    /*SL:412*/if ((a8 = a7.findMethod(a9, v5)) != null) {
                        /*SL:413*/break;
                    }
                }
            }
            /*SL:418*/if (a8 != null) {
                /*SL:419*/if (!v3) {
                    continue;
                }
                /*SL:420*/this.validateMethodVisibility(a5, a6, v2, a7, a8);
            }
            else {
                /*SL:422*/if (v4) {
                    continue;
                }
                /*SL:423*/this.printMessage(Diagnostic.Kind.WARNING, "Cannot find target for " + v2 + " method in " + a7, a5, a6);
            }
        }
    }
    
    private void validateMethodVisibility(final ExecutableElement a1, final AnnotationHandle a2, final String a3, final TypeHandle a4, final MethodHandle a5) {
        final Visibility v1 = /*EL:430*/a5.getVisibility();
        /*SL:431*/if (v1 == null) {
            /*SL:432*/return;
        }
        final Visibility v2 = /*EL:435*/TypeUtils.getVisibility(a1);
        final String v3 = /*EL:436*/"visibility of " + v1 + " method in " + a4;
        /*SL:437*/if (v1.ordinal() > v2.ordinal()) {
            /*SL:438*/this.printMessage(Diagnostic.Kind.WARNING, v2 + " " + a3 + " method cannot reduce " + v3, a1, a2);
        }
        else/*SL:439*/ if (v1 == Visibility.PRIVATE && v2.ordinal() > v1.ordinal()) {
            /*SL:440*/this.printMessage(Diagnostic.Kind.WARNING, v2 + " " + a3 + " method will upgrade " + v3, a1, a2);
        }
    }
    
    protected final void validateTargetField(final VariableElement v2, final AnnotationHandle v3, final AliasedElementName v4, final String v5) {
        final String v6 = /*EL:449*/v2.asType().toString();
        /*SL:451*/for (String a4 : this.mixin.getTargets()) {
            /*SL:452*/if (a4.isImaginary()) {
                /*SL:453*/continue;
            }
            FieldHandle a2 = /*EL:457*/a4.findField(v2);
            /*SL:458*/if (a2 != null) {
                /*SL:459*/continue;
            }
            final List<String> a3 = /*EL:463*/v4.getAliases();
            final Iterator<String> iterator2 = /*EL:464*/a3.iterator();
            while (iterator2.hasNext()) {
                a4 = iterator2.next();
                /*SL:465*/if ((a2 = a4.findField(a4, v6)) != null) {
                    /*SL:466*/break;
                }
            }
            /*SL:470*/if (a2 != null) {
                continue;
            }
            /*SL:471*/this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find target for " + v5 + " field in " + a4, v2, v3.asMirror());
        }
    }
    
    protected final void validateReferencedTarget(final ExecutableElement a4, final AnnotationHandle v1, final MemberInfo v2, final String v3) {
        final String v4 = /*EL:481*/v2.toDescriptor();
        /*SL:483*/for (final TypeHandle a5 : this.mixin.getTargets()) {
            /*SL:484*/if (a5.isImaginary()) {
                /*SL:485*/continue;
            }
            final MethodHandle a6 = /*EL:488*/a5.findMethod(v2.name, v4);
            /*SL:489*/if (a6 != null) {
                continue;
            }
            /*SL:490*/this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find target method for " + v3 + " in " + a5, a4, v1.asMirror());
        }
    }
    
    private void printMessage(final Diagnostic.Kind a1, final String a2, final Element a3, final AnnotationHandle a4) {
        /*SL:496*/if (a4 == null) {
            /*SL:497*/this.ap.printMessage(a1, a2, a3);
        }
        else {
            /*SL:499*/this.ap.printMessage(a1, a2, a3, a4.asMirror());
        }
    }
    
    protected static <T extends java.lang.Object> ObfuscationData<T> stripOwnerData(final ObfuscationData<T> v-2) {
        final ObfuscationData<T> obfuscationData = /*EL:504*/new ObfuscationData<T>();
        /*SL:505*/for (final ObfuscationType v1 : v-2) {
            final T a1 = /*EL:506*/v-2.get(v1);
            /*SL:507*/obfuscationData.put(v1, ((IMapping<T>)a1).move((String)null));
        }
        /*SL:509*/return obfuscationData;
    }
    
    protected static <T extends java.lang.Object> ObfuscationData<T> stripDescriptors(final ObfuscationData<T> v-2) {
        final ObfuscationData<T> obfuscationData = /*EL:513*/new ObfuscationData<T>();
        /*SL:514*/for (final ObfuscationType v1 : v-2) {
            final T a1 = /*EL:515*/v-2.get(v1);
            /*SL:516*/obfuscationData.put(v1, ((IMapping<T>)a1).transform((String)null));
        }
        /*SL:518*/return obfuscationData;
    }
    
    abstract static class AnnotatedElement<E extends java.lang.Object>
    {
        protected final E element;
        protected final AnnotationHandle annotation;
        private final String desc;
        
        public AnnotatedElement(final E a1, final AnnotationHandle a2) {
            this.element = (Element)a1;
            this.annotation = a2;
            this.desc = TypeUtils.getDescriptor((Element)a1);
        }
        
        public E getElement() {
            /*SL:80*/return (E)this.element;
        }
        
        public AnnotationHandle getAnnotation() {
            /*SL:84*/return this.annotation;
        }
        
        public String getSimpleName() {
            /*SL:88*/return this.getElement().getSimpleName().toString();
        }
        
        public String getDesc() {
            /*SL:92*/return this.desc;
        }
        
        public final void printMessage(final Messager a1, final Diagnostic.Kind a2, final CharSequence a3) {
            /*SL:96*/a1.printMessage(a2, a3, this.element, this.annotation.asMirror());
        }
    }
    
    static class AliasedElementName
    {
        protected final String originalName;
        private final List<String> aliases;
        private boolean caseSensitive;
        
        public AliasedElementName(final Element a1, final AnnotationHandle a2) {
            this.originalName = a1.getSimpleName().toString();
            this.aliases = a2.<String>getList(/*EL:97*/"aliases");
        }
        
        public AliasedElementName setCaseSensitive(final boolean a1) {
            /*SL:124*/this.caseSensitive = a1;
            /*SL:125*/return this;
        }
        
        public boolean isCaseSensitive() {
            /*SL:129*/return this.caseSensitive;
        }
        
        public boolean hasAliases() {
            /*SL:136*/return this.aliases.size() > 0;
        }
        
        public List<String> getAliases() {
            /*SL:143*/return this.aliases;
        }
        
        public String elementName() {
            /*SL:150*/return this.originalName;
        }
        
        public String baseName() {
            /*SL:154*/return this.originalName;
        }
        
        public boolean hasPrefix() {
            /*SL:158*/return false;
        }
    }
    
    static class ShadowElementName extends AliasedElementName
    {
        private final boolean hasPrefix;
        private final String prefix;
        private final String baseName;
        private String obfuscated;
        
        ShadowElementName(final Element a1, final AnnotationHandle a2) {
            super(a1, a2);
            this.prefix = a2.<String>getValue("prefix", "shadow$");
            boolean v1 = false;
            String v2 = this.originalName;
            if (v2.startsWith(this.prefix)) {
                v1 = true;
                v2 = v2.substring(this.prefix.length());
            }
            this.hasPrefix = v1;
            final String s = v2;
            this.baseName = s;
            this.obfuscated = s;
        }
        
        @Override
        public String toString() {
            /*SL:211*/return this.baseName;
        }
        
        @Override
        public String baseName() {
            /*SL:216*/return this.baseName;
        }
        
        public ShadowElementName setObfuscatedName(final IMapping<?> a1) {
            /*SL:226*/this.obfuscated = a1.getName();
            /*SL:227*/return this;
        }
        
        public ShadowElementName setObfuscatedName(final String a1) {
            /*SL:237*/this.obfuscated = a1;
            /*SL:238*/return this;
        }
        
        @Override
        public boolean hasPrefix() {
            /*SL:243*/return this.hasPrefix;
        }
        
        public String prefix() {
            /*SL:250*/return this.hasPrefix ? this.prefix : "";
        }
        
        public String name() {
            /*SL:257*/return this.prefix(this.baseName);
        }
        
        public String obfuscated() {
            /*SL:264*/return this.prefix(this.obfuscated);
        }
        
        public String prefix(final String a1) {
            /*SL:274*/return this.hasPrefix ? (this.prefix + a1) : a1;
        }
    }
}
