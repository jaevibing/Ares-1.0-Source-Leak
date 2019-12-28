package org.spongepowered.tools.obfuscation;

import java.util.Collection;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.service.ObfuscationServices;
import java.util.ArrayList;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;
import java.util.List;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;

public class ObfuscationManager implements IObfuscationManager
{
    private final IMixinAnnotationProcessor ap;
    private final List<ObfuscationEnvironment> environments;
    private final IObfuscationDataProvider obfs;
    private final IReferenceManager refs;
    private final List<IMappingConsumer> consumers;
    private boolean initDone;
    
    public ObfuscationManager(final IMixinAnnotationProcessor a1) {
        this.environments = new ArrayList<ObfuscationEnvironment>();
        this.consumers = new ArrayList<IMappingConsumer>();
        this.ap = a1;
        this.obfs = new ObfuscationDataProvider(a1, this.environments);
        this.refs = new ReferenceManager(a1, this.environments);
    }
    
    @Override
    public void init() {
        /*SL:77*/if (this.initDone) {
            /*SL:78*/return;
        }
        /*SL:80*/this.initDone = true;
        /*SL:81*/ObfuscationServices.getInstance().initProviders(this.ap);
        /*SL:82*/for (final ObfuscationType v1 : ObfuscationType.types()) {
            /*SL:83*/if (v1.isSupported()) {
                /*SL:84*/this.environments.add(v1.createEnvironment());
            }
        }
    }
    
    @Override
    public IObfuscationDataProvider getDataProvider() {
        /*SL:91*/return this.obfs;
    }
    
    @Override
    public IReferenceManager getReferenceManager() {
        /*SL:96*/return this.refs;
    }
    
    @Override
    public IMappingConsumer createMappingConsumer() {
        final Mappings v1 = /*EL:101*/new Mappings();
        /*SL:102*/this.consumers.add(v1);
        /*SL:103*/return v1;
    }
    
    @Override
    public List<ObfuscationEnvironment> getEnvironments() {
        /*SL:108*/return this.environments;
    }
    
    @Override
    public void writeMappings() {
        /*SL:116*/for (final ObfuscationEnvironment v1 : this.environments) {
            /*SL:117*/v1.writeMappings(this.consumers);
        }
    }
    
    @Override
    public void writeReferences() {
        /*SL:126*/this.refs.write();
    }
}
