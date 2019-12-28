package org.spongepowered.asm.mixin;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.util.JavaVersion;
import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Sets;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import org.spongepowered.asm.service.ITransformer;
import java.util.Iterator;
import java.util.Collections;
import org.spongepowered.asm.mixin.extensibility.IEnvironmentTokenProvider;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.service.MixinService;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import org.spongepowered.asm.obfuscation.RemapperChain;
import java.util.Map;
import java.util.List;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.util.perf.Profiler;
import org.apache.logging.log4j.Logger;
import java.util.Set;
import org.spongepowered.asm.util.ITokenProvider;

public final class MixinEnvironment implements ITokenProvider
{
    private static final Set<String> excludeTransformers;
    private static MixinEnvironment currentEnvironment;
    private static Phase currentPhase;
    private static CompatibilityLevel compatibility;
    private static boolean showHeader;
    private static final Logger logger;
    private static final Profiler profiler;
    private final IMixinService service;
    private final Phase phase;
    private final String configsKey;
    private final boolean[] options;
    private final Set<String> tokenProviderClasses;
    private final List<TokenProviderWrapper> tokenProviders;
    private final Map<String, Integer> internalTokens;
    private final RemapperChain remappers;
    private Side side;
    private List<ILegacyClassTransformer> transformers;
    private String obfuscationContext;
    
    MixinEnvironment(final Phase v2) {
        this.tokenProviderClasses = new HashSet<String>();
        this.tokenProviders = new ArrayList<TokenProviderWrapper>();
        this.internalTokens = new HashMap<String, Integer>();
        this.remappers = new RemapperChain();
        this.obfuscationContext = null;
        this.service = MixinService.getService();
        this.phase = v2;
        this.configsKey = "mixin.configs." + this.phase.name.toLowerCase();
        final Object v3 = this.getVersion();
        if (v3 == null || !"0.7.11".equals(v3)) {
            throw new MixinException("Environment conflict, mismatched versions or you didn't call MixinBootstrap.init()");
        }
        this.service.checkEnv(this);
        this.options = new boolean[Option.values().length];
        for (final Option a1 : Option.values()) {
            this.options[a1.ordinal()] = a1.getBooleanValue();
        }
        if (MixinEnvironment.showHeader) {
            MixinEnvironment.showHeader = false;
            this.printHeader(v3);
        }
    }
    
    private void printHeader(final Object v-9) {
        final String codeSource = /*EL:957*/this.getCodeSource();
        final String name = /*EL:958*/this.service.getName();
        final Side side = /*EL:959*/this.getSide();
        MixinEnvironment.logger.info(/*EL:960*/"SpongePowered MIXIN Subsystem Version={} Source={} Service={} Env={}", new Object[] { v-9, codeSource, name, side });
        final boolean option = /*EL:962*/this.getOption(Option.DEBUG_VERBOSE);
        /*SL:963*/if (option || this.getOption(Option.DEBUG_EXPORT) || this.getOption(Option.DEBUG_PROFILER)) {
            final PrettyPrinter prettyPrinter = /*EL:964*/new PrettyPrinter(32);
            /*SL:965*/prettyPrinter.add("SpongePowered MIXIN%s", option ? " (Verbose debugging enabled)" : "").centre().hr();
            /*SL:966*/prettyPrinter.kv("Code source", (Object)codeSource);
            /*SL:967*/prettyPrinter.kv("Internal Version", v-9);
            /*SL:968*/prettyPrinter.kv("Java 8 Supported", CompatibilityLevel.JAVA_8.isSupported()).hr();
            /*SL:969*/prettyPrinter.kv("Service Name", (Object)name);
            /*SL:970*/prettyPrinter.kv("Service Class", (Object)this.service.getClass().getName()).hr();
            /*SL:971*/for (final Option v0 : Option.values()) {
                final StringBuilder v = /*EL:972*/new StringBuilder();
                /*SL:973*/for (int a1 = 0; a1 < v0.depth; ++a1) {
                    /*SL:974*/v.append("- ");
                }
                /*SL:976*/prettyPrinter.kv(v0.property, "%s<%s>", v, v0);
            }
            /*SL:978*/prettyPrinter.hr().kv("Detected Side", side);
            /*SL:979*/prettyPrinter.print(System.err);
        }
    }
    
    private String getCodeSource() {
        try {
            /*SL:985*/return this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
        }
        catch (Throwable v1) {
            /*SL:987*/return "Unknown";
        }
    }
    
    public Phase getPhase() {
        /*SL:997*/return this.phase;
    }
    
    @Deprecated
    public List<String> getMixinConfigs() {
        List<String> v1 = /*EL:1008*/GlobalProperties.<List<String>>get(this.configsKey);
        /*SL:1009*/if (v1 == null) {
            /*SL:1010*/v1 = new ArrayList<String>();
            /*SL:1011*/GlobalProperties.put(this.configsKey, v1);
        }
        /*SL:1013*/return v1;
    }
    
    @Deprecated
    public MixinEnvironment addConfiguration(final String a1) {
        MixinEnvironment.logger.warn(/*EL:1025*/"MixinEnvironment::addConfiguration is deprecated and will be removed. Use Mixins::addConfiguration instead!");
        /*SL:1026*/Mixins.addConfiguration(a1, this);
        /*SL:1027*/return this;
    }
    
    void registerConfig(final String a1) {
        final List<String> v1 = /*EL:1031*/this.getMixinConfigs();
        /*SL:1032*/if (!v1.contains(a1)) {
            /*SL:1033*/v1.add(a1);
        }
    }
    
    @Deprecated
    public MixinEnvironment registerErrorHandlerClass(final String a1) {
        /*SL:1046*/Mixins.registerErrorHandlerClass(a1);
        /*SL:1047*/return this;
    }
    
    public MixinEnvironment registerTokenProviderClass(final String v-1) {
        /*SL:1057*/if (!this.tokenProviderClasses.contains(v-1)) {
            try {
                final Class<? extends IEnvironmentTokenProvider> a1 = /*EL:1060*/(Class<? extends IEnvironmentTokenProvider>)this.service.getClassProvider().findClass(/*EL:1061*/v-1, true);
                final IEnvironmentTokenProvider v1 = /*EL:1062*/(IEnvironmentTokenProvider)a1.newInstance();
                /*SL:1063*/this.registerTokenProvider(v1);
            }
            catch (Throwable v2) {
                MixinEnvironment.logger.error(/*EL:1065*/"Error instantiating " + v-1, v2);
            }
        }
        /*SL:1068*/return this;
    }
    
    public MixinEnvironment registerTokenProvider(final IEnvironmentTokenProvider v-1) {
        /*SL:1078*/if (v-1 != null && !this.tokenProviderClasses.contains(v-1.getClass().getName())) {
            final String a1 = /*EL:1079*/v-1.getClass().getName();
            final TokenProviderWrapper v1 = /*EL:1080*/new TokenProviderWrapper(v-1, this);
            MixinEnvironment.logger.info(/*EL:1081*/"Adding new token provider {} to {}", new Object[] { a1, this });
            /*SL:1082*/this.tokenProviders.add(v1);
            /*SL:1083*/this.tokenProviderClasses.add(a1);
            /*SL:1084*/Collections.<TokenProviderWrapper>sort(this.tokenProviders);
        }
        /*SL:1087*/return this;
    }
    
    @Override
    public Integer getToken(String v-1) {
        /*SL:1099*/v-1 = v-1.toUpperCase();
        /*SL:1101*/for (final TokenProviderWrapper v1 : this.tokenProviders) {
            final Integer a1 = /*EL:1102*/v1.getToken(v-1);
            /*SL:1103*/if (a1 != null) {
                /*SL:1104*/return a1;
            }
        }
        /*SL:1108*/return this.internalTokens.get(v-1);
    }
    
    @Deprecated
    public Set<String> getErrorHandlerClasses() {
        /*SL:1119*/return Mixins.getErrorHandlerClasses();
    }
    
    public Object getActiveTransformer() {
        /*SL:1128*/return GlobalProperties.<Object>get("mixin.transformer");
    }
    
    public void setActiveTransformer(final ITransformer a1) {
        /*SL:1137*/if (a1 != null) {
            /*SL:1138*/GlobalProperties.put("mixin.transformer", a1);
        }
    }
    
    public MixinEnvironment setSide(final Side a1) {
        /*SL:1149*/if (a1 != null && this.getSide() == Side.UNKNOWN && a1 != Side.UNKNOWN) {
            /*SL:1150*/this.side = a1;
        }
        /*SL:1152*/return this;
    }
    
    public Side getSide() {
        /*SL:1161*/if (this.side == null) {
            /*SL:1162*/for (final Side v1 : Side.values()) {
                /*SL:1163*/if (v1.detect()) {
                    /*SL:1164*/this.side = v1;
                    /*SL:1165*/break;
                }
            }
        }
        /*SL:1170*/return (this.side != null) ? this.side : Side.UNKNOWN;
    }
    
    public String getVersion() {
        /*SL:1179*/return GlobalProperties.<String>get("mixin.initialised");
    }
    
    public boolean getOption(final Option a1) {
        /*SL:1189*/return this.options[a1.ordinal()];
    }
    
    public void setOption(final Option a1, final boolean a2) {
        /*SL:1199*/this.options[a1.ordinal()] = a2;
    }
    
    public String getOptionValue(final Option a1) {
        /*SL:1209*/return a1.getStringValue();
    }
    
    public <E extends Enum<E>> E getOption(final Option a1, final E a2) {
        /*SL:1221*/return a1.<E>getEnumValue(a2);
    }
    
    public void setObfuscationContext(final String a1) {
        /*SL:1230*/this.obfuscationContext = a1;
    }
    
    public String getObfuscationContext() {
        /*SL:1237*/return this.obfuscationContext;
    }
    
    public String getRefmapObfuscationContext() {
        final String v1 = Option.OBFUSCATION_TYPE.getStringValue();
        /*SL:1245*/if (v1 != null) {
            /*SL:1246*/return v1;
        }
        /*SL:1248*/return this.obfuscationContext;
    }
    
    public RemapperChain getRemappers() {
        /*SL:1255*/return this.remappers;
    }
    
    public void audit() {
        final Object v0 = /*EL:1262*/this.getActiveTransformer();
        /*SL:1263*/if (v0 instanceof MixinTransformer) {
            final MixinTransformer v = /*EL:1264*/(MixinTransformer)v0;
            /*SL:1265*/v.audit(this);
        }
    }
    
    public List<ILegacyClassTransformer> getTransformers() {
        /*SL:1276*/if (this.transformers == null) {
            /*SL:1277*/this.buildTransformerDelegationList();
        }
        /*SL:1280*/return Collections.<ILegacyClassTransformer>unmodifiableList((List<? extends ILegacyClassTransformer>)this.transformers);
    }
    
    public void addTransformerExclusion(final String a1) {
        MixinEnvironment.excludeTransformers.add(/*EL:1289*/a1);
        /*SL:1292*/this.transformers = null;
    }
    
    private void buildTransformerDelegationList() {
        MixinEnvironment.logger.debug(/*EL:1302*/"Rebuilding transformer delegation list:");
        /*SL:1303*/this.transformers = new ArrayList<ILegacyClassTransformer>();
        /*SL:1304*/for (final ITransformer transformer : this.service.getTransformers()) {
            /*SL:1305*/if (!(transformer instanceof ILegacyClassTransformer)) {
                /*SL:1306*/continue;
            }
            final ILegacyClassTransformer legacyClassTransformer = /*EL:1309*/(ILegacyClassTransformer)transformer;
            final String name = /*EL:1310*/legacyClassTransformer.getName();
            boolean b = /*EL:1311*/true;
            /*SL:1312*/for (final String v1 : MixinEnvironment.excludeTransformers) {
                /*SL:1313*/if (name.contains(v1)) {
                    /*SL:1314*/b = false;
                    /*SL:1315*/break;
                }
            }
            /*SL:1318*/if (b && !legacyClassTransformer.isDelegationExcluded()) {
                MixinEnvironment.logger.debug(/*EL:1319*/"  Adding:    {}", new Object[] { name });
                /*SL:1320*/this.transformers.add(legacyClassTransformer);
            }
            else {
                MixinEnvironment.logger.debug(/*EL:1322*/"  Excluding: {}", new Object[] { name });
            }
        }
        MixinEnvironment.logger.debug(/*EL:1326*/"Transformer delegation list created with {} entries", new Object[] { this.transformers.size() });
    }
    
    @Override
    public String toString() {
        /*SL:1334*/return String.format("%s[%s]", this.getClass().getSimpleName(), this.phase);
    }
    
    private static Phase getCurrentPhase() {
        /*SL:1341*/if (MixinEnvironment.currentPhase == Phase.NOT_INITIALISED) {
            init(Phase.PREINIT);
        }
        /*SL:1345*/return MixinEnvironment.currentPhase;
    }
    
    public static void init(final Phase v1) {
        /*SL:1354*/if (MixinEnvironment.currentPhase == Phase.NOT_INITIALISED) {
            MixinEnvironment.currentPhase = /*EL:1355*/v1;
            final MixinEnvironment a1 = getEnvironment(/*EL:1356*/v1);
            getProfiler().setActive(/*EL:1357*/a1.getOption(Option.DEBUG_PROFILER));
            /*SL:1359*/MixinLogWatcher.begin();
        }
    }
    
    public static MixinEnvironment getEnvironment(final Phase a1) {
        /*SL:1370*/if (a1 == null) {
            /*SL:1371*/return Phase.DEFAULT.getEnvironment();
        }
        /*SL:1373*/return a1.getEnvironment();
    }
    
    public static MixinEnvironment getDefaultEnvironment() {
        /*SL:1382*/return getEnvironment(Phase.DEFAULT);
    }
    
    public static MixinEnvironment getCurrentEnvironment() {
        /*SL:1391*/if (MixinEnvironment.currentEnvironment == null) {
            MixinEnvironment.currentEnvironment = getEnvironment(getCurrentPhase());
        }
        /*SL:1395*/return MixinEnvironment.currentEnvironment;
    }
    
    public static CompatibilityLevel getCompatibilityLevel() {
        /*SL:1402*/return MixinEnvironment.compatibility;
    }
    
    @Deprecated
    public static void setCompatibilityLevel(final CompatibilityLevel a1) throws IllegalArgumentException {
        final StackTraceElement[] v1 = /*EL:1414*/Thread.currentThread().getStackTrace();
        /*SL:1415*/if (!"org.spongepowered.asm.mixin.transformer.MixinConfig".equals(v1[2].getClassName())) {
            MixinEnvironment.logger.warn(/*EL:1416*/"MixinEnvironment::setCompatibilityLevel is deprecated and will be removed. Set level via config instead!");
        }
        /*SL:1419*/if (a1 != MixinEnvironment.compatibility && a1.isAtLeast(MixinEnvironment.compatibility)) {
            /*SL:1420*/if (!a1.isSupported()) {
                /*SL:1421*/throw new IllegalArgumentException("The requested compatibility level " + a1 + " could not be set. Level is not supported");
            }
            MixinEnvironment.compatibility = /*EL:1424*/a1;
            MixinEnvironment.logger.info(/*EL:1425*/"Compatibility level set to {}", new Object[] { a1 });
        }
    }
    
    public static Profiler getProfiler() {
        /*SL:1435*/return MixinEnvironment.profiler;
    }
    
    static void gotoPhase(final Phase a1) {
        /*SL:1444*/if (a1 == null || a1.ordinal < 0) {
            /*SL:1445*/throw new IllegalArgumentException("Cannot go to the specified phase, phase is null or invalid");
        }
        /*SL:1448*/if (a1.ordinal > getCurrentPhase().ordinal) {
            /*SL:1449*/MixinService.getService().beginPhase();
        }
        /*SL:1452*/if (a1 == Phase.DEFAULT) {
            /*SL:1453*/MixinLogWatcher.end();
        }
        MixinEnvironment.currentPhase = /*EL:1456*/a1;
        MixinEnvironment.currentEnvironment = getEnvironment(getCurrentPhase());
    }
    
    static {
        excludeTransformers = Sets.<String>newHashSet("net.minecraftforge.fml.common.asm.transformers.EventSubscriptionTransformer", "cpw.mods.fml.common.asm.transformers.EventSubscriptionTransformer", "net.minecraftforge.fml.common.asm.transformers.TerminalTransformer", "cpw.mods.fml.common.asm.transformers.TerminalTransformer");
        MixinEnvironment.currentPhase = Phase.NOT_INITIALISED;
        MixinEnvironment.compatibility = Option.DEFAULT_COMPATIBILITY_LEVEL.<CompatibilityLevel>getEnumValue(CompatibilityLevel.JAVA_6);
        MixinEnvironment.showHeader = true;
        logger = LogManager.getLogger("mixin");
        profiler = new Profiler();
    }
    
    public static final class Phase
    {
        static final Phase NOT_INITIALISED;
        public static final Phase PREINIT;
        public static final Phase INIT;
        public static final Phase DEFAULT;
        static final List<Phase> phases;
        final int ordinal;
        final String name;
        private MixinEnvironment environment;
        
        private Phase(final int a1, final String a2) {
            this.ordinal = a1;
            this.name = a2;
        }
        
        @Override
        public String toString() {
            /*SL:124*/return this.name;
        }
        
        public static Phase forName(final String v1) {
            /*SL:135*/for (final Phase a1 : Phase.phases) {
                /*SL:136*/if (a1.name.equals(v1)) {
                    /*SL:137*/return a1;
                }
            }
            /*SL:140*/return null;
        }
        
        MixinEnvironment getEnvironment() {
            /*SL:144*/if (this.ordinal < 0) {
                /*SL:145*/throw new IllegalArgumentException("Cannot access the NOT_INITIALISED environment");
            }
            /*SL:148*/if (this.environment == null) {
                /*SL:149*/this.environment = new MixinEnvironment(this);
            }
            /*SL:152*/return this.environment;
        }
        
        static {
            NOT_INITIALISED = new Phase(-1, "NOT_INITIALISED");
            PREINIT = new Phase(0, "PREINIT");
            INIT = new Phase(1, "INIT");
            DEFAULT = new Phase(2, "DEFAULT");
            phases = ImmutableList.<Phase>of(Phase.PREINIT, Phase.INIT, Phase.DEFAULT);
        }
    }
    
    public enum Side
    {
        UNKNOWN {
            @Override
            protected boolean detect() {
                /*SL:167*/return false;
            }
        }, 
        CLIENT {
            @Override
            protected boolean detect() {
                final String v1 = /*EL:177*/MixinService.getService().getSideName();
                /*SL:178*/return "CLIENT".equals(v1);
            }
        }, 
        SERVER {
            @Override
            protected boolean detect() {
                final String v1 = /*EL:188*/MixinService.getService().getSideName();
                /*SL:189*/return "SERVER".equals(v1) || "DEDICATEDSERVER".equals(v1);
            }
        };
        
        protected abstract boolean detect();
    }
    
    public enum Option
    {
        DEBUG_ALL("debug"), 
        DEBUG_EXPORT(Option.DEBUG_ALL, "export"), 
        DEBUG_EXPORT_FILTER(Option.DEBUG_EXPORT, "filter", false), 
        DEBUG_EXPORT_DECOMPILE(Option.DEBUG_EXPORT, Inherit.ALLOW_OVERRIDE, "decompile"), 
        DEBUG_EXPORT_DECOMPILE_THREADED(Option.DEBUG_EXPORT_DECOMPILE, Inherit.ALLOW_OVERRIDE, "async"), 
        DEBUG_EXPORT_DECOMPILE_MERGESIGNATURES(Option.DEBUG_EXPORT_DECOMPILE, Inherit.ALLOW_OVERRIDE, "mergeGenericSignatures"), 
        DEBUG_VERIFY(Option.DEBUG_ALL, "verify"), 
        DEBUG_VERBOSE(Option.DEBUG_ALL, "verbose"), 
        DEBUG_INJECTORS(Option.DEBUG_ALL, "countInjections"), 
        DEBUG_STRICT(Option.DEBUG_ALL, Inherit.INDEPENDENT, "strict"), 
        DEBUG_UNIQUE(Option.DEBUG_STRICT, "unique"), 
        DEBUG_TARGETS(Option.DEBUG_STRICT, "targets"), 
        DEBUG_PROFILER(Option.DEBUG_ALL, Inherit.ALLOW_OVERRIDE, "profiler"), 
        DUMP_TARGET_ON_FAILURE("dumpTargetOnFailure"), 
        CHECK_ALL("checks"), 
        CHECK_IMPLEMENTS(Option.CHECK_ALL, "interfaces"), 
        CHECK_IMPLEMENTS_STRICT(Option.CHECK_IMPLEMENTS, Inherit.ALLOW_OVERRIDE, "strict"), 
        IGNORE_CONSTRAINTS("ignoreConstraints"), 
        HOT_SWAP("hotSwap"), 
        ENVIRONMENT(Inherit.ALWAYS_FALSE, "env"), 
        OBFUSCATION_TYPE(Option.ENVIRONMENT, Inherit.ALWAYS_FALSE, "obf"), 
        DISABLE_REFMAP(Option.ENVIRONMENT, Inherit.INDEPENDENT, "disableRefMap"), 
        REFMAP_REMAP(Option.ENVIRONMENT, Inherit.INDEPENDENT, "remapRefMap"), 
        REFMAP_REMAP_RESOURCE(Option.ENVIRONMENT, Inherit.INDEPENDENT, "refMapRemappingFile", ""), 
        REFMAP_REMAP_SOURCE_ENV(Option.ENVIRONMENT, Inherit.INDEPENDENT, "refMapRemappingEnv", "searge"), 
        REFMAP_REMAP_ALLOW_PERMISSIVE(Option.ENVIRONMENT, Inherit.INDEPENDENT, "allowPermissiveMatch", true, "true"), 
        IGNORE_REQUIRED(Option.ENVIRONMENT, Inherit.INDEPENDENT, "ignoreRequired"), 
        DEFAULT_COMPATIBILITY_LEVEL(Option.ENVIRONMENT, Inherit.INDEPENDENT, "compatLevel"), 
        SHIFT_BY_VIOLATION_BEHAVIOUR(Option.ENVIRONMENT, Inherit.INDEPENDENT, "shiftByViolation", "warn"), 
        INITIALISER_INJECTION_MODE("initialiserInjectionMode", "default");
        
        private static final String PREFIX = "mixin";
        final Option parent;
        final Inherit inheritance;
        final String property;
        final String defaultValue;
        final boolean isFlag;
        final int depth;
        
        private Option(final String a1) {
            this(null, a1, true);
        }
        
        private Option(final Inherit a1, final String a2) {
            this(null, a1, a2, true);
        }
        
        private Option(final String a1, final boolean a2) {
            this(null, a1, a2);
        }
        
        private Option(final String a1, final String a2) {
            this(null, Inherit.INDEPENDENT, a1, false, a2);
        }
        
        private Option(final Option a1, final String a2) {
            this(a1, Inherit.INHERIT, a2, true);
        }
        
        private Option(final Option a1, final Inherit a2, final String a3) {
            this(a1, a2, a3, true);
        }
        
        private Option(final Option a1, final String a2, final boolean a3) {
            this(a1, Inherit.INHERIT, a2, a3, null);
        }
        
        private Option(final Option a1, final Inherit a2, final String a3, final boolean a4) {
            this(a1, a2, a3, a4, null);
        }
        
        private Option(final Option a1, final String a2, final String a3) {
            this(a1, Inherit.INHERIT, a2, false, a3);
        }
        
        private Option(final Option a1, final Inherit a2, final String a3, final String a4) {
            this(a1, a2, a3, false, a4);
        }
        
        private Option(Option a1, final Inherit a2, final String a3, final boolean a4, final String a5) {
            super(s, n);
            this.parent = a1;
            this.inheritance = a2;
            this.property = ((a1 != null) ? a1.property : "mixin") + "." + a3;
            this.defaultValue = a5;
            this.isFlag = a4;
            int a6;
            for (a6 = 0; a1 != null; a1 = a1.parent, ++a6) {}
            this.depth = a6;
        }
        
        Option getParent() {
            /*SL:534*/return this.parent;
        }
        
        String getProperty() {
            /*SL:538*/return this.property;
        }
        
        @Override
        public String toString() {
            /*SL:543*/return this.isFlag ? String.valueOf(this.getBooleanValue()) : this.getStringValue();
        }
        
        private boolean getLocalBooleanValue(final boolean a1) {
            /*SL:547*/return Boolean.parseBoolean(System.getProperty(this.property, Boolean.toString(a1)));
        }
        
        private boolean getInheritedBooleanValue() {
            /*SL:551*/return this.parent != null && this.parent.getBooleanValue();
        }
        
        final boolean getBooleanValue() {
            /*SL:555*/if (this.inheritance == Inherit.ALWAYS_FALSE) {
                /*SL:556*/return false;
            }
            final boolean v1 = /*EL:559*/this.getLocalBooleanValue(false);
            /*SL:560*/if (this.inheritance == Inherit.INDEPENDENT) {
                /*SL:561*/return v1;
            }
            final boolean v2 = /*EL:564*/v1 || this.getInheritedBooleanValue();
            /*SL:565*/return (this.inheritance == Inherit.INHERIT) ? v2 : this.getLocalBooleanValue(v2);
        }
        
        final String getStringValue() {
            /*SL:569*/return (this.inheritance == Inherit.INDEPENDENT || this.parent == null || this.parent.getBooleanValue()) ? /*EL:570*/System.getProperty(this.property, this.defaultValue) : this.defaultValue;
        }
        
         <E extends Enum<E>> E getEnumValue(final E v2) {
            final String v3 = /*EL:575*/System.getProperty(this.property, v2.name());
            try {
                /*SL:577*/return Enum.<E>valueOf(v2.getClass(), v3.toUpperCase());
            }
            catch (IllegalArgumentException a1) {
                /*SL:579*/return v2;
            }
        }
        
        private enum Inherit
        {
            INHERIT, 
            ALLOW_OVERRIDE, 
            INDEPENDENT, 
            ALWAYS_FALSE;
        }
    }
    
    public enum CompatibilityLevel
    {
        JAVA_6(6, 50, false), 
        JAVA_7(7, 51, false) {
            @Override
            boolean isSupported() {
                /*SL:601*/return JavaVersion.current() >= 1.7;
            }
        }, 
        JAVA_8(8, 52, true) {
            @Override
            boolean isSupported() {
                /*SL:613*/return JavaVersion.current() >= 1.8;
            }
        }, 
        JAVA_9(9, 53, true) {
            @Override
            boolean isSupported() {
                /*SL:625*/return false;
            }
        };
        
        private static final int CLASS_V1_9 = 53;
        private final int ver;
        private final int classVersion;
        private final boolean supportsMethodsInInterfaces;
        private CompatibilityLevel maxCompatibleLevel;
        
        private CompatibilityLevel(final int a1, final int a2, final boolean a3) {
            this.ver = a1;
            this.classVersion = a2;
            this.supportsMethodsInInterfaces = a3;
        }
        
        private void setMaxCompatibleLevel(final CompatibilityLevel a1) {
            /*SL:649*/this.maxCompatibleLevel = a1;
        }
        
        boolean isSupported() {
            /*SL:657*/return true;
        }
        
        public int classVersion() {
            /*SL:664*/return this.classVersion;
        }
        
        public boolean supportsMethodsInInterfaces() {
            /*SL:672*/return this.supportsMethodsInInterfaces;
        }
        
        public boolean isAtLeast(final CompatibilityLevel a1) {
            /*SL:683*/return a1 == null || this.ver >= a1.ver;
        }
        
        public boolean canElevateTo(final CompatibilityLevel a1) {
            /*SL:693*/return a1 == null || this.maxCompatibleLevel == null || /*EL:696*/a1.ver <= this.maxCompatibleLevel.ver;
        }
        
        public boolean canSupport(final CompatibilityLevel a1) {
            /*SL:706*/return a1 == null || /*EL:710*/a1.canElevateTo(this);
        }
    }
    
    static class TokenProviderWrapper implements Comparable<TokenProviderWrapper>
    {
        private static int nextOrder;
        private final int priority;
        private final int order;
        private final IEnvironmentTokenProvider provider;
        private final MixinEnvironment environment;
        
        public TokenProviderWrapper(final IEnvironmentTokenProvider a1, final MixinEnvironment a2) {
            this.provider = a1;
            this.environment = a2;
            this.order = TokenProviderWrapper.nextOrder++;
            this.priority = a1.getPriority();
        }
        
        @Override
        public int compareTo(final TokenProviderWrapper a1) {
            /*SL:737*/if (a1 == null) {
                /*SL:738*/return 0;
            }
            /*SL:740*/if (a1.priority == this.priority) {
                /*SL:741*/return a1.order - this.order;
            }
            /*SL:743*/return a1.priority - this.priority;
        }
        
        public IEnvironmentTokenProvider getProvider() {
            /*SL:747*/return this.provider;
        }
        
        Integer getToken(final String a1) {
            /*SL:751*/return this.provider.getToken(a1, this.environment);
        }
        
        static {
            TokenProviderWrapper.nextOrder = 0;
        }
    }
    
    static class MixinLogWatcher
    {
        static MixinAppender appender;
        static org.apache.logging.log4j.core.Logger log;
        static Level oldLevel;
        
        static void begin() {
            final Logger v1 = /*EL:779*/LogManager.getLogger("FML");
            /*SL:780*/if (!(v1 instanceof org.apache.logging.log4j.core.Logger)) {
                /*SL:781*/return;
            }
            MixinLogWatcher.log = /*EL:784*/(org.apache.logging.log4j.core.Logger)v1;
            MixinLogWatcher.oldLevel = MixinLogWatcher.log.getLevel();
            MixinLogWatcher.appender.start();
            MixinLogWatcher.log.addAppender((Appender)MixinLogWatcher.appender);
            MixinLogWatcher.log.setLevel(Level.ALL);
        }
        
        static void end() {
            /*SL:794*/if (MixinLogWatcher.log != null) {
                MixinLogWatcher.log.removeAppender((Appender)MixinLogWatcher.appender);
            }
        }
        
        static {
            MixinLogWatcher.appender = new MixinAppender();
            MixinLogWatcher.oldLevel = null;
        }
        
        static class MixinAppender extends AbstractAppender
        {
            MixinAppender() {
                super("MixinLogWatcherAppender", (Filter)null, (Layout)null);
            }
            
            public void append(final LogEvent a1) {
                /*SL:811*/if (a1.getLevel() != Level.DEBUG || !"Validating minecraft".equals(a1.getMessage().getFormattedMessage())) {
                    /*SL:812*/return;
                }
                /*SL:816*/MixinEnvironment.gotoPhase(Phase.INIT);
                /*SL:823*/if (MixinLogWatcher.log.getLevel() == Level.ALL) {
                    MixinLogWatcher.log.setLevel(MixinLogWatcher.oldLevel);
                }
            }
        }
    }
}
