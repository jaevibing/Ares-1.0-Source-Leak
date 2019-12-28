package com.ares.gui.guis.escgui;

import net.minecraft.client.gui.GuiScreen;
import com.ares.hack.optimizations.BaseOptimization;
import com.ares.gui.guis.AresGui;
import com.ares.gui.buttons.ToggleButton;
import com.ares.gui.elements.Element;

public class ElementOptimization extends Element
{
    private static String textColor;
    private static int listPlaces;
    private ToggleButton button;
    private final String text;
    private int listPlace;
    
    public ElementOptimization(final AresGui a1, final BaseOptimization a2) {
        super(a1, a1.field_146294_l / 2, a1.field_146295_m / 4 + 24 * (ElementOptimization.listPlaces - 2) - 16);
        this.text = a2.name;
        this.listPlace = ElementOptimization.listPlaces;
        this.x = a1.field_146294_l / 2 - ElementOptimization.mc.field_71466_p.func_78256_a(this.text) / 2 - 11;
        this.y = a1.field_146295_m / 4 + 24 * (this.listPlace - 2) - 16;
        this.<OptimizationToggleButton>addButton(new OptimizationToggleButton(a1, this.x + ElementOptimization.mc.field_71466_p.func_78256_a(this.text) + 2, this.y + ElementOptimization.mc.field_71466_p.field_78288_b / 2 - 10, 20, 20, a2));
        ++ElementOptimization.listPlaces;
    }
    
    @Override
    public void draw(final GuiScreen a1) {
        /*SL:37*/a1.func_73731_b(ElementOptimization.mc.field_71466_p, this.text, this.x, this.y, Integer.parseInt(ElementOptimization.textColor, 16));
    }
    
    public static void clear() {
        ElementOptimization.listPlaces = /*EL:42*/2;
    }
    
    static {
        ElementOptimization.textColor = "FFFFFF";
        ElementOptimization.listPlaces = 2;
    }
}
