package com.ares.event.world;

import com.mojang.authlib.GameProfile;

public class PlayerJoin extends PlayerConnection
{
    public PlayerJoin(final GameProfile a1) {
        super(a1);
    }
}
