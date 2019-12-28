package com.ares.hack.hacks.ui;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import java.text.DecimalFormat;
import com.ares.utils.TimeUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.packet.PacketRecieved;
import java.time.Instant;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "LagAlert", description = "Get notified when the server isnt responding", category = EnumCategory.UI)
public class ServerNotResponding extends BaseHack
{
    private Instant lastRecieved;
    
    public ServerNotResponding() {
        this.lastRecieved = Instant.EPOCH;
    }
    
    @SubscribeEvent
    public void onPacketRecieved(final PacketRecieved a1) {
        /*SL:22*/this.lastRecieved = Instant.now();
    }
    
    public void onRender2d() {
        /*SL:28*/if (this.getEnabled()) {
            final long milliSecondsPassed = /*EL:30*/TimeUtils.getMilliSecondsPassed(this.lastRecieved, Instant.now());
            /*SL:32*/if (milliSecondsPassed >= 1000L) {
                String v1 = /*EL:34*/"§7Server has not responded for §r";
                /*SL:35*/v1 += new DecimalFormat("0.0").format(milliSecondsPassed / 1000L);
                /*SL:36*/v1 += "s";
                final ScaledResolution v2 = /*EL:38*/new ScaledResolution(ServerNotResponding.mc);
                final int v3 = /*EL:39*/v2.func_78326_a() / 2;
                ServerNotResponding.mc.field_71466_p.func_78276_b(/*EL:41*/v1, v3 - ServerNotResponding.mc.field_71466_p.func_78256_a(v1), 4, Color.WHITE.getRGB());
            }
        }
    }
}
