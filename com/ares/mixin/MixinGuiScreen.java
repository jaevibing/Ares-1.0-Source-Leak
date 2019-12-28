package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.gui.RenderGuiBackground;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiScreen.class })
public class MixinGuiScreen
{
    @Shadow
    public Minecraft field_146297_k;
    
    @Inject(method = { "drawWorldBackground(I)V" }, at = { @At("HEAD") }, cancellable = true)
    private void drawWorldBackgroundWrapper(final int a1, final CallbackInfo a2) {
        final RenderGuiBackground v1 = /*EL:23*/new RenderGuiBackground();
        MinecraftForge.EVENT_BUS.post(/*EL:24*/(Event)v1);
        /*SL:26*/if (this.field_146297_k.field_71441_e != null && v1.getResult() == Event.Result.ALLOW) {
            /*SL:28*/a2.cancel();
        }
    }
}
