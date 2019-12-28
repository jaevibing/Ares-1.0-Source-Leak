package com.ares.hack.optimizations;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import com.ares.event.client.gui.RenderGuiBackground;

@Info(name = "Clear GUI", description = "No Gui Background", defaultOn = false)
public class ClearGui extends BaseOptimization
{
    @SubscribeEvent
    public void shouldRenderGuiBackground(final RenderGuiBackground a1) {
        /*SL:20*/if (this.getEnabled()) {
            /*SL:35*/a1.setResult(Event.Result.ALLOW);
        }
        else {
            /*SL:39*/a1.setResult(Event.Result.DENY);
        }
    }
}
