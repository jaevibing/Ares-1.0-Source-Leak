package com.ares.hack.hacks.client;

import net.minecraft.entity.Entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import com.ares.Globals;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "DebugCrosshair", description = "Show f3 crosshair", category = EnumCategory.CLIENT)
public class DebugCrosshair extends BaseHack
{
    @SubscribeEvent
    public void stopRegularCrosshair(final RenderGameOverlayEvent v-1) {
        /*SL:27*/if (this.getEnabled() && v-1.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            /*SL:29*/v-1.setCanceled(true);
            final int a1 = /*EL:30*/new ScaledResolution(DebugCrosshair.mc).func_78326_a();
            final int v1 = /*EL:31*/new ScaledResolution(DebugCrosshair.mc).func_78328_b();
            final float v2 = /*EL:32*/(float)ObfuscationReflectionHelper.getPrivateValue((Class)Gui.class, (Object)Globals.mc.field_71456_v, new String[] { "zLevel", "field_73735_i" });
            renderCrosshair(/*EL:33*/v-1.getPartialTicks(), a1, v1, v2);
        }
    }
    
    private static void renderCrosshair(final float v1, final int v2, final int v3, final float v4) {
        final GameSettings v5 = DebugCrosshair.mc.field_71474_y;
        /*SL:41*/if (v5.field_74320_O == 0) {
            /*SL:43*/if (DebugCrosshair.mc.field_71442_b.func_78747_a() && DebugCrosshair.mc.field_147125_j == null) {
                final RayTraceResult a1 = DebugCrosshair.mc.field_71476_x;
                /*SL:47*/if (a1 == null || a1.field_72313_a != RayTraceResult.Type.BLOCK) {
                    /*SL:49*/return;
                }
                final BlockPos a2 = /*EL:52*/a1.func_178782_a();
                final IBlockState a3 = DebugCrosshair.mc.field_71441_e.func_180495_p(/*EL:54*/a2);
                /*SL:55*/if (!a3.func_177230_c().hasTileEntity(a3) || !(DebugCrosshair.mc.field_71441_e.func_175625_s(a2) instanceof IInventory)) {
                    /*SL:57*/return;
                }
            }
            /*SL:61*/if (!v5.field_74319_N) {
                /*SL:63*/GlStateManager.func_179094_E();
                /*SL:64*/GlStateManager.func_179109_b((float)(v2 / 2), (float)(v3 / 2), v4);
                final Entity a4 = DebugCrosshair.mc.func_175606_aa();
                /*SL:66*/if (a4 != null) {
                    /*SL:68*/GlStateManager.func_179114_b(a4.field_70127_C + (a4.field_70125_A - a4.field_70127_C) * v1, -1.0f, 0.0f, 0.0f);
                    /*SL:69*/GlStateManager.func_179114_b(a4.field_70126_B + (a4.field_70177_z - a4.field_70126_B) * v1, 0.0f, 1.0f, 0.0f);
                    /*SL:70*/GlStateManager.func_179152_a(-1.0f, -1.0f, -1.0f);
                    /*SL:71*/OpenGlHelper.func_188785_m(10);
                    /*SL:72*/GlStateManager.func_179121_F();
                }
            }
        }
    }
}
