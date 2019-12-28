package com.ares.hack.hacks.player;

import com.ares.utils.chat.ChatUtils;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "ConstantQMain", description = "Does \"/queue main\" once a minute to help you get through the 2b2t queue", category = EnumCategory.PLAYER)
public class ConstantQMain extends BaseHack
{
    private static long lastSentCmd;
    private Setting<Boolean> onlyEnd;
    
    public ConstantQMain() {
        this.onlyEnd = new BooleanSetting("Only in end", this, true);
    }
    
    public void onLogic() {
        /*SL:35*/if (System.currentTimeMillis() >= ConstantQMain.lastSentCmd + 30000L && this.getEnabled()) {
            /*SL:37*/if (ConstantQMain.mc.field_71439_g == null) {
                return;
            }
            /*SL:39*/if (!this.onlyEnd.getValue() || (ConstantQMain.mc.field_71439_g.field_71093_bK != -1 && ConstantQMain.mc.field_71439_g.field_71093_bK != 0)) {
                ConstantQMain.lastSentCmd = /*EL:41*/System.currentTimeMillis();
                ConstantQMain.mc.field_71439_g.func_71165_d(/*EL:42*/"/queue main");
                /*SL:43*/ChatUtils.printMessage("/queue main");
            }
        }
    }
    
    public void onDisabled() {
        ConstantQMain.lastSentCmd = /*EL:49*/0L;
    }
    
    static {
        ConstantQMain.lastSentCmd = 0L;
    }
}
