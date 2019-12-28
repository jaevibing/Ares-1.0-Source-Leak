package com.ares.hack.hacks.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.entity.passive.AbstractHorse;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Donkey Drop", description = "Drop all items in donkeys inv", category = EnumCategory.MISC)
public class DonkeyDrop extends BaseHack
{
    public void onLogic() {
        /*SL:17*/if (this.getEnabled() && DonkeyDrop.mc.field_71439_g.func_184187_bx() instanceof AbstractHorse && DonkeyDrop.mc.field_71439_g.field_71070_bA instanceof ContainerHorseInventory) {
            /*SL:19*/for (int v0 = 2; v0 < 17; ++v0) {
                final ItemStack v = (ItemStack)DonkeyDrop.mc.field_71439_g.field_71070_bA.func_75138_a().get(/*EL:21*/v0);
                /*SL:22*/if (!v.func_190926_b() && v.func_77973_b() != Items.field_190931_a) {
                    DonkeyDrop.mc.field_71442_b.func_187098_a(DonkeyDrop.mc.field_71439_g.field_71070_bA.field_75152_c, /*EL:24*/v0, 0, ClickType.PICKUP, (EntityPlayer)DonkeyDrop.mc.field_71439_g);
                    DonkeyDrop.mc.field_71442_b.func_187098_a(DonkeyDrop.mc.field_71439_g.field_71070_bA.field_75152_c, /*EL:25*/-999, 0, ClickType.PICKUP, (EntityPlayer)DonkeyDrop.mc.field_71439_g);
                }
            }
        }
        /*SL:30*/this.setEnabled(false);
    }
}
