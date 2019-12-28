package com.ares.event.client.gui.chat;

import java.awt.Color;
import com.ares.event.AresEvent;

public class GetChatBackgroundColour extends AresEvent
{
    public Color color;
    
    public GetChatBackgroundColour(final int a1) {
        this(new Color(a1));
    }
    
    public GetChatBackgroundColour(final Color a1) {
        this.color = a1;
    }
}
