package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.fontrenderer.DrawString;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ FontRenderer.class })
public abstract class MixinFontRenderer
{
    @Shadow
    protected abstract void func_78255_a(final String p0, final boolean p1);
    
    @Redirect(method = { "renderString" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
    private void renderStrPosWrapper(final FontRenderer a1, final String a2, final boolean a3) {
        final DrawString v1 = /*EL:23*/new DrawString(a2);
        MinecraftForge.EVENT_BUS.post(/*EL:24*/(Event)v1);
        /*SL:25*/this.func_78255_a(v1.text, a3);
    }
}
