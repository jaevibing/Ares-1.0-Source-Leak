package com.ares.hack.hacks.player;

import java.util.stream.IntStream;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Random;
import com.ares.event.packet.PacketSent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiOpenEvent;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import net.minecraft.util.text.TextComponentString;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "AutoSign", description = "Automatically place signs with text", category = EnumCategory.PLAYER)
public class AutoSign extends BaseHack
{
    private static final TextComponentString[] emptySignText;
    private String[] text;
    private Setting<Boolean> chunkBan;
    
    public AutoSign() {
        this.text = new String[] { "Ares Client", "Is the best", "2b2t client", "discord/pdMhDwN" };
        this.chunkBan = new BooleanSetting("Try Chunk Ban", this, false);
    }
    
    @SubscribeEvent
    public void onOpenGui(final GuiOpenEvent v0) {
        /*SL:39*/if (this.getEnabled() && /*EL:41*/v0.getGui() instanceof GuiEditSign) {
            final GuiEditSign v = /*EL:43*/(GuiEditSign)v0.getGui();
            final TileEntitySign v2 = /*EL:45*/(TileEntitySign)ObfuscationReflectionHelper.getPrivateValue((Class)GuiScreen.class, (Object)v, new String[] { "tileSign", "field_146848_f" });
            /*SL:46*/if (v2 != null) {
                final BlockPos a1 = /*EL:48*/v2.func_174877_v();
                AutoSign.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:51*/(Packet)new CPacketUpdateSign(a1, (ITextComponent[])AutoSign.emptySignText));
                /*SL:54*/v0.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketSent v-2) {
        /*SL:63*/if (this.getEnabled() && v-2.packet instanceof CPacketUpdateSign) {
            final CPacketUpdateSign cPacketUpdateSign = /*EL:65*/(CPacketUpdateSign)v-2.packet;
            String[] v0 = /*EL:66*/cPacketUpdateSign.func_187017_b();
            /*SL:68*/if (this.chunkBan.getValue()) {
                final String v = getOverflowingLines();
                /*SL:71*/for (int a1 = 0; a1 < 4; ++a1) {
                    /*SL:73*/v0[a1] = v.substring(a1 * 384, (a1 + 1) * 384);
                }
            }
            else {
                /*SL:78*/v0 = this.text;
            }
            /*SL:81*/ObfuscationReflectionHelper.setPrivateValue((Class)CPacketUpdateSign.class, (Object)cPacketUpdateSign, (Object)v0, new String[] { "lines", "field_149590_d" });
        }
    }
    
    private static String getOverflowingLines() {
        final IntStream v1 = /*EL:92*/new Random().ints(128, 1112063).map(a1 -> (a1 < 55296) ? a1 : (a1 + 2048));
        /*SL:93*/return v1.limit(1536L).<Object>mapToObj(a1 -> String.valueOf((char)a1)).<String, ?>collect((Collector<? super Object, ?, String>)Collectors.joining());
    }
    
    static {
        emptySignText = new TextComponentString[] { new TextComponentString(""), new TextComponentString(""), new TextComponentString(""), new TextComponentString("") };
    }
}
