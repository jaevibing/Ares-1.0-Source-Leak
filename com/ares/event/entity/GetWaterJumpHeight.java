package com.ares.event.entity;

import com.ares.event.AresEvent;

public class GetWaterJumpHeight extends AresEvent
{
    public double height;
    
    public GetWaterJumpHeight(final double a1) {
        this.height = a1;
    }
}
