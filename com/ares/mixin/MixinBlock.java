package com.ares.mixin;

import com.ares.event.world.GetLightmapCoords;
import com.ares.event.world.GetAmbientOcclusionLightValue;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.block.GetBlockOpacity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Block.class })
public class MixinBlock
{
    @Inject(method = { "shouldSideBeRendered" }, at = { @At("HEAD") }, cancellable = true)
    public void shouldSideBeRendered(final IBlockState a1, final IBlockAccess a2, final BlockPos a3, final EnumFacing a4, final CallbackInfoReturnable<Boolean> a5) {
        final GetBlockOpacity v1 = /*EL:23*/new GetBlockOpacity(null);
        MinecraftForge.EVENT_BUS.post(/*EL:24*/(Event)v1);
        /*SL:25*/if (v1.alphaMultiplier != null) {
            /*SL:27*/((Block)this).func_149715_a((float)v1.alphaMultiplier);
            /*SL:28*/a5.setReturnValue(true);
        }
    }
    
    @Inject(method = { "getAmbientOcclusionLightValue" }, at = { @At("RETURN") }, cancellable = true)
    private void getAmbientOcclusionLightValue(final CallbackInfoReturnable<Float> a1) {
        final GetAmbientOcclusionLightValue v1 = /*EL:35*/new GetAmbientOcclusionLightValue(a1.getReturnValue());
        MinecraftForge.EVENT_BUS.post(/*EL:36*/(Event)v1);
        /*SL:37*/a1.setReturnValue(v1.level);
    }
    
    @Inject(method = { "getPackedLightmapCoords" }, at = { @At("HEAD") }, cancellable = true)
    private void getPackedLightmapCoordsWrapper(final IBlockState a1, final IBlockAccess a2, final BlockPos a3, final CallbackInfoReturnable<Integer> a4) {
        final GetLightmapCoords v1 = /*EL:43*/new GetLightmapCoords(null);
        MinecraftForge.EVENT_BUS.post(/*EL:44*/(Event)v1);
        /*SL:45*/if (v1.lightmapCoords != null) {
            /*SL:47*/a4.setReturnValue(v1.lightmapCoords);
        }
    }
}
