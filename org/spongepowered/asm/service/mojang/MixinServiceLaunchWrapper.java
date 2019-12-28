package org.spongepowered.asm.service.mojang;

import org.apache.logging.log4j.LogManager;
import java.lang.reflect.Method;
import net.minecraft.launchwrapper.ITweaker;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import org.spongepowered.asm.util.perf.Profiler;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import java.net.URLClassLoader;
import java.util.Iterator;
import net.minecraft.launchwrapper.IClassTransformer;
import java.util.ArrayList;
import org.spongepowered.asm.service.ITransformer;
import java.net.URL;
import java.io.InputStream;
import org.spongepowered.asm.mixin.throwables.MixinException;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.spongepowered.asm.launch.GlobalProperties;
import java.util.List;
import org.spongepowered.asm.mixin.MixinEnvironment;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.IClassNameTransformer;
import org.spongepowered.asm.util.ReEntranceLock;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.service.IClassBytecodeProvider;
import org.spongepowered.asm.service.IClassProvider;
import org.spongepowered.asm.service.IMixinService;

public class MixinServiceLaunchWrapper implements IMixinService, IClassProvider, IClassBytecodeProvider
{
    public static final String BLACKBOARD_KEY_TWEAKCLASSES = "TweakClasses";
    public static final String BLACKBOARD_KEY_TWEAKS = "Tweaks";
    private static final String LAUNCH_PACKAGE = "org.spongepowered.asm.launch.";
    private static final String MIXIN_PACKAGE = "org.spongepowered.asm.mixin.";
    private static final String STATE_TWEAKER = "org.spongepowered.asm.mixin.EnvironmentStateTweaker";
    private static final String TRANSFORMER_PROXY_CLASS = "org.spongepowered.asm.mixin.transformer.Proxy";
    private static final Logger logger;
    private final LaunchClassLoaderUtil classLoaderUtil;
    private final ReEntranceLock lock;
    private IClassNameTransformer nameTransformer;
    
    public MixinServiceLaunchWrapper() {
        this.classLoaderUtil = new LaunchClassLoaderUtil(Launch.classLoader);
        this.lock = new ReEntranceLock(1);
    }
    
    @Override
    public String getName() {
        /*SL:99*/return "LaunchWrapper";
    }
    
    @Override
    public boolean isValid() {
        try {
            Launch.classLoader.hashCode();
        }
        catch (Throwable v1) {
            /*SL:111*/return false;
        }
        /*SL:113*/return true;
    }
    
    @Override
    public void prepare() {
        Launch.classLoader.addClassLoaderExclusion(/*EL:122*/"org.spongepowered.asm.launch.");
    }
    
    @Override
    public MixinEnvironment.Phase getInitialPhase() {
        /*SL:130*/if (findInStackTrace("net.minecraft.launchwrapper.Launch", "launch") > 132) {
            /*SL:131*/return MixinEnvironment.Phase.DEFAULT;
        }
        /*SL:133*/return MixinEnvironment.Phase.PREINIT;
    }
    
    @Override
    public void init() {
        /*SL:141*/if (findInStackTrace("net.minecraft.launchwrapper.Launch", "launch") < 4) {
            MixinServiceLaunchWrapper.logger.error(/*EL:142*/"MixinBootstrap.doInit() called during a tweak constructor!");
        }
        final List<String> v1 = /*EL:145*/GlobalProperties.<List<String>>get("TweakClasses");
        /*SL:146*/if (v1 != null) {
            /*SL:147*/v1.add("org.spongepowered.asm.mixin.EnvironmentStateTweaker");
        }
    }
    
    @Override
    public ReEntranceLock getReEntranceLock() {
        /*SL:156*/return this.lock;
    }
    
    @Override
    public Collection<String> getPlatformAgents() {
        /*SL:164*/return ImmutableList.<String>of("org.spongepowered.asm.launch.platform.MixinPlatformAgentFML");
    }
    
    @Override
    public IClassProvider getClassProvider() {
        /*SL:174*/return this;
    }
    
    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        /*SL:182*/return this;
    }
    
    @Override
    public Class<?> findClass(final String a1) throws ClassNotFoundException {
        /*SL:191*/return (Class<?>)Launch.classLoader.findClass(a1);
    }
    
    @Override
    public Class<?> findClass(final String a1, final boolean a2) throws ClassNotFoundException {
        /*SL:200*/return Class.forName(a1, a2, (ClassLoader)Launch.classLoader);
    }
    
    @Override
    public Class<?> findAgentClass(final String a1, final boolean a2) throws ClassNotFoundException {
        /*SL:209*/return Class.forName(a1, a2, Launch.class.getClassLoader());
    }
    
    @Override
    public void beginPhase() {
        Launch.classLoader.registerTransformer(/*EL:217*/"org.spongepowered.asm.mixin.transformer.Proxy");
    }
    
    @Override
    public void checkEnv(final Object a1) {
        /*SL:226*/if (a1.getClass().getClassLoader() != Launch.class.getClassLoader()) {
            /*SL:227*/throw new MixinException("Attempted to init the mixin environment in the wrong classloader");
        }
    }
    
    @Override
    public InputStream getResourceAsStream(final String a1) {
        /*SL:237*/return Launch.classLoader.getResourceAsStream(a1);
    }
    
    @Override
    public void registerInvalidClass(final String a1) {
        /*SL:246*/this.classLoaderUtil.registerInvalidClass(a1);
    }
    
    @Override
    public boolean isClassLoaded(final String a1) {
        /*SL:255*/return this.classLoaderUtil.isClassLoaded(a1);
    }
    
    @Override
    public String getClassRestrictions(final String a1) {
        String v1 = /*EL:264*/"";
        /*SL:265*/if (this.classLoaderUtil.isClassClassLoaderExcluded(a1, null)) {
            /*SL:266*/v1 = "PACKAGE_CLASSLOADER_EXCLUSION";
        }
        /*SL:268*/if (this.classLoaderUtil.isClassTransformerExcluded(a1, null)) {
            /*SL:269*/v1 = ((v1.length() > 0) ? (v1 + ",") : "") + "PACKAGE_TRANSFORMER_EXCLUSION";
        }
        /*SL:271*/return v1;
    }
    
    @Override
    public URL[] getClassPath() {
        /*SL:279*/return Launch.classLoader.getSources().<URL>toArray(new URL[0]);
    }
    
    @Override
    public Collection<ITransformer> getTransformers() {
        final List<IClassTransformer> transformers = (List<IClassTransformer>)Launch.classLoader.getTransformers();
        final List<ITransformer> list = /*EL:288*/new ArrayList<ITransformer>(transformers.size());
        /*SL:289*/for (final IClassTransformer v1 : transformers) {
            /*SL:290*/if (v1 instanceof ITransformer) {
                /*SL:291*/list.add((ITransformer)v1);
            }
            else {
                /*SL:293*/list.add(new LegacyTransformerHandle(v1));
            }
            /*SL:296*/if (v1 instanceof IClassNameTransformer) {
                MixinServiceLaunchWrapper.logger.debug(/*EL:297*/"Found name transformer: {}", new Object[] { v1.getClass().getName() });
                /*SL:298*/this.nameTransformer = (IClassNameTransformer)v1;
            }
        }
        /*SL:302*/return list;
    }
    
    @Override
    public byte[] getClassBytes(final String v2, final String v3) throws IOException {
        final byte[] v4 = Launch.classLoader.getClassBytes(/*EL:311*/v2);
        /*SL:312*/if (v4 != null) {
            /*SL:313*/return v4;
        }
        final URLClassLoader v5 = /*EL:316*/(URLClassLoader)Launch.class.getClassLoader();
        InputStream v6 = /*EL:318*/null;
        try {
            final String a1 = /*EL:320*/v3.replace('.', '/').concat(".class");
            /*SL:321*/v6 = v5.getResourceAsStream(a1);
            /*SL:322*/return IOUtils.toByteArray(v6);
        }
        catch (Exception a2) {
            /*SL:324*/return null;
        }
        finally {
            /*SL:326*/IOUtils.closeQuietly(v6);
        }
    }
    
    @Override
    public byte[] getClassBytes(final String v1, final boolean v2) throws ClassNotFoundException, IOException {
        final String v3 = /*EL:342*/v1.replace('/', '.');
        final String v4 = /*EL:343*/this.unmapClassName(v3);
        final Profiler v5 = /*EL:345*/MixinEnvironment.getProfiler();
        final Profiler.Section v6 = /*EL:346*/v5.begin(1, "class.load");
        byte[] v7 = /*EL:347*/this.getClassBytes(v4, v3);
        /*SL:348*/v6.end();
        /*SL:350*/if (v2) {
            final Profiler.Section a1 = /*EL:351*/v5.begin(1, "class.transform");
            /*SL:352*/v7 = this.applyTransformers(v4, v3, v7, v5);
            /*SL:353*/a1.end();
        }
        /*SL:356*/if (v7 == null) {
            /*SL:357*/throw new ClassNotFoundException(String.format("The specified class '%s' was not found", v3));
        }
        /*SL:360*/return v7;
    }
    
    private byte[] applyTransformers(final String v2, final String v3, byte[] v4, final Profiler v5) {
        /*SL:374*/if (this.classLoaderUtil.isClassExcluded(v2, v3)) {
            /*SL:375*/return v4;
        }
        final MixinEnvironment v6 = /*EL:378*/MixinEnvironment.getCurrentEnvironment();
        /*SL:380*/for (Profiler.Section a4 : v6.getTransformers()) {
            /*SL:382*/this.lock.clear();
            final int a2 = /*EL:384*/a4.getName().lastIndexOf(46);
            final String a3 = /*EL:385*/a4.getName().substring(a2 + 1);
            /*SL:386*/a4 = v5.begin(2, a3.toLowerCase());
            /*SL:387*/a4.setInfo(a4.getName());
            /*SL:388*/v4 = a4.transformClassBytes(v2, v3, v4);
            /*SL:389*/a4.end();
            /*SL:391*/if (this.lock.isSet()) {
                /*SL:393*/v6.addTransformerExclusion(a4.getName());
                /*SL:395*/this.lock.clear();
                MixinServiceLaunchWrapper.logger.info(/*EL:396*/"A re-entrant transformer '{}' was detected and will no longer process meta class data", new Object[] { a4.getName() });
            }
        }
        /*SL:401*/return v4;
    }
    
    private String unmapClassName(final String a1) {
        /*SL:405*/if (this.nameTransformer == null) {
            /*SL:406*/this.findNameTransformer();
        }
        /*SL:409*/if (this.nameTransformer != null) {
            /*SL:410*/return this.nameTransformer.unmapClassName(a1);
        }
        /*SL:413*/return a1;
    }
    
    private void findNameTransformer() {
        final List<IClassTransformer> transformers = (List<IClassTransformer>)Launch.classLoader.getTransformers();
        /*SL:418*/for (final IClassTransformer v1 : transformers) {
            /*SL:419*/if (v1 instanceof IClassNameTransformer) {
                MixinServiceLaunchWrapper.logger.debug(/*EL:420*/"Found name transformer: {}", new Object[] { v1.getClass().getName() });
                /*SL:421*/this.nameTransformer = (IClassNameTransformer)v1;
            }
        }
    }
    
    @Override
    public ClassNode getClassNode(final String a1) throws ClassNotFoundException, IOException {
        /*SL:432*/return this.getClassNode(this.getClassBytes(a1, true), 0);
    }
    
    private ClassNode getClassNode(final byte[] a1, final int a2) {
        final ClassNode v1 = /*EL:443*/new ClassNode();
        final ClassReader v2 = /*EL:444*/new ClassReader(a1);
        /*SL:445*/v2.accept(v1, a2);
        /*SL:446*/return v1;
    }
    
    @Override
    public final String getSideName() {
        /*SL:456*/for (final ITweaker v1 : GlobalProperties.<List<ITweaker>>get("Tweaks")) {
            /*SL:457*/if (v1.getClass().getName().endsWith(".common.launcher.FMLServerTweaker")) {
                /*SL:458*/return "SERVER";
            }
            /*SL:459*/if (v1.getClass().getName().endsWith(".common.launcher.FMLTweaker")) {
                /*SL:460*/return "CLIENT";
            }
        }
        String v2 = /*EL:464*/this.getSideName("net.minecraftforge.fml.relauncher.FMLLaunchHandler", "side");
        /*SL:465*/if (v2 != null) {
            /*SL:466*/return v2;
        }
        /*SL:469*/v2 = this.getSideName("cpw.mods.fml.relauncher.FMLLaunchHandler", "side");
        /*SL:470*/if (v2 != null) {
            /*SL:471*/return v2;
        }
        /*SL:474*/v2 = this.getSideName("com.mumfrey.liteloader.launch.LiteLoaderTweaker", "getEnvironmentType");
        /*SL:475*/if (v2 != null) {
            /*SL:476*/return v2;
        }
        /*SL:479*/return "UNKNOWN";
    }
    
    private String getSideName(final String v-1, final String v0) {
        try {
            final Class<?> a1 = /*EL:484*/Class.forName(v-1, false, (ClassLoader)Launch.classLoader);
            final Method a2 = /*EL:485*/a1.getDeclaredMethod(v0, (Class<?>[])new Class[0]);
            /*SL:486*/return ((Enum)a2.invoke(null, new Object[0])).name();
        }
        catch (Exception v) {
            /*SL:488*/return null;
        }
    }
    
    private static int findInStackTrace(final String a2, final String v1) {
        final Thread v2 = /*EL:493*/Thread.currentThread();
        /*SL:495*/if (!"main".equals(v2.getName())) {
            /*SL:496*/return 0;
        }
        final StackTraceElement[] stackTrace;
        final StackTraceElement[] v3 = /*EL:500*/stackTrace = v2.getStackTrace();
        for (final StackTraceElement a3 : stackTrace) {
            /*SL:501*/if (a2.equals(a3.getClassName()) && v1.equals(a3.getMethodName())) {
                /*SL:502*/return a3.getLineNumber();
            }
        }
        /*SL:506*/return 0;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}
