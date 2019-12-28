package com.ares.event.client.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import com.ares.event.AresEvent;

public class GetShulkerContents extends AresEvent
{
    public NonNullList<ItemStack> items;
    
    public GetShulkerContents(final NonNullList<ItemStack> a1) {
        this.items = a1;
    }
}
