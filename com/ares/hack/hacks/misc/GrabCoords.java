package com.ares.hack.hacks.misc;

import com.ares.utils.chat.ChatUtils;
import com.ares.utils.Utils;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Grab Coords", description = "Copy coords to clipboard", category = EnumCategory.MISC)
public class GrabCoords extends BaseHack
{
    public void onEnabled() {
        /*SL:38*/Utils.copyToClipboard(Utils.vectorToString(GrabCoords.mc.field_71439_g.func_174791_d(), new boolean[0]));
        /*SL:39*/ChatUtils.printMessage("Copied coords to clipboard", "red");
        /*SL:40*/this.setEnabled(false);
    }
}
