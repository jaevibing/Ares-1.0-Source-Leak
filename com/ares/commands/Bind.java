package com.ares.commands;

import java.util.Iterator;
import java.util.Arrays;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import com.ares.utils.chat.ChatUtils;

public class Bind extends CommandBase
{
    public Bind() {
        super(new String[] { "bind", "keybind" });
    }
    
    @Override
    public boolean execute(final String[] v-2) {
        /*SL:19*/if (v-2.length <= 0) {
            /*SL:21*/ChatUtils.printMessage("Please specify a hack!");
            /*SL:22*/return false;
        }
        StringBuilder sb = /*EL:25*/new StringBuilder();
        /*SL:26*/for (final String a1 : v-2) {
            sb.append(a1);
        }
        /*SL:28*/for (final BaseHack v1 : HackManager.getAll()) {
            /*SL:30*/if (sb.toString().equalsIgnoreCase(v1.name.replace(" ", ""))) {
                /*SL:32*/ChatUtils.printMessage(v1.name + ": " + v1.bind.getValue());
                /*SL:33*/return true;
            }
        }
        /*SL:37*/sb = new StringBuilder();
        /*SL:38*/for (final String v2 : Arrays.<String>copyOf(v-2, v-2.length - 1)) {
            sb.append(v2);
        }
        /*SL:40*/for (final BaseHack v1 : HackManager.getAll()) {
            /*SL:42*/if (sb.toString().equalsIgnoreCase(v1.name.replace(" ", ""))) {
                /*SL:44*/v1.bind.setValue(v-2[v-2.length - 1].toUpperCase());
                /*SL:45*/ChatUtils.printMessage("Set keybind of hack '" + v1.name + "' to '" + v1.bind.getValue() + "'");
                /*SL:46*/return true;
            }
        }
        /*SL:50*/ChatUtils.printMessage(sb.toString() + " not found!", "red");
        /*SL:51*/return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:57*/return "-t <hackname>";
    }
}
