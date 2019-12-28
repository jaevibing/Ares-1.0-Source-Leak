package com.ares.hack.settings;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class BindHandler
{
    @SubscribeEvent(receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent v-1) {
        /*SL:12*/if (Keyboard.getEventKeyState()) {
            final int v0 = /*EL:14*/Keyboard.getEventKey();
            /*SL:16*/if (v0 != 0) {
                final String v = /*EL:18*/Keyboard.getKeyName(v0);
                /*SL:20*/if (!v.equalsIgnoreCase("NONE")) {
                    /*SL:22*/for (final Setting<String> a1 : EnumSettingType.BIND.getAll()) {
                        /*SL:24*/if (v.equalsIgnoreCase(a1.getValue())) {
                            /*SL:26*/a1.getHack().setEnabled(!a1.getHack().getEnabled());
                        }
                    }
                }
            }
        }
    }
}
