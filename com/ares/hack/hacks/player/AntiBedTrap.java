package com.ares.hack.hacks.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.network.play.server.SPacketUseBed;
import com.ares.event.packet.PacketSent;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Anti Bed Trap", description = "Prevent sleeping in beds", category = EnumCategory.PLAYER)
public class AntiBedTrap extends BaseHack
{
    @SubscribeEvent
    public void onPacketSend(final PacketSent a1) {
        /*SL:16*/if (this.getEnabled() && a1.packet instanceof SPacketUseBed) {
            /*SL:18*/a1.setCanceled(true);
            /*SL:19*/ChatUtils.printMessage("Phew, that was close!", "green");
        }
    }
}
