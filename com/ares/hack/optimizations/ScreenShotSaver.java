package com.ares.hack.optimizations;

import java.util.Date;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.util.text.ITextComponent;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.TextComponentString;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.OpenGlHelper;
import java.io.File;
import net.minecraft.client.shader.Framebuffer;
import java.text.SimpleDateFormat;

class ScreenShotSaver implements Runnable
{
    private static final SimpleDateFormat DATE_FORMAT;
    private int width;
    private int height;
    private int[] pixels;
    private Framebuffer frameBuffer;
    private File screenshotDir;
    
    ScreenShotSaver(final int a1, final int a2, final int[] a3, final Framebuffer a4, final File a5) {
        this.width = a1;
        this.height = a2;
        this.pixels = a3;
        this.frameBuffer = a4;
        this.screenshotDir = a5;
    }
    
    @Override
    public void run() {
        wtf(/*EL:111*/this.pixels, this.width, this.height);
        BufferedImage bufferedImage = /*EL:112*/null;
        try {
            /*SL:115*/if (OpenGlHelper.func_148822_b()) {
                /*SL:118*/bufferedImage = new BufferedImage(this.frameBuffer.field_147621_c, this.frameBuffer.field_147618_d, 1);
                int v0;
                /*SL:121*/for (int i = v0 = this.frameBuffer.field_147620_b - this.frameBuffer.field_147618_d; i < this.frameBuffer.field_147620_b; ++i) {
                    /*SL:123*/for (int v = 0; v < this.frameBuffer.field_147621_c; ++v) {
                        /*SL:125*/bufferedImage.setRGB(v, i - v0, this.pixels[i * this.frameBuffer.field_147622_a + v]);
                    }
                }
            }
            else {
                /*SL:131*/bufferedImage = new BufferedImage(this.width, this.height, 1);
                /*SL:132*/bufferedImage.setRGB(0, 0, this.width, this.height, this.pixels, 0, this.width);
            }
            final File timestampedPNGFileForDirectory = getTimestampedPNGFileForDirectory(/*EL:135*/this.screenshotDir);
            /*SL:136*/ImageIO.write(bufferedImage, "png", timestampedPNGFileForDirectory);
            final ITextComponent v2 = /*EL:138*/(ITextComponent)new TextComponentString(timestampedPNGFileForDirectory.getName());
            /*SL:139*/v2.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.OPEN_FILE, timestampedPNGFileForDirectory.getAbsolutePath()));
            /*SL:140*/v2.func_150256_b().func_150228_d(true);
            /*SL:142*/ChatUtils.printMessage((ITextComponent)new TextComponentTranslation("screenshot.success", new Object[] { v2 }));
        }
        catch (Exception ex) {
            FMLLog.log.info(/*EL:146*/"Failed to save screenshot");
            /*SL:147*/ex.printStackTrace();
            /*SL:148*/ChatUtils.printMessage((ITextComponent)new TextComponentTranslation("screenshot.failure", new Object[] { ex.getMessage() }));
        }
    }
    
    private static void wtf(final int[] a3, final int v1, final int v2) {
        final int[] v3 = /*EL:154*/new int[v1];
        /*SL:156*/for (int a4 = v2 / 2, a5 = 0; a5 < a4; ++a5) {
            /*SL:158*/System.arraycopy(a3, a5 * v1, v3, 0, v1);
            /*SL:159*/System.arraycopy(a3, (v2 - 1 - a5) * v1, a3, a5 * v1, v1);
            /*SL:160*/System.arraycopy(v3, 0, a3, (v2 - 1 - a5) * v1, v1);
        }
    }
    
    private static File getTimestampedPNGFileForDirectory(final File v1) {
        final String v2 = ScreenShotSaver.DATE_FORMAT.format(/*EL:166*/new Date());
        int v3 = /*EL:167*/1;
        File a1;
        while (true) {
            /*SL:171*/a1 = new File(v1, v2 + ((v3 == 1) ? "" : ("_" + v3)) + ".png");
            /*SL:173*/if (!a1.exists()) {
                break;
            }
            /*SL:178*/++v3;
        }
        return a1;
    }
    
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    }
}
