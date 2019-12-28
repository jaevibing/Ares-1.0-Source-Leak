package com.ares.event.client.gui.chat;

import net.minecraft.util.text.ITextComponent;
import com.ares.event.AresEvent;

public class AresChatEvent extends AresEvent
{
    public ITextComponent txt;
    
    public AresChatEvent(final ITextComponent a1) {
        this.txt = a1;
    }
}
