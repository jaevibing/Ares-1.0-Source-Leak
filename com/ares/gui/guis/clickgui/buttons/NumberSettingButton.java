package com.ares.gui.guis.clickgui.buttons;

import org.lwjgl.opengl.GL11;
import com.ares.gui.guis.clickgui.GuiUtil;
import com.ares.Globals;
import net.minecraft.client.gui.GuiScreen;
import com.ares.hack.settings.Setting;

public class NumberSettingButton extends SettingButton
{
    private Setting setting;
    
    public NumberSettingButton(final Setting a1) {
        super(a1);
        this.setting = a1;
    }
    
    @Override
    public void onDrawButton(final GuiScreen a1, final int a2, final int a3) {
        Globals.mc.field_71446_o.func_110577_a(GuiUtil.white);
        /*SL:41*/GL11.glPushAttrib(1048575);
        /*SL:42*/GL11.glPushMatrix();
        /*SL:44*/GL11.glTranslatef(0.0f, 0.0f, 0.0f);
        /*SL:46*/a1.func_73729_b(this.x + 1, this.y + this.height - 2, 0, 0, (this.width - 2) * this.setting.getValue().intValue() / Math.max(this.setting.getMax().intValue() - this.setting.getMin().intValue(), 1), 1);
        /*SL:48*/GL11.glPopMatrix();
        /*SL:49*/GL11.glPopAttrib();
    }
    
    @Override
    public Setting getSetting() {
        /*SL:54*/return this.setting;
    }
}
