package com.ares.hack.hacks.player;

import com.ares.event.client.movement.ShouldWalkOffEdge;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.WorldUtils;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Scaffold", description = "Automatically bridges for you", category = EnumCategory.PLAYER)
public class Scaffold extends BaseHack
{
    private Setting<Integer> radius;
    private Setting<Boolean> down;
    private Setting<Boolean> tower;
    
    public Scaffold() {
        this.radius = new IntegerSetting("Radius", this, 0, 0, 2);
        this.down = new BooleanSetting("Down", this, true);
        this.tower = new BooleanSetting("Tower", this, true);
    }
    
    public void onLogic() {
        /*SL:29*/if (!this.getEnabled()) {
            return;
        }
        final int field_70461_c = Scaffold.mc.field_71439_g.field_71071_by.field_70461_c;
        final int v0 = /*EL:32*/WorldUtils.getBlockInHotbar();
        /*SL:34*/if (v0 == -1) {
            /*SL:38*/ChatUtils.printMessage("No blocks found in hotbar!", "red");
            /*SL:39*/this.setEnabled(false);
            /*SL:40*/return;
        }
        Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = v0;
        /*SL:44*/if (this.radius.getValue() != 0 && this.down.getValue()) {
            /*SL:46*/this.radius.setValue(0);
        }
        /*SL:49*/if (Scaffold.mc.field_71474_y.field_151444_V.func_151470_d() && this.down.getValue()) {
            final float v = /*EL:51*/(float)Math.toRadians(Scaffold.mc.field_71439_g.field_70177_z);
            /*SL:53*/if (Scaffold.mc.field_71474_y.field_74351_w.func_151470_d()) {
                Scaffold.mc.field_71439_g.field_70159_w = /*EL:55*/-MathHelper.func_76126_a(v) * 0.03f;
                Scaffold.mc.field_71439_g.field_70179_y = /*EL:56*/MathHelper.func_76134_b(v) * 0.03f;
            }
            /*SL:58*/if (Scaffold.mc.field_71474_y.field_74368_y.func_151470_d()) {
                Scaffold.mc.field_71439_g.field_70159_w = /*EL:60*/MathHelper.func_76126_a(v) * 0.03f;
                Scaffold.mc.field_71439_g.field_70179_y = /*EL:61*/-MathHelper.func_76134_b(v) * 0.03f;
            }
            /*SL:63*/if (Scaffold.mc.field_71474_y.field_74370_x.func_151470_d()) {
                Scaffold.mc.field_71439_g.field_70159_w = /*EL:65*/MathHelper.func_76134_b(v) * 0.03f;
                Scaffold.mc.field_71439_g.field_70179_y = /*EL:66*/MathHelper.func_76126_a(v) * 0.03f;
            }
            /*SL:68*/if (Scaffold.mc.field_71474_y.field_74366_z.func_151470_d()) {
                Scaffold.mc.field_71439_g.field_70159_w = /*EL:70*/-MathHelper.func_76134_b(v) * 0.03f;
                Scaffold.mc.field_71439_g.field_70179_y = /*EL:71*/-MathHelper.func_76126_a(v) * 0.03f;
            }
            final BlockPos v2 = /*EL:74*/new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - 2.0, Scaffold.mc.field_71439_g.field_70161_v);
            /*SL:77*/if (Scaffold.mc.field_71441_e.func_180495_p(v2).func_185904_a().func_76222_j()) {
                WorldUtils.placeBlockMainHand(v2);
            }
            /*SL:79*/if (Math.abs(Scaffold.mc.field_71439_g.field_70159_w) > 0.03 && Scaffold.mc.field_71441_e.func_180495_p(new BlockPos(v2.func_177958_n() + Scaffold.mc.field_71439_g.field_70159_w / Math.abs(Scaffold.mc.field_71439_g.field_70159_w), (double)(v2.func_177956_o() - 1), (double)v2.func_177952_p())).func_185904_a().func_76222_j()) {
                /*SL:81*/WorldUtils.placeBlockMainHand(new BlockPos(v2.func_177958_n() + Scaffold.mc.field_71439_g.field_70159_w / Math.abs(Scaffold.mc.field_71439_g.field_70159_w), (double)(v2.func_177956_o() - 1), (double)v2.func_177952_p()));
            }
            else/*SL:82*/ if (Math.abs(Scaffold.mc.field_71439_g.field_70179_y) > 0.03 && Scaffold.mc.field_71441_e.func_180495_p(new BlockPos((double)v2.func_177958_n(), (double)(v2.func_177956_o() - 1), v2.func_177952_p() + Scaffold.mc.field_71439_g.field_70179_y / Math.abs(Scaffold.mc.field_71439_g.field_70179_y))).func_185904_a().func_76222_j()) {
                /*SL:84*/WorldUtils.placeBlockMainHand(new BlockPos((double)v2.func_177958_n(), (double)(v2.func_177956_o() - 1), v2.func_177952_p() + Scaffold.mc.field_71439_g.field_70179_y / Math.abs(Scaffold.mc.field_71439_g.field_70179_y)));
            }
            Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:87*/field_70461_c;
            /*SL:89*/return;
        }
        /*SL:93*/if (this.radius.getValue() == 0) {
            final BlockPos v3 = /*EL:95*/new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - 1.0, Scaffold.mc.field_71439_g.field_70161_v);
            /*SL:97*/if (Scaffold.mc.field_71441_e.func_180495_p(v3).func_185904_a().func_76222_j()) {
                WorldUtils.placeBlockMainHand(v3);
            }
            /*SL:99*/if (Math.abs(Scaffold.mc.field_71439_g.field_70159_w) > 0.033 && Scaffold.mc.field_71441_e.func_180495_p(new BlockPos(v3.func_177958_n() + Scaffold.mc.field_71439_g.field_70159_w / Math.abs(Scaffold.mc.field_71439_g.field_70159_w), (double)v3.func_177956_o(), (double)v3.func_177952_p())).func_185904_a().func_76222_j()) {
                /*SL:101*/WorldUtils.placeBlockMainHand(new BlockPos(v3.func_177958_n() + Scaffold.mc.field_71439_g.field_70159_w / Math.abs(Scaffold.mc.field_71439_g.field_70159_w), (double)v3.func_177956_o(), (double)v3.func_177952_p()));
            }
            else/*SL:102*/ if (Math.abs(Scaffold.mc.field_71439_g.field_70179_y) > 0.033 && Scaffold.mc.field_71441_e.func_180495_p(new BlockPos((double)v3.func_177958_n(), (double)v3.func_177956_o(), v3.func_177952_p() + Scaffold.mc.field_71439_g.field_70179_y / Math.abs(Scaffold.mc.field_71439_g.field_70179_y))).func_185904_a().func_76222_j()) {
                /*SL:104*/WorldUtils.placeBlockMainHand(new BlockPos((double)v3.func_177958_n(), (double)v3.func_177956_o(), v3.func_177952_p() + Scaffold.mc.field_71439_g.field_70179_y / Math.abs(Scaffold.mc.field_71439_g.field_70179_y)));
            }
            Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:107*/field_70461_c;
            /*SL:109*/return;
        }
        final ArrayList<BlockPos> v4 = /*EL:114*/new ArrayList<BlockPos>();
        /*SL:115*/for (int v5 = -this.radius.getValue(); v5 <= this.radius.getValue(); ++v5) {
            /*SL:117*/for (int v6 = -this.radius.getValue(); v6 <= this.radius.getValue(); ++v6) {
                /*SL:119*/v4.add(new BlockPos(Scaffold.mc.field_71439_g.field_70165_t + v5, Scaffold.mc.field_71439_g.field_70163_u - 1.0, Scaffold.mc.field_71439_g.field_70161_v + v6));
            }
        }
        /*SL:123*/for (final BlockPos v7 : v4) {
            /*SL:125*/if (Scaffold.mc.field_71441_e.func_180495_p(v7).func_185904_a().func_76222_j()) {
                /*SL:127*/WorldUtils.placeBlockMainHand(v7);
            }
        }
        Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:131*/field_70461_c;
    }
    
    @SubscribeEvent
    public void onEntityUpdate(final LivingEvent.LivingUpdateEvent v2) {
        /*SL:137*/if (!this.getEnabled() || !this.down.getValue()) {
            return;
        }
        /*SL:139*/if (v2.getEntityLiving() instanceof EntityPlayer) {
            final EntityPlayer a1 = /*EL:141*/(EntityPlayer)v2.getEntityLiving();
            /*SL:142*/if (a1.func_70093_af()) {
                /*SL:144*/if (Math.abs(Scaffold.mc.field_71439_g.field_70165_t) - (int)Math.abs(Scaffold.mc.field_71439_g.field_70165_t) < 0.1 || Math.abs(Scaffold.mc.field_71439_g.field_70165_t) - (int)Math.abs(Scaffold.mc.field_71439_g.field_70165_t) > 0.9) {
                    Scaffold.mc.field_71439_g.field_70165_t = /*EL:146*/Math.round(Math.abs(Scaffold.mc.field_71439_g.field_70165_t) - (int)Math.abs(Scaffold.mc.field_71439_g.field_70165_t));
                }
                /*SL:148*/if (Math.abs(Scaffold.mc.field_71439_g.field_70161_v) - (int)Math.abs(Scaffold.mc.field_71439_g.field_70161_v) < 0.1 || Math.abs(Scaffold.mc.field_71439_g.field_70161_v) - (int)Math.abs(Scaffold.mc.field_71439_g.field_70161_v) > 0.9) {
                    Scaffold.mc.field_71439_g.field_70161_v = /*EL:150*/Math.round(Math.abs(Scaffold.mc.field_71439_g.field_70161_v) - (int)Math.abs(Scaffold.mc.field_71439_g.field_70161_v));
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onJump(final LivingEvent.LivingJumpEvent a1) {
        /*SL:159*/if (this.getEnabled() && this.tower.getValue()) {
            final EntityPlayerSP field_71439_g = Scaffold.mc.field_71439_g;
            /*SL:161*/field_71439_g.field_70181_x += 0.1;
        }
    }
    
    @SubscribeEvent
    public void shouldWalkOff(final ShouldWalkOffEdge a1) {
        /*SL:168*/if (this.getEnabled()) {
            /*SL:170*/a1.isSneaking = true;
        }
    }
}
