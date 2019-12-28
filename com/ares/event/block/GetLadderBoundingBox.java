package com.ares.event.block;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import com.ares.event.AresEvent;

public class GetLadderBoundingBox extends AresEvent
{
    public IBlockState state;
    public IBlockAccess source;
    public BlockPos pos;
    public CallbackInfoReturnable<AxisAlignedBB> cir;
    public PropertyDirection facing;
    
    public GetLadderBoundingBox(final IBlockState a1, final IBlockAccess a2, final BlockPos a3, final PropertyDirection a4, final CallbackInfoReturnable<AxisAlignedBB> a5) {
        this.state = a1;
        this.source = a2;
        this.pos = a3;
        this.facing = a4;
        this.cir = a5;
    }
}
