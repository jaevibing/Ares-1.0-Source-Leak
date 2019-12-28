package com.ares.event.world;

import com.mojang.authlib.GameProfile;

public class PlayerLeave extends PlayerConnection
{
    public PlayerLeave(final GameProfile a1) {
        super(a1);
    }
}
