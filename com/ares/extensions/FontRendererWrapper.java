package com.ares.extensions;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.FontRenderer;

public class FontRendererWrapper extends FontRenderer
{
    public FontRendererWrapper(final GameSettings a1, final ResourceLocation a2, final TextureManager a3, final boolean a4) {
        super(a1, a2, a3, a4);
    }
}
