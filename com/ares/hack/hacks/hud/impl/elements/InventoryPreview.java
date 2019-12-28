package com.ares.hack.hacks.hud.impl.elements;

import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import com.ares.utils.render.Render2dUtils;
import com.ares.hack.hacks.hud.impl.GuiEditHud;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "Inventory", description = "Shows preview of inventory", category = EnumCategory.HUD, defaultIsVisible = false)
public class InventoryPreview extends MoveableHudElement
{
    private static final Color backgroundColor;
    
    public InventoryPreview() {
        super("Inventory Preview");
    }
    
    @Override
    public void render(final int v-5, final int v-4, final float v-3) {
        int x = /*EL:28*/18;
        /*SL:29*/x *= 9;
        final int y = /*EL:30*/54;
        /*SL:32*/this.getSize().x = x;
        /*SL:33*/this.getSize().y = y;
        /*SL:35*/super.render(v-5, v-4, v-3);
        /*SL:36*/if (!this.shouldRender()) {
            return;
        }
        final NonNullList<ItemStack> v0 = (NonNullList<ItemStack>)InventoryPreview.mc.field_71439_g.field_71071_by.field_70462_a;
        /*SL:40*/GlStateManager.func_179094_E();
        /*SL:41*/RenderHelper.func_74519_b();
        /*SL:42*/GlStateManager.func_179091_B();
        /*SL:43*/GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
        /*SL:44*/GlStateManager.func_179109_b(0.0f, 0.0f, 700.0f);
        /*SL:45*/RenderHelper.func_74518_a();
        /*SL:47*/if (!(InventoryPreview.mc.field_71462_r instanceof GuiEditHud)) {
            /*SL:48*/Render2dUtils.drawRect(this.getPos().x, this.getPos().y, this.getPos().x + this.getSize().x, this.getPos().y + this.getSize().y, InventoryPreview.backgroundColor.getRGB());
        }
        /*SL:50*/RenderHelper.func_74520_c();
        /*SL:52*/for (int v = 9; v < v0.size(); ++v) {
            final ItemStack a1 = /*EL:54*/(ItemStack)v0.get(v);
            final int a2 = /*EL:55*/this.getPos().x + v % 9 * 18;
            final int a3 = /*EL:56*/this.getPos().y + v / 9 * 18 - 18;
            /*SL:58*/if (!a1.func_190926_b()) {
                InventoryPreview.mc.func_175599_af().func_180450_b(/*EL:60*/a1, a2, a3);
                InventoryPreview.mc.func_175599_af().func_175030_a(InventoryPreview.mc.field_71466_p, /*EL:61*/a1, a2, a3);
            }
        }
        /*SL:64*/RenderHelper.func_74518_a();
        /*SL:66*/GlStateManager.func_179097_i();
        /*SL:67*/GlStateManager.func_179101_C();
        /*SL:68*/GlStateManager.func_179121_F();
        /*SL:70*/GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        backgroundColor = new Color(64, 64, 64, 127);
    }
}
