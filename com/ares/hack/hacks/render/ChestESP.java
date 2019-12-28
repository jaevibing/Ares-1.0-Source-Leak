package com.ares.hack.hacks.render;

import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import com.ares.utils.render.RenderUtils;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Chest ESP", description = "yes", category = EnumCategory.RENDER)
public class ChestESP extends BaseHack
{
    private Setting<Boolean> noNether;
    private Setting<Boolean> chams;
    private Setting<Boolean> outline;
    private Setting<Boolean> chests;
    private Setting<Boolean> enderChests;
    private Setting<Boolean> beds;
    private Setting<Double> chestsR;
    private Setting<Double> chestsG;
    private Setting<Double> chestsB;
    private Setting<Double> chestsA;
    private Setting<Double> bedsR;
    private Setting<Double> bedsG;
    private Setting<Double> bedsB;
    private Setting<Double> bedsA;
    private Setting<Double> enderChestsR;
    private Setting<Double> enderChestsG;
    private Setting<Double> enderChestsB;
    private Setting<Double> enderChestsA;
    
    public ChestESP() {
        this.noNether = new BooleanSetting("No Nether", this, false);
        this.chams = new BooleanSetting("Chams", this, true);
        this.outline = new BooleanSetting("Outlines", this, true);
        this.chests = new BooleanSetting("Chests", this, true);
        this.enderChests = new BooleanSetting("Ender Chests", this, true);
        this.beds = new BooleanSetting("Beds", this, true);
        this.chestsR = new DoubleSetting("Chests R", this, 1.0, 0.0, 1.0);
        this.chestsG = new DoubleSetting("Chests G", this, 1.0, 0.0, 1.0);
        this.chestsB = new DoubleSetting("Chests B", this, 0.0, 0.0, 1.0);
        this.chestsA = new DoubleSetting("Chests A", this, 1.0, 0.0, 1.0);
        this.bedsR = new DoubleSetting("Beds R", this, 1.0, 0.0, 1.0);
        this.bedsG = new DoubleSetting("Beds G", this, 1.0, 0.0, 1.0);
        this.bedsB = new DoubleSetting("Beds B", this, 0.0, 0.0, 1.0);
        this.bedsA = new DoubleSetting("Beds A", this, 1.0, 0.0, 1.0);
        this.enderChestsR = new DoubleSetting("E Chests R", this, 0.0, 0.0, 1.0);
        this.enderChestsG = new DoubleSetting("E Chests G", this, 1.0, 0.0, 1.0);
        this.enderChestsB = new DoubleSetting("E Chests B", this, 0.0, 0.0, 1.0);
        this.enderChestsA = new DoubleSetting("E Chests A", this, 1.0, 0.0, 1.0);
    }
    
    public void onRender3d() {
        /*SL:46*/if (!this.getEnabled()) {
            return;
        }
        /*SL:49*/if (this.noNether.getValue() && ChestESP.mc.field_71439_g.field_71093_bK == -1) {
            return;
        }
        /*SL:51*/RenderUtils.glStart();
        /*SL:53*/for (final TileEntity tileEntity : ChestESP.mc.field_71441_e.field_147482_g) {
            final boolean b = /*EL:55*/this.chests.getValue() && tileEntity instanceof TileEntityChest;
            final boolean b2 = /*EL:56*/this.beds.getValue() && tileEntity instanceof TileEntityBed;
            final boolean b3 = /*EL:57*/this.enderChests.getValue() && tileEntity instanceof TileEntityEnderChest;
            /*SL:60*/if (!b && !b2 && !b3) {
                continue;
            }
            float n = /*EL:62*/0.0f;
            float n2 = /*EL:63*/0.0f;
            float n3 = /*EL:64*/0.0f;
            float n4 = /*EL:65*/0.0f;
            /*SL:67*/if (b) {
                /*SL:69*/n = (float)(Object)this.chestsR.getValue();
                /*SL:70*/n2 = (float)(Object)this.chestsG.getValue();
                /*SL:71*/n3 = (float)(Object)this.chestsB.getValue();
                /*SL:72*/n4 = (float)(Object)this.chestsA.getValue();
            }
            /*SL:74*/if (b2) {
                /*SL:76*/n = (float)(Object)this.bedsR.getValue();
                /*SL:77*/n2 = (float)(Object)this.bedsG.getValue();
                /*SL:78*/n3 = (float)(Object)this.bedsB.getValue();
                /*SL:79*/n4 = (float)(Object)this.bedsA.getValue();
            }
            /*SL:81*/if (b3) {
                /*SL:83*/n = (float)(Object)this.enderChestsR.getValue();
                /*SL:84*/n2 = (float)(Object)this.enderChestsG.getValue();
                /*SL:85*/n3 = (float)(Object)this.enderChestsB.getValue();
                /*SL:86*/n4 = (float)(Object)this.enderChestsA.getValue();
            }
            AxisAlignedBB v0 = /*EL:89*/RenderUtils.getBoundingBox(tileEntity.func_174877_v());
            /*SL:91*/if (tileEntity instanceof TileEntityChest) {
                final TileEntityChest v = /*EL:93*/(TileEntityChest)tileEntity;
                /*SL:96*/if (v.field_145990_j != null) {
                    continue;
                }
                if (v.field_145988_l != null) {
                    /*SL:97*/continue;
                }
                /*SL:99*/if (v.field_145991_k != null) {
                    /*SL:100*/v0 = v0.func_111270_a(RenderUtils.getBoundingBox(v.field_145991_k.func_174877_v()));
                }
                else/*SL:101*/ if (v.field_145992_i != null) {
                    /*SL:102*/v0 = v0.func_111270_a(RenderUtils.getBoundingBox(v.field_145992_i.func_174877_v()));
                }
            }
            /*SL:105*/GL11.glColor4f(n, n2, n3, n4);
            /*SL:106*/if (this.chams.getValue()) {
                /*SL:116*/RenderUtils.drawSolidBox(v0);
            }
            /*SL:120*/if (this.outline.getValue()) {
                /*SL:130*/RenderUtils.drawOutlinedBox(v0);
            }
            /*SL:132*/GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        /*SL:135*/RenderUtils.glEnd();
    }
}
