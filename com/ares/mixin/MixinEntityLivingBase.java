package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.ares.event.client.movement.SmoothElytraLand;
import net.minecraft.world.World;
import com.ares.event.entity.GetWaterJumpHeight;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.entity.GetWaterSlowdown;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityLivingBase.class })
public abstract class MixinEntityLivingBase extends MixinEntity
{
    @Shadow
    public void func_70664_aZ() {
    }
    
    @ModifyConstant(method = { "getWaterSlowDown" }, constant = { @Constant(floatValue = 0.8f) })
    public float getWaterSlowDownWrapper(final float a1) {
        final GetWaterSlowdown v1 = /*EL:25*/new GetWaterSlowdown(a1);
        MinecraftForge.EVENT_BUS.post(/*EL:26*/(Event)v1);
        /*SL:27*/return v1.initialVal;
    }
    
    @ModifyConstant(method = { "handleJumpWater" }, constant = { @Constant(doubleValue = 0.03999999910593033) })
    public double handleJumpWaterWrap(final double a1) {
        final GetWaterJumpHeight v1 = /*EL:33*/new GetWaterJumpHeight(a1);
        MinecraftForge.EVENT_BUS.post(/*EL:34*/(Event)v1);
        /*SL:36*/return v1.height;
    }
    
    @ModifyConstant(method = { "handleJumpLava" }, constant = { @Constant(doubleValue = 0.03999999910593033) })
    public double handleJumpLavaWrap(final double a1) {
        final GetWaterJumpHeight v1 = /*EL:42*/new GetWaterJumpHeight(a1);
        MinecraftForge.EVENT_BUS.post(/*EL:43*/(Event)v1);
        /*SL:45*/return v1.height;
    }
    
    @Redirect(method = { "travel" }, at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z", ordinal = 1))
    private boolean isWorldRemoteWrapper(final World a1) {
        final SmoothElytraLand v1 = /*EL:52*/new SmoothElytraLand(a1.field_72995_K);
        MinecraftForge.EVENT_BUS.post(/*EL:53*/(Event)v1);
        /*SL:54*/return v1.isWorldRemote;
    }
}
