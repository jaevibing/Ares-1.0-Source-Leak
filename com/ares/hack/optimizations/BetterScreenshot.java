package com.ares.hack.optimizations;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import java.io.File;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.text.TextComponentString;
import com.ares.event.client.render.TakeScreenshot;
import java.nio.IntBuffer;

@Info(name = "Better Screenshots", description = "No lag screenshots", defaultOn = true)
public class BetterScreenshot extends BaseOptimization
{
    private IntBuffer pixelBuffer;
    private int[] pixels;
    
    @SubscribeEvent
    public void onScreenshot(final TakeScreenshot a1) {
        /*SL:33*/if (this.getEnabled()) {
            /*SL:35*/a1.setCanceled(true);
            /*SL:37*/this.saveScreenshot(a1.gameDirectory, a1.width, a1.height, a1.buffer);
            /*SL:39*/a1.result = (ITextComponent)new TextComponentString("Creating screenshot...");
        }
    }
    
    public void saveScreenshot(final File a1, int a2, int a3, final Framebuffer a4) {
        final File v1 = /*EL:45*/new File(a1, "screenshots");
        /*SL:46*/v1.mkdir();
        /*SL:48*/if (OpenGlHelper.func_148822_b()) {
            /*SL:50*/a2 = a4.field_147622_a;
            /*SL:51*/a3 = a4.field_147620_b;
        }
        final int v2 = /*EL:54*/a2 * a3;
        /*SL:55*/if (this.pixelBuffer == null || this.pixelBuffer.capacity() < v2) {
            /*SL:57*/this.pixelBuffer = BufferUtils.createIntBuffer(v2);
            /*SL:58*/this.pixels = new int[v2];
        }
        /*SL:61*/GL11.glPixelStorei(3333, 1);
        /*SL:62*/GL11.glPixelStorei(3317, 1);
        /*SL:63*/this.pixelBuffer.clear();
        /*SL:65*/if (OpenGlHelper.func_148822_b()) {
            /*SL:67*/GlStateManager.func_179144_i(a4.field_147617_g);
            /*SL:68*/GL11.glGetTexImage(3553, 0, 32993, 33639, this.pixelBuffer);
        }
        else {
            /*SL:72*/GL11.glReadPixels(0, 0, a2, a3, 32993, 33639, this.pixelBuffer);
        }
        /*SL:75*/this.pixelBuffer.get(this.pixels);
        /*SL:77*/new Thread(/*EL:82*/new ScreenShotSaver(a2, a3, this.pixels, BetterScreenshot.mc.func_147110_a(), v1), "Screenshot creation thread").start();
    }
}
