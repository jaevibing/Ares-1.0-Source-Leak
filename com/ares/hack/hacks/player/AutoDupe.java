package com.ares.hack.hacks.player;

import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto Dupe", description = "", category = EnumCategory.PLAYER)
public class AutoDupe extends BaseHack
{
    private boolean isVanished;
    
    public AutoDupe() {
        this.isVanished = false;
    }
    
    public void onLogic() {
        /*SL:14*/if (!this.getEnabled()) {
            return;
        }
        /*SL:16*/if (!this.isVanished) {
            AutoDupe.mc.field_71439_g.func_71165_d(/*EL:18*/".vanish dismount");
            /*SL:19*/this.isVanished = true;
        }
    }
}
