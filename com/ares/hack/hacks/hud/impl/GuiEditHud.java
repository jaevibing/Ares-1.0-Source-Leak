package com.ares.hack.hacks.hud.impl;

import com.ares.hack.hacks.hud.api.MoveableHudElement;
import com.ares.hack.hacks.hud.api.AbstractHudElement;
import com.ares.Ares;
import net.minecraft.client.gui.GuiScreen;

public class GuiEditHud extends GuiScreen
{
    public void func_73866_w_() {
    }
    
    public void func_73863_a(final int v1, final int v2, final float v3) {
        /*SL:23*/for (AbstractHudElement a2 = (AbstractHudElement)0; a2 < Ares.hudManager.elements.size(); ++a2) {
            /*SL:25*/a2 = Ares.hudManager.elements.get(a2);
            /*SL:26*/a2.render(v1, v2, v3);
        }
    }
    
    protected void func_73864_a(final int v1, final int v2, final int v3) {
        /*SL:33*/for (AbstractHudElement a2 = (AbstractHudElement)0; a2 < Ares.hudManager.elements.size(); ++a2) {
            /*SL:35*/a2 = Ares.hudManager.elements.get(a2);
            /*SL:36*/a2.onMouseClick(v1, v2, v3);
        }
    }
    
    protected void func_146286_b(final int v1, final int v2, final int v3) {
        /*SL:43*/for (AbstractHudElement a2 = (AbstractHudElement)0; a2 < Ares.hudManager.elements.size(); ++a2) {
            /*SL:45*/a2 = Ares.hudManager.elements.get(a2);
            /*SL:46*/a2.onMouseRelease(v1, v2, v3);
        }
    }
    
    public void func_146273_a(final int a4, final int v1, final int v2, final long v3) {
        /*SL:53*/for (int a5 = 0; a5 < Ares.hudManager.elements.size(); ++a5) {
            final AbstractHudElement a6 = Ares.hudManager.elements.get(/*EL:55*/a5);
            /*SL:56*/a6.mouseClickMove(a4, v1, v2);
        }
    }
    
    public boolean func_73868_f() {
        /*SL:63*/return false;
    }
    
    public void func_146281_b() {
        Ares.hudManager.elements.stream().filter(/*EL:71*/a1 -> a1 instanceof MoveableHudElement).<Object>map(/*EL:72*/a1 -> a1).forEach(/*EL:73*/a1 -> a1.setDragging(false));
    }
}
