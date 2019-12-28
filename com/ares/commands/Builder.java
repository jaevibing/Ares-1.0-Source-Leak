package com.ares.commands;

import java.util.HashMap;
import com.ares.utils.WorldUtils;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import com.ares.utils.render.RenderUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Iterator;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.Map;

public class Builder extends CommandBase
{
    private static int MODE;
    private static final int NO_MODE = 0;
    private static final int RECORD_MODE = 1;
    private static final int BUILD_MODE = 2;
    private static final int LOOP_MODE = 3;
    private static Map<String, ArrayList<Vec3d>> configs;
    private static ArrayList<Vec3d> currentConfig;
    private String recordingName;
    private ArrayList<Vec3d> recordedPositions;
    private int blocksThisTick;
    private int delay;
    
    public Builder() {
        super(new String[] { "build", "builder", "br" });
        this.recordedPositions = new ArrayList<Vec3d>();
        this.blocksThisTick = 0;
        this.delay = 6;
    }
    
    @Override
    public String getSyntax() {
        /*SL:41*/return "-builder/br <mode> [arg]";
    }
    
    @Override
    public boolean execute(final String[] v0) {
        /*SL:47*/if (v0.length == 1) {
            /*SL:49*/if (v0[0].equalsIgnoreCase("stop")) {
                /*SL:52*/if (this.recordedPositions.size() > 0) {
                    Builder.configs.put(/*EL:54*/this.recordingName, this.recordedPositions);
                }
                /*SL:57*/this.recordedPositions.clear();
                Builder.MODE = /*EL:59*/0;
                /*SL:60*/return true;
            }
            /*SL:62*/if (v0[0].equalsIgnoreCase("list")) {
                final StringBuilder v = /*EL:65*/new StringBuilder("Available configs: ");
                /*SL:67*/for (final String a1 : Builder.configs.keySet()) {
                    /*SL:69*/v.append(a1);
                    /*SL:70*/v.append(" ");
                }
                /*SL:73*/ChatUtils.printMessage(v.toString(), "red");
                /*SL:75*/return true;
            }
            /*SL:76*/if (Builder.configs.containsKey(v0[0])) {
                Builder.currentConfig = Builder.configs.get(/*EL:79*/v0[0]);
                Builder.MODE = /*EL:81*/2;
                /*SL:82*/return true;
            }
        }
        /*SL:86*/if (v0.length == 2) {
            /*SL:88*/if (v0[0].equalsIgnoreCase("record")) {
                /*SL:91*/this.recordingName = v0[1];
                /*SL:92*/this.recordedPositions.clear();
                Builder.MODE = /*EL:94*/1;
                /*SL:95*/return true;
            }
            /*SL:97*/if (v0[0].equalsIgnoreCase("loop")) {
                /*SL:99*/if (Builder.configs.containsKey(v0[1])) {
                    Builder.currentConfig = Builder.configs.get(/*EL:102*/v0[1]);
                    Builder.MODE = /*EL:104*/3;
                    /*SL:105*/return true;
                }
                /*SL:109*/ChatUtils.printMessage("Config not found! Use 'br list' to see all configs", "red");
                /*SL:110*/return false;
            }
        }
        /*SL:115*/return false;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent a1) {
        /*SL:122*/if (Builder.MODE == 0) {
            /*SL:124*/this.blocksThisTick = 0;
            /*SL:125*/return;
        }
        /*SL:128*/if (Builder.MODE == 1) {
            /*SL:130*/this.recordMode();
        }
        else/*SL:132*/ if (Builder.MODE == 2) {
            /*SL:134*/this.buildMode();
        }
        else/*SL:136*/ if (Builder.MODE == 3) {
            /*SL:138*/this.loopMode();
        }
    }
    
    private void recordMode() {
        /*SL:146*/RenderUtils.glStart(0.0f, 255.0f, 0.0f, 1.0f);
        /*SL:148*/for (final Vec3d v0 : this.recordedPositions) {
            final BlockPos v = /*EL:150*/new BlockPos(this.mc.field_71439_g.func_174791_d().func_178787_e(v0).field_72450_a, this.mc.field_71439_g.func_174791_d().func_178787_e(v0).field_72448_b, this.mc.field_71439_g.func_174791_d().func_178787_e(v0).field_72449_c);
            final AxisAlignedBB v2 = /*EL:152*/RenderUtils.getBoundingBox(v);
            /*SL:154*/RenderUtils.drawOutlinedBox(v2);
        }
        /*SL:157*/RenderUtils.glEnd();
    }
    
    @SubscribeEvent
    public void onPlace(final PlayerInteractEvent.RightClickBlock v2) {
        /*SL:163*/if (Builder.MODE == 1) {
            final BlockPos a1 = /*EL:165*/this.mc.field_71476_x.func_178782_a().func_177972_a(this.mc.field_71476_x.field_178784_b);
            /*SL:167*/this.recordedPositions.add(new Vec3d(a1.func_177958_n() - this.mc.field_71439_g.func_174791_d().field_72450_a, a1.func_177956_o() - this.mc.field_71439_g.func_174791_d().field_72448_b, a1.func_177952_p() - this.mc.field_71439_g.func_174791_d().field_72449_c));
            /*SL:168*/ChatUtils.printMessage("added block" + this.recordedPositions.get(this.recordedPositions.size() - 1).toString());
        }
    }
    
    private void buildMode() {
        /*SL:175*/if (this.blocksThisTick % this.delay != 0) {
            /*SL:177*/++this.blocksThisTick;
            /*SL:178*/return;
        }
        final int field_70461_c = /*EL:181*/this.mc.field_71439_g.field_71071_by.field_70461_c;
        final int blockInHotbar = /*EL:182*/WorldUtils.getBlockInHotbar();
        /*SL:184*/if (blockInHotbar == -1) {
            Builder.MODE = /*EL:186*/0;
            /*SL:187*/ChatUtils.printMessage("Blocks were not found in your hotbar!", "red");
            /*SL:188*/return;
        }
        /*SL:190*/this.mc.field_71439_g.field_71071_by.field_70461_c = blockInHotbar;
        /*SL:192*/for (final BlockPos v1 : this.getPositions()) {
            /*SL:194*/if (this.mc.field_71441_e.func_180495_p(v1).func_185904_a().func_76222_j()) {
                /*SL:196*/WorldUtils.placeBlockMainHand(v1);
                /*SL:197*/ChatUtils.printMessage("place");
                /*SL:198*/++this.blocksThisTick;
                /*SL:199*/return;
            }
        }
        /*SL:203*/this.mc.field_71439_g.field_71071_by.field_70461_c = field_70461_c;
        Builder.MODE = /*EL:205*/0;
    }
    
    private void loopMode() {
        /*SL:211*/if (this.blocksThisTick % this.delay != 0) {
            /*SL:213*/++this.blocksThisTick;
            /*SL:214*/return;
        }
        final int field_70461_c = /*EL:217*/this.mc.field_71439_g.field_71071_by.field_70461_c;
        final int blockInHotbar = /*EL:218*/WorldUtils.getBlockInHotbar();
        /*SL:220*/if (blockInHotbar == -1) {
            Builder.MODE = /*EL:222*/0;
            /*SL:223*/ChatUtils.printMessage("Blocks were not found in your hotbar!", "red");
            /*SL:224*/return;
        }
        /*SL:226*/this.mc.field_71439_g.field_71071_by.field_70461_c = blockInHotbar;
        /*SL:228*/for (final BlockPos v1 : this.getPositions()) {
            /*SL:230*/if (this.mc.field_71441_e.func_180495_p(v1).func_185904_a().func_76222_j()) {
                /*SL:232*/WorldUtils.placeBlockMainHand(v1);
                /*SL:233*/ChatUtils.printMessage("place");
                /*SL:234*/++this.blocksThisTick;
                /*SL:235*/return;
            }
        }
        /*SL:239*/this.mc.field_71439_g.field_71071_by.field_70461_c = field_70461_c;
    }
    
    private ArrayList<BlockPos> getPositions() {
        final ArrayList<BlockPos> list = /*EL:244*/new ArrayList<BlockPos>();
        /*SL:246*/for (final Vec3d v1 : Builder.currentConfig) {
            /*SL:248*/list.add(new BlockPos(this.mc.field_71439_g.func_174791_d().func_178787_e(v1)));
        }
        /*SL:251*/return list;
    }
    
    static {
        Builder.MODE = 0;
        Builder.configs = new HashMap<String, ArrayList<Vec3d>>();
        Builder.currentConfig = new ArrayList<Vec3d>();
    }
}
