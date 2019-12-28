package com.ares.hack.hacks.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.NonNullList;
import com.ares.extensions.Wrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import net.minecraft.util.ResourceLocation;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Inventory Preview", description = "Shows you a preview of whats in your inv", category = EnumCategory.RENDER)
public class InvPreview extends BaseHack
{
    private static final ResourceLocation SHULKER_ICON;
    private static final int CORNER = 5;
    private static final int BUFFER = 1;
    private static final int EDGE = 18;
    private Setting<Integer> xSetting;
    private Setting<Integer> ySetting;
    
    public InvPreview() {
        this.xSetting = new IntegerSetting("x", this, 2, 0, InvPreview.mc.field_71443_c + 100);
        this.ySetting = new IntegerSetting("y", this, 2, 0, InvPreview.mc.field_71440_d + 100);
    }
    
    public void onRender2d() {
        /*SL:49*/if (!this.getEnabled()) {
            return;
        }
        final int intValue = /*EL:53*/this.xSetting.getValue();
        final int intValue2 = /*EL:54*/this.ySetting.getValue();
        final NonNullList<ItemStack> field_70462_a = (NonNullList<ItemStack>)InvPreview.mc.field_71439_g.field_71071_by.field_70462_a;
        /*SL:58*/GlStateManager.func_179094_E();
        /*SL:59*/RenderHelper.func_74519_b();
        /*SL:60*/GlStateManager.func_179091_B();
        /*SL:61*/GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
        /*SL:62*/GlStateManager.func_179109_b(0.0f, 0.0f, 700.0f);
        InvPreview.mc.func_110434_K().func_110577_a(InvPreview.SHULKER_ICON);
        /*SL:64*/RenderHelper.func_74518_a();
        /*SL:66*/this.renderBackground(intValue, intValue2, 9, 3, 1973019);
        /*SL:68*/RenderHelper.func_74520_c();
        /*SL:70*/for (int v0 = 9; v0 < field_70462_a.size(); ++v0) {
            final ItemStack v = /*EL:72*/(ItemStack)field_70462_a.get(v0);
            final int v2 = /*EL:73*/intValue + 6 + v0 % 9 * 18;
            final int v3 = /*EL:74*/intValue2 + 6 + v0 / 9 * 18 - 18;
            /*SL:76*/if (!v.func_190926_b()) {
                InvPreview.mc.func_175599_af().func_180450_b(/*EL:78*/v, v2, v3);
                InvPreview.mc.func_175599_af().func_175030_a(Wrapper.fontRenderer, /*EL:79*/v, v2, v3);
            }
        }
        /*SL:82*/RenderHelper.func_74518_a();
        /*SL:84*/GlStateManager.func_179097_i();
        /*SL:85*/GlStateManager.func_179101_C();
        /*SL:86*/GlStateManager.func_179121_F();
        /*SL:88*/GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderBackground(final int a4, final int a5, final int v1, final int v2, final int v3) {
        InvPreview.mc.func_110434_K().func_110577_a(InvPreview.SHULKER_ICON);
        /*SL:95*/GlStateManager.func_179124_c(((v3 & 0xFF0000) >> 16) / 255.0f, ((v3 & 0xFF00) >> 8) / 255.0f, (v3 & 0xFF) / 255.0f);
        /*SL:99*/RenderHelper.func_74518_a();
        /*SL:101*/Gui.func_146110_a(a4, a5, 0.0f, 0.0f, 5, 5, 256.0f, 256.0f);
        /*SL:105*/Gui.func_146110_a(a4 + 5 + 18 * v1, a5 + 5 + 18 * v2, 25.0f, 25.0f, 5, 5, 256.0f, 256.0f);
        /*SL:109*/Gui.func_146110_a(a4 + 5 + 18 * v1, a5, 25.0f, 0.0f, 5, 5, 256.0f, 256.0f);
        /*SL:113*/Gui.func_146110_a(a4, a5 + 5 + 18 * v2, 0.0f, 25.0f, 5, 5, 256.0f, 256.0f);
        /*SL:117*/for (int a6 = 0; a6 < v2; ++a6) {
            /*SL:118*/Gui.func_146110_a(a4, a5 + 5 + 18 * a6, 0.0f, 6.0f, 5, 18, 256.0f, 256.0f);
            /*SL:122*/Gui.func_146110_a(a4 + 5 + 18 * v1, a5 + 5 + 18 * a6, 25.0f, 6.0f, 5, 18, 256.0f, 256.0f);
            /*SL:126*/for (int a7 = 0; a7 < v1; ++a7) {
                /*SL:127*/if (a6 == 0) {
                    /*SL:128*/Gui.func_146110_a(a4 + 5 + 18 * a7, a5, 6.0f, 0.0f, 18, 5, 256.0f, 256.0f);
                    /*SL:131*/Gui.func_146110_a(a4 + 5 + 18 * a7, a5 + 5 + 18 * v2, 6.0f, 25.0f, 18, 5, 256.0f, 256.0f);
                }
                /*SL:136*/Gui.func_146110_a(a4 + 5 + 18 * a7, a5 + 5 + 18 * a6, 6.0f, 6.0f, 18, 18, 256.0f, 256.0f);
            }
        }
        /*SL:142*/GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
    }
    
    static {
        SHULKER_ICON = new ResourceLocation("ares", "inv_slot.png");
    }
}
