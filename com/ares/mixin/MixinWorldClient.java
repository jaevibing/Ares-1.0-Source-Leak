package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.world.MakeFireworks;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ WorldClient.class })
public class MixinWorldClient
{
    @Redirect(method = { "makeFireworks" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addEffect(Lnet/minecraft/client/particle/Particle;)V"))
    private void makeFireworkParticles(final ParticleManager a1, final Particle a2) {
        final MakeFireworks v1 = /*EL:18*/new MakeFireworks();
        MinecraftForge.EVENT_BUS.post(/*EL:19*/(Event)v1);
        /*SL:21*/if (!v1.isCanceled()) {
            /*SL:22*/a1.func_78873_a(a2);
        }
    }
}
