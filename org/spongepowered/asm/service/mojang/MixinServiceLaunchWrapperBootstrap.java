package org.spongepowered.asm.service.mojang;

import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.service.IMixinServiceBootstrap;

public class MixinServiceLaunchWrapperBootstrap implements IMixinServiceBootstrap
{
    private static final String SERVICE_PACKAGE = "org.spongepowered.asm.service.";
    private static final String MIXIN_UTIL_PACKAGE = "org.spongepowered.asm.util.";
    private static final String ASM_PACKAGE = "org.spongepowered.asm.lib.";
    private static final String MIXIN_PACKAGE = "org.spongepowered.asm.mixin.";
    
    @Override
    public String getName() {
        /*SL:44*/return "LaunchWrapper";
    }
    
    @Override
    public String getServiceClassName() {
        /*SL:49*/return "org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper";
    }
    
    @Override
    public void bootstrap() {
        Launch.classLoader.addClassLoaderExclusion(/*EL:55*/"org.spongepowered.asm.service.");
        Launch.classLoader.addClassLoaderExclusion(/*EL:58*/"org.spongepowered.asm.lib.");
        Launch.classLoader.addClassLoaderExclusion(/*EL:59*/"org.spongepowered.asm.mixin.");
        Launch.classLoader.addClassLoaderExclusion(/*EL:60*/"org.spongepowered.asm.util.");
    }
}
