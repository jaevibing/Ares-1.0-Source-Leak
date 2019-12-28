package org.spongepowered.asm.launch;

import net.minecraft.launchwrapper.LaunchClassLoader;
import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;

public class MixinTweaker implements ITweaker
{
    public MixinTweaker() {
        MixinBootstrap.start();
    }
    
    public final void acceptOptions(final List<String> a1, final File a2, final File a3, final String a4) {
        /*SL:53*/MixinBootstrap.doInit(a1);
    }
    
    public final void injectIntoClassLoader(final LaunchClassLoader a1) {
        /*SL:62*/MixinBootstrap.inject();
    }
    
    public String getLaunchTarget() {
        /*SL:70*/return MixinBootstrap.getPlatform().getLaunchTarget();
    }
    
    public String[] getLaunchArguments() {
        /*SL:78*/return new String[0];
    }
}
