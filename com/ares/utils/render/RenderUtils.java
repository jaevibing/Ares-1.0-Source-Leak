package com.ares.utils.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import com.ares.Globals;
import net.minecraft.client.renderer.entity.RenderManager;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;

public class RenderUtils
{
    public static void circleESP(final BlockPos a2, final double a3, final Color v1) throws Exception {
        final double v2 = /*EL:40*/(double)ObfuscationReflectionHelper.getPrivateValue((Class)RenderManager.class, (Object)Globals.mc.func_175598_ae(), new String[] { "renderPosX", "field_78725_b" });
        final double v3 = /*EL:41*/(double)ObfuscationReflectionHelper.getPrivateValue((Class)RenderManager.class, (Object)Globals.mc.func_175598_ae(), new String[] { "renderPosY", "field_78726_c" });
        final double v4 = /*EL:42*/(double)ObfuscationReflectionHelper.getPrivateValue((Class)RenderManager.class, (Object)Globals.mc.func_175598_ae(), new String[] { "renderPosZ", "field_78723_d" });
        final double v5 = /*EL:45*/a2.func_177958_n() + 0.5 - v2;
        final double v6 = /*EL:46*/a2.func_177956_o() - v3;
        final double v7 = /*EL:47*/a2.func_177952_p() + 0.5 - v4;
        /*SL:48*/GL11.glPushMatrix();
        /*SL:49*/GL11.glBlendFunc(770, 771);
        /*SL:50*/GL11.glEnable(3042);
        /*SL:51*/GL11.glLineWidth(2.0f);
        /*SL:52*/GL11.glDisable(3553);
        /*SL:53*/GL11.glDisable(2929);
        /*SL:54*/GL11.glDepthMask(false);
        /*SL:55*/GL11.glColor4d((double)(v1.getRed() / 255.0f), (double)(v1.getGreen() / 255.0f), (double)(v1.getBlue() / 255.0f), 0.25);
        /*SL:56*/GL11.glBegin(9);
        /*SL:57*/for (int a4 = 0; a4 <= 360; ++a4) {
            /*SL:58*/GL11.glVertex3d(v5 + Math.sin(a4 * 3.141592653589793 / 180.0) * a3, v6, v7 + Math.cos(a4 * 3.141592653589793 / 180.0) * a3);
        }
        /*SL:60*/GL11.glEnd();
        /*SL:61*/GL11.glLineWidth(2.0f);
        /*SL:62*/GL11.glEnable(3553);
        /*SL:63*/GL11.glEnable(2929);
        /*SL:64*/GL11.glDepthMask(true);
        /*SL:65*/GL11.glDisable(3042);
        /*SL:66*/GL11.glPopMatrix();
        /*SL:67*/GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void blockEsp(final AxisAlignedBB a1, final int a2, final int a3, final int a4, final float a5) {
        /*SL:72*/GL11.glPushMatrix();
        /*SL:73*/GL11.glBlendFunc(770, 771);
        /*SL:74*/GL11.glEnable(3042);
        /*SL:75*/GL11.glLineWidth(2.0f);
        /*SL:76*/GL11.glDisable(3553);
        /*SL:77*/GL11.glDisable(2929);
        /*SL:78*/GL11.glDepthMask(false);
        /*SL:79*/GL11.glColor4d((double)(a2 / 255.0f), (double)(a3 / 255.0f), (double)(a4 / 255.0f), (double)a5);
        drawColorBox(/*EL:80*/a1, 0.0f, 0.0f, 0.0f, 0.0f);
        /*SL:81*/GL11.glColor4d((double)(a2 / 255.0f), (double)(a3 / 255.0f), (double)(a4 / 255.0f), (double)a5);
        drawSelectionBoundingBox(/*EL:82*/a1);
        /*SL:83*/GL11.glLineWidth(2.0f);
        /*SL:84*/GL11.glEnable(3553);
        /*SL:85*/GL11.glEnable(2929);
        /*SL:86*/GL11.glDepthMask(true);
        /*SL:87*/GL11.glDisable(3042);
        /*SL:88*/GL11.glPopMatrix();
    }
    
    public static void blockEsp(final BlockPos a1, final int a2, final int a3, final int a4, final float a5, final double a6, final double a7) {
        final double v1 = /*EL:93*/a1.func_177958_n();
        final double v2 = /*EL:94*/a1.func_177956_o();
        final double v3 = /*EL:95*/a1.func_177952_p();
        blockEsp(/*EL:96*/new AxisAlignedBB(v1, v2, v3, v1 + a7, v2 + 1.0, v3 + a6), a2, a3, a4, a5);
    }
    
    public static void drawColorBox(final AxisAlignedBB a1, final float a2, final float a3, final float a4, final float a5) {
        final Tessellator v1 = /*EL:101*/Tessellator.func_178181_a();
        final BufferBuilder v2 = /*EL:102*/v1.func_178180_c();
        /*SL:103*/v2.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        /*SL:104*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:105*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:106*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:107*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:108*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:109*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:110*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:111*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:112*/v1.func_78381_a();
        /*SL:113*/v2.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        /*SL:114*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:115*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:116*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:117*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:118*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:119*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:120*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:121*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:122*/v1.func_78381_a();
        /*SL:123*/v2.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        /*SL:124*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:125*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:126*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:127*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:128*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:129*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:130*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:131*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:132*/v1.func_78381_a();
        /*SL:133*/v2.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        /*SL:134*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:135*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:136*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:137*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:138*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:139*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:140*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:141*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:142*/v1.func_78381_a();
        /*SL:143*/v2.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        /*SL:144*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:145*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:146*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:147*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:148*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:149*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:150*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:151*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:152*/v1.func_78381_a();
        /*SL:153*/v2.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        /*SL:154*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:155*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:156*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:157*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:158*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:159*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:160*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:161*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f).func_181666_a(a2, a3, a4, a5).func_181675_d();
        /*SL:162*/v1.func_78381_a();
    }
    
    public static void drawSelectionBoundingBox(final AxisAlignedBB a1) {
        final Tessellator v1 = /*EL:166*/Tessellator.func_178181_a();
        final BufferBuilder v2 = /*EL:167*/v1.func_178180_c();
        /*SL:168*/v2.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        /*SL:169*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181675_d();
        /*SL:170*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c).func_181675_d();
        /*SL:171*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f).func_181675_d();
        /*SL:172*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f).func_181675_d();
        /*SL:173*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181675_d();
        /*SL:174*/v1.func_78381_a();
        /*SL:175*/v2.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        /*SL:176*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181675_d();
        /*SL:177*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c).func_181675_d();
        /*SL:178*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f).func_181675_d();
        /*SL:179*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f).func_181675_d();
        /*SL:180*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181675_d();
        /*SL:181*/v1.func_78381_a();
        /*SL:182*/v2.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        /*SL:183*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c).func_181675_d();
        /*SL:184*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c).func_181675_d();
        /*SL:185*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c).func_181675_d();
        /*SL:186*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c).func_181675_d();
        /*SL:187*/v2.func_181662_b(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f).func_181675_d();
        /*SL:188*/v2.func_181662_b(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f).func_181675_d();
        /*SL:189*/v2.func_181662_b(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f).func_181675_d();
        /*SL:190*/v2.func_181662_b(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f).func_181675_d();
        /*SL:191*/v1.func_78381_a();
    }
    
    public static void drawSolidEntityESP(final double a1, final double a2, final double a3, final double a4, final double a5, final float a6, final float a7, final float a8, final float a9) {
        /*SL:196*/GL11.glPushMatrix();
        /*SL:197*/GL11.glEnable(3042);
        /*SL:198*/GL11.glBlendFunc(770, 771);
        /*SL:200*/GL11.glDisable(3553);
        /*SL:201*/GL11.glEnable(2848);
        /*SL:202*/GL11.glDisable(2929);
        /*SL:203*/GL11.glDepthMask(false);
        /*SL:204*/GL11.glColor4f(a6, a7, a8, a9);
        drawSelectionBoundingBox(/*EL:205*/new AxisAlignedBB(a1 - a4, a2, a3 - a4, a1 + a4, a2 + a5, a3 + a4));
        /*SL:206*/GL11.glDisable(2848);
        /*SL:207*/GL11.glEnable(3553);
        /*SL:209*/GL11.glEnable(2929);
        /*SL:210*/GL11.glDepthMask(true);
        /*SL:211*/GL11.glDisable(3042);
        /*SL:212*/GL11.glPopMatrix();
    }
    
    public static void drawSolidBox(final AxisAlignedBB a1) {
        /*SL:217*/GL11.glBegin(7);
        /*SL:219*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c);
        /*SL:220*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c);
        /*SL:221*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f);
        /*SL:222*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f);
        /*SL:224*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c);
        /*SL:225*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f);
        /*SL:226*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f);
        /*SL:227*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c);
        /*SL:229*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c);
        /*SL:230*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c);
        /*SL:231*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c);
        /*SL:232*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c);
        /*SL:234*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c);
        /*SL:235*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c);
        /*SL:236*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f);
        /*SL:237*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f);
        /*SL:239*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f);
        /*SL:240*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f);
        /*SL:241*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f);
        /*SL:242*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f);
        /*SL:244*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c);
        /*SL:245*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f);
        /*SL:246*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f);
        /*SL:247*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c);
        /*SL:249*/GL11.glEnd();
    }
    
    public static void drawOutlinedBox(final AxisAlignedBB a1) {
        /*SL:254*/GL11.glBegin(1);
        /*SL:256*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c);
        /*SL:257*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c);
        /*SL:259*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c);
        /*SL:260*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f);
        /*SL:262*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f);
        /*SL:263*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f);
        /*SL:265*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f);
        /*SL:266*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c);
        /*SL:268*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72339_c);
        /*SL:269*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c);
        /*SL:271*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72339_c);
        /*SL:272*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c);
        /*SL:274*/GL11.glVertex3d(a1.field_72336_d, a1.field_72338_b, a1.field_72334_f);
        /*SL:275*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f);
        /*SL:277*/GL11.glVertex3d(a1.field_72340_a, a1.field_72338_b, a1.field_72334_f);
        /*SL:278*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f);
        /*SL:280*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c);
        /*SL:281*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c);
        /*SL:283*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72339_c);
        /*SL:284*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f);
        /*SL:286*/GL11.glVertex3d(a1.field_72336_d, a1.field_72337_e, a1.field_72334_f);
        /*SL:287*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f);
        /*SL:289*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72334_f);
        /*SL:290*/GL11.glVertex3d(a1.field_72340_a, a1.field_72337_e, a1.field_72339_c);
        /*SL:292*/GL11.glEnd();
    }
    
    public static void glStart() {
        /*SL:297*/GL11.glEnable(3042);
        /*SL:298*/GL11.glBlendFunc(770, 771);
        /*SL:299*/GL11.glEnable(2848);
        /*SL:300*/GL11.glLineWidth(2.0f);
        /*SL:301*/GL11.glDisable(3553);
        /*SL:302*/GL11.glEnable(2884);
        /*SL:303*/GL11.glDisable(2929);
        final double v1 = Globals.mc.func_175598_ae().field_78730_l;
        final double v2 = Globals.mc.func_175598_ae().field_78731_m;
        final double v3 = Globals.mc.func_175598_ae().field_78728_n;
        /*SL:309*/GL11.glPushMatrix();
        /*SL:310*/GL11.glTranslated(-v1, -v2, -v3);
    }
    
    public static void glStart(final float a1, final float a2, final float a3, final float a4) {
        glStart();
        /*SL:317*/GL11.glColor4f(a1, a2, a3, a4);
    }
    
    public static void glEnd() {
        /*SL:322*/GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        /*SL:323*/GL11.glPopMatrix();
        /*SL:325*/GL11.glEnable(2929);
        /*SL:326*/GL11.glEnable(3553);
        /*SL:327*/GL11.glDisable(3042);
        /*SL:328*/GL11.glDisable(2848);
    }
    
    public static AxisAlignedBB getBoundingBox(final BlockPos a1) {
        /*SL:333*/return Globals.mc.field_71441_e.func_180495_p(a1).func_185900_c((IBlockAccess)Globals.mc.field_71441_e, a1).func_186670_a(a1);
    }
    
    public static void drawRect(final int a3, int a4, int a5, int a6, int v1, final int v2) {
        /*SL:338*/if (a4 < a6) {
            final int a7 = /*EL:340*/a4;
            /*SL:341*/a4 = a6;
            /*SL:342*/a6 = a7;
        }
        /*SL:345*/if (a5 < v1) {
            final int a8 = /*EL:347*/a5;
            /*SL:348*/a5 = v1;
            /*SL:349*/v1 = a8;
        }
        final float v3 = /*EL:352*/(v2 >> 24 & 0xFF) / 255.0f;
        final float v4 = /*EL:353*/(v2 >> 16 & 0xFF) / 255.0f;
        final float v5 = /*EL:354*/(v2 >> 8 & 0xFF) / 255.0f;
        final float v6 = /*EL:355*/(v2 & 0xFF) / 255.0f;
        final Tessellator v7 = /*EL:356*/Tessellator.func_178181_a();
        final BufferBuilder v8 = /*EL:357*/v7.func_178180_c();
        /*SL:358*/GlStateManager.func_179147_l();
        /*SL:359*/GlStateManager.func_179090_x();
        /*SL:360*/GlStateManager.func_179120_a(770, 771, 1, 0);
        /*SL:361*/GlStateManager.func_179131_c(v4, v5, v6, v3);
        /*SL:362*/v8.func_181668_a(a3, DefaultVertexFormats.field_181705_e);
        /*SL:363*/v8.func_181662_b((double)a4, (double)v1, 0.0).func_181675_d();
        /*SL:364*/v8.func_181662_b((double)a6, (double)v1, 0.0).func_181675_d();
        /*SL:365*/v8.func_181662_b((double)a6, (double)a5, 0.0).func_181675_d();
        /*SL:366*/v8.func_181662_b((double)a4, (double)a5, 0.0).func_181675_d();
        /*SL:367*/v7.func_78381_a();
        /*SL:368*/GlStateManager.func_179098_w();
        /*SL:369*/GlStateManager.func_179084_k();
    }
}
