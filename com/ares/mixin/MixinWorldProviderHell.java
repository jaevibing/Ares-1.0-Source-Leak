package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.world.GetDimensionBrightness;
import net.minecraft.world.WorldProviderHell;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ WorldProviderHell.class })
public class MixinWorldProviderHell
{
    @ModifyConstant(method = { "generateLightBrightnessTable" }, constant = { @Constant(floatValue = 0.9f) })
    private float getBrightness(final float a1) {
        final GetDimensionBrightness v1 = /*EL:16*/new GetDimensionBrightness(a1);
        MinecraftForge.EVENT_BUS.post(/*EL:17*/(Event)v1);
        /*SL:18*/return v1.initial;
    }
}
