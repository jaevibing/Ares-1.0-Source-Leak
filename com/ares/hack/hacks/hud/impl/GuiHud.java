package com.ares.hack.hacks.hud.impl;

import com.ares.hack.hacks.hud.api.AbstractHudElement;
import com.ares.Ares;
import net.minecraft.client.gui.Gui;

public class GuiHud extends Gui
{
    public static void drawScreen(final int a1, final int a2, final float a3) {
        Ares.hudManager.elements.stream().filter(/*EL:12*/AbstractHudElement::isVisible).forEach(/*EL:13*/a4 -> a4.render(a1, a2, a3));
    }
}
