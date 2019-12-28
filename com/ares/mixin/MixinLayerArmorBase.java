package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import com.ares.utils.ColourUtils;
import com.ares.hack.hacks.render.RainbowEnchant;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { LayerArmorBase.class }, priority = 999999999)
public class MixinLayerArmorBase
{
    @Redirect(method = { "renderEnchantedGlint" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FFFF)V"))
    private static void renderEnchantedGlint(float a2, float a3, float a4, float v1) {
        /*SL:19*/if (RainbowEnchant.INSTANCE != null && RainbowEnchant.INSTANCE.getEnabled()) {
            final Color a5 = /*EL:21*/ColourUtils.rainbow();
            /*SL:22*/a2 = a5.getRed();
            /*SL:23*/a4 = a5.getBlue();
            /*SL:24*/a3 = a5.getGreen();
            /*SL:25*/v1 = a5.getAlpha();
        }
        /*SL:27*/GlStateManager.func_179131_c(a2, a3, a4, v1);
    }
}
