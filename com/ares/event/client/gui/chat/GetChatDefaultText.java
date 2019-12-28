package com.ares.event.client.gui.chat;

import com.ares.event.AresEvent;

public class GetChatDefaultText extends AresEvent
{
    public String defaultText;
    public final String lastText;
    
    public GetChatDefaultText(final String a1, final String a2) {
        this.defaultText = a1;
        this.lastText = a2;
    }
}
