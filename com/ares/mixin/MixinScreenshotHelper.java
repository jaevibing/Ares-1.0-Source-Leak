package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import com.ares.event.client.render.TakeScreenshot;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.shader.Framebuffer;
import javax.annotation.Nullable;
import java.io.File;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ScreenShotHelper.class })
public class MixinScreenshotHelper
{
    @Redirect(method = { "Lnet/minecraft/util/ScreenShotHelper;saveScreenshot(Ljava/io/File;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ScreenShotHelper;saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/text/ITextComponent;"))
    private static ITextComponent saveScreenshot(final File a1, @Nullable final String a2, final int a3, final int a4, final Framebuffer a5) {
        final TakeScreenshot v1 = /*EL:33*/new TakeScreenshot(a1, a2, a3, a4, a5);
        MinecraftForge.EVENT_BUS.post(/*EL:34*/(Event)v1);
        /*SL:36*/if (!v1.isCanceled()) {
            /*SL:38*/return ScreenShotHelper.func_148259_a(a1, (String)null, a3, a4, a5);
        }
        /*SL:41*/return v1.result;
    }
}
