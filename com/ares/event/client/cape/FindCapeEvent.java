package com.ares.event.client.cape;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.ares.event.AresEvent;

public class FindCapeEvent extends AresEvent
{
    public NetworkPlayerInfo networkPlayerInfo;
    public ResourceLocation capeLoc;
    
    public FindCapeEvent(final NetworkPlayerInfo a1) {
        this.networkPlayerInfo = a1;
        this.capeLoc = null;
    }
}
