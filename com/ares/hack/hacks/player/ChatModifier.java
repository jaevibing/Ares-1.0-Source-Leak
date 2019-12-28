package com.ares.hack.hacks.player;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.network.play.client.CPacketChatMessage;
import com.ares.event.packet.PacketSent;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.utils.chat.modifiers.Soviet;
import com.ares.utils.chat.modifiers.Fancy;
import com.ares.utils.chat.modifiers.Disabled;
import com.ares.utils.chat.modifiers.L33t;
import com.ares.utils.chat.modifiers.JustLearntEngrish;
import com.ares.utils.chat.modifiers.Chav;
import com.ares.utils.chat.modifiers.Reverse;
import com.ares.utils.chat.modifiers.Emphasize;
import com.ares.hack.settings.Setting;
import com.ares.utils.chat.modifiers.ChatModifier;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Chat Modifier", description = "Modify your chat messages", category = EnumCategory.PLAYER)
public class ChatModifier extends BaseHack
{
    private com.ares.utils.chat.modifiers.ChatModifier[] mutators;
    private Setting<Boolean> emphasize;
    private Setting<Boolean> Reverse;
    private Setting<Boolean> chav;
    private Setting<Boolean> justLearntEngrish;
    private Setting<Boolean> l33t;
    private Setting<Boolean> disabled;
    private Setting<Boolean> fancy;
    private Setting<Boolean> soviet;
    
    public ChatModifier() {
        this.mutators = new com.ares.utils.chat.modifiers.ChatModifier[] { new Emphasize(), new Reverse(), new Chav(), new JustLearntEngrish(), new L33t(), new Disabled(), new Fancy(), new Soviet() };
        this.emphasize = new BooleanSetting("Emphasize", this, false);
        this.Reverse = new BooleanSetting("Reverse", this, false);
        this.chav = new BooleanSetting("Chav", this, false);
        this.justLearntEngrish = new BooleanSetting("JustLearntEngrish", this, false);
        this.l33t = new BooleanSetting("L33t", this, false);
        this.disabled = new BooleanSetting("Disabled", this, false);
        this.fancy = new BooleanSetting("Fancy", this, false);
        this.soviet = new BooleanSetting("Soviet", this, false);
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSent v-7) {
        /*SL:40*/if (v-7.packet instanceof CPacketChatMessage) {
            System.out.println(/*EL:42*/"Was packet");
            /*SL:43*/if (this.getEnabled()) {
                final CPacketChatMessage cPacketChatMessage = /*EL:45*/(CPacketChatMessage)v-7.packet;
                String s = /*EL:47*/cPacketChatMessage.func_149439_c();
                final boolean b = /*EL:49*/s.startsWith("/") || s.startsWith("!");
                /*SL:50*/if (!b) {
                    /*SL:52*/for (final com.ares.utils.chat.modifiers.ChatModifier v0 : this.mutators) {
                        try {
                            boolean a1 = /*EL:56*/false;
                            final String name = /*EL:57*/v0.getName();
                            switch (name) {
                                case "Emphasize": {
                                    /*SL:60*/a1 = this.emphasize.getValue();
                                    /*SL:61*/break;
                                }
                                case "Reverse": {
                                    /*SL:63*/a1 = this.Reverse.getValue();
                                    /*SL:64*/break;
                                }
                                case "Chav": {
                                    /*SL:66*/a1 = this.chav.getValue();
                                    /*SL:67*/break;
                                }
                                case "JustLearntEngrish": {
                                    /*SL:69*/a1 = this.justLearntEngrish.getValue();
                                    /*SL:70*/break;
                                }
                                case "L33t": {
                                    /*SL:72*/a1 = this.l33t.getValue();
                                    /*SL:73*/break;
                                }
                                case "Disabled": {
                                    /*SL:75*/a1 = this.disabled.getValue();
                                    /*SL:76*/break;
                                }
                                case "Fancy": {
                                    /*SL:78*/a1 = this.fancy.getValue();
                                    /*SL:79*/break;
                                }
                                case "Soviet": {
                                    /*SL:81*/a1 = this.soviet.getValue();
                                    break;
                                }
                            }
                            /*SL:85*/if (a1) {
                                /*SL:87*/s = v0.mutate(s);
                            }
                        }
                        catch (Exception v) {
                            /*SL:89*/v.printStackTrace();
                        }
                    }
                }
                try {
                    /*SL:94*/ObfuscationReflectionHelper.setPrivateValue((Class)CPacketChatMessage.class, (Object)cPacketChatMessage, (Object)s, new String[] { "message", "field_149440_a" });
                }
                catch (Exception ex) {
                    /*SL:98*/ChatUtils.printMessage("Disabled chat modifier due to error: " + ex.getMessage());
                    /*SL:99*/this.setEnabled(false);
                    /*SL:100*/ex.printStackTrace();
                }
            }
        }
    }
}
