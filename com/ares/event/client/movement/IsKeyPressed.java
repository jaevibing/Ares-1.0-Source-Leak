package com.ares.event.client.movement;

import com.ares.event.AresEvent;

public class IsKeyPressed extends AresEvent
{
    public boolean initialValue;
    public boolean actual;
    
    public IsKeyPressed(final boolean a1, final boolean a2) {
        this.initialValue = a1;
        this.actual = a2;
    }
}
