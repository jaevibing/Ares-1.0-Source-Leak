package com.ares.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.client.movement.ShouldWalkOffEdge;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Safe Walk", description = "Prevent you from walking off edges", category = EnumCategory.MOVEMENT)
public class SafeWalk extends BaseHack
{
    @SubscribeEvent
    public void shouldWalkOff(final ShouldWalkOffEdge a1) {
        /*SL:17*/if (this.getEnabled()) {
            /*SL:19*/a1.isSneaking = true;
        }
    }
}
