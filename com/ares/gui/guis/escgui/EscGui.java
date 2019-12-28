package com.ares.gui.guis.escgui;

import net.minecraft.client.gui.GuiButton;
import java.util.Iterator;
import com.ares.hack.optimizations.BaseOptimization;
import com.ares.gui.elements.ElementRectangle;
import com.ares.utils.ColourUtils;
import com.ares.gui.elements.ElementTextCentered;
import java.util.ArrayList;
import com.ares.gui.guis.AresGui;

public class EscGui extends AresGui
{
    public static EscGui INSTANCE;
    private ArrayList<ElementOptimization> elementOptimizations;
    
    public EscGui() {
        this.elementOptimizations = new ArrayList<ElementOptimization>();
    }
    
    public void func_73866_w_() {
        /*SL:21*/this.drawDefaultBackground = true;
        /*SL:23*/this.<ElementTextCentered>addElement(new ElementTextCentered(this, this.field_146294_l / 2, 40, "Ares Client Optimizations", 1.5, "FFFFFF"));
        /*SL:24*/this.<ElementRectangle>addElement(new ElementRectangle(this, this.field_146294_l / 2 - (int)(this.field_146297_k.field_71466_p.func_78256_a("Ares Client Optimizations") * 1.5) / 2, 40 + (int)(this.field_146297_k.field_71466_p.field_78288_b * 1.5) + 1, (int)(this.field_146297_k.field_71466_p.func_78256_a("Ares Client Optimizations") * 1.5), 1, ColourUtils.getAresRed(1.0f)));
        /*SL:26*/for (final BaseOptimization v1 : BaseOptimization.getAll()) {
            /*SL:28*/this.elementOptimizations.add(new ElementOptimization(this, v1));
        }
        /*SL:31*/super.func_73866_w_();
    }
    
    public void func_146284_a(final GuiButton a1) {
        /*SL:37*/if (a1 instanceof OptimizationToggleButton) {
            /*SL:39*/((OptimizationToggleButton)a1).toggle();
        }
    }
    
    public void func_146281_b() {
        /*SL:46*/ElementOptimization.clear();
        /*SL:47*/this.elementOptimizations.clear();
    }
    
    static {
        EscGui.INSTANCE = new EscGui();
    }
}
