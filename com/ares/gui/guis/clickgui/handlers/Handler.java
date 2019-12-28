package com.ares.gui.guis.clickgui.handlers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class Handler
{
    public void onRender() {
    }
    
    @SubscribeEvent
    public void renderGameOverlayEvent(final RenderGameOverlayEvent.Post a1) {
        /*SL:32*/if (a1.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }
        /*SL:33*/this.onRender();
    }
}
