package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.mirror.TypeHandleSimulated;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationMirror;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.lang.annotation.Annotation;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.asm.mixin.gen.Accessor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeReference;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import javax.lang.model.element.TypeElement;
import java.io.InputStream;
import javax.tools.FileObject;
import javax.annotation.processing.Filer;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.File;
import com.google.common.collect.ImmutableList;
import org.spongepowered.tools.obfuscation.validation.TargetValidator;
import org.spongepowered.tools.obfuscation.validation.ParentValidator;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.Map;
import org.spongepowered.tools.obfuscation.interfaces.IJavadocProvider;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
import org.spongepowered.asm.util.ITokenProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

final class AnnotatedMixins implements IMixinAnnotationProcessor, ITokenProvider, ITypeHandleProvider, IJavadocProvider
{
    private static final String MAPID_SYSTEM_PROPERTY = "mixin.target.mapid";
    private static Map<ProcessingEnvironment, AnnotatedMixins> instances;
    private final CompilerEnvironment env;
    private final ProcessingEnvironment processingEnv;
    private final Map<String, AnnotatedMixin> mixins;
    private final List<AnnotatedMixin> mixinsForPass;
    private final IObfuscationManager obf;
    private final List<IMixinValidator> validators;
    private final Map<String, Integer> tokenCache;
    private final TargetMap targets;
    private Properties properties;
    
    private AnnotatedMixins(final ProcessingEnvironment a1) {
        this.mixins = new HashMap<String, AnnotatedMixin>();
        this.mixinsForPass = new ArrayList<AnnotatedMixin>();
        this.tokenCache = new HashMap<String, Integer>();
        this.env = this.detectEnvironment(a1);
        this.processingEnv = a1;
        this.printMessage(Diagnostic.Kind.NOTE, "SpongePowered MIXIN Annotation Processor Version=0.7.11");
        this.targets = this.initTargetMap();
        (this.obf = new ObfuscationManager(this)).init();
        this.validators = (List<IMixinValidator>)ImmutableList.<MixinValidator>of(new ParentValidator(this), new TargetValidator(this));
        this.initTokenCache(this.getOption("tokens"));
    }
    
    protected TargetMap initTargetMap() {
        final TargetMap create = /*EL:159*/TargetMap.create(System.getProperty("mixin.target.mapid"));
        /*SL:160*/System.setProperty("mixin.target.mapid", create.getSessionId());
        final String v0 = /*EL:161*/this.getOption("dependencyTargetsFile");
        /*SL:162*/if (v0 != null) {
            try {
                /*SL:164*/create.readImports(new File(v0));
            }
            catch (IOException v) {
                /*SL:166*/this.printMessage(Diagnostic.Kind.WARNING, "Could not read from specified imports file: " + v0);
            }
        }
        /*SL:169*/return create;
    }
    
    private void initTokenCache(final String v-5) {
        /*SL:173*/if (v-5 != null) {
            final Pattern compile = /*EL:174*/Pattern.compile("^([A-Z0-9\\-_\\.]+)=([0-9]+)$");
            final String[] split;
            final String[] array = /*EL:177*/split = v-5.replaceAll("\\s", "").toUpperCase().split("[;,]");
            for (final String v1 : split) {
                final Matcher a1 = /*EL:178*/compile.matcher(v1);
                /*SL:179*/if (a1.matches()) {
                    /*SL:180*/this.tokenCache.put(a1.group(1), Integer.parseInt(a1.group(2)));
                }
            }
        }
    }
    
    @Override
    public ITypeHandleProvider getTypeProvider() {
        /*SL:188*/return this;
    }
    
    @Override
    public ITokenProvider getTokenProvider() {
        /*SL:193*/return this;
    }
    
    @Override
    public IObfuscationManager getObfuscationManager() {
        /*SL:198*/return this.obf;
    }
    
    @Override
    public IJavadocProvider getJavadocProvider() {
        /*SL:203*/return this;
    }
    
    @Override
    public ProcessingEnvironment getProcessingEnvironment() {
        /*SL:208*/return this.processingEnv;
    }
    
    @Override
    public CompilerEnvironment getCompilerEnvironment() {
        /*SL:213*/return this.env;
    }
    
    @Override
    public Integer getToken(final String a1) {
        /*SL:218*/if (this.tokenCache.containsKey(a1)) {
            /*SL:219*/return this.tokenCache.get(a1);
        }
        final String v1 = /*EL:222*/this.getOption(a1);
        Integer v2 = /*EL:223*/null;
        try {
            /*SL:225*/v2 = Integer.parseInt(v1);
        }
        catch (Exception ex) {}
        /*SL:230*/this.tokenCache.put(a1, v2);
        /*SL:231*/return v2;
    }
    
    @Override
    public String getOption(final String a1) {
        /*SL:236*/if (a1 == null) {
            /*SL:237*/return null;
        }
        final String v1 = /*EL:240*/this.processingEnv.getOptions().get(a1);
        /*SL:241*/if (v1 != null) {
            /*SL:242*/return v1;
        }
        /*SL:245*/return this.getProperties().getProperty(a1);
    }
    
    public Properties getProperties() {
        /*SL:249*/if (this.properties == null) {
            /*SL:250*/this.properties = new Properties();
            try {
                final Filer filer = /*EL:253*/this.processingEnv.getFiler();
                final FileObject v0 = /*EL:254*/filer.getResource(StandardLocation.SOURCE_PATH, "", "mixin.properties");
                /*SL:255*/if (v0 != null) {
                    final InputStream v = /*EL:256*/v0.openInputStream();
                    /*SL:257*/this.properties.load(v);
                    /*SL:258*/v.close();
                }
            }
            catch (Exception ex) {}
        }
        /*SL:265*/return this.properties;
    }
    
    private CompilerEnvironment detectEnvironment(final ProcessingEnvironment a1) {
        /*SL:269*/if (a1.getClass().getName().contains("jdt")) {
            /*SL:270*/return CompilerEnvironment.JDT;
        }
        /*SL:273*/return CompilerEnvironment.JAVAC;
    }
    
    public void writeMappings() {
        /*SL:280*/this.obf.writeMappings();
    }
    
    public void writeReferences() {
        /*SL:287*/this.obf.writeReferences();
    }
    
    public void clear() {
        /*SL:294*/this.mixins.clear();
    }
    
    public void registerMixin(final TypeElement v2) {
        final String v3 = /*EL:301*/v2.getQualifiedName().toString();
        /*SL:303*/if (!this.mixins.containsKey(v3)) {
            final AnnotatedMixin a1 = /*EL:304*/new AnnotatedMixin(this, v2);
            /*SL:305*/this.targets.registerTargets(a1);
            /*SL:306*/a1.runValidators(IMixinValidator.ValidationPass.EARLY, this.validators);
            /*SL:307*/this.mixins.put(v3, a1);
            /*SL:308*/this.mixinsForPass.add(a1);
        }
    }
    
    public AnnotatedMixin getMixin(final TypeElement a1) {
        /*SL:316*/return this.getMixin(a1.getQualifiedName().toString());
    }
    
    public AnnotatedMixin getMixin(final String a1) {
        /*SL:323*/return this.mixins.get(a1);
    }
    
    public Collection<TypeMirror> getMixinsTargeting(final TypeMirror a1) {
        /*SL:327*/return this.getMixinsTargeting((TypeElement)((DeclaredType)a1).asElement());
    }
    
    public Collection<TypeMirror> getMixinsTargeting(final TypeElement v-2) {
        final List<TypeMirror> list = /*EL:331*/new ArrayList<TypeMirror>();
        /*SL:333*/for (final TypeReference v1 : this.targets.getMixinsTargeting(v-2)) {
            final TypeHandle a1 = /*EL:334*/v1.getHandle(this.processingEnv);
            /*SL:335*/if (a1 != null) {
                /*SL:336*/list.add(a1.getType());
            }
        }
        /*SL:340*/return list;
    }
    
    public void registerAccessor(final TypeElement a1, final ExecutableElement a2) {
        final AnnotatedMixin v1 = /*EL:350*/this.getMixin(a1);
        /*SL:351*/if (v1 == null) {
            /*SL:352*/this.printMessage(Diagnostic.Kind.ERROR, "Found @Accessor annotation on a non-mixin method", a2);
            /*SL:353*/return;
        }
        final AnnotationHandle v2 = /*EL:356*/AnnotationHandle.of(a2, Accessor.class);
        /*SL:357*/v1.registerAccessor(a2, v2, this.shouldRemap(v1, v2));
    }
    
    public void registerInvoker(final TypeElement a1, final ExecutableElement a2) {
        final AnnotatedMixin v1 = /*EL:367*/this.getMixin(a1);
        /*SL:368*/if (v1 == null) {
            /*SL:369*/this.printMessage(Diagnostic.Kind.ERROR, "Found @Accessor annotation on a non-mixin method", a2);
            /*SL:370*/return;
        }
        final AnnotationHandle v2 = /*EL:373*/AnnotationHandle.of(a2, Invoker.class);
        /*SL:374*/v1.registerInvoker(a2, v2, this.shouldRemap(v1, v2));
    }
    
    public void registerOverwrite(final TypeElement a1, final ExecutableElement a2) {
        final AnnotatedMixin v1 = /*EL:384*/this.getMixin(a1);
        /*SL:385*/if (v1 == null) {
            /*SL:386*/this.printMessage(Diagnostic.Kind.ERROR, "Found @Overwrite annotation on a non-mixin method", a2);
            /*SL:387*/return;
        }
        final AnnotationHandle v2 = /*EL:390*/AnnotationHandle.of(a2, Overwrite.class);
        /*SL:391*/v1.registerOverwrite(a2, v2, this.shouldRemap(v1, v2));
    }
    
    public void registerShadow(final TypeElement a1, final VariableElement a2, final AnnotationHandle a3) {
        final AnnotatedMixin v1 = /*EL:402*/this.getMixin(a1);
        /*SL:403*/if (v1 == null) {
            /*SL:404*/this.printMessage(Diagnostic.Kind.ERROR, "Found @Shadow annotation on a non-mixin field", a2);
            /*SL:405*/return;
        }
        /*SL:408*/v1.registerShadow(a2, a3, this.shouldRemap(v1, a3));
    }
    
    public void registerShadow(final TypeElement a1, final ExecutableElement a2, final AnnotationHandle a3) {
        final AnnotatedMixin v1 = /*EL:419*/this.getMixin(a1);
        /*SL:420*/if (v1 == null) {
            /*SL:421*/this.printMessage(Diagnostic.Kind.ERROR, "Found @Shadow annotation on a non-mixin method", a2);
            /*SL:422*/return;
        }
        /*SL:425*/v1.registerShadow(a2, a3, this.shouldRemap(v1, a3));
    }
    
    public void registerInjector(final TypeElement a1, final ExecutableElement a2, final AnnotationHandle a3) {
        final AnnotatedMixin v1 = /*EL:437*/this.getMixin(a1);
        /*SL:438*/if (v1 == null) {
            /*SL:439*/this.printMessage(Diagnostic.Kind.ERROR, "Found " + a3 + " annotation on a non-mixin method", a2);
            /*SL:440*/return;
        }
        final InjectorRemap v2 = /*EL:443*/new InjectorRemap(this.shouldRemap(v1, a3));
        /*SL:444*/v1.registerInjector(a2, a3, v2);
        /*SL:445*/v2.dispatchPendingMessages(this);
    }
    
    public void registerSoftImplements(final TypeElement a1, final AnnotationHandle a2) {
        final AnnotatedMixin v1 = /*EL:457*/this.getMixin(a1);
        /*SL:458*/if (v1 == null) {
            /*SL:459*/this.printMessage(Diagnostic.Kind.ERROR, "Found @Implements annotation on a non-mixin class");
            /*SL:460*/return;
        }
        /*SL:463*/v1.registerSoftImplements(a2);
    }
    
    public void onPassStarted() {
        /*SL:471*/this.mixinsForPass.clear();
    }
    
    public void onPassCompleted(final RoundEnvironment v2) {
        /*SL:478*/if (!"true".equalsIgnoreCase(this.getOption("disableTargetExport"))) {
            /*SL:479*/this.targets.write(true);
        }
        /*SL:482*/for (final AnnotatedMixin a1 : v2.processingOver() ? this.mixins.values() : this.mixinsForPass) {
            /*SL:483*/a1.runValidators(v2.processingOver() ? IMixinValidator.ValidationPass.FINAL : IMixinValidator.ValidationPass.LATE, this.validators);
        }
    }
    
    private boolean shouldRemap(final AnnotatedMixin a1, final AnnotationHandle a2) {
        /*SL:488*/return a2.getBoolean("remap", a1.remap());
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind a1, final CharSequence a2) {
        /*SL:496*/if (this.env == CompilerEnvironment.JAVAC || a1 != Diagnostic.Kind.NOTE) {
            /*SL:497*/this.processingEnv.getMessager().printMessage(a1, a2);
        }
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind a1, final CharSequence a2, final Element a3) {
        /*SL:506*/this.processingEnv.getMessager().printMessage(a1, a2, a3);
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind a1, final CharSequence a2, final Element a3, final AnnotationMirror a4) {
        /*SL:514*/this.processingEnv.getMessager().printMessage(a1, a2, a3, a4);
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind a1, final CharSequence a2, final Element a3, final AnnotationMirror a4, final AnnotationValue a5) {
        /*SL:522*/this.processingEnv.getMessager().printMessage(a1, a2, a3, a4, a5);
    }
    
    @Override
    public TypeHandle getTypeHandle(String v-4) {
        /*SL:531*/v-4 = v-4.replace('/', '.');
        final Elements elementUtils = /*EL:533*/this.processingEnv.getElementUtils();
        final TypeElement typeElement = /*EL:534*/elementUtils.getTypeElement(v-4);
        /*SL:535*/if (typeElement != null) {
            try {
                /*SL:537*/return new TypeHandle(typeElement);
            }
            catch (NullPointerException ex) {}
        }
        final int lastIndex = /*EL:543*/v-4.lastIndexOf(46);
        /*SL:544*/if (lastIndex > -1) {
            final String a1 = /*EL:545*/v-4.substring(0, lastIndex);
            final PackageElement v1 = /*EL:546*/elementUtils.getPackageElement(a1);
            /*SL:547*/if (v1 != null) {
                /*SL:548*/return new TypeHandle(v1, v-4);
            }
        }
        /*SL:552*/return null;
    }
    
    @Override
    public TypeHandle getSimulatedHandle(String v2, final TypeMirror v3) {
        /*SL:560*/v2 = v2.replace('/', '.');
        final int v4 = /*EL:561*/v2.lastIndexOf(46);
        /*SL:562*/if (v4 > -1) {
            final String a1 = /*EL:563*/v2.substring(0, v4);
            final PackageElement a2 = /*EL:564*/this.processingEnv.getElementUtils().getPackageElement(a1);
            /*SL:565*/if (a2 != null) {
                /*SL:566*/return new TypeHandleSimulated(a2, v2, v3);
            }
        }
        /*SL:570*/return new TypeHandleSimulated(v2, v3);
    }
    
    @Override
    public String getJavadoc(final Element a1) {
        final Elements v1 = /*EL:579*/this.processingEnv.getElementUtils();
        /*SL:580*/return v1.getDocComment(a1);
    }
    
    public static AnnotatedMixins getMixinsForEnvironment(final ProcessingEnvironment a1) {
        AnnotatedMixins v1 = AnnotatedMixins.instances.get(/*EL:587*/a1);
        /*SL:588*/if (v1 == null) {
            /*SL:589*/v1 = new AnnotatedMixins(a1);
            AnnotatedMixins.instances.put(/*EL:590*/a1, v1);
        }
        /*SL:592*/return v1;
    }
    
    static {
        AnnotatedMixins.instances = new HashMap<ProcessingEnvironment, AnnotatedMixins>();
    }
}
