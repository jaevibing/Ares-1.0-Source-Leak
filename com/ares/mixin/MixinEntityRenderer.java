package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.entity.Entity;
import com.ares.event.client.movement.TurnPlayerFromMouseInput;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.world.RenderWorldEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { EntityRenderer.class }, priority = 999999999)
public class MixinEntityRenderer
{
    @Inject(method = { "renderWorld(FJ)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onRenderWorld(final CallbackInfo a1) {
        final RenderWorldEvent v1 = /*EL:20*/new RenderWorldEvent();
        MinecraftForge.EVENT_BUS.post(/*EL:21*/(Event)v1);
        /*SL:22*/if (v1.isCanceled()) {
            /*SL:23*/a1.cancel();
        }
    }
    
    @Redirect(method = { "updateCameraAndRender" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;turn(FF)V"))
    protected void onTurnPlayerWrapper(final EntityPlayerSP a1, final float a2, final float a3) {
        final TurnPlayerFromMouseInput v1 = /*EL:29*/new TurnPlayerFromMouseInput((Entity)a1, a2, a3);
        MinecraftForge.EVENT_BUS.post(/*EL:30*/(Event)v1);
        /*SL:31*/if (!v1.isCanceled()) {
            /*SL:33*/v1.entity.func_70082_c(v1.yaw, v1.pitch);
        }
    }
}
