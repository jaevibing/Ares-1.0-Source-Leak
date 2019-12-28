package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;

public class Config
{
    private final String name;
    private final MixinConfig config;
    
    public Config(final MixinConfig a1) {
        this.name = a1.getName();
        this.config = a1;
    }
    
    public String getName() {
        /*SL:51*/return this.name;
    }
    
    MixinConfig get() {
        /*SL:58*/return this.config;
    }
    
    public boolean isVisited() {
        /*SL:65*/return this.config.isVisited();
    }
    
    public IMixinConfig getConfig() {
        /*SL:72*/return this.config;
    }
    
    public MixinEnvironment getEnvironment() {
        /*SL:79*/return this.config.getEnvironment();
    }
    
    @Override
    public String toString() {
        /*SL:87*/return this.config.toString();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:95*/return a1 instanceof Config && this.name.equals(((Config)a1).name);
    }
    
    @Override
    public int hashCode() {
        /*SL:103*/return this.name.hashCode();
    }
    
    @Deprecated
    public static Config create(final String a1, final MixinEnvironment a2) {
        /*SL:116*/return MixinConfig.create(a1, a2);
    }
    
    public static Config create(final String a1) {
        /*SL:126*/return MixinConfig.create(a1, MixinEnvironment.getDefaultEnvironment());
    }
}
