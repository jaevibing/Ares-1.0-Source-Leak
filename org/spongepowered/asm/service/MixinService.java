package org.spongepowered.asm.service;

import org.apache.logging.log4j.LogManager;
import java.util.ServiceConfigurationError;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.ServiceLoader;
import org.apache.logging.log4j.Logger;

public final class MixinService
{
    private static final Logger logger;
    private static MixinService instance;
    private ServiceLoader<IMixinServiceBootstrap> bootstrapServiceLoader;
    private final Set<String> bootedServices;
    private ServiceLoader<IMixinService> serviceLoader;
    private IMixinService service;
    
    private MixinService() {
        this.bootedServices = new HashSet<String>();
        this.service = null;
        this.runBootServices();
    }
    
    private void runBootServices() {
        /*SL:76*/this.bootstrapServiceLoader = ServiceLoader.<IMixinServiceBootstrap>load(IMixinServiceBootstrap.class, this.getClass().getClassLoader());
        /*SL:77*/for (final IMixinServiceBootstrap v0 : this.bootstrapServiceLoader) {
            try {
                /*SL:79*/v0.bootstrap();
                /*SL:80*/this.bootedServices.add(v0.getServiceClassName());
            }
            catch (Throwable v) {
                MixinService.logger.catching(/*EL:82*/v);
            }
        }
    }
    
    private static MixinService getInstance() {
        /*SL:91*/if (MixinService.instance == null) {
            MixinService.instance = /*EL:92*/new MixinService();
        }
        /*SL:95*/return MixinService.instance;
    }
    
    public static void boot() {
        getInstance();
    }
    
    public static IMixinService getService() {
        /*SL:106*/return getInstance().getServiceInstance();
    }
    
    private synchronized IMixinService getServiceInstance() {
        /*SL:110*/if (this.service == null) {
            /*SL:111*/this.service = this.initService();
            /*SL:112*/if (this.service == null) {
                /*SL:113*/throw new ServiceNotAvailableError("No mixin host service is available");
            }
        }
        /*SL:116*/return this.service;
    }
    
    private IMixinService initService() {
        /*SL:120*/this.serviceLoader = ServiceLoader.<IMixinService>load(IMixinService.class, this.getClass().getClassLoader());
        final Iterator<IMixinService> v0 = /*EL:121*/this.serviceLoader.iterator();
        /*SL:122*/while (v0.hasNext()) {
            try {
                final IMixinService v = /*EL:124*/v0.next();
                /*SL:125*/if (this.bootedServices.contains(v.getClass().getName())) {
                    MixinService.logger.debug(/*EL:126*/"MixinService [{}] was successfully booted in {}", new Object[] { v.getName(), this.getClass().getClassLoader() });
                }
                /*SL:128*/if (v.isValid()) {
                    /*SL:129*/return v;
                }
                /*SL:135*/continue;
            }
            catch (ServiceConfigurationError v2) {
                v2.printStackTrace();
            }
            catch (Throwable v3) {
                v3.printStackTrace();
            }
        }
        /*SL:137*/return null;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}
