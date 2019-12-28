package com.ares.mixin;

import com.ares.event.block.CheckBlockLight;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.world.GetDimensionBrightness;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ World.class })
public class MixinWorld
{
    @Inject(method = { "getSunBrightnessFactor" }, at = { @At("RETURN") }, cancellable = true, remap = false)
    private void getBrightnessOfSun(final float a1, final CallbackInfoReturnable<Float> a2) {
        final GetDimensionBrightness v1 = /*EL:20*/new GetDimensionBrightness(a2.getReturnValue());
        MinecraftForge.EVENT_BUS.post(/*EL:21*/(Event)v1);
        /*SL:22*/a2.setReturnValue(v1.initial);
    }
    
    @Inject(method = { "getSunBrightnessBody" }, at = { @At("RETURN") }, cancellable = true, remap = false)
    private void getBrightnessBodyOfSun(final float a1, final CallbackInfoReturnable<Float> a2) {
        final GetDimensionBrightness v1 = /*EL:28*/new GetDimensionBrightness(a2.getReturnValue());
        MinecraftForge.EVENT_BUS.post(/*EL:29*/(Event)v1);
        /*SL:30*/a2.setReturnValue(v1.initial);
    }
    
    @Inject(method = { "checkLightFor" }, at = { @At("HEAD") }, cancellable = true)
    private void checkLightForWrapper(final EnumSkyBlock a1, final BlockPos a2, final CallbackInfoReturnable<Boolean> a3) {
        final CheckBlockLight v1 = /*EL:36*/new CheckBlockLight(null);
        MinecraftForge.EVENT_BUS.post(/*EL:37*/(Event)v1);
        /*SL:38*/if (v1.checkLight != null) {
            /*SL:40*/a3.setReturnValue(v1.checkLight);
        }
    }
}
