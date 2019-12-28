package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.block.CanLiquidCollide;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockLiquid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockLiquid.class })
public class MixinBlockLiquid
{
    @Inject(method = { "canCollideCheck" }, at = { @At("HEAD") }, cancellable = true)
    public void canCollideWithLiquid(final IBlockState a1, final boolean a2, final CallbackInfoReturnable<Boolean> a3) {
        final CanLiquidCollide v1 = /*EL:18*/new CanLiquidCollide(a3);
        MinecraftForge.EVENT_BUS.post(/*EL:19*/(Event)v1);
    }
}
