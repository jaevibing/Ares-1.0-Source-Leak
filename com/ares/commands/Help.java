package com.ares.commands;

import java.util.Iterator;
import com.ares.utils.chat.ChatUtils;

public class Help extends CommandBase
{
    public Help() {
        super(new String[] { "help", "commands" });
    }
    
    @Override
    public boolean execute(final String[] v2) {
        final StringBuilder v3 = /*EL:29*/new StringBuilder();
        /*SL:30*/for (final CommandBase a1 : CommandBase.objects) {
            /*SL:32*/v3.append(a1.aliases.get(0)).append(", ");
        }
        /*SL:35*/ChatUtils.printMessage("Commands:\n" + v3.toString());
        /*SL:37*/return true;
    }
    
    @Override
    public String getSyntax() {
        /*SL:43*/return "-help";
    }
}
