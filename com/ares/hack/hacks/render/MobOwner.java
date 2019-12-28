package com.ares.hack.hacks.render;

import java.util.Iterator;
import com.ares.api.MojangWebApi;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.Entity;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Mob Owner", description = "Show you owners of mobs", category = EnumCategory.RENDER)
public class MobOwner extends BaseHack
{
    public void onLogic() {
        /*SL:33*/if (!this.getEnabled()) {
            return;
        }
        /*SL:35*/for (final Entity v0 : MobOwner.mc.field_71441_e.field_72996_f) {
            /*SL:37*/if (v0 instanceof EntityTameable) {
                final EntityTameable v = /*EL:39*/(EntityTameable)v0;
                /*SL:40*/if (v.func_70909_n() && v.func_70902_q() != null) {
                    /*SL:42*/v.func_174805_g(true);
                    /*SL:43*/v.func_96094_a("Owner: " + v.func_70902_q().func_145748_c_().func_150254_d());
                }
            }
            /*SL:46*/if (v0 instanceof AbstractHorse) {
                final AbstractHorse v2 = /*EL:48*/(AbstractHorse)v0;
                /*SL:49*/if (!v2.func_110248_bS() || v2.func_184780_dh() == null) {
                    continue;
                }
                /*SL:51*/v2.func_174805_g(true);
                /*SL:53*/v2.func_96094_a("Owner: " + MojangWebApi.grabRealName(v2.func_184780_dh().toString()));
            }
        }
    }
    
    public void onDisabled() {
        /*SL:63*/for (final Entity v1 : MobOwner.mc.field_71441_e.field_72996_f) {
            /*SL:65*/if (!(v1 instanceof EntityTameable)) {
                if (!(v1 instanceof AbstractHorse)) {
                    continue;
                }
            }
            try {
                /*SL:69*/v1.func_174805_g(false);
            }
            catch (Exception ex) {}
        }
    }
}
