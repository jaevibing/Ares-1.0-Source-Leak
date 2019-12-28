package com.ares.gui.guis.clickgui;

import com.ares.gui.guis.clickgui.buttons.HackButton;
import com.ares.hack.hacks.BaseHack;
import org.lwjgl.opengl.GL11;
import com.ares.Globals;
import java.util.Iterator;
import com.ares.gui.guis.clickgui.buttons.Button;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;

public class GuiNotif extends GuiScreen
{
    public static int mouseX;
    public static int mouseY;
    
    public GuiNotif() {
        this.field_146291_p = true;
    }
    
    public void func_73866_w_() {
        /*SL:43*/super.func_73866_w_();
        /*SL:44*/this.field_146291_p = true;
    }
    
    protected void func_73869_a(final char a1, final int a2) throws IOException {
        /*SL:49*/super.func_73869_a(a1, a2);
    }
    
    public void func_73863_a(final int a3, final int v1, final float v2) {
        GuiNotif.mouseX = /*EL:55*/a3;
        GuiNotif.mouseY = /*EL:56*/v1;
        /*SL:58*/for (final Button a4 : GuiUtil.getButtons()) {
            /*SL:60*/if (a4.isVisible) {
                a4.drawButton(this, a3, v1);
            }
        }
        /*SL:63*/this.renderTooltip(a3, v1);
        /*SL:65*/super.func_73863_a(a3, v1, v2);
    }
    
    private void renderTooltip(final int v-2, final int v-1) {
        final Button v0 = /*EL:70*/GuiUtil.findHoveredBtn(v-2, v-1);
        /*SL:71*/if (v0 != null) {
            final BaseHack v = /*EL:73*/GuiUtil.btnToHack(v0);
            /*SL:74*/if (v != null) {
                Globals.mc.field_71446_o.func_110577_a(GuiUtil.white);
                /*SL:77*/GL11.glPushAttrib(1048575);
                /*SL:78*/GL11.glPushMatrix();
                /*SL:79*/GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                /*SL:81*/GL11.glColor4f(255.0f, 255.0f, 255.0f, 1.0f);
                final int a1 = /*EL:83*/this.field_146289_q.func_78256_a(v.description) + 1;
                final int a2 = /*EL:84*/this.field_146289_q.field_78288_b + 1;
                /*SL:85*/this.func_73729_b(v-2 + 5, v-1 + 5, a1, a2, a1, a2);
                /*SL:87*/GL11.glPopMatrix();
                /*SL:88*/GL11.glPopAttrib();
                /*SL:90*/this.field_146289_q.func_78276_b(v.description, v-2 + 6, v-1 + 6, 0);
            }
        }
    }
    
    protected void func_73864_a(final int v1, final int v2, final int v3) throws IOException {
        /*SL:98*/for (HackButton a2 : GuiUtil.getButtons()) {
            /*SL:100*/if (!a2.isVisible) {
                continue;
            }
            /*SL:102*/if (v1 >= a2.x && v1 <= a2.x + a2.width && v2 >= a2.y && v2 <= a2.y + a2.height && v3 == 0) {
                final Iterator<HackButton> iterator2 = /*EL:104*/HackButton.getAll().iterator();
                while (iterator2.hasNext()) {
                    a2 = iterator2.next();
                    /*SL:106*/if (a2.text.equals(a2.text)) {
                        /*SL:108*/a2.getHack().setEnabled(!a2.getHack().getEnabled());
                        /*SL:109*/return;
                    }
                }
                /*SL:113*/a2.leftClickToggled = !a2.leftClickToggled;
                /*SL:114*/return;
            }
            /*SL:116*/if (v1 >= a2.x && v1 <= a2.x + a2.width && v2 >= a2.y && v2 <= a2.y + a2.height && v3 == 1) {
                /*SL:118*/a2.rightClickToggled = !a2.rightClickToggled;
                /*SL:119*/return;
            }
        }
        /*SL:123*/super.func_73864_a(v1, v2, v3);
    }
    
    public boolean func_73868_f() {
        /*SL:129*/return false;
    }
    
    static {
        GuiNotif.mouseX = 0;
        GuiNotif.mouseY = 0;
    }
}
