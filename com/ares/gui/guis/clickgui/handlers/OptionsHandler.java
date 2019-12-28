package com.ares.gui.guis.clickgui.handlers;

import com.ares.hack.settings.EnumSettingType;
import com.ares.gui.guis.clickgui.buttons.Button;
import java.util.Iterator;
import com.ares.utils.Utils;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import com.ares.gui.guis.clickgui.GuiNotif;
import java.util.Objects;
import com.ares.gui.guis.clickgui.buttons.SettingButton;
import java.util.ArrayList;
import com.ares.gui.guis.clickgui.buttons.HackButton;

public class OptionsHandler extends Handler
{
    @Override
    public void onRender() {
        /*SL:36*/for (final HackButton hackButton : HackButton.getAll()) {
            Button button = /*EL:38*/null;
            /*SL:40*/for (final SettingButton settingButton : Objects.<ArrayList<SettingButton>>requireNonNull(hackButton.getSettingButtons())) {
                final Number v0 = (GuiNotif.mouseX - /*EL:42*/settingButton.x) / Integer.valueOf(settingButton.width).doubleValue();
                /*SL:44*/switch (settingButton.getSetting().getType()) {
                    case BIND: {
                        /*SL:47*/if (settingButton.leftClickToggled) {
                            /*SL:49*/settingButton.text = "Bind: ...";
                            /*SL:51*/if (Keyboard.getEventKeyState()) {
                                /*SL:53*/settingButton.getSetting().setValue(Keyboard.getKeyName(Keyboard.getEventKey()));
                                /*SL:54*/settingButton.text = "Bind: " + settingButton.getSetting().getValue();
                                /*SL:55*/settingButton.leftClickToggled = false;
                                /*SL:56*/hackButton.leftClickToggled = false;
                                /*SL:57*/settingButton.held = true;
                            }
                            /*SL:60*/if (settingButton.rightClickToggled) {
                                /*SL:62*/settingButton.getSetting().setValue("NONE");
                                /*SL:63*/settingButton.text = "Bind: " + settingButton.getSetting().getValue();
                                /*SL:64*/settingButton.leftClickToggled = false;
                            }
                        }
                        else {
                            /*SL:68*/settingButton.text = "Bind: " + settingButton.getSetting().getValue();
                        }
                        /*SL:71*/if (settingButton.rightClickToggled) {
                            /*SL:73*/settingButton.rightClickToggled = false;
                            break;
                        }
                        break;
                    }
                    case ENUM: {
                        /*SL:79*/if (settingButton.leftClickToggled) {
                            int v = /*EL:81*/0;
                            while (v < settingButton.getSetting().getModes().length) {
                                /*SL:83*/if (settingButton.getSetting().getModes()[v].equals(settingButton.getSetting().getValue())) {
                                    /*SL:85*/if (v == 0) {
                                        settingButton.getSetting().setValue(settingButton.getSetting().getModes()[settingButton.getSetting().getModes().length - 1]);
                                        break;
                                    }
                                    /*SL:86*/settingButton.getSetting().setValue(settingButton.getSetting().getModes()[v - 1]);
                                    /*SL:87*/break;
                                }
                                else {
                                    ++v;
                                }
                            }
                            /*SL:91*/settingButton.leftClickToggled = false;
                        }
                        /*SL:94*/settingButton.text = settingButton.getSetting().getName() + ": " + settingButton.getSetting().getValue().toString();
                        /*SL:96*/break;
                    }
                    case BOOLEAN: {
                        /*SL:99*/if (settingButton.leftClickToggled) {
                            /*SL:101*/settingButton.getSetting().setValue(!settingButton.getSetting().getValue());
                            /*SL:102*/settingButton.leftClickToggled = false;
                        }
                        /*SL:104*/settingButton.text = settingButton.getSetting().getName() + ": " + settingButton.getSetting().getValue();
                        /*SL:106*/break;
                    }
                    case DOUBLE: {
                        /*SL:109*/if (settingButton.leftClickToggled) {
                            /*SL:111*/settingButton.held = true;
                            /*SL:112*/settingButton.leftClickToggled = false;
                        }
                        /*SL:115*/if (!Mouse.isButtonDown(0)) {
                            settingButton.held = false;
                        }
                        /*SL:117*/if (settingButton.held && GuiNotif.mouseX >= settingButton.x && GuiNotif.mouseX <= settingButton.x + settingButton.width) {
                            /*SL:119*/settingButton.getSetting().setValue(v0.doubleValue() * (settingButton.getSetting().getMax().doubleValue() - settingButton.getSetting().getMin().doubleValue()) + settingButton.getSetting().getMin().doubleValue());
                        }
                        /*SL:122*/settingButton.text = settingButton.getSetting().getName() + ": " + Utils.roundDouble(settingButton.getSetting().getValue().doubleValue(), 2);
                        /*SL:124*/break;
                    }
                    case FLOAT: {
                        /*SL:127*/if (settingButton.leftClickToggled) {
                            /*SL:129*/settingButton.held = true;
                            /*SL:130*/settingButton.leftClickToggled = false;
                        }
                        /*SL:133*/if (!Mouse.isButtonDown(0)) {
                            settingButton.held = false;
                        }
                        /*SL:135*/if (settingButton.held && GuiNotif.mouseX >= settingButton.x && GuiNotif.mouseX <= settingButton.x + settingButton.width) {
                            /*SL:137*/settingButton.getSetting().setValue(v0.floatValue() * (settingButton.getSetting().getMax().floatValue() - settingButton.getSetting().getMin().floatValue()) + settingButton.getSetting().getMin().floatValue());
                        }
                        /*SL:140*/settingButton.text = settingButton.getSetting().getName() + ": " + Utils.roundDouble(settingButton.getSetting().getValue().doubleValue(), 2);
                        /*SL:142*/break;
                    }
                    case INTEGER: {
                        /*SL:145*/if (settingButton.leftClickToggled) {
                            /*SL:147*/settingButton.held = true;
                            /*SL:148*/settingButton.leftClickToggled = false;
                        }
                        /*SL:151*/if (!Mouse.isButtonDown(0)) {
                            settingButton.held = false;
                        }
                        /*SL:153*/if (settingButton.held && GuiNotif.mouseX >= settingButton.x && GuiNotif.mouseX <= settingButton.x + settingButton.width) {
                            final Number v2 = /*EL:155*/v0.doubleValue() * (settingButton.getSetting().getMax().intValue() - settingButton.getSetting().getMin().intValue()) + settingButton.getSetting().getMin().intValue();
                            /*SL:156*/settingButton.getSetting().setValue(v2.intValue());
                        }
                        /*SL:159*/settingButton.text = settingButton.getSetting().getName() + ": " + settingButton.getSetting().getValue().intValue();
                        break;
                    }
                }
                /*SL:166*/settingButton.x = settingButton.getHackButton().x + settingButton.width;
                /*SL:167*/if (button != null) {
                    /*SL:169*/settingButton.y = button.y + button.height;
                }
                else {
                    /*SL:172*/settingButton.y = hackButton.y;
                }
                /*SL:175*/button = settingButton;
            }
        }
    }
}
