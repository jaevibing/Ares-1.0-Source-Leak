package com.ares.mixin;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.entity.GetPortalCooldown;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.Shadow;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { EntityPlayer.class }, priority = 9900)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase
{
    @Shadow
    public abstract GameProfile func_146103_bH();
    
    @ModifyConstant(method = { "attackTargetEntityWithCurrentItem" }, constant = { @Constant(doubleValue = 0.6) })
    private double decelerate(final double a1) {
        /*SL:20*/return 1.0;
    }
    
    @Redirect(method = { "attackTargetEntityWithCurrentItem" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;setSprinting(Z)V"))
    private void dontSprintPlsThx(final EntityPlayer a1, final boolean a2) {
    }
    
    @ModifyConstant(method = { "getPortalCooldown" }, constant = { @Constant(intValue = 10) })
    private int getModifiedPortalCooldown(final int a1) {
        final GetPortalCooldown v1 = /*EL:33*/new GetPortalCooldown(a1, (EntityPlayer)this);
        MinecraftForge.EVENT_BUS.post(/*EL:34*/(Event)v1);
        /*SL:36*/return v1.cooldown;
    }
}
