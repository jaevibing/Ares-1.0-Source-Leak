package com.ares.hack.hacks.hud;

import javax.annotation.Nullable;
import com.ares.hack.hacks.hud.impl.elements.ChestCountElement;
import com.ares.hack.hacks.hud.impl.elements.PingElement;
import com.ares.hack.hacks.hud.impl.elements.FpsElement;
import com.ares.hack.hacks.hud.impl.elements.TimeElement;
import com.ares.hack.hacks.hud.impl.elements.CrystalCountElement;
import com.ares.hack.hacks.hud.impl.elements.ObbyCountElement;
import com.ares.hack.hacks.hud.impl.elements.TotemCountElement;
import com.ares.hack.hacks.hud.impl.elements.PlayerPreviewElement;
import com.ares.hack.hacks.hud.impl.elements.InventoryPreview;
import com.ares.hack.hacks.hud.impl.elements.Watermark;
import com.ares.hack.hacks.hud.impl.elements.EnabledHacksElement;
import java.util.ArrayList;
import com.ares.hack.hacks.hud.api.AbstractHudElement;
import java.util.List;
import com.ares.Globals;

public class HudManager implements Globals
{
    public final List<AbstractHudElement> elements;
    
    public HudManager() {
        (this.elements = new ArrayList<AbstractHudElement>()).add(new EnabledHacksElement());
        this.elements.add(new Watermark());
        this.elements.add(new InventoryPreview());
        this.elements.add(new PlayerPreviewElement());
        this.elements.add(new TotemCountElement());
        this.elements.add(new ObbyCountElement());
        this.elements.add(new CrystalCountElement());
        this.elements.add(new TimeElement());
        this.elements.add(new FpsElement());
        this.elements.add(new PingElement());
        this.elements.add(new ChestCountElement());
    }
    
    public void bringToFront(final AbstractHudElement a1) {
        /*SL:35*/if (this.elements.contains(a1)) {
            /*SL:37*/this.elements.remove(a1);
            /*SL:38*/this.elements.add(a1);
        }
    }
    
    @Nullable
    public AbstractHudElement getMouseOver(final int v2, final int v3) {
        /*SL:45*/for (AbstractHudElement a2 = (AbstractHudElement)(this.elements.size() - 1); a2 >= 0; --a2) {
            /*SL:47*/a2 = this.elements.get(a2);
            /*SL:48*/if (a2.isMouseOver(v2, v3)) {
                /*SL:49*/return a2;
            }
        }
        /*SL:51*/return null;
    }
}
