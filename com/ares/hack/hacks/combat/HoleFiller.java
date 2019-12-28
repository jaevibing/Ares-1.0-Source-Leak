package com.ares.hack.hacks.combat;

import java.util.Iterator;
import com.ares.utils.WorldUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Hole Filler", description = "Fill holes that enemies could jump into", category = EnumCategory.COMBAT)
public class HoleFiller extends BaseHack
{
    private Setting<Double> radius;
    private Setting<Double> range;
    
    public HoleFiller() {
        this.radius = new DoubleSetting("Radius", this, 3.0, 1.0, 5.0);
        this.range = new DoubleSetting("Range", this, 5.0, 1.0, 10.0);
    }
    
    public void onLogic() {
        /*SL:24*/if (this.getEnabled()) {
            /*SL:26*/for (final EntityPlayer entityPlayer : HoleFiller.mc.field_71441_e.field_73010_i) {
                /*SL:28*/if (!entityPlayer.func_110124_au().equals(HoleFiller.mc.field_71439_g.func_110124_au())) {
                    final double doubleValue = /*EL:30*/this.radius.getValue();
                    final BlockPos func_180425_c = /*EL:31*/entityPlayer.func_180425_c();
                    final Iterable<BlockPos> func_177980_a = /*EL:32*/(Iterable<BlockPos>)BlockPos.func_177980_a(func_180425_c.func_177963_a(-doubleValue, -doubleValue, -doubleValue), func_180425_c.func_177963_a(doubleValue, doubleValue, doubleValue));
                    /*SL:34*/for (final BlockPos a1 : func_177980_a) {
                        /*SL:36*/if (HoleFiller.mc.field_71439_g.func_174831_c(a1) > this.range.getValue()) {
                            continue;
                        }
                        /*SL:38*/if (!HoleFiller.mc.field_71441_e.func_180495_p(a1).func_185904_a().func_76222_j() || !HoleFiller.mc.field_71441_e.func_180495_p(a1.func_177982_a(0, 1, 0)).func_185904_a().func_76222_j()) {
                            continue;
                        }
                        final boolean b = HoleFiller.mc.field_71441_e.func_180495_p(/*EL:40*/a1.func_177982_a(0, -1, 0)).func_185904_a().func_76220_a() && HoleFiller.mc.field_71441_e.func_180495_p(/*EL:41*/a1.func_177982_a(1, 0, 0)).func_185904_a().func_76220_a() && HoleFiller.mc.field_71441_e.func_180495_p(/*EL:43*/a1.func_177982_a(0, 0, 1)).func_185904_a().func_76220_a() && HoleFiller.mc.field_71441_e.func_180495_p(/*EL:45*/a1.func_177982_a(-1, 0, 0)).func_185904_a().func_76220_a() && HoleFiller.mc.field_71441_e.func_180495_p(/*EL:47*/a1.func_177982_a(0, 0, -1)).func_185904_a().func_76220_a() && HoleFiller.mc.field_71441_e.func_180495_p(/*EL:49*/a1.func_177982_a(0, 0, 0)).func_185904_a() == Material.field_151579_a && HoleFiller.mc.field_71441_e.func_180495_p(/*EL:51*/a1.func_177982_a(0, 1, 0)).func_185904_a() == Material.field_151579_a && HoleFiller.mc.field_71441_e.func_180495_p(/*EL:53*/a1.func_177982_a(0, 2, 0)).func_185904_a() == Material.field_151579_a;
                        /*SL:59*/if (!b || !HoleFiller.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(a1)).isEmpty()) {
                            continue;
                        }
                        final int v0 = /*EL:61*/WorldUtils.findBlock(Blocks.field_150343_Z);
                        /*SL:63*/if (v0 == -1) {
                            continue;
                        }
                        final int v = HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c;
                        HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:67*/v0;
                        /*SL:69*/WorldUtils.placeBlockMainHand(a1);
                        HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:71*/v;
                    }
                }
            }
        }
    }
}
