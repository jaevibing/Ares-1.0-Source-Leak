package com.ares.hack.hacks.combat;

import java.util.Iterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import com.ares.utils.data.FriendUtils;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.WorldUtils;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import com.ares.utils.TimeUtils;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.number.DoubleSetting;
import java.time.Instant;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto Trap", description = "Trap nearby players", category = EnumCategory.COMBAT)
public class AutoTrap extends BaseHack
{
    private Setting<Double> range;
    private Setting<Integer> delay;
    private Instant lastPlaced;
    
    public AutoTrap() {
        this.range = new DoubleSetting("Range", this, 8.0, 0.0, 15.0);
        this.delay = new IntegerSetting("Millisecond delay", this, 1000, 100, 1500);
        this.lastPlaced = Instant.EPOCH;
    }
    
    public void onLogic() {
        /*SL:31*/if (!this.getEnabled()) {
            return;
        }
        final Instant now = /*EL:33*/Instant.now();
        /*SL:34*/if (!TimeUtils.haveMilliSecondsPassed(this.lastPlaced, now, new Long(this.delay.getValue()))) {
            /*SL:36*/return;
        }
        final int field_70461_c = AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c;
        final int item = /*EL:41*/WorldUtils.findItem(Item.func_150898_a(Blocks.field_150343_Z));
        /*SL:42*/if (item == -1) {
            /*SL:44*/this.setEnabled(false);
            /*SL:45*/ChatUtils.printMessage("Obsidian was not found in your hotbar!", "red");
            /*SL:46*/return;
        }
        AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:48*/item;
        /*SL:50*/for (final EntityPlayer a4 : AutoTrap.mc.field_71441_e.field_73010_i) {
            /*SL:52*/if (FriendUtils.isFriend(a4)) {
                continue;
            }
            /*SL:54*/if (AutoTrap.mc.field_71439_g.func_70032_d((Entity)a4) > this.range.getValue() || a4 == AutoTrap.mc.field_71439_g || FriendUtils.isFriend(a4)) {
                continue;
            }
            final BlockPos[] array2;
            final BlockPos[] array = /*EL:68*/array2 = new BlockPos[] { WorldUtils.getRelativeBlockPos(a4, 1, 0, 0), WorldUtils.getRelativeBlockPos(a4, 1, 1, 0), WorldUtils.getRelativeBlockPos(a4, 0, 1, 1), WorldUtils.getRelativeBlockPos(a4, -1, 1, 0), WorldUtils.getRelativeBlockPos(a4, 0, 1, -1), WorldUtils.getRelativeBlockPos(a4, 0, 2, -1), WorldUtils.getRelativeBlockPos(a4, 0, 2, 0) };
            for (final BlockPos v1 : array2) {
                /*SL:70*/if (AutoTrap.mc.field_71441_e.func_180495_p(v1).func_185904_a().func_76222_j() && AutoTrap.mc.field_71441_e.func_72839_b(/*EL:72*/(Entity)null, new AxisAlignedBB(v1)).isEmpty()) {
                    /*SL:79*/WorldUtils.placeBlockMainHand(v1);
                    /*SL:80*/this.lastPlaced = now;
                    /*SL:81*/return;
                }
            }
        }
        AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:88*/field_70461_c;
    }
}
