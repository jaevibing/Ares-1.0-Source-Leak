package com.ares.event.world;

import javax.annotation.Nullable;
import com.ares.event.AresEvent;

public class GetLightmapCoords extends AresEvent
{
    @Nullable
    public Integer lightmapCoords;
    
    public GetLightmapCoords(@Nullable final Integer a1) {
        this.lightmapCoords = a1;
    }
}
