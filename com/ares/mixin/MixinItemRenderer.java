package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.render.RenderItemInFirstPerson;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { ItemRenderer.class }, priority = 999999999)
public abstract class MixinItemRenderer
{
    @Shadow
    public abstract void func_187457_a(final AbstractClientPlayer p0, final float p1, final float p2, final EnumHand p3, final float p4, final ItemStack p5, final float p6);
    
    @Inject(method = { "renderWaterOverlayTexture" }, at = { @At("HEAD") }, cancellable = true)
    private void renderWaterOverlayTexture(final float a1, final CallbackInfo a2) {
    }
    
    @Redirect(method = { "renderItemInFirstPerson(F)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"))
    private void renderItemInFirstPerson(final ItemRenderer a1, final AbstractClientPlayer a2, final float a3, final float a4, final EnumHand a5, final float a6, final ItemStack a7, final float a8) {
        final RenderItemInFirstPerson v1 = /*EL:43*/new RenderItemInFirstPerson(a1, a2, a3, a4, a5, a6, a7, a8);
        MinecraftForge.EVENT_BUS.post(/*EL:44*/(Event)v1);
        /*SL:45*/v1.itemRenderer.func_187457_a(v1.player, v1.partialTicks, v1.pitch, v1.hand, v1.swingProgress, v1.stack, v1.equipProgress);
    }
}
