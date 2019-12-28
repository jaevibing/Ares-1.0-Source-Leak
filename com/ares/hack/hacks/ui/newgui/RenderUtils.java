package com.ares.hack.hacks.ui.newgui;

import org.lwjgl.opengl.GL11;
import com.ares.utils.data.Vec2i;

public class RenderUtils
{
    public static void renderOutline(final Vec2i a1, final Vec2i a2) {
        /*SL:13*/GL11.glBegin(2);
        /*SL:14*/GL11.glVertex2i(a1.x, a1.y);
        /*SL:15*/GL11.glVertex2i(a2.x, a1.y);
        /*SL:16*/GL11.glVertex2i(a2.x, a2.y);
        /*SL:17*/GL11.glVertex2i(a1.x, a2.y);
        /*SL:18*/GL11.glEnd();
    }
}
