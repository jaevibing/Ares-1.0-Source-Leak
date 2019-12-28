package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import org.spongepowered.asm.lib.tree.InnerClassNode;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.ClassReader;
import java.util.HashSet;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.service.MixinService;
import java.io.IOException;
import java.util.Set;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.Pseudo;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTargetAlreadyLoadedException;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.util.Collection;
import org.spongepowered.asm.lib.Type;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Collections;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.base.Functions;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import java.util.List;
import org.spongepowered.asm.util.perf.Profiler;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

class MixinInfo implements Comparable<MixinInfo>, IMixinInfo
{
    private static final IMixinService classLoaderUtil;
    static int mixinOrder;
    private final transient Logger logger;
    private final transient Profiler profiler;
    private final transient MixinConfig parent;
    private final String name;
    private final String className;
    private final int priority;
    private final boolean virtual;
    private final List<ClassInfo> targetClasses;
    private final List<String> targetClassNames;
    private final transient int order;
    private final transient IMixinService service;
    private final transient IMixinConfigPlugin plugin;
    private final transient MixinEnvironment.Phase phase;
    private final transient ClassInfo info;
    private final transient SubType type;
    private final transient boolean strict;
    private transient State pendingState;
    private transient State state;
    
    MixinInfo(final IMixinService v1, final MixinConfig v2, final String v3, final boolean v4, final IMixinConfigPlugin v5, final boolean v6) {
        this.logger = LogManager.getLogger("mixin");
        this.profiler = MixinEnvironment.getProfiler();
        this.order = MixinInfo.mixinOrder++;
        this.service = v1;
        this.parent = v2;
        this.name = v3;
        this.className = v2.getMixinPackage() + v3;
        this.plugin = v5;
        this.phase = v2.getEnvironment().getPhase();
        this.strict = v2.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_TARGETS);
        try {
            final byte[] a1 = this.loadMixinClass(this.className, v4);
            this.pendingState = new State(a1);
            this.info = this.pendingState.getClassInfo();
            this.type = SubType.getTypeFor(this);
        }
        catch (InvalidMixinException a2) {
            throw a2;
        }
        catch (Exception a3) {
            throw new InvalidMixinException(this, a3);
        }
        if (!this.type.isLoadable()) {
            MixinInfo.classLoaderUtil.registerInvalidClass(this.className);
        }
        try {
            this.priority = this.readPriority(this.pendingState.getClassNode());
            this.virtual = this.readPseudo(this.pendingState.getClassNode());
            this.targetClasses = this.readTargetClasses(this.pendingState.getClassNode(), v6);
            this.targetClassNames = Collections.<String>unmodifiableList(Lists.<ClassInfo, ? extends String>transform(this.targetClasses, (Function<? super ClassInfo, ? extends String>)Functions.toStringFunction()));
        }
        catch (InvalidMixinException a4) {
            throw a4;
        }
        catch (Exception a5) {
            throw new InvalidMixinException(this, a5);
        }
    }
    
    void validate() {
        /*SL:822*/if (this.pendingState == null) {
            /*SL:823*/throw new IllegalStateException("No pending validation state for " + this);
        }
        try {
            /*SL:827*/this.pendingState.validate(this.type, this.targetClasses);
            /*SL:828*/this.state = this.pendingState;
        }
        finally {
            /*SL:830*/this.pendingState = null;
        }
    }
    
    protected List<ClassInfo> readTargetClasses(final MixinClassNode a1, final boolean a2) {
        /*SL:842*/if (a1 == null) {
            /*SL:843*/return Collections.<ClassInfo>emptyList();
        }
        final AnnotationNode v1 = /*EL:846*/Annotations.getInvisible(a1, Mixin.class);
        /*SL:847*/if (v1 == null) {
            /*SL:848*/throw new InvalidMixinException(this, String.format("The mixin '%s' is missing an @Mixin annotation", this.className));
        }
        final List<ClassInfo> v2 = /*EL:851*/new ArrayList<ClassInfo>();
        final List<Type> v3 = /*EL:852*/Annotations.<List<Type>>getValue(v1, "value");
        final List<String> v4 = /*EL:853*/Annotations.<List<String>>getValue(v1, "targets");
        /*SL:855*/if (v3 != null) {
            /*SL:856*/this.readTargets(v2, (Collection<String>)Lists.<Type, Object>transform(v3, (Function<? super Type, ?>)new Function<Type, String>() {
                @Override
                public String apply(final Type a1) {
                    /*SL:859*/return a1.getClassName();
                }
            }), a2, false);
        }
        /*SL:864*/if (v4 != null) {
            /*SL:865*/this.readTargets(v2, (Collection<String>)Lists.<String, Object>transform(v4, (Function<? super String, ?>)new Function<String, String>() {
                @Override
                public String apply(final String a1) {
                    /*SL:868*/return MixinInfo.this.getParent().remapClassName(MixinInfo.this.getClassRef(), a1);
                }
            }), a2, true);
        }
        /*SL:873*/return v2;
    }
    
    private void readTargets(final Collection<ClassInfo> v2, final Collection<String> v3, final boolean v4, final boolean v5) {
        /*SL:880*/for (ClassInfo a4 : v3) {
            final String a2 = /*EL:881*/a4.replace('/', '.');
            /*SL:882*/if (MixinInfo.classLoaderUtil.isClassLoaded(a2) && !this.isReloading()) {
                final String a3 = /*EL:883*/String.format("Critical problem: %s target %s was already transformed.", this, a2);
                /*SL:884*/if (this.parent.isRequired()) {
                    /*SL:885*/throw new MixinTargetAlreadyLoadedException(this, a3, a2);
                }
                /*SL:887*/this.logger.error(a3);
            }
            /*SL:890*/if (this.shouldApplyMixin(v4, a2)) {
                /*SL:891*/a4 = this.getTarget(a2, v5);
                /*SL:892*/if (a4 == null || v2.contains(a4)) {
                    continue;
                }
                /*SL:893*/v2.add(a4);
                /*SL:894*/a4.addMixin(this);
            }
        }
    }
    
    private boolean shouldApplyMixin(final boolean a1, final String a2) {
        final Profiler.Section v1 = /*EL:901*/this.profiler.begin("plugin");
        final boolean v2 = /*EL:902*/this.plugin == null || a1 || this.plugin.shouldApplyMixin(a2, this.className);
        /*SL:903*/v1.end();
        /*SL:904*/return v2;
    }
    
    private ClassInfo getTarget(final String a1, final boolean a2) throws InvalidMixinException {
        final ClassInfo v1 = /*EL:908*/ClassInfo.forName(a1);
        /*SL:909*/if (v1 == null) {
            /*SL:910*/if (this.isVirtual()) {
                /*SL:911*/this.logger.debug("Skipping virtual target {} for {}", new Object[] { a1, this });
            }
            else {
                /*SL:913*/this.handleTargetError(String.format("@Mixin target %s was not found %s", a1, this));
            }
            /*SL:915*/return null;
        }
        /*SL:917*/this.type.validateTarget(a1, v1);
        /*SL:918*/if (a2 && v1.isPublic() && !this.isVirtual()) {
            /*SL:919*/this.handleTargetError(String.format("@Mixin target %s is public in %s and should be specified in value", a1, this));
        }
        /*SL:921*/return v1;
    }
    
    private void handleTargetError(final String a1) {
        /*SL:925*/if (this.strict) {
            /*SL:926*/this.logger.error(a1);
            /*SL:927*/throw new InvalidMixinException(this, a1);
        }
        /*SL:929*/this.logger.warn(a1);
    }
    
    protected int readPriority(final ClassNode a1) {
        /*SL:939*/if (a1 == null) {
            /*SL:940*/return this.parent.getDefaultMixinPriority();
        }
        final AnnotationNode v1 = /*EL:943*/Annotations.getInvisible(a1, Mixin.class);
        /*SL:944*/if (v1 == null) {
            /*SL:945*/throw new InvalidMixinException(this, String.format("The mixin '%s' is missing an @Mixin annotation", this.className));
        }
        final Integer v2 = /*EL:948*/Annotations.<Integer>getValue(v1, "priority");
        /*SL:949*/return (v2 == null) ? this.parent.getDefaultMixinPriority() : v2;
    }
    
    protected boolean readPseudo(final ClassNode a1) {
        /*SL:953*/return Annotations.getInvisible(a1, Pseudo.class) != null;
    }
    
    private boolean isReloading() {
        /*SL:957*/return this.pendingState instanceof Reloaded;
    }
    
    private State getState() {
        /*SL:965*/return (this.state != null) ? this.state : this.pendingState;
    }
    
    ClassInfo getClassInfo() {
        /*SL:972*/return this.info;
    }
    
    @Override
    public IMixinConfig getConfig() {
        /*SL:980*/return this.parent;
    }
    
    MixinConfig getParent() {
        /*SL:987*/return this.parent;
    }
    
    @Override
    public int getPriority() {
        /*SL:995*/return this.priority;
    }
    
    @Override
    public String getName() {
        /*SL:1003*/return this.name;
    }
    
    @Override
    public String getClassName() {
        /*SL:1011*/return this.className;
    }
    
    @Override
    public String getClassRef() {
        /*SL:1019*/return this.getClassInfo().getName();
    }
    
    @Override
    public byte[] getClassBytes() {
        /*SL:1027*/return this.getState().getClassBytes();
    }
    
    @Override
    public boolean isDetachedSuper() {
        /*SL:1036*/return this.getState().isDetachedSuper();
    }
    
    public boolean isUnique() {
        /*SL:1043*/return this.getState().isUnique();
    }
    
    public boolean isVirtual() {
        /*SL:1050*/return this.virtual;
    }
    
    public boolean isAccessor() {
        /*SL:1057*/return this.type instanceof SubType.Accessor;
    }
    
    public boolean isLoadable() {
        /*SL:1064*/return this.type.isLoadable();
    }
    
    public Level getLoggingLevel() {
        /*SL:1071*/return this.parent.getLoggingLevel();
    }
    
    @Override
    public MixinEnvironment.Phase getPhase() {
        /*SL:1079*/return this.phase;
    }
    
    @Override
    public MixinClassNode getClassNode(final int a1) {
        /*SL:1087*/return this.getState().createClassNode(a1);
    }
    
    @Override
    public List<String> getTargetClasses() {
        /*SL:1095*/return this.targetClassNames;
    }
    
    List<InterfaceInfo> getSoftImplements() {
        /*SL:1102*/return Collections.<InterfaceInfo>unmodifiableList(this.getState().getSoftImplements());
    }
    
    Set<String> getSyntheticInnerClasses() {
        /*SL:1109*/return Collections.<String>unmodifiableSet((Set<? extends String>)this.getState().getSyntheticInnerClasses());
    }
    
    Set<String> getInnerClasses() {
        /*SL:1116*/return Collections.<String>unmodifiableSet((Set<? extends String>)this.getState().getInnerClasses());
    }
    
    List<ClassInfo> getTargets() {
        /*SL:1123*/return Collections.<ClassInfo>unmodifiableList((List<? extends ClassInfo>)this.targetClasses);
    }
    
    Set<String> getInterfaces() {
        /*SL:1132*/return this.getState().getInterfaces();
    }
    
    MixinTargetContext createContextFor(final TargetClassContext a1) {
        final MixinClassNode v1 = /*EL:1142*/this.getClassNode(8);
        final Profiler.Section v2 = /*EL:1143*/this.profiler.begin("pre");
        final MixinTargetContext v3 = /*EL:1144*/this.type.createPreProcessor(v1).prepare().createContextFor(a1);
        /*SL:1145*/v2.end();
        /*SL:1146*/return v3;
    }
    
    private byte[] loadMixinClass(final String v-2, final boolean v-1) throws ClassNotFoundException {
        byte[] v0 = /*EL:1158*/null;
        try {
            /*SL:1161*/if (v-1) {
                final String a1 = /*EL:1162*/this.service.getClassRestrictions(v-2);
                /*SL:1163*/if (a1.length() > 0) {
                    /*SL:1164*/this.logger.error("Classloader restrictions [{}] encountered loading {}, name: {}", new Object[] { a1, this, v-2 });
                }
            }
            /*SL:1167*/v0 = this.service.getBytecodeProvider().getClassBytes(v-2, v-1);
        }
        catch (ClassNotFoundException a2) {
            /*SL:1170*/throw new ClassNotFoundException(String.format("The specified mixin '%s' was not found", v-2));
        }
        catch (IOException v) {
            /*SL:1172*/this.logger.warn("Failed to load mixin {}, the specified mixin will not be applied", new Object[] { v-2 });
            /*SL:1173*/throw new InvalidMixinException(this, "An error was encountered whilst loading the mixin class", v);
        }
        /*SL:1176*/return v0;
    }
    
    void reloadMixin(final byte[] a1) {
        /*SL:1185*/if (this.pendingState != null) {
            /*SL:1186*/throw new IllegalStateException("Cannot reload mixin while it is initialising");
        }
        /*SL:1188*/this.pendingState = new Reloaded(this.state, a1);
        /*SL:1189*/this.validate();
    }
    
    @Override
    public int compareTo(final MixinInfo a1) {
        /*SL:1197*/if (a1 == null) {
            /*SL:1198*/return 0;
        }
        /*SL:1200*/if (a1.priority == this.priority) {
            /*SL:1201*/return this.order - a1.order;
        }
        /*SL:1203*/return this.priority - a1.priority;
    }
    
    public void preApply(final String v1, final ClassNode v2) {
        /*SL:1210*/if (this.plugin != null) {
            final Profiler.Section a1 = /*EL:1211*/this.profiler.begin("plugin");
            /*SL:1212*/this.plugin.preApply(v1, v2, this.className, this);
            /*SL:1213*/a1.end();
        }
    }
    
    public void postApply(final String v1, final ClassNode v2) {
        /*SL:1221*/if (this.plugin != null) {
            final Profiler.Section a1 = /*EL:1222*/this.profiler.begin("plugin");
            /*SL:1223*/this.plugin.postApply(v1, v2, this.className, this);
            /*SL:1224*/a1.end();
        }
        /*SL:1227*/this.parent.postApply(v1, v2);
    }
    
    @Override
    public String toString() {
        /*SL:1235*/return String.format("%s:%s", this.parent.getName(), this.name);
    }
    
    static {
        classLoaderUtil = MixinService.getService();
        MixinInfo.mixinOrder = 0;
    }
    
    class MixinMethodNode extends MethodNode
    {
        private final String originalName;
        
        public MixinMethodNode(final int a2, final String a3, final String a4, final String a5, final String[] a6) {
            super(327680, a2, a3, a4, a5, a6);
            this.originalName = a3;
        }
        
        @Override
        public String toString() {
            /*SL:96*/return String.format("%s%s", this.originalName, this.desc);
        }
        
        public String getOriginalName() {
            /*SL:100*/return this.originalName;
        }
        
        public boolean isInjector() {
            /*SL:104*/return this.getInjectorAnnotation() != null || this.isSurrogate();
        }
        
        public boolean isSurrogate() {
            /*SL:108*/return this.getVisibleAnnotation(Surrogate.class) != null;
        }
        
        public boolean isSynthetic() {
            /*SL:112*/return Bytecode.hasFlag(this, 4096);
        }
        
        public AnnotationNode getVisibleAnnotation(final Class<? extends Annotation> a1) {
            /*SL:116*/return Annotations.getVisible(this, a1);
        }
        
        public AnnotationNode getInjectorAnnotation() {
            /*SL:120*/return InjectionInfo.getInjectorAnnotation(MixinInfo.this, this);
        }
        
        public IMixinInfo getOwner() {
            /*SL:124*/return MixinInfo.this;
        }
    }
    
    class MixinClassNode extends ClassNode
    {
        public final List<MixinMethodNode> mixinMethods;
        
        public MixinClassNode(final MixinInfo a1, final MixinInfo a2) {
            this(a1, 327680);
        }
        
        public MixinClassNode(final int a2) {
            super(a2);
            this.mixinMethods = (List<MixinMethodNode>)this.methods;
        }
        
        public MixinInfo getMixin() {
            /*SL:147*/return MixinInfo.this;
        }
        
        @Override
        public MethodVisitor visitMethod(final int a1, final String a2, final String a3, final String a4, final String[] a5) {
            final MethodNode v1 = /*EL:152*/new MixinMethodNode(a1, a2, a3, a4, a5);
            /*SL:153*/this.methods.add(v1);
            /*SL:154*/return v1;
        }
    }
    
    class State
    {
        private byte[] mixinBytes;
        private final ClassInfo classInfo;
        private boolean detachedSuper;
        private boolean unique;
        protected final Set<String> interfaces;
        protected final List<InterfaceInfo> softImplements;
        protected final Set<String> syntheticInnerClasses;
        protected final Set<String> innerClasses;
        protected MixinClassNode classNode;
        
        State(final MixinInfo a1, final byte[] a2) {
            this(a1, a2, null);
        }
        
        State(final byte[] a2, final ClassInfo a3) {
            this.interfaces = new HashSet<String>();
            this.softImplements = new ArrayList<InterfaceInfo>();
            this.syntheticInnerClasses = new HashSet<String>();
            this.innerClasses = new HashSet<String>();
            this.mixinBytes = a2;
            this.connect();
            this.classInfo = ((a3 != null) ? a3 : ClassInfo.fromClassNode(this.getClassNode()));
        }
        
        private void connect() {
            /*SL:223*/this.classNode = this.createClassNode(0);
        }
        
        private void complete() {
            /*SL:227*/this.classNode = null;
        }
        
        ClassInfo getClassInfo() {
            /*SL:231*/return this.classInfo;
        }
        
        byte[] getClassBytes() {
            /*SL:235*/return this.mixinBytes;
        }
        
        MixinClassNode getClassNode() {
            /*SL:239*/return this.classNode;
        }
        
        boolean isDetachedSuper() {
            /*SL:243*/return this.detachedSuper;
        }
        
        boolean isUnique() {
            /*SL:247*/return this.unique;
        }
        
        List<? extends InterfaceInfo> getSoftImplements() {
            /*SL:251*/return this.softImplements;
        }
        
        Set<String> getSyntheticInnerClasses() {
            /*SL:255*/return this.syntheticInnerClasses;
        }
        
        Set<String> getInnerClasses() {
            /*SL:259*/return this.innerClasses;
        }
        
        Set<String> getInterfaces() {
            /*SL:263*/return this.interfaces;
        }
        
        MixinClassNode createClassNode(final int a1) {
            final MixinClassNode v1 = /*EL:273*/new MixinClassNode(MixinInfo.this);
            final ClassReader v2 = /*EL:274*/new ClassReader(this.mixinBytes);
            /*SL:275*/v2.accept(v1, a1);
            /*SL:276*/return v1;
        }
        
        void validate(final SubType v1, final List<ClassInfo> v2) {
            final MixinPreProcessorStandard v3 = /*EL:286*/v1.createPreProcessor(this.getClassNode()).prepare();
            /*SL:287*/for (final ClassInfo a1 : v2) {
                /*SL:288*/v3.conform(a1);
            }
            /*SL:291*/v1.validate(this, v2);
            /*SL:293*/this.detachedSuper = v1.isDetachedSuper();
            /*SL:294*/this.unique = (Annotations.getVisible(this.getClassNode(), Unique.class) != null);
            /*SL:297*/this.validateInner();
            /*SL:298*/this.validateClassVersion();
            /*SL:299*/this.validateRemappables(v2);
            /*SL:302*/this.readImplementations(v1);
            /*SL:303*/this.readInnerClasses();
            /*SL:306*/this.validateChanges(v1, v2);
            /*SL:309*/this.complete();
        }
        
        private void validateInner() {
            /*SL:314*/if (!this.classInfo.isProbablyStatic()) {
                /*SL:315*/throw new InvalidMixinException(MixinInfo.this, "Inner class mixin must be declared static");
            }
        }
        
        private void validateClassVersion() {
            /*SL:320*/if (this.classNode.version > MixinEnvironment.getCompatibilityLevel().classVersion()) {
                String format = /*EL:321*/".";
                /*SL:322*/for (final MixinEnvironment.CompatibilityLevel v1 : MixinEnvironment.CompatibilityLevel.values()) {
                    /*SL:323*/if (v1.classVersion() >= this.classNode.version) {
                        /*SL:324*/format = String.format(". Mixin requires compatibility level %s or above.", v1.name());
                    }
                }
                /*SL:328*/throw new InvalidMixinException(MixinInfo.this, "Unsupported mixin class version " + this.classNode.version + format);
            }
        }
        
        private void validateRemappables(final List<ClassInfo> v-2) {
            /*SL:334*/if (v-2.size() > 1) {
                /*SL:335*/for (final FieldNode a1 : this.classNode.fields) {
                    /*SL:336*/this.validateRemappable(Shadow.class, a1.name, Annotations.getVisible(a1, Shadow.class));
                }
                /*SL:339*/for (final MethodNode v0 : this.classNode.methods) {
                    /*SL:340*/this.validateRemappable(Shadow.class, v0.name, Annotations.getVisible(v0, Shadow.class));
                    final AnnotationNode v = /*EL:341*/Annotations.getVisible(v0, Overwrite.class);
                    /*SL:342*/if (v != null && ((v0.access & 0x8) == 0x0 || (v0.access & 0x1) == 0x0)) {
                        /*SL:343*/throw new InvalidMixinException(MixinInfo.this, "Found @Overwrite annotation on " + v0.name + " in " + MixinInfo.this);
                    }
                }
            }
        }
        
        private void validateRemappable(final Class<Shadow> a1, final String a2, final AnnotationNode a3) {
            /*SL:350*/if (a3 != null && Annotations.<Boolean>getValue(a3, "remap", Boolean.TRUE)) {
                /*SL:351*/throw new InvalidMixinException(MixinInfo.this, "Found a remappable @" + a1.getSimpleName() + " annotation on " + a2 + " in " + this);
            }
        }
        
        void readImplementations(final SubType v-3) {
            /*SL:360*/this.interfaces.addAll(this.classNode.interfaces);
            /*SL:361*/this.interfaces.addAll(v-3.getInterfaces());
            final AnnotationNode invisible = /*EL:363*/Annotations.getInvisible(this.classNode, Implements.class);
            /*SL:364*/if (invisible == null) {
                /*SL:365*/return;
            }
            final List<AnnotationNode> list = /*EL:368*/Annotations.<List<AnnotationNode>>getValue(invisible);
            /*SL:369*/if (list == null) {
                /*SL:370*/return;
            }
            /*SL:373*/for (final AnnotationNode v1 : list) {
                final InterfaceInfo a1 = /*EL:374*/InterfaceInfo.fromAnnotation(MixinInfo.this, v1);
                /*SL:375*/this.softImplements.add(a1);
                /*SL:376*/this.interfaces.add(a1.getInternalName());
                /*SL:378*/if (!(this instanceof Reloaded)) {
                    /*SL:379*/this.classInfo.addInterface(a1.getInternalName());
                }
            }
        }
        
        void readInnerClasses() {
            /*SL:390*/for (final InnerClassNode v0 : this.classNode.innerClasses) {
                final ClassInfo v = /*EL:391*/ClassInfo.forName(v0.name);
                /*SL:392*/if ((v0.outerName != null && v0.outerName.equals(this.classInfo.getName())) || v0.name.startsWith(this.classNode.name + "$")) {
                    /*SL:394*/if (v.isProbablyStatic() && v.isSynthetic()) {
                        /*SL:395*/this.syntheticInnerClasses.add(v0.name);
                    }
                    else {
                        /*SL:397*/this.innerClasses.add(v0.name);
                    }
                }
            }
        }
        
        protected void validateChanges(final SubType a1, final List<ClassInfo> a2) {
            /*SL:404*/a1.createPreProcessor(this.classNode).prepare();
        }
    }
    
    class Reloaded extends State
    {
        private final State previous;
        
        Reloaded(final State a2, final byte[] a3) {
            /*SL:405*/super(a3, a2.getClassInfo());
            this.previous = a2;
        }
        
        @Override
        protected void validateChanges(final SubType a1, final List<ClassInfo> a2) {
            /*SL:429*/if (!this.syntheticInnerClasses.equals(this.previous.syntheticInnerClasses)) {
                /*SL:430*/throw new MixinReloadException(MixinInfo.this, "Cannot change inner classes");
            }
            /*SL:432*/if (!this.interfaces.equals(this.previous.interfaces)) {
                /*SL:433*/throw new MixinReloadException(MixinInfo.this, "Cannot change interfaces");
            }
            /*SL:435*/if (!new HashSet(this.softImplements).equals(new HashSet(this.previous.softImplements))) {
                /*SL:436*/throw new MixinReloadException(MixinInfo.this, "Cannot change soft interfaces");
            }
            final List<ClassInfo> v1 = /*EL:438*/MixinInfo.this.readTargetClasses(this.classNode, true);
            /*SL:439*/if (!new HashSet(v1).equals(new HashSet(a2))) {
                /*SL:440*/throw new MixinReloadException(MixinInfo.this, "Cannot change target classes");
            }
            final int v2 = /*EL:442*/MixinInfo.this.readPriority(this.classNode);
            /*SL:443*/if (v2 != MixinInfo.this.getPriority()) {
                /*SL:444*/throw new MixinReloadException(MixinInfo.this, "Cannot change mixin priority");
            }
        }
    }
    
    abstract static class SubType
    {
        protected final MixinInfo mixin;
        protected final String annotationType;
        protected final boolean targetMustBeInterface;
        protected boolean detached;
        
        SubType(final MixinInfo a1, final String a2, final boolean a3) {
            this.mixin = a1;
            this.annotationType = a2;
            this.targetMustBeInterface = a3;
        }
        
        Collection<String> getInterfaces() {
            /*SL:481*/return (Collection<String>)Collections.<Object>emptyList();
        }
        
        boolean isDetachedSuper() {
            /*SL:491*/return this.detached;
        }
        
        boolean isLoadable() {
            /*SL:501*/return false;
        }
        
        void validateTarget(final String v1, final ClassInfo v2) {
            final boolean v3 = /*EL:511*/v2.isInterface();
            /*SL:512*/if (v3 != this.targetMustBeInterface) {
                final String a1 = /*EL:513*/v3 ? "" : "not ";
                /*SL:514*/throw new InvalidMixinException(this.mixin, this.annotationType + " target type mismatch: " + v1 + " is " + a1 + "an interface in " + this);
            }
        }
        
        abstract void validate(final State p0, final List<ClassInfo> p1);
        
        abstract MixinPreProcessorStandard createPreProcessor(final MixinClassNode p0);
        
        static SubType getTypeFor(final MixinInfo v1) {
            /*SL:645*/if (!v1.getClassInfo().isInterface()) {
                /*SL:646*/return new Standard(v1);
            }
            boolean v2 = /*EL:649*/false;
            /*SL:650*/for (final ClassInfo.Method a1 : v1.getClassInfo().getMethods()) {
                /*SL:651*/v2 |= !a1.isAccessor();
            }
            /*SL:654*/if (v2) {
                /*SL:656*/return new Interface(v1);
            }
            /*SL:660*/return new Accessor(v1);
        }
        
        static class Standard extends SubType
        {
            Standard(final MixinInfo a1) {
                super(a1, "@Mixin", false);
            }
            
            @Override
            void validate(final State v-3, final List<ClassInfo> v-2) {
                final ClassNode classNode = /*EL:534*/v-3.getClassNode();
                /*SL:536*/for (final ClassInfo v1 : v-2) {
                    /*SL:537*/if (classNode.superName.equals(v1.getSuperName())) {
                        /*SL:538*/continue;
                    }
                    /*SL:541*/if (!v1.hasSuperClass(classNode.superName, ClassInfo.Traversal.SUPER)) {
                        ClassInfo a2 = /*EL:542*/ClassInfo.forName(classNode.superName);
                        /*SL:543*/if (a2.isMixin()) {
                            final Iterator<ClassInfo> iterator2 = /*EL:545*/a2.getTargets().iterator();
                            while (iterator2.hasNext()) {
                                a2 = iterator2.next();
                                /*SL:546*/if (v-2.contains(a2)) {
                                    /*SL:547*/throw new InvalidMixinException(this.mixin, "Illegal hierarchy detected. Derived mixin " + this + " targets the same class " + a2.getClassName() + /*EL:548*/" as its superclass " + a2.getClassName());
                                }
                            }
                        }
                        /*SL:554*/throw new InvalidMixinException(this.mixin, "Super class '" + classNode.superName.replace('/', '.') + "' of " + this.mixin.getName() + /*EL:555*/" was not found in the hierarchy of target class '" + v1 + "'");
                    }
                    /*SL:558*/this.detached = true;
                }
            }
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode a1) {
                /*SL:564*/return new MixinPreProcessorStandard(this.mixin, a1);
            }
        }
        
        static class Interface extends SubType
        {
            Interface(final MixinInfo a1) {
                super(a1, "@Mixin", true);
            }
            
            @Override
            void validate(final State a1, final List<ClassInfo> a2) {
                /*SL:579*/if (!MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) {
                    /*SL:580*/throw new InvalidMixinException(this.mixin, "Interface mixin not supported in current enviromnment");
                }
                final ClassNode v1 = /*EL:583*/a1.getClassNode();
                /*SL:585*/if (!"java/lang/Object".equals(v1.superName)) {
                    /*SL:586*/throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + v1.superName.replace('/', '.'));
                }
            }
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode a1) {
                /*SL:593*/return new MixinPreProcessorInterface(this.mixin, a1);
            }
        }
        
        static class Accessor extends SubType
        {
            private final Collection<String> interfaces;
            
            Accessor(final MixinInfo a1) {
                super(a1, "@Mixin", false);
                (this.interfaces = new ArrayList<String>()).add(a1.getClassRef());
            }
            
            @Override
            boolean isLoadable() {
                /*SL:612*/return true;
            }
            
            @Override
            Collection<String> getInterfaces() {
                /*SL:617*/return this.interfaces;
            }
            
            @Override
            void validateTarget(final String a1, final ClassInfo a2) {
                final boolean v1 = /*EL:622*/a2.isInterface();
                /*SL:623*/if (v1 && !MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) {
                    /*SL:624*/throw new InvalidMixinException(this.mixin, "Accessor mixin targetting an interface is not supported in current enviromnment");
                }
            }
            
            @Override
            void validate(final State a1, final List<ClassInfo> a2) {
                final ClassNode v1 = /*EL:630*/a1.getClassNode();
                /*SL:632*/if (!"java/lang/Object".equals(v1.superName)) {
                    /*SL:633*/throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + v1.superName.replace('/', '.'));
                }
            }
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode a1) {
                /*SL:640*/return new MixinPreProcessorAccessor(this.mixin, a1);
            }
        }
    }
}
