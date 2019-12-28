package com.ares.hack.hacks.hud.impl.elements;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "Player", description = "Shows preview of player", category = EnumCategory.HUD, defaultIsVisible = false)
public class PlayerPreviewElement extends MoveableHudElement
{
    public PlayerPreviewElement() {
        super("Player Preview Element");
    }
    
    @Override
    public void render(final int a1, final int a2, final float a3) {
        /*SL:24*/this.getSize().x = 25;
        /*SL:25*/this.getSize().y = 25;
        /*SL:27*/super.render(a1, a2, a3);
        /*SL:28*/if (!this.shouldRender()) {
            return;
        }
        final EntityPlayer v1 = (EntityPlayer)PlayerPreviewElement.mc.field_71439_g;
        /*SL:33*/GlStateManager.func_179094_E();
        /*SL:34*/GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
        /*SL:35*/RenderHelper.func_74519_b();
        /*SL:36*/GlStateManager.func_179141_d();
        /*SL:37*/GlStateManager.func_179103_j(7424);
        /*SL:38*/GlStateManager.func_179141_d();
        /*SL:39*/GlStateManager.func_179126_j();
        /*SL:40*/GlStateManager.func_179114_b(0.0f, 0.0f, 5.0f, 0.0f);
        /*SL:42*/GlStateManager.func_179142_g();
        /*SL:43*/GlStateManager.func_179094_E();
        /*SL:44*/GlStateManager.func_179109_b(this.getPos().x + this.getSize().x, this.getPos().y + this.getSize().y, 50.0f);
        /*SL:45*/GlStateManager.func_179152_a(-50.0f, 50.0f, 50.0f);
        /*SL:46*/GlStateManager.func_179114_b(180.0f, 0.0f, 0.0f, 1.0f);
        /*SL:47*/GlStateManager.func_179114_b(135.0f, 0.0f, 1.0f, 0.0f);
        /*SL:48*/RenderHelper.func_74519_b();
        /*SL:49*/GlStateManager.func_179114_b(-135.0f, 0.0f, 1.0f, 0.0f);
        /*SL:50*/GlStateManager.func_179114_b(-(float)Math.atan(this.getPos().y / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        /*SL:51*/GlStateManager.func_179109_b(0.0f, 0.0f, 0.0f);
        final RenderManager v2 = /*EL:52*/Minecraft.func_71410_x().func_175598_ae();
        /*SL:53*/v2.func_178631_a(180.0f);
        /*SL:54*/v2.func_178633_a(false);
        /*SL:55*/v2.func_188391_a((Entity)v1, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        /*SL:56*/v2.func_178633_a(true);
        /*SL:57*/GlStateManager.func_179121_F();
        /*SL:58*/RenderHelper.func_74518_a();
        /*SL:59*/GlStateManager.func_179101_C();
        /*SL:60*/GlStateManager.func_179138_g(OpenGlHelper.field_77476_b);
        /*SL:61*/GlStateManager.func_179090_x();
        /*SL:62*/GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
        /*SL:65*/GlStateManager.func_179143_c(515);
        /*SL:66*/GlStateManager.func_179117_G();
        /*SL:67*/GlStateManager.func_179097_i();
        /*SL:68*/GlStateManager.func_179121_F();
    }
}
