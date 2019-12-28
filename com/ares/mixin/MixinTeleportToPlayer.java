package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.entity.GetPlayerType;
import net.minecraft.world.GameType;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ TeleportToPlayer.class })
public class MixinTeleportToPlayer
{
    @Redirect(method = { "<init>(Ljava/util/Collection;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetworkPlayerInfo;getGameType()Lnet/minecraft/world/GameType;"))
    public GameType getPlayerTypeWrapper(final NetworkPlayerInfo a1) {
        final GetPlayerType v1 = /*EL:21*/new GetPlayerType(a1, a1.func_178848_b());
        MinecraftForge.EVENT_BUS.post(/*EL:22*/(Event)v1);
        /*SL:24*/return v1.gameType;
    }
}
