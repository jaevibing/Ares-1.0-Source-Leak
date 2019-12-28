package com.ares.subguis;

import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import com.ares.gui.guis.escgui.EscGui;
import com.ares.Globals;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;

public class IngameMenuGui extends GuiIngameMenu
{
    public void func_73866_w_() {
        /*SL:16*/super.func_73866_w_();
        /*SL:18*/this.field_146292_n.add(new GuiButton(69, this.field_146294_l / 2 - 100, this.field_146295_m / 4 - 16, "Ares"));
    }
    
    public void func_146284_a(final GuiButton a1) throws IOException {
        /*SL:24*/if (a1.field_146127_k == 69) {
            Globals.mc.func_147108_a((GuiScreen)EscGui.INSTANCE);
        }
        else {
            /*SL:30*/super.func_146284_a(a1);
        }
    }
}
