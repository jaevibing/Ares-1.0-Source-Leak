package com.ares.hack.hacks.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Anti FOV", description = "Cap your FOV", category = EnumCategory.CLIENT)
public class AntiFOV extends BaseHack
{
    private Setting<Integer> maxFOV;
    
    public AntiFOV() {
        this.maxFOV = new IntegerSetting("Max FOV", this, 125, 0, 360);
    }
    
    @SubscribeEvent
    public void onRenderFOV(final EntityViewRenderEvent.FOVModifier a1) {
        /*SL:35*/if (this.getEnabled()) {
            /*SL:37*/a1.setFOV(Math.min(a1.getFOV(), this.maxFOV.getValue()));
        }
    }
}
