package com.ares.event.world;

import net.minecraft.util.math.BlockPos;
import com.ares.event.AresEvent;

public class MakeCubeOpaque extends AresEvent
{
    public final BlockPos pos;
    
    public MakeCubeOpaque(final BlockPos a1) {
        this.pos = a1;
    }
    
    public boolean isCancelable() {
        /*SL:18*/return true;
    }
}
