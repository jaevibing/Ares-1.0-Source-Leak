package com.ares.commands;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.math.BlockPos;

public class NomadBase extends CommandBase
{
    boolean isBuilding;
    int tickDelay;
    boolean delay;
    int tickCount;
    BlockPos[] base;
    
    public NomadBase() {
        super(new String[] { "nomadbase", "fitbase", "autonomadbase" });
        this.isBuilding = false;
        this.tickDelay = 0;
        this.delay = false;
        this.tickCount = 0;
    }
    
    @Override
    public boolean execute(final String[] a1) {
        /*SL:26*/if (a1.length == 0) {
            /*SL:28*/this.tickCount = 0;
            /*SL:29*/this.isBuilding = true;
        }
        /*SL:31*/if ((a1.length > 1 && a1[0].equalsIgnoreCase("delay")) || a1[0].equalsIgnoreCase("setdelay")) {
            /*SL:33*/this.tickDelay = Integer.valueOf(a1[1]);
            /*SL:34*/if (this.tickDelay == 0) {
                /*SL:36*/this.delay = false;
            }
            else {
                /*SL:39*/this.delay = true;
            }
        }
        /*SL:42*/return true;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent v-6) {
        /*SL:48*/if (!this.isBuilding) {
            return;
        }
        /*SL:50*/if (this.delay && this.tickCount % this.tickDelay != 0) {
            /*SL:52*/++this.tickCount;
            /*SL:53*/return;
        }
        final BlockPos[] array = /*EL:56*/{ /*EL:57*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, -1, -1)), /*EL:58*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, -1, 1)), /*EL:59*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, -1, 1)), /*EL:60*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, -1, -1)), /*EL:61*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, -1, -1)), /*EL:62*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, -1, 1)), /*EL:63*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, -1, 1)), /*EL:64*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, -1, -1)), /*EL:66*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, -1, -2)), /*EL:67*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, -1, -2)), /*EL:68*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, -1, -2)), /*EL:70*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, -1, 2)), /*EL:71*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, -1, 2)), /*EL:72*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, -1, 2)), /*EL:74*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, -1, 0)), /*EL:75*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, -1, 1)), /*EL:76*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, -1, -1)), /*EL:78*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, -1, 0)), /*EL:79*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, -1, 1)), /*EL:80*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, -1, -1)) };
        final BlockPos[] array2 = /*EL:84*/{ /*EL:85*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 1, 1)), /*EL:86*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 1, -1)), /*EL:87*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 1, 1)), /*EL:88*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 1, -1)), /*EL:89*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 1, 2)), /*EL:90*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 1, 2)), /*EL:91*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 1, -2)), /*EL:92*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 1, -2)) };
        final BlockPos[] array3 = /*EL:95*/{ /*EL:96*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 2, 1)), /*EL:97*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 2, -1)), /*EL:98*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 2, 1)), /*EL:99*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 2, -1)), /*EL:100*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 2, 2)), /*EL:101*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 2, 2)), /*EL:102*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 2, -2)), /*EL:103*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 2, -2)) };
        final BlockPos[] array4 = /*EL:106*/{ /*EL:107*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 2, 0)), /*EL:108*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 2, 0)), /*EL:109*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 2, 2)), /*EL:110*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 2, -2)) };
        final int n = /*EL:113*/MathHelper.func_76128_c(this.mc.field_71439_g.field_70177_z * 4.0f / 360.0f + 0.5) & 0x3;
        BlockPos[] v1;
        BlockPos[] v2 = null;
        /*SL:117*/if (n == 0) {
            final BlockPos[] a1 = /*EL:120*/{ /*EL:122*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 0)), /*EL:123*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 1)), /*EL:124*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, -1)), /*EL:125*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, 0)), /*EL:126*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, 1)), /*EL:127*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, -1)), /*EL:128*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, 2)), /*EL:129*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 2)), /*EL:130*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 2)), /*EL:132*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, -2)), /*EL:133*/new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, -2)) };
            /*SL:148*/v1 = new BlockPos[] { new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 1)) };
        }
        else/*SL:150*/ if (n == 1) {
            /*SL:166*/v2 = new BlockPos[] { new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, -2)) };
            /*SL:181*/v1 = new BlockPos[] { new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, -1)) };
        }
        else/*SL:183*/ if (n == 2) {
            /*SL:199*/v2 = new BlockPos[] { new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, -2)) };
            /*SL:214*/v1 = new BlockPos[] { new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, -1)) };
        }
        else {
            /*SL:232*/v2 = new BlockPos[] { new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 0, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, 2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 0, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 0, -2)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 0, -2)) };
            /*SL:247*/v1 = new BlockPos[] { new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-2, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(-1, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(0, 3, -1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 0)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, 1)), new BlockPos((Vec3i)this.mc.field_71439_g.func_180425_c().func_177982_a(1, 3, -1)) };
        }
        final int v3 = /*EL:251*/this.mc.field_71439_g.field_71071_by.field_70461_c;
        int v4 = /*EL:252*/-1;
        /*SL:254*/for (int v5 = 0; v5 < 9; ++v5) {
            if (/*EL:256*/this.mc.field_71439_g.field_71071_by.func_70301_a(v5) != ItemStack.field_190927_a && this.mc.field_71439_g.field_71071_by.func_70301_a(v5).func_77973_b() instanceof ItemBlock && Block.func_149634_a(this.mc.field_71439_g.field_71071_by.func_70301_a(v5).func_77973_b()).func_176223_P().func_185913_b()) {
                /*SL:261*/v4 = v5;
                /*SL:262*/break;
            }
        }
        /*SL:265*/if (v4 != -1) {
            /*SL:267*/this.mc.field_71439_g.field_71071_by.field_70461_c = v4;
            /*SL:274*/for (final BlockPos v6 : array) {
                /*SL:276*/if (this.mc.field_71441_e.func_180495_p(v6).func_185904_a().func_76222_j()) {
                    /*SL:278*/WorldUtils.placeBlockMainHand(v6);
                    /*SL:279*/if (this.delay) {
                        /*SL:281*/++this.tickCount;
                        /*SL:282*/return;
                    }
                }
            }
            /*SL:287*/for (final BlockPos v6 : v2) {
                /*SL:289*/if (this.mc.field_71441_e.func_180495_p(v6).func_185904_a().func_76222_j()) {
                    /*SL:291*/WorldUtils.placeBlockMainHand(v6);
                    /*SL:292*/if (this.delay) {
                        /*SL:294*/++this.tickCount;
                        /*SL:295*/return;
                    }
                }
            }
            /*SL:300*/for (final BlockPos v6 : array2) {
                /*SL:302*/if (this.mc.field_71441_e.func_180495_p(v6).func_185904_a().func_76222_j()) {
                    /*SL:304*/WorldUtils.placeBlockMainHand(v6);
                    /*SL:305*/if (this.delay) {
                        /*SL:307*/++this.tickCount;
                        /*SL:308*/return;
                    }
                }
            }
            /*SL:313*/for (final BlockPos v6 : array3) {
                /*SL:315*/if (this.mc.field_71441_e.func_180495_p(v6).func_185904_a().func_76222_j()) {
                    /*SL:317*/WorldUtils.placeBlockMainHand(v6);
                    /*SL:318*/if (this.delay) {
                        /*SL:320*/++this.tickCount;
                        /*SL:321*/return;
                    }
                }
            }
            /*SL:326*/for (final BlockPos v6 : array4) {
                /*SL:328*/if (this.mc.field_71441_e.func_180495_p(v6).func_185904_a().func_76222_j()) {
                    /*SL:330*/WorldUtils.placeBlockMainHand(v6);
                    /*SL:331*/if (this.delay) {
                        /*SL:333*/++this.tickCount;
                        /*SL:334*/return;
                    }
                }
            }
            /*SL:339*/for (final BlockPos v6 : v1) {
                /*SL:341*/if (this.mc.field_71441_e.func_180495_p(v6).func_185904_a().func_76222_j()) {
                    /*SL:343*/WorldUtils.placeBlockMainHand(v6);
                    /*SL:344*/if (this.delay) {
                        /*SL:346*/++this.tickCount;
                        /*SL:347*/return;
                    }
                }
            }
            /*SL:354*/this.mc.field_71439_g.field_71071_by.field_70461_c = v3;
            /*SL:357*/this.isBuilding = false;
            /*SL:358*/return;
        }
        ChatUtils.printMessage("No blocks found in hotbar!", "red");
        this.isBuilding = false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:363*/return "-nomadbase or -nomadbase setdelay <0/1/2/..> (6 is the best)";
    }
}
