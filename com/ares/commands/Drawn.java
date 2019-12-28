package com.ares.commands;

import java.util.Iterator;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import com.ares.utils.chat.ChatUtils;

public class Drawn extends CommandBase
{
    public Drawn() {
        super(new String[] { "drawn", "shown", "visible" });
    }
    
    @Override
    public boolean execute(final String[] v-4) {
        /*SL:21*/if (v-4.length < 1) {
            /*SL:23*/ChatUtils.printMessage("Invalid args!");
            /*SL:24*/return false;
        }
        final StringBuilder sb = /*EL:27*/new StringBuilder();
        /*SL:28*/for (final String a1 : v-4) {
            sb.append(a1);
        }
        final String string = /*EL:30*/sb.toString();
        /*SL:32*/for (final BaseHack v0 : HackManager.getAll()) {
            /*SL:34*/if (string.equalsIgnoreCase(v0.name.replace(" ", ""))) {
                /*SL:36*/v0.isVisible.setValue(!v0.isVisible.getValue());
                String v = /*EL:38*/"not ";
                /*SL:39*/if (v0.isVisible.getValue()) {
                    /*SL:40*/v = "";
                }
                /*SL:42*/ChatUtils.printMessage("Hack '" + string + "' is now " + v + "drawn");
                /*SL:43*/return true;
            }
        }
        /*SL:47*/ChatUtils.printMessage("Could not find hack '" + string + "'");
        /*SL:48*/return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:54*/return "-drawn Twerk";
    }
}
