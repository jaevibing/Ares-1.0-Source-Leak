package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderChunk.class })
public class MixinRenderChunk
{
    @Inject(method = { "setPosition" }, at = { @At("HEAD") })
    public void setPosition(final int a1, final int a2, final int a3, final CallbackInfo a4) {
    }
}
