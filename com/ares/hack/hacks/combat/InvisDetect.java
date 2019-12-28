package com.ares.hack.hacks.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.Vec3d;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.Utils;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "InvisDetect", description = "Can help locate people in entity god mode", category = EnumCategory.COMBAT)
public class InvisDetect extends BaseHack
{
    @SubscribeEvent
    public void onPlaySound(final PlaySoundAtEntityEvent v2) {
        /*SL:35*/if (v2.getEntity() == null) {
            return;
        }
        /*SL:41*/if (v2.getSound().equals(SoundEvents.field_187709_dP) || v2.getSound().equals(SoundEvents.field_187729_cv) || v2.getSound().equals(SoundEvents.field_187732_cw) || v2.getSound().equals(SoundEvents.field_191256_dG)) {
            final Vec3d a1 = /*EL:46*/v2.getEntity().func_174791_d();
            /*SL:47*/ChatUtils.printMessage("Invis PlayerPreviewElement at: " + Utils.vectorToString(a1, new boolean[0]));
        }
    }
}
