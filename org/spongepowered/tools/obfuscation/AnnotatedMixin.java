package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import org.spongepowered.asm.mixin.Mixin;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.Pseudo;
import java.util.Collection;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;

class AnnotatedMixin
{
    private final AnnotationHandle annotation;
    private final Messager messager;
    private final ITypeHandleProvider typeProvider;
    private final IObfuscationManager obf;
    private final IMappingConsumer mappings;
    private final TypeElement mixin;
    private final List<ExecutableElement> methods;
    private final TypeHandle handle;
    private final List<TypeHandle> targets;
    private final TypeHandle primaryTarget;
    private final String classRef;
    private final boolean remap;
    private final boolean virtual;
    private final AnnotatedMixinElementHandlerOverwrite overwrites;
    private final AnnotatedMixinElementHandlerShadow shadows;
    private final AnnotatedMixinElementHandlerInjector injectors;
    private final AnnotatedMixinElementHandlerAccessor accessors;
    private final AnnotatedMixinElementHandlerSoftImplements softImplements;
    private boolean validated;
    
    public AnnotatedMixin(final IMixinAnnotationProcessor a1, final TypeElement a2) {
        this.targets = new ArrayList<TypeHandle>();
        this.validated = false;
        this.typeProvider = a1.getTypeProvider();
        this.obf = a1.getObfuscationManager();
        this.mappings = this.obf.createMappingConsumer();
        this.messager = a1;
        this.mixin = a2;
        this.handle = new TypeHandle(a2);
        this.methods = new ArrayList<ExecutableElement>((Collection<? extends ExecutableElement>)this.handle.<Object>getEnclosedElements(ElementKind.METHOD));
        this.virtual = this.handle.getAnnotation(Pseudo.class).exists();
        this.annotation = this.handle.getAnnotation(Mixin.class);
        this.classRef = TypeUtils.getInternalName(a2);
        this.primaryTarget = this.initTargets();
        this.remap = (this.annotation.getBoolean("remap", true) && this.targets.size() > 0);
        this.overwrites = new AnnotatedMixinElementHandlerOverwrite(a1, this);
        this.shadows = new AnnotatedMixinElementHandlerShadow(a1, this);
        this.injectors = new AnnotatedMixinElementHandlerInjector(a1, this);
        this.accessors = new AnnotatedMixinElementHandlerAccessor(a1, this);
        this.softImplements = new AnnotatedMixinElementHandlerSoftImplements(a1, this);
    }
    
    AnnotatedMixin runValidators(final IMixinValidator.ValidationPass v1, final Collection<IMixinValidator> v2) {
        /*SL:183*/for (final IMixinValidator a1 : v2) {
            /*SL:184*/if (!a1.validate(v1, this.mixin, this.annotation, this.targets)) {
                /*SL:185*/break;
            }
        }
        /*SL:189*/if (v1 == IMixinValidator.ValidationPass.FINAL && !this.validated) {
            /*SL:190*/this.validated = true;
            /*SL:191*/this.runFinalValidation();
        }
        /*SL:194*/return this;
    }
    
    private TypeHandle initTargets() {
        TypeHandle typeHandle = /*EL:198*/null;
        try {
            /*SL:202*/for (final TypeMirror v0 : this.annotation.<TypeMirror>getList()) {
                final TypeHandle v = /*EL:203*/new TypeHandle((DeclaredType)v0);
                /*SL:204*/if (this.targets.contains(v)) {
                    /*SL:205*/continue;
                }
                /*SL:207*/this.addTarget(v);
                /*SL:208*/if (typeHandle != null) {
                    continue;
                }
                /*SL:209*/typeHandle = v;
            }
        }
        catch (Exception ex) {
            /*SL:213*/this.printMessage(Diagnostic.Kind.WARNING, "Error processing public targets: " + ex.getClass().getName() + ": " + ex.getMessage(), this);
        }
        try {
            /*SL:218*/for (final String v2 : this.annotation.<String>getList("targets")) {
                TypeHandle v = /*EL:219*/this.typeProvider.getTypeHandle(v2);
                /*SL:220*/if (this.targets.contains(v)) {
                    /*SL:221*/continue;
                }
                /*SL:223*/if (this.virtual) {
                    /*SL:224*/v = this.typeProvider.getSimulatedHandle(v2, this.mixin.asType());
                }
                else {
                    /*SL:225*/if (v == null) {
                        /*SL:226*/this.printMessage(Diagnostic.Kind.ERROR, "Mixin target " + v2 + " could not be found", this);
                        /*SL:227*/return null;
                    }
                    /*SL:228*/if (v.isPublic()) {
                        /*SL:229*/this.printMessage(Diagnostic.Kind.WARNING, "Mixin target " + v2 + " is public and must be specified in value", this);
                        /*SL:230*/return null;
                    }
                }
                /*SL:232*/this.addSoftTarget(v, v2);
                /*SL:233*/if (typeHandle != null) {
                    continue;
                }
                /*SL:234*/typeHandle = v;
            }
        }
        catch (Exception ex) {
            /*SL:238*/this.printMessage(Diagnostic.Kind.WARNING, "Error processing private targets: " + ex.getClass().getName() + ": " + ex.getMessage(), this);
        }
        /*SL:241*/if (typeHandle == null) {
            /*SL:242*/this.printMessage(Diagnostic.Kind.ERROR, "Mixin has no targets", this);
        }
        /*SL:245*/return typeHandle;
    }
    
    private void printMessage(final Diagnostic.Kind a1, final CharSequence a2, final AnnotatedMixin a3) {
        /*SL:252*/this.messager.printMessage(a1, a2, this.mixin, this.annotation.asMirror());
    }
    
    private void addSoftTarget(final TypeHandle a1, final String a2) {
        final ObfuscationData<String> v1 = /*EL:256*/this.obf.getDataProvider().getObfClass(a1);
        /*SL:257*/if (!v1.isEmpty()) {
            /*SL:258*/this.obf.getReferenceManager().addClassMapping(this.classRef, a2, v1);
        }
        /*SL:261*/this.addTarget(a1);
    }
    
    private void addTarget(final TypeHandle a1) {
        /*SL:265*/this.targets.add(a1);
    }
    
    @Override
    public String toString() {
        /*SL:270*/return this.mixin.getSimpleName().toString();
    }
    
    public AnnotationHandle getAnnotation() {
        /*SL:274*/return this.annotation;
    }
    
    public TypeElement getMixin() {
        /*SL:281*/return this.mixin;
    }
    
    public TypeHandle getHandle() {
        /*SL:288*/return this.handle;
    }
    
    public String getClassRef() {
        /*SL:295*/return this.classRef;
    }
    
    public boolean isInterface() {
        /*SL:302*/return this.mixin.getKind() == ElementKind.INTERFACE;
    }
    
    @Deprecated
    public TypeHandle getPrimaryTarget() {
        /*SL:310*/return this.primaryTarget;
    }
    
    public List<TypeHandle> getTargets() {
        /*SL:317*/return this.targets;
    }
    
    public boolean isMultiTarget() {
        /*SL:324*/return this.targets.size() > 1;
    }
    
    public boolean remap() {
        /*SL:331*/return this.remap;
    }
    
    public IMappingConsumer getMappings() {
        /*SL:335*/return this.mappings;
    }
    
    private void runFinalValidation() {
        /*SL:339*/for (final ExecutableElement v1 : this.methods) {
            /*SL:340*/this.overwrites.registerMerge(v1);
        }
    }
    
    public void registerOverwrite(final ExecutableElement a1, final AnnotationHandle a2, final boolean a3) {
        /*SL:345*/this.methods.remove(a1);
        /*SL:346*/this.overwrites.registerOverwrite(new AnnotatedMixinElementHandlerOverwrite.AnnotatedElementOverwrite(a1, a2, a3));
    }
    
    public void registerShadow(final VariableElement a1, final AnnotationHandle a2, final boolean a3) {
        /*SL:350*/this.shadows.registerShadow(this.shadows.new AnnotatedElementShadowField(a1, a2, a3));
    }
    
    public void registerShadow(final ExecutableElement a1, final AnnotationHandle a2, final boolean a3) {
        /*SL:354*/this.methods.remove(a1);
        /*SL:355*/this.shadows.registerShadow(this.shadows.new AnnotatedElementShadowMethod(a1, a2, a3));
    }
    
    public void registerInjector(final ExecutableElement v-8, final AnnotationHandle v-7, final InjectorRemap v-6) {
        /*SL:359*/this.methods.remove(v-8);
        /*SL:360*/this.injectors.registerInjector(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjector(v-8, v-7, v-6));
        final List<AnnotationHandle> annotationList = /*EL:362*/v-7.getAnnotationList("at");
        /*SL:363*/for (final AnnotationHandle a1 : annotationList) {
            /*SL:364*/this.registerInjectionPoint(v-8, v-7, a1, v-6, "@At(%s)");
        }
        final List<AnnotationHandle> annotationList2 = /*EL:367*/v-7.getAnnotationList("slice");
        /*SL:368*/for (final AnnotationHandle annotationHandle : annotationList2) {
            final String a2 = /*EL:369*/annotationHandle.<String>getValue("id", "");
            final AnnotationHandle a3 = /*EL:371*/annotationHandle.getAnnotation("from");
            /*SL:372*/if (a3 != null) {
                /*SL:373*/this.registerInjectionPoint(v-8, v-7, a3, v-6, "@Slice[" + a2 + "](from=@At(%s))");
            }
            final AnnotationHandle v1 = /*EL:376*/annotationHandle.getAnnotation("to");
            /*SL:377*/if (v1 != null) {
                /*SL:378*/this.registerInjectionPoint(v-8, v-7, v1, v-6, "@Slice[" + a2 + "](to=@At(%s))");
            }
        }
    }
    
    public void registerInjectionPoint(final ExecutableElement a1, final AnnotationHandle a2, final AnnotationHandle a3, final InjectorRemap a4, final String a5) {
        /*SL:384*/this.injectors.registerInjectionPoint(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjectionPoint(a1, a2, a3, a4), a5);
    }
    
    public void registerAccessor(final ExecutableElement a1, final AnnotationHandle a2, final boolean a3) {
        /*SL:388*/this.methods.remove(a1);
        /*SL:389*/this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementAccessor(a1, a2, a3));
    }
    
    public void registerInvoker(final ExecutableElement a1, final AnnotationHandle a2, final boolean a3) {
        /*SL:393*/this.methods.remove(a1);
        /*SL:394*/this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementInvoker(a1, a2, a3));
    }
    
    public void registerSoftImplements(final AnnotationHandle a1) {
        /*SL:398*/this.softImplements.process(a1);
    }
}
