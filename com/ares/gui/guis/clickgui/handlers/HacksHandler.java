package com.ares.gui.guis.clickgui.handlers;

import com.ares.gui.guis.clickgui.buttons.SettingButton;
import java.util.Iterator;
import com.ares.gui.guis.clickgui.buttons.HackButton;

public class HacksHandler extends Handler
{
    @Override
    public void onRender() {
        /*SL:28*/for (final HackButton hackButton : HackButton.getAll()) {
            /*SL:30*/if (hackButton.getHack().getEnabled()) {
                hackButton.textColor = "FF0000";
            }
            else {
                /*SL:31*/hackButton.textColor = "FFFFFF";
            }
            /*SL:33*/if (hackButton.rightClickToggled) {
                /*SL:35*/hackButton.getSettingButtons().forEach(a1 -> a1.isVisible = !a1.isVisible);
                /*SL:37*/for (final HackButton v1 : hackButton.getCategoryButton().getHackButtons()) {
                    /*SL:39*/if (v1 != hackButton) {
                        /*SL:41*/v1.getSettingButtons().forEach(a1 -> a1.isVisible = false);
                    }
                }
                /*SL:45*/hackButton.rightClickToggled = false;
            }
        }
    }
}
