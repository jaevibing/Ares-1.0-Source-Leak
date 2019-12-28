package com.ares.commands;

import java.util.Iterator;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;

public class ViewInventory extends CommandBase
{
    public ViewInventory() {
        super(new String[] { "viewinv", "inventory", "inventoryview" });
    }
    
    @Override
    public boolean execute(final String[] v2) {
        /*SL:17*/if (v2.length < 1) {
            /*SL:19*/this.mc.func_147108_a((GuiScreen)new GuiInventory((EntityPlayer)this.mc.field_71439_g));
            /*SL:20*/return true;
        }
        /*SL:23*/for (final EntityPlayer a1 : this.mc.field_71441_e.field_73010_i) {
            /*SL:25*/if (a1.getDisplayNameString().equalsIgnoreCase(v2[0])) {
                /*SL:27*/this.mc.func_147108_a((GuiScreen)new GuiInventory(a1));
                /*SL:28*/return true;
            }
        }
        /*SL:32*/ChatUtils.printMessage("Could not find player " + v2[0]);
        /*SL:33*/return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:39*/return "-viewinv FitMC";
    }
}
