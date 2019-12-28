package com.ares.hack.hacks.ui;

import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import java.awt.Color;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.EnumSetting;
import net.minecraft.client.util.ITooltipFlag;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Item info", description = "Show extra info about the item your holding", category = EnumCategory.UI)
public class ItemInfo extends BaseHack
{
    private final Setting<ITooltipFlag.TooltipFlags> type;
    private final Setting<Integer> x;
    private final Setting<Integer> y;
    
    public ItemInfo() {
        this.type = new EnumSetting<ITooltipFlag.TooltipFlags>("Type", this, ITooltipFlag.TooltipFlags.ADVANCED);
        this.x = new IntegerSetting("x", this, 0, 0, (int)Math.round(ItemInfo.mc.field_71443_c * 1.2));
        this.y = new IntegerSetting("y", this, 0, 0, (int)Math.round(ItemInfo.mc.field_71440_d * 1.2));
    }
    
    public void onRender2d() {
        /*SL:24*/if (this.getEnabled()) {
            final ItemStack func_70448_g = ItemInfo.mc.field_71439_g.field_71071_by.func_70448_g();
            /*SL:29*/if (!func_70448_g.func_190926_b()) {
                final List<String> func_82840_a = /*EL:31*/(List<String>)func_70448_g.func_82840_a((EntityPlayer)ItemInfo.mc.field_71439_g, (ITooltipFlag)this.type.getValue());
                /*SL:33*/if (func_70448_g.func_77978_p() != null) {
                    /*SL:35*/for (final String v1 : func_70448_g.func_77978_p().func_150296_c()) {
                        /*SL:37*/func_82840_a.add(v1 + ":" + func_70448_g.func_77978_p().func_74781_a(v1).toString());
                    }
                }
                int v2 = /*EL:41*/0;
                boolean v3 = /*EL:42*/true;
                /*SL:43*/for (final String v4 : func_82840_a) {
                    /*SL:45*/if (v4.isEmpty()) {
                        continue;
                    }
                    ItemInfo.mc.field_71466_p.func_78276_b(/*EL:47*/v4, (int)this.x.getValue(), this.y.getValue() + v2, v3 ? Color.WHITE.getRGB() : Color.LIGHT_GRAY.getRGB());
                    /*SL:48*/v3 = false;
                    /*SL:49*/v2 += ItemInfo.mc.field_71466_p.field_78288_b + 2;
                }
            }
        }
    }
}
