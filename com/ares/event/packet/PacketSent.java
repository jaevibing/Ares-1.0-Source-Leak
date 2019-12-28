package com.ares.event.packet;

import net.minecraft.network.Packet;

public class PacketSent extends PacketEvent
{
    public PacketSent(final Packet<?> a1) {
        super(a1);
    }
}
