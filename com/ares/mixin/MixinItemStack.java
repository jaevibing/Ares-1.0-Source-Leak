package com.ares.mixin;

import net.minecraft.client.util.ITooltipFlag;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemStack.class })
public class MixinItemStack
{
    @Shadow
    int field_77991_e;
    int actualDamage;
    
    @Inject(method = { "<init>(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V" }, at = { @At("RETURN") })
    private void postInit(final Item a1, final int a2, final int a3, final NBTTagCompound a4, final CallbackInfo a5) {
        /*SL:27*/this.field_77991_e = a3;
        /*SL:28*/this.actualDamage = a3;
    }
    
    @Redirect(method = { "<init>(Lnet/minecraft/nbt/NBTTagCompound;)V" }, at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I"))
    private int max(final int a1, final int a2) {
        /*SL:35*/return this.actualDamage = a2;
    }
    
    @Redirect(method = { "getTooltip" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItemDamage()I"))
    private int getItemDamage(final ItemStack a1) {
        /*SL:41*/return this.actualDamage;
    }
    
    @Redirect(method = { "getTooltip" }, at = @At(value = "INVOKE", target = "net/minecraft/item/ItemStack.isItemDamaged()Z"))
    private boolean isItemDamaged(final ItemStack a1) {
        /*SL:47*/return true;
    }
    
    @Redirect(method = { "getTooltip" }, at = @At(value = "INVOKE", target = "net/minecraft/client/util/ITooltipFlag.isAdvanced()Z", ordinal = 2))
    private boolean isAdvanced(final ITooltipFlag a1) {
        /*SL:53*/return true;
    }
}
