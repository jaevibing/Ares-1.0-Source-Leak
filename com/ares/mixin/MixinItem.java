package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Item.class })
public class MixinItem
{
    @Inject(method = { "Lnet/minecraft/item/Item;setDamage(Lnet/minecraft/item/ItemStack;I)V" }, at = { @At("RETURN") }, remap = false)
    private void setDamageWrap(final ItemStack a1, final int a2, final CallbackInfo a3) {
        try {
            /*SL:19*/ObfuscationReflectionHelper.setPrivateValue((Class)ItemStack.class, (Object)a1, (Object)a2, new String[] { "itemDamage", "field_77991_e" });
        }
        catch (Exception ex) {}
    }
}
