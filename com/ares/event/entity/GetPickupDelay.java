package com.ares.event.entity;

import com.ares.event.AresEvent;

public class GetPickupDelay extends AresEvent
{
    public int delay;
    
    public GetPickupDelay(final int a1) {
        this.delay = a1;
    }
}
