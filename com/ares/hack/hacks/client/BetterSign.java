package com.ares.hack.hacks.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.client.gui.GuiScreen;
import com.ares.subguis.BetterSignGui;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiOpenEvent;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Better Sign", description = "Better Sign GUI", category = EnumCategory.CLIENT)
public class BetterSign extends BaseHack
{
    @SubscribeEvent
    public void onOpenGUI(final GuiOpenEvent v0) {
        /*SL:36*/if (v0.getGui() instanceof GuiEditSign && this.getEnabled()) {
            try {
                final TileEntitySign a1 = /*EL:40*/(TileEntitySign)ObfuscationReflectionHelper.getPrivateValue((Class)GuiEditSign.class, (Object)v0.getGui(), new String[] { "tileSign", "field_146848_f" });
                /*SL:41*/v0.setGui((GuiScreen)new BetterSignGui(a1));
            }
            catch (Exception v) {
                /*SL:45*/ChatUtils.printMessage("Disabled Secret Close due to an error: " + v.toString());
                /*SL:46*/this.setEnabled(false);
            }
        }
    }
}
