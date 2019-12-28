package com.ares.gui.guis.containergui;

import com.ares.utils.chat.ChatUtils;
import net.minecraft.client.gui.GuiScreen;
import com.ares.hack.hacks.hud.impl.GuiEditHud;
import com.ares.hack.settings.EnumSettingType;
import com.ares.gui.guis.containergui.buttons.SettingButton;
import com.ares.gui.guis.containergui.buttons.DropDownButton;
import com.ares.gui.guis.containergui.buttons.HackToggleButton;
import com.ares.gui.guis.containergui.buttons.CategoryButton;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import com.ares.gui.guis.containergui.elements.ElementHackList;
import com.ares.hack.categories.Category;
import net.minecraft.util.ResourceLocation;
import com.ares.gui.elements.ElementImage;
import com.ares.utils.ColourUtils;
import com.ares.gui.elements.ElementContainer;
import com.ares.gui.buttons.TextButton;
import com.ares.gui.buttons.ImageButton;
import com.ares.gui.guis.containergui.elements.ElementCategoryList;
import com.ares.gui.guis.AresGui;

public class ContainerGui extends AresGui
{
    ElementCategoryList categoryList;
    ImageButton editHudImageButton;
    TextButton editHudButton;
    
    public void func_73866_w_() {
        /*SL:36*/super.func_73866_w_();
        /*SL:37*/this.field_146291_p = true;
        final int n = /*EL:39*/this.field_146294_l / 2 - this.field_146294_l / 6;
        final int a3 = /*EL:40*/this.field_146295_m / 2 - this.field_146295_m / 3;
        final int n2 = /*EL:41*/this.field_146294_l / 3;
        final int n3 = /*EL:42*/this.field_146295_m * 2 / 3;
        final ElementContainer elementContainer = /*EL:45*/this.<ElementContainer>addElement(new ElementContainer(this, n, a3 + n3 / 8, n2 / 4, n3 * 3 / 4, new float[] { 0.0f, 0.0f, 0.0f, 0.8f }, ColourUtils.getAresRed(1.0f)));
        final ElementContainer elementContainer2 = /*EL:46*/this.<ElementContainer>addElement(new ElementContainer(this, n, a3, elementContainer.width, n3 / 8, new float[] { 0.0f, 0.0f, 0.0f, 0.8f }, ColourUtils.getAresRed(1.0f)));
        final ElementContainer elementContainer3 = /*EL:47*/this.<ElementContainer>addElement(new ElementContainer(this, n + elementContainer.width, elementContainer2.y, n2 - elementContainer.width, elementContainer.height + elementContainer2.height, new float[] { 0.0f, 0.0f, 0.0f, 0.8f }, ColourUtils.getAresRed(1.0f)));
        final ElementImage elementImage = /*EL:49*/this.<ElementImage>addElement(new ElementImage(this, elementContainer2.x - (elementContainer2.width - elementContainer2.height * 2) / 2, elementContainer2.y, elementContainer2.height * 2, elementContainer2.height, new ResourceLocation("ares", "ares_logo_horizontal.png")));
        /*SL:50*/this.categoryList = this.<ElementCategoryList>addElement(new ElementCategoryList(this, elementContainer.x, elementContainer.y + 4, elementContainer.width));
        /*SL:52*/for (final Category v1 : Category.getAll()) {
            /*SL:54*/this.<ElementHackList>addElement(new ElementHackList(this, elementContainer3.x, elementContainer3.y, elementContainer3.width, v1));
        }
        /*SL:56*/ElementHackList.setList(ElementHackList.currentCategory);
        /*SL:58*/this.editHudImageButton = (ImageButton)this.func_189646_b((GuiButton)new ImageButton(this, elementContainer.x, elementContainer.y + elementContainer.height - 30, 30, 30, new ResourceLocation("ares", "move_arrow.png")));
        /*SL:59*/this.editHudButton = (TextButton)this.func_189646_b((GuiButton)new TextButton(this, elementContainer.x + 32, elementContainer.y + elementContainer.height - this.field_146297_k.field_71466_p.field_78288_b - 5 - this.field_146297_k.field_71466_p.field_78288_b / 2, "Edit hud"));
    }
    
    protected void func_73869_a(final char a1, final int a2) throws IOException {
        /*SL:64*/super.func_73869_a(a1, a2);
    }
    
    protected void func_146284_a(final GuiButton v2) throws IOException {
        /*SL:70*/if (v2 instanceof CategoryButton) {
            /*SL:72*/ElementHackList.setList(((CategoryButton)v2).getCategory());
        }
        /*SL:75*/if (v2 instanceof HackToggleButton) {
            /*SL:77*/((HackToggleButton)v2).toggle();
        }
        /*SL:80*/if (v2 instanceof DropDownButton) {
            /*SL:82*/((DropDownButton)v2).toggle();
        }
        /*SL:85*/if (v2 instanceof SettingButton) {
            /*SL:87*/if (((SettingButton)v2).setting.getType() == EnumSettingType.BOOLEAN) {
                /*SL:89*/((SettingButton)v2).setting.setValue(!((SettingButton)v2).setting.getValue());
            }
            else/*SL:91*/ if (((SettingButton)v2).setting.getType() == EnumSettingType.ENUM) {
                int a1 = /*EL:93*/0;
                while (a1 < ((SettingButton)v2).setting.getModes().length) {
                    /*SL:95*/if (((SettingButton)v2).setting.getModes()[a1].equals(((SettingButton)v2).setting.getValue())) {
                        /*SL:97*/if (a1 == 0) {
                            ((SettingButton)v2).setting.setValue(((SettingButton)v2).setting.getModes()[((SettingButton)v2).setting.getModes().length - 1]);
                            break;
                        }
                        /*SL:98*/((SettingButton)v2).setting.setValue(((SettingButton)v2).setting.getModes()[a1 - 1]);
                        /*SL:99*/break;
                    }
                    else {
                        ++a1;
                    }
                }
            }
            else/*SL:103*/ if (((SettingButton)v2).setting.getType() == EnumSettingType.BIND) {
                /*SL:105*/((SettingButton)v2).click();
            }
        }
        /*SL:109*/if (v2 == this.editHudButton || v2 == this.editHudImageButton) {
            /*SL:111*/this.field_146297_k.func_152344_a(() -> {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiEditHud());
                ChatUtils.printMessage("Opened Hud Editor");
                return;
            });
        }
        /*SL:118*/super.func_146284_a(v2);
    }
}
