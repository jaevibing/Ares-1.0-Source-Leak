package com.ares.hack.optimizations;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import com.ares.event.client.cape.ShouldCapeEvent;

@Info(name = "Capes", description = "toggle Ares capes", defaultOn = true)
public class Capes extends BaseOptimization
{
    @SubscribeEvent
    public void onCapes(final ShouldCapeEvent a1) {
        /*SL:13*/if (this.getEnabled()) {
            a1.setResult(Event.Result.ALLOW);
        }
        /*SL:14*/if (!this.getEnabled()) {
            a1.setResult(Event.Result.DENY);
        }
    }
}
