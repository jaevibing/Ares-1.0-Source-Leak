package com.ares.hack.hacks.render;

import com.ares.event.block.CheckBlockLight;
import com.ares.event.world.GetLightmapCoords;
import com.ares.event.world.GetDimensionBrightness;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.world.GetAmbientOcclusionLightValue;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Full Bright", description = "Big Brightness bois", category = EnumCategory.RENDER)
public class FullBright extends BaseHack
{
    @SubscribeEvent
    public void getAmbOcclLight(final GetAmbientOcclusionLightValue a1) {
        /*SL:17*/if (this.getEnabled()) {
            /*SL:19*/a1.level = 1.0f;
        }
    }
    
    @SubscribeEvent
    public void getDimensionBrightness(final GetDimensionBrightness a1) {
        /*SL:26*/if (this.getEnabled()) {
            /*SL:28*/a1.initial = 1.0f;
        }
    }
    
    @SubscribeEvent
    public void getLightmapCoords(final GetLightmapCoords a1) {
        /*SL:35*/if (this.getEnabled()) {
            /*SL:37*/a1.lightmapCoords = 1000;
        }
    }
    
    @SubscribeEvent
    public void checkLightFor(final CheckBlockLight a1) {
        /*SL:44*/if (this.getEnabled()) {
            /*SL:46*/a1.checkLight = false;
        }
    }
}
