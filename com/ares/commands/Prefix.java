package com.ares.commands;

import com.ares.utils.chat.ChatUtils;

public class Prefix extends CommandBase
{
    public Prefix() {
        super("prefix");
    }
    
    @Override
    public boolean execute(final String[] a1) {
        /*SL:12*/if (a1.length > 0) {
            Command.commandPrefix = /*EL:14*/a1[0];
            /*SL:15*/ChatUtils.printMessage("Set cmd prefix to " + Command.commandPrefix, "red");
            /*SL:16*/return true;
        }
        /*SL:18*/return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:24*/return "Usage: .prefix <new prefix character>";
    }
}
