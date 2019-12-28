package com.ares.event.block;

import javax.annotation.Nullable;
import com.ares.event.AresEvent;

public class CheckBlockLight extends AresEvent
{
    @Nullable
    public Boolean checkLight;
    
    public CheckBlockLight(@Nullable final Boolean a1) {
        this.checkLight = a1;
    }
}
