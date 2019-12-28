package com.ares.hack.hacks.player;

import net.minecraft.util.EnumFacing;
import com.ares.Globals;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.WorldUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.ServerTick;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Item;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto Wither", description = "2 tick withers", category = EnumCategory.PLAYER)
public class AutoWither extends BaseHack
{
    private Item SOUL_SAND;
    private Item WITHER_SKULL;
    private BlockPos basePos;
    private int stage;
    
    public AutoWither() {
        this.SOUL_SAND = new ItemStack(Blocks.field_150425_aM).func_77973_b();
        this.WITHER_SKULL = new ItemStack((Block)Blocks.field_150465_bP).func_77973_b();
        this.basePos = new BlockPos(0, 0, 0);
        this.stage = -1;
    }
    
    public void onEnabled() {
        /*SL:28*/++this.stage;
        /*SL:31*/this.stage0();
        /*SL:32*/++this.stage;
    }
    
    @SubscribeEvent
    public void onServerTick(final ServerTick a1) {
        /*SL:38*/if (!this.getEnabled() || this.stage > 1) {
            /*SL:40*/this.stage = -1;
            /*SL:41*/this.setEnabled(false);
            /*SL:42*/return;
        }
        /*SL:45*/if (this.stage == 0) {
            /*SL:47*/this.stage0();
        }
        /*SL:49*/if (this.stage == 1) {
            /*SL:51*/this.stage1();
            /*SL:52*/this.stage = -1;
            /*SL:53*/this.setEnabled(false);
            /*SL:54*/return;
        }
        /*SL:56*/++this.stage;
    }
    
    private boolean stage0() {
        /*SL:61*/if (AutoWither.mc.field_71476_x == null || AutoWither.mc.field_71476_x.field_178784_b == null) {
            /*SL:62*/this.basePos = AutoWither.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 0);
        }
        else {
            /*SL:64*/this.basePos = AutoWither.mc.field_71476_x.func_178782_a().func_177972_a(AutoWither.mc.field_71476_x.field_178784_b);
        }
        final int item = /*EL:66*/WorldUtils.findItem(this.SOUL_SAND);
        final int v0 = /*EL:67*/this.getSkull();
        /*SL:69*/if (v0 == -1 || item == -1) {
            final String v = /*EL:71*/(v0 == -1) ? "Wither Skull" : "Soul Sand";
            /*SL:72*/ChatUtils.printMessage(v + " was not found in your hotbar!", "red");
            /*SL:73*/this.setEnabled(false);
            /*SL:74*/return false;
        }
        AutoWither.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:77*/WorldUtils.findItem(this.SOUL_SAND);
        /*SL:79*/WorldUtils.placeBlockMainHand(this.basePos);
        /*SL:80*/if (isX()) {
            /*SL:82*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(0, 1, 0));
            /*SL:83*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(1, 1, 0));
            /*SL:84*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(-1, 1, 0));
        }
        else {
            /*SL:88*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(0, 1, 0));
            /*SL:89*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(0, 1, 1));
            /*SL:90*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(0, 1, -1));
        }
        /*SL:92*/return true;
    }
    
    private boolean stage1() {
        final int v1 = /*EL:97*/this.getSkull();
        /*SL:99*/if (v1 != -1) {
            AutoWither.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:101*/v1;
            /*SL:103*/if (isX()) {
                /*SL:105*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(0, 2, 0));
                /*SL:106*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(1, 2, 0));
                /*SL:107*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(-1, 2, 0));
            }
            else {
                /*SL:111*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(0, 2, 0));
                /*SL:112*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(0, 2, 1));
                /*SL:113*/WorldUtils.placeBlockMainHand(this.basePos.func_177982_a(0, 2, -1));
            }
            /*SL:116*/return true;
        }
        /*SL:118*/return false;
    }
    
    private int getSkull() {
        /*SL:123*/for (int v0 = 0; v0 < 9; ++v0) {
            final ItemStack v = Globals.mc.field_71439_g.field_71071_by.func_70301_a(/*EL:125*/v0);
            /*SL:129*/if (v.func_77973_b().func_77653_i(v).equals("Wither Skeleton Skull")) {
                /*SL:131*/return v0;
            }
        }
        /*SL:134*/return -1;
    }
    
    public static boolean isX() {
        final EnumFacing v1 = AutoWither.mc.field_71439_g.func_174811_aO();
        /*SL:140*/return v1 != EnumFacing.EAST && v1 != EnumFacing.WEST;
    }
}
