package com.ares.hack.hacks.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.client.gui.GuiScreen;
import com.ares.subguis.AresGuiGameOver;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "AntiDeathScreen", description = "Prevents the death screen from incorrectly coming up during combat", category = EnumCategory.COMBAT)
public class AntiDeathScreen extends BaseHack
{
    public static AntiDeathScreen INSTANCE;
    
    public AntiDeathScreen() {
        AntiDeathScreen.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onOpenGUI(final GuiOpenEvent v0) {
        /*SL:40*/if (v0.getGui() instanceof GuiGameOver) {
            try {
                final ITextComponent a1 = /*EL:44*/(ITextComponent)ObfuscationReflectionHelper.getPrivateValue((Class)GuiGameOver.class, (Object)v0.getGui(), new String[] { "causeOfDeath", "field_184871_f" });
                /*SL:45*/v0.setGui((GuiScreen)new AresGuiGameOver(a1, v0.getGui()));
            }
            catch (Exception v) {
                /*SL:49*/ChatUtils.printMessage("Disabled Anti Death Screen due to an error: " + v.toString());
                /*SL:50*/v.printStackTrace();
                /*SL:51*/this.setEnabled(false);
            }
        }
    }
}
