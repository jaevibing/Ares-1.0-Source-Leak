package com.ares.hack.hacks.combat;

import com.ares.event.world.PlayerDeath;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import com.ares.hack.settings.settings.EnumSetting;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "AutoEz", description = "Automatically ez", category = EnumCategory.COMBAT)
public class AutoEz extends BaseHack
{
    private Setting<Messages> message;
    private int hasBeenCombat;
    private EntityPlayer target;
    
    public AutoEz() {
        this.message = new EnumSetting<Messages>("Text", this, Messages.ARES);
    }
    
    @SubscribeEvent
    public void onAttack(final AttackEntityEvent v2) {
        /*SL:64*/if (this.getEnabled() && v2.getTarget() instanceof EntityPlayer) {
            final EntityPlayer a1 = /*EL:66*/(EntityPlayer)v2.getTarget();
            /*SL:67*/if (v2.getEntityPlayer().func_110124_au().equals(AutoEz.mc.field_71439_g.func_110124_au())) {
                /*SL:69*/if (a1.func_110143_aJ() <= 0.0f || a1.field_70128_L || !AutoEz.mc.field_71441_e.field_73010_i.contains(a1)) {
                    AutoEz.mc.field_71439_g.func_71165_d(/*EL:71*/this.message.toString());
                    /*SL:72*/return;
                }
                /*SL:74*/this.hasBeenCombat = 500;
                /*SL:75*/this.target = a1;
            }
        }
    }
    
    public void onLogic() {
        /*SL:83*/if (AutoEz.mc.field_71439_g.field_70128_L) {
            this.hasBeenCombat = 0;
        }
        /*SL:85*/if (this.hasBeenCombat > 0 && (this.target.func_110143_aJ() <= 0.0f || this.target.field_70128_L || !AutoEz.mc.field_71441_e.field_73010_i.contains(this.target))) {
            /*SL:87*/if (this.getEnabled()) {
                AutoEz.mc.field_71439_g.func_71165_d(/*EL:89*/this.message.toString());
            }
            /*SL:91*/this.hasBeenCombat = 0;
        }
        /*SL:93*/--this.hasBeenCombat;
    }
    
    @SubscribeEvent
    public void onPlayerDeath(final PlayerDeath a1) {
        /*SL:99*/if (!this.getEnabled()) {
            return;
        }
        /*SL:101*/if (!a1.getPlayer().equals((Object)AutoEz.mc.field_71439_g) && a1.getPlayer().equals((Object)this.target)) {
            AutoEz.mc.field_71439_g.func_71165_d(/*EL:103*/this.message.toString());
        }
    }
    
    enum Messages
    {
        SHITSTAIN("Nice fight, shit_stain.pl owns me and all!"), 
        SHITTIER("Nice fight, shit_tier.pl owns me and all!"), 
        DOTSHIT("Nice fight, DotShit.cc owns me and all!"), 
        EZ("Ez"), 
        COOKIEDRAGON(/*EL:105*/"Nice fight, cookiedragon234 owns me and all!"), 
        TIGERMOUTHBEAR("Nice fight, tigermouthbear owns me and all!"), 
        ARES("Nice fight, Ares Client owns me and all!");
        
        private String name;
        
        private Messages(final String a1) {
            this.name = a1;
        }
        
        @Override
        public String toString() {
            /*SL:52*/return this.name;
        }
    }
}
