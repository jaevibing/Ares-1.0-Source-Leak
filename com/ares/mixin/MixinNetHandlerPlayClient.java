package com.ares.mixin;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.world.PlayerDeath;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.Globals;
import net.minecraft.network.play.server.SPacketCombatEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { NetHandlerPlayClient.class }, priority = 999999999)
public class MixinNetHandlerPlayClient
{
    @Shadow
    private boolean field_147309_h;
    @Shadow
    private WorldClient field_147300_g;
    @Shadow
    private Minecraft field_147299_f;
    
    @Inject(method = { "handleChunkData" }, at = { @At("RETURN") })
    private void postHandleChunkData(final SPacketChunkData a1, final CallbackInfo a2) {
    }
    
    @Inject(method = { "handleCombatEvent" }, at = { @At("INVOKE") })
    private void onPlayerDeath(final SPacketCombatEvent v2, final CallbackInfo v3) {
        /*SL:42*/if (v2.field_179776_a == SPacketCombatEvent.Event.ENTITY_DIED) {
            System.out.println(/*EL:44*/"A player died! " + v2.field_179775_c);
            Entity a2 = Globals.mc.field_71441_e.func_73045_a(/*EL:45*/v2.field_179775_c);
            /*SL:46*/if (a2 instanceof EntityPlayer) {
                /*SL:48*/a2 = new PlayerDeath((EntityPlayer)a2);
                MinecraftForge.EVENT_BUS.post(/*EL:49*/(Event)a2);
            }
        }
    }
}
