package com.ares.commands;

import net.minecraft.world.border.WorldBorder;
import com.ares.utils.chat.ChatUtils;

public class GetWorldBorder extends CommandBase
{
    public GetWorldBorder() {
        super(new String[] { "getworldborder", "worldborder", "border" });
    }
    
    @Override
    public boolean execute(final String[] a1) {
        final WorldBorder v1 = /*EL:20*/this.mc.field_71441_e.func_175723_af();
        final double v2 = /*EL:22*/v1.func_177728_d();
        final double v3 = /*EL:23*/v1.func_177733_e();
        final double v4 = /*EL:24*/v1.func_177726_b();
        final double v5 = /*EL:25*/v1.func_177736_c();
        /*SL:27*/ChatUtils.printMessage("World border is at:\nMinX: " + v4 + "\nMinZ: " + v5 + "\nMaxX: " + v2 + "\nMaxZ: " + v3 + "\n", "green");
        /*SL:35*/return true;
    }
    
    @Override
    public String getSyntax() {
        /*SL:41*/return "-worldborder";
    }
}
