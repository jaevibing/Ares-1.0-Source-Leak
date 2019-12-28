package org.spongepowered.asm.mixin.transformer.ext;

import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;

public final class Extensions
{
    private final MixinTransformer transformer;
    private final List<IExtension> extensions;
    private final Map<Class<? extends IExtension>, IExtension> extensionMap;
    private final List<IClassGenerator> generators;
    private final List<IClassGenerator> generatorsView;
    private final Map<Class<? extends IClassGenerator>, IClassGenerator> generatorMap;
    private List<IExtension> activeExtensions;
    
    public Extensions(final MixinTransformer a1) {
        this.extensions = new ArrayList<IExtension>();
        this.extensionMap = new HashMap<Class<? extends IExtension>, IExtension>();
        this.generators = new ArrayList<IClassGenerator>();
        this.generatorsView = Collections.<IClassGenerator>unmodifiableList((List<? extends IClassGenerator>)this.generators);
        this.generatorMap = new HashMap<Class<? extends IClassGenerator>, IClassGenerator>();
        this.activeExtensions = Collections.<IExtension>emptyList();
        this.transformer = a1;
    }
    
    public MixinTransformer getTransformer() {
        /*SL:88*/return this.transformer;
    }
    
    public void add(final IExtension a1) {
        /*SL:97*/this.extensions.add(a1);
        /*SL:98*/this.extensionMap.put(a1.getClass(), a1);
    }
    
    public List<IExtension> getExtensions() {
        /*SL:105*/return Collections.<IExtension>unmodifiableList((List<? extends IExtension>)this.extensions);
    }
    
    public List<IExtension> getActiveExtensions() {
        /*SL:112*/return this.activeExtensions;
    }
    
    public <T extends java.lang.Object> T getExtension(final Class<? extends IExtension> a1) {
        /*SL:124*/return Extensions.<T>lookup((Class<? extends T>)a1, (Map<Class<? extends T>, T>)this.extensionMap, (List<T>)this.extensions);
    }
    
    public void select(final MixinEnvironment v2) {
        final ImmutableList.Builder<IExtension> v3 = /*EL:133*/ImmutableList.<IExtension>builder();
        /*SL:135*/for (final IExtension a1 : this.extensions) {
            /*SL:136*/if (a1.checkActive(v2)) {
                /*SL:137*/v3.add(a1);
            }
        }
        /*SL:141*/this.activeExtensions = v3.build();
    }
    
    public void preApply(final ITargetClassContext v2) {
        /*SL:150*/for (final IExtension a1 : this.activeExtensions) {
            /*SL:151*/a1.preApply(v2);
        }
    }
    
    public void postApply(final ITargetClassContext v2) {
        /*SL:161*/for (final IExtension a1 : this.activeExtensions) {
            /*SL:162*/a1.postApply(v2);
        }
    }
    
    public void export(final MixinEnvironment a3, final String a4, final boolean v1, final byte[] v2) {
        /*SL:176*/for (final IExtension a5 : this.activeExtensions) {
            /*SL:177*/a5.export(a3, a4, v1, v2);
        }
    }
    
    public void add(final IClassGenerator a1) {
        /*SL:187*/this.generators.add(a1);
        /*SL:188*/this.generatorMap.put(a1.getClass(), a1);
    }
    
    public List<IClassGenerator> getGenerators() {
        /*SL:195*/return this.generatorsView;
    }
    
    public <T extends java.lang.Object> T getGenerator(final Class<? extends IClassGenerator> a1) {
        /*SL:205*/return Extensions.<T>lookup((Class<? extends T>)a1, (Map<Class<? extends T>, T>)this.generatorMap, (List<T>)this.generators);
    }
    
    private static <T> T lookup(final Class<? extends T> a2, final Map<Class<? extends T>, T> a3, final List<T> v1) {
        T v2 = /*EL:209*/a3.get(a2);
        /*SL:210*/if (v2 == null) {
            /*SL:211*/for (final T a4 : v1) {
                /*SL:212*/if (a2.isAssignableFrom(a4.getClass())) {
                    /*SL:213*/v2 = a4;
                    /*SL:214*/break;
                }
            }
            /*SL:218*/if (v2 == null) {
                /*SL:219*/throw new IllegalArgumentException("Extension for <" + a2.getName() + "> could not be found");
            }
            /*SL:222*/a3.put(a2, v2);
        }
        /*SL:225*/return v2;
    }
}
