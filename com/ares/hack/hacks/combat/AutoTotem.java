package com.ares.hack.hacks.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto Totem", description = "Works in containergui", category = EnumCategory.COMBAT)
public class AutoTotem extends BaseHack
{
    private boolean shouldEquip;
    private Setting<Boolean> alwaysHolding;
    private Setting<Integer> maxDamageToNotHold;
    private Setting<Boolean> hotbar;
    private Setting<Integer> hotbarSlot;
    
    public AutoTotem() {
        this.shouldEquip = true;
        this.alwaysHolding = new BooleanSetting("Always Holding", this, true);
        this.maxDamageToNotHold = new IntegerSetting("Min Health to Equip", this, 6, 0, 20);
        this.hotbar = new BooleanSetting("Refill Hotbar Slot", this, false);
        this.hotbarSlot = new IntegerSetting("Hotbar Slot", this, 9, 0, 9);
    }
    
    public void onLogic() {
        /*SL:26*/if (this.getEnabled()) {
            /*SL:28*/if (AutoTotem.mc.field_71439_g.func_110143_aJ() <= this.maxDamageToNotHold.getValue() && !this.alwaysHolding.getValue()) {
                this.shouldEquip = true;
            }
            /*SL:30*/if (this.shouldEquip && AutoTotem.mc.field_71439_g.func_184592_cb().func_190926_b()) {
                final int v1 = /*EL:32*/this.findItemInWholeInv(Items.field_190929_cY);
                /*SL:34*/if (v1 != -1) {
                    AutoTotem.mc.field_71442_b.func_187098_a(/*EL:37*/0, v1, 0, ClickType.PICKUP_ALL, (EntityPlayer)AutoTotem.mc.field_71439_g);
                    AutoTotem.mc.field_71442_b.func_187098_a(/*EL:39*/0, 45, 0, ClickType.PICKUP_ALL, (EntityPlayer)AutoTotem.mc.field_71439_g);
                }
            }
            /*SL:43*/if (this.alwaysHolding.getValue()) {
                this.shouldEquip = true;
            }
            /*SL:44*/if (!this.alwaysHolding.getValue()) {
                this.shouldEquip = false;
            }
            /*SL:46*/if (this.hotbar.getValue()) {
                final ItemStack v2 = AutoTotem.mc.field_71439_g.field_71071_by.func_70301_a(/*EL:48*/(int)this.hotbarSlot.getValue());
                /*SL:49*/if (v2.func_190926_b()) {
                    final int v3 = /*EL:51*/this.findItemInWholeInv(Items.field_190929_cY);
                    AutoTotem.mc.field_71442_b.func_187098_a(/*EL:54*/0, v3, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.field_71439_g);
                    AutoTotem.mc.field_71442_b.func_187098_a(/*EL:56*/0, (int)this.hotbarSlot.getValue(), 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.field_71439_g);
                }
            }
        }
    }
    
    private int findItemInWholeInv(final Item v2) {
        /*SL:64*/for (int a1 = 9; a1 <= 44; ++a1) {
            /*SL:66*/if (AutoTotem.mc.field_71439_g.field_71069_bz.func_75139_a(a1).func_75211_c().func_77973_b() == v2) {
                /*SL:68*/return a1;
            }
        }
        /*SL:71*/return -1;
    }
}
