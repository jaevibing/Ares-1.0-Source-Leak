package com.ares.hack.hacks.ui.swing;

import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Swing", description = "Popup console", category = EnumCategory.UI)
public class Swing extends BaseHack
{
    public static Setting<Boolean> chat;
    private boolean isClosed;
    private SwingImpl swingImp;
    
    public Swing() {
        this.isClosed = false;
        Swing.chat = new BooleanSetting("Show Chat", this, true);
    }
    
    public void onEnabled() {
        /*SL:24*/if (this.swingImp == null || this.isClosed) {
            /*SL:25*/this.swingImp = new SwingImpl();
        }
        /*SL:27*/this.isClosed = false;
    }
    
    public void onDisabled() {
        /*SL:33*/this.swingImp.close();
        /*SL:34*/this.isClosed = true;
    }
    
    public void onLogic() {
        /*SL:40*/if (this.swingImp != null) {
            /*SL:41*/this.swingImp.onUpdate();
        }
    }
}
