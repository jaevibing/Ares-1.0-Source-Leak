package com.ares.subguis;

import com.ares.api.MojangWebApi;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import com.ares.Globals;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;

public class MainMenuGui extends GuiMainMenu
{
    public void func_73866_w_() {
        /*SL:33*/super.func_73866_w_();
        /*SL:36*/this.field_146292_n.add(new GuiButton(62, 2, 2, 98, 20, "Login"));
    }
    
    protected void func_146284_a(final GuiButton a1) throws IOException {
        /*SL:42*/if (a1.field_146126_j.equals("Login")) {
            Globals.mc.func_147108_a(/*EL:44*/(GuiScreen)new LoginGui((GuiScreen)this));
        }
        else {
            /*SL:48*/super.func_146284_a(a1);
        }
    }
    
    public void func_73863_a(final int a1, final int a2, final float a3) {
        /*SL:55*/super.func_73863_a(a1, a2, a3);
        String v1 = /*EL:57*/"[ONLINE]";
        Color v2 = Color.GREEN;
        /*SL:60*/if (!MojangWebApi.isAuthUp()) {
            /*SL:62*/v1 = "[OFFLINE]";
            /*SL:63*/v2 = Color.RED;
        }
        String v3 = /*EL:66*/"[ONLINE]";
        Color v4 = Color.GREEN;
        /*SL:69*/if (!MojangWebApi.isSeshUp()) {
            /*SL:71*/v3 = "[OFFLINE]";
            /*SL:72*/v4 = Color.RED;
        }
        /*SL:75*/this.field_146297_k.field_71466_p.func_175065_a("Auth Server:     " + v1, 2.0f, 25.0f, v2.getRGB(), true);
        /*SL:76*/this.field_146297_k.field_71466_p.func_175065_a("Session Server: " + v3, 2.0f, (float)(25 + this.field_146297_k.field_71466_p.field_78288_b + 2), v4.getRGB(), true);
    }
}
