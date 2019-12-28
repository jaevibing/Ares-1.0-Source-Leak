package com.ares.hack.hacks.misc;

import com.ares.utils.chat.ChatUtils;
import com.ares.utils.Utils;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import com.ares.hack.hacks.client.Notifications;
import java.util.Collection;
import com.ares.hack.settings.settings.EnumSetting;
import java.util.ArrayList;
import com.ares.utils.ColourUtils;
import com.ares.hack.settings.Setting;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Visual Range", description = "Get notified when someone enters your render distance", category = EnumCategory.MISC)
public class VisualRange extends BaseHack
{
    private List<EntityPlayer> playerEntities;
    private Setting<ColourUtils.Colours> color;
    private Setting<Mode> mode;
    
    public VisualRange() {
        this.playerEntities = new ArrayList<EntityPlayer>();
        this.color = new EnumSetting<ColourUtils.Colours>("Color", this, ColourUtils.Colours.RED);
        this.mode = new EnumSetting<Mode>("Mode", this, Mode.PRIVATE);
    }
    
    public void onLogic() {
        /*SL:51*/if (!this.getEnabled()) {
            return;
        }
        final List<EntityPlayer> list = /*EL:53*/new ArrayList<EntityPlayer>(VisualRange.mc.field_71441_e.field_73010_i);
        /*SL:54*/list.removeAll(this.playerEntities);
        /*SL:56*/for (final EntityPlayer v1 : list) {
            /*SL:58*/if (v1 == null) {
                continue;
            }
            /*SL:60*/if (VisualRange.mc.field_71441_e.field_73010_i.contains(v1)) {
                /*SL:62*/this.sendMessage("PlayerPreviewElement '" + getName(v1) + /*EL:63*/"' entered your render distance at " + vecToStr(v1.func_174791_d()), this.color.getValue().toString());
                Notifications.INSTANCE.visualRangeTrigger(/*EL:66*/v1);
            }
            else {
                /*SL:68*/if (!this.playerEntities.contains(v1)) {
                    continue;
                }
                /*SL:70*/this.sendMessage("PlayerPreviewElement '" + getName(v1) + /*EL:71*/"' left your render distance at " + vecToStr(v1.func_174791_d()), this.color.getValue().toString());
            }
        }
        /*SL:76*/this.playerEntities = (List<EntityPlayer>)VisualRange.mc.field_71441_e.field_73010_i;
    }
    
    private static String vecToStr(final Vec3d a1) {
        /*SL:81*/if (a1 != null) {
            /*SL:83*/return Utils.vectorToString(a1, new boolean[0]);
        }
        /*SL:85*/return "[null]";
    }
    
    private static String getName(final EntityPlayer v1) {
        /*SL:90*/if (v1 != null) {
            final String a1 = /*EL:92*/v1.getDisplayNameString();
            /*SL:94*/if (a1 != null) {
                /*SL:95*/return v1.getDisplayNameString();
            }
        }
        /*SL:97*/return "[null]";
    }
    
    private void sendMessage(final String a1, final String a2) {
        /*SL:102*/if (this.mode.getValue() == Mode.PRIVATE) {
            /*SL:104*/ChatUtils.printMessage(a1, a2);
        }
        else {
            VisualRange.mc.field_71439_g.func_71165_d(/*EL:108*/a1);
        }
    }
    
    enum Mode
    {
        PRIVATE, 
        PUBLIC;
    }
}
