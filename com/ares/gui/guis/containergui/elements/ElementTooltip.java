package com.ares.gui.guis.containergui.elements;

import com.ares.hack.hacks.BaseHack;
import com.ares.gui.guis.AresGui;
import com.ares.gui.elements.ElementText;
import com.ares.gui.elements.ElementRectangle;
import com.ares.gui.elements.Element;

public class ElementTooltip extends Element
{
    ElementRectangle tooltipRect;
    ElementText tooltipText;
    
    public ElementTooltip(final AresGui a1, final BaseHack a2) {
        super(a1, 0, 0);
        this.tooltipRect = this.<ElementRectangle>addElement(new ElementRectangle(a1, 0, 0, ElementTooltip.mc.field_71466_p.func_78256_a(a2.description) + 1, ElementTooltip.mc.field_71466_p.field_78288_b + 1, new float[] { 1.0f, 1.0f, 1.0f, 1.0f }));
        this.tooltipText = this.<ElementText>addElement(new ElementText(a1, 0, 0, a2.description, "000000"));
        this.setVisible(false);
    }
}
