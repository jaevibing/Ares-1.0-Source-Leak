package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.world.ChunkRender;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.ChunkRenderContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ChunkRenderContainer.class })
public class MixinChunkRenderContainer
{
    @Inject(method = { "preRenderChunk" }, at = { @At("HEAD") })
    public void preRenderChunk(final RenderChunk a1, final CallbackInfo a2) {
        final ChunkRender.Pre v1 = /*EL:18*/new ChunkRender.Pre(a1);
        MinecraftForge.EVENT_BUS.post(/*EL:19*/(Event)v1);
    }
}
