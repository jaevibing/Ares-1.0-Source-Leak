package org.spongepowered.tools.obfuscation.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.ObfuscationType;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import java.util.HashSet;
import java.util.Set;
import java.util.ServiceLoader;

public final class ObfuscationServices
{
    private static ObfuscationServices instance;
    private final ServiceLoader<IObfuscationService> serviceLoader;
    private final Set<IObfuscationService> services;
    
    private ObfuscationServices() {
        this.services = new HashSet<IObfuscationService>();
        this.serviceLoader = ServiceLoader.<IObfuscationService>load(IObfuscationService.class, this.getClass().getClassLoader());
    }
    
    public static ObfuscationServices getInstance() {
        /*SL:69*/if (ObfuscationServices.instance == null) {
            ObfuscationServices.instance = /*EL:70*/new ObfuscationServices();
        }
        /*SL:72*/return ObfuscationServices.instance;
    }
    
    public void initProviders(final IMixinAnnotationProcessor v-6) {
        try {
            /*SL:82*/for (final IObfuscationService obfuscationService : this.serviceLoader) {
                /*SL:83*/if (!this.services.contains(obfuscationService)) {
                    /*SL:84*/this.services.add(obfuscationService);
                    final String simpleName = /*EL:86*/obfuscationService.getClass().getSimpleName();
                    final Collection<ObfuscationTypeDescriptor> obfuscationTypes = /*EL:88*/obfuscationService.getObfuscationTypes();
                    /*SL:89*/if (obfuscationTypes == null) {
                        continue;
                    }
                    /*SL:90*/for (final ObfuscationTypeDescriptor v0 : obfuscationTypes) {
                        try {
                            final ObfuscationType a1 = /*EL:92*/ObfuscationType.create(v0, v-6);
                            /*SL:93*/v-6.printMessage(Diagnostic.Kind.NOTE, simpleName + " supports type: \"" + a1 + "\"");
                        }
                        catch (Exception v) {
                            /*SL:95*/v.printStackTrace();
                        }
                    }
                }
            }
        }
        catch (ServiceConfigurationError serviceConfigurationError) {
            /*SL:102*/v-6.printMessage(Diagnostic.Kind.ERROR, serviceConfigurationError.getClass().getSimpleName() + ": " + serviceConfigurationError.getMessage());
            /*SL:103*/serviceConfigurationError.printStackTrace();
        }
    }
    
    public Set<String> getSupportedOptions() {
        final Set<String> set = /*EL:111*/new HashSet<String>();
        /*SL:112*/for (final IObfuscationService v0 : this.serviceLoader) {
            final Set<String> v = /*EL:113*/v0.getSupportedOptions();
            /*SL:114*/if (v != null) {
                /*SL:115*/set.addAll(v);
            }
        }
        /*SL:118*/return set;
    }
    
    public IObfuscationService getService(final Class<? extends IObfuscationService> v2) {
        /*SL:128*/for (final IObfuscationService a1 : this.serviceLoader) {
            /*SL:129*/if (v2.getName().equals(a1.getClass().getName())) {
                /*SL:130*/return a1;
            }
        }
        /*SL:133*/return null;
    }
}
