package com.ares.event.client.render;

import java.util.List;
import com.ares.event.AresEvent;

public class RenderTab extends AresEvent
{
    public List players;
    public int size;
    
    public RenderTab(final List a1, final int a2) {
        this.players = a1;
        this.size = a2;
    }
}
