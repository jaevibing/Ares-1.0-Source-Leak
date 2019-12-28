package com.ares.event.entity;

import net.minecraft.entity.player.EntityPlayer;
import com.ares.event.AresEvent;

public class GetPortalCooldown extends AresEvent
{
    public int cooldown;
    public EntityPlayer player;
    
    public GetPortalCooldown(final int a1, final EntityPlayer a2) {
        this.cooldown = a1;
        this.player = a2;
    }
}
