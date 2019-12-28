package com.ares.gui.buttons;

import com.ares.Globals;
import com.ares.gui.guis.AresGui;

public class TextButtonCentered extends TextButton
{
    public TextButtonCentered(final AresGui a1, final int a2, final int a3, final String a4) {
        super(a1, a2 - Globals.mc.field_71466_p.func_78256_a(a4) / 2, a3, a4);
    }
    
    public TextButtonCentered(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final String a6, final double a7) {
        super(a1, a2 - Globals.mc.field_71466_p.func_78256_a(a6) / 2, a3, a4, a5, a6, a7);
    }
}
