package com.ares.hack.hacks.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.network.play.client.CPacketChatMessage;
import com.ares.event.packet.PacketSent;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Chat Append", description = "Show off your new client", category = EnumCategory.MISC, defaultOn = true)
public class ChatAppend extends BaseHack
{
    @SubscribeEvent
    public void onPacket(final PacketSent v-2) {
        /*SL:41*/if (v-2.packet instanceof CPacketChatMessage && this.getEnabled()) {
            final CPacketChatMessage cPacketChatMessage = /*EL:43*/(CPacketChatMessage)v-2.packet;
            final boolean v0 = /*EL:44*/cPacketChatMessage.func_149439_c().startsWith("/") || cPacketChatMessage.func_149439_c().startsWith("!");
            /*SL:46*/if (!v0) {
                final String v = /*EL:48*/cPacketChatMessage.func_149439_c().concat(" » \u028c\u0433\u1d07\u0455");
                try {
                    /*SL:52*/ObfuscationReflectionHelper.setPrivateValue((Class)CPacketChatMessage.class, (Object)cPacketChatMessage, (Object)v, new String[] { "message", "field_149440_a" });
                }
                catch (Exception a1) {
                    /*SL:56*/ChatUtils.printMessage("Disabled chat append due to error: " + a1.getMessage());
                    /*SL:57*/this.setEnabled(false);
                    /*SL:58*/a1.printStackTrace();
                }
            }
        }
    }
}
