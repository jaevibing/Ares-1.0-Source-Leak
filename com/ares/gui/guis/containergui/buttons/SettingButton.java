package com.ares.gui.guis.containergui.buttons;

import com.ares.gui.elements.Element;
import com.ares.Globals;
import net.minecraft.client.renderer.GlStateManager;
import com.ares.utils.Utils;
import com.ares.hack.settings.settings.number.NumberSetting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import com.ares.hack.settings.EnumSettingType;
import com.ares.utils.ColourUtils;
import com.ares.gui.guis.AresGui;
import com.ares.hack.settings.Setting;
import com.ares.gui.buttons.Button;

public class SettingButton extends Button
{
    public static final double scale = 0.8;
    public Setting setting;
    private AresGui gui;
    private float[] color;
    private String text;
    private boolean clicked;
    
    public SettingButton(final AresGui a1, final int a2, final int a3, final Setting a4) {
        super(a1, a2, a3, "");
        this.color = ColourUtils.getAresRed(0.8f);
        this.clicked = false;
        this.gui = a1;
        this.setting = a4;
        this.text = a4.getName() + ": " + a4.getValue();
        this.field_146121_g = (int)(SettingButton.mc.field_71466_p.field_78288_b * 0.8) + 1;
    }
    
    public void click() {
        /*SL:38*/this.clicked = !this.clicked;
    }
    
    public void draw() {
        final Number v0 = /*EL:44*/(this.gui.mouseX - this.field_146128_h) / Integer.valueOf(this.field_146120_f).doubleValue();
        /*SL:46*/if (this.setting.getType() == EnumSettingType.DOUBLE && this.func_146115_a() && Mouse.isButtonDown(0)) {
            /*SL:48*/this.setting.setValue(v0.doubleValue() * (this.setting.getMax().doubleValue() - this.setting.getMin().doubleValue()) + this.setting.getMin().doubleValue());
        }
        else/*SL:50*/ if (this.setting.getType() == EnumSettingType.FLOAT && this.func_146115_a() && Mouse.isButtonDown(0)) {
            /*SL:52*/this.setting.setValue(v0.floatValue() * (this.setting.getMax().floatValue() - this.setting.getMin().floatValue()) + this.setting.getMin().floatValue());
        }
        else/*SL:54*/ if (this.setting.getType() == EnumSettingType.INTEGER && this.func_146115_a() && Mouse.isButtonDown(0)) {
            final Number v = /*EL:56*/v0.doubleValue() * (this.setting.getMax().intValue() - this.setting.getMin().intValue()) + this.setting.getMin().intValue();
            /*SL:57*/this.setting.setValue(v.intValue());
        }
        /*SL:60*/if (this.setting.getType() == EnumSettingType.BIND && this.clicked) {
            /*SL:63*/if (Keyboard.getEventKeyState()) {
                /*SL:65*/this.setting.setValue(Keyboard.getKeyName(Keyboard.getEventKey()));
                /*SL:66*/this.text = this.setting.getName() + ": " + this.setting.getValue();
                /*SL:67*/this.clicked = false;
            }
            else/*SL:69*/ if (Mouse.isButtonDown(1)) {
                /*SL:71*/this.setting.setValue("NONE");
                /*SL:72*/this.text = this.setting.getName() + ": " + this.setting.getValue();
                /*SL:73*/this.clicked = false;
            }
            else {
                /*SL:77*/this.text = this.setting.getName() + ": ...";
            }
        }
        else/*SL:82*/ if (this.setting instanceof NumberSetting) {
            /*SL:84*/this.text = this.setting.getName() + ": " + Utils.roundDouble(this.setting.getValue().doubleValue(), 2);
        }
        else {
            /*SL:88*/this.text = this.setting.getName() + ": " + this.setting.getValue();
        }
        /*SL:92*/this.field_146120_f = (int)(SettingButton.mc.field_71466_p.func_78256_a(this.text) * 0.8);
        /*SL:94*/GlStateManager.func_179094_E();
        /*SL:95*/GlStateManager.func_179139_a(0.8, 0.8, 1.0);
        /*SL:96*/this.func_73731_b(SettingButton.mc.field_71466_p, this.text, (int)(this.field_146128_h / 0.8), (int)(this.field_146129_i / 0.8), Integer.parseInt("FFFFFF", 16));
        /*SL:97*/GlStateManager.func_179121_F();
        Globals.mc.field_71446_o.func_110577_a(Element.WHITE);
        /*SL:100*/if (this.func_146115_a()) {
            /*SL:102*/GlStateManager.func_179131_c(this.color[0], this.color[1] + 0.2f, this.color[2] + 0.2f, this.color[3]);
        }
        else {
            /*SL:106*/GlStateManager.func_179131_c(this.color[0], this.color[1], this.color[2], this.color[3]);
        }
        /*SL:108*/this.func_73729_b(this.field_146128_h, this.field_146129_i + SettingButton.mc.field_71466_p.field_78288_b, 0, 0, (int)(SettingButton.mc.field_71466_p.func_78256_a(this.text) * 0.8), 1);
        /*SL:109*/GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        /*SL:112*/if (this.setting.getType() == EnumSettingType.DOUBLE || this.setting.getType() == EnumSettingType.FLOAT || this.setting.getType() == EnumSettingType.INTEGER) {
            final int v2 = /*EL:114*/this.field_146120_f * this.setting.getValue().intValue() / Math.max(this.setting.getMax().intValue() - this.setting.getMin().intValue(), 1);
            /*SL:115*/this.func_73729_b(this.field_146128_h, this.field_146129_i + SettingButton.mc.field_71466_p.field_78288_b - 1, 0, 0, v2, 1);
        }
    }
}
