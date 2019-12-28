package com.ares.hack.hacks.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import com.ares.utils.WorldUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Surround", description = "Surrounds your feet with obsidian", category = EnumCategory.COMBAT)
public class Surround extends BaseHack
{
    private BlockPos lastPos;
    
    public Surround() {
        this.lastPos = new BlockPos(0, -100, 0);
    }
    
    public void onLogic() {
        /*SL:36*/if (!this.getEnabled() || !Surround.mc.field_71439_g.field_70122_E) {
            return;
        }
        final int field_70461_c = Surround.mc.field_71439_g.field_71071_by.field_70461_c;
        final int block = /*EL:39*/WorldUtils.findBlock(Blocks.field_150343_Z);
        /*SL:41*/if (block != -1) {
            final BlockPos blockPos = /*EL:43*/new BlockPos(Surround.mc.field_71439_g.func_174791_d());
            /*SL:45*/if (blockPos.equals((Object)this.lastPos)) {
                final BlockPos[] array2;
                final BlockPos[] array = /*EL:58*/array2 = new BlockPos[] { blockPos.func_177982_a(0, -1, 1), blockPos.func_177982_a(1, -1, 0), blockPos.func_177982_a(0, -1, -1), blockPos.func_177982_a(-1, -1, 0), blockPos.func_177982_a(0, 0, 1), blockPos.func_177982_a(1, 0, 0), blockPos.func_177982_a(0, 0, -1), blockPos.func_177982_a(-1, 0, 0) };
                for (final BlockPos v1 : array2) {
                    /*SL:60*/if (Surround.mc.field_71441_e.func_180495_p(v1).func_185904_a().func_76222_j() && Surround.mc.field_71441_e.func_72839_b(/*EL:62*/(Entity)null, new AxisAlignedBB(v1)).isEmpty()) {
                        Surround.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:70*/block;
                        /*SL:71*/WorldUtils.placeBlockMainHand(v1);
                    }
                }
                Surround.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:76*/field_70461_c;
            }
            else {
                /*SL:80*/this.setEnabled(false);
            }
        }
    }
    
    public void onEnabled() {
        /*SL:88*/this.lastPos = new BlockPos(Surround.mc.field_71439_g.func_174791_d());
    }
}
