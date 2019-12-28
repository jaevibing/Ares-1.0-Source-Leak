package com.ares.hack.hacks.chatbot;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChat;
import com.ares.event.packet.PacketRecieved;
import com.ares.utils.chat.ChatUtils;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Chat Bot", description = "Scriptable chat bot", category = EnumCategory.CHATBOT)
public class ChatBot extends BaseHack
{
    private ChatBotScriptHandler handler;
    private long lastSendMessage;
    
    public ChatBot() {
        this.lastSendMessage = 0L;
    }
    
    public void onEnabled() {
        try {
            /*SL:21*/this.handler = new ChatBotScriptHandler();
        }
        catch (Exception v1) {
            /*SL:25*/this.setEnabled(false);
            /*SL:26*/ChatUtils.printMessage("Failed to initialise chatbot script: " + v1.getMessage(), "red");
            /*SL:27*/v1.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onPacketReceived(final PacketRecieved v2) {
        /*SL:34*/if (this.getEnabled() && v2.packet instanceof SPacketChat && /*EL:36*/System.currentTimeMillis() - this.lastSendMessage > 5000L) {
            final SPacketChat a1 = /*EL:38*/(SPacketChat)v2.packet;
            /*SL:40*/this.handleChat(a1.func_148915_c().func_150260_c(), a1.func_192590_c().name());
            /*SL:42*/this.lastSendMessage = System.currentTimeMillis();
        }
    }
    
    private void handleChat(final String v2, final String v3) {
        /*SL:49*/if (ChatBot.mc.field_71439_g == null || v2.startsWith("<" + ChatBot.mc.field_71439_g.func_70005_c_()) || v2.startsWith("<" + ChatBot.mc.field_71439_g.getDisplayNameString())) {
            return;
        }
        try {
            /*SL:53*/if (this.handler == null) {
                /*SL:55*/this.handler = new ChatBotScriptHandler();
            }
            final String a1 = /*EL:58*/this.handler.onChatRecieved(v2, v3);
            /*SL:60*/if (a1 != null) {
                ChatBot.mc.field_71439_g.func_71165_d(/*EL:62*/a1);
            }
        }
        catch (Exception a2) {
            /*SL:67*/this.setEnabled(false);
            /*SL:68*/ChatUtils.printMessage("Failure while invoking chatbot script: " + a2.getMessage(), "red");
            /*SL:69*/a2.printStackTrace();
        }
    }
}
