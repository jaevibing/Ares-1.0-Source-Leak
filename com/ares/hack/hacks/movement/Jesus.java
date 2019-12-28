package com.ares.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.entity.GetWaterJumpHeight;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Jesus", description = "Walk on water", category = EnumCategory.MOVEMENT)
public class Jesus extends BaseHack
{
    @SubscribeEvent
    public void onWaterJumpHeight(final GetWaterJumpHeight a1) {
        /*SL:22*/if (this.getEnabled()) {
            /*SL:24*/a1.height = 0.4;
        }
    }
}
