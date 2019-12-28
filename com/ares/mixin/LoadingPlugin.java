package com.ares.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class LoadingPlugin implements IFMLLoadingPlugin
{
    public LoadingPlugin() {
        System.out.println("Ares Coremod initialising");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.ares.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }
    
    public String[] getASMTransformerClass() {
        /*SL:24*/return new String[0];
    }
    
    public String getModContainerClass() {
        /*SL:30*/return null;
    }
    
    public String getSetupClass() {
        /*SL:36*/return null;
    }
    
    public void injectData(final Map<String, Object> a1) {
    }
    
    public String getAccessTransformerClass() {
        /*SL:46*/return null;
    }
}
