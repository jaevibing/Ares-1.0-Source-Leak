package com.ares.subguis;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.client.event.GuiScreenEvent;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiGameOver;

public class AresGuiGameOver extends GuiGameOver
{
    GuiScreen original;
    int prevHealth;
    
    public AresGuiGameOver(@Nullable final ITextComponent a1, final GuiScreen a2) {
        super(a1);
        this.prevHealth = 0;
        this.original = a2;
    }
    
    public void func_73866_w_() {
        /*SL:32*/super.func_73866_w_();
        final GuiButton v1 = /*EL:35*/new GuiButton(420, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 120, "Hide Death Screen");
        /*SL:36*/this.field_146292_n.add(v1);
        /*SL:37*/v1.field_146124_l = true;
    }
    
    protected void func_146284_a(final GuiButton a1) throws IOException {
        /*SL:60*/if (a1.field_146127_k == 420) {
            /*SL:62*/this.field_146297_k.func_147108_a((GuiScreen)null);
            /*SL:63*/this.field_146297_k.field_71439_g.field_70128_L = false;
            /*SL:64*/this.prevHealth = (int)this.field_146297_k.field_71439_g.func_110143_aJ();
            /*SL:65*/this.field_146297_k.field_71439_g.func_70606_j(1.0f);
        }
        /*SL:70*/super.func_146284_a(a1);
    }
    
    public void func_73876_c() {
        /*SL:80*/if (this.field_146297_k.field_71439_g != null && !this.field_146297_k.field_71439_g.field_70128_L && this.field_146297_k.field_71439_g.func_110143_aJ() > 0.0f) {
            /*SL:83*/this.field_146297_k.func_147108_a((GuiScreen)null);
            /*SL:84*/this.field_146297_k.func_71381_h();
        }
        else {
            /*SL:88*/super.func_73876_c();
        }
    }
    
    @SubscribeEvent
    public void onKeyPress(final GuiScreenEvent.KeyboardInputEvent v2) {
        /*SL:95*/if (Keyboard.getEventKeyState()) {
            final int a1 = /*EL:97*/Keyboard.getEventKey();
            /*SL:98*/if (a1 != 0 && Keyboard.getKeyName(a1).equalsIgnoreCase("ESCAPE")) {
                /*SL:100*/this.field_146297_k.func_147108_a(this.original);
                /*SL:101*/this.field_146297_k.field_71439_g.field_70128_L = true;
                /*SL:102*/this.field_146297_k.field_71439_g.func_70606_j((float)this.prevHealth);
            }
        }
    }
}
