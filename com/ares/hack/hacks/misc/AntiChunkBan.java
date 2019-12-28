package com.ares.hack.hacks.misc;

import com.ares.event.world.RenderWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketChunkData;
import com.ares.event.packet.PacketRecieved;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Anti Chunk Ban", description = "Prevent being chunk banned", category = EnumCategory.MISC)
public class AntiChunkBan extends BaseHack
{
    private Setting<Boolean> noChunkPacket;
    private Setting<Boolean> noRender;
    private Setting<Boolean> selfKill;
    private Setting<Integer> killDelay;
    private int delayPassed;
    
    public AntiChunkBan() {
        this.noChunkPacket = new BooleanSetting("No Chunk Packet", this, true);
        this.noRender = new BooleanSetting("No World Render", this, true);
        this.selfKill = new BooleanSetting("Spam Kill", this, false);
        this.killDelay = new IntegerSetting("Kill Delay", this, 1, 0, 20);
        this.delayPassed = 0;
    }
    
    public void onLogic() {
        /*SL:29*/if (this.getEnabled() && this.selfKill.getValue()) {
            /*SL:31*/--this.delayPassed;
            /*SL:33*/if (this.delayPassed <= 0) {
                /*SL:35*/this.delayPassed = this.killDelay.getValue();
                AntiChunkBan.mc.field_71439_g.func_71165_d(/*EL:36*/"/kill");
            }
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketRecieved a1) {
        /*SL:44*/if (this.getEnabled() && this.noChunkPacket.getValue() && a1.packet instanceof SPacketChunkData) {
            /*SL:46*/a1.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRenderWorld(final RenderWorldEvent a1) {
        /*SL:53*/if (this.getEnabled() && this.noRender.getValue()) {
            /*SL:55*/a1.setCanceled(true);
        }
    }
}
