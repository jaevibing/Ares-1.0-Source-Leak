package com.ares.hack.hacks.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import com.ares.event.packet.PacketRecieved;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Fake Item", description = "Always be holding your first item", category = EnumCategory.PLAYER)
public class FakeItem extends BaseHack
{
    private boolean shouldSend;
    
    @SubscribeEvent
    public void onPacketRecieved(final PacketRecieved v-2) {
        /*SL:24*/if (this.getEnabled()) {
            /*SL:27*/if (v-2.packet instanceof CPacketPlayerTryUseItemOnBlock) {
                /*SL:29*/if (this.shouldSend) {
                    /*SL:31*/this.shouldSend = false;
                    /*SL:32*/return;
                }
                /*SL:35*/v-2.setCanceled(true);
                final CPacketPlayerTryUseItemOnBlock a1 = /*EL:37*/(CPacketPlayerTryUseItemOnBlock)v-2.packet;
                final BlockPos v1 = /*EL:38*/a1.func_187023_a();
                final EnumFacing v2 = /*EL:39*/a1.func_187024_b();
                final EnumHand enumHand = /*EL:40*/a1.func_187022_c();
                final float v3 = /*EL:41*/a1.func_187026_d();
                final float v4 = /*EL:42*/a1.func_187025_e();
                final float v5 = /*EL:43*/a1.func_187020_f();
                swap();
                /*SL:46*/this.shouldSend = true;
                FakeItem.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:47*/(Packet)new CPacketPlayerTryUseItemOnBlock(v1, v2, enumHand, v3, v4, v5));
                swap();
            }
            /*SL:50*/if (v-2.packet instanceof CPacketPlayerTryUseItem) {
                /*SL:52*/if (this.shouldSend) {
                    /*SL:54*/this.shouldSend = false;
                    /*SL:55*/return;
                }
                final CPacketPlayerTryUseItem v6 = /*EL:58*/(CPacketPlayerTryUseItem)v-2.packet;
                final EnumHand enumHand = /*EL:59*/v6.func_187028_a();
                /*SL:61*/v-2.setCanceled(true);
                swap();
                /*SL:64*/this.shouldSend = true;
                FakeItem.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:65*/(Packet)new CPacketPlayerTryUseItem(enumHand));
                swap();
            }
            /*SL:68*/if (v-2.packet instanceof CPacketUseEntity) {
                /*SL:70*/if (this.shouldSend) {
                    /*SL:72*/this.shouldSend = false;
                    /*SL:73*/return;
                }
                final CPacketUseEntity v7 = /*EL:76*/(CPacketUseEntity)v-2.packet;
                /*SL:78*/if (v7.func_149565_c() == CPacketUseEntity.Action.ATTACK) {
                    final Entity v8 = /*EL:80*/v7.func_149564_a((World)FakeItem.mc.field_71441_e);
                    /*SL:82*/if (v8 != null) {
                        /*SL:84*/v-2.setCanceled(true);
                        swap();
                        /*SL:87*/this.shouldSend = true;
                        FakeItem.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:88*/(Packet)new CPacketUseEntity(v8));
                        swap();
                    }
                }
            }
        }
    }
    
    private static void swap() {
        FakeItem.mc.field_71442_b.func_187098_a(FakeItem.mc.field_71439_g.field_71069_bz.field_75152_c, /*EL:101*/9, FakeItem.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)FakeItem.mc.field_71439_g);
    }
}
