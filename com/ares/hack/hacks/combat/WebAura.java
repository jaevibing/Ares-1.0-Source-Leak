package com.ares.hack.hacks.combat;

import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.ares.utils.WorldUtils;
import net.minecraft.init.Blocks;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Web Aura", description = "Trap people camping in holes", category = EnumCategory.COMBAT)
public class WebAura extends BaseHack
{
    private Setting<Double> range;
    
    public WebAura() {
        this.range = new DoubleSetting("Range", this, 4.0, 1.0, 10.0);
    }
    
    public void onLogic() {
        /*SL:23*/if (!this.getEnabled()) {
            return;
        }
        final int block = /*EL:25*/WorldUtils.findBlock(Blocks.field_150321_G);
        /*SL:26*/if (block == -1) {
            return;
        }
        final List<EntityPlayer> list = (List<EntityPlayer>)WebAura.mc.field_71441_e.field_73010_i.stream().filter(/*EL:30*/a1 -> WebAura.mc.field_71439_g.func_70032_d(a1) <= this.range.getValue() && !WebAura.mc.field_71439_g.equals((Object)a1)).collect(/*EL:38*/Collectors.<Object>toList());
        /*SL:40*/if (list.size() > 0) {
            WebAura.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:42*/block;
        }
        /*SL:45*/for (final EntityPlayer v0 : list) {
            final BlockPos v = /*EL:47*/new BlockPos((int)v0.field_70165_t, (int)v0.field_70163_u, (int)v0.field_70161_v);
            /*SL:50*/if (WebAura.mc.field_71441_e.func_180495_p(v).func_185904_a().func_76222_j()) {
                /*SL:51*/WorldUtils.placeBlockMainHand(v);
            }
            /*SL:52*/if (WebAura.mc.field_71441_e.func_180495_p(v.func_177982_a(1, 0, 0)).func_185904_a().func_76222_j()) {
                /*SL:53*/WorldUtils.placeBlockMainHand(v.func_177982_a(1, 0, 0));
            }
            /*SL:54*/if (WebAura.mc.field_71441_e.func_180495_p(v.func_177982_a(0, 0, 1)).func_185904_a().func_76222_j()) {
                /*SL:55*/WorldUtils.placeBlockMainHand(v.func_177982_a(0, 0, 1));
            }
            /*SL:56*/if (WebAura.mc.field_71441_e.func_180495_p(v.func_177982_a(0, 0, -1)).func_185904_a().func_76222_j()) {
                /*SL:57*/WorldUtils.placeBlockMainHand(v.func_177982_a(0, 0, -1));
            }
            /*SL:58*/if (WebAura.mc.field_71441_e.func_180495_p(v.func_177982_a(-1, 0, 0)).func_185904_a().func_76222_j()) {
                /*SL:59*/WorldUtils.placeBlockMainHand(v.func_177982_a(-1, 0, 0));
            }
        }
    }
}
