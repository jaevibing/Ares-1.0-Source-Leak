package com.ares.event.world;

import net.minecraft.entity.player.EntityPlayer;
import com.ares.event.AresEvent;

public class PlayerDeath extends AresEvent
{
    private EntityPlayer player;
    
    public PlayerDeath(final EntityPlayer a1) {
        this.player = a1;
    }
    
    public EntityPlayer getPlayer() {
        /*SL:22*/return this.player;
    }
}
