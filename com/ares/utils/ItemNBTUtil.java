package com.ares.utils;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;

public final class ItemNBTUtil
{
    public static boolean detectNBT(final ItemStack a1) {
        /*SL:37*/return a1.func_77942_o();
    }
    
    public static void initNBT(final ItemStack a1) {
        /*SL:44*/if (!detectNBT(a1)) {
            injectNBT(/*EL:45*/a1, new NBTTagCompound());
        }
    }
    
    public static void injectNBT(final ItemStack a1, final NBTTagCompound a2) {
        /*SL:51*/a1.func_77982_d(a2);
    }
    
    public static NBTTagCompound getNBT(final ItemStack a1) {
        initNBT(/*EL:57*/a1);
        /*SL:58*/return a1.func_77978_p();
    }
    
    public static void setBoolean(final ItemStack a1, final String a2, final boolean a3) {
        getNBT(/*EL:64*/a1).func_74757_a(a2, a3);
    }
    
    public static void setByte(final ItemStack a1, final String a2, final byte a3) {
        getNBT(/*EL:68*/a1).func_74774_a(a2, a3);
    }
    
    public static void setShort(final ItemStack a1, final String a2, final short a3) {
        getNBT(/*EL:72*/a1).func_74777_a(a2, a3);
    }
    
    public static void setInt(final ItemStack a1, final String a2, final int a3) {
        getNBT(/*EL:76*/a1).func_74768_a(a2, a3);
    }
    
    public static void setLong(final ItemStack a1, final String a2, final long a3) {
        getNBT(/*EL:80*/a1).func_74772_a(a2, a3);
    }
    
    public static void setFloat(final ItemStack a1, final String a2, final float a3) {
        getNBT(/*EL:84*/a1).func_74776_a(a2, a3);
    }
    
    public static void setDouble(final ItemStack a1, final String a2, final double a3) {
        getNBT(/*EL:88*/a1).func_74780_a(a2, a3);
    }
    
    public static void setCompound(final ItemStack a1, final String a2, final NBTTagCompound a3) {
        /*SL:92*/if (!a2.equalsIgnoreCase("ench")) {
            getNBT(/*EL:93*/a1).func_74782_a(a2, (NBTBase)a3);
        }
    }
    
    public static void setString(final ItemStack a1, final String a2, final String a3) {
        getNBT(/*EL:97*/a1).func_74778_a(a2, a3);
    }
    
    public static void setList(final ItemStack a1, final String a2, final NBTTagList a3) {
        getNBT(/*EL:101*/a1).func_74782_a(a2, (NBTBase)a3);
    }
    
    public static boolean verifyExistence(final ItemStack a1, final String a2) {
        /*SL:108*/return !a1.func_190926_b() && detectNBT(a1) && getNBT(a1).func_74764_b(a2);
    }
    
    @Deprecated
    public static boolean verifyExistance(final ItemStack a1, final String a2) {
        /*SL:113*/return verifyExistence(a1, a2);
    }
    
    public static boolean getBoolean(final ItemStack a1, final String a2, final boolean a3) {
        /*SL:117*/return verifyExistence(a1, a2) ? getNBT(a1).func_74767_n(a2) : a3;
    }
    
    public static byte getByte(final ItemStack a1, final String a2, final byte a3) {
        /*SL:121*/return verifyExistence(a1, a2) ? getNBT(a1).func_74771_c(a2) : a3;
    }
    
    public static short getShort(final ItemStack a1, final String a2, final short a3) {
        /*SL:125*/return verifyExistence(a1, a2) ? getNBT(a1).func_74765_d(a2) : a3;
    }
    
    public static int getInt(final ItemStack a1, final String a2, final int a3) {
        /*SL:129*/return verifyExistence(a1, a2) ? getNBT(a1).func_74762_e(a2) : a3;
    }
    
    public static long getLong(final ItemStack a1, final String a2, final long a3) {
        /*SL:133*/return verifyExistence(a1, a2) ? getNBT(a1).func_74763_f(a2) : a3;
    }
    
    public static float getFloat(final ItemStack a1, final String a2, final float a3) {
        /*SL:137*/return verifyExistence(a1, a2) ? getNBT(a1).func_74760_g(a2) : a3;
    }
    
    public static double getDouble(final ItemStack a1, final String a2, final double a3) {
        /*SL:141*/return verifyExistence(a1, a2) ? getNBT(a1).func_74769_h(a2) : a3;
    }
    
    public static NBTTagCompound getCompound(final ItemStack a1, final String a2, final boolean a3) {
        /*SL:147*/return verifyExistence(a1, a2) ? getNBT(a1).func_74775_l(a2) : (a3 ? null : new NBTTagCompound());
    }
    
    public static String getString(final ItemStack a1, final String a2, final String a3) {
        /*SL:151*/return verifyExistence(a1, a2) ? getNBT(a1).func_74779_i(a2) : a3;
    }
    
    public static NBTTagList getList(final ItemStack a1, final String a2, final int a3, final boolean a4) {
        /*SL:155*/return verifyExistence(a1, a2) ? getNBT(a1).func_150295_c(a2, a3) : (a4 ? null : new NBTTagList());
    }
}
