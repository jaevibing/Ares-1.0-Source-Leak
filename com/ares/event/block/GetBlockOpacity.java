package com.ares.event.block;

import javax.annotation.Nullable;
import com.ares.event.AresEvent;

public class GetBlockOpacity extends AresEvent
{
    @Nullable
    public Float alphaMultiplier;
    
    public GetBlockOpacity(@Nullable final Float a1) {
        this.alphaMultiplier = a1;
    }
}
