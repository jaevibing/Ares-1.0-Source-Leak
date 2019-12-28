package com.ares.hack.hacks.client;

import com.ares.event.client.ares.HackDisabled;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import com.ares.event.client.ares.HackEnabled;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "MsgOnToggle", description = "Sends message to chat on module toggle", category = EnumCategory.CLIENT, defaultOn = true)
public class MsgOnToggle extends BaseHack
{
    @SubscribeEvent
    public void onHackEnabled(final HackEnabled a1) {
        /*SL:16*/if (this.getEnabled() && !a1.hack.name.equalsIgnoreCase("clickgui")) {
            /*SL:18*/ChatUtils.printMessage(a1.hack.name + " was enabled", "green");
        }
    }
    
    @SubscribeEvent
    public void onHackDisabled(final HackDisabled a1) {
        /*SL:25*/if (this.getEnabled() && !a1.hack.name.equalsIgnoreCase("clickgui")) {
            /*SL:27*/ChatUtils.printMessage(a1.hack.name + " was disabled", "red");
        }
    }
}
