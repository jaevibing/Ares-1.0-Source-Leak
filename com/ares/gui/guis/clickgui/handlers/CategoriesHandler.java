package com.ares.gui.guis.clickgui.handlers;

import com.ares.gui.guis.clickgui.buttons.Button;
import java.util.Iterator;
import com.ares.gui.guis.clickgui.buttons.SettingButton;
import com.ares.gui.guis.clickgui.buttons.HackButton;
import com.ares.gui.guis.clickgui.GuiNotif;
import org.lwjgl.input.Mouse;
import com.ares.gui.guis.clickgui.buttons.CategoryButton;

public class CategoriesHandler extends Handler
{
    private CategoryButton catToRemap;
    
    @Override
    public void onRender() {
        /*SL:33*/if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
            /*SL:35*/for (final CategoryButton v1 : CategoryButton.getAllInverted()) {
                if (v1.isDragging) {
                    break;
                }
            }
            /*SL:37*/for (final CategoryButton v1 : CategoryButton.getAllInverted()) {
                /*SL:39*/if (GuiNotif.mouseX > v1.x && GuiNotif.mouseY > v1.y && GuiNotif.mouseX < v1.x + 100 && GuiNotif.mouseY < v1.y + 20) {
                    /*SL:41*/this.setRemap(v1);
                    /*SL:42*/break;
                }
                /*SL:45*/for (final HackButton v2 : v1.getHackButtons()) {
                    /*SL:47*/if (GuiNotif.mouseX > v2.x && GuiNotif.mouseY > v2.y && GuiNotif.mouseX < v2.x + v2.width && GuiNotif.mouseY < v2.y + v2.height) {
                        /*SL:49*/this.setRemap(v1);
                        /*SL:50*/break;
                    }
                    /*SL:53*/for (final SettingButton v3 : v2.getSettingButtons()) {
                        /*SL:55*/if (GuiNotif.mouseX > v3.x && GuiNotif.mouseY > v3.y && GuiNotif.mouseX < v3.x + v3.width && GuiNotif.mouseY < v3.y + v3.height) {
                            /*SL:57*/this.setRemap(v1);
                            /*SL:58*/break;
                        }
                    }
                }
            }
        }
        /*SL:65*/this.remapCat();
        /*SL:68*/for (final CategoryButton v1 : CategoryButton.getAllInverted()) {
            /*SL:72*/if (Mouse.isButtonDown(0) && GuiNotif.mouseX > v1.x && GuiNotif.mouseY > v1.y && GuiNotif.mouseX < v1.x + 100 && GuiNotif.mouseY < v1.y + 20 && /*EL:74*/!v1.isDragging) {
                /*SL:76*/v1.StartMouseX = GuiNotif.mouseX - v1.x;
                /*SL:77*/v1.StartMouseY = GuiNotif.mouseY - v1.y;
                /*SL:79*/v1.isDragging = true;
            }
            /*SL:83*/if (v1.isDragging) {
                /*SL:85*/v1.x = GuiNotif.mouseX - v1.StartMouseX;
                /*SL:86*/v1.y = GuiNotif.mouseY - v1.StartMouseY;
            }
            /*SL:89*/if (!Mouse.isButtonDown(0)) {
                /*SL:91*/v1.isDragging = false;
            }
            /*SL:94*/if (v1.isDragging && Mouse.isButtonDown(0)) {
                /*SL:96*/break;
            }
        }
        /*SL:100*/for (final CategoryButton v1 : CategoryButton.getAllInverted()) {
            /*SL:103*/if (!v1.rightClickToggled) {
                /*SL:105*/for (final HackButton v2 : v1.getHackButtons()) {
                    /*SL:107*/v2.isVisible = false;
                    /*SL:108*/v2.getSettingButtons().forEach(a1 -> a1.isVisible = false);
                }
            }
            else {
                /*SL:111*/v1.getHackButtons().forEach(a1 -> a1.isVisible = true);
            }
            Button v4 = /*EL:115*/null;
            /*SL:116*/for (final HackButton v5 : v1.getHackButtons()) {
                /*SL:118*/v5.x = v1.x;
                /*SL:119*/if (v4 != null) {
                    /*SL:121*/v5.y = v4.y + v4.height;
                }
                else {
                    /*SL:124*/v5.y = v1.y + 20;
                }
                /*SL:127*/v4 = v5;
            }
        }
    }
    
    private void setRemap(final CategoryButton a1) {
        /*SL:136*/this.catToRemap = a1;
    }
    
    private void remapCat() {
        /*SL:141*/if (this.catToRemap != null) {
            /*SL:143*/CategoryButton.getAll().remove(this.catToRemap);
            /*SL:144*/CategoryButton.getAll().add(this.catToRemap);
        }
    }
}
