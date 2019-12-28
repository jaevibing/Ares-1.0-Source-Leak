package com.ares.hack.hacks.player;

import net.minecraft.util.math.Vec3i;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import com.ares.utils.render.RenderUtils;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.WorldUtils;
import net.minecraft.item.Item;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.block.BlockAir;
import java.util.Comparator;
import java.util.Collection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.EnumFacing;
import java.util.ArrayList;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.settings.number.IntegerSetting;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Highway Bot", description = "Automatically Build Highways", category = EnumCategory.PLAYER)
public class HighwayBot extends BaseHack
{
    private final Setting<Integer> highwayWidth;
    private final Setting<Integer> highwayHeight;
    private final Setting<Boolean> debug;
    private final Setting<Integer> actionsPerTick;
    private final Setting<Boolean> autoWalk;
    private int[] picks;
    private final List<ActionableBlockPos> blocks;
    private BlockPos inFront;
    private int actionsThisTick;
    
    public HighwayBot() {
        this.highwayWidth = new IntegerSetting("Width", this, 2, 1, 2);
        this.highwayHeight = new IntegerSetting("Height", this, 3, 2, 5);
        this.debug = new BooleanSetting("Debug", this, true);
        this.actionsPerTick = new IntegerSetting("Per Tick", this, 1, 1, 10);
        this.autoWalk = new BooleanSetting("Auto Walk", this, true);
        this.picks = new int[] { 278, 285, 274, 270, 257 };
        this.blocks = new ArrayList<ActionableBlockPos>();
    }
    
    public void onLogic() {
        /*SL:56*/if (!this.getEnabled()) {
            return;
        }
        /*SL:58*/this.doStuff();
        /*SL:60*/if (this.autoWalk.getValue()) {
            final EnumFacing v1 = HighwayBot.mc.field_71439_g.func_174811_aO();
            /*SL:63*/this.inFront = new BlockPos(HighwayBot.mc.field_71439_g.field_70165_t, HighwayBot.mc.field_71439_g.field_70163_u, HighwayBot.mc.field_71439_g.field_70161_v);
            /*SL:65*/if (v1 == EnumFacing.NORTH) {
                /*SL:67*/this.inFront = this.inFront.func_177982_a(0, -1, -1);
            }
            else/*SL:69*/ if (v1 == EnumFacing.SOUTH) {
                /*SL:71*/this.inFront = this.inFront.func_177982_a(0, -1, 1);
            }
            else/*SL:73*/ if (v1 == EnumFacing.WEST) {
                /*SL:75*/this.inFront = this.inFront.func_177982_a(-1, -1, 0);
            }
            else {
                /*SL:79*/this.inFront = this.inFront.func_177982_a(1, -1, 0);
            }
            final IBlockState v2 = HighwayBot.mc.field_71441_e.func_180495_p(/*EL:82*/this.inFront);
            final boolean v3 = /*EL:83*/v2.func_177230_c() instanceof BlockObsidian;
            /*SL:85*/if (this.actionsThisTick <= 0 && v3) {
                /*SL:87*/KeyBinding.func_74510_a(HighwayBot.mc.field_71474_y.field_74351_w.func_151463_i(), true);
            }
            else {
                HighwayBot.mc.field_71439_g.field_70179_y = /*EL:91*/0.0;
                HighwayBot.mc.field_71439_g.field_70159_w = /*EL:92*/0.0;
                /*SL:93*/KeyBinding.func_74510_a(HighwayBot.mc.field_71474_y.field_74351_w.func_151463_i(), false);
            }
        }
    }
    
    private void doStuff() {
        /*SL:100*/this.blocks.clear();
        /*SL:101*/this.blocks.addAll(this.findBlocks());
        /*SL:103*/this.blocks.sort(Comparator.<? super ActionableBlockPos>comparingDouble(a1 -> HighwayBot.mc.field_71439_g.func_174818_b((BlockPos)a1)));
        /*SL:105*/this.actionsThisTick = 0;
        final IBlockState v1;
        final List<ActionableBlockPos> list = /*EL:108*/this.blocks.stream().filter(a1 -> {
            v1 = HighwayBot.mc.field_71441_e.func_180495_p((BlockPos)a1);
            return !v1.func_185904_a().func_76222_j() && !(v1.func_177230_c() instanceof BlockAir) && !(v1.func_177230_c() instanceof BlockObsidian);
        }).<List<ActionableBlockPos>, ?>collect(/*EL:120*/(Collector<? super Object, ?, List<ActionableBlockPos>>)Collectors.<? super Object>toList());
        /*SL:122*/if (list.size() > 0) {
            int field_70461_c = /*EL:124*/-1;
            /*SL:125*/for (final int v2 : this.picks) {
                final int v3 = /*EL:127*/WorldUtils.findItem(Item.func_150899_d(v2));
                /*SL:128*/if (v3 != -1) {
                    /*SL:130*/field_70461_c = v3;
                    /*SL:131*/break;
                }
            }
            /*SL:135*/if (field_70461_c == -1) {
                /*SL:137*/ChatUtils.printMessage("No picaxe in hotbar");
                /*SL:138*/this.setEnabled(false);
                /*SL:139*/return;
            }
            HighwayBot.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:143*/field_70461_c;
            HighwayBot.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:144*/(Packet)new CPacketHeldItemChange(field_70461_c));
        }
        /*SL:147*/for (final ActionableBlockPos actionableBlockPos : list) {
            HighwayBot.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:151*/(Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, (BlockPos)actionableBlockPos, EnumFacing.UP));
            HighwayBot.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:152*/(Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, (BlockPos)actionableBlockPos, EnumFacing.UP));
            /*SL:153*/this.blocks.remove(actionableBlockPos);
            /*SL:155*/++this.actionsThisTick;
            /*SL:156*/if (this.actionsThisTick >= this.actionsPerTick.getValue()) {
                return;
            }
        }
        final IBlockState v4;
        final List<ActionableBlockPos> list2 = /*EL:159*/this.blocks.stream().filter(a1 -> {
            v4 = HighwayBot.mc.field_71441_e.func_180495_p((BlockPos)a1);
            return a1.blockAction == BlockAction.REPLACE && v4.func_185904_a().func_76222_j();
        }).<List<ActionableBlockPos>, ?>collect(/*EL:169*/(Collector<? super Object, ?, List<ActionableBlockPos>>)Collectors.<? super Object>toList());
        /*SL:171*/if (list2.size() > 0) {
            final int block = /*EL:173*/WorldUtils.findBlock(Blocks.field_150343_Z);
            /*SL:175*/if (block == -1) {
                /*SL:177*/ChatUtils.printMessage("No obsidian in hotbar");
                /*SL:178*/this.setEnabled(false);
                /*SL:179*/return;
            }
            HighwayBot.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:183*/block;
            HighwayBot.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:184*/(Packet)new CPacketHeldItemChange(block));
        }
        /*SL:187*/for (final ActionableBlockPos a2 : list2) {
            /*SL:190*/WorldUtils.placeBlockMainHand(a2);
            /*SL:191*/this.blocks.remove(a2);
            /*SL:193*/++this.actionsThisTick;
            /*SL:194*/if (this.actionsThisTick >= this.actionsPerTick.getValue()) {
                return;
            }
        }
    }
    
    public void onRender3d() {
        /*SL:201*/if (!this.getEnabled() || !this.debug.getValue()) {
            return;
        }
        /*SL:203*/RenderUtils.glStart();
        /*SL:205*/GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.2f);
        /*SL:207*/this.blocks.stream().filter(a1 -> a1.blockAction == BlockAction.REPLACE).<Object>map(/*EL:208*/(Function<? super Object, ?>)AxisAlignedBB::new).forEach(/*EL:209*/(Consumer<? super Object>)RenderUtils::drawSolidBox);
        /*SL:212*/GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.2f);
        /*SL:214*/this.blocks.stream().filter(a1 -> a1.blockAction == BlockAction.DESTROY).<Object>map(/*EL:215*/(Function<? super Object, ?>)AxisAlignedBB::new).forEach(/*EL:216*/(Consumer<? super Object>)RenderUtils::drawSolidBox);
        /*SL:220*/if (this.inFront != null) {
            /*SL:222*/GL11.glColor4f(0.0f, 0.0f, 1.0f, 0.5f);
            /*SL:223*/RenderUtils.drawSolidBox(new AxisAlignedBB(this.inFront));
        }
        /*SL:226*/RenderUtils.glEnd();
    }
    
    private List<ActionableBlockPos> findBlocks() {
        final EnumFacing func_174811_aO = HighwayBot.mc.field_71439_g.func_174811_aO();
        final BlockPos blockPos = /*EL:232*/new BlockPos(HighwayBot.mc.field_71439_g.field_70165_t, HighwayBot.mc.field_71439_g.field_70163_u, HighwayBot.mc.field_71439_g.field_70161_v);
        final List<ActionableBlockPos> list = /*EL:234*/new ArrayList<ActionableBlockPos>();
        /*SL:236*/for (int v0 = 1; v0 <= 3; ++v0) {
            /*SL:238*/if (func_174811_aO == EnumFacing.NORTH) {
                /*SL:241*/for (int v = 0; v < this.highwayWidth.getValue(); ++v) {
                    /*SL:243*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v, -1, -v0), BlockAction.REPLACE));
                    /*SL:244*/if (v != 0) {
                        /*SL:245*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v, -1, -v0), BlockAction.REPLACE));
                    }
                }
                /*SL:249*/for (int v = 0; v < this.highwayWidth.getValue(); ++v) {
                    /*SL:251*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v, 0, -v0), BlockAction.DESTROY));
                    /*SL:252*/if (v != 0) {
                        /*SL:253*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v, 0, -v0), BlockAction.DESTROY));
                    }
                }
                /*SL:256*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-this.highwayWidth.getValue(), 0, -v0), BlockAction.REPLACE));
                /*SL:257*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a((int)this.highwayWidth.getValue(), 0, -v0), BlockAction.REPLACE));
                /*SL:260*/for (int v = 1; v < this.highwayHeight.getValue(); ++v) {
                    /*SL:262*/for (int v2 = 0; v2 < this.highwayWidth.getValue(); ++v2) {
                        /*SL:264*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v2, v, -v0), BlockAction.DESTROY));
                        /*SL:265*/if (v2 != 0) {
                            /*SL:266*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v2, v, -v0), BlockAction.DESTROY));
                        }
                    }
                }
            }
            else/*SL:270*/ if (func_174811_aO == EnumFacing.SOUTH) {
                /*SL:273*/for (int v = 0; v < this.highwayWidth.getValue(); ++v) {
                    /*SL:275*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v, -1, v0), BlockAction.REPLACE));
                    /*SL:276*/if (v != 0) {
                        /*SL:277*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v, -1, v0), BlockAction.REPLACE));
                    }
                }
                /*SL:281*/for (int v = 0; v < this.highwayWidth.getValue(); ++v) {
                    /*SL:283*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v, 0, v0), BlockAction.DESTROY));
                    /*SL:284*/if (v != 0) {
                        /*SL:285*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v, 0, v0), BlockAction.DESTROY));
                    }
                }
                /*SL:288*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-this.highwayWidth.getValue(), 0, v0), BlockAction.REPLACE));
                /*SL:289*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a((int)this.highwayWidth.getValue(), 0, v0), BlockAction.REPLACE));
                /*SL:292*/for (int v = 1; v < this.highwayHeight.getValue(); ++v) {
                    /*SL:294*/for (int v2 = 0; v2 < this.highwayWidth.getValue(); ++v2) {
                        /*SL:296*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v2, v, v0), BlockAction.DESTROY));
                        /*SL:297*/if (v2 != 0) {
                            /*SL:298*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v2, v, v0), BlockAction.DESTROY));
                        }
                    }
                }
            }
            else/*SL:302*/ if (func_174811_aO == EnumFacing.WEST) {
                /*SL:305*/for (int v = 0; v < this.highwayWidth.getValue(); ++v) {
                    /*SL:307*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v0, -1, v), BlockAction.REPLACE));
                    /*SL:308*/if (v != 0) {
                        /*SL:309*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v0, -1, -v), BlockAction.REPLACE));
                    }
                }
                /*SL:313*/for (int v = 0; v < this.highwayWidth.getValue(); ++v) {
                    /*SL:315*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v0, 0, v), BlockAction.DESTROY));
                    /*SL:316*/if (v != 0) {
                        /*SL:317*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v0, 0, -v), BlockAction.DESTROY));
                    }
                }
                /*SL:320*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v0, 0, -this.highwayWidth.getValue()), BlockAction.REPLACE));
                /*SL:321*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v0, 0, (int)this.highwayWidth.getValue()), BlockAction.REPLACE));
                /*SL:324*/for (int v = 1; v < this.highwayHeight.getValue(); ++v) {
                    /*SL:326*/for (int v2 = 0; v2 < this.highwayWidth.getValue(); ++v2) {
                        /*SL:328*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v0, v, v2), BlockAction.DESTROY));
                        /*SL:329*/if (v2 != 0) {
                            /*SL:330*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(-v0, v, -v2), BlockAction.DESTROY));
                        }
                    }
                }
            }
            else/*SL:334*/ if (func_174811_aO == EnumFacing.EAST) {
                /*SL:337*/for (int v = 0; v < this.highwayWidth.getValue(); ++v) {
                    /*SL:339*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v0, -1, v), BlockAction.REPLACE));
                    /*SL:340*/if (v != 0) {
                        /*SL:341*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v0, -1, -v), BlockAction.REPLACE));
                    }
                }
                /*SL:345*/for (int v = 0; v < this.highwayWidth.getValue(); ++v) {
                    /*SL:347*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v0, 0, v), BlockAction.DESTROY));
                    /*SL:348*/if (v != 0) {
                        /*SL:349*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v0, 0, -v), BlockAction.DESTROY));
                    }
                }
                /*SL:352*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v0, 0, -this.highwayWidth.getValue()), BlockAction.REPLACE));
                /*SL:353*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v0, 0, (int)this.highwayWidth.getValue()), BlockAction.REPLACE));
                /*SL:356*/for (int v = 1; v < this.highwayHeight.getValue(); ++v) {
                    /*SL:358*/for (int v2 = 0; v2 < this.highwayWidth.getValue(); ++v2) {
                        /*SL:360*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v0, v, v2), BlockAction.DESTROY));
                        /*SL:361*/if (v2 != 0) {
                            /*SL:362*/list.add(new ActionableBlockPos((Vec3i)blockPos.func_177982_a(v0, v, -v2), BlockAction.DESTROY));
                        }
                    }
                }
            }
        }
        /*SL:368*/return list;
    }
}
