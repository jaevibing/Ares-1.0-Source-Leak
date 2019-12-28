package com.ares.event.packet;

import net.minecraft.network.Packet;

public class PacketRecieved extends PacketEvent
{
    public PacketRecieved(final Packet<?> a1) {
        super(a1);
    }
}
