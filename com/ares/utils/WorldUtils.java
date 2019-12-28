package com.ares.utils;

import net.minecraft.item.ItemBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import com.ares.Globals;

public class WorldUtils implements Globals
{
    public static void placeBlockMainHand(final BlockPos a1) {
        placeBlock(EnumHand.MAIN_HAND, /*EL:37*/a1);
    }
    
    public static void placeBlock(final EnumHand v-8, final BlockPos v-7) {
        final Vec3d vec3d = /*EL:43*/new Vec3d(WorldUtils.mc.field_71439_g.field_70165_t, WorldUtils.mc.field_71439_g.field_70163_u + WorldUtils.mc.field_71439_g.func_70047_e(), WorldUtils.mc.field_71439_g.field_70161_v);
        /*SL:46*/for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos a1 = /*EL:48*/v-7.func_177972_a(enumFacing);
            final EnumFacing a2 = /*EL:49*/enumFacing.func_176734_d();
            /*SL:59*/if (WorldUtils.mc.field_71441_e.func_180495_p(a1).func_177230_c().func_176209_a(WorldUtils.mc.field_71441_e.func_180495_p(a1), false)) {
                final Vec3d v1 = /*EL:62*/new Vec3d((Vec3i)a1).func_72441_c(0.5, 0.5, 0.5).func_178787_e(/*EL:63*/new Vec3d(a2.func_176730_m()).func_186678_a(0.5));
                /*SL:66*/if (vec3d.func_72436_e(v1) <= 18.0625) {
                    final double v2 = /*EL:70*/v1.field_72450_a - vec3d.field_72450_a;
                    final double v3 = /*EL:71*/v1.field_72448_b - vec3d.field_72448_b;
                    final double v4 = /*EL:72*/v1.field_72449_c - vec3d.field_72449_c;
                    final double v5 = /*EL:74*/Math.sqrt(v2 * v2 + v4 * v4);
                    final float v6 = /*EL:76*/(float)Math.toDegrees(Math.atan2(v4, v2)) - 90.0f;
                    final float v7 = /*EL:77*/(float)(-Math.toDegrees(Math.atan2(v3, v5)));
                    final float[] v8 = /*EL:79*/{ WorldUtils.mc.field_71439_g.field_70177_z + /*EL:81*/MathHelper.func_76142_g(v6 - WorldUtils.mc.field_71439_g.field_70177_z), WorldUtils.mc.field_71439_g.field_70125_A + /*EL:83*/MathHelper.func_76142_g(v7 - WorldUtils.mc.field_71439_g.field_70125_A) };
                    WorldUtils.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:85*/(Packet)new CPacketPlayer.Rotation(v8[0], v8[1], WorldUtils.mc.field_71439_g.field_70122_E));
                    WorldUtils.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:87*/(Packet)new CPacketEntityAction((Entity)WorldUtils.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                    WorldUtils.mc.field_71442_b.func_187099_a(WorldUtils.mc.field_71439_g, WorldUtils.mc.field_71441_e, /*EL:88*/a1, a2, v1, v-8);
                    WorldUtils.mc.field_71439_g.func_184609_a(/*EL:90*/v-8);
                    WorldUtils.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:91*/(Packet)new CPacketEntityAction((Entity)WorldUtils.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                    /*SL:93*/return;
                }
            }
        }
    }
    
    public static int findBlock(final Block a1) {
        /*SL:99*/return findItem(new ItemStack(a1).func_77973_b());
    }
    
    public static int findItem(final Item v0) {
        try {
            /*SL:106*/for (int v = 0; v < 9; ++v) {
                final ItemStack a1 = WorldUtils.mc.field_71439_g.field_71071_by.func_70301_a(/*EL:108*/v);
                /*SL:109*/if (v0 == a1.func_77973_b()) {
                    /*SL:111*/return v;
                }
            }
        }
        catch (Exception ex) {}
        /*SL:115*/return -1;
    }
    
    public static double[] calculateLookAt(final double a1, final double a2, final double a3, final EntityPlayer a4) {
        double v1 = /*EL:121*/a4.field_70165_t - a1;
        double v2 = /*EL:122*/a4.field_70163_u - a2;
        double v3 = /*EL:123*/a4.field_70161_v - a3;
        final double v4 = /*EL:125*/Math.sqrt(v1 * v1 + v2 * v2 + v3 * v3);
        /*SL:127*/v1 /= v4;
        /*SL:128*/v2 /= v4;
        /*SL:129*/v3 /= v4;
        double v5 = /*EL:131*/Math.asin(v2);
        double v6 = /*EL:132*/Math.atan2(v3, v1);
        /*SL:135*/v5 = v5 * 180.0 / 3.141592653589793;
        /*SL:136*/v6 = v6 * 180.0 / 3.141592653589793;
        /*SL:138*/v6 += 90.0;
        /*SL:140*/return new double[] { v6, v5 };
    }
    
    public static void rotate(final float a1, final float a2) {
        WorldUtils.mc.field_71439_g.field_70177_z = /*EL:146*/a1;
        WorldUtils.mc.field_71439_g.field_70125_A = /*EL:147*/a2;
    }
    
    public static void rotate(final double[] a1) {
        WorldUtils.mc.field_71439_g.field_70177_z = /*EL:152*/(float)a1[0];
        WorldUtils.mc.field_71439_g.field_70125_A = /*EL:153*/(float)a1[1];
    }
    
    public static void lookAtBlock(final BlockPos a1) {
        rotate(calculateLookAt(/*EL:158*/a1.func_177958_n(), a1.func_177956_o(), a1.func_177952_p(), (EntityPlayer)WorldUtils.mc.field_71439_g));
    }
    
    public static BlockPos getRelativeBlockPos(final EntityPlayer a4, final int v1, final int v2, final int v3) {
        final int[] v4 = /*EL:164*/{ (int)a4.field_70165_t, (int)a4.field_70163_u, (int)a4.field_70161_v };
        final BlockPos v5;
        /*SL:167*/if (a4.field_70165_t < 0.0 && a4.field_70161_v < 0.0) {
            final BlockPos a5 = /*EL:169*/new BlockPos(v4[0] + v1 - 1, v4[1] + v2, v4[2] + v3 - 1);
        }
        else/*SL:170*/ if (a4.field_70165_t < 0.0 && a4.field_70161_v > 0.0) {
            final BlockPos a6 = /*EL:172*/new BlockPos(v4[0] + v1 - 1, v4[1] + v2, v4[2] + v3);
        }
        else/*SL:173*/ if (a4.field_70165_t > 0.0 && a4.field_70161_v < 0.0) {
            final BlockPos a7 = /*EL:175*/new BlockPos(v4[0] + v1, v4[1] + v2, v4[2] + v3 - 1);
        }
        else {
            /*SL:178*/v5 = new BlockPos(v4[0] + v1, v4[1] + v2, v4[2] + v3);
        }
        /*SL:181*/return v5;
    }
    
    public static int getBlockInHotbar() {
        /*SL:187*/for (int v1 = 0; v1 < 9; ++v1) {
            if (WorldUtils.mc.field_71439_g.field_71071_by.func_70301_a(/*EL:189*/v1) != ItemStack.field_190927_a && WorldUtils.mc.field_71439_g.field_71071_by.func_70301_a(v1).func_77973_b() instanceof ItemBlock && Block.func_149634_a(WorldUtils.mc.field_71439_g.field_71071_by.func_70301_a(v1).func_77973_b()).func_176223_P().func_185913_b()) {
                /*SL:194*/return v1;
            }
        }
        /*SL:197*/return -1;
    }
}
