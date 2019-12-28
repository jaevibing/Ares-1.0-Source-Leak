package com.ares.hack.hacks.combat;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.Entity;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Boat Aura", description = "Attack nearby boats", category = EnumCategory.COMBAT)
public class BoatAura extends BaseHack
{
    private final Setting<Double> range;
    private final Setting<Boolean> boats;
    private final Setting<Boolean> minecarts;
    
    public BoatAura() {
        this.range = new DoubleSetting("Range", this, 5.0, 0.0, 10.0);
        this.boats = new BooleanSetting("Boats", this, true);
        this.minecarts = new BooleanSetting("Minecarts", this, true);
    }
    
    public void onLogic() {
        /*SL:23*/if (this.getEnabled()) {
            /*SL:25*/for (final Entity v1 : BoatAura.mc.field_71441_e.field_72996_f) {
                /*SL:30*/if (!v1.func_110124_au().equals(BoatAura.mc.field_71439_g.func_110124_au()) && ((v1 instanceof EntityBoat && this.boats.getValue()) || /*EL:32*/(v1 instanceof EntityMinecart && this.minecarts.getValue())) && BoatAura.mc.field_71439_g.func_70032_d(/*EL:40*/v1) <= this.range.getValue()) {
                    BoatAura.mc.field_71442_b.func_78764_a((EntityPlayer)BoatAura.mc.field_71439_g, /*EL:42*/v1);
                }
            }
        }
    }
}
