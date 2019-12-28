package com.ares.event.packet;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import com.ares.event.AresEvent;

@Cancelable
public class PacketEvent extends AresEvent
{
    public Packet packet;
    
    public PacketEvent(final Packet<?> a1) {
        this.packet = a1;
    }
    
    public boolean isCancelable() {
        /*SL:20*/return true;
    }
}
