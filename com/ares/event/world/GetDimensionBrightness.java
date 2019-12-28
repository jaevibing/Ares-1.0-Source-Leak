package com.ares.event.world;

import com.ares.event.AresEvent;

public class GetDimensionBrightness extends AresEvent
{
    public float initial;
    
    public GetDimensionBrightness(final float a1) {
        this.initial = a1;
    }
}
