package com.ares.hack.hacks.player;

import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import com.ares.hack.settings.settings.EnumSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "FastPlace", description = "Place blocks or use items faster", category = EnumCategory.PLAYER)
public class FastPlace extends BaseHack
{
    private Setting<Whitelist> expOnly;
    
    public FastPlace() {
        this.expOnly = new EnumSetting<Whitelist>("Whitelist", this, Whitelist.ALL);
    }
    
    public void onLogic() {
        /*SL:47*/if (!this.getEnabled()) {
            return;
        }
        final Item v1 = FastPlace.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b();
        final boolean v2 = /*EL:51*/v1 instanceof ItemExpBottle;
        final boolean v3 = /*EL:52*/v1 instanceof ItemEndCrystal;
        /*SL:54*/switch (this.expOnly.getValue()) {
            case ALL: {
                /*SL:57*/this.setClickDelay();
                /*SL:58*/break;
            }
            case EXP_ONLY: {
                /*SL:60*/if (v2) {
                    /*SL:61*/this.setClickDelay();
                    break;
                }
                break;
            }
            case CRYSTAL_ONLY: {
                /*SL:64*/if (v3) {
                    /*SL:65*/this.setClickDelay();
                    break;
                }
                break;
            }
            case EXP_AND_CRYSTAL_ONLY: {
                /*SL:68*/if (v3 || v2) {
                    /*SL:69*/this.setClickDelay();
                    break;
                }
                break;
            }
        }
    }
    
    private void setClickDelay() {
        try {
            /*SL:78*/ObfuscationReflectionHelper.setPrivateValue((Class)Minecraft.class, (Object)FastPlace.mc, (Object)0, new String[] { "rightClickDelayTimer", "field_71467_ac" });
        }
        catch (Exception v1) {
            /*SL:82*/v1.printStackTrace();
            /*SL:83*/this.setEnabled(false);
            /*SL:84*/ChatUtils.printMessage("Disabled fastplace due to error: " + v1.toString());
        }
    }
    
    enum Whitelist
    {
        ALL, 
        EXP_ONLY, 
        CRYSTAL_ONLY, 
        EXP_AND_CRYSTAL_ONLY;
    }
}
