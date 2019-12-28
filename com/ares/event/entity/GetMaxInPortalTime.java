package com.ares.event.entity;

import net.minecraft.entity.Entity;
import com.ares.event.AresEvent;

public class GetMaxInPortalTime extends AresEvent
{
    public Entity entity;
    public int maxInPortalTime;
    
    public GetMaxInPortalTime(final Entity a1, final int a2) {
        this.entity = a1;
        this.maxInPortalTime = a2;
    }
}
