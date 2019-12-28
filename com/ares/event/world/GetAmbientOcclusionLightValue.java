package com.ares.event.world;

import com.ares.event.AresEvent;

public class GetAmbientOcclusionLightValue extends AresEvent
{
    public float level;
    
    public GetAmbientOcclusionLightValue(final float a1) {
        this.level = a1;
    }
}
