package com.ares.event.client.render;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import com.ares.event.AresEvent;

public class RenderOutline extends AresEvent
{
    public Entity target;
    public Entity viewer;
    public ICamera camera;
    
    public RenderOutline(final Entity a1, final Entity a2, final ICamera a3) {
        this.target = a1;
        this.viewer = a2;
        this.camera = a3;
    }
}
