package com.ares.commands;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.entity.Entity;

public class Spectate extends CommandBase
{
    public Spectate() {
        super(new String[] { "spectate", "view", "watch", "possess" });
    }
    
    @Override
    public boolean execute(final String[] v0) {
        try {
            /*SL:20*/if (v0[0].equalsIgnoreCase("off") || v0[0].equalsIgnoreCase("self")) {
                /*SL:22*/this.mc.func_175607_a((Entity)this.mc.field_71439_g);
                /*SL:23*/ChatUtils.printMessage("Now viewing from own perspective", "green");
                /*SL:24*/return true;
            }
            /*SL:28*/for (final EntityPlayer a1 : this.mc.field_71441_e.field_73010_i) {
                /*SL:30*/if (a1.getDisplayNameString().equalsIgnoreCase(v0[0])) {
                    /*SL:32*/this.mc.func_175607_a((Entity)a1);
                    /*SL:33*/ChatUtils.printMessage("Now viewing from perspective of '" + a1.getDisplayNameString() + "'", "green");
                    /*SL:34*/return true;
                }
            }
            /*SL:37*/ChatUtils.printMessage("Couldnt find player '" + v0[0] + "'");
        }
        catch (Exception v) {
            /*SL:42*/ChatUtils.printMessage("Error: " + v.getMessage(), "red");
            /*SL:43*/v.printStackTrace();
        }
        /*SL:45*/return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:51*/return "-spectate <playername/self>";
    }
}
