package com.ares.utils.player;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import com.ares.Globals;

public class InventoryUtils implements Globals
{
    public static int findBlockInHotbar(final Block a1) {
        /*SL:18*/return findItemInHotbar(new ItemStack(a1).func_77973_b());
    }
    
    public static int findItemInHotbar(final Item v1) {
        /*SL:23*/for (int a1 = 0; a1 <= 9; ++a1) {
            /*SL:25*/if (InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(a1).func_75211_c().func_77973_b() == v1) {
                /*SL:27*/return a1;
            }
        }
        /*SL:30*/return -1;
    }
    
    public static int findBlockInInventory(final Block a1, final boolean a2) {
        /*SL:35*/return findItemInInventory(new ItemStack(a1).func_77973_b(), a2);
    }
    
    public static int findItemInInventory(final Item a2, final boolean v1) {
        int v2 = /*EL:40*/9;
        /*SL:41*/if (v1) {
            /*SL:42*/v2 = 0;
        }
        /*SL:44*/for (int a3 = v2; a3 <= 44; ++a3) {
            /*SL:46*/if (InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(a3).func_75211_c().func_77973_b() == a2) {
                /*SL:48*/return a3;
            }
        }
        /*SL:51*/return -1;
    }
    
    public static int findBlockInHotbarQuantity(final Block a1) {
        /*SL:56*/return findItemInHotbarQuantity(new ItemStack(a1).func_77973_b());
    }
    
    public static int findItemInHotbarQuantity(final Item a1) {
        /*SL:61*/return findItemInInventoryQuantity(a1, true, true, QuantityType.NUMSTACKS);
    }
    
    public static void findBlockInHotbarQuantity(final Block a1, final QuantityType a2) {
        findItemInInventoryQuantity(/*EL:66*/new ItemStack(a1).func_77973_b(), true, true, a2);
    }
    
    public static void findBlockInInventoryQuantity(final Block a1, final boolean a2) {
        findItemInInventoryQuantity(/*EL:71*/new ItemStack(a1).func_77973_b(), a2);
    }
    
    public static int findItemInInventoryQuantity(final Item a1, final boolean a2) {
        /*SL:76*/return findItemInInventoryQuantity(a1, a2, false, QuantityType.NUMSTACKS);
    }
    
    public static void findBlockInInventoryQuantity(final Block a1, final boolean a2, final QuantityType a3) {
        findItemInInventoryQuantity(/*EL:81*/new ItemStack(a1).func_77973_b(), a2, false, a3);
    }
    
    public static int findItemInInventoryQuantity(final Item a3, final boolean a4, final boolean v1, final QuantityType v2) {
        int v3 = /*EL:86*/0;
        int v4 = /*EL:88*/9;
        /*SL:89*/if (a4) {
            /*SL:90*/v4 = 0;
        }
        int v5 = /*EL:92*/44;
        /*SL:93*/if (v1) {
            /*SL:94*/v5 = 9;
        }
        /*SL:96*/for (int a5 = v4; a5 <= v5; ++a5) {
            final ItemStack a6 = InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(/*EL:98*/a5).func_75211_c();
            /*SL:99*/if (a6.func_77973_b() == a3) {
                /*SL:101*/if (v2 == QuantityType.NUMSTACKS) {
                    /*SL:102*/++v3;
                }
                else {
                    /*SL:104*/v3 += a6.func_190916_E();
                }
            }
        }
        /*SL:107*/return v3;
    }
    
    public enum QuantityType
    {
        NUMSTACKS, 
        NUMITEMS;
    }
}
