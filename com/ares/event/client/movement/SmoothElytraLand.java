package com.ares.event.client.movement;

import com.ares.event.AresEvent;

public class SmoothElytraLand extends AresEvent
{
    public boolean isWorldRemote;
    
    public SmoothElytraLand(final boolean a1) {
        this.isWorldRemote = a1;
    }
}
