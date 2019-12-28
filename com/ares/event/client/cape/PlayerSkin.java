package com.ares.event.client.cape;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.ares.event.AresEvent;

public abstract class PlayerSkin extends AresEvent
{
    public NetworkPlayerInfo networkPlayerInfo;
    
    public PlayerSkin(final NetworkPlayerInfo a1) {
        this.networkPlayerInfo = a1;
    }
    
    public static class HasSkin extends PlayerSkin
    {
        public boolean result;
        
        public HasSkin(final NetworkPlayerInfo a1, final boolean a2) {
            super(a1);
            this.result = a2;
        }
    }
    
    public static class GetSkin extends PlayerSkin
    {
        public ResourceLocation skinLocation;
        
        public GetSkin(final NetworkPlayerInfo a1, final ResourceLocation a2) {
            super(a1);
            this.skinLocation = a2;
        }
    }
}
