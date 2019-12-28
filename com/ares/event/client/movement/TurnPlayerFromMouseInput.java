package com.ares.event.client.movement;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import com.ares.event.AresEvent;

@Cancelable
public class TurnPlayerFromMouseInput extends AresEvent
{
    public Entity entity;
    public float yaw;
    public float pitch;
    
    public TurnPlayerFromMouseInput(final Entity a1, final float a2, final float a3) {
        this.entity = a1;
        this.yaw = a2;
        this.pitch = a3;
    }
}
