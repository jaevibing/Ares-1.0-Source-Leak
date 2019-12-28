package org.spongepowered.asm.mixin.transformer;

import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import org.spongepowered.asm.service.MixinService;
import org.apache.logging.log4j.Level;
import java.util.Collections;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.refmap.RemappingReferenceMapper;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import org.spongepowered.asm.util.VersionNumber;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.spongepowered.asm.launch.MixinInitialisationError;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.IMixinService;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import java.util.Set;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;

final class MixinConfig implements Comparable<MixinConfig>, IMixinConfig
{
    private static int configOrder;
    private static final Set<String> globalMixinList;
    private final Logger logger;
    private final transient Map<String, List<MixinInfo>> mixinMapping;
    private final transient Set<String> unhandledTargets;
    private final transient List<MixinInfo> mixins;
    private transient Config handle;
    @SerializedName("target")
    private String selector;
    @SerializedName("minVersion")
    private String version;
    @SerializedName("compatibilityLevel")
    private String compatibility;
    @SerializedName("required")
    private boolean required;
    @SerializedName("priority")
    private int priority;
    @SerializedName("mixinPriority")
    private int mixinPriority;
    @SerializedName("package")
    private String mixinPackage;
    @SerializedName("mixins")
    private List<String> mixinClasses;
    @SerializedName("client")
    private List<String> mixinClassesClient;
    @SerializedName("server")
    private List<String> mixinClassesServer;
    @SerializedName("setSourceFile")
    private boolean setSourceFile;
    @SerializedName("refmap")
    private String refMapperConfig;
    @SerializedName("verbose")
    private boolean verboseLogging;
    private final transient int order;
    private final transient List<IListener> listeners;
    private transient IMixinService service;
    private transient MixinEnvironment env;
    private transient String name;
    @SerializedName("plugin")
    private String pluginClassName;
    @SerializedName("injectors")
    private InjectorOptions injectorOptions;
    @SerializedName("overwrites")
    private OverwriteOptions overwriteOptions;
    private transient IMixinConfigPlugin plugin;
    private transient IReferenceMapper refMapper;
    private transient boolean prepared;
    private transient boolean visited;
    
    private MixinConfig() {
        this.logger = LogManager.getLogger("mixin");
        this.mixinMapping = new HashMap<String, List<MixinInfo>>();
        this.unhandledTargets = new HashSet<String>();
        this.mixins = new ArrayList<MixinInfo>();
        this.priority = 1000;
        this.mixinPriority = 1000;
        this.setSourceFile = false;
        this.order = MixinConfig.configOrder++;
        this.listeners = new ArrayList<IListener>();
        this.injectorOptions = new InjectorOptions();
        this.overwriteOptions = new OverwriteOptions();
        this.prepared = false;
        this.visited = false;
    }
    
    private boolean onLoad(final IMixinService a1, final String a2, final MixinEnvironment a3) {
        /*SL:325*/this.service = a1;
        /*SL:326*/this.name = a2;
        /*SL:327*/this.env = this.parseSelector(this.selector, a3);
        /*SL:328*/this.required &= !this.env.getOption(MixinEnvironment.Option.IGNORE_REQUIRED);
        /*SL:329*/this.initCompatibilityLevel();
        /*SL:330*/this.initInjectionPoints();
        /*SL:331*/return this.checkVersion();
    }
    
    private void initCompatibilityLevel() {
        /*SL:336*/if (this.compatibility == null) {
            /*SL:337*/return;
        }
        final MixinEnvironment.CompatibilityLevel v1 = /*EL:340*/MixinEnvironment.CompatibilityLevel.valueOf(this.compatibility.trim().toUpperCase());
        final MixinEnvironment.CompatibilityLevel v2 = /*EL:341*/MixinEnvironment.getCompatibilityLevel();
        /*SL:343*/if (v1 == v2) {
            /*SL:344*/return;
        }
        /*SL:348*/if (v2.isAtLeast(v1) && /*EL:349*/!v2.canSupport(v1)) {
            /*SL:350*/throw new MixinInitialisationError("Mixin config " + this.name + " requires compatibility level " + v1 + " which is too old");
        }
        /*SL:355*/if (!v2.canElevateTo(v1)) {
            /*SL:356*/throw new MixinInitialisationError("Mixin config " + this.name + " requires compatibility level " + v1 + " which is prohibited by " + v2);
        }
        /*SL:360*/MixinEnvironment.setCompatibilityLevel(v1);
    }
    
    private MixinEnvironment parseSelector(final String v-5, final MixinEnvironment v-4) {
        /*SL:365*/if (v-5 != null) {
            final String[] split;
            final String[] array = /*EL:367*/split = v-5.split("[&\\| ]");
            for (String v1 : split) {
                /*SL:368*/v1 = v1.trim();
                final Pattern a1 = /*EL:369*/Pattern.compile("^@env(?:ironment)?\\(([A-Z]+)\\)$");
                final Matcher a2 = /*EL:370*/a1.matcher(v1);
                /*SL:371*/if (a2.matches()) {
                    /*SL:373*/return MixinEnvironment.getEnvironment(MixinEnvironment.Phase.forName(a2.group(1)));
                }
            }
            final MixinEnvironment.Phase forName = /*EL:377*/MixinEnvironment.Phase.forName(v-5);
            /*SL:378*/if (forName != null) {
                /*SL:379*/return MixinEnvironment.getEnvironment(forName);
            }
        }
        /*SL:382*/return v-4;
    }
    
    private void initInjectionPoints() {
        /*SL:387*/if (this.injectorOptions.injectionPoints == null) {
            /*SL:388*/return;
        }
        /*SL:391*/for (final String v0 : this.injectorOptions.injectionPoints) {
            try {
                final Class<?> v = /*EL:393*/this.service.getClassProvider().findClass(v0, true);
                /*SL:394*/if (InjectionPoint.class.isAssignableFrom(v)) {
                    /*SL:395*/InjectionPoint.register((Class<? extends InjectionPoint>)v);
                }
                else {
                    /*SL:397*/this.logger.error("Unable to register injection point {} for {}, class must extend InjectionPoint", new Object[] { v, this });
                }
            }
            catch (Throwable v2) {
                /*SL:400*/this.logger.catching(v2);
            }
        }
    }
    
    private boolean checkVersion() throws MixinInitialisationError {
        /*SL:406*/if (this.version == null) {
            /*SL:407*/this.logger.error("Mixin config {} does not specify \"minVersion\" property", new Object[] { this.name });
        }
        final VersionNumber v1 = /*EL:410*/VersionNumber.parse(this.version);
        final VersionNumber v2 = /*EL:411*/VersionNumber.parse(this.env.getVersion());
        /*SL:412*/if (v1.compareTo(v2) <= 0) {
            /*SL:423*/return true;
        }
        this.logger.warn("Mixin config {} requires mixin subsystem version {} but {} was found. The mixin config will not be applied.", new Object[] { this.name, v1, v2 });
        if (this.required) {
            throw new MixinInitialisationError("Required mixin config " + this.name + " requires mixin subsystem version " + v1);
        }
        return false;
    }
    
    void addListener(final IListener a1) {
        /*SL:432*/this.listeners.add(a1);
    }
    
    void onSelect() {
        /*SL:439*/if (this.pluginClassName != null) {
            try {
                final Class<?> v1 = /*EL:441*/this.service.getClassProvider().findClass(this.pluginClassName, true);
                /*SL:442*/this.plugin = (IMixinConfigPlugin)v1.newInstance();
                /*SL:444*/if (this.plugin != null) {
                    /*SL:445*/this.plugin.onLoad(this.mixinPackage);
                }
            }
            catch (Throwable v2) {
                /*SL:448*/v2.printStackTrace();
                /*SL:449*/this.plugin = null;
            }
        }
        /*SL:453*/if (!this.mixinPackage.endsWith(".")) {
            /*SL:454*/this.mixinPackage += ".";
        }
        boolean v3 = /*EL:457*/false;
        /*SL:459*/if (this.refMapperConfig == null) {
            /*SL:460*/if (this.plugin != null) {
                /*SL:461*/this.refMapperConfig = this.plugin.getRefMapperConfig();
            }
            /*SL:464*/if (this.refMapperConfig == null) {
                /*SL:465*/v3 = true;
                /*SL:466*/this.refMapperConfig = "mixin.refmap.json";
            }
        }
        /*SL:470*/this.refMapper = ReferenceMapper.read(this.refMapperConfig);
        /*SL:471*/this.verboseLogging |= this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
        /*SL:473*/if (!v3 && this.refMapper.isDefault() && !this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
            /*SL:474*/this.logger.warn("Reference map '{}' for {} could not be read. If this is a development environment you can ignore this message", new Object[] { this.refMapperConfig, this });
        }
        /*SL:478*/if (this.env.getOption(MixinEnvironment.Option.REFMAP_REMAP)) {
            /*SL:479*/this.refMapper = RemappingReferenceMapper.of(this.env, this.refMapper);
        }
    }
    
    void prepare() {
        /*SL:497*/if (this.prepared) {
            /*SL:498*/return;
        }
        /*SL:500*/this.prepared = true;
        /*SL:502*/this.prepareMixins(this.mixinClasses, false);
        /*SL:504*/switch (this.env.getSide()) {
            case CLIENT: {
                /*SL:506*/this.prepareMixins(this.mixinClassesClient, false);
                /*SL:507*/break;
            }
            case SERVER: {
                /*SL:509*/this.prepareMixins(this.mixinClassesServer, false);
                /*SL:510*/break;
            }
            default: {
                /*SL:514*/this.logger.warn("Mixin environment was unable to detect the current side, sided mixins will not be applied");
                break;
            }
        }
    }
    
    void postInitialise() {
        /*SL:520*/if (this.plugin != null) {
            final List<String> v1 = /*EL:521*/this.plugin.getMixins();
            /*SL:522*/this.prepareMixins(v1, true);
        }
        final Iterator<MixinInfo> v2 = /*EL:525*/this.mixins.iterator();
        while (v2.hasNext()) {
            final MixinInfo v3 = /*EL:526*/v2.next();
            try {
                /*SL:528*/v3.validate();
                /*SL:529*/for (final IListener v4 : this.listeners) {
                    /*SL:530*/v4.onInit(v3);
                }
            }
            catch (InvalidMixinException v5) {
                /*SL:533*/this.logger.error(v5.getMixin() + ": " + v5.getMessage(), (Throwable)v5);
                /*SL:534*/this.removeMixin(v3);
                /*SL:535*/v2.remove();
            }
            catch (Exception v6) {
                /*SL:537*/this.logger.error(v6.getMessage(), (Throwable)v6);
                /*SL:538*/this.removeMixin(v3);
                /*SL:539*/v2.remove();
            }
        }
    }
    
    private void removeMixin(final MixinInfo v-1) {
        /*SL:545*/for (final List<MixinInfo> v1 : this.mixinMapping.values()) {
            final Iterator<MixinInfo> a1 = /*EL:546*/v1.iterator();
            while (a1.hasNext()) {
                /*SL:547*/if (v-1 == a1.next()) {
                    /*SL:548*/a1.remove();
                }
            }
        }
    }
    
    private void prepareMixins(final List<String> v-6, final boolean v-5) {
        /*SL:555*/if (v-6 == null) {
            /*SL:556*/return;
        }
        /*SL:559*/for (final String v4 : v-6) {
            final String string = /*EL:560*/this.mixinPackage + v4;
            /*SL:562*/if (v4 != null) {
                if (MixinConfig.globalMixinList.contains(string)) {
                    /*SL:563*/continue;
                }
                MixinInfo a3 = /*EL:566*/null;
                try {
                    /*SL:569*/a3 = new MixinInfo(this.service, this, v4, true, this.plugin, v-5);
                    /*SL:570*/if (a3.getTargetClasses().size() <= 0) {
                        continue;
                    }
                    MixinConfig.globalMixinList.add(/*EL:571*/string);
                    /*SL:572*/for (String a2 : a3.getTargetClasses()) {
                        /*SL:573*/a2 = a2.replace('/', '.');
                        /*SL:574*/this.mixinsFor(a2).add(a3);
                        /*SL:575*/this.unhandledTargets.add(a2);
                    }
                    /*SL:577*/for (final IListener v1 : this.listeners) {
                        /*SL:578*/v1.onPrepare(a3);
                    }
                    /*SL:580*/this.mixins.add(a3);
                }
                catch (InvalidMixinException v2) {
                    /*SL:583*/if (this.required) {
                        /*SL:584*/throw v2;
                    }
                    /*SL:586*/this.logger.error(v2.getMessage(), (Throwable)v2);
                }
                catch (Exception v3) {
                    /*SL:588*/if (this.required) {
                        /*SL:589*/throw new InvalidMixinException(a3, "Error initialising mixin " + a3 + " - " + v3.getClass() + ": " + v3.getMessage(), v3);
                    }
                    /*SL:591*/this.logger.error(v3.getMessage(), (Throwable)v3);
                }
            }
        }
    }
    
    void postApply(final String a1, final ClassNode a2) {
        /*SL:597*/this.unhandledTargets.remove(a1);
    }
    
    public Config getHandle() {
        /*SL:604*/if (this.handle == null) {
            /*SL:605*/this.handle = new Config(this);
        }
        /*SL:607*/return this.handle;
    }
    
    @Override
    public boolean isRequired() {
        /*SL:615*/return this.required;
    }
    
    @Override
    public MixinEnvironment getEnvironment() {
        /*SL:624*/return this.env;
    }
    
    @Override
    public String getName() {
        /*SL:632*/return this.name;
    }
    
    @Override
    public String getMixinPackage() {
        /*SL:640*/return this.mixinPackage;
    }
    
    @Override
    public int getPriority() {
        /*SL:648*/return this.priority;
    }
    
    public int getDefaultMixinPriority() {
        /*SL:656*/return this.mixinPriority;
    }
    
    public int getDefaultRequiredInjections() {
        /*SL:666*/return this.injectorOptions.defaultRequireValue;
    }
    
    public String getDefaultInjectorGroup() {
        final String v1 = /*EL:675*/this.injectorOptions.defaultGroup;
        /*SL:676*/return (v1 != null && !v1.isEmpty()) ? v1 : "default";
    }
    
    public boolean conformOverwriteVisibility() {
        /*SL:686*/return this.overwriteOptions.conformAccessModifiers;
    }
    
    public boolean requireOverwriteAnnotations() {
        /*SL:696*/return this.overwriteOptions.requireOverwriteAnnotations;
    }
    
    public int getMaxShiftByValue() {
        /*SL:708*/return Math.min(Math.max(this.injectorOptions.maxShiftBy, 0), 5);
    }
    
    public boolean select(final MixinEnvironment a1) {
        /*SL:713*/this.visited = true;
        /*SL:714*/return this.env == a1;
    }
    
    boolean isVisited() {
        /*SL:719*/return this.visited;
    }
    
    int getDeclaredMixinCount() {
        /*SL:728*/return getCollectionSize(this.mixinClasses, this.mixinClassesClient, this.mixinClassesServer);
    }
    
    int getMixinCount() {
        /*SL:737*/return this.mixins.size();
    }
    
    public List<String> getClasses() {
        /*SL:744*/return Collections.<String>unmodifiableList((List<? extends String>)this.mixinClasses);
    }
    
    public boolean shouldSetSourceFile() {
        /*SL:752*/return this.setSourceFile;
    }
    
    public IReferenceMapper getReferenceMapper() {
        /*SL:759*/if (this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
            /*SL:760*/return ReferenceMapper.DEFAULT_MAPPER;
        }
        /*SL:762*/this.refMapper.setContext(this.env.getRefmapObfuscationContext());
        /*SL:763*/return this.refMapper;
    }
    
    String remapClassName(final String a1, final String a2) {
        /*SL:771*/return this.getReferenceMapper().remap(a1, a2);
    }
    
    @Override
    public IMixinConfigPlugin getPlugin() {
        /*SL:779*/return this.plugin;
    }
    
    @Override
    public Set<String> getTargets() {
        /*SL:787*/return Collections.<String>unmodifiableSet((Set<? extends String>)this.mixinMapping.keySet());
    }
    
    public Set<String> getUnhandledTargets() {
        /*SL:794*/return Collections.<String>unmodifiableSet((Set<? extends String>)this.unhandledTargets);
    }
    
    public Level getLoggingLevel() {
        /*SL:801*/return this.verboseLogging ? Level.INFO : Level.DEBUG;
    }
    
    public boolean packageMatch(final String a1) {
        /*SL:812*/return a1.startsWith(this.mixinPackage);
    }
    
    public boolean hasMixinsFor(final String a1) {
        /*SL:823*/return this.mixinMapping.containsKey(a1);
    }
    
    public List<MixinInfo> getMixinsFor(final String a1) {
        /*SL:833*/return this.mixinsFor(a1);
    }
    
    private List<MixinInfo> mixinsFor(final String a1) {
        List<MixinInfo> v1 = /*EL:837*/this.mixinMapping.get(a1);
        /*SL:838*/if (v1 == null) {
            /*SL:839*/v1 = new ArrayList<MixinInfo>();
            /*SL:840*/this.mixinMapping.put(a1, v1);
        }
        /*SL:842*/return v1;
    }
    
    public List<String> reloadMixin(final String v2, final byte[] v3) {
        Iterator<MixinInfo> a2 = /*EL:853*/this.mixins.iterator();
        while (a2.hasNext()) {
            /*SL:854*/a2 = a2.next();
            /*SL:855*/if (a2.getClassName().equals(v2)) {
                /*SL:856*/a2.reloadMixin(v3);
                /*SL:857*/return a2.getTargetClasses();
            }
        }
        /*SL:860*/return Collections.<String>emptyList();
    }
    
    @Override
    public String toString() {
        /*SL:865*/return this.name;
    }
    
    @Override
    public int compareTo(final MixinConfig a1) {
        /*SL:873*/if (a1 == null) {
            /*SL:874*/return 0;
        }
        /*SL:876*/if (a1.priority == this.priority) {
            /*SL:877*/return this.order - a1.order;
        }
        /*SL:879*/return this.priority - a1.priority;
    }
    
    static Config create(final String v-1, final MixinEnvironment v0) {
        try {
            final IMixinService a1 = /*EL:892*/MixinService.getService();
            final MixinConfig a2 = /*EL:893*/(MixinConfig)new Gson().fromJson((Reader)new InputStreamReader(a1.getResourceAsStream(v-1)), (Class)MixinConfig.class);
            /*SL:894*/if (a2.onLoad(a1, v-1, v0)) {
                /*SL:895*/return a2.getHandle();
            }
            /*SL:897*/return null;
        }
        catch (Exception v) {
            /*SL:899*/v.printStackTrace();
            /*SL:900*/throw new IllegalArgumentException(String.format("The specified resource '%s' was invalid or could not be read", v-1), v);
        }
    }
    
    private static int getCollectionSize(final Collection<?>... v1) {
        int v2 = /*EL:905*/0;
        /*SL:906*/for (final Collection<?> a1 : v1) {
            /*SL:907*/if (a1 != null) {
                /*SL:908*/v2 += a1.size();
            }
        }
        /*SL:911*/return v2;
    }
    
    static {
        MixinConfig.configOrder = 0;
        globalMixinList = new HashSet<String>();
    }
    
    static class InjectorOptions
    {
        @SerializedName("defaultRequire")
        int defaultRequireValue;
        @SerializedName("defaultGroup")
        String defaultGroup;
        @SerializedName("injectionPoints")
        List<String> injectionPoints;
        @SerializedName("maxShiftBy")
        int maxShiftBy;
        
        InjectorOptions() {
            this.defaultRequireValue = 0;
            this.defaultGroup = "default";
            this.maxShiftBy = 0;
        }
    }
    
    static class OverwriteOptions
    {
        @SerializedName("conformVisibility")
        boolean conformAccessModifiers;
        @SerializedName("requireAnnotations")
        boolean requireOverwriteAnnotations;
    }
    
    interface IListener
    {
        void onPrepare(MixinInfo p0);
        
        void onInit(MixinInfo p0);
    }
}
