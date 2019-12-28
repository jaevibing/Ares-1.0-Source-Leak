package com.ares.hack.hacks.combat;

import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import com.ares.utils.render.RenderUtils;
import java.util.Comparator;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.Iterator;
import java.util.List;
import com.ares.utils.WorldUtils;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import com.ares.utils.data.FriendUtils;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import net.minecraft.init.Items;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.settings.number.IntegerSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto Crystal", description = "Auto Place Crystals, Modified Kami paste", category = EnumCategory.COMBAT)
public class AutoCrystal extends BaseHack
{
    private Setting<Integer> placePerTick;
    private Setting<Boolean> shouldPlace;
    private Setting<Boolean> shouldBreak;
    private Setting<Boolean> shouldSwitch;
    private Setting<Boolean> noGappleSwitch;
    private Setting<Boolean> dontCancelMining;
    private Setting<Double> placeRange;
    private Setting<Double> breakRange;
    private Setting<Double> throughWallsRange;
    private Setting<Double> minDamage;
    private BlockPos currentTarget;
    private Entity currentEntTarget;
    private long systemTime;
    
    public AutoCrystal() {
        this.placePerTick = new IntegerSetting("Place Per Tick", this, 1, 0, 6);
        this.shouldPlace = new BooleanSetting("Place", this, true);
        this.shouldBreak = new BooleanSetting("Break", this, true);
        this.shouldSwitch = new BooleanSetting("Switch", this, true);
        this.noGappleSwitch = new BooleanSetting("No Gapple Switch", this, true);
        this.dontCancelMining = new BooleanSetting("Dont cancel mining", this, true);
        this.placeRange = new DoubleSetting("Place Range", this, 4.0, 0.0, 10.0);
        this.breakRange = new DoubleSetting("Break Range", this, 4.0, 0.0, 10.0);
        this.throughWallsRange = new DoubleSetting("Raytrace Place Range", this, 3.0, 0.0, 10.0);
        this.minDamage = new DoubleSetting("Min Damage", this, 4.0, 0.0, 20.0);
        this.systemTime = -1L;
    }
    
    public void onLogic() {
        /*SL:69*/if (!this.getEnabled()) {
            return;
        }
        /*SL:71*/if (this.shouldBreak.getValue()) {
            /*SL:72*/this.breakCrystals();
        }
        /*SL:74*/if (this.shouldPlace.getValue()) {
            /*SL:76*/for (int v1 = 0; v1 < this.placePerTick.getValue(); ++v1) {
                /*SL:78*/this.placeCrystals();
            }
        }
    }
    
    private void placeCrystals() {
        /*SL:87*/this.currentTarget = null;
        /*SL:88*/this.currentEntTarget = null;
        final boolean b = /*EL:90*/!this.noGappleSwitch.getValue() || AutoCrystal.mc.field_71439_g.func_184607_cu().func_77973_b() != Items.field_151153_ao;
        final boolean b2 = /*EL:91*/!this.dontCancelMining.getValue() || AutoCrystal.mc.field_71439_g.func_184607_cu().func_77973_b() != Items.field_151046_w;
        /*SL:96*/if (!b || !b2) {
            /*SL:97*/return;
        }
        final List<BlockPos> availableCrystalBlocks = /*EL:106*/this.findAvailableCrystalBlocks();
        final List<EntityPlayer> list = /*EL:107*/new ArrayList<EntityPlayer>();
        /*SL:109*/for (final EntityPlayer v1 : AutoCrystal.mc.field_71441_e.field_73010_i) {
            /*SL:111*/if (!FriendUtils.isFriend(v1)) {
                /*SL:113*/list.add(v1);
            }
        }
        double v2 = /*EL:118*/0.1;
        double v3 = /*EL:119*/1000.0;
        BlockPos v4 = /*EL:120*/null;
        /*SL:123*/for (final EntityPlayer v5 : list) {
            /*SL:126*/if (!v5.func_110124_au().equals(AutoCrystal.mc.field_71439_g.func_110124_au())) {
                if (v5.field_70128_L) {
                    /*SL:127*/continue;
                }
                /*SL:130*/for (final BlockPos v6 : availableCrystalBlocks) {
                    /*SL:133*/if (v5.func_174818_b(v6) >= 169.0) {
                        /*SL:134*/continue;
                    }
                    final double v7 = calculateDamage(/*EL:136*/v6.func_177958_n() + /*EL:137*/0.5, v6.func_177956_o() + 1, v6.func_177952_p() + 0.5, (Entity)v5) / 10.0f;
                    final double v8 = calculateDamage(/*EL:138*/v6.func_177958_n() + /*EL:139*/0.5, v6.func_177956_o() + 1, v6.func_177952_p() + 0.5, (Entity)AutoCrystal.mc.field_71439_g) / 10.0f;
                    /*SL:142*/if (v7 < this.minDamage.getValue()) {
                        continue;
                    }
                    boolean v9 = /*EL:144*/true;
                    final RayTraceResult v10 = AutoCrystal.mc.field_71441_e.func_72933_a(/*EL:149*/new Vec3d(AutoCrystal.mc.field_71439_g.field_70165_t, AutoCrystal.mc.field_71439_g.field_70163_u + AutoCrystal.mc.field_71439_g.func_70047_e(), AutoCrystal.mc.field_71439_g.field_70161_v), /*EL:155*/new Vec3d(v6.func_177958_n() + 0.5, v6.func_177956_o() - 0.5, v6.func_177952_p() + 0.5));
                    /*SL:159*/v9 = ((v10 != null && v10.field_72313_a == RayTraceResult.Type.BLOCK) || AutoCrystal.mc.field_71439_g.func_70011_f(/*EL:166*/(double)v6.func_177958_n(), (double)v6.func_177956_o(), (double)v6.func_177952_p()) <= this.throughWallsRange.getValue());
                    final boolean v11 = AutoCrystal.mc.field_71439_g.func_70011_f(/*EL:169*/(double)v6.func_177958_n(), (double)v6.func_177956_o(), (double)v6.func_177952_p()) <= this.placeRange.getValue();
                    /*SL:171*/if (!v11 || !v9) {
                        continue;
                    }
                    /*SL:174*/if (v7 > v2) {
                        /*SL:176*/v2 = v7;
                        /*SL:177*/v3 = v8;
                        /*SL:178*/v4 = v6;
                        /*SL:179*/this.currentEntTarget = (Entity)v5;
                        /*SL:180*/this.currentTarget = v6;
                    }
                    else {
                        /*SL:183*/if (v7 != v2 || v8 >= v3) {
                            continue;
                        }
                        /*SL:185*/v2 = v7;
                        /*SL:186*/v3 = v8;
                        /*SL:187*/v4 = v6;
                        /*SL:188*/this.currentEntTarget = (Entity)v5;
                        /*SL:189*/this.currentTarget = v6;
                    }
                }
            }
        }
        /*SL:197*/if (v2 < this.minDamage.getValue() || v4 == null) {
            /*SL:198*/return;
        }
        final boolean v12 = AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP || AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
        /*SL:205*/if (!v12 && !this.shouldSwitch.getValue()) {
            /*SL:207*/return;
        }
        /*SL:211*/if (v12) {
            final RayTraceResult v13 = AutoCrystal.mc.field_71441_e.func_72933_a(/*EL:232*/new Vec3d(AutoCrystal.mc.field_71439_g.field_70165_t, AutoCrystal.mc.field_71439_g.field_70163_u + AutoCrystal.mc.field_71439_g.func_70047_e(), AutoCrystal.mc.field_71439_g.field_70161_v), /*EL:238*/new Vec3d(v4.func_177958_n() + 0.5, v4.func_177956_o() - 0.5, v4.func_177952_p() + 0.5));
            EnumFacing v14;
            /*SL:243*/if (v13 == null || v13.field_178784_b == null) {
                /*SL:244*/v14 = EnumFacing.UP;
            }
            else {
                /*SL:246*/v14 = v13.field_178784_b;
            }
            EnumHand v15 = EnumHand.MAIN_HAND;
            /*SL:249*/if (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
                /*SL:250*/v15 = EnumHand.OFF_HAND;
            }
            final Vec3d v16 = /*EL:252*/new Vec3d((Vec3i)v4).func_72441_c(0.5, 0.5, 0.5).func_178787_e(/*EL:254*/new Vec3d(v14.func_176730_m()).func_186678_a(0.5));
            AutoCrystal.mc.field_71442_b.func_187099_a(AutoCrystal.mc.field_71439_g, AutoCrystal.mc.field_71441_e, /*EL:257*/v4, v14, v16, v15);
            AutoCrystal.mc.field_71439_g.func_184609_a(/*EL:266*/v15);
            /*SL:267*/return;
        }
        final int v17 = WorldUtils.findItem(Items.field_185158_cP);
        if (v17 >= 0) {
            AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = v17;
        }
    }
    
    private void breakCrystals() {
        final EntityEnderCrystal entityEnderCrystal = (EntityEnderCrystal)AutoCrystal.mc.field_71441_e.field_72996_f.stream().filter(/*EL:280*/a1 -> a1 instanceof EntityEnderCrystal).map(/*EL:281*/a1 -> a1).min(/*EL:283*/Comparator.<? super T, Comparable>comparing(a1 -> AutoCrystal.mc.field_71439_g.func_70032_d(a1))).orElse(null);
        /*SL:288*/if (entityEnderCrystal != null) {
            final double n = AutoCrystal.mc.field_71439_g.func_70032_d(/*EL:290*/(Entity)entityEnderCrystal);
            boolean v0 = /*EL:292*/true;
            final RayTraceResult v = AutoCrystal.mc.field_71441_e.func_72933_a(/*EL:297*/new Vec3d(AutoCrystal.mc.field_71439_g.field_70165_t, AutoCrystal.mc.field_71439_g.field_70163_u + AutoCrystal.mc.field_71439_g.func_70047_e(), AutoCrystal.mc.field_71439_g.field_70161_v), new Vec3d(entityEnderCrystal.field_70165_t + 0.5, entityEnderCrystal.field_70163_u - 0.5, entityEnderCrystal.field_70161_v + 0.5));
            /*SL:307*/if (v != null && v.field_72313_a == RayTraceResult.Type.BLOCK) {
                /*SL:309*/v0 = (n <= this.throughWallsRange.getValue());
            }
            final boolean v2 = /*EL:313*/n <= this.placeRange.getValue();
            /*SL:315*/if (v2 && v0 && /*EL:318*/System.nanoTime() / 1000000L - this.systemTime >= 250L) {
                AutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)AutoCrystal.mc.field_71439_g, /*EL:321*/(Entity)entityEnderCrystal);
                AutoCrystal.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                /*SL:325*/this.systemTime = System.nanoTime() / 1000000L;
            }
        }
    }
    
    public void onRender3d() {
        /*SL:335*/RenderUtils.glStart(255.0f, 255.0f, 255.0f, 1.0f);
        /*SL:337*/if (this.currentTarget != null) {
            final AxisAlignedBB v1 = /*EL:339*/RenderUtils.getBoundingBox(this.currentTarget);
            /*SL:341*/RenderUtils.drawOutlinedBox(v1);
        }
        /*SL:344*/if (this.currentEntTarget != null) {
            /*SL:346*/RenderUtils.drawOutlinedBox(this.currentEntTarget.func_174813_aQ());
        }
        /*SL:349*/RenderUtils.glEnd();
    }
    
    public void onRender2d() {
    }
    
    public void onDisabled() {
        /*SL:362*/this.currentTarget = null;
        /*SL:363*/this.currentEntTarget = null;
    }
    
    private List<BlockPos> findAvailableCrystalBlocks() {
        final double v1 = /*EL:400*/this.placeRange.getValue();
        final NonNullList<BlockPos> v2 = /*EL:401*/(NonNullList<BlockPos>)NonNullList.func_191196_a();
        /*SL:402*/v2.addAll((Collection)getSphere(/*EL:404*/new BlockPos(Math.floor(AutoCrystal.mc.field_71439_g.field_70165_t), Math.floor(AutoCrystal.mc.field_71439_g.field_70163_u), Math.floor(AutoCrystal.mc.field_71439_g.field_70161_v)), (float)v1, /*EL:406*/(int)Math.round(this.placeRange.getValue()), false, true, 0).stream().filter(/*EL:411*/(Predicate<? super Object>)this::canPlaceCrystal).<List<? super Object>, ?>collect(/*EL:413*/(Collector<? super Object, ?, List<? super Object>>)Collectors.<Object>toList()));
        /*SL:415*/return (List<BlockPos>)v2;
    }
    
    public static List<BlockPos> getSphere(final BlockPos a6, final float v1, final int v2, final boolean v3, final boolean v4, final int v5) {
        final List<BlockPos> v6 = /*EL:421*/new ArrayList<BlockPos>();
        final int v7 = /*EL:422*/a6.func_177958_n();
        final int v8 = /*EL:423*/a6.func_177956_o();
        final int v9 = /*EL:424*/a6.func_177952_p();
        /*SL:425*/for (int a7 = v7 - (int)v1; a7 <= v7 + v1; ++a7) {
            /*SL:427*/for (int a8 = v9 - (int)v1; a8 <= v9 + v1; ++a8) {
                /*SL:429*/for (int a9 = v4 ? (v8 - (int)v1) : v8; a9 < (v4 ? (v8 + v1) : (v8 + v2)); ++a9) {
                    final double a10 = /*EL:431*/(v7 - a7) * (v7 - a7) + (v9 - a8) * (v9 - a8) + (v4 ? ((v8 - a9) * (v8 - a9)) : 0);
                    /*SL:432*/if (a10 < v1 * v1 && (!v3 || a10 >= (v1 - 1.0f) * (v1 - 1.0f))) {
                        final BlockPos a11 = /*EL:434*/new BlockPos(a7, a9 + v5, a8);
                        /*SL:435*/v6.add(a11);
                    }
                }
            }
        }
        /*SL:440*/return v6;
    }
    
    private boolean canPlaceCrystal(final BlockPos a1) {
        final BlockPos v1 = /*EL:446*/a1.func_177982_a(0, 1, 0);
        final BlockPos v2 = /*EL:447*/a1.func_177982_a(0, 2, 0);
        /*SL:457*/return (AutoCrystal.mc.field_71441_e.func_180495_p(a1).func_177230_c() == Blocks.field_150357_h || AutoCrystal.mc.field_71441_e.func_180495_p(a1).func_177230_c() == Blocks.field_150343_Z) && AutoCrystal.mc.field_71441_e.func_180495_p(v1).func_177230_c() == Blocks.field_150350_a && AutoCrystal.mc.field_71441_e.func_180495_p(v2).func_177230_c() == Blocks.field_150350_a && AutoCrystal.mc.field_71441_e.func_72872_a(/*EL:459*/(Class)Entity.class, new AxisAlignedBB(v1)).isEmpty();
    }
    
    private static float calculateDamage(final double a1, final double a2, final double a3, final Entity a4) {
        final float v1 = /*EL:473*/12.0f;
        final double v2 = /*EL:474*/a4.func_70011_f(a1, a2, a3) / 12.0;
        final Vec3d v3 = /*EL:475*/new Vec3d(a1, a2, a3);
        final double v4 = /*EL:476*/a4.field_70170_p.func_72842_a(v3, a4.func_174813_aQ());
        final double v5 = /*EL:477*/(1.0 - v2) * v4;
        final float v6 = /*EL:478*/(int)((v5 * v5 + v5) / 2.0 * 7.0 * 12.0 + 1.0);
        double v7 = /*EL:479*/1.0;
        /*SL:480*/if (a4 instanceof EntityLivingBase) {
            /*SL:481*/v7 = getBlastReduction((EntityLivingBase)a4, getDamageMultiplied(v6), new Explosion((World)AutoCrystal.mc.field_71441_e, (Entity)null, a1, a2, a3, 6.0f, false, true));
        }
        /*SL:483*/return (float)v7;
    }
    
    private static float getBlastReduction(final EntityLivingBase a2, float a3, final Explosion v1) {
        /*SL:489*/if (a2 instanceof EntityPlayer) {
            final EntityPlayer a4 = /*EL:490*/(EntityPlayer)a2;
            /*SL:492*/a3 = CombatRules.func_189427_a(a3, (float)a4.func_70658_aO(), (float)a4.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
            /*SL:506*/return a3;
        }
        /*SL:508*/a3 = CombatRules.func_189427_a(a3, (float)a2.func_70658_aO(), (float)a2.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
        /*SL:509*/return a3;
    }
    
    private static float getDamageMultiplied(final float a1) {
        final int v1 = AutoCrystal.mc.field_71441_e.func_175659_aa().func_151525_a();
        /*SL:516*/return a1 * ((v1 == 0) ? 0.0f : ((v1 == 2) ? 1.0f : ((v1 == 1) ? 0.5f : 1.5f)));
    }
    
    public static final class GeometryMasks
    {
        final class Quad
        {
            static final int DOWN = 1;
            static final int UP = 2;
            static final int NORTH = 4;
            static final int SOUTH = 8;
            static final int WEST = 16;
            static final int EAST = 32;
            static final int ALL = 63;
        }
        
        public final class Line
        {
            static final int DOWN_WEST = 17;
            static final int UP_WEST = 18;
            static final int DOWN_EAST = 33;
            static final int UP_EAST = 34;
            static final int DOWN_NORTH = 5;
            static final int UP_NORTH = 6;
            static final int DOWN_SOUTH = 9;
            static final int UP_SOUTH = 10;
            static final int NORTH_WEST = 20;
            static final int NORTH_EAST = 36;
            static final int SOUTH_WEST = 24;
            static final int SOUTH_EAST = 40;
            static final int ALL = 63;
        }
    }
}
