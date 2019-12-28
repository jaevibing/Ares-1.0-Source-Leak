package com.ares.event.world;

import com.mojang.authlib.GameProfile;
import com.ares.event.AresEvent;

public class PlayerConnection extends AresEvent
{
    public GameProfile gameProfile;
    
    public PlayerConnection(final GameProfile a1) {
        this.gameProfile = a1;
    }
}
