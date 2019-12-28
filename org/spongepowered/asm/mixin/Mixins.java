package org.spongepowered.asm.mixin;

import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import java.util.LinkedHashSet;
import org.spongepowered.asm.launch.GlobalProperties;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.Config;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public final class Mixins
{
    private static final Logger logger;
    private static final String CONFIGS_KEY = "mixin.configs.queue";
    private static final Set<String> errorHandlers;
    
    public static void addConfigurations(final String... v1) {
        final MixinEnvironment v2 = /*EL:66*/MixinEnvironment.getDefaultEnvironment();
        /*SL:67*/for (final String a1 : v1) {
            createConfiguration(/*EL:68*/a1, v2);
        }
    }
    
    public static void addConfiguration(final String a1) {
        createConfiguration(/*EL:78*/a1, MixinEnvironment.getDefaultEnvironment());
    }
    
    @Deprecated
    static void addConfiguration(final String a1, final MixinEnvironment a2) {
        createConfiguration(/*EL:83*/a1, a2);
    }
    
    private static void createConfiguration(final String a2, final MixinEnvironment v1) {
        Config v2 = /*EL:88*/null;
        try {
            /*SL:91*/v2 = Config.create(a2, v1);
        }
        catch (Exception a3) {
            Mixins.logger.error(/*EL:93*/"Error encountered reading mixin config " + a2 + ": " + a3.getClass().getName() + " " + a3.getMessage(), (Throwable)a3);
        }
        registerConfiguration(/*EL:96*/v2);
    }
    
    private static void registerConfiguration(final Config a1) {
        /*SL:100*/if (a1 == null) {
            /*SL:101*/return;
        }
        final MixinEnvironment v1 = /*EL:104*/a1.getEnvironment();
        /*SL:105*/if (v1 != null) {
            /*SL:106*/v1.registerConfig(a1.getName());
        }
        getConfigs().add(/*EL:108*/a1);
    }
    
    public static int getUnvisitedCount() {
        int n = /*EL:127*/0;
        /*SL:128*/for (final Config v1 : getConfigs()) {
            /*SL:129*/if (!v1.isVisited()) {
                /*SL:130*/++n;
            }
        }
        /*SL:133*/return n;
    }
    
    public static Set<Config> getConfigs() {
        Set<Config> v1 = /*EL:141*/GlobalProperties.<Set<Config>>get("mixin.configs.queue");
        /*SL:142*/if (v1 == null) {
            /*SL:143*/v1 = new LinkedHashSet<Config>();
            /*SL:144*/GlobalProperties.put("mixin.configs.queue", v1);
        }
        /*SL:146*/return v1;
    }
    
    public static void registerErrorHandlerClass(final String a1) {
        /*SL:155*/if (a1 != null) {
            Mixins.errorHandlers.add(/*EL:156*/a1);
        }
    }
    
    public static Set<String> getErrorHandlerClasses() {
        /*SL:164*/return Collections.<String>unmodifiableSet((Set<? extends String>)Mixins.errorHandlers);
    }
    
    static {
        logger = LogManager.getLogger("mixin");
        errorHandlers = new LinkedHashSet<String>();
    }
}
