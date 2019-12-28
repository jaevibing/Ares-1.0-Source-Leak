package com.ares.event.entity;

import com.ares.event.AresEvent;

public class GetWaterSlowdown extends AresEvent
{
    public float initialVal;
    
    public GetWaterSlowdown(final float a1) {
        this.initialVal = a1;
    }
}
