package org.spongepowered.asm.mixin;

import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;

public class EnvironmentStateTweaker implements ITweaker
{
    public void acceptOptions(final List<String> a1, final File a2, final File a3, final String a4) {
    }
    
    public void injectIntoClassLoader(final LaunchClassLoader a1) {
        /*SL:48*/MixinBootstrap.getPlatform().inject();
    }
    
    public String getLaunchTarget() {
        /*SL:53*/return "";
    }
    
    public String[] getLaunchArguments() {
        /*SL:58*/MixinEnvironment.gotoPhase(MixinEnvironment.Phase.DEFAULT);
        /*SL:59*/return new String[0];
    }
}
