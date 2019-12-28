package com.ares;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.client.Minecraft;

public interface Globals
{
    public static final Minecraft mc = Minecraft.func_71410_x();
    public static final boolean isAlpha = true;
    
    default NetworkManager getNetworkManager() {
        /*SL:32*/return FMLClientHandler.instance().getClientToServerNetworkManager();
    }
}
