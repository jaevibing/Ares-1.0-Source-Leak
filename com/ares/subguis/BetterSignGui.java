package com.ares.subguis;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import com.google.common.base.Predicate;
import java.util.LinkedList;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import java.util.List;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.client.gui.GuiScreen;

public class BetterSignGui extends GuiScreen
{
    public final TileEntitySign sign;
    private static int focusedField;
    private List<GuiTextField> textFields;
    private String[] defaultStrings;
    
    public BetterSignGui(final TileEntitySign a1) {
        this.sign = a1;
    }
    
    public void func_73866_w_() {
        /*SL:55*/this.field_146292_n.clear();
        /*SL:56*/Keyboard.enableRepeatEvents(true);
        /*SL:58*/this.textFields = new LinkedList<GuiTextField>();
        /*SL:59*/this.defaultStrings = new String[4];
        /*SL:60*/for (int v0 = 0; v0 < 4; ++v0) {
            final GuiTextField v = /*EL:62*/new GuiTextField(v0, this.field_146289_q, this.field_146294_l / 2 + 4, 75 + v0 * 24, 120, 20);
            /*SL:63*/v.func_175205_a((Predicate)this::validateText);
            /*SL:64*/v.func_146203_f(100);
            final String v2 = /*EL:66*/this.sign.field_145915_a[v0].func_150260_c();
            /*SL:68*/v.func_146180_a(this.defaultStrings[v0] = v2);
            /*SL:70*/this.textFields.add(v);
        }
        /*SL:73*/this.textFields.get(BetterSignGui.focusedField).func_146195_b(true);
        /*SL:75*/this.func_189646_b(new GuiButton(4, this.field_146294_l / 2 + 5, this.field_146295_m / 4 + 120, 120, 20, "Done"));
        /*SL:85*/this.func_189646_b(new GuiButton(5, this.field_146294_l / 2 - 125, this.field_146295_m / 4 + 120, 120, 20, "Cancel"));
        /*SL:96*/this.func_189646_b(new GuiButton(6, this.field_146294_l / 2 - 41, 147, 40, 20, "Shift"));
        /*SL:107*/this.func_189646_b(new GuiButton(7, this.field_146294_l / 2 - 41, 123, 40, 20, "Clear"));
        /*SL:118*/this.sign.func_145913_a(false);
    }
    
    protected void func_73864_a(final int a1, final int a2, final int a3) throws IOException {
        /*SL:124*/super.func_73864_a(a1, a2, a3);
        final int v1 = BetterSignGui.focusedField;
        /*SL:127*/this.textFields.forEach(a4 -> a4.func_146192_a(a1, a2, a3));
        /*SL:128*/this.updateFocusField();
        /*SL:130*/if (BetterSignGui.focusedField == v1 && !this.textFields.get(BetterSignGui.focusedField).func_146206_l()) {
            /*SL:131*/this.textFields.get(BetterSignGui.focusedField).func_146195_b(true);
        }
    }
    
    protected void func_73869_a(final char v1, final int v2) {
        /*SL:137*/switch (v2) {
            case 1: {
                /*SL:140*/this.exit();
                /*SL:141*/return;
            }
            case 15: {
                final int a4 = func_146272_n() ? /*EL:143*/-1 : 1;
                /*SL:144*/this.tabFocus(a4);
                /*SL:145*/return;
            }
            case 200: {
                /*SL:147*/this.tabFocus(-1);
                /*SL:148*/return;
            }
            case 28:
            case 156:
            case 208: {
                /*SL:150*/this.tabFocus(1);
                break;
            }
        }
        /*SL:152*/this.textFields.forEach(a3 -> a3.func_146201_a(v1, v2));
        /*SL:153*/this.sign.field_145915_a[BetterSignGui.focusedField] = (ITextComponent)new TextComponentString(this.textFields.get(BetterSignGui.focusedField).func_146179_b());
    }
    
    protected void func_146284_a(final GuiButton v-2) throws IOException {
        /*SL:160*/super.func_146284_a(v-2);
        /*SL:162*/switch (v-2.field_146127_k) {
            case 5: {
                /*SL:165*/for (int a3 = 0; a3 < 4; ++a3) {
                    /*SL:166*/this.sign.field_145915_a[a3] = (ITextComponent)new TextComponentString(this.defaultStrings[a3]);
                }
            }
            case 4: {
                /*SL:168*/this.exit();
                /*SL:169*/break;
            }
            case 6: {
                final String[] array = /*EL:171*/new String[4];
                /*SL:172*/for (int v0 = 0; v0 < 4; ++v0) {
                    final int v = func_146272_n() ? /*EL:173*/1 : -1;
                    final int v2 = /*EL:174*/this.wrapLine(v0 + v);
                    /*SL:175*/array[v0] = this.sign.field_145915_a[v2].func_150260_c();
                }
                final int v3;
                final Object o;
                /*SL:178*/this.textFields.forEach(a2 -> {
                    v3 = a2.func_175206_d();
                    a2.func_146180_a(o[v3]);
                    this.sign.field_145915_a[v3] = (ITextComponent)new TextComponentString(o[v3]);
                    return;
                });
                /*SL:183*/break;
            }
            case 7: {
                final int v4;
                /*SL:185*/this.textFields.forEach(a1 -> {
                    v4 = a1.func_175206_d();
                    a1.func_146180_a("");
                    this.sign.field_145915_a[v4] = (ITextComponent)new TextComponentString("");
                    return;
                });
                break;
            }
        }
    }
    
    public void func_73863_a(final int v2, final int v3, final float v4) {
        /*SL:198*/this.func_146276_q_();
        /*SL:199*/this.func_73732_a(this.field_146289_q, I18n.func_135052_a("sign.edit", new Object[0]), this.field_146294_l / 2, 40, 16777215);
        /*SL:200*/GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        /*SL:201*/GlStateManager.func_179094_E();
        /*SL:202*/GlStateManager.func_179109_b(this.field_146294_l / 2 - 63.0f, 0.0f, 50.0f);
        final float v5 = /*EL:203*/93.75f;
        /*SL:204*/GlStateManager.func_179152_a(-93.75f, -93.75f, -93.75f);
        /*SL:205*/GlStateManager.func_179114_b(180.0f, 0.0f, 1.0f, 0.0f);
        final Block v6 = /*EL:206*/this.sign.func_145838_q();
        /*SL:208*/if (v6 == Blocks.field_150472_an) {
            final float a1 = /*EL:209*/this.sign.func_145832_p() * 360 / 16.0f;
            /*SL:210*/GlStateManager.func_179114_b(a1, 0.0f, 1.0f, 0.0f);
            /*SL:211*/GlStateManager.func_179109_b(0.0f, -1.0625f, 0.0f);
        }
        else {
            final int a2 = /*EL:214*/this.sign.func_145832_p();
            float a3 = /*EL:215*/0.0f;
            /*SL:217*/if (a2 == 2) {
                /*SL:218*/a3 = 180.0f;
            }
            /*SL:220*/if (a2 == 4) {
                /*SL:221*/a3 = 90.0f;
            }
            /*SL:223*/if (a2 == 5) {
                /*SL:224*/a3 = -90.0f;
            }
            /*SL:226*/GlStateManager.func_179114_b(a3, 0.0f, 1.0f, 0.0f);
            /*SL:227*/GlStateManager.func_179109_b(0.0f, -0.7625f, 0.0f);
        }
        /*SL:230*/this.sign.field_145918_i = -1;
        TileEntityRendererDispatcher.field_147556_a.func_147549_a(/*EL:231*/(TileEntity)this.sign, -0.5, -0.75, -0.5, 0.0f);
        /*SL:232*/GlStateManager.func_179121_F();
        /*SL:234*/this.textFields.forEach(GuiTextField::func_146194_f);
        /*SL:236*/super.func_73863_a(v2, v3, v4);
    }
    
    void updateFocusField() {
        /*SL:241*/this.textFields.forEach(a1 -> {
            if (a1.func_146206_l()) {
                BetterSignGui.focusedField = a1.func_175206_d();
            }
        });
    }
    
    void exit() {
        /*SL:249*/this.sign.func_70296_d();
        /*SL:250*/this.field_146297_k.func_147108_a((GuiScreen)null);
    }
    
    public void func_146281_b() {
        /*SL:256*/Keyboard.enableRepeatEvents(false);
        final NetHandlerPlayClient v1 = /*EL:257*/this.field_146297_k.func_147114_u();
        /*SL:259*/if (v1 != null) {
            /*SL:260*/v1.func_147297_a((Packet)new CPacketUpdateSign(this.sign.func_174877_v(), this.sign.field_145915_a));
        }
        /*SL:262*/this.sign.func_145913_a(true);
    }
    
    void tabFocus(final int a1) {
        /*SL:267*/this.textFields.get(BetterSignGui.focusedField).func_146195_b(false);
        BetterSignGui.focusedField = /*EL:268*/this.wrapLine(BetterSignGui.focusedField + a1);
        /*SL:269*/this.textFields.get(BetterSignGui.focusedField).func_146195_b(true);
    }
    
    int wrapLine(final int a1) {
        /*SL:274*/if (a1 > 3) {
            /*SL:275*/return 0;
        }
        /*SL:276*/if (a1 < 0) {
            /*SL:277*/return 3;
        }
        /*SL:278*/return a1;
    }
    
    boolean validateText(final String v2) {
        /*SL:283*/if (this.field_146289_q.func_78256_a(v2) > 90) {
            /*SL:284*/return false;
        }
        final char[] charArray;
        final char[] v3 = /*EL:287*/charArray = v2.toCharArray();
        for (final char a1 : charArray) {
            /*SL:288*/if (!ChatAllowedCharacters.func_71566_a(a1)) {
                /*SL:289*/return false;
            }
        }
        /*SL:291*/return true;
    }
    
    static {
        BetterSignGui.focusedField = 0;
    }
}
