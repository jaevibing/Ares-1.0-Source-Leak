package com.ares.hack.hacks.combat;

import net.minecraft.util.math.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import com.ares.utils.chat.ChatUtils;
import com.ares.utils.WorldUtils;
import net.minecraft.init.Items;
import com.ares.hack.hacks.HackManager;
import com.ares.hack.hacks.player.AntiBedTrap;
import com.ares.hack.settings.settings.number.FloatSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto Bed Bomb", description = "Automatically suicide bomb with beds", category = EnumCategory.COMBAT)
public class AutoBedBomb extends BaseHack
{
    private final Setting<Float> range;
    
    public AutoBedBomb() {
        this.range = new FloatSetting("Range", this, 5.0f, 1.0f, 10.0f);
    }
    
    public void onEnabled() {
        /*SL:27*/if (AutoBedBomb.mc.field_71476_x == null || AutoBedBomb.mc.field_71476_x.field_178784_b == null) {
            return;
        }
        final BaseHack v1 = /*EL:29*/HackManager.getHack(AntiBedTrap.class);
        boolean v2 = /*EL:31*/false;
        /*SL:32*/if (v1 != null) {
            /*SL:34*/v2 = v1.getEnabled();
            /*SL:35*/v1.setEnabled(false);
        }
        /*SL:38*/this.run();
        /*SL:39*/this.setEnabled(false);
        /*SL:41*/if (v1 != null) {
            /*SL:43*/v1.setEnabled(v2);
        }
    }
    
    private boolean run() {
        BlockPos v1 = /*EL:49*/null;
        /*SL:51*/if (AutoBedBomb.mc.field_71476_x == null || AutoBedBomb.mc.field_71476_x.field_178784_b == null) {
            /*SL:53*/return false;
        }
        /*SL:57*/v1 = AutoBedBomb.mc.field_71476_x.func_178782_a().func_177972_a(AutoBedBomb.mc.field_71476_x.field_178784_b);
        final int v2 = /*EL:60*/WorldUtils.findItem(Items.field_151104_aV);
        /*SL:62*/if (v2 == -1) {
            /*SL:64*/ChatUtils.printMessage("A bed was not found in your hotbar!", "red");
            /*SL:65*/this.setEnabled(false);
            /*SL:66*/return false;
        }
        AutoBedBomb.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:69*/v2;
        /*SL:71*/WorldUtils.placeBlockMainHand(v1);
        AutoBedBomb.mc.field_71439_g.field_71174_a.func_147297_a(/*EL:73*/(Packet)new CPacketPlayerTryUseItemOnBlock(v1, EnumFacing.UP, EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
        /*SL:77*/return true;
    }
}
