package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.block.GetLadderBoundingBox;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.BlockLadder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockLadder.class })
public class MixinBlockLadder
{
    @Shadow
    @Final
    public static PropertyDirection field_176382_a;
    
    @Inject(method = { "getBoundingBox" }, at = { @At("HEAD") }, cancellable = true)
    public void getBoundingBox(final IBlockState a1, final IBlockAccess a2, final BlockPos a3, final CallbackInfoReturnable<AxisAlignedBB> a4) {
        final GetLadderBoundingBox v1 = /*EL:28*/new GetLadderBoundingBox(a1, a2, a3, MixinBlockLadder.field_176382_a, a4);
        MinecraftForge.EVENT_BUS.post(/*EL:29*/(Event)v1);
    }
}
