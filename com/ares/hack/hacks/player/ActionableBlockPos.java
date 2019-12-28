package com.ares.hack.hacks.player;

import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

class ActionableBlockPos extends BlockPos
{
    BlockAction blockAction;
    
    public ActionableBlockPos(final int a1, final int a2, final int a3, final BlockAction a4) {
        super(a1, a2, a3);
        this.blockAction = a4;
    }
    
    public ActionableBlockPos(final double a1, final double a2, final double a3, final BlockAction a4) {
        super(a1, a2, a3);
        this.blockAction = a4;
    }
    
    public ActionableBlockPos(final Entity a1, final BlockAction a2) {
        super(a1);
        this.blockAction = a2;
    }
    
    public ActionableBlockPos(final Vec3d a1, final BlockAction a2) {
        super(a1);
        this.blockAction = a2;
    }
    
    public ActionableBlockPos(final Vec3i a1, final BlockAction a2) {
        super(a1);
        this.blockAction = a2;
    }
    
    public static ActionableBlockPos fromBlockPos(final BlockPos a1, final BlockAction a2) {
        /*SL:414*/return new ActionableBlockPos(a1.func_177958_n(), a1.func_177956_o(), a1.func_177952_p(), a2);
    }
}
