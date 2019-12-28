package org.spongepowered.asm.launch;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.List;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.launch.platform.MixinPlatformManager;
import org.apache.logging.log4j.Logger;

public abstract class MixinBootstrap
{
    public static final String VERSION = "0.7.11";
    private static final Logger logger;
    private static boolean initialised;
    private static boolean initState;
    private static MixinPlatformManager platform;
    
    @Deprecated
    public static void addProxy() {
        /*SL:92*/MixinService.getService().beginPhase();
    }
    
    public static MixinPlatformManager getPlatform() {
        /*SL:99*/if (MixinBootstrap.platform == null) {
            final Object v1 = /*EL:100*/GlobalProperties.<Object>get("mixin.platform");
            /*SL:101*/if (v1 instanceof MixinPlatformManager) {
                MixinBootstrap.platform = /*EL:102*/(MixinPlatformManager)v1;
            }
            else {
                /*SL:105*/GlobalProperties.put("mixin.platform", MixinBootstrap.platform = new MixinPlatformManager());
                MixinBootstrap.platform.init();
            }
        }
        /*SL:109*/return MixinBootstrap.platform;
    }
    
    public static void init() {
        /*SL:116*/if (!start()) {
            /*SL:117*/return;
        }
        doInit(/*EL:120*/null);
    }
    
    static boolean start() {
        /*SL:127*/if (!isSubsystemRegistered()) {
            registerSubsystem(/*EL:135*/"0.7.11");
            /*SL:137*/if (!MixinBootstrap.initialised) {
                MixinBootstrap.initialised = /*EL:138*/true;
                final String v1 = /*EL:140*/System.getProperty("sun.java.command");
                /*SL:141*/if (v1 != null && v1.contains("GradleStart")) {
                    /*SL:142*/System.setProperty("mixin.env.remapRefMap", "true");
                }
                final MixinEnvironment.Phase v2 = /*EL:145*/MixinService.getService().getInitialPhase();
                /*SL:146*/if (v2 == MixinEnvironment.Phase.DEFAULT) {
                    MixinBootstrap.logger.error(/*EL:147*/"Initialising mixin subsystem after game pre-init phase! Some mixins may be skipped.");
                    /*SL:148*/MixinEnvironment.init(v2);
                    getPlatform().prepare(/*EL:149*/null);
                    MixinBootstrap.initState = /*EL:150*/false;
                }
                else {
                    /*SL:152*/MixinEnvironment.init(v2);
                }
                /*SL:155*/MixinService.getService().beginPhase();
            }
            getPlatform();
            /*SL:160*/return true;
        }
        if (!checkSubsystemVersion()) {
            throw new MixinInitialisationError("Mixin subsystem version " + getActiveSubsystemVersion() + " was already initialised. Cannot bootstrap version " + "0.7.11");
        }
        return false;
    }
    
    static void doInit(final List<String> a1) {
        /*SL:167*/if (MixinBootstrap.initialised) {
            getPlatform().getPhaseProviderClasses();
            /*SL:182*/if (MixinBootstrap.initState) {
                getPlatform().prepare(/*EL:183*/a1);
                /*SL:184*/MixinService.getService().init();
            }
            /*SL:186*/return;
        }
        if (isSubsystemRegistered()) {
            MixinBootstrap.logger.warn("Multiple Mixin containers present, init suppressed for 0.7.11");
            return;
        }
        throw new IllegalStateException("MixinBootstrap.doInit() called before MixinBootstrap.start()");
    }
    
    static void inject() {
        getPlatform().inject();
    }
    
    private static boolean isSubsystemRegistered() {
        /*SL:193*/return GlobalProperties.<Object>get("mixin.initialised") != null;
    }
    
    private static boolean checkSubsystemVersion() {
        /*SL:197*/return "0.7.11".equals(getActiveSubsystemVersion());
    }
    
    private static Object getActiveSubsystemVersion() {
        final Object v1 = /*EL:201*/GlobalProperties.<Object>get("mixin.initialised");
        /*SL:202*/return (v1 != null) ? v1 : "";
    }
    
    private static void registerSubsystem(final String a1) {
        /*SL:206*/GlobalProperties.put("mixin.initialised", a1);
    }
    
    static {
        logger = LogManager.getLogger("mixin");
        MixinBootstrap.initialised = false;
        MixinBootstrap.initState = true;
        MixinService.boot();
        MixinService.getService().prepare();
    }
}
