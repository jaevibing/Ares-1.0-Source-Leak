package org.spongepowered.tools.agent;

import org.spongepowered.asm.service.IMixinService;
import java.lang.instrument.ClassDefinition;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.lang.instrument.ClassFileTransformer;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import java.util.List;
import java.lang.instrument.Instrumentation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;

public class MixinAgent implements IHotSwap
{
    public static final byte[] ERROR_BYTECODE;
    static final MixinAgentClassLoader classLoader;
    static final Logger logger;
    static Instrumentation instrumentation;
    private static List<MixinAgent> agents;
    final MixinTransformer classTransformer;
    
    public MixinAgent(final MixinTransformer a1) {
        this.classTransformer = a1;
        MixinAgent.agents.add(this);
        if (MixinAgent.instrumentation != null) {
            this.initTransformer();
        }
    }
    
    private void initTransformer() {
        MixinAgent.instrumentation.addTransformer(/*EL:170*/new Transformer(), true);
    }
    
    @Override
    public void registerMixinClass(final String a1) {
        MixinAgent.classLoader.addMixinClass(/*EL:175*/a1);
    }
    
    @Override
    public void registerTargetClass(final String a1, final byte[] a2) {
        MixinAgent.classLoader.addTargetClass(/*EL:180*/a1, a2);
    }
    
    public static void init(final Instrumentation v1) {
        MixinAgent.instrumentation = /*EL:190*/v1;
        /*SL:191*/if (!MixinAgent.instrumentation.isRedefineClassesSupported()) {
            MixinAgent.logger.error(/*EL:192*/"The instrumentation doesn't support re-definition of classes");
        }
        /*SL:194*/for (final MixinAgent a1 : MixinAgent.agents) {
            /*SL:195*/a1.initTransformer();
        }
    }
    
    public static void premain(final String a1, final Instrumentation a2) {
        /*SL:209*/System.setProperty("mixin.hotSwap", "true");
        init(/*EL:210*/a2);
    }
    
    public static void agentmain(final String a1, final Instrumentation a2) {
        init(/*EL:223*/a2);
    }
    
    static {
        ERROR_BYTECODE = new byte[] { 1 };
        classLoader = new MixinAgentClassLoader();
        logger = LogManager.getLogger("mixin.agent");
        MixinAgent.instrumentation = null;
        MixinAgent.agents = new ArrayList<MixinAgent>();
    }
    
    class Transformer implements ClassFileTransformer
    {
        @Override
        public byte[] transform(final ClassLoader a4, final String a5, final Class<?> v1, final ProtectionDomain v2, final byte[] v3) throws IllegalClassFormatException {
            /*SL:58*/if (v1 == null) {
                /*SL:59*/return null;
            }
            final byte[] v4 = MixinAgent.classLoader.getFakeMixinBytecode(/*EL:62*/v1);
            /*SL:63*/if (v4 != null) {
                final List<String> a6 = /*EL:64*/this.reloadMixin(a5, v3);
                /*SL:65*/if (a6 == null || !this.reApplyMixins(a6)) {
                    /*SL:66*/return MixinAgent.ERROR_BYTECODE;
                }
                /*SL:69*/return v4;
            }
            else {
                try {
                    MixinAgent.logger.info(/*EL:73*/"Redefining class " + a5);
                    /*SL:74*/return MixinAgent.this.classTransformer.transformClassBytes(null, a5, v3);
                }
                catch (Throwable a7) {
                    MixinAgent.logger.error(/*EL:76*/"Error while re-transforming class " + a5, a7);
                    /*SL:77*/return MixinAgent.ERROR_BYTECODE;
                }
            }
        }
        
        private List<String> reloadMixin(final String v2, final byte[] v3) {
            MixinAgent.logger.info(/*EL:82*/"Redefining mixin {}", new Object[] { v2 });
            try {
                /*SL:84*/return MixinAgent.this.classTransformer.reload(v2.replace('/', '.'), v3);
            }
            catch (MixinReloadException a1) {
                MixinAgent.logger.error(/*EL:86*/"Mixin {} cannot be reloaded, needs a restart to be applied: {} ", new Object[] { a1.getMixinInfo(), a1.getMessage() });
            }
            catch (Throwable a2) {
                MixinAgent.logger.error(/*EL:89*/"Error while finding targets for mixin " + v2, a2);
            }
            /*SL:91*/return null;
        }
        
        private boolean reApplyMixins(final List<String> v-5) {
            final IMixinService service = /*EL:102*/MixinService.getService();
            /*SL:104*/for (final String s : v-5) {
                final String replace = /*EL:105*/s.replace('/', '.');
                MixinAgent.logger.debug(/*EL:106*/"Re-transforming target class {}", new Object[] { s });
                try {
                    final Class<?> a1 = /*EL:108*/service.getClassProvider().findClass(replace);
                    byte[] v1 = MixinAgent.classLoader.getOriginalTargetBytecode(/*EL:109*/replace);
                    /*SL:110*/if (v1 == null) {
                        MixinAgent.logger.error(/*EL:111*/"Target class {} bytecode is not registered", new Object[] { replace });
                        /*SL:112*/return false;
                    }
                    /*SL:114*/v1 = MixinAgent.this.classTransformer.transformClassBytes(null, replace, v1);
                    MixinAgent.instrumentation.redefineClasses(/*EL:115*/new ClassDefinition(a1, v1));
                }
                catch (Throwable v2) {
                    MixinAgent.logger.error(/*EL:117*/"Error while re-transforming target class " + s, v2);
                    /*SL:118*/return false;
                }
            }
            /*SL:121*/return true;
        }
    }
}
