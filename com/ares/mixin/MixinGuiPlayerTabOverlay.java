package com.ares.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { GuiPlayerTabOverlay.class }, priority = 999999999)
public class MixinGuiPlayerTabOverlay
{
    @Redirect(method = { "renderPlayerlist" }, at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", ordinal = 0))
    public int noMin(final int a1, final int a2) {
        /*SL:17*/return a1;
    }
    
    @ModifyConstant(method = { "renderPlayerlist" }, constant = { @Constant(intValue = 20, ordinal = 0) })
    public int getNumRows(final int a1) {
        /*SL:23*/return 30;
    }
    
    @Redirect(method = { "renderPlayerlist" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isIntegratedServerRunning()Z"))
    public boolean renderPlayerIcons(final Minecraft a1) {
        /*SL:29*/return true;
    }
}
