package com.ares.hack.hacks.player;

import net.minecraft.entity.player.InventoryPlayer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import com.ares.utils.chat.ChatUtils;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Hotbar Replenish", description = "Replenish items in your hotbar when they are used", category = EnumCategory.PLAYER)
public class HotbarReplenish extends BaseHack
{
    private Setting<Integer> cooldownSetting;
    private Setting<Integer> minStackSize;
    private int cooldown;
    
    public HotbarReplenish() {
        this.cooldownSetting = new IntegerSetting("Cooldown in ticks", this, 100, 0, 200);
        this.minStackSize = new IntegerSetting("Min Stack Size (percent)", this, 20, 1, 99);
        this.cooldown = 0;
    }
    
    public void onEnabled() {
        /*SL:24*/this.setEnabled(false);
        /*SL:25*/ChatUtils.printMessage("Still in development...", "red");
    }
    
    public void onLogic() {
        /*SL:31*/--this.cooldown;
        /*SL:33*/if (this.cooldown <= 0) {
            final List<ItemStack> hotbar = getHotbar();
            /*SL:38*/for (final ItemStack v1 : hotbar) {
                /*SL:40*/if (v1 != null && getStackSizePercent(/*EL:43*/v1) >= this.minStackSize.getValue()) {
                    continue;
                }
            }
            /*SL:50*/this.cooldown = this.cooldownSetting.getValue();
        }
    }
    
    private static int getStackSizePercent(final ItemStack a1) {
        /*SL:56*/return (int)Math.ceil(a1.func_190916_E() * 100.0f / a1.func_77976_d());
    }
    
    private static List<ItemStack> getHotbar() {
        /*SL:61*/return (List<ItemStack>)HotbarReplenish.mc.field_71439_g.field_71071_by.field_70462_a.subList(0, InventoryPlayer.func_70451_h());
    }
}
