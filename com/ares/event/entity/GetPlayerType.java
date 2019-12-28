package com.ares.event.entity;

import net.minecraft.world.GameType;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.ares.event.AresEvent;

public class GetPlayerType extends AresEvent
{
    public final NetworkPlayerInfo networkPlayerInfo;
    public GameType gameType;
    
    public GetPlayerType(final NetworkPlayerInfo a1, final GameType a2) {
        this.networkPlayerInfo = a1;
        this.gameType = a2;
    }
}
