package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
import org.spongepowered.asm.mixin.throwables.MixinPrepareError;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import java.util.Collections;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.SortedSet;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.throwables.MixinApplyError;
import java.util.TreeSet;
import java.lang.reflect.Method;
import org.spongepowered.asm.util.PrettyPrinter;
import java.util.Map;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;
import org.spongepowered.asm.mixin.throwables.ClassAlreadyLoadedException;
import org.apache.logging.log4j.LogManager;
import java.util.Collection;
import java.util.HashSet;
import java.lang.reflect.Constructor;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckInterfaces;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionClassExporter;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgsClassGenerator;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.service.ITransformer;
import java.util.UUID;
import java.util.ArrayList;
import org.spongepowered.asm.service.MixinService;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.util.ReEntranceLock;
import java.util.List;
import org.spongepowered.asm.service.IMixinService;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.transformers.TreeTransformer;

public class MixinTransformer extends TreeTransformer
{
    private static final String MIXIN_AGENT_CLASS = "org.spongepowered.tools.agent.MixinAgent";
    private static final String METRONOME_AGENT_CLASS = "org.spongepowered.metronome.Agent";
    static final Logger logger;
    private final IMixinService service;
    private final List<MixinConfig> configs;
    private final List<MixinConfig> pendingConfigs;
    private final ReEntranceLock lock;
    private final String sessionId;
    private final Extensions extensions;
    private final IHotSwap hotSwapper;
    private final MixinPostProcessor postProcessor;
    private final Profiler profiler;
    private MixinEnvironment currentEnvironment;
    private Level verboseLoggingLevel;
    private boolean errorState;
    private int transformedCount;
    
    MixinTransformer() {
        this.service = MixinService.getService();
        this.configs = new ArrayList<MixinConfig>();
        this.pendingConfigs = new ArrayList<MixinConfig>();
        this.sessionId = UUID.randomUUID().toString();
        this.verboseLoggingLevel = Level.DEBUG;
        this.errorState = false;
        this.transformedCount = 0;
        final MixinEnvironment v1 = MixinEnvironment.getCurrentEnvironment();
        final Object v2 = v1.getActiveTransformer();
        if (v2 instanceof ITransformer) {
            throw new MixinException("Terminating MixinTransformer instance " + this);
        }
        v1.setActiveTransformer(this);
        this.lock = this.service.getReEntranceLock();
        this.extensions = new Extensions(this);
        this.hotSwapper = this.initHotSwapper(v1);
        this.postProcessor = new MixinPostProcessor();
        this.extensions.add(new ArgsClassGenerator());
        this.extensions.add(new InnerClassGenerator());
        this.extensions.add(new ExtensionClassExporter(v1));
        this.extensions.add(new ExtensionCheckClass());
        this.extensions.add(new ExtensionCheckInterfaces());
        this.profiler = MixinEnvironment.getProfiler();
    }
    
    private IHotSwap initHotSwapper(final MixinEnvironment v-1) {
        /*SL:249*/if (!v-1.getOption(MixinEnvironment.Option.HOT_SWAP)) {
            /*SL:250*/return null;
        }
        try {
            MixinTransformer.logger.info(/*EL:254*/"Attempting to load Hot-Swap agent");
            final Class<? extends IHotSwap> a1 = /*EL:257*/(Class<? extends IHotSwap>)Class.forName("org.spongepowered.tools.agent.MixinAgent");
            final Constructor<? extends IHotSwap> v1 = /*EL:258*/a1.getDeclaredConstructor(MixinTransformer.class);
            /*SL:259*/return (IHotSwap)v1.newInstance(this);
        }
        catch (Throwable v2) {
            MixinTransformer.logger.info(/*EL:261*/"Hot-swap agent could not be loaded, hot swapping of mixins won't work. {}: {}", new Object[] { v2.getClass().getSimpleName(), /*EL:262*/v2.getMessage() });
            /*SL:265*/return null;
        }
    }
    
    public void audit(final MixinEnvironment v-4) {
        final Set<String> set = /*EL:274*/new HashSet<String>();
        /*SL:276*/for (final MixinConfig a1 : this.configs) {
            /*SL:277*/set.addAll(a1.getUnhandledTargets());
        }
        final Logger logger = /*EL:280*/LogManager.getLogger("mixin/audit");
        /*SL:282*/for (final String v0 : set) {
            try {
                /*SL:284*/logger.info("Force-loading class {}", new Object[] { v0 });
                /*SL:285*/this.service.getClassProvider().findClass(v0, true);
            }
            catch (ClassNotFoundException v) {
                /*SL:287*/logger.error("Could not force-load " + v0, (Throwable)v);
            }
        }
        /*SL:291*/for (final MixinConfig v2 : this.configs) {
            /*SL:292*/for (final String v3 : v2.getUnhandledTargets()) {
                final ClassAlreadyLoadedException v4 = /*EL:293*/new ClassAlreadyLoadedException(v3 + " was already classloaded");
                /*SL:294*/logger.error("Could not force-load " + v3, (Throwable)v4);
            }
        }
        /*SL:298*/if (v-4.getOption(MixinEnvironment.Option.DEBUG_PROFILER)) {
            /*SL:299*/this.printProfilerSummary();
        }
    }
    
    private void printProfilerSummary() {
        final DecimalFormat decimalFormat = /*EL:304*/new DecimalFormat("(###0.000");
        final DecimalFormat decimalFormat2 = /*EL:305*/new DecimalFormat("(###0.0");
        final PrettyPrinter printer = /*EL:306*/this.profiler.printer(false, false);
        final long totalTime = /*EL:308*/this.profiler.get("mixin.prepare").getTotalTime();
        final long totalTime2 = /*EL:309*/this.profiler.get("mixin.read").getTotalTime();
        final long totalTime3 = /*EL:310*/this.profiler.get("mixin.apply").getTotalTime();
        final long totalTime4 = /*EL:311*/this.profiler.get("mixin.write").getTotalTime();
        final long totalTime5 = /*EL:312*/this.profiler.get("mixin").getTotalTime();
        final long totalTime6 = /*EL:314*/this.profiler.get("class.load").getTotalTime();
        final long totalTime7 = /*EL:315*/this.profiler.get("class.transform").getTotalTime();
        final long totalTime8 = /*EL:316*/this.profiler.get("mixin.debug.export").getTotalTime();
        final long n = /*EL:317*/totalTime5 - totalTime6 - totalTime7 - totalTime8;
        final double n2 = /*EL:318*/n / totalTime5 * 100.0;
        final double n3 = /*EL:319*/totalTime6 / totalTime5 * 100.0;
        final double n4 = /*EL:320*/totalTime7 / totalTime5 * 100.0;
        final double n5 = /*EL:321*/totalTime8 / totalTime5 * 100.0;
        long n6 = /*EL:323*/0L;
        Profiler.Section section = /*EL:324*/null;
        /*SL:326*/for (final Profiler.Section v0 : this.profiler.getSections()) {
            final long v = /*EL:327*/v0.getName().startsWith("class.transform.") ? v0.getTotalTime() : 0L;
            /*SL:328*/if (v > n6) {
                /*SL:329*/n6 = v;
                /*SL:330*/section = v0;
            }
        }
        /*SL:334*/printer.hr().add("Summary").hr().add();
        final String a2 = /*EL:336*/"%9d ms %12s seconds)";
        /*SL:337*/printer.kv("Total mixin time", a2, totalTime5, decimalFormat.format(totalTime5 * 0.001)).add();
        /*SL:338*/printer.kv("Preparing mixins", a2, totalTime, decimalFormat.format(totalTime * 0.001));
        /*SL:339*/printer.kv("Reading input", a2, totalTime2, decimalFormat.format(totalTime2 * 0.001));
        /*SL:340*/printer.kv("Applying mixins", a2, totalTime3, decimalFormat.format(totalTime3 * 0.001));
        /*SL:341*/printer.kv("Writing output", a2, totalTime4, decimalFormat.format(totalTime4 * 0.001)).add();
        /*SL:343*/printer.kv("of which", (Object)"");
        /*SL:344*/printer.kv("Time spent loading from disk", a2, totalTime6, decimalFormat.format(totalTime6 * 0.001));
        /*SL:345*/printer.kv("Time spent transforming classes", a2, totalTime7, decimalFormat.format(totalTime7 * 0.001)).add();
        /*SL:347*/if (section != null) {
            /*SL:348*/printer.kv("Worst transformer", (Object)section.getName());
            /*SL:349*/printer.kv("Class", (Object)section.getInfo());
            /*SL:350*/printer.kv("Time spent", "%s seconds", section.getTotalSeconds());
            /*SL:351*/printer.kv("called", "%d times", section.getTotalCount()).add();
        }
        /*SL:354*/printer.kv("   Time allocation:     Processing mixins", "%9d ms %10s%% of total)", n, decimalFormat2.format(n2));
        /*SL:355*/printer.kv("Loading classes", "%9d ms %10s%% of total)", totalTime6, decimalFormat2.format(n3));
        /*SL:356*/printer.kv("Running transformers", "%9d ms %10s%% of total)", totalTime7, decimalFormat2.format(n4));
        /*SL:357*/if (totalTime8 > 0L) {
            /*SL:358*/printer.kv("Exporting classes (debug)", "%9d ms %10s%% of total)", totalTime8, decimalFormat2.format(n5));
        }
        /*SL:360*/printer.add();
        try {
            final Class<?> v2 = /*EL:363*/this.service.getClassProvider().findAgentClass("org.spongepowered.metronome.Agent", false);
            final Method v3 = /*EL:364*/v2.getDeclaredMethod("getTimes", (Class<?>[])new Class[0]);
            final Map<String, Long> v4 = /*EL:367*/(Map<String, Long>)v3.invoke(null, new Object[0]);
            /*SL:369*/printer.hr().add("Transformer Times").hr().add();
            int v5 = /*EL:371*/10;
            /*SL:372*/for (final Map.Entry<String, Long> v6 : v4.entrySet()) {
                /*SL:373*/v5 = Math.max(v5, v6.getKey().length());
            }
            /*SL:376*/for (final Map.Entry<String, Long> v6 : v4.entrySet()) {
                final String v7 = /*EL:377*/v6.getKey();
                long v8 = /*EL:378*/0L;
                /*SL:379*/for (final Profiler.Section v9 : this.profiler.getSections()) {
                    /*SL:380*/if (v7.equals(v9.getInfo())) {
                        /*SL:381*/v8 = v9.getTotalTime();
                        /*SL:382*/break;
                    }
                }
                /*SL:386*/if (v8 > 0L) {
                    /*SL:387*/printer.add("%-" + v5 + "s %8s ms %8s ms in mixin)", v7, v6.getValue() + v8, "(" + v8);
                }
                else {
                    /*SL:389*/printer.add("%-" + v5 + "s %8s ms", v7, v6.getValue());
                }
            }
            /*SL:393*/printer.add();
        }
        catch (Throwable t) {}
        /*SL:400*/printer.print();
    }
    
    @Override
    public String getName() {
        /*SL:408*/return this.getClass().getName();
    }
    
    @Override
    public boolean isDelegationExcluded() {
        /*SL:417*/return true;
    }
    
    @Override
    public synchronized byte[] transformClassBytes(final String v-5, final String v-4, byte[] v-3) {
        /*SL:426*/if (v-4 == null || this.errorState) {
            /*SL:427*/return v-3;
        }
        final MixinEnvironment currentEnvironment = /*EL:430*/MixinEnvironment.getCurrentEnvironment();
        /*SL:432*/if (v-3 == null) {
            /*SL:433*/for (Profiler.Section a2 : this.extensions.getGenerators()) {
                /*SL:434*/a2 = this.profiler.begin("generator", a2.getClass().getSimpleName().toLowerCase());
                /*SL:435*/v-3 = a2.generate(v-4);
                /*SL:436*/a2.end();
                /*SL:437*/if (v-3 != null) {
                    /*SL:438*/this.extensions.export(currentEnvironment, v-4.replace('.', '/'), false, v-3);
                    /*SL:439*/return v-3;
                }
            }
            /*SL:442*/return v-3;
        }
        final boolean check = /*EL:445*/this.lock.push().check();
        final Profiler.Section v0 = /*EL:447*/this.profiler.begin("mixin");
        /*SL:449*/if (!check) {
            try {
                /*SL:451*/this.checkSelect(currentEnvironment);
            }
            catch (Exception a3) {
                /*SL:453*/this.lock.pop();
                /*SL:454*/v0.end();
                /*SL:455*/throw new MixinException(a3);
            }
        }
        try {
            /*SL:460*/if (this.postProcessor.canTransform(v-4)) {
                final Profiler.Section v = /*EL:461*/this.profiler.begin("postprocessor");
                final byte[] v2 = /*EL:462*/this.postProcessor.transformClassBytes(v-5, v-4, v-3);
                /*SL:463*/v.end();
                /*SL:464*/this.extensions.export(currentEnvironment, v-4, false, v2);
                /*SL:465*/return v2;
            }
            SortedSet<MixinInfo> a4 = /*EL:468*/null;
            boolean v3 = /*EL:469*/false;
            /*SL:471*/for (final MixinConfig v4 : this.configs) {
                /*SL:472*/if (v4.packageMatch(v-4)) {
                    /*SL:473*/v3 = true;
                }
                else {
                    /*SL:477*/if (!v4.hasMixinsFor(v-4)) {
                        continue;
                    }
                    /*SL:478*/if (a4 == null) {
                        /*SL:479*/a4 = new TreeSet<MixinInfo>();
                    }
                    /*SL:483*/a4.addAll((Collection<?>)v4.getMixinsFor(v-4));
                }
            }
            /*SL:487*/if (v3) {
                /*SL:488*/throw new NoClassDefFoundError(String.format("%s is a mixin class and cannot be referenced directly", v-4));
            }
            /*SL:491*/if (a4 != null) {
                /*SL:493*/if (check) {
                    MixinTransformer.logger.warn(/*EL:494*/"Re-entrance detected, this will cause serious problems.", (Throwable)new MixinException());
                    /*SL:495*/throw new MixinApplyError("Re-entrance error.");
                }
                /*SL:498*/if (this.hotSwapper != null) {
                    /*SL:499*/this.hotSwapper.registerTargetClass(v-4, v-3);
                }
                try {
                    final Profiler.Section v5 = /*EL:504*/this.profiler.begin("read");
                    final ClassNode v6 = /*EL:505*/this.readClass(v-3, true);
                    final TargetClassContext v7 = /*EL:506*/new TargetClassContext(currentEnvironment, this.extensions, this.sessionId, v-4, v6, a4);
                    /*SL:508*/v5.end();
                    /*SL:509*/v-3 = this.applyMixins(currentEnvironment, v7);
                    /*SL:510*/++this.transformedCount;
                }
                catch (InvalidMixinException v8) {
                    /*SL:512*/this.dumpClassOnFailure(v-4, v-3, currentEnvironment);
                    /*SL:513*/this.handleMixinApplyError(v-4, v8, currentEnvironment);
                }
            }
            /*SL:517*/return v-3;
        }
        catch (Throwable v9) {
            /*SL:519*/v9.printStackTrace();
            /*SL:520*/this.dumpClassOnFailure(v-4, v-3, currentEnvironment);
            /*SL:521*/throw new MixinTransformerError("An unexpected critical error was encountered", v9);
        }
        finally {
            /*SL:523*/this.lock.pop();
            /*SL:524*/v0.end();
        }
    }
    
    public List<String> reload(final String v1, final byte[] v2) {
        /*SL:536*/if (this.lock.getDepth() > 0) {
            /*SL:537*/throw new MixinApplyError("Cannot reload mixin if re-entrant lock entered");
        }
        final List<String> v3 = /*EL:539*/new ArrayList<String>();
        /*SL:540*/for (final MixinConfig a1 : this.configs) {
            /*SL:541*/v3.addAll(a1.reloadMixin(v1, v2));
        }
        /*SL:543*/return v3;
    }
    
    private void checkSelect(final MixinEnvironment a1) {
        /*SL:547*/if (this.currentEnvironment != a1) {
            /*SL:548*/this.select(a1);
            /*SL:549*/return;
        }
        final int v1 = /*EL:552*/Mixins.getUnvisitedCount();
        /*SL:553*/if (v1 > 0 && this.transformedCount == 0) {
            /*SL:554*/this.select(a1);
        }
    }
    
    private void select(final MixinEnvironment v-9) {
        /*SL:559*/this.verboseLoggingLevel = (v-9.getOption(MixinEnvironment.Option.DEBUG_VERBOSE) ? Level.INFO : Level.DEBUG);
        /*SL:560*/if (this.transformedCount > 0) {
            MixinTransformer.logger.log(/*EL:561*/this.verboseLoggingLevel, "Ending {}, applied {} mixins", new Object[] { this.currentEnvironment, this.transformedCount });
        }
        final String s = /*EL:563*/(this.currentEnvironment == v-9) ? "Checking for additional" : "Preparing";
        MixinTransformer.logger.log(/*EL:564*/this.verboseLoggingLevel, "{} mixins for {}", new Object[] { s, v-9 });
        /*SL:566*/this.profiler.setActive(true);
        /*SL:567*/this.profiler.mark(v-9.getPhase().toString() + ":prepare");
        final Profiler.Section begin = /*EL:568*/this.profiler.begin("prepare");
        /*SL:570*/this.selectConfigs(v-9);
        /*SL:571*/this.extensions.select(v-9);
        final int prepareConfigs = /*EL:572*/this.prepareConfigs(v-9);
        /*SL:573*/this.currentEnvironment = v-9;
        /*SL:574*/this.transformedCount = 0;
        /*SL:576*/begin.end();
        final long time = /*EL:578*/begin.getTime();
        final double seconds = /*EL:579*/begin.getSeconds();
        /*SL:580*/if (seconds > 0.25) {
            final long a1 = /*EL:581*/this.profiler.get("class.load").getTime();
            final long v1 = /*EL:582*/this.profiler.get("class.transform").getTime();
            final long v2 = /*EL:583*/this.profiler.get("mixin.plugin").getTime();
            final String v3 = /*EL:584*/new DecimalFormat("###0.000").format(seconds);
            final String v4 = /*EL:585*/new DecimalFormat("###0.0").format(time / prepareConfigs);
            MixinTransformer.logger.log(/*EL:587*/this.verboseLoggingLevel, "Prepared {} mixins in {} sec ({}ms avg) ({}ms load, {}ms transform, {}ms plugin)", new Object[] { prepareConfigs, /*EL:588*/v3, v4, a1, v1, v2 });
        }
        /*SL:591*/this.profiler.mark(v-9.getPhase().toString() + ":apply");
        /*SL:592*/this.profiler.setActive(v-9.getOption(MixinEnvironment.Option.DEBUG_PROFILER));
    }
    
    private void selectConfigs(final MixinEnvironment v-2) {
        final Iterator<Config> iterator = /*EL:601*/Mixins.getConfigs().iterator();
        while (iterator.hasNext()) {
            final Config v0 = /*EL:602*/iterator.next();
            try {
                final MixinConfig a1 = /*EL:604*/v0.get();
                /*SL:605*/if (!a1.select(v-2)) {
                    continue;
                }
                /*SL:606*/iterator.remove();
                MixinTransformer.logger.log(/*EL:607*/this.verboseLoggingLevel, "Selecting config {}", new Object[] { a1 });
                /*SL:608*/a1.onSelect();
                /*SL:609*/this.pendingConfigs.add(a1);
            }
            catch (Exception v) {
                MixinTransformer.logger.warn(/*EL:612*/String.format("Failed to select mixin config: %s", v0), (Throwable)v);
            }
        }
        /*SL:616*/Collections.<MixinConfig>sort(this.pendingConfigs);
    }
    
    private int prepareConfigs(final MixinEnvironment v-4) {
        int n = /*EL:626*/0;
        final IHotSwap hotSwapper = /*EL:628*/this.hotSwapper;
        /*SL:629*/for (final MixinConfig a1 : this.pendingConfigs) {
            /*SL:630*/a1.addListener(this.postProcessor);
            /*SL:631*/if (hotSwapper != null) {
                /*SL:632*/a1.addListener(new MixinConfig.IListener() {
                    @Override
                    public void onPrepare(final MixinInfo a1) {
                        /*SL:635*//*EL:636*/hotSwapper.registerMixinClass(a1.getClassName());
                    }
                    
                    @Override
                    public void onInit(final MixinInfo a1) {
                    }
                });
            }
        }
        /*SL:644*/for (final MixinConfig v0 : this.pendingConfigs) {
            try {
                MixinTransformer.logger.log(/*EL:646*/this.verboseLoggingLevel, "Preparing {} ({})", new Object[] { v0, v0.getDeclaredMixinCount() });
                /*SL:647*/v0.prepare();
                /*SL:648*/n += v0.getMixinCount();
            }
            catch (InvalidMixinException v) {
                /*SL:650*/this.handleMixinPrepareError(v0, v, v-4);
            }
            catch (Exception v3) {
                final String v2 = /*EL:652*/v3.getMessage();
                MixinTransformer.logger.error(/*EL:653*/"Error encountered whilst initialising mixin config '" + v0.getName() + "': " + v2, (Throwable)v3);
            }
        }
        /*SL:657*/for (final MixinConfig v0 : this.pendingConfigs) {
            final IMixinConfigPlugin v4 = /*EL:658*/v0.getPlugin();
            /*SL:659*/if (v4 == null) {
                /*SL:660*/continue;
            }
            final Set<String> v5 = /*EL:663*/new HashSet<String>();
            /*SL:664*/for (final MixinConfig v6 : this.pendingConfigs) {
                /*SL:665*/if (!v6.equals(v0)) {
                    /*SL:666*/v5.addAll(v6.getTargets());
                }
            }
            /*SL:670*/v4.acceptTargets(v0.getTargets(), Collections.<String>unmodifiableSet((Set<? extends String>)v5));
        }
        /*SL:673*/for (final MixinConfig v0 : this.pendingConfigs) {
            try {
                /*SL:675*/v0.postInitialise();
            }
            catch (InvalidMixinException v) {
                /*SL:677*/this.handleMixinPrepareError(v0, v, v-4);
            }
            catch (Exception v3) {
                final String v2 = /*EL:679*/v3.getMessage();
                MixinTransformer.logger.error(/*EL:680*/"Error encountered during mixin config postInit step'" + v0.getName() + "': " + v2, (Throwable)v3);
            }
        }
        /*SL:684*/this.configs.addAll(this.pendingConfigs);
        /*SL:685*/Collections.<MixinConfig>sort(this.configs);
        /*SL:686*/this.pendingConfigs.clear();
        /*SL:688*/return n;
    }
    
    private byte[] applyMixins(final MixinEnvironment v1, final TargetClassContext v2) {
        Profiler.Section v3 = /*EL:700*/this.profiler.begin("preapply");
        /*SL:701*/this.extensions.preApply(v2);
        /*SL:702*/v3 = v3.next("apply");
        /*SL:703*/this.apply(v2);
        /*SL:704*/v3 = v3.next("postapply");
        try {
            /*SL:706*/this.extensions.postApply(v2);
        }
        catch (ExtensionCheckClass.ValidationFailedException a1) {
            MixinTransformer.logger.info(/*EL:708*/a1.getMessage());
            /*SL:710*/if (v2.isExportForced() || v1.getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                /*SL:711*/this.writeClass(v2);
            }
        }
        /*SL:714*/v3.end();
        /*SL:715*/return this.writeClass(v2);
    }
    
    private void apply(final TargetClassContext a1) {
        /*SL:724*/a1.applyMixins();
    }
    
    private void handleMixinPrepareError(final MixinConfig a1, final InvalidMixinException a2, final MixinEnvironment a3) throws MixinPrepareError {
        /*SL:728*/this.handleMixinError(a1.getName(), a2, a3, ErrorPhase.PREPARE);
    }
    
    private void handleMixinApplyError(final String a1, final InvalidMixinException a2, final MixinEnvironment a3) throws MixinApplyError {
        /*SL:732*/this.handleMixinError(a1, a2, a3, ErrorPhase.APPLY);
    }
    
    private void handleMixinError(final String a4, final InvalidMixinException v1, final MixinEnvironment v2, final ErrorPhase v3) throws Error {
        /*SL:736*/this.errorState = true;
        final IMixinInfo v4 = /*EL:738*/v1.getMixin();
        /*SL:740*/if (v4 == null) {
            MixinTransformer.logger.error(/*EL:741*/"InvalidMixinException has no mixin!", (Throwable)v1);
            /*SL:742*/throw v1;
        }
        final IMixinConfig v5 = /*EL:745*/v4.getConfig();
        final MixinEnvironment.Phase v6 = /*EL:746*/v4.getPhase();
        IMixinErrorHandler.ErrorAction v7 = /*EL:747*/v5.isRequired() ? IMixinErrorHandler.ErrorAction.ERROR : IMixinErrorHandler.ErrorAction.WARN;
        /*SL:749*/if (v2.getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            /*SL:750*/new PrettyPrinter().add("Invalid Mixin").centre().hr(/*EL:751*/'-').kvWidth(/*EL:752*/10).kv(/*EL:753*/"Action", (Object)v3.name()).kv(/*EL:754*/"Mixin", (Object)v4.getClassName()).kv(/*EL:755*/"Config", (Object)v5.getName()).kv(/*EL:756*/"Phase", v6).hr(/*EL:757*/'-').add(/*EL:758*/"    %s", v1.getClass().getName()).hr(/*EL:759*/'-').addWrapped(/*EL:760*/"    %s", v1.getMessage()).hr(/*EL:761*/'-').add(/*EL:762*/v1, 8).trace(/*EL:763*/v7.logLevel);
        }
        /*SL:767*/for (final IMixinErrorHandler a5 : this.getErrorHandlers(v4.getPhase())) {
            final IMixinErrorHandler.ErrorAction a6 = /*EL:768*/v3.onError(a5, a4, v1, v4, v7);
            /*SL:769*/if (a6 != null) {
                /*SL:770*/v7 = a6;
            }
        }
        MixinTransformer.logger.log(/*EL:774*/v7.logLevel, v3.getLogMessage(a4, v1, v4), (Throwable)v1);
        /*SL:776*/this.errorState = false;
        /*SL:778*/if (v7 == IMixinErrorHandler.ErrorAction.ERROR) {
            /*SL:779*/throw new MixinApplyError(v3.getErrorMessage(v4, v5, v6), v1);
        }
    }
    
    private List<IMixinErrorHandler> getErrorHandlers(final MixinEnvironment.Phase v-4) {
        final List<IMixinErrorHandler> list = /*EL:784*/new ArrayList<IMixinErrorHandler>();
        /*SL:786*/for (final String s : Mixins.getErrorHandlerClasses()) {
            try {
                MixinTransformer.logger.info(/*EL:788*/"Instancing error handler class {}", new Object[] { s });
                final Class<?> a1 = /*EL:789*/this.service.getClassProvider().findClass(s, true);
                final IMixinErrorHandler v1 = /*EL:790*/(IMixinErrorHandler)a1.newInstance();
                /*SL:791*/if (v1 == null) {
                    continue;
                }
                /*SL:792*/list.add(v1);
            }
            catch (Throwable t) {}
        }
        /*SL:799*/return list;
    }
    
    private byte[] writeClass(final TargetClassContext a1) {
        /*SL:803*/return this.writeClass(a1.getClassName(), a1.getClassNode(), a1.isExportForced());
    }
    
    private byte[] writeClass(final String a1, final ClassNode a2, final boolean a3) {
        final Profiler.Section v1 = /*EL:808*/this.profiler.begin("write");
        final byte[] v2 = /*EL:809*/this.writeClass(a2);
        /*SL:810*/v1.end();
        /*SL:811*/this.extensions.export(this.currentEnvironment, a1, a3, v2);
        /*SL:812*/return v2;
    }
    
    private void dumpClassOnFailure(final String a3, final byte[] v1, final MixinEnvironment v2) {
        /*SL:816*/if (v2.getOption(MixinEnvironment.Option.DUMP_TARGET_ON_FAILURE)) {
            final ExtensionClassExporter a4 = /*EL:817*/(ExtensionClassExporter)this.extensions.getExtension((Class)ExtensionClassExporter.class);
            /*SL:818*/a4.dumpClass(a3.replace('.', '/') + ".target", v1);
        }
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    enum ErrorPhase
    {
        PREPARE {
            @Override
            IMixinErrorHandler.ErrorAction onError(final IMixinErrorHandler a3, final String a4, final InvalidMixinException a5, final IMixinInfo v1, final IMixinErrorHandler.ErrorAction v2) {
                try {
                    /*SL:88*/return a3.onPrepareError(v1.getConfig(), a5, v1, v2);
                }
                catch (AbstractMethodError a6) {
                    /*SL:91*/return v2;
                }
            }
            
            @Override
            protected String getContext(final IMixinInfo a1, final String a2) {
                /*SL:97*/return String.format("preparing %s in %s", a1.getName(), a2);
            }
        }, 
        APPLY {
            @Override
            IMixinErrorHandler.ErrorAction onError(final IMixinErrorHandler a3, final String a4, final InvalidMixinException a5, final IMixinInfo v1, final IMixinErrorHandler.ErrorAction v2) {
                try {
                    /*SL:107*/return a3.onApplyError(a4, a5, v1, v2);
                }
                catch (AbstractMethodError a6) {
                    /*SL:110*/return v2;
                }
            }
            
            @Override
            protected String getContext(final IMixinInfo a1, final String a2) {
                /*SL:116*/return String.format("%s -> %s", a1, a2);
            }
        };
        
        private final String text;
        
        private ErrorPhase(final int n) {
            this.text = this.name().toLowerCase();
        }
        
        abstract IMixinErrorHandler.ErrorAction onError(final IMixinErrorHandler p0, final String p1, final InvalidMixinException p2, final IMixinInfo p3, final IMixinErrorHandler.ErrorAction p4);
        
        protected abstract String getContext(final IMixinInfo p0, final String p1);
        
        public String getLogMessage(final String a1, final InvalidMixinException a2, final IMixinInfo a3) {
            /*SL:134*/return String.format("Mixin %s failed %s: %s %s", this.text, this.getContext(a3, a1), a2.getClass().getName(), a2.getMessage());
        }
        
        public String getErrorMessage(final IMixinInfo a1, final IMixinConfig a2, final MixinEnvironment.Phase a3) {
            /*SL:138*/return String.format("Mixin [%s] from phase [%s] in config [%s] FAILED during %s", a1, a3, a2, this.name());
        }
    }
}
