package com.ares.hack.hacks.player;

import com.ares.event.entity.GetMaxInPortalTime;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.entity.GetPortalCooldown;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Instant Portal", description = "ez pz", category = EnumCategory.PLAYER)
public class InstantPortalReuse extends BaseHack
{
    private Setting<Integer> cooldown;
    private Setting<Integer> waitTime;
    
    public InstantPortalReuse() {
        this.cooldown = new IntegerSetting("Cooldown", this, 2, 0, 10);
        this.waitTime = new IntegerSetting("Wait Time", this, 2, 0, 80);
    }
    
    @SubscribeEvent
    public void getPortalCooldown(final GetPortalCooldown a1) {
        /*SL:20*/if (this.getEnabled() && (a1.player == null || a1.player.func_110124_au().equals(InstantPortalReuse.mc.field_71439_g.func_110124_au()))) {
            /*SL:22*/a1.cooldown = this.cooldown.getValue();
        }
    }
    
    @SubscribeEvent
    public void getMaxInPortalTime(final GetMaxInPortalTime a1) {
        /*SL:29*/if (this.getEnabled() && (a1.entity == null || a1.entity.func_110124_au().equals(InstantPortalReuse.mc.field_71439_g.func_110124_au()))) {
            /*SL:31*/a1.maxInPortalTime = this.waitTime.getValue();
        }
    }
}
