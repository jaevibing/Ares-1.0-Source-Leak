package com.ares.hack.hacks.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.network.play.client.CPacketPlayer;
import com.ares.event.packet.PacketSent;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Fake Rotation", description = "Fake your rotation", category = EnumCategory.PLAYER)
public class FakeRotation extends BaseHack
{
    @SubscribeEvent
    public void onPacketSent(final PacketSent v2) {
        /*SL:34*/if (!this.getEnabled()) {
            return;
        }
        /*SL:36*/if (v2.packet instanceof CPacketPlayer) {
            try {
                /*SL:40*/ObfuscationReflectionHelper.setPrivateValue((Class)CPacketPlayer.class, (Object)v2.packet, (Object)(-90), new String[] { "pitch", "field_149473_f" });
            }
            catch (Exception a1) {
                /*SL:44*/ChatUtils.printMessage("Disabled fake rotation due to error: " + a1.toString(), "red");
                /*SL:45*/a1.printStackTrace();
            }
        }
    }
}
