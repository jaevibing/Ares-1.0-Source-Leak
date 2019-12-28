package com.ares.event.client.render;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.shader.Framebuffer;
import java.io.File;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import com.ares.event.AresEvent;

@Cancelable
public class TakeScreenshot extends AresEvent
{
    public File gameDirectory;
    public String screenshotName;
    public int width;
    public int height;
    public Framebuffer buffer;
    public ITextComponent result;
    
    public TakeScreenshot(final File a1, final String a2, final int a3, final int a4, final Framebuffer a5) {
        this.gameDirectory = a1;
        this.screenshotName = a2;
        this.width = a3;
        this.height = a4;
        this.buffer = a5;
        this.result = (ITextComponent)new TextComponentString("Screenshot meant to be here?");
    }
}
