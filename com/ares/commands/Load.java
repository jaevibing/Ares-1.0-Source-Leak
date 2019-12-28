package com.ares.commands;

import com.ares.utils.chat.ChatUtils;
import com.ares.config.ConfigReadManager;
import java.io.File;

public class Load extends CommandBase
{
    public Load() {
        super("load");
    }
    
    @Override
    public boolean execute(final String[] v0) {
        /*SL:15*/if (v0.length <= 0) {
            return false;
        }
        try {
            final String v;
            /*SL:21*/if (v0[0].isEmpty()) {
                final String a1 = /*EL:22*/"Ares/config.json";
            }
            else {
                /*SL:24*/v = "Ares/config-" + v0[0].toLowerCase() + ".json";
            }
            /*SL:26*/ConfigReadManager.read(new File(v));
            /*SL:27*/ChatUtils.printMessage("Loaded " + v + " config", "green");
        }
        catch (Exception v2) {
            /*SL:31*/v2.printStackTrace();
            /*SL:32*/ChatUtils.printMessage("Error when reading config: " + v2.getMessage(), "red");
        }
        /*SL:34*/return true;
    }
    
    @Override
    public String getSyntax() {
        /*SL:41*/return "-load <config name>";
    }
}
