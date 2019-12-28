package org.spongepowered.asm.launch.platform;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.net.URL;
import java.io.File;
import org.spongepowered.asm.service.MixinService;
import java.util.Iterator;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.net.URI;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class MixinPlatformManager
{
    private static final String DEFAULT_MAIN_CLASS = "net.minecraft.client.main.Main";
    private static final String MIXIN_TWEAKER_CLASS = "org.spongepowered.asm.launch.MixinTweaker";
    private static final Logger logger;
    private final Map<URI, MixinContainer> containers;
    private MixinContainer primaryContainer;
    private boolean prepared;
    private boolean injected;
    
    public MixinPlatformManager() {
        this.containers = new LinkedHashMap<URI, MixinContainer>();
        this.prepared = false;
    }
    
    public void init() {
        MixinPlatformManager.logger.debug(/*EL:98*/"Initialising Mixin Platform Manager");
        URI v0 = /*EL:101*/null;
        try {
            /*SL:103*/v0 = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            /*SL:104*/if (v0 != null) {
                MixinPlatformManager.logger.debug(/*EL:105*/"Mixin platform: primary container is {}", new Object[] { v0 });
                /*SL:106*/this.primaryContainer = this.addContainer(v0);
            }
        }
        catch (URISyntaxException v) {
            /*SL:109*/v.printStackTrace();
        }
        /*SL:113*/this.scanClasspath();
    }
    
    public Collection<String> getPhaseProviderClasses() {
        final Collection<String> v1 = /*EL:120*/this.primaryContainer.getPhaseProviders();
        /*SL:121*/if (v1 != null) {
            /*SL:122*/return Collections.<String>unmodifiableCollection((Collection<? extends String>)v1);
        }
        /*SL:125*/return (Collection<String>)Collections.<Object>emptyList();
    }
    
    public final MixinContainer addContainer(final URI a1) {
        final MixinContainer v1 = /*EL:136*/this.containers.get(a1);
        /*SL:137*/if (v1 != null) {
            /*SL:138*/return v1;
        }
        MixinPlatformManager.logger.debug(/*EL:141*/"Adding mixin platform agents for container {}", new Object[] { a1 });
        final MixinContainer v2 = /*EL:142*/new MixinContainer(this, a1);
        /*SL:143*/this.containers.put(a1, v2);
        /*SL:145*/if (this.prepared) {
            /*SL:146*/v2.prepare();
        }
        /*SL:148*/return v2;
    }
    
    public final void prepare(final List<String> v0) {
        /*SL:157*/this.prepared = true;
        /*SL:158*/for (final MixinContainer a1 : this.containers.values()) {
            /*SL:159*/a1.prepare();
        }
        /*SL:161*/if (v0 != null) {
            /*SL:162*/this.parseArgs(v0);
        }
        else {
            final String v = /*EL:164*/System.getProperty("sun.java.command");
            /*SL:165*/if (v != null) {
                /*SL:166*/this.parseArgs(Arrays.<String>asList(v.split(" ")));
            }
        }
    }
    
    private void parseArgs(final List<String> v2) {
        boolean v3 = /*EL:177*/false;
        /*SL:178*/for (final String a1 : v2) {
            /*SL:179*/if (v3) {
                /*SL:180*/this.addConfig(a1);
            }
            /*SL:182*/v3 = "--mixin".equals(a1);
        }
    }
    
    public final void inject() {
        /*SL:190*/if (this.injected) {
            /*SL:191*/return;
        }
        /*SL:193*/this.injected = true;
        /*SL:195*/if (this.primaryContainer != null) {
            /*SL:196*/this.primaryContainer.initPrimaryContainer();
        }
        /*SL:199*/this.scanClasspath();
        MixinPlatformManager.logger.debug(/*EL:200*/"inject() running with {} agents", new Object[] { this.containers.size() });
        /*SL:201*/for (final MixinContainer v0 : this.containers.values()) {
            try {
                /*SL:203*/v0.inject();
            }
            catch (Exception v) {
                /*SL:205*/v.printStackTrace();
            }
        }
    }
    
    private void scanClasspath() {
        final URL[] classPath;
        final URL[] array = /*EL:216*/classPath = MixinService.getService().getClassProvider().getClassPath();
        for (final URL v0 : classPath) {
            try {
                final URI v = /*EL:218*/v0.toURI();
                /*SL:219*/if (!this.containers.containsKey(v)) {
                    MixinPlatformManager.logger.debug(/*EL:222*/"Scanning {} for mixin tweaker", new Object[] { v });
                    /*SL:223*/if ("file".equals(v.getScheme()) && new File(v).exists()) {
                        final MainAttributes v2 = /*EL:226*/MainAttributes.of(v);
                        final String v3 = /*EL:227*/v2.get("TweakClass");
                        /*SL:228*/if ("org.spongepowered.asm.launch.MixinTweaker".equals(v3)) {
                            MixinPlatformManager.logger.debug(/*EL:229*/"{} contains a mixin tweaker, adding agents", new Object[] { v });
                            /*SL:230*/this.addContainer(v);
                        }
                    }
                }
            }
            catch (Exception v4) {
                /*SL:233*/v4.printStackTrace();
            }
        }
    }
    
    public String getLaunchTarget() {
        /*SL:243*/for (final MixinContainer v0 : this.containers.values()) {
            final String v = /*EL:244*/v0.getLaunchTarget();
            /*SL:245*/if (v != null) {
                /*SL:246*/return v;
            }
        }
        /*SL:250*/return "net.minecraft.client.main.Main";
    }
    
    final void setCompatibilityLevel(final String v0) {
        try {
            final MixinEnvironment.CompatibilityLevel a1 = /*EL:262*/MixinEnvironment.CompatibilityLevel.valueOf(v0.toUpperCase());
            MixinPlatformManager.logger.debug(/*EL:263*/"Setting mixin compatibility level: {}", new Object[] { a1 });
            /*SL:264*/MixinEnvironment.setCompatibilityLevel(a1);
        }
        catch (IllegalArgumentException v) {
            MixinPlatformManager.logger.warn(/*EL:266*/"Invalid compatibility level specified: {}", new Object[] { v0 });
        }
    }
    
    final void addConfig(String v-1) {
        /*SL:280*/if (v-1.endsWith(".json")) {
            MixinPlatformManager.logger.debug(/*EL:281*/"Registering mixin config: {}", new Object[] { v-1 });
            /*SL:282*/Mixins.addConfiguration(v-1);
        }
        else/*SL:283*/ if (v-1.contains(".json@")) {
            final int a1 = /*EL:284*/v-1.indexOf(".json@");
            final String v1 = /*EL:285*/v-1.substring(a1 + 6);
            /*SL:286*/v-1 = v-1.substring(0, a1 + 5);
            final MixinEnvironment.Phase v2 = /*EL:287*/MixinEnvironment.Phase.forName(v1);
            /*SL:288*/if (v2 != null) {
                MixinPlatformManager.logger.warn(/*EL:289*/"Setting config phase via manifest is deprecated: {}. Specify target in config instead", new Object[] { v-1 });
                MixinPlatformManager.logger.debug(/*EL:290*/"Registering mixin config: {}", new Object[] { v-1 });
                /*SL:291*/MixinEnvironment.getEnvironment(v2).addConfiguration(v-1);
            }
        }
    }
    
    final void addTokenProvider(final String v-1) {
        /*SL:306*/if (v-1.contains("@")) {
            final String[] a1 = /*EL:307*/v-1.split("@", 2);
            final MixinEnvironment.Phase v1 = /*EL:308*/MixinEnvironment.Phase.forName(a1[1]);
            /*SL:309*/if (v1 != null) {
                MixinPlatformManager.logger.debug(/*EL:310*/"Registering token provider class: {}", new Object[] { a1[0] });
                /*SL:311*/MixinEnvironment.getEnvironment(v1).registerTokenProviderClass(a1[0]);
            }
            /*SL:313*/return;
        }
        /*SL:316*/MixinEnvironment.getDefaultEnvironment().registerTokenProviderClass(v-1);
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}
