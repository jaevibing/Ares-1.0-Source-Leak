package com.ares.hack.hacks.ui;

import java.util.Iterator;
import java.awt.Color;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Players Potions", description = "Show players potions", category = EnumCategory.UI)
public class PlayersPotions extends BaseHack
{
    private final Setting<Integer> x;
    private final Setting<Integer> y;
    
    public PlayersPotions() {
        this.x = new IntegerSetting("x", this, 0, 0, (int)Math.round(PlayersPotions.mc.field_71443_c * 1.3));
        this.y = new IntegerSetting("y", this, 0, 0, (int)Math.round(PlayersPotions.mc.field_71443_c * 1.3));
    }
    
    public void onRender2d() {
        /*SL:23*/if (this.getEnabled()) {
            int n = /*EL:25*/0;
            /*SL:26*/for (final EntityPlayer entityPlayer : PlayersPotions.mc.field_71441_e.field_73010_i) {
                /*SL:28*/if (entityPlayer.func_110124_au().equals(PlayersPotions.mc.field_71439_g.func_110124_au())) {
                    continue;
                }
                final String displayNameString = /*EL:30*/entityPlayer.getDisplayNameString();
                /*SL:31*/for (final Map.Entry<Potion, PotionEffect> v0 : entityPlayer.func_193076_bZ().entrySet()) {
                    final String v = /*EL:33*/v0.getKey().func_76393_a();
                    final String v2 = /*EL:34*/Potion.func_188410_a((PotionEffect)v0.getValue(), 1.0f);
                    PlayersPotions.mc.field_71466_p.func_78276_b(/*EL:36*/displayNameString + " : " + v + " : " + v2, (int)this.x.getValue(), this.y.getValue() + n, Color.WHITE.getRGB());
                    /*SL:38*/n += PlayersPotions.mc.field_71466_p.field_78288_b + 2;
                }
            }
        }
    }
}
