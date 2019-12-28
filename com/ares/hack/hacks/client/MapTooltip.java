package com.ares.hack.hacks.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.storage.MapData;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.World;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraft.util.ResourceLocation;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Map Tooltip", description = "Tooltips to preview maps", category = EnumCategory.CLIENT)
public class MapTooltip extends BaseHack
{
    private static final ResourceLocation MAP;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void makeTooltip(final ItemTooltipEvent a1) {
    }
    
    @SubscribeEvent
    public void renderTooltip(final RenderTooltipEvent.PostText v-2) {
        /*SL:50*/if (!this.getEnabled()) {
            return;
        }
        /*SL:52*/if (!v-2.getStack().func_190926_b() && v-2.getStack().func_77973_b() instanceof ItemMap) {
            final MapData func_77873_a = Items.field_151098_aY.func_77873_a(/*EL:54*/v-2.getStack(), (World)MapTooltip.mc.field_71441_e);
            /*SL:56*/if (func_77873_a != null) {
                /*SL:59*/GlStateManager.func_179094_E();
                /*SL:60*/GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
                /*SL:61*/RenderHelper.func_74518_a();
                MapTooltip.mc.func_110434_K().func_110577_a(MapTooltip.MAP);
                final Tessellator a1 = /*EL:63*/Tessellator.func_178181_a();
                final BufferBuilder v1 = /*EL:64*/a1.func_178180_c();
                final int v2 = /*EL:66*/7;
                final float v3 = /*EL:67*/135.0f;
                final float v4 = /*EL:68*/0.5f;
                /*SL:70*/GlStateManager.func_179109_b((float)v-2.getX(), v-2.getY() - v3 * v4 - 5.0f, 0.0f);
                /*SL:71*/GlStateManager.func_179152_a(v4, v4, v4);
                /*SL:74*/v1.func_181668_a(7, DefaultVertexFormats.field_181707_g);
                /*SL:75*/v1.func_181662_b((double)(-v2), (double)v3, 0.0).func_187315_a(0.0, 1.0).func_181675_d();
                /*SL:76*/v1.func_181662_b((double)v3, (double)v3, 0.0).func_187315_a(1.0, 1.0).func_181675_d();
                /*SL:77*/v1.func_181662_b((double)v3, (double)(-v2), 0.0).func_187315_a(1.0, 0.0).func_181675_d();
                /*SL:78*/v1.func_181662_b((double)(-v2), (double)(-v2), 0.0).func_187315_a(0.0, 0.0).func_181675_d();
                /*SL:79*/a1.func_78381_a();
                MapTooltip.mc.field_71460_t.func_147701_i().func_148250_a(/*EL:82*/func_77873_a, false);
                /*SL:84*/GlStateManager.func_179145_e();
                /*SL:85*/GlStateManager.func_179121_F();
            }
        }
    }
    
    static {
        MAP = new ResourceLocation("map/map_background.png");
    }
}
