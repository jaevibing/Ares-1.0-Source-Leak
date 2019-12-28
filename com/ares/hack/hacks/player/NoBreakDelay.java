package com.ares.hack.hacks.player;

import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "No Break Delay", description = "like fast place but for breaking", category = EnumCategory.PLAYER)
public class NoBreakDelay extends BaseHack
{
    public void onLogic() {
        /*SL:15*/if (!this.getEnabled()) {
            return;
        }
        try {
            /*SL:19*/ObfuscationReflectionHelper.setPrivateValue((Class)PlayerControllerMP.class, (Object)NoBreakDelay.mc.field_71442_b, /*EL:22*/(Object)0, new String[] { "blockHitDelay", "field_78781_i" });
        }
        catch (Exception v1) {
            /*SL:29*/this.setEnabled(false);
            /*SL:30*/ChatUtils.printMessage("Disabled fastplace due to error: " + v1.getMessage());
            /*SL:31*/v1.printStackTrace();
        }
    }
}
