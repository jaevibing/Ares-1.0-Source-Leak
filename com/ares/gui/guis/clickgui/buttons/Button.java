package com.ares.gui.guis.clickgui.buttons;

import java.util.List;
import org.lwjgl.opengl.GL11;
import com.ares.Globals;
import net.minecraft.client.gui.GuiScreen;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;

public class Button
{
    private static final ResourceLocation WHITE;
    public boolean leftClickToggled;
    public boolean rightClickToggled;
    public int x;
    public int y;
    public int width;
    public int height;
    public String text;
    public boolean isVisible;
    private float[] color;
    public String textColor;
    public static ArrayList<Button> Objects;
    
    Button(final int a1, final int a2, final int a3, final int a4, final String a5, final boolean a6, final float[] a7) {
        this.leftClickToggled = false;
        this.rightClickToggled = false;
        this.textColor = "FFFFFF";
        this.x = a1;
        this.y = a2;
        this.width = a3;
        this.height = a4;
        this.text = a5;
        this.isVisible = a6;
        this.color = a7;
        Button.Objects.add(this);
    }
    
    public void drawButton(final GuiScreen a1, final int a2, final int a3) {
        final int v1 = /*EL:65*/1;
        Globals.mc.field_71446_o.func_110577_a(Button.WHITE);
        /*SL:68*/GL11.glPushAttrib(1048575);
        /*SL:69*/GL11.glPushMatrix();
        /*SL:70*/GL11.glTranslatef(0.0f, 0.0f, 0.0f);
        /*SL:72*/GL11.glColor4f(this.color[0], this.color[1], this.color[2], this.color[3]);
        final List<String> v2 = /*EL:74*/(List<String>)a1.field_146297_k.field_71466_p.func_78271_c(this.text, this.width - (v1 + 1));
        boolean v3 = /*EL:75*/false;
        final int v4 = /*EL:76*/v2.size() * a1.field_146297_k.field_71466_p.field_78288_b + 15;
        /*SL:77*/if (v4 > a1.field_146297_k.field_71466_p.field_78288_b + 15) {
            /*SL:79*/v3 = true;
            /*SL:80*/this.height = v4;
        }
        /*SL:83*/a1.func_73729_b(this.x, this.y, 0, 0, this.width, this.height);
        /*SL:85*/GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        /*SL:87*/a1.func_73729_b(this.x + v1, this.y + v1, 0, 0, this.width - v1 * 2, this.height - v1 * 2);
        /*SL:89*/GL11.glPopMatrix();
        /*SL:90*/GL11.glPopAttrib();
        /*SL:92*/a1.field_146297_k.field_71466_p.func_78279_b(this.text, this.x + (v1 + 1) + (this.width - (v1 + 1) - a1.field_146297_k.field_71466_p.func_78256_a((String)v2.get(0))) / 2, this.y + this.height / 2 - a1.field_146297_k.field_71466_p.field_78288_b * v2.size() / 2, this.width - (v1 + 1), Integer.parseInt(this.textColor, 16));
        /*SL:95*/this.onDrawButton(a1, a2, a3);
    }
    
    public void onDrawButton(final GuiScreen a1, final int a2, final int a3) {
    }
    
    static {
        WHITE = new ResourceLocation("ares", "white.png");
        Button.Objects = new ArrayList<Button>();
    }
}
