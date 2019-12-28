package com.ares.hack.hacks.ui.newgui.api;

import com.ares.utils.data.Vec2i;
import com.ares.Globals;

public class ButtonGroup implements IRenderable, Globals
{
    private static final int X_PADDING = 2;
    private static final int Y_PADDING = 2;
    private static final int X_BORDER = 1;
    private static final int Y_BORDER = 1;
    private int selected;
    private String[] options;
    
    public ButtonGroup(final String[] a1) {
        this(a1, 0);
    }
    
    public ButtonGroup(final String[] a1, final int a2) {
        this.options = a1;
        this.selected = a2;
    }
    
    public Vec2i getSize() {
        int a2 = /*EL:33*/4;
        /*SL:34*/a2 += ButtonGroup.mc.field_71466_p.field_78288_b;
        /*SL:35*/a2 += 2;
        /*SL:37*/a2 = 1;
        /*SL:38*/for (final String v1 : this.options) {
            /*SL:40*/a2 += ButtonGroup.mc.field_71466_p.func_78256_a(v1);
            /*SL:41*/a2 += 4;
            /*SL:42*/++a2;
        }
        /*SL:45*/return new Vec2i(a2, a2);
    }
    
    @Override
    public void onRender(final Vec2i a1) {
    }
    
    @Override
    public void onMouseDown(final Vec2i a1, final int a2) {
    }
    
    @Override
    public void onMouseRelease(final Vec2i a1, final int a2) {
    }
    
    @Override
    public void mouseClickMove(final Vec2i a1, final int a2) {
    }
}
