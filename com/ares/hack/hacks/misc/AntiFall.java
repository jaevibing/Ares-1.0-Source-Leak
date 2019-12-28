package com.ares.hack.hacks.misc;

import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Anti Fall", description = "Tries to prevent you falling a certain distance by lagging you back", category = EnumCategory.MISC)
public class AntiFall extends BaseHack
{
    private final Setting<Integer> distance;
    private boolean isFlying;
    
    public AntiFall() {
        this.distance = new IntegerSetting("Max Fall Distance", this, 10, 3, 40);
        this.isFlying = false;
    }
    
    public void onLogic() {
        /*SL:18*/if (this.getEnabled()) {
            /*SL:20*/if (AntiFall.mc.field_71439_g.field_70143_R >= this.distance.getValue()) {
                AntiFall.mc.field_71439_g.field_71075_bZ.field_75100_b = /*EL:22*/true;
                /*SL:23*/this.isFlying = true;
            }
            else/*SL:25*/ if (this.isFlying) {
                AntiFall.mc.field_71439_g.field_71075_bZ.field_75100_b = /*EL:27*/false;
                /*SL:28*/this.isFlying = false;
            }
        }
    }
}
