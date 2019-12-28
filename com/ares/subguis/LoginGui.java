package com.ares.subguis;

import java.io.IOException;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.ares.api.AuthUtils;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import java.io.File;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class LoginGui extends GuiScreen
{
    private GuiScreen original;
    private GuiTextField usernameField;
    private GuiTextField passwordField;
    private String error;
    
    public LoginGui(final GuiScreen a1) {
        this.error = "";
        this.original = a1;
    }
    
    public void func_73866_w_() {
        /*SL:48*/Keyboard.enableRepeatEvents(true);
        /*SL:49*/this.usernameField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 60 + 0, 202, 20);
        /*SL:50*/this.passwordField = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 60 + 26, 202, 20);
        /*SL:51*/this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 60 + 52, "Login"));
        /*SL:52*/this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 60 + 76, "Cancel"));
        /*SL:53*/this.usernameField.func_146203_f(500);
        /*SL:54*/this.passwordField.func_146203_f(500);
        /*SL:56*/super.func_73866_w_();
        try {
            final String read = /*EL:60*/Files.asCharSource(new File("Ares/accounts.txt"), Charsets.UTF_8).read();
            /*SL:61*/if (!read.isEmpty()) {
                final String[] v0 = /*EL:63*/read.split(":");
                try {
                    /*SL:67*/if (!AuthUtils.mcLogin(v0[0].trim(), v0[1].trim())) {
                        System.out.println(/*EL:69*/"Could not log in");
                        /*SL:70*/this.error = "Could not log in";
                        /*SL:71*/return;
                    }
                }
                catch (AuthenticationException v) {
                    /*SL:76*/v.printStackTrace();
                    System.out.println(/*EL:77*/"Could not log in: " + v.toString());
                    /*SL:78*/this.error = "Could not log in: " + v.toString();
                    /*SL:79*/return;
                }
                /*SL:81*/this.field_146297_k.func_147108_a(this.original);
            }
        }
        catch (Exception ex) {
            /*SL:86*/ex.printStackTrace();
        }
    }
    
    public void func_146281_b() {
        /*SL:93*/Keyboard.enableRepeatEvents(false);
    }
    
    public void func_73876_c() {
        /*SL:99*/this.usernameField.func_146178_a();
        /*SL:100*/this.passwordField.func_146178_a();
    }
    
    public void func_73864_a(final int a1, final int a2, final int a3) throws IOException {
        /*SL:106*/this.usernameField.func_146192_a(a1, a2, a3);
        /*SL:107*/this.passwordField.func_146192_a(a1, a2, a3);
        /*SL:108*/super.func_73864_a(a1, a2, a3);
    }
    
    public void func_146284_a(final GuiButton v2) {
        /*SL:114*/if (v2.field_146127_k == 1) {
            System.out.println(/*EL:116*/"Attempting subguis, username: " + this.usernameField.func_146179_b().trim());
            try {
                /*SL:119*/if (!AuthUtils.mcLogin(this.usernameField.func_146179_b().trim(), this.passwordField.func_146179_b().trim())) {
                    System.out.println(/*EL:121*/"Could not log in");
                    /*SL:122*/this.error = "Could not log in";
                    /*SL:123*/return;
                }
            }
            catch (AuthenticationException a1) {
                /*SL:128*/a1.printStackTrace();
                System.out.println(/*EL:129*/"Could not log in: " + a1.toString());
                /*SL:130*/this.error = "Could not log in: " + a1.toString();
                /*SL:131*/return;
            }
            /*SL:133*/this.field_146297_k.func_147108_a(this.original);
        }
        else/*SL:135*/ if (v2.field_146127_k == 2) {
            /*SL:137*/this.field_146297_k.func_147108_a(this.original);
        }
    }
    
    protected void func_73869_a(final char a1, final int a2) {
        /*SL:144*/this.usernameField.func_146201_a(a1, a2);
        /*SL:145*/this.passwordField.func_146201_a(a1, a2);
        /*SL:146*/if (a1 == '\t') {
            /*SL:149*/if (this.usernameField.func_146206_l()) {
                /*SL:151*/this.usernameField.func_146195_b(false);
                /*SL:152*/this.passwordField.func_146195_b(true);
            }
            else/*SL:154*/ if (this.passwordField.func_146206_l()) {
                /*SL:156*/this.usernameField.func_146195_b(false);
                /*SL:157*/this.passwordField.func_146195_b(false);
            }
        }
        /*SL:160*/if (a1 == '\r') {
            /*SL:162*/this.func_146284_a(this.field_146292_n.get(0));
        }
    }
    
    public void func_73863_a(final int a3, final int v1, final float v2) {
        /*SL:169*/this.func_146276_q_();
        /*SL:170*/this.func_73732_a(this.field_146289_q, "Ares Client: Login to Minecraft", this.field_146294_l / 2, 2, 16777215);
        /*SL:171*/this.func_73731_b(this.field_146289_q, "Email", this.field_146294_l / 2 - 100 - 50, this.field_146295_m / 4 + 60 + 0 + 6, 16777215);
        /*SL:172*/this.func_73731_b(this.field_146289_q, "Password", this.field_146294_l / 2 - 100 - 50, this.field_146295_m / 4 + 60 + 26 + 6, 16777215);
        /*SL:173*/this.func_73732_a(this.field_146289_q, this.error, this.field_146294_l / 2, this.field_146295_m / 4 + 60 + 100, 16711680);
        try {
            /*SL:176*/this.usernameField.func_146194_f();
            /*SL:177*/this.passwordField.func_146194_f();
        }
        catch (Exception a4) {
            /*SL:181*/a4.printStackTrace();
        }
        /*SL:184*/super.func_73863_a(a3, v1, v2);
    }
}
