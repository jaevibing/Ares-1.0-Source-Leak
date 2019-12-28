package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.render.RenderItemFromModel;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import com.ares.utils.ColourUtils;
import com.ares.hack.hacks.render.RainbowEnchant;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { RenderItem.class }, priority = 999999999)
public abstract class MixinRenderItem
{
    @Shadow
    protected abstract void func_191961_a(final IBakedModel p0, final ItemStack p1);
    
    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderModel(final int a1) {
        /*SL:25*/if (RainbowEnchant.INSTANCE != null && RainbowEnchant.INSTANCE.getEnabled()) {
            /*SL:27*/return ColourUtils.rainbow().getRGB();
        }
        /*SL:29*/return a1;
    }
    
    @Redirect(method = { "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"))
    private void renderItemFromModelWrapper(final RenderItem a1, final IBakedModel a2, final ItemStack a3) {
        final RenderItemFromModel v1 = /*EL:41*/new RenderItemFromModel();
        MinecraftForge.EVENT_BUS.post(/*EL:42*/(Event)v1);
        /*SL:43*/this.func_191961_a(a2, a3);
    }
    
    @Redirect(method = { "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableRescaleNormal()V"))
    private void rescaleNormalWrapper() {
        final RenderItemFromModel v1 = /*EL:55*/new RenderItemFromModel();
        MinecraftForge.EVENT_BUS.post(/*EL:56*/(Event)v1);
    }
}
