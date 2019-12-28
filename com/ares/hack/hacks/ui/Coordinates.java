package com.ares.hack.hacks.ui;

import net.minecraft.util.math.Vec3d;
import java.awt.Color;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Coordinates", description = "Show your coordinates", category = EnumCategory.UI)
public class Coordinates extends BaseHack
{
    private Setting<Integer> x;
    private Setting<Integer> y;
    
    public Coordinates() {
        this.x = new IntegerSetting("x", this, 50, 0, (int)Math.round(Coordinates.mc.field_71443_c * 1.2));
        this.y = new IntegerSetting("y", this, 50, 0, (int)Math.round(Coordinates.mc.field_71440_d * 1.2));
    }
    
    public void onRender2d() {
        /*SL:20*/if (this.getEnabled()) {
            Coordinates.mc.field_71466_p.func_78276_b(/*EL:22*/this.vecFormat(Coordinates.mc.field_71439_g.func_174791_d()), (int)this.x.getValue(), (int)this.y.getValue(), Color.WHITE.getRGB());
        }
    }
    
    private String vecFormat(final Vec3d a1) {
        /*SL:28*/return /*EL:29*/(int)Math.floor(a1.field_72450_a) + ", " + (int)Math.floor(a1.field_72448_b) + ", " + (int)Math.floor(a1.field_72449_c) + " (" + /*EL:31*/(int)Math.floor(a1.field_72450_a) / 8 + ", " + (int)Math.floor(a1.field_72449_c) / 8 + ")";
    }
}
