package com.ares.hack.hacks.client;

import java.util.Objects;
import com.ares.utils.chat.ChatUtils;
import org.apache.logging.log4j.LogManager;
import com.ares.hack.hacks.chatbot.ChatBotScriptHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.text.ChatType;
import java.util.regex.Pattern;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.hacks.chatbot.ScriptHandler;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Chat Filter", description = "Filter your chat", category = EnumCategory.CLIENT)
public class ChatFilter extends BaseHack
{
    private Setting<Boolean> whispers;
    private Setting<Boolean> mentions;
    private Setting<Boolean> gameInfo;
    private ScriptHandler scriptHandler;
    
    public ChatFilter() {
        this.whispers = new BooleanSetting("Allow Whispers", this, true);
        this.mentions = new BooleanSetting("Allow Mentions", this, true);
        this.gameInfo = new BooleanSetting("Allow Game Info", this, true);
    }
    
    @SubscribeEvent
    public void onClientChatRecieved(final ClientChatReceivedEvent v0) {
        /*SL:29*/if (this.getEnabled()) {
            /*SL:31*/v0.setCanceled(true);
            final String v = /*EL:33*/v0.getMessage().func_150260_c().toLowerCase();
            /*SL:36*/if (this.whispers.getValue()) {
                final String[] a1 = /*EL:38*/v.split(Pattern.quote(" "));
                /*SL:40*/if (a1.length >= 3 && /*EL:42*/a1[1].equals("whispers:")) {
                    /*SL:44*/v0.setCanceled(false);
                }
            }
            /*SL:50*/if (this.mentions.getValue() && v.contains(ChatFilter.mc.field_71439_g.func_70005_c_().toLowerCase())) {
                /*SL:52*/v0.setCanceled(false);
            }
            /*SL:60*/if (this.gameInfo.getValue() && v0.getType() == ChatType.GAME_INFO) {
                /*SL:62*/v0.setCanceled(false);
            }
            /*SL:65*/if (!v0.isCanceled()) {
                /*SL:67*/v0.setCanceled(this.isExcludedByScript(v0.getMessage().func_150260_c()));
            }
        }
    }
    
    public boolean isExcludedByScript(final String v0) {
        /*SL:76*/if (this.scriptHandler == null) {
            try {
                /*SL:82*/this.scriptHandler = new ScriptHandler().eval(ChatBotScriptHandler.getScriptContents("Ares/chatfilter.js")).addLogger(LogManager.getLogger("BackdooredChatFilter"));
            }
            catch (Exception a1) {
                /*SL:86*/this.setEnabled(false);
                /*SL:87*/ChatUtils.printMessage("Failed to initialise Chat Filter script: " + a1.getMessage(), "red");
                /*SL:88*/a1.printStackTrace();
                /*SL:89*/return false;
            }
        }
        try {
            final Object v = /*EL:95*/this.scriptHandler.invokeFunction("isExcluded", v0);
            /*SL:96*/return Objects.<Boolean>requireNonNull(v);
        }
        catch (Exception v2) {
            /*SL:100*/v2.printStackTrace();
            /*SL:102*/return false;
        }
    }
}
