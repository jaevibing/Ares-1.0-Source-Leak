package com.ares.hack.hacks.hud.impl.elements;

import com.ares.utils.player.InventoryUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import com.ares.utils.render.Render2dUtils;
import com.ares.hack.hacks.hud.impl.GuiEditHud;
import java.awt.Color;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "Obby Count", description = "Lists the amount of obsidian in inventory", category = EnumCategory.HUD, defaultIsVisible = false)
public class ObbyCountElement extends MoveableHudElement
{
    private static final Color backgroundColor;
    
    public ObbyCountElement() {
        super("Obby Counter");
    }
    
    @Override
    public void render(final int a1, final int a2, final float a3) {
        /*SL:28*/this.getSize().x = 18;
        /*SL:29*/this.getSize().y = 18;
        /*SL:31*/super.render(a1, a2, a3);
        /*SL:32*/if (!this.shouldRender()) {
            return;
        }
        /*SL:34*/if (!(ObbyCountElement.mc.field_71462_r instanceof GuiEditHud)) {
            /*SL:35*/Render2dUtils.drawRect(this.getPos().x, this.getPos().y, this.getPos().x + this.getSize().x, this.getPos().y + this.getSize().y, ObbyCountElement.backgroundColor.getRGB());
        }
        final ItemStack v1 = /*EL:37*/new ItemStack(Blocks.field_150343_Z);
        /*SL:38*/v1.func_190920_e(InventoryUtils.findItemInInventoryQuantity(v1.func_77973_b(), true));
        /*SL:40*/Render2dUtils.renderItemOnScreen(this.getPos().x, this.getPos().y, v1);
    }
    
    static {
        backgroundColor = new Color(64, 64, 64, 127);
    }
}
