package com.ares.utils.render;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import com.ares.Globals;

public class Render2dUtils implements Globals
{
    public static void drawRect(final int a1, final int a2, final int a3, final int a4, final int a5) {
        final float v1 = /*EL:19*/(a5 >> 24 & 0xFF) / 255.0f;
        final float v2 = /*EL:20*/(a5 >> 16 & 0xFF) / 255.0f;
        final float v3 = /*EL:21*/(a5 >> 8 & 0xFF) / 255.0f;
        final float v4 = /*EL:22*/(a5 & 0xFF) / 255.0f;
        final Tessellator v5 = /*EL:24*/Tessellator.func_178181_a();
        final BufferBuilder v6 = /*EL:25*/v5.func_178180_c();
        /*SL:26*/GlStateManager.func_179147_l();
        /*SL:27*/GlStateManager.func_179090_x();
        /*SL:28*/GlStateManager.func_179120_a(770, 771, 1, 0);
        /*SL:29*/v6.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        /*SL:31*/v6.func_181662_b((double)a1, (double)a4, 0.0).func_181666_a(v2, v3, v4, v1).func_181675_d();
        /*SL:32*/v6.func_181662_b((double)a3, (double)a4, 0.0).func_181666_a(v2, v3, v4, v1).func_181675_d();
        /*SL:33*/v6.func_181662_b((double)a3, (double)a2, 0.0).func_181666_a(v2, v3, v4, v1).func_181675_d();
        /*SL:34*/v6.func_181662_b((double)a1, (double)a2, 0.0).func_181666_a(v2, v3, v4, v1).func_181675_d();
        /*SL:36*/v5.func_78381_a();
        /*SL:37*/GlStateManager.func_179098_w();
        /*SL:38*/GlStateManager.func_179084_k();
    }
    
    public static void drawBorderedRect(final int a1, final int a2, final int a3, final int a4, final Color a5) {
        /*SL:43*/GL11.glDisable(3553);
        /*SL:44*/GL11.glColor4f((float)a5.getRed(), (float)a5.getBlue(), (float)a5.getGreen(), (float)a5.getAlpha());
        /*SL:45*/GL11.glPushMatrix();
        /*SL:46*/GL11.glLineWidth(1.0f);
        /*SL:48*/GL11.glBegin(1);
        /*SL:51*/GL11.glVertex2d((double)a1, (double)a2);
        /*SL:53*/GL11.glVertex2d((double)(a1 + a3), (double)a2);
        /*SL:55*/GL11.glVertex2d((double)(a1 + a3), (double)(a2 + a4));
        /*SL:57*/GL11.glVertex2d((double)a1, (double)(a2 + a4));
        /*SL:59*/GL11.glEnd();
        /*SL:60*/GL11.glPopMatrix();
        /*SL:61*/GL11.glEnable(3553);
    }
    
    public static void renderBlockOnScreen(final int a1, final int a2, final Block a3) {
        renderItemOnScreen(/*EL:67*/a1, a2, new ItemStack(a3));
    }
    
    public static void renderItemOnScreen(final int a1, final int a2, final ItemStack a3) {
        /*SL:72*/GlStateManager.func_179094_E();
        /*SL:73*/RenderHelper.func_74519_b();
        /*SL:74*/GlStateManager.func_179091_B();
        /*SL:75*/GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
        /*SL:76*/GlStateManager.func_179109_b(0.0f, 0.0f, 700.0f);
        /*SL:77*/RenderHelper.func_74520_c();
        Render2dUtils.mc.func_175599_af().func_180450_b(/*EL:79*/a3, a1, a2);
        Render2dUtils.mc.func_175599_af().func_175030_a(Render2dUtils.mc.field_71466_p, /*EL:80*/a3, a1, a2);
        /*SL:82*/RenderHelper.func_74518_a();
        /*SL:83*/GlStateManager.func_179097_i();
        /*SL:84*/GlStateManager.func_179101_C();
        /*SL:85*/GlStateManager.func_179121_F();
        /*SL:86*/GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
