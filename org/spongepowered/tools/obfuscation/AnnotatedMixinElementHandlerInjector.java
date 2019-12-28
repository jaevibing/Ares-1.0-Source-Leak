package org.spongepowered.tools.obfuscation;

import java.util.HashMap;
import java.util.Map;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.injection.Coerce;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import javax.lang.model.element.AnnotationMirror;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.mixin.injection.struct.InvalidMemberDescriptorException;
import javax.annotation.processing.Messager;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

class AnnotatedMixinElementHandlerInjector extends AnnotatedMixinElementHandler
{
    AnnotatedMixinElementHandlerInjector(final IMixinAnnotationProcessor a1, final AnnotatedMixin a2) {
        super(a1, a2);
    }
    
    public void registerInjector(final AnnotatedElementInjector v-4) {
        /*SL:150*/if (this.mixin.isInterface()) {
            /*SL:151*/this.ap.printMessage(Diagnostic.Kind.ERROR, "Injector in interface is unsupported", v-4.getElement());
        }
        /*SL:154*/for (final String s : v-4.getAnnotation().<String>getList("method")) {
            final MemberInfo parse = /*EL:155*/MemberInfo.parse(s);
            /*SL:156*/if (parse.name == null) {
                /*SL:157*/continue;
            }
            try {
                /*SL:161*/parse.validate();
            }
            catch (InvalidMemberDescriptorException a1) {
                /*SL:163*/v-4.printMessage(this.ap, Diagnostic.Kind.ERROR, a1.getMessage());
            }
            /*SL:166*/if (parse.desc != null) {
                /*SL:167*/this.validateReferencedTarget((ExecutableElement)v-4.getElement(), v-4.getAnnotation(), parse, v-4.toString());
            }
            /*SL:170*/if (!v-4.shouldRemap()) {
                /*SL:171*/continue;
            }
            /*SL:174*/for (final TypeHandle v1 : this.mixin.getTargets()) {
                /*SL:175*/if (!this.registerInjector(v-4, s, parse, v1)) {
                    /*SL:176*/break;
                }
            }
        }
    }
    
    private boolean registerInjector(final AnnotatedElementInjector v-9, final String v-8, final MemberInfo v-7, final TypeHandle v-6) {
        final String descriptor = /*EL:183*/v-6.findDescriptor(v-7);
        /*SL:184*/if (descriptor == null) {
            final Diagnostic.Kind a1 = /*EL:185*/this.mixin.isMultiTarget() ? Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
            /*SL:186*/if (v-6.isSimulated()) {
                /*SL:187*/v-9.printMessage(this.ap, Diagnostic.Kind.NOTE, v-9 + " target '" + v-8 + "' in @Pseudo mixin will not be obfuscated");
            }
            else/*SL:188*/ if (v-6.isImaginary()) {
                /*SL:189*/v-9.printMessage(this.ap, a1, v-9 + " target requires method signature because enclosing type information for " + v-6 + " is unavailable");
            }
            else/*SL:191*/ if (!v-7.isInitialiser()) {
                /*SL:192*/v-9.printMessage(this.ap, a1, "Unable to determine signature for " + v-9 + " target method");
            }
            /*SL:194*/return true;
        }
        final String string = /*EL:197*/v-9 + " target " + v-7.name;
        final MappingMethod mappingMethod = /*EL:198*/v-6.getMappingMethod(v-7.name, descriptor);
        ObfuscationData<MappingMethod> obfuscationData = /*EL:199*/this.obf.getDataProvider().getObfMethod(mappingMethod);
        /*SL:200*/if (obfuscationData.isEmpty()) {
            /*SL:201*/if (v-6.isSimulated()) {
                /*SL:202*/obfuscationData = this.obf.getDataProvider().getRemappedMethod(mappingMethod);
            }
            else {
                /*SL:203*/if (v-7.isClassInitialiser()) {
                    /*SL:204*/return true;
                }
                final Diagnostic.Kind a2 = /*EL:206*/v-7.isConstructor() ? Diagnostic.Kind.WARNING : Diagnostic.Kind.ERROR;
                /*SL:207*/v-9.addMessage(a2, "No obfuscation mapping for " + string, v-9.getElement(), v-9.getAnnotation());
                /*SL:208*/return false;
            }
        }
        final IReferenceManager referenceManager = /*EL:212*/this.obf.getReferenceManager();
        try {
            /*SL:215*/if ((v-7.owner == null && this.mixin.isMultiTarget()) || v-6.isSimulated()) {
                /*SL:216*/obfuscationData = AnnotatedMixinElementHandler.<MappingMethod>stripOwnerData(obfuscationData);
            }
            /*SL:218*/referenceManager.addMethodMapping(this.classRef, v-8, obfuscationData);
        }
        catch (ReferenceManager.ReferenceConflictException v2) {
            final String v1 = /*EL:220*/this.mixin.isMultiTarget() ? "Multi-target" : "Target";
            /*SL:222*/if (v-9.hasCoerceArgument() && v-7.owner == null && v-7.desc == null) {
                final MemberInfo a3 = /*EL:223*/MemberInfo.parse(v2.getOld());
                final MemberInfo a4 = /*EL:224*/MemberInfo.parse(v2.getNew());
                /*SL:225*/if (a3.name.equals(a4.name)) {
                    /*SL:226*/obfuscationData = AnnotatedMixinElementHandler.<MappingMethod>stripDescriptors(obfuscationData);
                    /*SL:227*/referenceManager.setAllowConflicts(true);
                    /*SL:228*/referenceManager.addMethodMapping(this.classRef, v-8, obfuscationData);
                    /*SL:229*/referenceManager.setAllowConflicts(false);
                    /*SL:232*/v-9.printMessage(this.ap, Diagnostic.Kind.WARNING, "Coerced " + v1 + " reference has conflicting descriptors for " + string + ": Storing bare references " + obfuscationData.values() + /*EL:233*/" in refMap");
                    /*SL:234*/return true;
                }
            }
            /*SL:238*/v-9.printMessage(this.ap, Diagnostic.Kind.ERROR, v1 + " reference conflict for " + string + ": " + v-8 + " -> " + v2.getNew() + /*EL:239*/" previously defined as " + v2.getOld());
        }
        /*SL:242*/return true;
    }
    
    public void registerInjectionPoint(final AnnotatedElementInjectionPoint a1, final String a2) {
        /*SL:250*/if (this.mixin.isInterface()) {
            /*SL:251*/this.ap.printMessage(Diagnostic.Kind.ERROR, "Injector in interface is unsupported", a1.getElement());
        }
        /*SL:254*/if (!a1.shouldRemap()) {
            /*SL:255*/return;
        }
        final String v1 = /*EL:258*/InjectionPointData.parseType(a1.getAt().<String>getValue("value"));
        final String v2 = /*EL:259*/a1.getAt().<String>getValue("target");
        /*SL:261*/if ("NEW".equals(v1)) {
            /*SL:262*/this.remapNewTarget(String.format(a2, v1 + ".<target>"), v2, a1);
            /*SL:263*/this.remapNewTarget(String.format(a2, v1 + ".args[class]"), a1.getAtArg("class"), a1);
        }
        else {
            /*SL:265*/this.remapReference(String.format(a2, v1 + ".<target>"), v2, a1);
        }
    }
    
    protected final void remapNewTarget(final String v-5, final String v-4, final AnnotatedElementInjectionPoint v-3) {
        /*SL:270*/if (v-4 == null) {
            /*SL:271*/return;
        }
        final MemberInfo parse = /*EL:274*/MemberInfo.parse(v-4);
        final String ctorType = /*EL:275*/parse.toCtorType();
        /*SL:277*/if (ctorType != null) {
            String a3 = /*EL:278*/parse.toCtorDesc();
            final MappingMethod v1 = /*EL:279*/new MappingMethod(ctorType, ".", (a3 != null) ? a3 : "()V");
            final ObfuscationData<MappingMethod> v2 = /*EL:280*/this.obf.getDataProvider().getRemappedMethod(v1);
            /*SL:281*/if (v2.isEmpty()) {
                /*SL:282*/this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find class mapping for " + v-5 + " '" + ctorType + "'", v-3.getElement(), v-3.getAnnotation().asMirror());
                /*SL:284*/return;
            }
            final ObfuscationData<String> v3 = /*EL:287*/new ObfuscationData<String>();
            /*SL:288*/for (final ObfuscationType a2 : v2) {
                /*SL:289*/a3 = v2.get(a2);
                /*SL:290*/if (a3 == null) {
                    /*SL:291*/v3.put(a2, a3.getOwner());
                }
                else {
                    /*SL:293*/v3.put(a2, a3.getDesc().replace(")V", ")L" + a3.getOwner() + ";"));
                }
            }
            /*SL:297*/this.obf.getReferenceManager().addClassMapping(this.classRef, v-4, v3);
        }
        /*SL:300*/v-3.notifyRemapped();
    }
    
    protected final void remapReference(final String v-4, final String v-3, final AnnotatedElementInjectionPoint v-2) {
        /*SL:304*/if (v-3 == null) {
            /*SL:305*/return;
        }
        final AnnotationMirror mirror = /*EL:309*/((this.ap.getCompilerEnvironment() == IMixinAnnotationProcessor.CompilerEnvironment.JDT) ? v-2.getAt() : v-2.getAnnotation()).asMirror();
        final MemberInfo v0 = /*EL:311*/MemberInfo.parse(v-3);
        /*SL:312*/if (!v0.isFullyQualified()) {
            final String a1 = /*EL:313*/(v0.owner == null) ? ((v0.desc == null) ? "owner and signature" : "owner") : "signature";
            /*SL:314*/this.ap.printMessage(Diagnostic.Kind.ERROR, v-4 + " is not fully qualified, missing " + a1, v-2.getElement(), mirror);
            /*SL:315*/return;
        }
        try {
            /*SL:319*/v0.validate();
        }
        catch (InvalidMemberDescriptorException a2) {
            /*SL:321*/this.ap.printMessage(Diagnostic.Kind.ERROR, a2.getMessage(), v-2.getElement(), mirror);
        }
        try {
            /*SL:325*/if (v0.isField()) {
                final ObfuscationData<MappingField> a3 = /*EL:326*/this.obf.getDataProvider().getObfFieldRecursive(v0);
                /*SL:327*/if (a3.isEmpty()) {
                    /*SL:328*/this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find field mapping for " + v-4 + " '" + v-3 + "'", v-2.getElement(), mirror);
                    /*SL:330*/return;
                }
                /*SL:332*/this.obf.getReferenceManager().addFieldMapping(this.classRef, v-3, v0, a3);
            }
            else {
                final ObfuscationData<MappingMethod> a4 = /*EL:334*/this.obf.getDataProvider().getObfMethodRecursive(v0);
                /*SL:335*/if (a4.isEmpty() && /*EL:336*/(v0.owner == null || !v0.owner.startsWith("java/lang/"))) {
                    /*SL:337*/this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find method mapping for " + v-4 + " '" + v-3 + "'", v-2.getElement(), mirror);
                    /*SL:339*/return;
                }
                /*SL:342*/this.obf.getReferenceManager().addMethodMapping(this.classRef, v-3, v0, a4);
            }
        }
        catch (ReferenceManager.ReferenceConflictException v) {
            /*SL:347*/this.ap.printMessage(Diagnostic.Kind.ERROR, "Unexpected reference conflict for " + v-4 + ": " + v-3 + " -> " + v.getNew() + /*EL:348*/" previously defined as " + v.getOld(), v-2.getElement(), mirror);
            /*SL:349*/return;
        }
        /*SL:352*/v-2.notifyRemapped();
    }
    
    static class AnnotatedElementInjector extends AnnotatedElement<ExecutableElement>
    {
        private final InjectorRemap state;
        
        public AnnotatedElementInjector(final ExecutableElement a1, final AnnotationHandle a2, final InjectorRemap a3) {
            super((Element)a1, a2);
            this.state = a3;
        }
        
        public boolean shouldRemap() {
            /*SL:69*/return this.state.shouldRemap();
        }
        
        public boolean hasCoerceArgument() {
            /*SL:73*/if (!this.annotation.toString().equals("@Inject")) {
                /*SL:74*/return false;
            }
            final Iterator<? extends VariableElement> iterator = /*EL:77*/((ExecutableElement)this.element).getParameters().iterator();
            if (iterator.hasNext()) {
                final VariableElement v1 = (VariableElement)iterator.next();
                /*SL:78*/return AnnotationHandle.of(v1, Coerce.class).exists();
            }
            /*SL:81*/return false;
        }
        
        public void addMessage(final Diagnostic.Kind a1, final CharSequence a2, final Element a3, final AnnotationHandle a4) {
            /*SL:85*/this.state.addMessage(a1, a2, a3, a4);
        }
        
        @Override
        public String toString() {
            /*SL:90*/return this.getAnnotation().toString();
        }
    }
    
    static class AnnotatedElementInjectionPoint extends AnnotatedElement<ExecutableElement>
    {
        private final AnnotationHandle at;
        private Map<String, String> args;
        private final InjectorRemap state;
        
        public AnnotatedElementInjectionPoint(final ExecutableElement a1, final AnnotationHandle a2, final AnnotationHandle a3, final InjectorRemap a4) {
            super((Element)a1, a2);
            this.at = a3;
            this.state = a4;
        }
        
        public boolean shouldRemap() {
            /*SL:113*/return this.at.getBoolean("remap", this.state.shouldRemap());
        }
        
        public AnnotationHandle getAt() {
            /*SL:117*/return this.at;
        }
        
        public String getAtArg(final String v-1) {
            /*SL:121*/if (this.args == null) {
                /*SL:122*/this.args = new HashMap<String, String>();
                /*SL:123*/for (final String v1 : this.at.<String>getList("args")) {
                    /*SL:124*/if (v1 == null) {
                        /*SL:125*/continue;
                    }
                    final int a1 = /*EL:127*/v1.indexOf(61);
                    /*SL:128*/if (a1 > -1) {
                        /*SL:129*/this.args.put(v1.substring(0, a1), v1.substring(a1 + 1));
                    }
                    else {
                        /*SL:131*/this.args.put(v1, "");
                    }
                }
            }
            /*SL:136*/return this.args.get(v-1);
        }
        
        public void notifyRemapped() {
            /*SL:140*/this.state.notifyRemapped();
        }
    }
}
