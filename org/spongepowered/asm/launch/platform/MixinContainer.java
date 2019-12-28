package org.spongepowered.asm.launch.platform;

import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.launch.GlobalProperties;
import java.util.Collection;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import java.net.URI;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class MixinContainer
{
    private static final List<String> agentClasses;
    private final Logger logger;
    private final URI uri;
    private final List<IMixinPlatformAgent> agents;
    
    public MixinContainer(final MixinPlatformManager v-5, final URI v-4) {
        this.logger = LogManager.getLogger("mixin");
        this.agents = new ArrayList<IMixinPlatformAgent>();
        this.uri = v-4;
        for (final String s : MixinContainer.agentClasses) {
            try {
                final Class<IMixinPlatformAgent> a1 = (Class<IMixinPlatformAgent>)Class.forName(s);
                final Constructor<IMixinPlatformAgent> a2 = a1.getDeclaredConstructor(MixinPlatformManager.class, URI.class);
                this.logger.debug("Instancing new {} for {}", new Object[] { a1.getSimpleName(), this.uri });
                final IMixinPlatformAgent v1 = a2.newInstance(v-5, v-4);
                this.agents.add(v1);
            }
            catch (Exception ex) {
                this.logger.catching((Throwable)ex);
            }
        }
    }
    
    public URI getURI() {
        /*SL:80*/return this.uri;
    }
    
    public Collection<String> getPhaseProviders() {
        final List<String> list = /*EL:87*/new ArrayList<String>();
        /*SL:88*/for (final IMixinPlatformAgent v0 : this.agents) {
            final String v = /*EL:89*/v0.getPhaseProvider();
            /*SL:90*/if (v != null) {
                /*SL:91*/list.add(v);
            }
        }
        /*SL:94*/return list;
    }
    
    public void prepare() {
        /*SL:101*/for (final IMixinPlatformAgent v1 : this.agents) {
            /*SL:102*/this.logger.debug("Processing prepare() for {}", new Object[] { v1 });
            /*SL:103*/v1.prepare();
        }
    }
    
    public void initPrimaryContainer() {
        /*SL:112*/for (final IMixinPlatformAgent v1 : this.agents) {
            /*SL:113*/this.logger.debug("Processing launch tasks for {}", new Object[] { v1 });
            /*SL:114*/v1.initPrimaryContainer();
        }
    }
    
    public void inject() {
        /*SL:122*/for (final IMixinPlatformAgent v1 : this.agents) {
            /*SL:123*/this.logger.debug("Processing inject() for {}", new Object[] { v1 });
            /*SL:124*/v1.inject();
        }
    }
    
    public String getLaunchTarget() {
        /*SL:136*/for (final IMixinPlatformAgent v0 : this.agents) {
            final String v = /*EL:137*/v0.getLaunchTarget();
            /*SL:138*/if (v != null) {
                /*SL:139*/return v;
            }
        }
        /*SL:142*/return null;
    }
    
    static {
        GlobalProperties.put("mixin.agents", agentClasses = new ArrayList<String>());
        for (final String v1 : MixinService.getService().getPlatformAgents()) {
            MixinContainer.agentClasses.add(v1);
        }
        MixinContainer.agentClasses.add("org.spongepowered.asm.launch.platform.MixinPlatformAgentDefault");
    }
}
