package com.ares.utils.csgo;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import com.ares.Globals;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;

public class CsgoRenderUtils
{
    public static void renderText3d(final String a1, final Vec3d a2) {
        final float v1 = /*EL:15*/(float)ObfuscationReflectionHelper.getPrivateValue((Class)RenderManager.class, (Object)Globals.mc.func_175598_ae(), new String[] { "playerViewX", "field_78732_j" });
        final float v2 = /*EL:16*/(float)ObfuscationReflectionHelper.getPrivateValue((Class)RenderManager.class, (Object)Globals.mc.func_175598_ae(), new String[] { "playerViewY", "field_78735_i" });
        final float v3 = /*EL:18*/1.6f;
        final float v4 = /*EL:19*/(float)(0.01666666753590107 * Globals.mc.field_71439_g.func_70011_f(a2.field_72450_a, a2.field_72448_b, a2.field_72449_c) / 2.0);
        /*SL:20*/GL11.glPushMatrix();
        /*SL:21*/GL11.glTranslatef((float)a2.field_72450_a, (float)a2.field_72448_b, (float)a2.field_72449_c);
        /*SL:22*/GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        /*SL:23*/GL11.glRotatef(-v2, 0.0f, 1.0f, 0.0f);
        /*SL:24*/GL11.glRotatef(v1, 1.0f, 0.0f, 0.0f);
        /*SL:25*/GL11.glScalef(-v4, -v4, v4);
        /*SL:26*/GL11.glDepthMask(false);
        /*SL:27*/GL11.glDisable(2896);
        final Tessellator v5 = /*EL:28*/Tessellator.func_178181_a();
        final BufferBuilder v6 = /*EL:29*/v5.func_178180_c();
        final int v7 = (int)(-Globals.mc.field_71439_g.func_70011_f(/*EL:30*/a2.field_72450_a, a2.field_72448_b, a2.field_72449_c)) / (int)v3;
        /*SL:38*/GL11.glDisable(3553);
        final int v8 = Globals.mc.field_71466_p.func_78256_a(/*EL:39*/a1) / 2;
        Globals.mc.field_71466_p.func_175063_a(/*EL:42*/a1, (float)(-v8), (float)v7, 16777215);
        Globals.mc.field_71460_t.func_175072_h();
        /*SL:45*/GL11.glLineWidth(1.0f);
        /*SL:46*/GL11.glEnable(3553);
        /*SL:47*/GL11.glDisable(3042);
        /*SL:48*/GL11.glDisable(2896);
        /*SL:49*/GL11.glBlendFunc(770, 771);
        /*SL:50*/GL11.glEnable(2929);
        /*SL:51*/GL11.glDepthMask(true);
        /*SL:53*/GL11.glPopMatrix();
    }
}
