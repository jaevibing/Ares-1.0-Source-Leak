package com.ares.hack.hacks.client;

import com.ares.utils.chat.ChatUtils;
import org.apache.commons.io.FileUtils;
import java.nio.charset.StandardCharsets;
import java.io.File;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChat;
import com.ares.event.packet.PacketRecieved;
import com.ares.utils.TrayUtils;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Notifications", description = "Toast Notifications", category = EnumCategory.CLIENT)
public class Notifications extends BaseHack
{
    public static Notifications INSTANCE;
    private String discordWebhook;
    private Setting<Boolean> discord;
    private Setting<Boolean> VisualRange;
    private Setting<Boolean> Queue;
    
    public Notifications() {
        this.discordWebhook = null;
        this.discord = new BooleanSetting("Discord Webhook", this, false);
        this.VisualRange = new BooleanSetting("Visual Range", this, true);
        this.Queue = new BooleanSetting("Queue", this, true);
        Notifications.INSTANCE = this;
    }
    
    public void visualRangeTrigger(final EntityPlayer a1) {
        /*SL:54*/if (!this.getEnabled() || !this.VisualRange.getValue()) {
            return;
        }
        /*SL:56*/TrayUtils.sendMessage("Visual Range", a1.getDisplayNameString() + " entered your visual range");
    }
    
    @SubscribeEvent
    public void onPacket(final PacketRecieved v-1) {
        /*SL:62*/if (this.getEnabled() && this.Queue.getValue() && v-1.packet instanceof SPacketChat) {
            final SPacketChat a1 = /*EL:64*/(SPacketChat)v-1.packet;
            final String v1 = /*EL:66*/a1.func_148915_c().func_150260_c().toLowerCase();
            /*SL:67*/if (v1.startsWith("connecting to")) {
                /*SL:69*/TrayUtils.sendMessage(a1.func_148915_c().func_150260_c());
            }
        }
    }
    
    public void onEnabled() {
        try {
            final File v1 = /*EL:79*/new File("Ares/discordwebhook.txt");
            /*SL:81*/if (!v1.exists()) {
                /*SL:83*/v1.createNewFile();
                /*SL:84*/throw new RuntimeException("discordwebhook.txt did not exist");
            }
            /*SL:87*/this.discordWebhook = FileUtils.readFileToString(v1, StandardCharsets.UTF_8).trim();
            /*SL:89*/if (this.discordWebhook.isEmpty()) {
                /*SL:91*/throw new RuntimeException("discordwebhook.txt was empty");
            }
            /*SL:94*/ChatUtils.printMessage("Set discord webhook to: " + this.discordWebhook);
        }
        catch (Exception v2) {
            /*SL:98*/ChatUtils.printMessage("Couldnt get discord webhook: " + v2.getMessage());
            /*SL:99*/v2.printStackTrace();
        }
    }
    
    private void send(final String a1) {
        /*SL:105*/if (this.discord.getValue()) {
            /*SL:109*/return;
        }
    }
}
