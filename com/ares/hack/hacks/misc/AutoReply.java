package com.ares.hack.hacks.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChat;
import com.ares.event.packet.PacketRecieved;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto Reply", description = "Tell those scrubs whos boss", category = EnumCategory.MISC)
public class AutoReply extends BaseHack
{
    private final String FILE_PATH = "Ebic/autoreplies.txt";
    private String[] replies;
    
    public AutoReply() {
        this.replies = new String[0];
    }
    
    @SubscribeEvent
    public void onClientChat(final PacketRecieved v2) {
        /*SL:35*/if (this.getEnabled() && v2.packet instanceof SPacketChat) {
            final SPacketChat a1 = /*EL:37*/(SPacketChat)v2.packet;
            /*SL:39*/if (a1.func_148915_c().func_150260_c().contains(" whispers: ")) {
                AutoReply.mc.field_71439_g.func_71165_d(/*EL:41*/"/r Shut up Scrub");
            }
        }
    }
}
