package org.spongepowered.asm.launch.platform;

import java.util.HashSet;
import java.util.Collections;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IRemapper;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.launchwrapper.Launch;
import java.io.File;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.lang.reflect.Method;
import java.util.List;
import org.spongepowered.asm.launch.GlobalProperties;
import java.net.URI;
import net.minecraft.launchwrapper.ITweaker;
import java.util.Set;

public class MixinPlatformAgentFML extends MixinPlatformAgentAbstract
{
    private static final String LOAD_CORE_MOD_METHOD = "loadCoreMod";
    private static final String GET_REPARSEABLE_COREMODS_METHOD = "getReparseableCoremods";
    private static final String CORE_MOD_MANAGER_CLASS = "net.minecraftforge.fml.relauncher.CoreModManager";
    private static final String CORE_MOD_MANAGER_CLASS_LEGACY = "cpw.mods.fml.relauncher.CoreModManager";
    private static final String GET_IGNORED_MODS_METHOD = "getIgnoredMods";
    private static final String GET_IGNORED_MODS_METHOD_LEGACY = "getLoadedCoremods";
    private static final String FML_REMAPPER_ADAPTER_CLASS = "org.spongepowered.asm.bridge.RemapperAdapterFML";
    private static final String FML_CMDLINE_COREMODS = "fml.coreMods.load";
    private static final String FML_PLUGIN_WRAPPER_CLASS = "FMLPluginWrapper";
    private static final String FML_CORE_MOD_INSTANCE_FIELD = "coreModInstance";
    private static final String MFATT_FORCELOADASMOD = "ForceLoadAsMod";
    private static final String MFATT_FMLCOREPLUGIN = "FMLCorePlugin";
    private static final String MFATT_COREMODCONTAINSMOD = "FMLCorePluginContainsFMLMod";
    private static final String FML_TWEAKER_DEOBF = "FMLDeobfTweaker";
    private static final String FML_TWEAKER_INJECTION = "FMLInjectionAndSortingTweaker";
    private static final String FML_TWEAKER_TERMINAL = "TerminalTweaker";
    private static final Set<String> loadedCoreMods;
    private final ITweaker coreModWrapper;
    private final String fileName;
    private Class<?> clCoreModManager;
    private boolean initInjectionState;
    
    public MixinPlatformAgentFML(final MixinPlatformManager a1, final URI a2) {
        super(a1, a2);
        this.fileName = this.container.getName();
        this.coreModWrapper = this.initFMLCoreMod();
    }
    
    private ITweaker initFMLCoreMod() {
        try {
            try {
                /*SL:143*/this.clCoreModManager = getCoreModManagerClass();
            }
            catch (ClassNotFoundException v1) {
                MixinPlatformAgentAbstract.logger.info(/*EL:145*/"FML platform manager could not load class {}. Proceeding without FML support.", new Object[] { v1.getMessage() });
                /*SL:147*/return null;
            }
            /*SL:150*/if ("true".equalsIgnoreCase(this.attributes.get("ForceLoadAsMod"))) {
                MixinPlatformAgentAbstract.logger.debug(/*EL:151*/"ForceLoadAsMod was specified for {}, attempting force-load", new Object[] { this.fileName });
                /*SL:152*/this.loadAsMod();
            }
            /*SL:155*/return this.injectCorePlugin();
        }
        catch (Exception v2) {
            MixinPlatformAgentAbstract.logger.catching(/*EL:157*/(Throwable)v2);
            /*SL:158*/return null;
        }
    }
    
    private void loadAsMod() {
        try {
            getIgnoredMods(/*EL:175*/this.clCoreModManager).remove(this.fileName);
        }
        catch (Exception v1) {
            MixinPlatformAgentAbstract.logger.catching(/*EL:177*/(Throwable)v1);
        }
        /*SL:180*/if (this.attributes.get("FMLCorePluginContainsFMLMod") != null) {
            /*SL:181*/if (this.isIgnoredReparseable()) {
                MixinPlatformAgentAbstract.logger.debug(/*EL:182*/"Ignoring request to add {} to reparseable coremod collection - it is a deobfuscated dependency", new Object[] { this.fileName });
                /*SL:184*/return;
            }
            /*SL:186*/this.addReparseableJar();
        }
    }
    
    private boolean isIgnoredReparseable() {
        /*SL:191*/return this.container.toString().contains("deobfedDeps");
    }
    
    private void addReparseableJar() {
        try {
            final Method v1 = /*EL:200*/this.clCoreModManager.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.reparseablecoremodsmethod", "getReparseableCoremods"), (Class<?>[])new Class[0]);
            final List<String> v2 = /*EL:203*/(List<String>)v1.invoke(null, new Object[0]);
            /*SL:204*/if (!v2.contains(this.fileName)) {
                MixinPlatformAgentAbstract.logger.debug(/*EL:205*/"Adding {} to reparseable coremod collection", new Object[] { this.fileName });
                /*SL:206*/v2.add(this.fileName);
            }
        }
        catch (Exception v3) {
            MixinPlatformAgentAbstract.logger.catching(/*EL:209*/(Throwable)v3);
        }
    }
    
    private ITweaker injectCorePlugin() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String v1 = /*EL:214*/this.attributes.get("FMLCorePlugin");
        /*SL:215*/if (v1 == null) {
            /*SL:216*/return null;
        }
        /*SL:219*/if (this.isAlreadyInjected(v1)) {
            MixinPlatformAgentAbstract.logger.debug(/*EL:220*/"{} has core plugin {}. Skipping because it was already injected.", new Object[] { this.fileName, v1 });
            /*SL:221*/return null;
        }
        MixinPlatformAgentAbstract.logger.debug(/*EL:224*/"{} has core plugin {}. Injecting it into FML for co-initialisation:", new Object[] { this.fileName, v1 });
        final Method v2 = /*EL:225*/this.clCoreModManager.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.loadcoremodmethod", "loadCoreMod"), LaunchClassLoader.class, String.class, File.class);
        /*SL:227*/v2.setAccessible(true);
        final ITweaker v3 = /*EL:228*/(ITweaker)v2.invoke(null, Launch.classLoader, v1, this.container);
        /*SL:229*/if (v3 == null) {
            MixinPlatformAgentAbstract.logger.debug(/*EL:230*/"Core plugin {} could not be loaded.", new Object[] { v1 });
            /*SL:231*/return null;
        }
        /*SL:236*/this.initInjectionState = isTweakerQueued("FMLInjectionAndSortingTweaker");
        MixinPlatformAgentFML.loadedCoreMods.add(/*EL:238*/v1);
        /*SL:239*/return v3;
    }
    
    private boolean isAlreadyInjected(final String v-5) {
        /*SL:244*/if (MixinPlatformAgentFML.loadedCoreMods.contains(v-5)) {
            /*SL:245*/return true;
        }
        try {
            final List<ITweaker> list = /*EL:250*/GlobalProperties.<List<ITweaker>>get("Tweaks");
            /*SL:251*/if (list == null) {
                /*SL:252*/return false;
            }
            /*SL:255*/for (final ITweaker tweaker : list) {
                final Class<? extends ITweaker> class1 = /*EL:256*/tweaker.getClass();
                /*SL:257*/if ("FMLPluginWrapper".equals(class1.getSimpleName())) {
                    final Field a1 = /*EL:258*/class1.getField("coreModInstance");
                    /*SL:259*/a1.setAccessible(true);
                    final Object v1 = /*EL:260*/a1.get(tweaker);
                    /*SL:261*/if (v-5.equals(v1.getClass().getName())) {
                        /*SL:262*/return true;
                    }
                    continue;
                }
            }
        }
        catch (Exception ex) {}
        /*SL:270*/return false;
    }
    
    @Override
    public String getPhaseProvider() {
        /*SL:275*/return MixinPlatformAgentFML.class.getName() + "$PhaseProvider";
    }
    
    @Override
    public void prepare() {
        /*SL:283*/this.initInjectionState |= isTweakerQueued("FMLInjectionAndSortingTweaker");
    }
    
    @Override
    public void initPrimaryContainer() {
        /*SL:292*/if (this.clCoreModManager != null) {
            /*SL:294*/this.injectRemapper();
        }
    }
    
    private void injectRemapper() {
        try {
            MixinPlatformAgentAbstract.logger.debug(/*EL:300*/"Creating FML remapper adapter: {}", new Object[] { "org.spongepowered.asm.bridge.RemapperAdapterFML" });
            final Class<?> v1 = /*EL:301*/Class.forName("org.spongepowered.asm.bridge.RemapperAdapterFML", true, (ClassLoader)Launch.classLoader);
            final Method v2 = /*EL:302*/v1.getDeclaredMethod("create", (Class<?>[])new Class[0]);
            final IRemapper v3 = /*EL:303*/(IRemapper)v2.invoke(null, new Object[0]);
            /*SL:304*/MixinEnvironment.getDefaultEnvironment().getRemappers().add(v3);
        }
        catch (Exception v4) {
            MixinPlatformAgentAbstract.logger.debug(/*EL:306*/"Failed instancing FML remapper adapter, things will probably go horribly for notch-obf'd mods!");
        }
    }
    
    @Override
    public void inject() {
        /*SL:315*/if (this.coreModWrapper != null && this.checkForCoInitialisation()) {
            MixinPlatformAgentAbstract.logger.debug(/*EL:316*/"FML agent is co-initiralising coremod instance {} for {}", new Object[] { this.coreModWrapper, this.uri });
            /*SL:317*/this.coreModWrapper.injectIntoClassLoader(Launch.classLoader);
        }
    }
    
    @Override
    public String getLaunchTarget() {
        /*SL:326*/return null;
    }
    
    protected final boolean checkForCoInitialisation() {
        final boolean v1 = isTweakerQueued(/*EL:342*/"FMLInjectionAndSortingTweaker");
        final boolean v2 = isTweakerQueued(/*EL:343*/"TerminalTweaker");
        /*SL:344*/if ((this.initInjectionState && v2) || v1) {
            MixinPlatformAgentAbstract.logger.debug(/*EL:345*/"FML agent is skipping co-init for {} because FML will inject it normally", new Object[] { this.coreModWrapper });
            /*SL:346*/return false;
        }
        /*SL:349*/return !isTweakerQueued("FMLDeobfTweaker");
    }
    
    private static boolean isTweakerQueued(final String v1) {
        /*SL:360*/for (final String a1 : GlobalProperties.<List<String>>get("TweakClasses")) {
            /*SL:361*/if (a1.endsWith(v1)) {
                /*SL:362*/return true;
            }
        }
        /*SL:365*/return false;
    }
    
    private static Class<?> getCoreModManagerClass() throws ClassNotFoundException {
        try {
            /*SL:374*/return Class.forName(GlobalProperties.getString("mixin.launch.fml.coremodmanagerclass", "net.minecraftforge.fml.relauncher.CoreModManager"));
        }
        catch (ClassNotFoundException v1) {
            /*SL:377*/return Class.forName("cpw.mods.fml.relauncher.CoreModManager");
        }
    }
    
    private static List<String> getIgnoredMods(final Class<?> v-1) throws IllegalAccessException, InvocationTargetException {
        Method v0 = /*EL:383*/null;
        try {
            /*SL:386*/v0 = v-1.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.ignoredmodsmethod", "getIgnoredMods"), (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException v) {
            try {
                /*SL:391*/v0 = v-1.getDeclaredMethod("getLoadedCoremods", (Class<?>[])new Class[0]);
            }
            catch (NoSuchMethodException a1) {
                MixinPlatformAgentAbstract.logger.catching(Level.DEBUG, /*EL:393*/(Throwable)a1);
                /*SL:394*/return Collections.<String>emptyList();
            }
        }
        /*SL:398*/return (List<String>)v0.invoke(null, new Object[0]);
    }
    
    static {
        loadedCoreMods = new HashSet<String>();
        for (final String v1 : System.getProperty("fml.coreMods.load", "").split(",")) {
            if (!v1.isEmpty()) {
                MixinPlatformAgentAbstract.logger.debug("FML platform agent will ignore coremod {} specified on the command line", new Object[] { v1 });
                MixinPlatformAgentFML.loadedCoreMods.add(v1);
            }
        }
    }
}
