package com.ares.mixin;

import com.ares.event.entity.IsCurrentRenderViewEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.ares.event.client.movement.CancelSprint;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.ares.event.client.movement.SmoothElytraLand;
import net.minecraft.network.Packet;
import net.minecraft.client.network.NetHandlerPlayClient;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import com.ares.hack.hacks.movement.Speed;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.world.PlayerMove;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayerSP.class })
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer
{
    private Minecraft mc;
    
    public MixinEntityPlayerSP() {
        this.mc = Minecraft.func_71410_x();
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") })
    private void preMotion(final CallbackInfo a1) {
        final PlayerMove.Pre v1 = /*EL:29*/new PlayerMove.Pre(this.mc.field_71439_g.field_70177_z, this.mc.field_71439_g.field_70125_A, this.mc.field_71439_g.field_70122_E);
        MinecraftForge.EVENT_BUS.post(/*EL:31*/(Event)v1);
        /*SL:33*/this.mc.field_71439_g.field_70177_z = v1.yaw;
        /*SL:34*/this.mc.field_71439_g.field_70125_A = v1.pitch;
        /*SL:35*/this.mc.field_71439_g.field_70122_E = v1.onGround;
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("RETURN") })
    private void postMotion(final CallbackInfo a1) {
        final PlayerMove.Post v1 = /*EL:41*/new PlayerMove.Post(this.mc.field_71439_g.field_70177_z, this.mc.field_71439_g.field_70125_A, this.mc.field_71439_g.field_70122_E);
        MinecraftForge.EVENT_BUS.post(/*EL:43*/(Event)v1);
        /*SL:45*/this.mc.field_71439_g.field_70177_z = v1.yaw;
        /*SL:46*/this.mc.field_71439_g.field_70125_A = v1.pitch;
        /*SL:47*/this.mc.field_71439_g.field_70122_E = v1.onGround;
    }
    
    @Override
    public void func_70664_aZ() {
        try {
            final double v1 = /*EL:55*/((EntityPlayerSP)this).field_70159_w;
            final double v2 = /*EL:56*/((EntityPlayerSP)this).field_70179_y;
            /*SL:57*/super.func_70664_aZ();
            /*SL:58*/((Speed)HackManager.getHack(Speed.class)).onJump(v1, v2, (EntityPlayerSP)this);
        }
        catch (Exception v3) {
            /*SL:61*/v3.printStackTrace();
        }
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;sendPacket(Lnet/minecraft/network/Packet;)V"))
    private void onSendElytraStartPacket(final NetHandlerPlayClient a1, final Packet<?> a2) {
        final SmoothElytraLand v1 = /*EL:68*/new SmoothElytraLand(this.mc.field_71441_e.field_72995_K);
        MinecraftForge.EVENT_BUS.post(/*EL:69*/(Event)v1);
        /*SL:70*/if (!v1.isWorldRemote && !this.func_70090_H()) {
            /*SL:72*/this.func_70052_a(7, true);
        }
        /*SL:74*/a1.func_147297_a((Packet)a2);
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;collidedHorizontally:Z"))
    private boolean overrideCollidedHorizontally(final EntityPlayerSP a1) {
        final CancelSprint v1 = /*EL:80*/new CancelSprint(a1.field_70123_F);
        MinecraftForge.EVENT_BUS.post(/*EL:81*/(Event)v1);
        /*SL:82*/return v1.shouldCancel;
    }
    
    @Inject(method = { "isCurrentViewEntity" }, at = { @At("RETURN") }, cancellable = true)
    private void isCurrentViewEntityWrapper(final CallbackInfoReturnable<Boolean> a1) {
        final IsCurrentRenderViewEntity v1 = /*EL:88*/new IsCurrentRenderViewEntity(a1.getReturnValue());
        MinecraftForge.EVENT_BUS.post(/*EL:89*/(Event)v1);
        /*SL:90*/a1.setReturnValue(v1.result);
    }
}
