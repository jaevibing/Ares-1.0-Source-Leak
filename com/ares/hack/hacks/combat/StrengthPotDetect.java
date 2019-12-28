package com.ares.hack.hacks.combat;

import java.util.Iterator;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.init.MobEffects;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "StrengthPotAlert", description = "Detect when enemies strength pot", category = EnumCategory.COMBAT)
public class StrengthPotDetect extends BaseHack
{
    private Set<EntityPlayer> strengthedPlayers;
    
    public StrengthPotDetect() {
        this.strengthedPlayers = Collections.<EntityPlayer>newSetFromMap(new WeakHashMap<EntityPlayer, Boolean>());
    }
    
    public void onLogic() {
        /*SL:19*/if (!this.getEnabled()) {
            return;
        }
        /*SL:21*/for (final EntityPlayer v1 : StrengthPotDetect.mc.field_71441_e.field_73010_i) {
            /*SL:23*/if (v1.equals((Object)StrengthPotDetect.mc.field_71439_g)) {
                continue;
            }
            /*SL:25*/if (v1.func_70644_a(MobEffects.field_76420_g) && !this.strengthedPlayers.contains(v1)) {
                /*SL:27*/ChatUtils.printMessage("PlayerPreviewElement '" + v1.getDisplayNameString() + "' has strength potted", "yellow");
                /*SL:28*/this.strengthedPlayers.add(v1);
            }
            /*SL:30*/if (!this.strengthedPlayers.contains(v1) || v1.func_70644_a(MobEffects.field_76420_g)) {
                continue;
            }
            /*SL:32*/ChatUtils.printMessage("PlayerPreviewElement '" + v1.getDisplayNameString() + "' no longer has strength", "green");
            /*SL:33*/this.strengthedPlayers.remove(v1);
        }
    }
}
