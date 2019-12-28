package com.ares.hack.hacks.hud.impl.elements;

import net.minecraft.tileentity.TileEntity;
import com.ares.utils.ColourUtils;
import net.minecraft.tileentity.TileEntityChest;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "Chest Count", description = "Lists number of chests in render distance", category = EnumCategory.HUD, defaultIsVisible = false)
public class ChestCountElement extends MoveableHudElement
{
    public ChestCountElement() {
        super("Chest Count");
    }
    
    @Override
    public void render(final int a1, final int a2, final float a3) {
        /*SL:20*/super.render(a1, a2, a3);
        /*SL:21*/if (!this.shouldRender()) {
            return;
        }
        final long v1 = ChestCountElement.mc.field_71441_e.field_147482_g.stream().filter(/*EL:25*/a1 -> a1 instanceof TileEntityChest).count();
        final String v2 = /*EL:31*/v1 + " chests";
        /*SL:33*/ColourUtils.drawRainbowString(v2, this.getPos().x, this.getPos().y);
        /*SL:35*/this.getSize().x = ChestCountElement.mc.field_71466_p.func_78256_a(v2);
        /*SL:36*/this.getSize().y = ChestCountElement.mc.field_71466_p.field_78288_b;
    }
}
