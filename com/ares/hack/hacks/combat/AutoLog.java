package com.ares.hack.hacks.combat;

import com.ares.utils.player.PlayerUtils;
import com.ares.utils.player.InventoryUtils;
import net.minecraft.init.Items;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto Log", description = "Automaticall Disconnect", category = EnumCategory.COMBAT)
public class AutoLog extends BaseHack
{
    private final Setting<Integer> minTotems;
    
    public AutoLog() {
        this.minTotems = new IntegerSetting("Min Totems", this, 0, 0, 5);
    }
    
    public void onLogic() {
        /*SL:19*/if (this.getEnabled()) {
            final int v1 = /*EL:21*/InventoryUtils.findItemInInventoryQuantity(Items.field_190929_cY, true);
            /*SL:23*/if (v1 <= this.minTotems.getValue()) {
                /*SL:25*/PlayerUtils.disconnectFromWorld();
            }
        }
    }
}
