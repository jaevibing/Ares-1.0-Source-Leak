package com.ares.event.client.render;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import com.ares.event.AresEvent;

public class RenderItemInFirstPerson extends AresEvent
{
    public ItemRenderer itemRenderer;
    public AbstractClientPlayer player;
    public float partialTicks;
    public float pitch;
    public EnumHand hand;
    public float swingProgress;
    public ItemStack stack;
    public float equipProgress;
    
    public RenderItemInFirstPerson(final ItemRenderer a1, final AbstractClientPlayer a2, final float a3, final float a4, final EnumHand a5, final float a6, final ItemStack a7, final float a8) {
        this.itemRenderer = a1;
        this.player = a2;
        this.partialTicks = a3;
        this.pitch = a4;
        this.hand = a5;
        this.swingProgress = a6;
        this.stack = a7;
        this.equipProgress = a8;
    }
}
