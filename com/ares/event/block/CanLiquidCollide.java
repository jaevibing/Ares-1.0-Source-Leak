package com.ares.event.block;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.ares.event.AresEvent;

public class CanLiquidCollide extends AresEvent
{
    public CallbackInfoReturnable<Boolean> cir;
    
    public CanLiquidCollide(final CallbackInfoReturnable<Boolean> a1) {
        this.cir = a1;
    }
}
