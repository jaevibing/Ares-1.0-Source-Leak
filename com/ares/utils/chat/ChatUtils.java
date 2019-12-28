package com.ares.utils.chat;

import net.minecraftforge.fml.common.eventhandler.Event;
import com.ares.event.client.gui.chat.AresChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import java.util.Objects;
import net.minecraft.util.text.ITextComponent;
import com.ares.Globals;

public class ChatUtils implements Globals
{
    public static void printMessage(final String a1) {
        printMessage(/*EL:17*/a1, true);
    }
    
    public static void printMessage(final String a1, final boolean a2) {
        printMessage(/*EL:22*/a1, "white", a2);
    }
    
    public static void printMessage(final String a1, final String a2) {
        printMessage(/*EL:28*/a1, a2, true);
    }
    
    public static void printMessage(final String a2, final String a3, final boolean v1) {
        try {
            printMessage(/*EL:36*/Objects.<ITextComponent>requireNonNull(/*EL:37*/ITextComponent.Serializer.func_150699_a("{\"text\":\"" + a2 + "\",\"color\":\"" + a3 + "\"}")), v1);
        }
        catch (Exception a4) {
            /*SL:46*/a4.printStackTrace();
        }
    }
    
    public static void printMessage(final ITextComponent a1) {
        printMessage(/*EL:52*/a1, true);
    }
    
    public static void printMessage(final ITextComponent v-1, final boolean v0) {
        try {
            /*SL:61*/if (v0) {
                final ITextComponent a1 = /*EL:62*/new TextComponentString("§c<\u028c\u0433\u1d07\u0455>§r ").func_150257_a(v-1);
            }
            ChatUtils.mc.field_71456_v.func_191742_a(ChatType.SYSTEM, /*EL:66*/v-1);
            MinecraftForge.EVENT_BUS.post(/*EL:67*/(Event)new AresChatEvent(v-1));
        }
        catch (Exception v) {
            /*SL:71*/v.printStackTrace();
        }
    }
}
