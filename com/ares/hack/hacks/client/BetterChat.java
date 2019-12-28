package com.ares.hack.hacks.client;

import net.minecraftforge.fml.common.eventhandler.Event;
import com.ares.event.client.gui.chat.InfiniteChat;
import com.ares.event.client.gui.chat.GetChatDefaultText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.text.TextComponentString;
import java.time.temporal.TemporalAccessor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import com.ares.hack.settings.settings.EnumSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.utils.ColourUtils;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Better Chat", description = "Client side chat tweaks", category = EnumCategory.CLIENT)
public class BetterChat extends BaseHack
{
    private final Setting<Boolean> timestamps;
    private final Setting<Boolean> timestampSecs;
    private final Setting<ColourUtils.Colours> timestampColour;
    private final Setting<Boolean> chatPersistance;
    private final Setting<Boolean> infiniteLength;
    
    public BetterChat() {
        this.timestamps = new BooleanSetting("Timestamps", this, true);
        this.timestampSecs = new BooleanSetting("Timestamp Secs", this, true);
        this.timestampColour = new EnumSetting<ColourUtils.Colours>("Timestamp Colour", this, ColourUtils.Colours.RED);
        this.chatPersistance = new BooleanSetting("Persistance", this, true);
        this.infiniteLength = new BooleanSetting("Infinite Length", this, false);
    }
    
    @SubscribeEvent
    public void onClientChat(final ClientChatReceivedEvent v-1) {
        /*SL:39*/if (this.getEnabled() && this.timestamps.getValue()) {
            final String a1 = /*EL:41*/ColourUtils.strToColour(this.timestampColour.getValue().toString());
            final String v1 = /*EL:42*/this.timestampSecs.getValue() ? "hh:mm:ss" : "hh:mm";
            final TextComponentString v2 = /*EL:44*/new TextComponentString(a1 + "<" + /*EL:47*/DateTimeFormatter.ofPattern(v1).format(LocalDateTime.now()) + ">§r ");
            /*SL:51*/v-1.setMessage(v2.func_150257_a(v-1.getMessage()));
        }
    }
    
    @SubscribeEvent
    public void getChatDefaultText(final GetChatDefaultText a1) {
        /*SL:72*/if (this.getEnabled() && this.chatPersistance.getValue()) {
            System.out.println(/*EL:74*/"Made last: " + a1.lastText);
            /*SL:75*/a1.defaultText = a1.lastText;
        }
    }
    
    @SubscribeEvent
    public void shouldInfiniteChat(final InfiniteChat a1) {
        /*SL:82*/if (this.getEnabled() && this.infiniteLength.getValue()) {
            /*SL:83*/a1.setResult(Event.Result.ALLOW);
        }
        else {
            /*SL:85*/a1.setResult(Event.Result.DENY);
        }
    }
}
