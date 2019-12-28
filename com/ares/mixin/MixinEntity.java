package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.Slice;
import com.ares.event.client.movement.ShouldWalkOffEdge;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.ares.event.entity.GetMaxInPortalTime;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.movement.SetRotation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public abstract class MixinEntity
{
    @Shadow
    public abstract int func_82145_z();
    
    @Shadow
    protected abstract void func_70052_a(final int p0, final boolean p1);
    
    @Shadow
    public abstract boolean func_70090_H();
    
    @Inject(method = { "turn" }, at = { @At("HEAD") }, cancellable = true)
    private void turn(float a1, float a2, final CallbackInfo a3) {
        final SetRotation v1 = /*EL:29*/new SetRotation((Entity)this, a1, a2);
        MinecraftForge.EVENT_BUS.post(/*EL:30*/(Event)v1);
        /*SL:32*/if (v1.isCanceled()) {
            /*SL:33*/a3.cancel();
        }
        /*SL:35*/a1 = v1.yaw;
        /*SL:36*/a2 = v1.pitch;
    }
    
    @Redirect(method = { "onEntityUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getMaxInPortalTime()I"))
    private int getModifiedMaxInPortalTime(final Entity a1) {
        final GetMaxInPortalTime v1 = /*EL:42*/new GetMaxInPortalTime(a1, this.func_82145_z());
        MinecraftForge.EVENT_BUS.post(/*EL:43*/(Event)v1);
        /*SL:45*/return v1.maxInPortalTime;
    }
    
    @Redirect(method = { "move" }, slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;onGround:Z", ordinal = 0)), at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z", ordinal = 0))
    private boolean isSneakingWrapper(final Entity a1) {
        final ShouldWalkOffEdge v1 = /*EL:65*/new ShouldWalkOffEdge(a1.func_70093_af());
        MinecraftForge.EVENT_BUS.post(/*EL:66*/(Event)v1);
        /*SL:67*/return v1.isSneaking;
    }
}
