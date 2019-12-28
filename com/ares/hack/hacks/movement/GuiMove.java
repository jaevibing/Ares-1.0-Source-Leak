package com.ares.hack.hacks.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.client.movement.IsKeyPressed;
import net.minecraft.client.entity.EntityPlayerSP;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Gui Move", description = "Walk while in guis", category = EnumCategory.MOVEMENT)
public class GuiMove extends BaseHack
{
    public void onLogic() {
        /*SL:16*/if (this.getEnabled() && GuiMove.mc.field_71462_r != null && !(GuiMove.mc.field_71462_r instanceof GuiChat)) {
            /*SL:18*/if (Keyboard.isKeyDown(200)) {
                final EntityPlayerSP field_71439_g = GuiMove.mc.field_71439_g;
                /*SL:19*/field_71439_g.field_70125_A -= 2.0f;
            }
            /*SL:21*/if (Keyboard.isKeyDown(208)) {
                final EntityPlayerSP field_71439_g2 = GuiMove.mc.field_71439_g;
                /*SL:22*/field_71439_g2.field_70125_A += 2.0f;
            }
            /*SL:24*/if (Keyboard.isKeyDown(203)) {
                final EntityPlayerSP field_71439_g3 = GuiMove.mc.field_71439_g;
                /*SL:25*/field_71439_g3.field_70177_z -= 2.0f;
            }
            /*SL:27*/if (Keyboard.isKeyDown(205)) {
                final EntityPlayerSP field_71439_g4 = GuiMove.mc.field_71439_g;
                /*SL:28*/field_71439_g4.field_70177_z += 2.0f;
            }
        }
    }
    
    @SubscribeEvent
    public void isKeyPressed(final IsKeyPressed a1) {
        /*SL:35*/if (this.getEnabled() && !(GuiMove.mc.field_71462_r instanceof GuiChat)) {
            /*SL:37*/a1.initialValue = a1.actual;
        }
    }
}
