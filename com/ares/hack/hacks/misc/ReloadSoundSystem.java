package com.ares.hack.hacks.misc;

import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "ReloadSoundSys", description = "Reloads the sound system", category = EnumCategory.MISC)
public class ReloadSoundSystem extends BaseHack
{
    public void onEnabled() {
        try {
            final SoundManager v1 = /*EL:35*/(SoundManager)ObfuscationReflectionHelper.getPrivateValue((Class)SoundHandler.class, (Object)ReloadSoundSystem.mc.func_147118_V(), new String[] { "sndManager", "field_147694_f" });
            /*SL:36*/v1.func_148596_a();
        }
        catch (Exception v2) {
            System.out.println(/*EL:40*/"Could not restart sound manager: " + v2.toString());
            /*SL:41*/v2.printStackTrace();
            /*SL:42*/ChatUtils.printMessage("Could not restart sound manager: " + v2.toString(), "red");
        }
        /*SL:44*/this.setEnabled(false);
    }
}
