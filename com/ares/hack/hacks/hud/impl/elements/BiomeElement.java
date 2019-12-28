package com.ares.hack.hacks.hud.impl.elements;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import com.ares.utils.ColourUtils;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

public class BiomeElement extends MoveableHudElement
{
    public BiomeElement() {
        super("BiomeElement");
    }
    
    @Override
    public void render(final int a1, final int a2, final float a3) {
        /*SL:19*/super.render(a1, a2, a3);
        /*SL:20*/if (!this.shouldRender()) {
            return;
        }
        final Chunk v1 = BiomeElement.mc.field_71441_e.func_175726_f(BiomeElement.mc.field_71439_g.func_180425_c());
        final Biome v2 = /*EL:23*/v1.func_177411_a(BiomeElement.mc.field_71439_g.func_180425_c(), BiomeElement.mc.field_71441_e.func_72959_q());
        /*SL:25*/ColourUtils.drawRainbowString(v2.func_185359_l(), this.getPos().x, this.getPos().y);
    }
}
