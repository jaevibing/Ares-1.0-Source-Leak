package com.ares.event.client.movement;

import com.ares.event.AresEvent;

public class ShouldWalkOffEdge extends AresEvent
{
    public boolean isSneaking;
    
    public ShouldWalkOffEdge(final boolean a1) {
        this.isSneaking = a1;
    }
}
