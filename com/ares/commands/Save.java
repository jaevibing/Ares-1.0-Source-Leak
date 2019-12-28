package com.ares.commands;

import com.ares.utils.chat.ChatUtils;
import com.ares.config.ConfigSaveManager;
import java.io.File;

public class Save extends CommandBase
{
    public Save() {
        super("save");
    }
    
    @Override
    public boolean execute(final String[] v0) {
        try {
            final String v;
            /*SL:18*/if (v0.length <= 0 || v0[0].isEmpty()) {
                final String a1 = /*EL:19*/"Ares/config.json";
            }
            else {
                /*SL:21*/v = "Ares/config-" + v0[0].toLowerCase() + ".json";
            }
            /*SL:23*/ConfigSaveManager.save(new File(v));
            /*SL:24*/ChatUtils.printMessage("Saved " + v + " config", "green");
        }
        catch (Exception v2) {
            /*SL:28*/v2.printStackTrace();
            /*SL:29*/ChatUtils.printMessage("Error when reading config: " + v2.getMessage(), "red");
        }
        /*SL:31*/return true;
    }
    
    @Override
    public String getSyntax() {
        /*SL:37*/return "-save <save name>";
    }
}
