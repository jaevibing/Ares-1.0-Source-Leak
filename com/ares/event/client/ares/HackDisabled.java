package com.ares.event.client.ares;

import com.ares.hack.hacks.BaseHack;
import com.ares.event.AresEvent;

public class HackDisabled extends AresEvent
{
    public BaseHack hack;
    
    public HackDisabled(final BaseHack a1) {
        this.hack = a1;
    }
}
