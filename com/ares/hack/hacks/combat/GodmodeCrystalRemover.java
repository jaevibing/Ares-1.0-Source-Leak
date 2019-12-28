package com.ares.hack.hacks.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import com.ares.event.packet.PacketRecieved;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Godmode Crystal Remover", description = "fixes crystals not removing when in god mode", category = EnumCategory.COMBAT)
public class GodmodeCrystalRemover extends BaseHack
{
    @SubscribeEvent
    public void onPacketReceived(final PacketRecieved v-2) {
        /*SL:20*/if (this.getEnabled() && v-2.packet instanceof SPacketSoundEffect) {
            final SPacketSoundEffect sPacketSoundEffect = /*EL:22*/(SPacketSoundEffect)v-2.packet;
            /*SL:25*/if (sPacketSoundEffect.func_186977_b() == SoundCategory.BLOCKS && sPacketSoundEffect.func_186978_a() == SoundEvents.field_187539_bB) {
                /*SL:28*/for (final Entity v1 : GodmodeCrystalRemover.mc.field_71441_e.field_72996_f) {
                    /*SL:30*/if (v1 instanceof EntityEnderCrystal) {
                        final double a1 = /*EL:33*/v1.func_70011_f(sPacketSoundEffect.func_149207_d(), sPacketSoundEffect.func_149211_e(), sPacketSoundEffect.func_149210_f());
                        /*SL:35*/if (a1 > 6.0) {
                            continue;
                        }
                        /*SL:38*/v1.func_70106_y();
                    }
                }
            }
        }
    }
}
