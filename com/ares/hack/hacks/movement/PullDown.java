package com.ares.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import com.ares.hack.settings.settings.number.FloatSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Pull Down", description = "Fast fall", category = EnumCategory.MOVEMENT)
public class PullDown extends BaseHack
{
    private boolean isJumping;
    private Setting<Float> speed;
    
    public PullDown() {
        this.isJumping = false;
        this.speed = new FloatSetting("Speed", this, 10.0f, 0.0f, 20.0f);
    }
    
    public void onLogic() {
        /*SL:20*/if (this.isJumping && PullDown.mc.field_71439_g.field_70122_E) {
            this.isJumping = false;
        }
        /*SL:22*/if (!this.getEnabled() || PullDown.mc.field_71439_g.func_184613_cA() || PullDown.mc.field_71439_g.field_71075_bZ.field_75100_b) {
            return;
        }
        final boolean v1 = !PullDown.mc.field_71441_e.func_175623_d(PullDown.mc.field_71439_g.func_180425_c().func_177982_a(/*EL:25*/0, -1, 0)) || !PullDown.mc.field_71441_e.func_175623_d(PullDown.mc.field_71439_g.func_180425_c().func_177982_a(/*EL:27*/0, -2, 0));
        /*SL:30*/if (!PullDown.mc.field_71439_g.field_70122_E && !v1) {
            PullDown.mc.field_71439_g.field_70181_x = /*EL:32*/-this.speed.getValue();
        }
    }
    
    @SubscribeEvent
    public void onJump(final LivingEvent.LivingJumpEvent a1) {
        /*SL:39*/if (a1.getEntityLiving().equals((Object)PullDown.mc.field_71439_g)) {
            /*SL:41*/this.isJumping = true;
        }
    }
}
