package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.entity.DoPigAI;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import net.minecraft.entity.passive.EntityPig;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPig.class })
public class MixinEntityPig
{
    @ModifyArgs(method = { "travel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/EntityAnimal;travel(FFF)V"))
    private void travel(final Args a1, final float a2, final float a3, final float a4) {
        final DoPigAI v1 = /*EL:18*/new DoPigAI();
        MinecraftForge.EVENT_BUS.post(/*EL:19*/(Event)v1);
        /*SL:21*/if (v1.getResult() == Event.Result.ALLOW || v1.getResult() == Event.Result.DEFAULT) {
            /*SL:23*/a1.setAll(a2, a3, a4);
        }
        else {
            /*SL:27*/a1.setAll(a2, a3, 0);
        }
    }
}
