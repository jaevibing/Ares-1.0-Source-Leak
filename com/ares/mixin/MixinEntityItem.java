package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.entity.GetPickupDelay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityItem.class })
public abstract class MixinEntityItem
{
    @Shadow
    private int field_145804_b;
    
    @Inject(method = { "setPickupDelay" }, at = { @At("RETURN") }, cancellable = true)
    public void setPickupDelayWrap(final int a1, final CallbackInfo a2) {
        final GetPickupDelay v1 = /*EL:26*/new GetPickupDelay(this.field_145804_b);
        MinecraftForge.EVENT_BUS.post(/*EL:27*/(Event)v1);
        /*SL:28*/this.field_145804_b = v1.delay;
    }
    
    @Inject(method = { "setDefaultPickupDelay" }, at = { @At("RETURN") }, cancellable = true)
    public void setDefaultPickupDelayWrap(final CallbackInfo a1) {
        final GetPickupDelay v1 = /*EL:37*/new GetPickupDelay(this.field_145804_b);
        MinecraftForge.EVENT_BUS.post(/*EL:38*/(Event)v1);
        /*SL:39*/this.field_145804_b = v1.delay;
    }
    
    @Inject(method = { "setNoPickupDelay" }, at = { @At("RETURN") }, cancellable = true)
    public void setNoPickupDelayWrap(final CallbackInfo a1) {
        final GetPickupDelay v1 = /*EL:48*/new GetPickupDelay(this.field_145804_b);
        MinecraftForge.EVENT_BUS.post(/*EL:49*/(Event)v1);
        /*SL:50*/this.field_145804_b = v1.delay;
    }
}
