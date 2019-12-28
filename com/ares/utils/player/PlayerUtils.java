package com.ares.utils.player;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import com.ares.Globals;

public class PlayerUtils implements Globals
{
    public double getAdjustedBaseMoveSpeed() {
        double n = /*EL:14*/0.2873;
        final PotionEffect v0 = PlayerUtils.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c);
        /*SL:17*/if (PlayerUtils.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) && v0 != null) {
            final int v = /*EL:19*/v0.func_76458_c();
            /*SL:20*/n *= 1.0 + 0.2 * (v + 1);
        }
        /*SL:22*/return n;
    }
    
    public static void disconnectFromWorld() {
        PlayerUtils.mc.field_71441_e.func_72882_A();
        PlayerUtils.mc.func_71403_a(/*EL:31*/(WorldClient)null);
        /*SL:34*/if (PlayerUtils.mc.func_71387_A()) {
            PlayerUtils.mc.func_147108_a(/*EL:36*/(GuiScreen)new GuiMainMenu());
        }
        else/*SL:38*/ if (PlayerUtils.mc.func_181540_al()) {
            final RealmsBridge v1 = /*EL:40*/new RealmsBridge();
            /*SL:41*/v1.switchToRealms((GuiScreen)new GuiMainMenu());
        }
        else {
            PlayerUtils.mc.func_147108_a(/*EL:45*/(GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()));
        }
    }
}
