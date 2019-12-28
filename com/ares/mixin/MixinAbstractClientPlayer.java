package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.ares.event.client.cape.PlayerSkin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.cape.FindCapeEvent;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { AbstractClientPlayer.class }, priority = 999999999)
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer
{
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo func_175155_b();
    
    @Overwrite
    @Nullable
    public ResourceLocation func_110303_q() {
        final NetworkPlayerInfo v1 = /*EL:33*/this.func_175155_b();
        final FindCapeEvent v2 = /*EL:35*/new FindCapeEvent(v1);
        MinecraftForge.EVENT_BUS.post(/*EL:36*/(Event)v2);
        /*SL:38*/if (v2.capeLoc != null) {
            /*SL:40*/return v2.capeLoc;
        }
        /*SL:43*/return (v1 == null) ? null : v1.func_178861_h();
    }
    
    @Inject(method = { "hasSkin" }, at = { @At("RETURN") }, cancellable = true)
    public void hasSkin(final CallbackInfoReturnable<Boolean> a1) {
        final PlayerSkin.HasSkin v1 = /*EL:49*/new PlayerSkin.HasSkin(this.func_175155_b(), a1.getReturnValue());
        MinecraftForge.EVENT_BUS.post(/*EL:50*/(Event)v1);
        /*SL:51*/a1.setReturnValue(v1.result);
    }
    
    @Inject(method = { "getLocationSkin()Lnet/minecraft/util/ResourceLocation;" }, at = { @At("RETURN") }, cancellable = true)
    public void getSkin(final CallbackInfoReturnable<ResourceLocation> a1) {
        final PlayerSkin.GetSkin v1 = /*EL:57*/new PlayerSkin.GetSkin(this.func_175155_b(), a1.getReturnValue());
        MinecraftForge.EVENT_BUS.post(/*EL:58*/(Event)v1);
        /*SL:59*/a1.setReturnValue(v1.skinLocation);
    }
}
