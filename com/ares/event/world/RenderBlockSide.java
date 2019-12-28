package com.ares.event.world;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import com.ares.event.AresEvent;

@Cancelable
public class RenderBlockSide extends AresEvent
{
    public final IBlockState state;
    public final IBlockAccess world;
    public final BlockPos pos;
    public final EnumFacing side;
    
    public RenderBlockSide(final IBlockState a1, final IBlockAccess a2, final BlockPos a3, final EnumFacing a4) {
        this.state = a1;
        this.world = a2;
        this.pos = a3;
        this.side = a4;
    }
    
    public boolean isCancelable() {
        /*SL:29*/return true;
    }
}
