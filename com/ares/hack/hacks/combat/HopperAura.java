package com.ares.hack.hacks.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import com.ares.utils.WorldUtils;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.tileentity.TileEntityHopper;
import java.util.List;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.settings.number.DoubleSetting;
import java.util.HashSet;
import com.ares.hack.settings.Setting;
import net.minecraft.util.math.BlockPos;
import java.util.Set;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "HopperAura", description = "Break nearby hoppers", category = EnumCategory.COMBAT)
public class HopperAura extends BaseHack
{
    private Set<BlockPos> hoppersPlaced;
    private int[] picks;
    private Setting<Double> distance;
    private Setting<Boolean> lockRotations;
    private Setting<Boolean> breakOwn;
    
    public HopperAura() {
        this.hoppersPlaced = new HashSet<BlockPos>() {};
        this.picks = new int[] { 278, 285, 274, 270, 257 };
        this.distance = new DoubleSetting("Distance", this, 5.0, 1.0, 10.0);
        this.lockRotations = new BooleanSetting("LockRotations", this, true);
        this.breakOwn = new BooleanSetting("Break Own", this, false);
    }
    
    public void onLogic() {
        /*SL:54*/if (!this.getEnabled()) {
            return;
        }
        final List<TileEntity> list = (List<TileEntity>)HopperAura.mc.field_71441_e.field_147482_g.stream().filter(/*EL:56*/a1 -> a1 instanceof TileEntityHopper).collect(Collectors.<Object>toList());
        /*SL:58*/if (list.size() > 0) {
            /*SL:60*/for (final TileEntity tileEntity : list) {
                final BlockPos func_174877_v = /*EL:62*/tileEntity.func_174877_v();
                /*SL:65*/if (!this.breakOwn.getValue() && this.hoppersPlaced.contains(func_174877_v)) {
                    continue;
                }
                /*SL:67*/if (HopperAura.mc.field_71439_g.func_70011_f((double)func_174877_v.func_177958_n(), (double)func_174877_v.func_177956_o(), (double)func_174877_v.func_177952_p()) > this.distance.getValue()) {
                    continue;
                }
                /*SL:69*/for (final int v0 : this.picks) {
                    final int v = /*EL:71*/WorldUtils.findItem(Item.func_150899_d(v0));
                    /*SL:72*/if (v != -1) {
                        HopperAura.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:74*/v;
                        /*SL:76*/if (this.lockRotations.getValue()) {
                            WorldUtils.lookAtBlock(func_174877_v);
                        }
                        HopperAura.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:78*/(Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, tileEntity.func_174877_v(), EnumFacing.UP));
                        HopperAura.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:79*/(Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, tileEntity.func_174877_v(), EnumFacing.UP));
                        /*SL:80*/return;
                    }
                }
            }
        }
    }
    
    public void onDisabled() {
        /*SL:91*/this.hoppersPlaced.clear();
    }
    
    @SubscribeEvent
    public void onBlockPlace(final PlayerInteractEvent.RightClickBlock a1) {
        /*SL:97*/if (HopperAura.mc.field_71439_g.field_71071_by.func_70301_a(HopperAura.mc.field_71439_g.field_71071_by.field_70461_c).func_77973_b().equals(Item.func_150899_d(154))) {
            /*SL:99*/this.hoppersPlaced.add(HopperAura.mc.field_71476_x.func_178782_a().func_177972_a(HopperAura.mc.field_71476_x.field_178784_b));
        }
    }
}
