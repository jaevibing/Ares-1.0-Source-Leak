package com.ares.hack.hacks.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.client.render.RenderTab;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Extra Tab", description = "Display full tab menu", category = EnumCategory.CLIENT)
public class ExtraTab extends BaseHack
{
    public static ExtraTab INSTANCE;
    
    public ExtraTab() {
        ExtraTab.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onTabRender(final RenderTab a1) {
        /*SL:35*/if (this.getEnabled()) {
            /*SL:37*/a1.size = a1.players.size();
        }
    }
}
