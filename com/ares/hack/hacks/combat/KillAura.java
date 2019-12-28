package com.ares.hack.hacks.combat;

import java.util.Iterator;
import net.minecraft.util.EnumHand;
import com.ares.utils.data.FriendUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Kill Aura", description = "Attack players near you", category = EnumCategory.COMBAT)
public class KillAura extends BaseHack
{
    private Setting<Double> range;
    private Setting<Boolean> superOnly;
    private Setting<Boolean> playersOnly;
    private Setting<Integer> delay;
    private int hasWaited;
    
    public KillAura() {
        this.range = new DoubleSetting("Range", this, 5.0, 1.0, 15.0);
        this.superOnly = new BooleanSetting("32k Only", this, false);
        this.playersOnly = new BooleanSetting("Players only", this, true);
        this.delay = new IntegerSetting("Delay in ticks", this, 0, 0, 50);
        this.hasWaited = 0;
    }
    
    public void onLogic() {
        /*SL:46*/if (!this.getEnabled() || KillAura.mc.field_71439_g.field_70128_L || KillAura.mc.field_71441_e == null) {
            return;
        }
        /*SL:48*/if (this.hasWaited < this.delay.getValue()) {
            /*SL:50*/++this.hasWaited;
            /*SL:51*/return;
        }
        /*SL:54*/this.hasWaited = 0;
        /*SL:56*/for (final Entity v1 : KillAura.mc.field_71441_e.field_72996_f) {
            /*SL:59*/if (v1 instanceof EntityLivingBase) {
                if (v1 == KillAura.mc.field_71439_g) {
                    continue;
                }
                /*SL:68*/if (KillAura.mc.field_71439_g.func_70032_d(v1) > this.range.getValue() || ((EntityLivingBase)v1).func_110143_aJ() <= 0.0f || (!(v1 instanceof EntityPlayer) && this.playersOnly.getValue()) || (v1 instanceof EntityPlayer && FriendUtils.isFriend((EntityPlayer)v1)) || /*EL:70*/(!Auto32k.isSuperWeapon(KillAura.mc.field_71439_g.func_184614_ca()) && this.superOnly.getValue())) {
                    continue;
                }
                KillAura.mc.field_71442_b.func_78764_a((EntityPlayer)KillAura.mc.field_71439_g, /*EL:72*/v1);
                KillAura.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            }
        }
    }
}
