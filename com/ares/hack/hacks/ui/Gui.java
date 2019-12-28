package com.ares.hack.hacks.ui;

import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.guis.containergui.ContainerGui;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Gui", description = "Ares's main GUI", category = EnumCategory.UI, defaultIsVisible = false, defaultBind = "SEMICOLON")
public class Gui extends BaseHack
{
    public void onEnabled() {
        Gui.mc.func_147108_a(/*EL:30*/(GuiScreen)new ContainerGui());
        /*SL:31*/this.setEnabled(false);
    }
}
