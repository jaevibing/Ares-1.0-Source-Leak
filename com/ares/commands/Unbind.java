package com.ares.commands;

import java.util.Iterator;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import com.ares.utils.chat.ChatUtils;

public class Unbind extends CommandBase
{
    public Unbind() {
        super("unbind");
    }
    
    @Override
    public boolean execute(final String[] v-3) {
        /*SL:19*/if (v-3.length < 1) {
            /*SL:21*/ChatUtils.printMessage("Invalid args!");
            /*SL:22*/return false;
        }
        final StringBuilder sb = /*EL:25*/new StringBuilder();
        /*SL:26*/for (final String a1 : v-3) {
            sb.append(a1);
        }
        final String string = /*EL:28*/sb.toString();
        /*SL:30*/for (final BaseHack v1 : HackManager.getAll()) {
            /*SL:32*/if (string.equalsIgnoreCase(v1.name.replace(" ", ""))) {
                /*SL:34*/v1.bind.setValue("NONE");
                /*SL:35*/ChatUtils.printMessage("Bound " + string + " to " + v1.bind.getValue());
                /*SL:36*/return true;
            }
        }
        /*SL:40*/ChatUtils.printMessage("Could not find hack '" + string + "'");
        /*SL:41*/return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:47*/return "-unbind Twerk";
    }
}
