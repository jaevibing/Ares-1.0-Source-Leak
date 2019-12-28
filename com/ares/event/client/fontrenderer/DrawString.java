package com.ares.event.client.fontrenderer;

import com.ares.event.AresEvent;

public class DrawString extends AresEvent
{
    public String text;
    
    public DrawString(final String a1) {
        this.text = a1;
    }
}
