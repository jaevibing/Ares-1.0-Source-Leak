package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.movement.IsKeyPressed;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ KeyBinding.class })
public class MixinKeyBinding
{
    @Shadow
    private boolean field_74513_e;
    
    @Inject(method = { "isKeyDown" }, at = { @At("RETURN") }, cancellable = true)
    private void isKeyDown(final CallbackInfoReturnable<Boolean> a1) {
        final IsKeyPressed v1 = /*EL:21*/new IsKeyPressed(a1.getReturnValue(), this.field_74513_e);
        MinecraftForge.EVENT_BUS.post(/*EL:22*/(Event)v1);
        /*SL:23*/a1.setReturnValue(v1.initialValue);
    }
}
