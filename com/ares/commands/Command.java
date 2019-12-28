package com.ares.commands;

import net.minecraft.network.play.client.CPacketChatMessage;
import com.ares.event.packet.PacketSent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import com.ares.utils.chat.ChatUtils;
import com.ares.hack.settings.settings.EnumSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.settings.SettingManager;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraftforge.client.event.ClientChatEvent;

public class Command
{
    public static String commandPrefix;
    private static Command INSTANCE;
    
    public void Command() {
        Command.INSTANCE = /*EL:44*/this;
    }
    
    public static void processCommand(final String a1) {
        /*SL:48*/if (a1.startsWith(Command.commandPrefix)) {
            /*SL:51*/if (Command.INSTANCE == null) {
                Command.INSTANCE = /*EL:53*/new Command();
            }
            Command.INSTANCE.ClientChatEvent(/*EL:55*/new ClientChatEvent(a1));
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void ClientChatEvent(final ClientChatEvent v-3) {
        final String[] split = /*EL:62*/v-3.getMessage().split(" ");
        /*SL:64*/if (!split[0].startsWith(Command.commandPrefix)) {
            return;
        }
        /*SL:66*/if (split[0].startsWith(Command.commandPrefix)) {
            /*SL:68*/split[0] = split[0].replace(Command.commandPrefix, "");
        }
        final String[] array = /*EL:72*/new ArrayList(Arrays.<String>asList(split).subList(1, split.length)).<String>toArray(new String[split.length - 1]);
        /*SL:74*/for (final CommandBase v1 : CommandBase.objects) {
            /*SL:76*/if (v1.aliases.contains(split[0].toLowerCase())) {
                /*SL:78*/if (array.length == 0) {
                    final String[] a1 = /*EL:80*/{ "", "", "", "", "", "" };
                    /*SL:81*/v1.execute(a1);
                    /*SL:82*/return;
                }
                /*SL:85*/v1.execute(array);
                /*SL:88*/return;
            }
        }
        /*SL:92*/if (array.length >= 2) {
            /*SL:94*/for (final Setting v2 : SettingManager.getAllSettings()) {
                /*SL:96*/if (v2.getHack().name.equalsIgnoreCase(array[0])) {
                    try {
                        /*SL:100*/if (v2.getValue() instanceof Integer) {
                            /*SL:102*/v2.setValue(Integer.parseInt(array[1]));
                        }
                        else/*SL:104*/ if (v2.getValue() instanceof Double) {
                            /*SL:106*/v2.setValue(Double.parseDouble(array[1]));
                        }
                        else/*SL:108*/ if (v2.getValue() instanceof Float) {
                            /*SL:110*/v2.setValue(Float.parseFloat(array[1]));
                        }
                        else/*SL:112*/ if (v2.getValue() instanceof Long) {
                            /*SL:114*/v2.setValue(Long.parseLong(array[1]));
                        }
                        else/*SL:116*/ if (v2.getValue() instanceof Enum) {
                            /*SL:118*/((EnumSetting)v2).setValueFromString(array[1]);
                        }
                        else/*SL:120*/ if (v2.getValue() instanceof Boolean) {
                            /*SL:122*/v2.setValue(Boolean.valueOf(array[1]));
                        }
                        else/*SL:124*/ if (v2.getValue() instanceof String) {
                            /*SL:126*/v2.setValue(array[1]);
                        }
                        else {
                            System.out.println(/*EL:130*/"Settings of that type not supported yet");
                        }
                    }
                    catch (Exception v3) {
                        /*SL:135*/ChatUtils.printMessage(v3.getMessage(), "red");
                    }
                }
            }
        }
        /*SL:141*/ChatUtils.printMessage("Command not found! Type " + Command.commandPrefix + "help for a list of commands", "red");
    }
    
    @SubscribeEvent
    public void onPacket(final PacketSent v2) {
        /*SL:147*/if (v2.packet instanceof CPacketChatMessage) {
            final CPacketChatMessage a1 = /*EL:149*/(CPacketChatMessage)v2.packet;
            /*SL:151*/if (a1.func_149439_c().startsWith(Command.commandPrefix)) {
                v2.setCanceled(true);
            }
        }
    }
    
    private void execute(final CommandBase a1, final String[] a2) {
        /*SL:157*/if (!a1.execute(a2)) {
            /*SL:159*/ChatUtils.printMessage("Usage:\n" + a1.getSyntax(), "red");
        }
    }
    
    static {
        Command.commandPrefix = "-";
    }
}
