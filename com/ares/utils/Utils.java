package com.ares.utils;

import net.minecraft.init.Blocks;
import com.ares.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import java.nio.FloatBuffer;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import net.minecraft.client.renderer.ActiveRenderInfo;
import com.ares.utils.render.Plane;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.Color;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

public class Utils
{
    private static Minecraft mc;
    public static final Block[] SHULKERS;
    
    public static boolean isShulker(final Block a1) {
        /*SL:70*/return ArrayUtils.contains((Object[])Utils.SHULKERS, (Object)a1);
    }
    
    public static String vectorToString(final Vec3d a1, final boolean... a2) {
        final boolean v1 = /*EL:78*/a2.length <= 0 || a2[0];
        final StringBuilder v2 = /*EL:81*/new StringBuilder();
        /*SL:82*/v2.append('(');
        /*SL:84*/v2.append((int)Math.floor(a1.field_72450_a));
        /*SL:85*/v2.append(", ");
        /*SL:87*/if (v1) {
            /*SL:89*/v2.append((int)Math.floor(a1.field_72448_b));
            /*SL:90*/v2.append(", ");
        }
        /*SL:93*/v2.append((int)Math.floor(a1.field_72449_c));
        /*SL:95*/v2.append(")");
        /*SL:97*/return v2.toString();
    }
    
    public static String vectorToString(final BlockPos a1) {
        /*SL:102*/return vectorToString(new Vec3d((Vec3i)a1), new boolean[0]);
    }
    
    public static void drawRainbowString(final String a3, final int a4, final int v1, final float v2) {
        int v3 = /*EL:107*/a4;
        /*SL:108*/for (final char a5 : a3.toCharArray()) {
            final String a6 = /*EL:110*/String.valueOf(a5);
            /*SL:112*/v3 += a4;
        }
    }
    
    public static Color rainbowColour(final long a1, final float a2) {
        final float v1 = /*EL:118*/(System.nanoTime() + a1) / 1.0E10f % 1.0f;
        final Color v2 = /*EL:121*/new Color((int)Long.parseLong(/*EL:122*/Integer.toHexString(/*EL:123*/Color.HSBtoRGB(v1, 1.0f, 1.0f)), 16));
        /*SL:128*/return new Color(v2.getRed() / 255.0f * a2, v2.getGreen() / 255.0f * a2, v2.getBlue() / 255.0f * a2, v2.getAlpha() / 255.0f);
    }
    
    public static double roundDouble(final double a1, final int a2) {
        final double v1 = /*EL:133*/Math.pow(10.0, a2);
        /*SL:134*/return Math.round(a1 * v1) / v1;
    }
    
    public static void copyToClipboard(final String a1) {
        final StringSelection v1 = /*EL:139*/new StringSelection(a1);
        final Clipboard v2 = /*EL:140*/Toolkit.getDefaultToolkit().getSystemClipboard();
        /*SL:141*/v2.setContents(v1, v1);
    }
    
    public static boolean writeFile(final String v1, final String v2) {
        try {
            final BufferedWriter a1 = /*EL:148*/new BufferedWriter(new FileWriter(v1));
            /*SL:149*/a1.write(v2);
            /*SL:150*/a1.close();
            /*SL:151*/return true;
        }
        catch (Exception a2) {
            /*SL:155*/a2.printStackTrace();
            /*SL:157*/return false;
        }
    }
    
    public static Plane threeToTwoDimension(final Vec3d v1) {
        final Entity v2 = Utils.mc.func_175606_aa();
        /*SL:164*/if (v2 == null) {
            return new Plane(0.0, 0.0, false);
        }
        final ActiveRenderInfo v3 = /*EL:166*/new ActiveRenderInfo();
        final Vec3d v4 = Utils.mc.field_71439_g.func_174824_e(Utils.mc.func_184121_ak());
        final Vec3d v5 = /*EL:170*/ActiveRenderInfo.func_178806_a(v2, (double)Utils.mc.func_184121_ak());
        final float v6 = /*EL:172*/(float)(v4.field_72450_a + v5.field_72450_a - (float)v1.field_72450_a);
        final float v7 = /*EL:173*/(float)(v4.field_72448_b + v5.field_72448_b - (float)v1.field_72448_b);
        final float v8 = /*EL:174*/(float)(v4.field_72449_c + v5.field_72449_c - (float)v1.field_72449_c);
        final Vector4f v9 = /*EL:176*/new Vector4f(v6, v7, v8, 1.0f);
        final Matrix4f v10 = /*EL:178*/new Matrix4f();
        /*SL:179*/v10.load(/*EL:181*/(FloatBuffer)ObfuscationReflectionHelper.getPrivateValue((Class)ActiveRenderInfo.class, (Object)new ActiveRenderInfo(), new String[] { "MODELVIEW", "field_178812_b" }));
        final Matrix4f v11 = /*EL:189*/new Matrix4f();
        /*SL:190*/v11.load(/*EL:192*/(FloatBuffer)ObfuscationReflectionHelper.getPrivateValue((Class)ActiveRenderInfo.class, (Object)new ActiveRenderInfo(), new String[] { "PROJECTION", "field_178813_c" }));
        vecByMatrix(/*EL:200*/v9, v10);
        vecByMatrix(/*EL:201*/v9, v11);
        /*SL:203*/if (v9.w > 0.0f) {
            final Vector4f vector4f = /*EL:205*/v9;
            vector4f.x *= -100000.0f;
            final Vector4f vector4f2 = /*EL:206*/v9;
            vector4f2.y *= -100000.0f;
        }
        else {
            final float a1 = /*EL:210*/1.0f / v9.w;
            final Vector4f vector4f3 = /*EL:211*/v9;
            vector4f3.x *= a1;
            final Vector4f vector4f4 = /*EL:212*/v9;
            vector4f4.y *= a1;
        }
        final ScaledResolution v12 = /*EL:215*/new ScaledResolution(Utils.mc);
        final float v13 = /*EL:216*/v12.func_78326_a() / 2.0f;
        final float v14 = /*EL:217*/v12.func_78328_b() / 2.0f;
        /*SL:219*/v9.x = v13 + (0.5f * v9.x * v12.func_78326_a() + 0.5f);
        /*SL:220*/v9.y = v14 - (0.5f * v9.y * v12.func_78328_b() + 0.5f);
        boolean v15 = /*EL:222*/true;
        /*SL:224*/if (v9.x < 0.0f || v9.y < 0.0f || v9.x > v12.func_78326_a() || v9.y > v12.func_78328_b()) {
            /*SL:225*/v15 = false;
        }
        /*SL:227*/return new Plane(v9.x, v9.y, v15);
    }
    
    private static void vecByMatrix(final Vector4f a1, final Matrix4f a2) {
        final float v1 = /*EL:232*/a1.x;
        final float v2 = /*EL:233*/a1.y;
        final float v3 = /*EL:234*/a1.z;
        /*SL:235*/a1.x = v1 * a2.m00 + v2 * a2.m10 + v3 * a2.m20 + a2.m30;
        /*SL:236*/a1.y = v1 * a2.m01 + v2 * a2.m11 + v3 * a2.m21 + a2.m31;
        /*SL:237*/a1.z = v1 * a2.m02 + v2 * a2.m12 + v3 * a2.m22 + a2.m32;
        /*SL:238*/a1.w = v1 * a2.m03 + v2 * a2.m13 + v3 * a2.m23 + a2.m33;
    }
    
    public static float getPlayerDirection() {
        float v1 = Utils.mc.field_71439_g.field_70177_z;
        /*SL:244*/if (Utils.mc.field_71439_g.field_191988_bg < 0.0f) {
            /*SL:246*/v1 += 180.0f;
        }
        float v2 = /*EL:249*/1.0f;
        /*SL:250*/if (Utils.mc.field_71439_g.field_191988_bg < 0.0f) {
            /*SL:252*/v2 = -0.5f;
        }
        else/*SL:254*/ if (Utils.mc.field_71439_g.field_191988_bg > 0.0f) {
            /*SL:256*/v2 = 0.5f;
        }
        /*SL:259*/if (Utils.mc.field_71439_g.field_70702_br > 0.0f) {
            /*SL:261*/v1 -= 90.0f * v2;
        }
        /*SL:263*/if (Utils.mc.field_71439_g.field_70702_br < 0.0f) {
            /*SL:265*/v1 += 90.0f * v2;
        }
        /*SL:268*/v1 *= 0.017453292f;
        /*SL:269*/return v1;
    }
    
    static {
        Utils.mc = Globals.mc;
        SHULKERS = new Block[] { Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA };
    }
}
