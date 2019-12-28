package com.ares.gui.guis.containergui.buttons;

import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import com.ares.gui.guis.containergui.elements.ElementHackList;
import com.ares.gui.guis.AresGui;
import com.ares.gui.guis.containergui.elements.ElementHack;
import net.minecraft.util.ResourceLocation;
import com.ares.gui.buttons.Button;

public class DropDownButton extends Button
{
    private static final ResourceLocation DROP_DOWN_BUTTON;
    private boolean toggled;
    private ElementHack elementHack;
    
    public DropDownButton(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final ElementHack a6) {
        super(a1, a2, a3, a4, a5, "");
        this.toggled = false;
        this.elementHack = a6;
    }
    
    public void toggle() {
        /*SL:27*/for (final ElementHackList list : ElementHackList.getElements()) {
            /*SL:29*/if (list.getCategory() == this.elementHack.getHack().category) {
                /*SL:31*/if (list.leftHacks.values().contains(this.elementHack)) {
                    /*SL:33*/for (final ElementHack v1 : list.leftHacks.values()) {
                        /*SL:35*/if (v1 != this.elementHack && v1.dropButton.toggled) {
                            /*SL:37*/v1.dropButton.toggle();
                        }
                    }
                }
                /*SL:42*/if (!list.rightHacks.values().contains(this.elementHack)) {
                    continue;
                }
                /*SL:44*/for (final ElementHack v1 : list.rightHacks.values()) {
                    /*SL:46*/if (v1 != this.elementHack && v1.dropButton.toggled) {
                        /*SL:48*/v1.dropButton.toggle();
                    }
                }
            }
        }
        /*SL:55*/this.toggled = !this.toggled;
        /*SL:57*/if (this.toggled) {
            final ElementHack elementHack = /*EL:59*/this.elementHack;
            elementHack.height += this.elementHack.numOfSettings * (DropDownButton.mc.field_71466_p.field_78288_b + 4);
        }
        else {
            final ElementHack elementHack2 = /*EL:63*/this.elementHack;
            elementHack2.height -= this.elementHack.numOfSettings * (DropDownButton.mc.field_71466_p.field_78288_b + 4);
        }
    }
    
    public boolean getToggled() {
        /*SL:69*/return this.toggled;
    }
    
    public void draw() {
        DropDownButton.mc.func_110434_K().func_110577_a(DropDownButton.DROP_DOWN_BUTTON);
        /*SL:76*/GlStateManager.func_179094_E();
        /*SL:78*/if (this.toggled) {
            /*SL:80*/GlStateManager.func_179109_b((float)(this.field_146128_h + this.field_146120_f / 2), (float)(this.field_146129_i + this.field_146121_g / 2), 0.0f);
            /*SL:81*/GlStateManager.func_179114_b(90.0f, 0.0f, 0.0f, 1.0f);
            /*SL:82*/GlStateManager.func_179109_b((float)(-(this.field_146128_h + this.field_146120_f / 2)), (float)(-(this.field_146129_i + this.field_146121_g / 2)), 0.0f);
        }
        func_146110_a(/*EL:85*/this.field_146128_h, this.field_146129_i, 0.0f, 0.0f, this.field_146120_f, this.field_146121_g, (float)this.field_146120_f, (float)this.field_146121_g);
        /*SL:86*/GlStateManager.func_179121_F();
    }
    
    static {
        DROP_DOWN_BUTTON = new ResourceLocation("ares", "drop_down_button.png");
    }
}
