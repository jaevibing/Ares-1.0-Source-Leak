package com.ares.commands;

import java.util.Iterator;
import com.ares.utils.chat.ChatUtils;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;

public class Toggle extends CommandBase
{
    public Toggle() {
        super(new String[] { "toggle", "t" });
    }
    
    @Override
    public boolean execute(final String[] v-2) {
        final StringBuilder sb = /*EL:34*/new StringBuilder();
        /*SL:35*/for (final String a1 : v-2) {
            sb.append(a1);
        }
        /*SL:37*/for (final BaseHack v1 : HackManager.getAll()) {
            /*SL:39*/if (sb.toString().equalsIgnoreCase(v1.name.replace(" ", ""))) {
                /*SL:41*/v1.setEnabled(!v1.getEnabled());
                /*SL:42*/return true;
            }
        }
        /*SL:46*/ChatUtils.printMessage(sb.toString() + " not found!", "red");
        /*SL:47*/return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:53*/return "-t <hackname>";
    }
}
