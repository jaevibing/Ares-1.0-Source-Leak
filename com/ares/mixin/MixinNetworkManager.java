package com.ares.mixin;

import io.netty.handler.timeout.TimeoutException;
import com.ares.event.packet.PacketRecieved;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.packet.PacketSent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { NetworkManager.class }, priority = 999999999)
public class MixinNetworkManager
{
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onSendPacket(final Packet<?> a1, final CallbackInfo a2) {
        final PacketSent v1 = /*EL:21*/new PacketSent(a1);
        MinecraftForge.EVENT_BUS.post(/*EL:22*/(Event)v1);
        /*SL:23*/if (v1.isCanceled() && a2.isCancellable()) {
            /*SL:25*/a2.cancel();
        }
    }
    
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void onChannelRead(final ChannelHandlerContext a1, final Packet<?> a2, final CallbackInfo a3) {
        final PacketRecieved v1 = /*EL:32*/new PacketRecieved(a2);
        MinecraftForge.EVENT_BUS.post(/*EL:33*/(Event)v1);
        /*SL:34*/if (v1.isCanceled() && a3.isCancellable()) {
            /*SL:36*/a3.cancel();
        }
    }
    
    @Inject(method = { "exceptionCaught" }, at = { @At("HEAD") }, cancellable = true)
    private void exceptionCaught(final ChannelHandlerContext a1, final Throwable a2, final CallbackInfo a3) {
        /*SL:43*/if (!(a2 instanceof TimeoutException)) {
            /*SL:45*/a2.printStackTrace();
            /*SL:46*/a3.cancel();
        }
    }
}
