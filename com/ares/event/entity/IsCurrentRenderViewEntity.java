package com.ares.event.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import com.ares.Globals;
import com.ares.event.AresEvent;

public class IsCurrentRenderViewEntity extends AresEvent implements Globals
{
    public final Entity renderViewEntity;
    public final EntityPlayerSP localPlayer;
    public boolean result;
    
    public IsCurrentRenderViewEntity(final boolean a1) {
        this.result = a1;
        this.renderViewEntity = IsCurrentRenderViewEntity.mc.func_175606_aa();
        this.localPlayer = IsCurrentRenderViewEntity.mc.field_71439_g;
    }
}
