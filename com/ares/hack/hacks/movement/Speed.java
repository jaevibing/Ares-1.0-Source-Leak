package com.ares.hack.hacks.movement;

import net.minecraft.util.MovementInput;
import com.ares.Globals;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import com.ares.utils.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.world.PlayerMove;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Speed", description = "Speeeeeeeeeeeeeeed", category = EnumCategory.MOVEMENT)
public class Speed extends BaseHack
{
    private Setting<Boolean> forwardS;
    
    public Speed() {
        this.forwardS = new BooleanSetting("Only Forward", this, false);
    }
    
    @SubscribeEvent
    public void motion(final PlayerMove.Post a1) {
        /*SL:40*/if (!this.getEnabled()) {
            return;
        }
        /*SL:42*/this.run(0.405, 0.2f, 1.0064);
    }
    
    private void run(final double v2, final float v4, final double v5) {
        final boolean v6 = /*EL:47*/(!this.forwardS.getValue() && Speed.mc.field_71439_g.field_191988_bg != 0.0f) || Speed.mc.field_71439_g.field_191988_bg > 0.0f;
        /*SL:50*/if (v6 || Speed.mc.field_71439_g.field_70702_br != 0.0f) {
            Speed.mc.field_71439_g.func_70031_b(/*EL:52*/true);
            /*SL:53*/if (Speed.mc.field_71439_g.field_70122_E) {
                Speed.mc.field_71439_g.field_70181_x = /*EL:55*/v2;
                final float a1 = /*EL:56*/Utils.getPlayerDirection();
                final EntityPlayerSP field_71439_g = Speed.mc.field_71439_g;
                /*SL:57*/field_71439_g.field_70159_w -= MathHelper.func_76126_a(a1) * v4;
                final EntityPlayerSP field_71439_g2 = Speed.mc.field_71439_g;
                /*SL:58*/field_71439_g2.field_70179_y += MathHelper.func_76134_b(a1) * v4;
            }
            else {
                final double a2 = /*EL:62*/Math.sqrt(Speed.mc.field_71439_g.field_70159_w * Speed.mc.field_71439_g.field_70159_w + Speed.mc.field_71439_g.field_70179_y * Speed.mc.field_71439_g.field_70179_y);
                final double a3 = /*EL:63*/Utils.getPlayerDirection();
                Speed.mc.field_71439_g.field_70159_w = /*EL:65*/-Math.sin(a3) * v5 * a2;
                Speed.mc.field_71439_g.field_70179_y = /*EL:66*/Math.cos(a3) * v5 * a2;
            }
        }
    }
    
    public void onJump(final double a1, final double a2, final EntityPlayerSP a3) {
        final MovementInput v1 = Globals.mc.field_71439_g.field_71158_b;
        float v2 = /*EL:74*/v1.field_192832_b;
        float v3 = /*EL:75*/v1.field_78902_a;
        float v4 = Globals.mc.field_71439_g.field_70177_z;
        /*SL:77*/if (v2 != 0.0) {
            /*SL:79*/if (v3 > 0.0) {
                /*SL:81*/v4 += ((v2 > 0.0) ? -45 : 45);
            }
            else/*SL:83*/ if (v3 < 0.0) {
                /*SL:85*/v4 += ((v2 > 0.0) ? 45 : -45);
            }
            /*SL:87*/v3 = 0.0f;
            /*SL:88*/if (v2 > 0.0) {
                /*SL:90*/v2 = 1.0f;
            }
            else/*SL:92*/ if (v2 < 0.0) {
                /*SL:94*/v2 = -1.0f;
            }
        }
        /*SL:97*/if (v3 > 0.0) {
            /*SL:99*/v3 = 1.0f;
        }
        else/*SL:101*/ if (v3 < 0.0) {
            /*SL:103*/v3 = -1.0f;
        }
        /*SL:105*/a3.field_70159_w = a1 + (v2 * 0.2 * /*EL:113*/Math.cos(Math.toRadians(v4 + 90.0f)) + v3 * 0.2 * /*EL:119*/Math.sin(Math.toRadians(v4 + 90.0f)));
        /*SL:121*/a3.field_70179_y = a2 + (v2 * 0.2 * /*EL:129*/Math.sin(Math.toRadians(v4 + 90.0f)) - v3 * 0.2 * /*EL:135*/Math.cos(Math.toRadians(v4 + 90.0f)));
    }
}
