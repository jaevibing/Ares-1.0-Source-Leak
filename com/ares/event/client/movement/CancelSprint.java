package com.ares.event.client.movement;

import com.ares.event.AresEvent;

public class CancelSprint extends AresEvent
{
    public boolean shouldCancel;
    
    public CancelSprint(final boolean a1) {
        this.shouldCancel = a1;
    }
}
