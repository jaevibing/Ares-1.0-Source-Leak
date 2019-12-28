package com.ares.event.client.ares;

import com.ares.hack.hacks.BaseHack;
import com.ares.event.AresEvent;

public class HackEnabled extends AresEvent
{
    public BaseHack hack;
    
    public HackEnabled(final BaseHack a1) {
        this.hack = a1;
    }
}
